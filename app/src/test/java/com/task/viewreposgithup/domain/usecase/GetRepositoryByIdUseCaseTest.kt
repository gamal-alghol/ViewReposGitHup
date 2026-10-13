package com.task.viewreposgithup.domain.usecase

import com.task.viewreposgithup.domain.model.Repo
import com.task.viewreposgithup.domain.repository.RepoRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

/*
 * Unit test for GetRepositoryByIdUseCase.
 */
class GetRepositoryByIdUseCaseTest {

    private val repository: RepoRepository = mockk()
    private val useCase = GetRepositoryByIdUseCase(repository)

    @Test
    fun `invoke should return repository from repository flow`() = runTest {
        // Given
        val expectedRepo = Repo(
            id = 123L,
            name = "retrofit",
            stars = 42000,
            description = "A type-safe HTTP client for Android and Java",
            language = "Kotlin",
            isBookmarked = false
        )
        coEvery { repository.getRepositoryById(123L) } returns flowOf(expectedRepo)

        // When
        val result = useCase(123L).first()

        // Then
        assertNotNull(result)
        assertEquals(expectedRepo.id, result?.id)
        assertEquals("retrofit", result?.name)
        assertEquals(42000, result?.stars)
    }
}
