package com.joheba.hotelbediax.data.service.external

import com.joheba.hotelbediax.data.model.external.DestinationDto
import com.joheba.hotelbediax.data.model.external.DestinationListResponseDto
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiDestinationService {

    @GET(ApiUrl.DESTINATION)
    suspend fun getAll(): ResponseBody

    @GET(ApiUrl.DESTINATION)
    suspend fun getAll(@Query("page") page: Int): DestinationListResponseDto

    @DELETE("${ApiUrl.DESTINATION}{id}")
    suspend fun deleteById(
        @Path("id") id: Int,
    ): Boolean

    @DELETE(ApiUrl.DESTINATION)
    suspend fun deleteBunchById(@Query("ids") destinationIdList: List<Int>): Boolean

    @PUT("${ApiUrl.DESTINATION}{id}")
    suspend fun update(
        @Path("id") id: Int,
        @Body destination: DestinationDto,
    ): Boolean

    @PUT(ApiUrl.DESTINATION)
    suspend fun updateBunch(
        @Body destinationList: List<DestinationDto>,
    ): Boolean

    @POST(ApiUrl.DESTINATION)
    suspend fun create(
        @Body destination: DestinationDto,
    ): Boolean

    @POST(ApiUrl.DESTINATION)
    suspend fun createBunch(
        @Body destinationList: List<DestinationDto>,
    ): Boolean
}

enum class ApiUrl(val url: String) {
    BASE("https://api.hotelbediax.com/1/");

    companion object {
        const val DESTINATION = "destination"
    }
}