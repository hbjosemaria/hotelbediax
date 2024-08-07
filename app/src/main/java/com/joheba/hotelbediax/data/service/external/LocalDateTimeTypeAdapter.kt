package com.joheba.hotelbediax.data.service.external

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class LocalDateTimeTypeAdapter : TypeAdapter<LocalDateTime>() {
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    override fun write(out: JsonWriter, value: LocalDateTime) {
        out.value(value.format(formatter))
    }

    override fun read(`in`: JsonReader): LocalDateTime {
        val dateString = `in`.nextString()
        return LocalDateTime.parse(dateString, formatter)
    }
}