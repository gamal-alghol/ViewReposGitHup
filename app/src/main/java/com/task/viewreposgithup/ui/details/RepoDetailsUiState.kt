package com.task.viewreposgithup.ui.details

import com.task.viewreposgithup.domain.model.Repo

/*
 * UI State for Repository Details Screen.
 */
sealed interface RepoDetailsUiState {
    data object Loading : RepoDetailsUiState
    data class Success(val repo: Repo) : RepoDetailsUiState
    data class Error(val message: String) : RepoDetailsUiState
}
