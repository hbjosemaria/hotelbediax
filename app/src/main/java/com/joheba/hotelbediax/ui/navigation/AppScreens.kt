package com.joheba.hotelbediax.ui.navigation

import com.joheba.hotelbediax.R

sealed class MainAppScreens(val route: String, val titleResId: Int){
    data object Home: MainAppScreens("main_home", R.string.home)
    data object Destinations: MainAppScreens("main_destination", R.string.destinations)
    data object DestinationDetails: MainAppScreens("main_destination_detail", R.string.destination_detail)
}