package com.joheba.hotelbediax.ui.destinationdetails

import com.joheba.hotelbediax.domain.core.Destination

data class DestinationDetailsState(
    val result: DestinationDetailsStateResult = DestinationDetailsStateResult.Loading
)

sealed class DestinationDetailsStateResult {
    data object Loading: DestinationDetailsStateResult()
    data class Error(val error: DestinationDetailsStateErrorType): DestinationDetailsStateResult()
    data class Success(val destination: Destination?): DestinationDetailsStateResult()
}

enum class DestinationDetailsStateErrorType {
    GENERIC_ERROR
}