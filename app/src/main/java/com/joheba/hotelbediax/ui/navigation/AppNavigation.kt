package com.joheba.hotelbediax.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.joheba.hotelbediax.ui.destinationdetails.DestinationDetailsScreen
import com.joheba.hotelbediax.ui.main.destination.DestinationScreen
import com.joheba.hotelbediax.ui.main.home.HomeScreen

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
                }
            )
        }

        composable(
            route = MainAppScreens.DestinationDetails.route
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