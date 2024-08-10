package com.joheba.hotelbediax.ui.destinationdetails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.joheba.hotelbediax.R
import com.joheba.hotelbediax.domain.core.DestinationType
import com.joheba.hotelbediax.ui.common.composables.ImageWithMessage
import com.joheba.hotelbediax.ui.common.composables.LoadingIndicator
import com.joheba.hotelbediax.ui.common.composables.destinationfields.CountryCodeField
import com.joheba.hotelbediax.ui.common.composables.destinationfields.CountryCodeSelector
import com.joheba.hotelbediax.ui.common.composables.destinationfields.DescriptionField
import com.joheba.hotelbediax.ui.common.composables.destinationfields.NameField
import com.joheba.hotelbediax.ui.common.composables.destinationfields.TypeField
import com.joheba.hotelbediax.ui.common.composables.topbar.SingleScreenTopAppBar
import com.joheba.hotelbediax.ui.theme.filterNotOkButton
import com.joheba.hotelbediax.ui.theme.filterOkButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DestinationDetailsScreen(
    navigateBack: () -> Unit,
    destinationId: Int?,
    destinationDetailsViewModel: DestinationDetailsViewModel = hiltViewModel<DestinationDetailsViewModel, DestinationDetailsViewModel.DestinationDetailsViewModelFactory>(
        creationCallback = { factory ->
            factory.create(destinationId)
        }
    ),
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val state by destinationDetailsViewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    var isCountryCodeSelectorOpened by rememberSaveable { mutableStateOf(false) }
    var areFieldsEditable by rememberSaveable { mutableStateOf(false) }
    var isDeletionConfirmationOpened by rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(state.isOperationPerformed) {
        if (state.isOperationPerformed) {
            navigateBack()
        }
    }

    LaunchedEffect(state.snackbarItem) {
        if (state.snackbarItem.show) {
            snackbarHostState.currentSnackbarData?.dismiss()
            snackbarHostState.showSnackbar(
                message = context.getString(state.snackbarItem.messageResId),
                duration = SnackbarDuration.Short
            )
            destinationDetailsViewModel.resetSnackbar()
        }
    }

    Scaffold(
        topBar = {
            SingleScreenTopAppBar(
                scrollBehavior = scrollBehavior,
                navigateBack = navigateBack,
                screenTitleResId = R.string.destination_detail
            )
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState
            )
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

                    val childModifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            vertical = 12.dp
                        )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(6.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                            ) {
                                NameField(
                                    modifier = childModifier,
                                    value = state.name,
                                    onValueChange = { newValue ->
                                        destinationDetailsViewModel.updateName(newValue ?: "")
                                    },
                                    isEnabled = areFieldsEditable
                                )
                                DescriptionField(
                                    modifier = childModifier,
                                    value = state.description,
                                    onValueChange = { newValue ->
                                        destinationDetailsViewModel.updateDescription(
                                            newValue ?: ""
                                        )
                                    },
                                    isEnabled = areFieldsEditable
                                )
                                CountryCodeField(
                                    modifier = childModifier,
                                    value = state.countryCode,
                                    onValueChange = { newValue ->
                                        destinationDetailsViewModel.updateCountryCode(
                                            newValue ?: ""
                                        )
                                    },
                                    openCountryCodeSelector = {
                                        isCountryCodeSelectorOpened = true
                                    },
                                    isEnabled = areFieldsEditable
                                )
                                TypeField(
                                    modifier = childModifier,
                                    value = state.type,
                                    onValueChange = { newValue ->
                                        destinationDetailsViewModel.updateType(
                                            newValue ?: DestinationType.COUNTRY
                                        )
                                    },
                                    selectedFieldColor = filterOkButton,
                                    isEnabled = areFieldsEditable
                                )
                            }
                            if (areFieldsEditable) {
                                UpdateButtons(
                                    updateDestination = {
                                        if (destinationDetailsViewModel.checkAllFieldsAreFilled()) {
                                            destinationDetailsViewModel.updateDestination(destination)
                                        }
                                    },
                                    cancelUpdate = {
                                        areFieldsEditable = false
                                    }
                                )
                            } else {
                                OperationButtons(
                                    enableUpdate = { value ->
                                        areFieldsEditable = value
                                    },
                                    openDeleteConfirmation = {
                                        isDeletionConfirmationOpened = true
                                    }
                                )
                            }
                        }

                        if (isCountryCodeSelectorOpened) {
                            CountryCodeSelector(
                                modifier = Modifier
                                    .padding(paddingValues),
                                onDismiss = {
                                    isCountryCodeSelectorOpened = false
                                },
                                updateCountryCode = { newValue ->
                                    destinationDetailsViewModel.updateCountryCode(newValue ?: "")
                                }
                            )
                        }

                        if (isDeletionConfirmationOpened) {
                            DeleteConfirmationDialog(
                                onDismiss = {
                                    isDeletionConfirmationOpened = false
                                },
                                onConfirm = {
                                    destinationDetailsViewModel.deleteDestination(destination)
                                }
                            )
                        }
                    }
                }
            }


            if (isCountryCodeSelectorOpened) {
                CountryCodeSelector(
                    modifier = Modifier
                        .padding(paddingValues),
                    onDismiss = {
                        isCountryCodeSelectorOpened = false
                    },
                    updateCountryCode = { newValue ->
                        destinationDetailsViewModel.updateCountryCode(newValue ?: "")
                    }
                )
            }
        }
    }
}

@Composable
private fun OperationButtons(
    modifier: Modifier = Modifier,
    enableUpdate: (Boolean) -> Unit,
    openDeleteConfirmation: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Button(
            onClick = {
                openDeleteConfirmation()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = filterNotOkButton
            )
        ) {
            Text(
                text = stringResource(id = R.string.delete)
            )
        }
        Button(
            onClick = {
                enableUpdate(true)
            }
        ) {
            Text(
                text = stringResource(id = R.string.update)
            )
        }
    }
}

@Composable
private fun UpdateButtons(
    modifier: Modifier = Modifier,
    updateDestination: () -> Unit,
    cancelUpdate: () -> Unit,
) {
    Row (
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Button(
            onClick = {
                cancelUpdate()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = filterNotOkButton
            )
        ) {
            Text(
                text = stringResource(id = R.string.cancel)
            )
        }

        Button(
            onClick = {
                updateDestination()
            }
        ) {
            Text(
                text = stringResource(id = R.string.confirm)
            )
        }
    }
}

@Composable
private fun DeleteConfirmationDialog (
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        modifier = modifier,
        icon = {
            Icon(
                imageVector = Icons.Filled.Info,
                contentDescription = Icons.Filled.Info.name)
        },
        title = {
            Text(text = stringResource(id = R.string.delete_dialog_title))
        },
        text = {
            Text(text = stringResource(id = R.string.delete_dialog_text))
        },
        onDismissRequest = {
            onDismiss()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm()
                }
            ) {
                Text(
                    text = stringResource(id = R.string.confirm)
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismiss()
                }
            ) {
                Text(
                    text = stringResource(id = R.string.cancel)
                )
            }
        }
    )
}