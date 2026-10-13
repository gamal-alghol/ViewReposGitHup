package com.task.viewreposgithup.ui.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.task.viewreposgithup.ui.details.RepoDetailsScreen
import com.task.viewreposgithup.ui.list.RepoListScreen

/*
 * App Navigation Graph using Type-Safe Navigation with Kotlinx Serialization and Clean Architecture.
 */
@Composable
fun NavGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Screen.RepoList,
        enterTransition = { fadeIn(animationSpec = tween(300)) },
        exitTransition = { fadeOut(animationSpec = tween(300)) },
        popEnterTransition = { fadeIn(animationSpec = tween(300)) },
        popExitTransition = { fadeOut(animationSpec = tween(300)) }
    ) {
        composable<Screen.RepoList> {
            RepoListScreen(
                onRepoClick = { repoId ->
                    navController.navigate(Screen.RepoDetails(repoId))
                }
            )
        }

        composable<Screen.RepoDetails> {
            RepoDetailsScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}
