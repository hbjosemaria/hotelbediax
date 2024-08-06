package com.joheba.hotelbediax.data.model.external

import com.google.gson.annotations.SerializedName
import com.joheba.hotelbediax.data.model.local.DestinationEntity
import com.joheba.hotelbediax.domain.core.DestinationType
import java.time.LocalDateTime

data class DestinationDto (
    val id: Int,
    val name: String,
    val description: String,
    @SerializedName("country_code")
    val countryCode: String,
    val type: DestinationType,
    @SerializedName("last_modify")
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