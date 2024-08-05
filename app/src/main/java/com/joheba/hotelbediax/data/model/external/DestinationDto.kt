package com.joheba.hotelbediax.data.model.external

import com.joheba.hotelbediax.domain.core.Destination
import com.joheba.hotelbediax.domain.core.DestinationType
import java.time.LocalDateTime

data class DestinationDto (

    val id: Int,
    val name: String,
    val description: String,
    val countryCode: String,
    val type: DestinationType,
    val lastModify: LocalDateTime
) {
    fun toDomain() : Destination =
        Destination(
            id = id,
            name = name,
            description = description,
            countryCode = countryCode,
            type = type,
            lastModify = lastModify
        )
}