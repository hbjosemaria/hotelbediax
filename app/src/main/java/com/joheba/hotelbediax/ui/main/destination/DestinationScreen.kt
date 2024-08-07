package com.joheba.hotelbediax.ui.main.destination

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.joheba.hotelbediax.R
import com.joheba.hotelbediax.ui.common.composables.ImageWithMessage
import com.joheba.hotelbediax.ui.common.composables.LoadingIndicator
import com.joheba.hotelbediax.ui.common.composables.scaffolds.StandardNavigationSuiteScaffold

@Composable
fun DestinationScreen(
    selectedNavigationIndex: Int,
    navigateToItemRoute: (String) -> Unit,
    destinationViewModel: DestinationViewModel = hiltViewModel(),
) {

    val state by destinationViewModel.state.collectAsState()

    StandardNavigationSuiteScaffold(
        selectedNavigationIndex = selectedNavigationIndex,
        navigateToItemRoute = navigateToItemRoute
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            when (val result = state.result) {
                DestinationStateResult.Loading -> {
                    LoadingIndicator(
                        modifier = Modifier
                            .align(Alignment.Center)
                    )
                }

                is DestinationStateResult.Error -> {
                    ImageWithMessage(
                        modifier = Modifier
                            .align(Alignment.Center),
                        imageResId = R.drawable.error,
                        messageResId = R.string.destinations_error_message
                    )
                }

                is DestinationStateResult.Success -> {
                    val destinationList = result.destinationList.collectAsLazyPagingItems()
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        items(
                            count = destinationList.itemCount,
                            key = destinationList.itemKey { destination ->
                                destination.id
                            }
                        ) { destination ->
                            //TODO: implement success state
                        }
                    }
                }
            }
        }
    }
}