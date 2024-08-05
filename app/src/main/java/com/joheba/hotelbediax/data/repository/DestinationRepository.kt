package com.joheba.hotelbediax.data.repository

import androidx.paging.PagingSource
import com.joheba.hotelbediax.data.model.external.DestinationDto
import com.joheba.hotelbediax.data.model.local.DestinationEntity
import com.joheba.hotelbediax.data.service.external.ApiDestinationService
import com.joheba.hotelbediax.data.service.local.DestinationDao
import com.joheba.hotelbediax.data.service.local.HotelBediaXDatabase

interface LocalDestinationRepository {
    suspend fun getAll() : PagingSource<Int, DestinationEntity>
    suspend fun deleteById(id: Int): Int
    suspend fun update(destination: DestinationEntity): Int
    suspend fun create(destination: DestinationEntity): Int
}

interface ExternalDestinationRepository {
    suspend fun getAll() : PagingSource<Int, DestinationDto>
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
        TODO("Not yet implemented")


    override suspend fun update(destination: DestinationEntity): Int =
        TODO("Not yet implemented")


    override suspend fun create(destination: DestinationEntity): Int =
        TODO("Not yet implemented")

}

class ExternalDestinationRepositoryImpl(
    private val apiService: ApiDestinationService
) : ExternalDestinationRepository {

    override suspend fun getAll(): PagingSource<Int, DestinationDto> =
        TODO("Not yet implemented")

    override suspend fun deleteById(id: Int): Boolean =
        TODO("Not yet implemented")

    override suspend fun update(id: Int, destination: DestinationDto): Boolean =
        TODO("Not yet implemented")

    override suspend fun create(destination: DestinationDto): Boolean =
        TODO("Not yet implemented")

}