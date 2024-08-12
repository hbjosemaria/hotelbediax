package com.joheba.hotelbediax.domain.usecase

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.joheba.hotelbediax.data.workers.FirstSyncWorker
import com.joheba.hotelbediax.data.workers.TempSyncWorker
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class DestinationWorkerUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
) {

    private val firstSyncWorkerTag = "AllDestinationSyncWorker"
    private val firstSyncWorkTag = "AllDestinationSyncWork"
    private val tempSyncWorkerTag = "TempSyncWorker"
    private val tempSyncWorkTag = "TempSyncWork"

    fun firstSyncWorkerRequest() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(true)
            .setRequiresStorageNotLow(true)
            .build()

        val workRequest = OneTimeWorkRequestBuilder<FirstSyncWorker>()
            .setConstraints(constraints)
            .setBackoffCriteria(
                BackoffPolicy.LINEAR,
                WorkRequest.MIN_BACKOFF_MILLIS,
                TimeUnit.MILLISECONDS
            )
            .addTag(firstSyncWorkerTag)
            .build()

        WorkManager.getInstance(context)
            .enqueueUniqueWork(firstSyncWorkTag, ExistingWorkPolicy.KEEP, workRequest)
    }

    fun tempSyncWorkerRequest() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(true)
            .build()

        val workRequest = PeriodicWorkRequestBuilder<TempSyncWorker>(
            PeriodicWorkRequest.MIN_PERIODIC_INTERVAL_MILLIS,
            TimeUnit.MILLISECONDS
        )
            .setConstraints(constraints)
            .setBackoffCriteria(
                BackoffPolicy.LINEAR,
                WorkRequest.MIN_BACKOFF_MILLIS,
                TimeUnit.MILLISECONDS
            )
            .addTag(tempSyncWorkerTag)
            .build()

        WorkManager.getInstance(context)
            .enqueueUniquePeriodicWork(
                tempSyncWorkTag,
                ExistingPeriodicWorkPolicy.KEEP,
                workRequest
            )
    }
}