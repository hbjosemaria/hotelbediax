package com.joheba.hotelbediax.data.service.local.converters

import androidx.room.TypeConverter
import com.joheba.hotelbediax.domain.core.DestinationType

class DestinationTypeConverter {
    @TypeConverter
    fun fromDestinationType(destinationType: DestinationType?): String? =
        destinationType?.name


    @TypeConverter
    fun toDestinationType(type: String?): DestinationType? =
        type?.let { DestinationType.valueOf(it) }

}