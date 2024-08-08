package com.joheba.hotelbediax.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.joheba.hotelbediax.ui.destinationdetails.DestinationDetailsScreen
import com.joheba.hotelbediax.ui.main.destination.DestinationScreen
import com.joheba.hotelbediax.ui.main.home.HomeScreen
import com.joheba.hotelbediax.ui.newdestination.NewDestinationScreen

@Composable
fun AppNavigation(
    appNavigationViewModel: AppNavigationViewModel = hiltViewModel()
) {
    val navHostController = rememberNavController()
    val appNavigationState by appNavigationViewModel.appNavigationState.collectAsState()

    NavHost(
        navController = navHostController,
        startDestination = MainAppScreens.Home.route
    ) {
        composable(
            route = MainAppScreens.Home.route
        ) {
            HomeScreen(
                selectedNavigationIndex = appNavigationState.selectedNavigationIndex,
                navigateToItemRoute = { route: String ->
                    navigateToBottomNavigationItem(
                        navController = navHostController,
                        route = route
                    )
                },
                updateSelectedNavigationIndex = {newIndex: Int ->
                    appNavigationViewModel.updateSelectedNavigationIndex(newIndex)
                }
            )
        }

        composable(
            route = MainAppScreens.Destinations.route
        ) {
            DestinationScreen(
                selectedNavigationIndex = appNavigationState.selectedNavigationIndex,
                navigateToItemRoute = { route: String ->
                    navigateToBottomNavigationItem(
                        navController = navHostController,
                        route = route
                    )
                },
                updateSelectedNavigationIndex = { newIndex: Int ->
                    appNavigationViewModel.updateSelectedNavigationIndex(newIndex)
                },
                navigateToDestinationDetails = { destinationId: Int ->
                    navHostController.navigate(MainAppScreens.DestinationDetails.buildArgRoute(destinationId))
                }
            )
        }

        composable(
            route = MainAppScreens.DestinationDetails.buildRoute(),
            arguments = listOf(navArgument(NavigationVariableNames.DESTINATION_ID.variableName) {
                type = NavType.IntType
            })
        ) {
            val destinationId = navHostController.currentBackStackEntry?.arguments
                ?.getInt(NavigationVariableNames.DESTINATION_ID.variableName)

            DestinationDetailsScreen(
                navigateBack = {
                    navHostController.popBackStack(
                        route = MainAppScreens.Destinations.route,
                        inclusive = false
                    )
                },
                destinationId = destinationId
            )
        }

        composable(
            route = MainAppScreens.NewDestination.route
        ) {
            NewDestinationScreen(
                navigateBack = {
                    navHostController.popBackStack(
                        route = MainAppScreens.Destinations.route,
                        inclusive = false
                    )
                }
            )
        }
    }
}

enum class NavigationVariableNames(val variableName: String) {
    DESTINATION_ID("destinationId")
}

private fun navigateToBottomNavigationItem(
    navController: NavHostController,
    route: String,
) {
    navController.navigate(route) {
        popUpTo(navController.graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}