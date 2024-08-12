package com.joheba.hotelbediax.domain.usecase

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.joheba.hotelbediax.data.model.local.DestinationTempEntity
import com.joheba.hotelbediax.data.remotemediator.DestinationRemoteMediator
import com.joheba.hotelbediax.data.repository.DestinationTempRepository
import com.joheba.hotelbediax.data.repository.ExternalDestinationRepository
import com.joheba.hotelbediax.data.repository.LocalDestinationRepository
import com.joheba.hotelbediax.domain.core.Destination
import com.joheba.hotelbediax.ui.main.destination.DestinationFilters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class DestinationUseCase @Inject constructor(
    private val apiRepository: ExternalDestinationRepository,
    private val destinationRepository: LocalDestinationRepository,
    private val destinationTempRepository: DestinationTempRepository,
    private val remoteMediator: DestinationRemoteMediator
) {

    fun getDestinations(filters: DestinationFilters): Flow<PagingData<Destination>> =
        Pager(
            config = PagingConfig(
                pageSize = 200,
                prefetchDistance = 100,
                initialLoadSize = 400
            ),
            //If API endpoint allows pagination, then consider using a RemoteMediator
            //RemoteMediator uses cache invalidation. This feature could be extended to workers if needed
//            remoteMediator = remoteMediator,
            pagingSourceFactory = {
                destinationRepository.getAll(filters)
            }
        ).flow
            .map { pagingData ->
                pagingData.map {
                    it.toDomain()
                }
            }

    suspend fun getDestinationById(destinationId: Int): Destination =
        destinationRepository.getDestinationById(destinationId).toDomain()

    suspend fun createDestination(destination: Destination): Boolean {
        var locallyInserted = false
        val newDestination = destination.copy(
            id = destinationRepository.getNewId()
        )
        try {
            if (destinationRepository.create(newDestination.toEntity()) > 0) {
                locallyInserted = true
            }
            apiRepository.create(newDestination.toDto())
        } catch (e: Exception) {
            destinationTempRepository.addEnqueuedRecord(
                newDestination.toTempEntity(
                    DestinationTempEntity.DestinationTempEntityAction.CREATE
                )
            )
        }
        return locallyInserted
    }

    suspend fun updateDestination(destination: Destination): Boolean {
        val updatedDestination = destination.copy(
            lastModify = LocalDateTime.now()
        )
        val result = destinationRepository.update(updatedDestination.toEntity()) == 1
        try {
            apiRepository.update(updatedDestination.id, updatedDestination.toDto())
        } catch (e: Exception) {
            destinationTempRepository.addEnqueuedRecord(
                updatedDestination.toTempEntity(
                    DestinationTempEntity.DestinationTempEntityAction.UPDATE
                )
            )
        }
        return result
    }

    suspend fun deleteDestinationById(destination: Destination): Boolean {
        val result = destinationRepository.deleteById(destination.id) == 1
        try {
            apiRepository.deleteById(destination.id)
        } catch (e: Exception) {
            destinationTempRepository.addEnqueuedRecord(
                destination.toTempEntity(
                    DestinationTempEntity.DestinationTempEntityAction.DELETE
                )
            )
        }
        return result
    }

    suspend fun areOperationsPending(): Flow<Int> =
        destinationTempRepository.pendingTempOperationsNumber()

    suspend fun syncPendingOperations() {
        withContext(Dispatchers.IO) {
            val createOperations =
                destinationTempRepository.getCreationOperations().map { it.toDto() }
            val updateOperations =
                destinationTempRepository.getUpdateOperations().map { it.toDto() }
            val deleteOperations =
                destinationTempRepository.getDeleteOperations().map { it.id }

            launch {
                if (createOperations.isNotEmpty()) {
                    destinationTempRepository.syncCreateOperations(createOperations)
                    destinationTempRepository.clearCreationOperations()
                }
            }

            launch {
                if (updateOperations.isNotEmpty()) {
                    destinationTempRepository.syncUpdateOperations(updateOperations)
                    destinationTempRepository.clearUpdateOperations()
                }
            }

            launch {
                if (deleteOperations.isNotEmpty()) {
                    destinationTempRepository.syncDeleteOperations(deleteOperations)
                    destinationTempRepository.clearDeleteOperations()
                }
            }
        }
    }
}