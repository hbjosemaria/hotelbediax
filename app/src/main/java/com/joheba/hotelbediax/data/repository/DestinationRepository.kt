package com.joheba.hotelbediax.data.repository

import androidx.paging.PagingSource
import com.joheba.hotelbediax.data.model.external.DestinationDto
import com.joheba.hotelbediax.data.model.external.DestinationListResponseDto
import com.joheba.hotelbediax.data.model.local.DestinationEntity
import com.joheba.hotelbediax.data.service.external.ApiDestinationService
import com.joheba.hotelbediax.data.service.local.DestinationDao
import com.joheba.hotelbediax.ui.main.destination.DestinationFilters
import okhttp3.ResponseBody
import java.time.format.DateTimeFormatter
import javax.inject.Inject

//Both interfaces can be modified to manage also Language selection if needed
interface LocalDestinationRepository {
    fun getAll(
        filters: DestinationFilters,
    ): PagingSource<Int, DestinationEntity>

    suspend fun getDestinationById(destinationId: Int): DestinationEntity
    suspend fun deleteById(id: Int): Int
    suspend fun update(destination: DestinationEntity): Int
    suspend fun create(destination: DestinationEntity): Long
    suspend fun insertAll(destinationList: List<DestinationEntity>)
    suspend fun clearDestinations(): Int
    suspend fun getNewId(): Int
}

interface ExternalDestinationRepository {
    suspend fun getAll(): ResponseBody
    suspend fun getAll(page: Int): DestinationListResponseDto
    suspend fun deleteById(id: Int): Boolean
    suspend fun deleteBunchById(destinationIdList: List<Int>): Boolean
    suspend fun update(id: Int, destination: DestinationDto): Boolean
    suspend fun updateBunch(destinationList: List<DestinationDto>): Boolean
    suspend fun create(destination: DestinationDto): Boolean
    suspend fun createBunch(destinationList: List<DestinationDto>): Boolean
}

class LocalDestinationRepositoryImpl @Inject constructor(
    private val roomService: DestinationDao,
) : LocalDestinationRepository {
    override fun getAll(
        filters: DestinationFilters,
    ): PagingSource<Int, DestinationEntity> =
        roomService.getAll(
            id = filters.id,
            name = filters.name,
            description = filters.description,
            type = filters.type,
            countryCode = filters.countryCode,
            lastModify = filters.lastModify?.format(DateTimeFormatter.ISO_LOCAL_DATE)
        )

    override suspend fun getDestinationById(destinationId: Int): DestinationEntity =
        roomService.getDestinationById(destinationId)

    override suspend fun deleteById(id: Int): Int =
        roomService.deleteById(id)

    override suspend fun update(destination: DestinationEntity): Int =
        roomService.update(destination)

    override suspend fun create(destination: DestinationEntity): Long =
        roomService.create(destination)

    override suspend fun insertAll(destinationList: List<DestinationEntity>) =
        roomService.insertAll(destinationList)

    override suspend fun clearDestinations(): Int =
        roomService.clearDestinations()

    override suspend fun getNewId(): Int =
        roomService.getLastId() + 1

}

class ExternalDestinationRepositoryImpl @Inject constructor(
    private val apiService: ApiDestinationService,
) : ExternalDestinationRepository {

    override suspend fun getAll(): ResponseBody =
        apiService.getAll()

    override suspend fun getAll(page: Int): DestinationListResponseDto =
        apiService.getAll(page)

    override suspend fun deleteById(id: Int): Boolean =
        apiService.deleteById(id)

    override suspend fun deleteBunchById(destinationIdList: List<Int>): Boolean =
        apiService.deleteBunchById(destinationIdList)

    override suspend fun update(id: Int, destination: DestinationDto): Boolean =
        apiService.update(id, destination)

    override suspend fun updateBunch(destinationList: List<DestinationDto>): Boolean =
        apiService.updateBunch(destinationList)

    override suspend fun create(destination: DestinationDto): Boolean =
        apiService.create(destination)

    override suspend fun createBunch(destinationList: List<DestinationDto>): Boolean =
        apiService.createBunch(destinationList)

}