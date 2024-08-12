package com.joheba.hotelbediax.data.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.gson.GsonBuilder
import com.google.gson.stream.JsonReader
import com.joheba.hotelbediax.data.model.external.DestinationDto
import com.joheba.hotelbediax.data.repository.ExternalDestinationRepository
import com.joheba.hotelbediax.data.repository.LocalDestinationRepository
import com.joheba.hotelbediax.data.service.external.LocalDateTimeTypeAdapter
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import java.io.InputStreamReader
import java.time.LocalDateTime

@HiltWorker
class FirstSyncWorker @AssistedInject constructor(
    @Assisted val context: Context,
    @Assisted val params: WorkerParameters,
    private val externalDestinationRepository: ExternalDestinationRepository,
    private val localDestinationRepository: LocalDestinationRepository,
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            val responseBody = externalDestinationRepository.getAll()
            val gson = GsonBuilder()
                .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeTypeAdapter())
                .create()

            responseBody.source().use { source ->
                val reader = InputStreamReader(source.inputStream())
                val jsonReader = JsonReader(reader)

                val batchSize = 50
                var batchCount = 0
                val destinationList = mutableListOf<DestinationDto>()

                jsonReader.beginObject()

                while (jsonReader.hasNext()) {

                    if (jsonReader.nextName() == "destinations") {

                        jsonReader.beginArray()

                        while (jsonReader.hasNext()) {
                            val destinationDto = gson.fromJson<DestinationDto>(
                                jsonReader,
                                DestinationDto::class.java
                            )
                            destinationList.add(destinationDto)
                            batchCount++

                            if (batchCount == batchSize) {
                                async {
                                    localDestinationRepository.insertAll(destinationList.map { it.toEntity() })
                                }.await()
                                destinationList.clear()
                                batchCount = 0
                            }
                        }

                        if (batchCount > 0) {
                            localDestinationRepository.insertAll(destinationList.map { it.toEntity() })
                            destinationList.clear()
                        }

                        jsonReader.endArray()
                    } else {
                        jsonReader.skipValue()
                    }
                }
                jsonReader.endObject()
            }
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}