package com.joheba.hotelbediax.ui.newdestination

import com.joheba.hotelbediax.domain.core.DestinationType
import com.joheba.hotelbediax.ui.common.utils.SnackbarItem

data class NewDestinationState (
    val name: String = "",
    val description: String = "",
    val countryCode: String = "",
    val type: DestinationType = DestinationType.COUNTRY,
    val isDestinationCreated: Boolean = false,
    val snackbarItem: SnackbarItem = SnackbarItem()
)