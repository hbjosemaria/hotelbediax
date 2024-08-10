package com.joheba.hotelbediax.ui.newdestination

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joheba.hotelbediax.R
import com.joheba.hotelbediax.domain.core.Destination
import com.joheba.hotelbediax.domain.core.DestinationType
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
class NewDestinationViewModel @Inject constructor(
    private val useCase: DestinationUseCase
) : ViewModel(), SnackbarMessenger {

    private val _state = MutableStateFlow(NewDestinationState())
    val state = _state.asStateFlow()

    fun createDestination() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val destination = Destination (
                    name = state.value.name,
                    description = state.value.description,
                    countryCode = state.value.countryCode,
                    type = state.value.type
                )
                _state.value = _state.value.copy(
                    isDestinationCreated = useCase.createDestination(destination)
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