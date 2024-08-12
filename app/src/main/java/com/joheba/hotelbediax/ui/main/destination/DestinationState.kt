package com.joheba.hotelbediax.ui.main.destination

import androidx.paging.PagingData
import com.joheba.hotelbediax.domain.core.Destination
import com.joheba.hotelbediax.domain.core.DestinationType
import com.joheba.hotelbediax.ui.common.utils.SnackbarItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emptyFlow
import java.time.LocalDateTime

data class DestinationState(
    val result: DestinationStateResult = DestinationStateResult.Loading,
    val filters: DestinationFilters = DestinationFilters(),
    val areFiltersApplied: Boolean = false,
    val scrollToTop: Boolean = false,
    val pendingTempOperationsNumber: Int = 0,
    val snackbarItem: SnackbarItem = SnackbarItem(),
)

sealed class DestinationStateResult {
    data object Loading : DestinationStateResult()
    data class Error(val error: DestinationStateResultError) : DestinationStateResult()
    data class Success(val destinationList: Flow<PagingData<Destination>>) :
        DestinationStateResult()
}

enum class DestinationStateResultError {
    GENERIC_ERROR
}

data class DestinationFilters(
    val id: Int? = null,
    val name: String? = null,
    val description: String? = null,
    val countryCode: String? = null,
    val type: DestinationType? = null,
    val lastModify: LocalDateTime? = null
) {
    fun isAnyFilterFilled() : Boolean {
        return id != null || name != null || description != null || countryCode != null || type != null || lastModify != null
    }
}