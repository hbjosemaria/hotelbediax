package com.joheba.hotelbediax.domain.usecase

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.joheba.hotelbediax.data.model.local.DestinationTempEntity
import com.joheba.hotelbediax.data.remotemediator.DestinationRemoteMediator
import com.joheba.hotelbediax.data.repository.ExternalDestinationRepository
import com.joheba.hotelbediax.data.repository.LocalDestinationRepository
import com.joheba.hotelbediax.data.repository.LocalDestinationTempRepository
import com.joheba.hotelbediax.domain.core.Destination
import com.joheba.hotelbediax.ui.main.destination.DestinationFilters
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class DestinationUseCase @Inject constructor(
    private val apiRepository: ExternalDestinationRepository,
    private val destinationRepository: LocalDestinationRepository,
    private val tempRepository: LocalDestinationTempRepository,
    private val remoteMediator: DestinationRemoteMediator,
) {

    fun getDestinations(filters: DestinationFilters): Flow<PagingData<Destination>> =
        Pager(
            //You can either adapt this RemoteMediator to a single page method fetching
            config = PagingConfig(
                pageSize = 200,
                prefetchDistance = 100,
                initialLoadSize = 400
            ),
            //Or to a full list fetching method, but by doing this, you have to assist the RemoteMediator with an AssistedInject including the Int value for the whole list size
            // In case you want to test this option, then
//            config = PagingConfig(
//                pageSize = 210000,
//                prefetchDistance = 200,
//                initialLoadSize = 210000
//            ),
            remoteMediator = remoteMediator,
            pagingSourceFactory = {
                //Data will be always presented from the single source of truth with a PagingSource
                destinationRepository.getAll(
                    filters
                )
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
        try {
            val newDestination = destination.copy(
                id = destinationRepository.getNewId()
            )
            if (destinationRepository.create(newDestination.toEntity()) > 0) {
                locallyInserted = true
            }
            apiRepository.create(newDestination.toDto())
        } catch (e: Exception) {
            tempRepository.addEnqueuedRecord(destination.toTempEntity(DestinationTempEntity.DestinationTempEntityAction.CREATE))
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
            tempRepository.addEnqueuedRecord(updatedDestination.toTempEntity(DestinationTempEntity.DestinationTempEntityAction.UPDATE))
        }
        return result
    }

    suspend fun deleteDestinationById(destination: Destination): Boolean {
        val result = destinationRepository.deleteById(destination.id) == 1
        try {
            apiRepository.deleteById(destination.id)
        } catch (e: Exception) {
            tempRepository.addEnqueuedRecord(destination.toTempEntity(DestinationTempEntity.DestinationTempEntityAction.DELETE))
        }
        return result
    }
}