package com.joheba.hotelbediax.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

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
            //TODO: implement MainScreen
            // Send also selectedNavigationIndex for its navigationSuiteScaffold
        }

        composable(
            route = MainAppScreens.Destinations.route
        ) {
            //TODO: implement DestinationsScreen
            // Send also selectedNavigationIndex for its navigationSuiteScaffold
        }

        composable(
            route = MainAppScreens.DestinationDetails.route
        ) {
            val destination = navHostController.currentBackStackEntry?.arguments
                ?.getInt(NavigationVariableNames.DESTINATION_ID.variableName)

            //TODO: implement DestinationDetailsScreen
            // Send also its destination
            // Send also selectedNavigationIndex for its navigationSuiteScaffold

        }
    }
}

enum class NavigationVariableNames(val variableName: String) {
    DESTINATION_ID("destinationId")
}