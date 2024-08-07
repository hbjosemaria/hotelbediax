package com.joheba.hotelbediax.ui.newdestination

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.joheba.hotelbediax.R
import com.joheba.hotelbediax.ui.common.composables.topbar.SingleScreenTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewDestinationScreen(
    navigateBack: () -> Unit,
    newDestinationViewModel: NewDestinationViewModel = hiltViewModel()
) {

    val state by newDestinationViewModel.state.collectAsState()

    Scaffold(
        topBar = {
            SingleScreenTopAppBar(
                navigateBack = navigateBack,
                screenTitleResId = R.string.destination_new
            )
        }
    ) { paddingValues ->
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            //TODO: implement screen
        }
    }

}