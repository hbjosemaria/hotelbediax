package com.joheba.hotelbediax.data.service.local.converters

import androidx.room.TypeConverter
import com.joheba.hotelbediax.data.model.local.DestinationTempEntity

class DestinationTempActionConverter {
    @TypeConverter
    fun fromDestinationTempAction(destinationTypeAction: DestinationTempEntity.DestinationTempEntityAction): String =
        destinationTypeAction.name


    @TypeConverter
    fun toDestinationTempAction(type: String): DestinationTempEntity.DestinationTempEntityAction =
        DestinationTempEntity.DestinationTempEntityAction.valueOf(type)

}