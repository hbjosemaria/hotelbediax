package com.joheba.hotelbediax.domain.core

import androidx.compose.runtime.Immutable
import com.joheba.hotelbediax.R
import com.joheba.hotelbediax.data.model.external.DestinationDto
import com.joheba.hotelbediax.data.model.local.DestinationEntity
import com.joheba.hotelbediax.data.model.local.DestinationTempEntity
import java.time.LocalDateTime

@Immutable
data class Destination(
    val id: Int = 0,
    val name: String = "",
    val description: String = "",
    val countryCode: String = "",
    val type: DestinationType = DestinationType.COUNTRY,
    val lastModify: LocalDateTime = LocalDateTime.now(),
) {
    fun toEntity(): DestinationEntity =
        DestinationEntity(
            id = id,
            name = name,
            description = description,
            countryCode = countryCode,
            type = type,
            lastModify = lastModify
        )

    fun toTempEntity(action: DestinationTempEntity.DestinationTempEntityAction): DestinationTempEntity =
        DestinationTempEntity(
            id = id,
            name = name,
            description = description,
            countryCode = countryCode,
            type = type,
            lastModify = lastModify,
            action = action
        )

    fun toDto(): DestinationDto =
        DestinationDto(
            id = id,
            name = name,
            description = description,
            countryCode = countryCode,
            type = type,
            lastModify = lastModify
        )
}

enum class DestinationType(val nameResId: Int) {
    COUNTRY(R.string.country),
    CITY(R.string.city)
}