package com.joheba.hotelbediax.data.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "destination_remote_key")
data class DestinationRemoteKeyEntity(
    val prevKey: Int?,
    val nextKey: Int?,
    @PrimaryKey
    val destinationId: Int
)