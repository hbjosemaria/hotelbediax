package com.joheba.hotelbediax.ui.newdestination

import com.joheba.hotelbediax.domain.core.DestinationType

data class NewDestinationState (
    val name: String = "",
    val description: String = "",
    val countryCode: String = "",
    val type: DestinationType = DestinationType.COUNTRY
)