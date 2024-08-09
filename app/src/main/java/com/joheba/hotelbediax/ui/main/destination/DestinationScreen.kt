@file:OptIn(ExperimentalMaterial3Api::class)

package com.joheba.hotelbediax.ui.main.destination

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
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
import com.joheba.hotelbediax.ui.common.composables.scaffolds.StandardNavigationSuiteScaffold
import com.joheba.hotelbediax.ui.common.composables.topbar.MainTopAppBar
import com.joheba.hotelbediax.ui.theme.filterNotOkButton
import com.joheba.hotelbediax.ui.theme.filterOkButton
import com.joheba.hotelbediax.ui.theme.filterSecondaryButton
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
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
    val scrollState = rememberLazyListState()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    val state by destinationViewModel.state.collectAsState()
    val dateTimeFormatter =
        DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG, FormatStyle.SHORT)
    var isBottomSheetOpened by rememberSaveable { mutableStateOf(false) }
    var openedCountryCodeSelector by rememberSaveable { mutableStateOf(false) }
    var openedLastModifyPicker by rememberSaveable { mutableStateOf(false) }

    StandardNavigationSuiteScaffold(
        selectedNavigationIndex = selectedNavigationIndex,
        navigateToItemRoute = { route ->
            navigateToItemRoute(route)
            openedCountryCodeSelector = false
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
                                    //TODO: Add worker sync action
                                },
                                bottomSheetAction = {
                                    isBottomSheetOpened = true
                                }
                            )
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
                                    .fillMaxSize(),
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
                                    dateTimeFormatter = dateTimeFormatter,
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
                                            openedCountryCodeSelector = true
                                        }
                                    },
                                    openLastModifyPicker = {
                                        openedLastModifyPicker = true
                                    }
                                )
                            }
                        }
                        if (openedCountryCodeSelector) {
                            CountryCodeSelector(
                                modifier = Modifier
                                    .padding(paddingValues),
                                dismissCountryCodeSelector = {
                                    scope.launch {
                                        openedCountryCodeSelector = false
                                        isBottomSheetOpened = true
                                        sheetState.show()
                                    }
                                },
                                updateCountryCode = { newCountryCode ->
                                    destinationViewModel.updateFilterCountryCode(newCountryCode)
                                }
                            )
                        }
                        if (openedLastModifyPicker) {
                            LastModifyPicker(
                                modifier = Modifier
                                    .padding(paddingValues),
                                onDateSelected = { newLastModify ->
                                    destinationViewModel.updateFilterLastModify(newLastModify)
                                },
                                onDismiss = {
                                    openedLastModifyPicker = false
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
    dateTimeFormatter: DateTimeFormatter,
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
            IdFilter(
                modifier = childModifier,
                value = filters.id,
                onValueChange = updateId
            )
            NameFilter(
                modifier = childModifier,
                value = filters.name,
                onValueChange = updateName
            )
            DescriptionFilter(
                modifier = childModifier,
                value = filters.description,
                onValueChange = updateDescription,
            )
            CountryCodeFilter(
                modifier = childModifier,
                value = filters.countryCode,
                onValueChange = updateCountryCode,
                openCountryCodeSelector = openCountryCodeSelector,
            )
            LastModifyFilter(
                modifier = childModifier,
                value = filters.lastModify,
                dateTimeFormatter = dateTimeFormatter,
                onValueChange = updateLastModify,
                openLastModifyPicker = openLastModifyPicker
            )
            TypeFilter(
                modifier = childModifier,
                value = filters.type,
                onValueChange = updateType
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
private fun IdFilter(
    modifier: Modifier = Modifier,
    value: Int?,
    onValueChange: (Int?) -> Unit,
) {
    Row(
        modifier = modifier
    ) {
        TextField(
            modifier = Modifier
                .fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            ),
            value = value?.toString() ?: "",
            onValueChange = { newValue ->
                try {
                    if (newValue.isNotBlank()) {
                        onValueChange(newValue.toInt())
                    } else onValueChange(null)
                } catch (e: NumberFormatException) {
                    //Prevent value from being updated
                }
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Numbers,
                    contentDescription = Icons.Filled.Numbers.name
                )
            },
            label = {
                Text(
                    text = stringResource(id = R.string.filter_id)
                )
            },
            placeholder = {
                Text(
                    text = stringResource(id = R.string.filter_id_placeholder)
                )
            },
            trailingIcon = {
                value?.let {
                    IconButton(
                        onClick = {
                            onValueChange(null)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = Icons.Filled.Close.name
                        )
                    }
                }
            }
        )
    }
}

@Composable
private fun NameFilter(
    modifier: Modifier = Modifier,
    value: String?,
    onValueChange: (String?) -> Unit,
) {
    Row(
        modifier = modifier
    ) {
        TextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = value ?: "",
            onValueChange = { newValue ->
                if (newValue.isNotBlank()) {
                    onValueChange(newValue)
                } else onValueChange(null)
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.TextFields,
                    contentDescription = Icons.Filled.TextFields.name
                )
            },
            label = {
                Text(
                    text = stringResource(id = R.string.filter_name)
                )
            },
            placeholder = {
                Text(
                    text = stringResource(id = R.string.filter_name_placeholder)
                )
            },
            trailingIcon = {
                value?.let {
                    IconButton(
                        onClick = {
                            onValueChange(null)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = Icons.Filled.Close.name
                        )
                    }
                }
            }
        )
    }
}

