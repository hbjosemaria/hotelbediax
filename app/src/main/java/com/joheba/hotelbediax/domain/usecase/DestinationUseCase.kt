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
import com.joheba.hotelbediax.domain.core.DestinationType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class DestinationUseCase @Inject constructor(
    private val apiRepository: ExternalDestinationRepository,
    private val destinationRepository: LocalDestinationRepository,
    private val tempRepository: LocalDestinationTempRepository,
    private val remoteMediator: DestinationRemoteMediator
) {
    fun getDestinations(filter: DestinationFilterOptions? = null): Flow<PagingData<Destination>> =
        Pager(
            config = PagingConfig(
                pageSize = 20,
                prefetchDistance = 40,
                initialLoadSize = 60
            ),
            remoteMediator = remoteMediator,
            pagingSourceFactory = {
                destinationRepository.getAll()
            }
        ).flow
            .map { pagingData ->
                pagingData.filter { destinationEntity ->
                    when (filter) {
                        null -> true
                        DestinationFilterOptions.CITY -> {
                            destinationEntity.type == DestinationFilterOptions.CITY.destinationType
                        }
                        DestinationFilterOptions.COUNTRY -> {
                            destinationEntity.type == DestinationFilterOptions.COUNTRY.destinationType
                        }
                    }
                }.map {
                    it.toDomain()
                }
            }

    suspend fun getDestinationById(destinationId: Int): Destination? =
        destinationRepository.getDestinationById(destinationId)?.toDomain()

    suspend fun createDestination(destination: Destination): Boolean {
        val result = destinationRepository.create(destination.toEntity()) == 1
        try {
            apiRepository.create(destination.toDto())
        } catch (e: Exception) {
            tempRepository.addEnqueuedRecord(destination.toTempEntity(DestinationTempEntity.DestinationTempEntityAction.CREATE))
        }
        return result
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

    //TODO: add functions for filtering based in Destination parameters

}

//TODO: adapt this filter options to the other parameters that could have been used in a search
enum class DestinationFilterOptions(val destinationType: DestinationType) {
    CITY(DestinationType.CITY),
    COUNTRY(DestinationType.COUNTRY)
}