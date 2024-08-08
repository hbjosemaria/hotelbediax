package com.joheba.hotelbediax.ui.main.destination

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joheba.hotelbediax.R
import com.joheba.hotelbediax.domain.core.Destination
import com.joheba.hotelbediax.domain.usecase.DestinationUseCase
import com.joheba.hotelbediax.ui.common.contracts.SnackbarMessenger
import com.joheba.hotelbediax.ui.common.utils.SnackbarItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DestinationViewModel @Inject constructor(
    private val useCase: DestinationUseCase,
) : ViewModel(), SnackbarMessenger {

    private val _state = MutableStateFlow(DestinationState())
    val state = _state.asStateFlow()

    init {
        getDestinations()
    }

    fun getDestinations(filters: DestinationFilters = DestinationFilters()) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _state.value = _state.value.copy(
                    result = DestinationStateResult.Success(useCase.getDestinations(filters))
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    result = DestinationStateResult.Error(DestinationStateResultError.GENERIC_ERROR)
                )
            }

        }
    }

    fun createDestination(destination: Destination) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                useCase.createDestination(destination)
                updateSnackbar(
                    show = true,
                    messageResId = R.string.destination_created
                )
            } catch (e: Exception) {
                updateSnackbar(
                    show = true,
                    messageResId = R.string.destination_not_created,
                    isError = true
                )
            }
        }
    }

    fun updateDestination(destination: Destination) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                useCase.updateDestination(destination)
                updateSnackbar(
                    show = true,
                    messageResId = R.string.destination_updated
                )
            } catch (e: Exception) {
                updateSnackbar(
                    show = true,
                    messageResId = R.string.destination_not_updated,
                    isError = true
                )
            }
        }
    }

    fun deleteDestination(destination: Destination) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                useCase.deleteDestinationById(destination)
                updateSnackbar(
                    show = true,
                    messageResId = R.string.destination_deleted
                )
            } catch (e: Exception) {
                updateSnackbar(
                    show = true,
                    messageResId = R.string.destination_not_deleted,
                    isError = true
                )
            }
        }
    }

    override fun resetSnackbar() {
        _state.value = _state.value.copy(
            snackbarItem = SnackbarItem()
        )
    }

    override fun updateSnackbar(show: Boolean, messageResId: Int, isError: Boolean) {
        _state.value = _state.value.copy(
            snackbarItem = SnackbarItem(
                show = show,
                messageResId = messageResId,
                isError = isError
            )
        )
    }

}