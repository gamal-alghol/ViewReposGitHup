package com.task.viewreposgithup.ui.details

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.task.viewreposgithup.domain.model.Repo
import com.task.viewreposgithup.domain.usecase.GetRepositoryByIdUseCase
import com.task.viewreposgithup.domain.usecase.ToggleBookmarkUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/*
 * Unit test for RepoDetailsViewModel.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class RepoDetailsViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val getRepositoryByIdUseCase: GetRepositoryByIdUseCase = mockk()
    private val toggleBookmarkUseCase: ToggleBookmarkUseCase = mockk()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `uiState should emit Success when repository is found in DB`() = runTest {
        // Given
        val repoId = 100L
        val repo = Repo(
            id = repoId,
            name = "picasso",
            stars = 18000,
            description = "A powerful image downloading and caching library",
            language = "Java",
            isBookmarked = false
        )
        val savedStateHandle = SavedStateHandle(mapOf("repoId" to repoId))
        coEvery { getRepositoryByIdUseCase(repoId) } returns flowOf(repo)

        // When
        val viewModel = RepoDetailsViewModel(
            savedStateHandle = savedStateHandle,
            getRepositoryByIdUseCase = getRepositoryByIdUseCase,
            toggleBookmarkUseCase = toggleBookmarkUseCase
        )

        // Then
        viewModel.uiState.test {
            val item = awaitItem()
            val finalItem = if (item is RepoDetailsUiState.Loading) awaitItem() else item
            assertTrue(finalItem is RepoDetailsUiState.Success)
            assertEquals("picasso", (finalItem as RepoDetailsUiState.Success).repo.name)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `toggleBookmark should invoke toggleBookmarkUseCase with negated bookmark status`() = runTest {
        // Given
        val repoId = 100L
        val repo = Repo(
            id = repoId,
            name = "picasso",
            stars = 18000,
            description = "A powerful image downloading and caching library",
            language = "Java",
            isBookmarked = false
        )
        val savedStateHandle = SavedStateHandle(mapOf("repoId" to repoId))
        coEvery { getRepositoryByIdUseCase(repoId) } returns flowOf(repo)
        coEvery { toggleBookmarkUseCase(repoId, true) } just runs

        val viewModel = RepoDetailsViewModel(
            savedStateHandle = savedStateHandle,
            getRepositoryByIdUseCase = getRepositoryByIdUseCase,
            toggleBookmarkUseCase = toggleBookmarkUseCase
        )

        // Start collecting uiState so stateIn becomes active and updates uiState.value
        val collectJob = backgroundScope.launch {
            viewModel.uiState.collect()
        }

        testScheduler.advanceUntilIdle()

        // When
        viewModel.toggleBookmark()
        testScheduler.advanceUntilIdle()

        // Then
        coVerify(exactly = 1) { toggleBookmarkUseCase(repoId, true) }
        collectJob.cancel()
    }
}
