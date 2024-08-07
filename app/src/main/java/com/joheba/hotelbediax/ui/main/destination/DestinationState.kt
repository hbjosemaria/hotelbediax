package com.joheba.hotelbediax.ui.main.destination

import androidx.paging.PagingData
import com.joheba.hotelbediax.domain.core.Destination
import com.joheba.hotelbediax.domain.usecase.DestinationFilterOptions
import com.joheba.hotelbediax.ui.common.utils.SnackbarItem
import kotlinx.coroutines.flow.Flow

data class DestinationState (
    val result: DestinationStateResult = DestinationStateResult.Loading,
    val selectedFilter: DestinationFilterOptions? = null,
    val scrollToTop: Boolean = false,
    val snackbarItem: SnackbarItem = SnackbarItem()
)

sealed class DestinationStateResult {
    data object Loading: DestinationStateResult()
    data class Error(val error: DestinationStateResultError): DestinationStateResult()
    data class Success(val destinationList: Flow<PagingData<Destination>>): DestinationStateResult()
}

enum class DestinationStateResultError {
    GENERIC_ERROR
}