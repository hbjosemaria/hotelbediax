package com.joheba.hotelbediax.domain.core

import com.joheba.hotelbediax.data.model.local.DestinationEntity
import java.time.LocalDateTime

data class Destination (
    val id: Int,
    val name: String,
    val description: String,
    val countryCode: String,
    val type: DestinationType,
    val lastModify: LocalDateTime
) {
    fun toEntity() : DestinationEntity =
        DestinationEntity(
            id = id,
            name = name,
            description = description,
            countryCode = countryCode,
            type = type,
            lastModify = lastModify
        )
}

enum class DestinationType(
    val country: String,
    val city: String
)