package com.joheba.hotelbediax.data.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.joheba.hotelbediax.data.repository.DestinationTempRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext

@HiltWorker
class TempSyncWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted private val params: WorkerParameters,
    private val tempRepository: DestinationTempRepository,
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {

        try {
            val createOperations =
                tempRepository.getCreationOperations().map { it.toDto() }
            val updateOperations =
                tempRepository.getUpdateOperations().map { it.toDto() }
            val deleteOperations =
                tempRepository.getDeleteOperations().map { it.id }

            val createDeferred = async {
                if (createOperations.isNotEmpty()) {
                    tempRepository.syncCreateOperations(createOperations)
                    tempRepository.clearCreationOperations()
                }
            }

            val updateDeferred = async {
                if (updateOperations.isNotEmpty()) {
                    tempRepository.syncUpdateOperations(updateOperations)
                    tempRepository.clearUpdateOperations()
                }
            }

            val deleteDeferred = async {
                if (deleteOperations.isNotEmpty()) {
                    tempRepository.syncDeleteOperations(deleteOperations)
                    tempRepository.clearDeleteOperations()
                }
            }

            awaitAll(createDeferred, updateDeferred, deleteDeferred)

            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }

}