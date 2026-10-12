package com.task.viewreposgithup.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.task.viewreposgithup.data.local.AppDatabase
import com.task.viewreposgithup.data.mediator.RepoRemoteMediator
import com.task.viewreposgithup.data.remote.api.GithubApiService
import com.task.viewreposgithup.domain.model.Repo
import com.task.viewreposgithup.domain.repository.RepoRepository
import com.task.viewreposgithup.util.AppConstants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of RepoRepository using Paging 3 with RemoteMediator and Room Database.
 */
@Singleton
class RepoRepositoryImpl @Inject constructor(
    private val apiService: GithubApiService,
    private val database: AppDatabase
) : RepoRepository {

    private val repoDao = database.repoDao()

    @OptIn(ExperimentalPagingApi::class)
    override fun getRepositoriesPaged(
        language: String?,
        searchQuery: String,
        sortBy: String
    ): Flow<PagingData<Repo>> {
        val hasActiveFilter = !language.isNullOrBlank() || searchQuery.isNotBlank()

        return Pager(
            config = PagingConfig(
                pageSize = AppConstants.PAGE_SIZE,
                prefetchDistance = AppConstants.PREFETCH_DISTANCE,
                initialLoadSize = AppConstants.INITIAL_LOAD_SIZE,
                enablePlaceholders = false
            ),
            remoteMediator = if (hasActiveFilter) null else RepoRemoteMediator(
                apiService = apiService,
                database = database
            ),
            pagingSourceFactory = {
                repoDao.getFilteredRepositories(
                    language = language,
                    searchQuery = searchQuery,
                    sortBy = sortBy
                )
            }
        ).flow.map { pagingData ->
            pagingData.map { it.toDomain() }
        }
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun getRepositoryById(repoId: Long): Flow<Repo?> {
        return repoDao.getRepositoryById(repoId).map { entity ->
            entity?.toDomain()
        }
    }

    override suspend fun toggleBookmark(repoId: Long, isBookmarked: Boolean) {
        repoDao.updateBookmarkState(repoId, isBookmarked)
    }
}
