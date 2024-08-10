package com.joheba.hotelbediax.ui.newdestination

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
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
import com.joheba.hotelbediax.ui.common.composables.destinationfields.CountryCodeField
import com.joheba.hotelbediax.ui.common.composables.destinationfields.CountryCodeSelector
import com.joheba.hotelbediax.ui.common.composables.destinationfields.DescriptionField
import com.joheba.hotelbediax.ui.common.composables.destinationfields.NameField
import com.joheba.hotelbediax.ui.common.composables.destinationfields.TypeField
import com.joheba.hotelbediax.ui.common.composables.topbar.SingleScreenTopAppBar
import com.joheba.hotelbediax.ui.theme.filterOkButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewDestinationScreen(
    navigateBack: () -> Unit,
    newDestinationViewModel: NewDestinationViewModel = hiltViewModel()
) {

    val state by newDestinationViewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    var isCountryCodeSelectorOpened by rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(state.isDestinationCreated) {
        if (state.isDestinationCreated) {
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
            newDestinationViewModel.resetSnackbar()
        }
    }

    Scaffold(
        topBar = {
            SingleScreenTopAppBar(
                navigateBack = navigateBack,
                screenTitleResId = R.string.destination_new
            )
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState
            )
        }
    ) { paddingValues ->
        val childModifier = Modifier
            .fillMaxWidth()
            .padding(
                vertical = 12.dp
            )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column (
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
                        onValueChange = {newValue ->
                            newDestinationViewModel.updateName(newValue ?: "")
                        }
                    )
                    DescriptionField(
                        modifier = childModifier,
                        value = state.description,
                        onValueChange = {newValue ->
                            newDestinationViewModel.updateDescription(newValue ?: "")
                        }
                    )
                    CountryCodeField(
                        modifier = childModifier,
                        value = state.countryCode,
                        onValueChange = {newValue ->
                            newDestinationViewModel.updateCountryCode(newValue ?: "")
                        },
                        openCountryCodeSelector = {
                            isCountryCodeSelectorOpened = true
                        }
                    )
                    TypeField(
                        modifier = childModifier,
                        value = state.type,
                        onValueChange = {newValue ->
                            newDestinationViewModel.updateType(newValue ?: DestinationType.COUNTRY)
                        },
                        selectedFieldColor = filterOkButton
                    )
                }
                Button(
                    modifier = Modifier
                        .padding(bottom = 12.dp)
                        .width(200.dp),
                    onClick = {
                        if (newDestinationViewModel.checkAllFieldsAreFilled()) {
                            newDestinationViewModel.createDestination()
                        }
                    }
                ) {
                    Text (
                        text = stringResource(id = R.string.add)
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
                    updateCountryCode = {newValue ->
                        newDestinationViewModel.updateCountryCode(newValue ?: "")
                    }
                )
            }
        }
    }
}