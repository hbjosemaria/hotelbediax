package com.joheba.hotelbediax.data.service.local.converters

import androidx.room.TypeConverter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class LastModifyTypeConverter {

    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    @TypeConverter
    fun fromLastModifyType(lastModify: LocalDateTime?): String? =
        lastModify?.format(formatter)


    @TypeConverter
    fun toLastModifyType(lastModify: String?): LocalDateTime? =
        lastModify?.let { LocalDateTime.parse(it, formatter) }
}