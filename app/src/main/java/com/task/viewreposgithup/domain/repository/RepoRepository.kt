package com.task.viewreposgithup.domain.repository

import androidx.paging.PagingData
import com.task.viewreposgithup.domain.model.Repo
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for GitHub repositories operations.
 * Exposes paged repositories and single repository observation reactively.
 */
interface RepoRepository {
    /**
     * Retrieves paged repositories from local DB, hydrated by network via RemoteMediator,
     * supporting language filtering, search query, and sorting.
     */
    fun getRepositoriesPaged(
        language: String? = null,
        searchQuery: String = "",
        sortBy: String = "NAME"
    ): Flow<PagingData<Repo>>

    /**
     * Reactively fetches a single repository by its unique ID.
     */
    fun getRepositoryById(repoId: Long): Flow<Repo?>

    /**
     * Toggles bookmark state for a specified repository.
     */
    suspend fun toggleBookmark(repoId: Long, isBookmarked: Boolean)
}
