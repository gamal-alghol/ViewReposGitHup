package com.task.viewreposgithup.domain.usecase

import androidx.paging.PagingData
import com.task.viewreposgithup.domain.model.Repo
import com.task.viewreposgithup.domain.repository.RepoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * UseCase to fetch paginated list of repositories with filtering and sorting support.
 */
class GetRepositoriesUseCase @Inject constructor(
    private val repository: RepoRepository
) {
    operator fun invoke(
        language: String? = null,
        searchQuery: String = "",
        sortBy: String = "NAME"
    ): Flow<PagingData<Repo>> {
        return repository.getRepositoriesPaged(
            language = language,
            searchQuery = searchQuery,
            sortBy = sortBy
        )
    }
}
