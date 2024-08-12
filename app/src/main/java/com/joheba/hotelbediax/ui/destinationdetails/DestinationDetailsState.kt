package com.joheba.hotelbediax.ui.destinationdetails

import com.joheba.hotelbediax.domain.core.Destination
import com.joheba.hotelbediax.domain.core.DestinationType
import com.joheba.hotelbediax.ui.common.utils.SnackbarItem
import java.time.LocalDateTime

data class DestinationDetailsState(
    val result: DestinationDetailsStateResult = DestinationDetailsStateResult.Loading,
    val id: Int = 0,
    val name: String = "",
    val description: String = "",
    val countryCode: String = "",
    val lastModify: LocalDateTime? = null,
    val type: DestinationType = DestinationType.COUNTRY,
    val isOperationPerformed: Boolean = false,
    val snackbarItem: SnackbarItem = SnackbarItem()
)

sealed class DestinationDetailsStateResult {
    data object Loading: DestinationDetailsStateResult()
    data class Error(val error: DestinationDetailsStateErrorType): DestinationDetailsStateResult()
    data class Success(val destination: Destination): DestinationDetailsStateResult()
}

enum class DestinationDetailsStateErrorType {
    GENERIC_ERROR
}