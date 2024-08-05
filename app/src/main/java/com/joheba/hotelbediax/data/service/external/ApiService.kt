package com.joheba.hotelbediax.data.service.external

import androidx.paging.PagingSource
import com.joheba.hotelbediax.data.model.external.DestinationDto
import com.joheba.hotelbediax.data.repository.ExternalDestinationRepository
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiDestinationService: ExternalDestinationRepository {
    @GET(ApiUrl.DESTINATION)
    override suspend fun getAll(): PagingSource<Int, DestinationDto>

    @DELETE("${ApiUrl.DESTINATION}{id}")
    override suspend fun deleteById(
        @Path("id") id: Int
    ): Boolean

    @PUT("${ApiUrl.DESTINATION}{id}")
    override suspend fun update(
        @Path("id") id: Int,
        @Body destination: DestinationDto
    ): Boolean

    @POST(ApiUrl.DESTINATION)
    override suspend fun create(
        @Body destination: DestinationDto
    ): Boolean
}

enum class ApiUrl(val url: String) {
    BASE("https://api.hotelbediax.com/1/");

    companion object ApiEndpoints {
        const val DESTINATION = "destination/"
    }
}