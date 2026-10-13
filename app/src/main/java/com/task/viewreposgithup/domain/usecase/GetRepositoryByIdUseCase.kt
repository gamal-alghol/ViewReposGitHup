package com.task.viewreposgithup.domain.usecase

import com.task.viewreposgithup.domain.model.Repo
import com.task.viewreposgithup.domain.repository.RepoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/*
 * UseCase to reactively observe repository details by repoId.
 */
class GetRepositoryByIdUseCase @Inject constructor(
    private val repository: RepoRepository
) {
    /*
     * Invokes the use case to observe a single repository reactively by ID.
     */
    operator fun invoke(repoId: Long): Flow<Repo?> {
        return repository.getRepositoryById(repoId)
    }
}
