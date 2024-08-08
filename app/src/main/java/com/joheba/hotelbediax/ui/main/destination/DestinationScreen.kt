package com.joheba.hotelbediax.ui.main.destination

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.joheba.hotelbediax.R
import com.joheba.hotelbediax.domain.core.Destination
import com.joheba.hotelbediax.ui.common.composables.ColumnItemDivider
import com.joheba.hotelbediax.ui.common.composables.ImageWithMessage
import com.joheba.hotelbediax.ui.common.composables.LoadingIndicator
import com.joheba.hotelbediax.ui.common.composables.scaffolds.StandardNavigationSuiteScaffold
import com.joheba.hotelbediax.ui.common.composables.topbar.MainTopAppBar
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DestinationScreen(
    selectedNavigationIndex: Int,
    navigateToItemRoute: (String) -> Unit,
    navigateToDestinationDetails: (Int) -> Unit,
    updateSelectedNavigationIndex: (Int) -> Unit,
    destinationViewModel: DestinationViewModel = hiltViewModel(),
) {

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)

    val state by destinationViewModel.state.collectAsState()
    val dateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG, FormatStyle.SHORT)
    var isBottomSheetOpened by rememberSaveable { mutableStateOf(false) }

    StandardNavigationSuiteScaffold(
        selectedNavigationIndex = selectedNavigationIndex,
        navigateToItemRoute = navigateToItemRoute,
        updateSelectedNavigationIndex = updateSelectedNavigationIndex
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

                    Scaffold(
                        topBar = {
                            MainTopAppBar(
                                scrollBehavior = scrollBehavior,
                                syncAction = {
                                    //TODO: Add worker sync action
                                },
                                bottomSheetAction = {
                                    isBottomSheetOpened = true
                                }
                            )
                        }
                    ) { paddingValues ->
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(paddingValues)
                                .nestedScroll(scrollBehavior.nestedScrollConnection)
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
                                        },
                                        dateTimeFormatter = dateTimeFormatter
                                    )
                                } //TODO: add placeholder in case destination is null
                            }
                        }

                        if (isBottomSheetOpened) {
                            ModalBottomSheet(
                                modifier = Modifier
                                    .fillMaxSize(),
                                sheetState = sheetState,
                                properties = ModalBottomSheetProperties(
                                    shouldDismissOnBackPress = true
                                ),
                                onDismissRequest = { isBottomSheetOpened = false }
                            ) {
                                BottomSheetContent()
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
    dateTimeFormatter: DateTimeFormatter,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                navigateToDestinationDetails()
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier
                            .weight(1f),
                        text = destination.name,
                        style = MaterialTheme.typography.titleSmall
                    )
                    Text(
                        textAlign = TextAlign.End,
                        text = stringResource(
                            R.string.country_code,
                            destination.countryCode.uppercase()
                        ),
                        style = MaterialTheme.typography.labelSmall
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        modifier = Modifier
                            .weight(1f),
                        textAlign = TextAlign.Start,
                        text = stringResource(
                            R.string.last_modify,
                            destination.lastModify.format(dateTimeFormatter)
                        ),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.ExtraLight,
                        fontStyle = FontStyle.Italic
                    )
                    Text(
                        text = stringResource(
                            R.string.type,
                            destination.type.name.lowercase().capitalize(Locale.current)
                        ),
                        style = MaterialTheme.typography.labelSmall
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth(),
                        text = destination.description,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Light
                    )
                }
            }
        }
        ColumnItemDivider()
    }
}

@Composable
private fun BottomSheetContent(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        IdFilter()
        NameFilter()
        DescriptionFilter()
        TypeFilter()
        CountryCodeFilter()
        LastModifyFilter()
    }
}

@Composable
private fun IdFilter(
    modifier: Modifier = Modifier
) {
    //TODO: implement filter
}

@Composable
private fun NameFilter(
    modifier: Modifier = Modifier
) {
    //TODO: implement filter
}

@Composable
private fun DescriptionFilter(
    modifier: Modifier = Modifier
) {
    //TODO: implement filter
}

@Composable
private fun CountryCodeFilter(
    modifier: Modifier = Modifier
) {
    //TODO: implement filter
}

@Composable
private fun TypeFilter(
    modifier: Modifier = Modifier
) {
    //TODO: implement filter
}

@Composable
private fun LastModifyFilter(
    modifier: Modifier = Modifier
) {
    //TODO: implement filter
}

@Composable
private fun FilterButtons(
    modifier: Modifier = Modifier
) {

    //TODO: RESET BUTTON
    //TODO: FILTER BUTTON

}


@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun BottomSheetContentPrev() {
    BottomSheetContent()
}