@Composable
private fun DescriptionFilter(
    modifier: Modifier = Modifier,
    value: String?,
    onValueChange: (String?) -> Unit,
) {
    Row(
        modifier = modifier
    ) {
        TextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = value ?: "",
            onValueChange = { newValue ->
                if (newValue.isNotBlank()) {
                    onValueChange(newValue)
                } else onValueChange(null)
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.TextFields,
                    contentDescription = Icons.Filled.TextFields.name
                )
            },
            label = {
                Text(
                    text = stringResource(id = R.string.filter_description)
                )
            },
            placeholder = {
                Text(
                    text = stringResource(id = R.string.filter_description_placeholder)
                )
            },
            trailingIcon = {
                value?.let {
                    IconButton(
                        onClick = {
                            onValueChange(null)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = Icons.Filled.Close.name
                        )
                    }
                }
            }
        )
    }
}

@Composable
private fun CountryCodeFilter(
    modifier: Modifier = Modifier,
    value: String?,
    onValueChange: (String?) -> Unit,
    openCountryCodeSelector: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                openCountryCodeSelector()
            }
    ) {
        TextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = value?.uppercase() ?: "",
            onValueChange = { newValue ->
                if (newValue.isNotBlank()) {
                    onValueChange(newValue)
                } else onValueChange(null)
            },
            enabled = false,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Flag,
                    contentDescription = Icons.Filled.Flag.name
                )
            },
            label = {
                Text(
                    text = stringResource(id = R.string.filter_country_code)
                )
            },
            placeholder = {
                Text(
                    text = stringResource(id = R.string.filter_country_code_placeholder)
                )
            },
            trailingIcon = {
                value?.let {
                    IconButton(
                        onClick = {
                            onValueChange(null)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = Icons.Filled.Close.name
                        )
                    }
                }
            },
            colors = TextFieldDefaults.colors(
                disabledContainerColor = TextFieldDefaults.colors().unfocusedContainerColor,
                disabledPrefixColor = TextFieldDefaults.colors().unfocusedPrefixColor,
                disabledSuffixColor = TextFieldDefaults.colors().unfocusedSuffixColor,
                disabledLabelColor = TextFieldDefaults.colors().unfocusedLabelColor,
                disabledTextColor = TextFieldDefaults.colors().unfocusedTextColor,
                disabledIndicatorColor = TextFieldDefaults.colors().unfocusedIndicatorColor,
                disabledPlaceholderColor = TextFieldDefaults.colors().unfocusedPlaceholderColor,
                disabledLeadingIconColor = TextFieldDefaults.colors().unfocusedLeadingIconColor,
                disabledTrailingIconColor = TextFieldDefaults.colors().unfocusedTrailingIconColor,
                disabledSupportingTextColor = TextFieldDefaults.colors().unfocusedSupportingTextColor,
            )
        )
    }
}

@Composable
private fun CountryCodeSelector(
    modifier: Modifier = Modifier,
    dismissCountryCodeSelector: () -> Unit,
    updateCountryCode: (String?) -> Unit,
) {
    val countryCodeList = java.util.Locale.getISOCountries()
    val readableCountryCodeList = countryCodeList.map {
        java.util.Locale("", it).displayCountry
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background.copy(alpha = .95f))
            .clickable {
                dismissCountryCodeSelector()
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyColumn(
            modifier = Modifier
                .widthIn(
                    max = 300.dp
                )
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .align(Alignment.CenterHorizontally)
                .clip(RoundedCornerShape(8.dp))
        ) {
            items(
                count = countryCodeList.size,
                key = { index ->
                    countryCodeList[index]
                }
            ) { index ->
                Column(
                    modifier = Modifier
                        .wrapContentWidth()
                        .padding(6.dp)
                        .clickable {
                            updateCountryCode(countryCodeList[index])
                            dismissCountryCodeSelector()
                        },
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = readableCountryCodeList[index] + " (${countryCodeList[index]})",
                        textAlign = TextAlign.Center
                    )
                    ColumnItemDivider()
                }
            }
        }
    }
}

