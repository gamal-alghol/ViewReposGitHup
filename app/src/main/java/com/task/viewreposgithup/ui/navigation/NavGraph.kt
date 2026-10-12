package com.task.viewreposgithup.ui.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.task.viewreposgithup.ui.details.RepoDetailsScreen
import com.task.viewreposgithup.ui.list.RepoListScreen

/**
 * App Navigation Graph connecting Repositories List Screen and Details Screen with smooth, optimized transitions.
 */
@Composable
fun NavGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Screen.RepoList.route,
        enterTransition = { fadeIn(animationSpec = tween(200)) },
        exitTransition = { fadeOut(animationSpec = tween(200)) },
        popEnterTransition = { fadeIn(animationSpec = tween(200)) },
        popExitTransition = { fadeOut(animationSpec = tween(200)) }
    ) {
        composable(route = Screen.RepoList.route) {
            RepoListScreen(
                onRepoClick = { repoId ->
                    navController.navigate(Screen.RepoDetails.createRoute(repoId))
                }
            )
        }

        composable(
            route = Screen.RepoDetails.route,
            arguments = listOf(
                navArgument("repoId") {
                    type = NavType.LongType
                }
            )
        ) {
            RepoDetailsScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}
