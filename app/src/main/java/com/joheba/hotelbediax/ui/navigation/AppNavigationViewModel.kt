package com.joheba.hotelbediax.ui.navigation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
class AppNavigationViewModel() : ViewModel() {

    private val _appNavigationState = MutableStateFlow(AppNavigationState())
    val appNavigationState = _appNavigationState.asStateFlow()

    fun updateSelectedNavigationIndex(selectedIndex: Int) {
        _appNavigationState.value = _appNavigationState.value.copy(
            selectedNavigationIndex = selectedIndex
        )
    }

}