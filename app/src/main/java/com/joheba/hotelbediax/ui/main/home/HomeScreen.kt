package com.joheba.hotelbediax.ui.main.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.joheba.hotelbediax.R
import com.joheba.hotelbediax.ui.common.composables.ImageWithMessage
import com.joheba.hotelbediax.ui.common.composables.LoadingIndicator
import com.joheba.hotelbediax.ui.common.composables.scaffolds.StandardNavigationSuiteScaffold

@Composable
fun HomeScreen(
    selectedNavigationIndex: Int,
    navigateToItemRoute: (String) -> Unit,
    updateSelectedNavigationIndex: (Int) -> Unit,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val state by homeViewModel.state.collectAsState()

    StandardNavigationSuiteScaffold(
        selectedNavigationIndex = selectedNavigationIndex,
        navigateToItemRoute = navigateToItemRoute,
        updateSelectedNavigationIndex = updateSelectedNavigationIndex
    ) {
        Box(
            modifier = Modifier
                .systemBarsPadding()
                .padding(16.dp)
                .fillMaxSize()
        ) {
            when (state.result) {
                HomeStateResult.Loading -> {
                    //As Home won't be needed in this test, this will always have a Loading state
                    ImageWithMessage(
                        modifier = Modifier
                            .align(Alignment.Center),
                        imageResId = R.drawable.home,
                        messageResId = R.string.home_message
                    )
                }
                HomeStateResult.Error -> {
                    //Manage error state here if corresponds
                }
                HomeStateResult.Success -> {
                    //Manage success state here here if corresponds
                }
            }
        }
    }
}