package com.joheba.hotelbediax.ui.main.destination

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joheba.hotelbediax.domain.core.DestinationType
import com.joheba.hotelbediax.domain.usecase.DestinationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class DestinationViewModel @Inject constructor(
    private val useCase: DestinationUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(DestinationState())
    val state = _state.asStateFlow()

    init {
        getDestinations()
        getPendingTempOperations()
    }

    private fun getDestinations() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _state.value = _state.value.copy(
                    result = DestinationStateResult.Success(useCase.getDestinations(state.value.filters))
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    result = DestinationStateResult.Error(DestinationStateResultError.GENERIC_ERROR)
                )
            }

        }
    }

    private fun getPendingTempOperations() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                useCase.areOperationsPending().collect { amountOfOperations ->
                    _state.value = _state.value.copy(
                        pendingTempOperationsNumber = amountOfOperations
                    )
                }

            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    pendingTempOperationsNumber = 0
                )
            }
        }
    }

    fun forceSyncTempOperations() {
        viewModelScope.launch(Dispatchers.IO) {
            useCase.syncPendingOperations()
        }
    }

    fun applyFilters() {
        when {
            state.value.filters.isAnyFilterFilled() -> {
                _state.value = _state.value.copy(
                    areFiltersApplied = true
                )
                getDestinations()
            }
            !state.value.filters.isAnyFilterFilled() && state.value.areFiltersApplied -> {
                _state.value = _state.value.copy(
                    areFiltersApplied = false
                )
                getDestinations()
            } else -> {
                _state.value = _state.value.copy(
                    areFiltersApplied = false
                )
            }
        }
    }

    fun resetFilters() {
        _state.value = _state.value.copy(
            filters = DestinationFilters(),
            areFiltersApplied = false
        )
        getDestinations()
    }

    fun updateFilterId(id: Int?) {
        _state.value = _state.value.copy(
            filters = _state.value.filters.copy(
                id = id
            )
        )
    }

    fun updateFilterName(name: String?) {
        _state.value = _state.value.copy(
            filters = _state.value.filters.copy(
                name = name
            )
        )
    }

    fun updateFilterDescription(description: String?) {
        _state.value = _state.value.copy(
            filters = _state.value.filters.copy(
                description = description
            )
        )
    }

    fun updateFilterType(type: DestinationType?) {
        _state.value = _state.value.copy(
            filters = _state.value.filters.copy(
                type = type
            )
        )
    }

    fun updateFilterCountryCode(countryCode: String?) {
        _state.value = _state.value.copy(
            filters = _state.value.filters.copy(
                countryCode = countryCode
            )
        )
    }

    fun updateFilterLastModify(lastModify: LocalDateTime?) {
        _state.value = _state.value.copy(
            filters = _state.value.filters.copy(
                lastModify = lastModify
            )
        )
    }
}