package com.joheba.hotelbediax.data.repository

import com.joheba.hotelbediax.data.model.external.DestinationDto
import com.joheba.hotelbediax.data.model.local.DestinationTempEntity
import com.joheba.hotelbediax.data.service.external.ApiDestinationService
import com.joheba.hotelbediax.data.service.local.LocalDestinationTempDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface LocalDestinationTempRepository {
    suspend fun getCreationOperations(): List<DestinationTempEntity>
    suspend fun getUpdateOperations(): List<DestinationTempEntity>
    suspend fun getDeleteOperations(): List<DestinationTempEntity>
    suspend fun clearCreationOperations()
    suspend fun clearUpdateOperations()
    suspend fun clearDeleteOperations()
    suspend fun pendingTempOperationsNumber(): Flow<Int>
    suspend fun addEnqueuedRecord(destinationTemp: DestinationTempEntity)
}

interface ExternalDestinationTempRepository {
    suspend fun syncCreateOperations(destinationList: List<DestinationDto>): Boolean
    suspend fun syncUpdateOperations(destinationList: List<DestinationDto>): Boolean
    suspend fun syncDeleteOperations(destinationList: List<Int>): Boolean
}

interface DestinationTempRepository : LocalDestinationTempRepository,
    ExternalDestinationTempRepository

class DestinationTempRepositoryImpl @Inject constructor(
    private val roomService: LocalDestinationTempDao,
    private val apiService: ApiDestinationService,
) : DestinationTempRepository {
    override suspend fun getCreationOperations(): List<DestinationTempEntity> =
        roomService.getCreationOperations()

    override suspend fun getUpdateOperations(): List<DestinationTempEntity> =
        roomService.getUpdateOperations()

    override suspend fun getDeleteOperations(): List<DestinationTempEntity> =
        roomService.getDeleteOperations()

    override suspend fun clearCreationOperations() =
        roomService.clearCreationOperations()

    override suspend fun clearUpdateOperations() =
        roomService.clearUpdateOperations()

    override suspend fun clearDeleteOperations() =
        roomService.clearDeleteOperations()

    override suspend fun pendingTempOperationsNumber(): Flow<Int> =
        roomService.pendingTempOperationsNumber()

    override suspend fun addEnqueuedRecord(destinationTemp: DestinationTempEntity) =
        roomService.addEnqueuedRecord(destinationTemp)

    override suspend fun syncCreateOperations(destinationList: List<DestinationDto>): Boolean =
        apiService.createBunch(destinationList)

    override suspend fun syncUpdateOperations(destinationList: List<DestinationDto>): Boolean =
        apiService.updateBunch(destinationList)

    override suspend fun syncDeleteOperations(destinationList: List<Int>): Boolean =
        apiService.deleteBunchById(destinationList)
}
