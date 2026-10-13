package com.task.viewreposgithup.domain.usecase

import com.task.viewreposgithup.domain.repository.RepoRepository
import javax.inject.Inject

/*
 * UseCase to toggle bookmark status for a given repository.
 */
class ToggleBookmarkUseCase @Inject constructor(
    private val repository: RepoRepository
) {
    /*
     * Invokes the use case to toggle bookmark state for a repository.
     */
    suspend operator fun invoke(repoId: Long, isBookmarked: Boolean) {
        repository.toggleBookmark(repoId, isBookmarked)
    }
}
