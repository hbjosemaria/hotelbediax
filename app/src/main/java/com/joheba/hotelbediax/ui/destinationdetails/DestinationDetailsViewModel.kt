package com.joheba.hotelbediax.ui.destinationdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joheba.hotelbediax.R
import com.joheba.hotelbediax.domain.core.Destination
import com.joheba.hotelbediax.domain.core.DestinationType
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
                    val destination = useCase.getDestinationById(it)
                    _state.value = _state.value.copy(
                        result = DestinationDetailsStateResult.Success(
                            destination = destination
                        ),
                        id = destination.id,
                        name = destination.name,
                        description = destination.description,
                        type = destination.type,
                        countryCode = destination.countryCode,
                        lastModify = destination.lastModify
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
                val newDestination = destination.copy(
                    name = state.value.name,
                    description = state.value.description,
                    countryCode = state.value.countryCode,
                    type = state.value.type
                )
                _state.value = _state.value.copy(
                    isOperationPerformed = useCase.updateDestination(newDestination)
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
                _state.value = _state.value.copy(
                    isOperationPerformed = useCase.deleteDestinationById(destination)
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

    fun updateName(name: String) {
        _state.value = _state.value.copy(
            name = name
        )
    }

    fun updateDescription(description: String) {
        _state.value = _state.value.copy(
            description = description
        )
    }

    fun updateCountryCode(countryCode: String) {
        _state.value = _state.value.copy(
            countryCode = countryCode
        )
    }

    fun updateType(type: DestinationType) {
        _state.value = _state.value.copy(
            type = type
        )
    }

    fun checkAllFieldsAreFilled() : Boolean {
        val areAllFilled = state.value.name.isNotBlank() &&
                state.value.description.isNotBlank() &&
                state.value.countryCode.isNotBlank()

        if (!areAllFilled) {
            _state.value = _state.value.copy(
                snackbarItem = SnackbarItem(
                    show = true,
                    messageResId = R.string.all_fields_must_be_filled,
                    isError = true
                )
            )
        }

        return areAllFilled
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