@Composable
private fun TypeFilter(
    modifier: Modifier = Modifier,
    value: DestinationType?,
    onValueChange: (DestinationType?) -> Unit,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val isCountrySelected = value == DestinationType.COUNTRY
        val isCitySelected = value == DestinationType.CITY

        ElevatedButton(
            colors = ButtonDefaults.elevatedButtonColors(
                containerColor = if (isCountrySelected) filterNotOkButton else Color.Unspecified
            ),
            onClick = {
                onValueChange(if (isCountrySelected) null else DestinationType.COUNTRY)
            }
        ) {
            Text(
                text = stringResource(id = DestinationType.COUNTRY.nameResId)
            )
            Icon(
                imageVector = if (isCountrySelected) Icons.Filled.Remove else Icons.Filled.Add,
                contentDescription = if (isCountrySelected) Icons.Filled.Remove.name else Icons.Filled.Add.name
            )
        }
        ElevatedButton(
            colors = ButtonDefaults.elevatedButtonColors(
                containerColor = if (isCitySelected) filterNotOkButton else Color.Unspecified
            ),
            onClick = {
                onValueChange(if (isCitySelected) null else DestinationType.CITY)
            }
        ) {
            Text(
                text = stringResource(id = DestinationType.CITY.nameResId)
            )
            Icon(
                imageVector = if (isCitySelected) Icons.Filled.Remove else Icons.Filled.Add,
                contentDescription = if (isCitySelected) Icons.Filled.Remove.name else Icons.Filled.Add.name
            )
        }
    }
}

@Composable
private fun LastModifyFilter(
    modifier: Modifier = Modifier,
    value: LocalDateTime?,
    dateTimeFormatter: DateTimeFormatter,
    onValueChange: (LocalDateTime?) -> Unit,
    openLastModifyPicker: () -> Unit,
) {
    Row(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    openLastModifyPicker()
                }
        ) {
            TextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = value?.format(dateTimeFormatter) ?: "",
                onValueChange = { newValue ->
                    if (newValue.isNotBlank()) {
                        try {
                            val dateTime = LocalDateTime.parse(newValue)
                            onValueChange(dateTime)
                        } catch (e: DateTimeParseException) {
                            onValueChange(null)
                        }
                    } else onValueChange(null)
                },
                enabled = false,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.CalendarMonth,
                        contentDescription = Icons.Filled.CalendarMonth.name
                    )
                },
                label = {
                    Text(
                        text = stringResource(id = R.string.filter_last_modify)
                    )
                },
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.filter_last_modify_placeholder)
                    )
                },
                trailingIcon = {
                    value?.let {
                        IconButton(
                            onClick = {
                                onValueChange(null)
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = Icons.Filled.Close.name
                            )
                        }
                    }
                },
                colors = TextFieldDefaults.colors(
                    disabledContainerColor = TextFieldDefaults.colors().unfocusedContainerColor,
                    disabledPrefixColor = TextFieldDefaults.colors().unfocusedPrefixColor,
                    disabledSuffixColor = TextFieldDefaults.colors().unfocusedSuffixColor,
                    disabledLabelColor = TextFieldDefaults.colors().unfocusedLabelColor,
                    disabledTextColor = TextFieldDefaults.colors().unfocusedTextColor,
                    disabledIndicatorColor = TextFieldDefaults.colors().unfocusedIndicatorColor,
                    disabledPlaceholderColor = TextFieldDefaults.colors().unfocusedPlaceholderColor,
                    disabledLeadingIconColor = TextFieldDefaults.colors().unfocusedLeadingIconColor,
                    disabledTrailingIconColor = TextFieldDefaults.colors().unfocusedTrailingIconColor,
                    disabledSupportingTextColor = TextFieldDefaults.colors().unfocusedSupportingTextColor,
                )
            )
        }
    }
}

@Composable
private fun LastModifyPicker(
    modifier: Modifier = Modifier,
    onDateSelected: (LocalDateTime?) -> Unit,
    onDismiss: () -> Unit,
) {

    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        modifier = modifier,
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    val date = Instant.ofEpochMilli(datePickerState.selectedDateMillis ?: 0)
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime()
                    onDateSelected(date)
                    onDismiss()
                }) {
                Text(stringResource(id = R.string.select))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(id = R.string.cancel))
            }
        }
    ) {
        DatePicker(state = datePickerState)
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