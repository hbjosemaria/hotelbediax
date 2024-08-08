package com.joheba.hotelbediax.ui.main.destination

import androidx.paging.PagingData
import com.joheba.hotelbediax.domain.core.Destination
import com.joheba.hotelbediax.domain.core.DestinationType
import com.joheba.hotelbediax.ui.common.utils.SnackbarItem
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

data class DestinationState(
    val result: DestinationStateResult = DestinationStateResult.Loading,
    val filters: DestinationFilters = DestinationFilters(),
    val scrollToTop: Boolean = false,
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
)