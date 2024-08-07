package com.joheba.hotelbediax.ui.newdestination

import androidx.lifecycle.ViewModel
import com.joheba.hotelbediax.domain.usecase.DestinationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class NewDestinationViewModel @Inject constructor(
    private val destinationUseCase: DestinationUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(NewDestinationState())
    val state = _state.asStateFlow()

}