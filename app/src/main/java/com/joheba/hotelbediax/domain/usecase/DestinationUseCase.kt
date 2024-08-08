package com.joheba.hotelbediax.domain.usecase

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.filter
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
                pageSize = 100,
                prefetchDistance = 50,
                initialLoadSize = 200
            ),
            //Or to a full list fetching method, but by doing this, you have to assist the RemoteMediator with an AssistedInject including the Int value for the whole list size
//            config = PagingConfig(
//                pageSize = 210000,
//                prefetchDistance = 50,
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
        return try {
            destinationRepository.create(destination.toEntity())
            apiRepository.create(destination.toDto())
        } catch (e: Exception) {
            tempRepository.addEnqueuedRecord(destination.toTempEntity(DestinationTempEntity.DestinationTempEntityAction.CREATE))
            false
        }
    }

    suspend fun updateDestination(destination: Destination): Boolean {
        val result = destinationRepository.update(destination.toEntity()) == 1
        try {
            apiRepository.update(destination.id, destination.toDto())
        } catch (e: Exception) {
            tempRepository.addEnqueuedRecord(destination.toTempEntity(DestinationTempEntity.DestinationTempEntityAction.UPDATE))
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