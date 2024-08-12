@file:OptIn(ExperimentalMaterial3Api::class)

package com.joheba.hotelbediax.ui.main.destination

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.joheba.hotelbediax.R
import com.joheba.hotelbediax.domain.core.Destination
import com.joheba.hotelbediax.domain.core.DestinationType
import com.joheba.hotelbediax.ui.common.composables.ColumnItemDivider
import com.joheba.hotelbediax.ui.common.composables.ImageWithMessage
import com.joheba.hotelbediax.ui.common.composables.LoadingIndicator
import com.joheba.hotelbediax.ui.common.composables.destinationfields.CountryCodeField
import com.joheba.hotelbediax.ui.common.composables.destinationfields.CountryCodeSelector
import com.joheba.hotelbediax.ui.common.composables.destinationfields.DescriptionField
import com.joheba.hotelbediax.ui.common.composables.destinationfields.IdField
import com.joheba.hotelbediax.ui.common.composables.destinationfields.LastModifyField
import com.joheba.hotelbediax.ui.common.composables.destinationfields.LastModifyPicker
import com.joheba.hotelbediax.ui.common.composables.destinationfields.NameField
import com.joheba.hotelbediax.ui.common.composables.destinationfields.TypeField
import com.joheba.hotelbediax.ui.common.composables.scaffolds.StandardNavigationSuiteScaffold
import com.joheba.hotelbediax.ui.common.composables.topbar.MainTopAppBar
import com.joheba.hotelbediax.ui.theme.filterNotOkButton
import com.joheba.hotelbediax.ui.theme.filterOkButton
import com.joheba.hotelbediax.ui.theme.filterSecondaryButton
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DestinationScreen(
    selectedNavigationIndex: Int,
    navigateToItemRoute: (String) -> Unit,
    navigateToDestinationDetails: (Int) -> Unit,
    navigateToNewDestination: () -> Unit,
    updateSelectedNavigationIndex: (Int) -> Unit,
    destinationViewModel: DestinationViewModel = hiltViewModel(),
) {

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val scrollState = rememberLazyListState()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val scope = rememberCoroutineScope()

    val state by destinationViewModel.state.collectAsState()
    val dateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG, FormatStyle.SHORT)
    var isBottomSheetOpened by rememberSaveable { mutableStateOf(false) }
    var isCountryCodeSelectorOpened by rememberSaveable { mutableStateOf(false) }
    var isLastModifyPickerOpened by rememberSaveable { mutableStateOf(false) }

    StandardNavigationSuiteScaffold(
        selectedNavigationIndex = selectedNavigationIndex,
        navigateToItemRoute = { route ->
            navigateToItemRoute(route)
            isCountryCodeSelectorOpened = false
        },
        updateSelectedNavigationIndex = updateSelectedNavigationIndex,
        scrollToTop = {
            scope.launch {
                scrollState.scrollToItem(0)
            }
        }
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
                        modifier = Modifier
                            .fillMaxSize(),
                        topBar = {
                            MainTopAppBar(
                                scrollBehavior = scrollBehavior,
                                syncAction = {
                                    destinationViewModel.forceSyncTempOperations()
                                },
                                bottomSheetAction = {
                                    isBottomSheetOpened = true
                                },
                                pendingOperations = state.pendingTempOperationsNumber
                            )
                        },
                        floatingActionButton = {
                            FloatingActionButton(
                                onClick = {
                                    navigateToNewDestination()
                                },
                                containerColor = filterOkButton
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Add,
                                    contentDescription = Icons.Filled.Add.name
                                )
                            }
                        }
                    ) { paddingValues ->
                        when {
                            destinationList.loadState.refresh is LoadState.NotLoading
                                    && !destinationList.loadState.refresh.endOfPaginationReached
                                    && destinationList.loadState.append is LoadState.NotLoading
                                    && destinationList.loadState.append.endOfPaginationReached
                                    && destinationList.itemCount == 0 -> {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                ) {
                                    ImageWithMessage(
                                        modifier = Modifier
                                            .align(Alignment.Center),
                                        imageResId = R.drawable.filter_no_result,
                                        messageResId = R.string.filter_no_result
                                    )
                                }
                            }

                            else -> {
                                LazyColumn(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(paddingValues)
                                        .nestedScroll(scrollBehavior.nestedScrollConnection),
                                    state = scrollState
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
                                        }
                                    }
                                }
                            }

                        }
                        if (isBottomSheetOpened) {
                            ModalBottomSheet(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(paddingValues),
                                sheetState = sheetState,
                                properties = ModalBottomSheetProperties(
                                    shouldDismissOnBackPress = true
                                ),
                                onDismissRequest = {
                                    isBottomSheetOpened = false
                                }
                            ) {
                                BottomSheetContent(
                                    filters = state.filters,
                                    areFiltersApplied = state.areFiltersApplied,
                                    updateId = { newId ->
                                        destinationViewModel.updateFilterId(newId)
                                    },
                                    updateName = { newName ->
                                        destinationViewModel.updateFilterName(newName)
                                    },
                                    updateDescription = { newDescription ->
                                        destinationViewModel.updateFilterDescription(
                                            newDescription
                                        )
                                    },
                                    updateType = { newType ->
                                        destinationViewModel.updateFilterType(newType)
                                    },
                                    updateCountryCode = { newCountryCode ->
                                        destinationViewModel.updateFilterCountryCode(
                                            newCountryCode
                                        )
                                    },
                                    updateLastModify = { newLastModify ->
                                        destinationViewModel.updateFilterLastModify(
                                            newLastModify
                                        )
                                    },
                                    resetAllFilters = {
                                        destinationViewModel.resetFilters()
                                        scope.launch {
                                            sheetState.hide()
                                            isBottomSheetOpened = false
                                        }
                                    },
                                    applyFilters = {
                                        destinationViewModel.applyFilters()
                                        scope.launch {
                                            sheetState.hide()
                                            isBottomSheetOpened = false
                                        }
                                    },
                                    openCountryCodeSelector = {
                                        scope.launch {
                                            sheetState.hide()
                                            isBottomSheetOpened = false
                                            isCountryCodeSelectorOpened = true
                                            keyboardController?.hide()
                                            focusManager.clearFocus()
                                        }
                                    },
                                    openLastModifyPicker = {
                                        isLastModifyPickerOpened = true
                                        keyboardController?.hide()
                                        focusManager.clearFocus()
                                    }
                                )
                            }
                        }
                        if (isCountryCodeSelectorOpened) {
                            CountryCodeSelector(
                                modifier = Modifier
                                    .padding(paddingValues),
                                onDismiss = {
                                    scope.launch {
                                        isCountryCodeSelectorOpened = false
                                        isBottomSheetOpened = true
                                        sheetState.show()
                                    }
                                },
                                updateCountryCode = { newCountryCode ->
                                    destinationViewModel.updateFilterCountryCode(newCountryCode)
                                }
                            )
                        }
                        if (isLastModifyPickerOpened) {
                            LastModifyPicker(
                                modifier = Modifier
                                    .padding(paddingValues)
                                    .align(Alignment.Center),
                                onDateSelected = { newLastModify ->
                                    destinationViewModel.updateFilterLastModify(newLastModify)
                                },
                                onDismiss = {
                                    isLastModifyPickerOpened = false
                                }
                            )
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
                        .padding(
                            top = 12.dp
                        )
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
    modifier: Modifier = Modifier,
    filters: DestinationFilters,
    areFiltersApplied: Boolean,
    openCountryCodeSelector: () -> Unit,
    openLastModifyPicker: () -> Unit,
    updateId: (Int?) -> Unit,
    updateName: (String?) -> Unit,
    updateDescription: (String?) -> Unit,
    updateType: (DestinationType?) -> Unit,
    updateCountryCode: (String?) -> Unit,
    updateLastModify: (LocalDateTime?) -> Unit,
    resetAllFilters: () -> Unit,
    applyFilters: () -> Unit,
) {
    val childModifier = Modifier
        .fillMaxWidth()
        .padding(
            vertical = 12.dp
        )

    val dateTimeFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .padding(12.dp)
        ) {
            BottomSheetHeader(
                modifier = childModifier
            )
            IdField(
                modifier = childModifier,
                value = filters.id,
                onValueChange = updateId
            )
            NameField(
                modifier = childModifier,
                value = filters.name,
                onValueChange = updateName
            )
            DescriptionField(
                modifier = childModifier,
                value = filters.description,
                onValueChange = updateDescription,
            )
            CountryCodeField(
                modifier = childModifier,
                value = filters.countryCode,
                onValueChange = updateCountryCode,
                openCountryCodeSelector = openCountryCodeSelector,
            )
            LastModifyField(
                modifier = childModifier,
                value = filters.lastModify,
                dateTimeFormatter = dateTimeFormatter,
                onValueChange = updateLastModify,
                openLastModifyPicker = openLastModifyPicker
            )
            TypeField(
                modifier = childModifier,
                value = filters.type,
                onValueChange = updateType,
                selectedIcon = Icons.Filled.Remove,
                unSelectedIcon = Icons.Filled.Add,
                selectedFieldColor = filterNotOkButton
            )
        }
        FilterButtons(
            modifier = childModifier,
            resetAllFilters = resetAllFilters,
            applyFilters = applyFilters,
            areFiltersApplied = areFiltersApplied
        )
    }
}

@Composable
private fun BottomSheetHeader(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
    ) {
        Text(
            modifier = Modifier
                .padding(
                    bottom = 6.dp
                ),
            text = stringResource(id = R.string.filter_bottom_sheet_header),
            style = MaterialTheme.typography.titleMedium
        )
        ColumnItemDivider()
    }
}


@Composable
private fun FilterButtons(
    modifier: Modifier = Modifier,
    areFiltersApplied: Boolean,
    resetAllFilters: () -> Unit,
    applyFilters: () -> Unit,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ElevatedButton(
            onClick = { resetAllFilters() },
            enabled = areFiltersApplied,
            colors = ButtonDefaults.elevatedButtonColors(
                containerColor = filterSecondaryButton,
                contentColor = Color.White
            )
        ) {
            Text(
                text = stringResource(id = R.string.reset)
            )
        }
        ElevatedButton(
            onClick = { applyFilters() },
            colors = ButtonDefaults.elevatedButtonColors(
                containerColor = filterOkButton,
                contentColor = Color.White
            )
        ) {
            Text(
                text = stringResource(id = R.string.apply)
            )
        }
    }
}