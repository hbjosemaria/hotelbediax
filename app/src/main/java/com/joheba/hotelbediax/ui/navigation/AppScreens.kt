package com.joheba.hotelbediax.ui.navigation

import com.joheba.hotelbediax.R

sealed class MainAppScreens(val route: String, val titleResId: Int){
    data object Home: MainAppScreens("main_home", R.string.home)
    data object Destinations: MainAppScreens("main_destination", R.string.destinations)
    data object DestinationDetails: MainAppScreens("main_destination_detail", R.string.destination_detail) {
        fun buildArgRoute(value: Int): String {
            return "$route/${value}"
        }

        fun buildRoute(): String {
            return "$route/{${NavigationVariableNames.DESTINATION_ID.variableName}}"
        }
    }
    data object NewDestination: MainAppScreens("main_destination_new", R.string.destination_new)
}

