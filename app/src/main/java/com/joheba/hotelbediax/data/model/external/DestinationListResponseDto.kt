package com.joheba.hotelbediax.data.model.external

data class DestinationListResponseDto (
    val page: Int,
    val results: List<DestinationDto>,
    val totalPages: Int,
    val totalResults: Long
)