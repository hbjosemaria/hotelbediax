package com.joheba.hotelbediax.data.repository

import androidx.paging.PagingSource
import com.joheba.hotelbediax.data.model.external.DestinationDto
import com.joheba.hotelbediax.data.model.external.DestinationListResponseDto
import com.joheba.hotelbediax.data.model.local.DestinationEntity
import com.joheba.hotelbediax.data.service.external.ApiDestinationService
import com.joheba.hotelbediax.data.service.local.DestinationDao
import com.joheba.hotelbediax.data.service.local.HotelBediaXDatabase

//Both interfaces can be modified to manage also Language selection if needed
interface LocalDestinationRepository {
    suspend fun getAll() : PagingSource<Int, DestinationEntity>
    suspend fun deleteById(id: Int): Int
    suspend fun update(destination: DestinationEntity): Int
    suspend fun create(destination: DestinationEntity): Int
    suspend fun insertAll(destinationList: List<DestinationEntity>): Int
    suspend fun clearDestinations(): Int
}

interface ExternalDestinationRepository {
    suspend fun getAll(page: Int): DestinationListResponseDto
    suspend fun deleteById(id: Int): Boolean
    suspend fun update(id: Int, destination: DestinationDto): Boolean
    suspend fun create(destination: DestinationDto): Boolean
}

class LocalDestinationRepositoryImpl(
    private val roomService: DestinationDao
) : LocalDestinationRepository {
    override suspend fun getAll(): PagingSource<Int, DestinationEntity> =
        roomService.getAll()

    override suspend fun deleteById(id: Int): Int =
        roomService.deleteById(id)

    override suspend fun update(destination: DestinationEntity): Int =
        roomService.update(destination)

    override suspend fun create(destination: DestinationEntity): Int =
        roomService.create(destination)

    override suspend fun insertAll(destinationList: List<DestinationEntity>): Int =
        roomService.insertAll(destinationList)

    override suspend fun clearDestinations(): Int =
        roomService.clearDestinations()


}

class ExternalDestinationRepositoryImpl(
    private val apiService: ApiDestinationService
) : ExternalDestinationRepository {

    override suspend fun getAll(page: Int): DestinationListResponseDto =
        apiService.getAll(page)

    override suspend fun deleteById(id: Int): Boolean =
        apiService.deleteById(id)

    override suspend fun update(id: Int, destination: DestinationDto): Boolean =
        apiService.update(id, destination)

    override suspend fun create(destination: DestinationDto): Boolean =
        apiService.create(destination)

}