package com.joheba.hotelbediax.data.repository

import com.joheba.hotelbediax.data.model.external.DestinationDto
import com.joheba.hotelbediax.data.model.local.DestinationTempEntity
import com.joheba.hotelbediax.data.service.local.LocalDestinationTempDao
import javax.inject.Inject

interface LocalDestinationTempRepository {
    suspend fun getAll(): List<DestinationTempEntity>
    suspend fun clearAll()
    suspend fun addEnqueuedRecord(destinationTemp: DestinationTempEntity)
}

interface ExternalDestinationTempRepository {
    suspend fun syncCreate(destinationList: List<DestinationDto>): Boolean
    suspend fun syncUpdate(destinationList: List<DestinationDto>): Boolean
    suspend fun syncDelete(destinationList: List<DestinationDto>): Boolean
}

interface DestinationTempRepository : LocalDestinationTempRepository, ExternalDestinationTempRepository

class DestinationTempRepositoryImpl @Inject constructor(
    private val roomService: LocalDestinationTempDao,
    private val apiService: ExternalDestinationRepository
) : DestinationTempRepository {
    override suspend fun getAll(): List<DestinationTempEntity> =
        roomService.getAll()

    override suspend fun clearAll() =
        roomService.clearAll()

    override suspend fun addEnqueuedRecord(destinationTemp: DestinationTempEntity) =
        roomService.addEnqueuedRecord(destinationTemp)

    override suspend fun syncCreate(destinationList: List<DestinationDto>): Boolean =
        apiService.createBunch(destinationList)

    override suspend fun syncUpdate(destinationList: List<DestinationDto>): Boolean =
        apiService.updateBunch(destinationList)

    override suspend fun syncDelete(destinationList: List<DestinationDto>): Boolean =
        apiService.deleteBunchById(destinationList.map{it.id})
}