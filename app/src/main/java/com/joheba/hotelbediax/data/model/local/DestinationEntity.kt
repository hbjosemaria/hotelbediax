package com.joheba.hotelbediax.data.model.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.joheba.hotelbediax.domain.core.Destination
import com.joheba.hotelbediax.domain.core.DestinationType
import java.time.LocalDateTime

@Entity(
    tableName = "destination",
    indices = [Index(value = ["id"])]
)
data class DestinationEntity (
    @PrimaryKey
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

@Entity(
    tableName = "destination_temp",
    primaryKeys = ["id", "action"]
)
data class DestinationTempEntity (
    val id: Int,
    val name: String,
    val description: String,
    val countryCode: String,
    val type: DestinationType,
    val lastModify: LocalDateTime,
    val action: DestinationTempEntityAction
) {
    enum class DestinationTempEntityAction {
        CREATE,
        UPDATE,
        DELETE
    }
}

