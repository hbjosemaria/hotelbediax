package com.joheba.hotelbediax.data.service.external

import com.joheba.hotelbediax.data.di.RetrofitModule.gson
import com.joheba.hotelbediax.data.model.external.DestinationDto
import com.joheba.hotelbediax.data.model.external.DestinationListResponseDto
import com.joheba.hotelbediax.domain.core.DestinationType
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import java.time.LocalDateTime
import java.util.Locale

//Important note: to test the case where CRUD can't be synced in the API, change the response boolean parameter to false and its HTTP code to 409
// By doing so, you could verify that this operations are being registered in the temp table to sync them when the device has again internet connectivity
class FakeBackendInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestUrl = chain.request().url.toUri().toString()
        val requestMethod = chain.request().method
        return when {
            //GET to destination
            requestMethod.contains("GET") && requestUrl.contains(ApiUrl.DESTINATION) -> {
                getDestination(chain)
            }
            //POST to destination
            requestMethod.contains("POST") && requestUrl.contains(ApiUrl.DESTINATION) -> {
                postDestination(chain)
            }
            //PUT to destination/{id}
            requestMethod.contains("PUT") && requestUrl.contains("${ApiUrl.DESTINATION}{id}") -> {
                putDestinationId(chain)
            }
            //PUT to destination
            requestMethod.contains("PUT") && requestUrl.contains(ApiUrl.DESTINATION) -> {
                putDestination(chain)
            }
            //DELETE to destination/{id}
            requestMethod.contains("DELETE") && requestUrl.contains("${ApiUrl.DESTINATION}{id}") -> {
                deleteDestinationId(chain)
            }
            //DELETE to destination
            requestMethod.contains("DELETE") && requestUrl.contains(ApiUrl.DESTINATION)-> {
                deleteDestination(chain)
            }
            else -> chain.proceed(chain.request())
        }
    }

    private fun getDestination(
        chain: Interceptor.Chain
    ) : Response {
        val pageQuery = chain.request().url.queryParameter("page")
        val response = pageQuery?.let{ page ->
            val pageValue = page.toInt()
            DestinationListResponseDto(
                page = pageValue,
                results = (200 downTo 1).map { index ->
                    val id = 210001 - (pageValue * index)
                    DestinationDto(
                        id = id,
                        name = "Destination $id",
                        description = "An inspiring description for destination $id, one of the most exciting places of the world.",
                        countryCode = Locale.getISOCountries()[(Math.random() * 10).toInt()],
                        type = if (id % 2 == 0) DestinationType.CITY else DestinationType.COUNTRY,
                        lastModify = LocalDateTime.now().minusDays((Math.random() * 10).toLong()).minusHours((Math.random() * 10).toLong()).minusMinutes((Math.random() * 10).toLong())
                    )
                },
                totalPages = 1050,
                totalResults = 210000
            )
        } ?: DestinationListResponseDto(
            page = 1,
            results = (210000 downTo 1).map {
                DestinationDto(
                    id = it,
                    name = "Destination $it",
                    description = "An inspiring description for destination $it, one of the most exciting places of the world.",
                    countryCode = Locale.getISOCountries()[(Math.random() * 10).toInt()],
                    type = if (it % 2 == 0) DestinationType.CITY else DestinationType.COUNTRY,
                    lastModify = LocalDateTime.now().minusDays((Math.random() * 10).toLong()).minusHours((Math.random() * 10).toLong()).minusMinutes((Math.random() * 10).toLong())
                )
            },
            totalPages = 1,
            totalResults = 210000
        )
        val jsonResponse = gson.toJson(response)
        return Response.Builder()
            .code(200)
            .protocol(Protocol.HTTP_2)
            .message("OK")
            .body(
                jsonResponse
                    .toByteArray()
                    .toResponseBody(
                        "application/json".toMediaTypeOrNull()
                    )
            )
            .addHeader("content-type", "application/json")
            .request(chain.request())
            .build()
    }

    private fun postDestination(
        chain: Interceptor.Chain
    ) : Response {
        //Faking a successful call
        val response = true
        val jsonResponse = gson.toJson(response)
        return Response.Builder()
            .code(200)
            .protocol(Protocol.HTTP_2)
            .message("OK")
            .body(
                jsonResponse
                    .toByteArray()
                    .toResponseBody(
                        "application/json".toMediaTypeOrNull()
                    )
            )
            .addHeader("content-type", "application/json")
            .request(chain.request())
            .build()
    }

    private fun putDestinationId(
        chain: Interceptor.Chain
    ) : Response {
        //Faking a successful call
        val response = true
        val jsonResponse = gson.toJson(response)
        return Response.Builder()
            .code(200)
            .protocol(Protocol.HTTP_2)
            .message("OK")
            .body(
                jsonResponse
                    .toByteArray()
                    .toResponseBody(
                        "application/json".toMediaTypeOrNull()
                    )
            )
            .addHeader("content-type", "application/json")
            .request(chain.request())
            .build()
    }

    private fun putDestination(
        chain: Interceptor.Chain
    ) : Response {
        //Faking a successful call
        val response = true
        val jsonResponse = gson.toJson(response)
        return Response.Builder()
            .code(200)
            .protocol(Protocol.HTTP_2)
            .message("OK")
            .body(
                jsonResponse
                    .toByteArray()
                    .toResponseBody(
                        "application/json".toMediaTypeOrNull()
                    )
            )
            .addHeader("content-type", "application/json")
            .request(chain.request())
            .build()
    }

    private fun deleteDestinationId(
        chain: Interceptor.Chain
    ) : Response {
        //Faking a successful call
        val response = true
        val jsonResponse = gson.toJson(response)
        return Response.Builder()
            .code(200)
            .protocol(Protocol.HTTP_2)
            .message("OK")
            .body(
                jsonResponse
                    .toByteArray()
                    .toResponseBody(
                        "application/json".toMediaTypeOrNull()
                    )
            )
            .addHeader("content-type", "application/json")
            .request(chain.request())
            .build()
    }

    private fun deleteDestination(
        chain: Interceptor.Chain
    ) : Response {
        //Faking a successful call
        val response = true
        val jsonResponse = gson.toJson(response)
        return Response.Builder()
            .code(200)
            .protocol(Protocol.HTTP_2)
            .message("OK")
            .body(
                jsonResponse
                    .toByteArray()
                    .toResponseBody(
                        "application/json".toMediaTypeOrNull()
                    )
            )
            .addHeader("content-type", "application/json")
            .request(chain.request())
            .build()
    }
}