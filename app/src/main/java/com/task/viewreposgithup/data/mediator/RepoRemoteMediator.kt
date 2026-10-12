package com.task.viewreposgithup.data.mediator

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.task.viewreposgithup.data.local.AppDatabase
import com.task.viewreposgithup.data.local.entity.RemoteKeysEntity
import com.task.viewreposgithup.data.local.entity.RepoEntity
import com.task.viewreposgithup.data.remote.api.GithubApiService
import com.task.viewreposgithup.util.AppConstants
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class RepoRemoteMediator(
    private val apiService: GithubApiService,
    private val database: AppDatabase,
    private val org: String = AppConstants.DEFAULT_ORG
) : RemoteMediator<Int, RepoEntity>() {

    private val repoDao = database.repoDao()
    private val remoteKeysDao = database.remoteKeysDao()

    override suspend fun initialize(): InitializeAction {
        val localCount = repoDao.getCount()

        return if (localCount == 0) {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        } else {
            InitializeAction.SKIP_INITIAL_REFRESH
        }
    }
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, RepoEntity>
    ): MediatorResult {
        Log.d(
            "RepoRemoteMediator",
            "loadType=$loadType, " +
                    "pages=${state.pages.size}, " +
                    "items=${state.pages.sumOf { it.data.size }}, " +
                    "firstItem=${state.pages.firstOrNull()?.data?.firstOrNull()?.id}, " +
                    "lastItem=${state.pages.lastOrNull()?.data?.lastOrNull()?.id}"
        )
        return try {
            val page = when (loadType) {
                LoadType.REFRESH -> 1
                LoadType.PREPEND -> {
                    val remoteKeys = getRemoteKeyForFirstItem(state)

                    val prevKey = remoteKeys?.prevPage
                        ?: return MediatorResult.Success(
                            endOfPaginationReached = true
                        )

                    prevKey
                }
                LoadType.APPEND -> {
                    val remoteKey = getRemoteKeyForLastItem(state)

                    Log.d(
                        "RepoRemoteMediator",
                        "APPEND: stateItems=${state.pages.sumOf { it.data.size }}, " +
                                "lastItem=${state.pages.lastOrNull()?.data?.lastOrNull()?.id}, " +
                                "remoteKey=$remoteKey"
                    )

                    if (state.pages.none { it.data.isNotEmpty() }) {
                        Log.d(
                            "RepoRemoteMediator",
                            "APPEND skipped: PagingState has no loaded items"
                        )

                        return MediatorResult.Success(
                            endOfPaginationReached = false
                        )
                    }

                    val nextKey = remoteKey?.nextPage
                        ?: return MediatorResult.Success(
                            endOfPaginationReached = true
                        )

                    nextKey
                }
            }
            android.util.Log.d(
                "RepoRemoteMediator",
                "Requesting page=$page, loadType=$loadType"
            )
            val response = apiService.getOrgRepositories(
                org = org,
                page = page,
                perPage = state.config.pageSize
            )

            val endOfPaginationReached = response.isEmpty() || response.size < state.config.pageSize

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    remoteKeysDao.clearRemoteKeys()
                }

                val prevKey = if (page == 1) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1

                val keys = response.map { dto ->
                    RemoteKeysEntity(repoId = dto.id, prevPage = prevKey, nextPage = nextKey)
                }

                val repoEntities = response.map { dto ->
                    val existingRepo = repoDao.getRepositoryByIdSync(dto.id)
                    RepoEntity(
                        id = dto.id,
                        name = dto.name,
                        stargazersCount = dto.stargazersCount,
                        description = dto.description,
                        language = dto.language,
                        isBookmarked = existingRepo?.isBookmarked ?: false,
                        htmlUrl = dto.htmlUrl,
                        ownerName = dto.owner?.login ?: org,
                        ownerAvatarUrl = dto.owner?.avatarUrl,
                        updatedAt = dto.updatedAt
                    )
                }

                remoteKeysDao.insertAll(keys)
                repoDao.insertAll(repoEntities)
            }
            android.util.Log.d(
                "RepoRemoteMediator",
                "After insert: localCount=${repoDao.getCount()}"
            )

            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: IOException) {
            MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            if (exception.code() == 403 || exception.code() == 429) {
                val localCount = repoDao.getCount()
                if (localCount > 0 && loadType != LoadType.REFRESH) {
                    MediatorResult.Success(endOfPaginationReached = true)
                } else {
                    MediatorResult.Error(
                        Exception("GitHub API rate limit exceeded. Please ensure your Authorization Token is set.")
                    )
                }
            } else {
                MediatorResult.Error(exception)
            }
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, RepoEntity>): RemoteKeysEntity? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { repo ->
            remoteKeysDao.getRemoteKeysRepoId(repo.id)
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, RepoEntity>): RemoteKeysEntity? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { repo ->
            remoteKeysDao.getRemoteKeysRepoId(repo.id)
        }
    }
}