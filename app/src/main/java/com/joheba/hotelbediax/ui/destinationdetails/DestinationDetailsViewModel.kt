package com.joheba.hotelbediax.ui.destinationdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joheba.hotelbediax.domain.usecase.DestinationUseCase
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
    private val destinationUseCase: DestinationUseCase
) : ViewModel() {

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
                            destination = destinationUseCase.getDestinationById(it)
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
}