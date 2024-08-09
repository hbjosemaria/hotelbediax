package com.joheba.hotelbediax.ui.newdestination

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
class NewDestinationViewModel @Inject constructor(
    private val useCase: DestinationUseCase
) : ViewModel(), SnackbarMessenger {

    private val _state = MutableStateFlow(NewDestinationState())
    val state = _state.asStateFlow()

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