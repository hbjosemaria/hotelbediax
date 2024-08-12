package com.joheba.hotelbediax.data.model.external

import com.google.gson.annotations.SerializedName

data class DestinationListResponseDto(
    val page: Int = 1,
    @SerializedName("destinations")
    val results: List<DestinationDto>,
    val totalPages: Int = 1,
    val totalResults: Long = 0,
)