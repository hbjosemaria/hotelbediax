package com.joheba.hotelbediax.ui.destinationdetails

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.joheba.hotelbediax.R
import com.joheba.hotelbediax.ui.common.composables.ImageWithMessage
import com.joheba.hotelbediax.ui.common.composables.LoadingIndicator
import com.joheba.hotelbediax.ui.common.composables.topbar.SingleScreenTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DestinationDetailsScreen(
    navigateBack: () -> Unit,
    destinationId: Int?,
    destinationDetailsViewModel: DestinationDetailsViewModel = hiltViewModel<DestinationDetailsViewModel, DestinationDetailsViewModel.DestinationDetailsViewModelFactory>(
        creationCallback = { factory ->
            factory.create(destinationId)
        }
    )
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val state by destinationDetailsViewModel.state.collectAsState()

    Scaffold(
        topBar = {
            SingleScreenTopAppBar(
                scrollBehavior = scrollBehavior,
                navigateBack = navigateBack,
                screenTitleResId = R.string.destination_detail)
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val result = state.result) {
                DestinationDetailsStateResult.Loading -> {
                    LoadingIndicator(
                        modifier = Modifier
                            .align(Alignment.Center)
                    )
                }
                is DestinationDetailsStateResult.Error -> {
                    ImageWithMessage(
                        modifier = Modifier
                            .align(Alignment.Center),
                        imageResId = R.drawable.error,
                        messageResId = R.string.destination_detail_error_message
                    )
                }
                is DestinationDetailsStateResult.Success -> {
                    val destination = result.destination
                    //TODO: implement success state
                }
            }
        }
    }

}