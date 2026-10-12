package com.task.viewreposgithup.ui.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.task.viewreposgithup.domain.usecase.GetRepositoryByIdUseCase
import com.task.viewreposgithup.domain.usecase.ToggleBookmarkUseCase
import com.task.viewreposgithup.util.AppConstants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for Repository Details Screen.
 * Observes repository reactively from Room DB by repoId (NOT passed via bundle).
 */
@HiltViewModel
class RepoDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getRepositoryByIdUseCase: GetRepositoryByIdUseCase,
    private val toggleBookmarkUseCase: ToggleBookmarkUseCase
) : ViewModel() {

    private val repoId: Long = checkNotNull(savedStateHandle["repoId"])

    /**
     * Reactive StateFlow of RepoDetailsUiState observing Room Database changes.
     */
    val uiState: StateFlow<RepoDetailsUiState> = getRepositoryByIdUseCase(repoId)
        .map { repo ->
            if (repo != null) {
                RepoDetailsUiState.Success(repo)
            } else {
                RepoDetailsUiState.Error("Repository not found")
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(AppConstants.FLOW_TIMEOUT_MILLIS),
            initialValue = RepoDetailsUiState.Loading
        )

    fun toggleBookmark() {
        val currentState = uiState.value
        if (currentState is RepoDetailsUiState.Success) {
            viewModelScope.launch {
                toggleBookmarkUseCase(
                    repoId = repoId,
                    isBookmarked = !currentState.repo.isBookmarked
                )
            }
        }
    }
}
