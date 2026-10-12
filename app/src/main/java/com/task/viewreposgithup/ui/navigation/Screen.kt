package com.task.viewreposgithup.ui.navigation

/**
 * Navigation screen routes definitions.
 */
sealed class Screen(val route: String) {
    data object RepoList : Screen("repo_list")
    data object RepoDetails : Screen("repo_details/{repoId}") {
        fun createRoute(repoId: Long): String = "repo_details/$repoId"
    }
}
