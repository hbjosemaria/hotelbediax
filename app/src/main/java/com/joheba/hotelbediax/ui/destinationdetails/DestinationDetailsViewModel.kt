package com.joheba.hotelbediax.ui.destinationdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joheba.hotelbediax.R
import com.joheba.hotelbediax.domain.core.Destination
import com.joheba.hotelbediax.domain.usecase.DestinationUseCase
import com.joheba.hotelbediax.ui.common.contracts.SnackbarMessenger
import com.joheba.hotelbediax.ui.common.utils.SnackbarItem
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = DestinationDetailsViewModel.DestinationDetailsViewModelFactory::class)
class DestinationDetailsViewModel @AssistedInject constructor(
    @Assisted private val destinationId: Int?,
    private val useCase: DestinationUseCase
) : ViewModel(), SnackbarMessenger {

    @AssistedFactory
    interface DestinationDetailsViewModelFactory {
        fun create(destinationId: Int?): DestinationDetailsViewModel
    }

    private val _state = MutableStateFlow(DestinationDetailsState())
    val state = _state.asStateFlow()

    init {
        loadDestination()
    }

    private fun loadDestination() {
        destinationId?.let{
            viewModelScope.launch(Dispatchers.IO) {
                try{
                    _state.value = _state.value.copy(
                        result = DestinationDetailsStateResult.Success(
                            destination = useCase.getDestinationById(it)
                        )
                    )
                } catch (e: Exception) {
                    _state.value = _state.value.copy(
                        result = DestinationDetailsStateResult.Error(
                            error = DestinationDetailsStateErrorType.GENERIC_ERROR
                        )
                    )
                }
            }
        } ?: {
            _state.value = _state.value.copy(
                result = DestinationDetailsStateResult.Error(
                    error = DestinationDetailsStateErrorType.GENERIC_ERROR
                )
            )
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