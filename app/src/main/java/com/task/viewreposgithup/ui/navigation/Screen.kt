package com.task.viewreposgithup.ui.navigation

/*
 * Navigation screen routes definitions.
 */
import kotlinx.serialization.Serializable

@Serializable
sealed interface Screen {
    @Serializable
    data object RepoList : Screen

    @Serializable
    data class RepoDetails(val repoId: Long) : Screen
}
