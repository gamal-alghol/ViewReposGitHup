package com.task.viewreposgithup.domain.usecase

import com.task.viewreposgithup.domain.repository.RepoRepository
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.test.runTest
import org.junit.Test

/*
 * Unit test for ToggleBookmarkUseCase.
 */
class ToggleBookmarkUseCaseTest {

    private val repository: RepoRepository = mockk()
    private val useCase = ToggleBookmarkUseCase(repository)

    @Test
    fun `invoke should call toggleBookmark on repository with correct parameters`() = runTest {
        // Given
        val repoId = 456L
        val isBookmarked = true
        coVerify(exactly = 0) { repository.toggleBookmark(any(), any()) }
        io.mockk.coEvery { repository.toggleBookmark(repoId, isBookmarked) } just runs

        // When
        useCase(repoId, isBookmarked)

        // Then
        coVerify(exactly = 1) { repository.toggleBookmark(repoId, isBookmarked) }
    }
}
