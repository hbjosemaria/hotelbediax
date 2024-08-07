package com.joheba.hotelbediax.ui.main.destination

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Landscape
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.joheba.hotelbediax.R
import com.joheba.hotelbediax.domain.core.Destination
import com.joheba.hotelbediax.domain.core.DestinationType
import com.joheba.hotelbediax.ui.common.composables.ImageWithMessage
import com.joheba.hotelbediax.ui.common.composables.LoadingIndicator
import com.joheba.hotelbediax.ui.common.composables.scaffolds.StandardNavigationSuiteScaffold
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Locale

@Composable
fun DestinationScreen(
    selectedNavigationIndex: Int,
    navigateToItemRoute: (String) -> Unit,
    navigateToDestinationDetails: (Int) -> Unit,
    updateSelectedNavigationIndex: (Int) -> Unit,
    destinationViewModel: DestinationViewModel = hiltViewModel(),
) {

    val state by destinationViewModel.state.collectAsState()

    StandardNavigationSuiteScaffold(
        selectedNavigationIndex = selectedNavigationIndex,
        navigateToItemRoute = navigateToItemRoute,
        updateSelectedNavigationIndex = updateSelectedNavigationIndex
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(16.dp)
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
                    Log.i("listInfo:", destinationList.itemSnapshotList.toString())
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        items(
                            count = destinationList.itemCount,
                            key = destinationList.itemKey { destination ->
                                destination.id
                            }
                        ) { index ->
                            destinationList[index]?.let { destination ->
                                DestinationItem(
                                    destination = destination,
                                    navigateToDestinationDetails = {
                                        navigateToDestinationDetails(destination.id)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DestinationItem(
    destination: Destination,
    navigateToDestinationDetails: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                navigateToDestinationDetails()
            }
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
        ) {
            Icon(
                modifier = Modifier
                    .align(Alignment.Center),
                imageVector = if (destination.type == DestinationType.CITY) Icons.Filled.LocationCity else Icons.Filled.Flag,
                contentDescription = if (destination.type == DestinationType.CITY) DestinationType.CITY.name else DestinationType.COUNTRY.name
            )
        }
        Column(
            modifier = Modifier
                .weight(9f)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Column(

                ) {
                    //Name
                }
                Row(

                ) {
                    //CountryCode
                    //Type
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                //LastModification
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                //Description
            }

        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, showSystemUi = true)
@Composable
private fun DestinationItemPrev() {
    DestinationItem(
        Destination(
            id = 10,
            name = "Madrid",
            description = "Description of Madrid",
            countryCode = "es",
            type = DestinationType.CITY,
            lastModify = LocalDateTime.now()
        ),
        {}
    )
}