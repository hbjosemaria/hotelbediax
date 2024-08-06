package com.joheba.hotelbediax.data.repository

import com.joheba.hotelbediax.data.model.local.DestinationRemoteKeyEntity
import com.joheba.hotelbediax.data.service.local.DestinationRemoteKeyDao

interface DestinationRemoteKeyRepository {
    suspend fun getKey(destinationId: Int): DestinationRemoteKeyEntity?
    suspend fun insertAll(remoteKeyList: List<DestinationRemoteKeyEntity>): Int
    suspend fun clearKeys(): Int
}

class DestinationRemoteKeyRepositoryImpl(
    private val roomService: DestinationRemoteKeyDao
) : DestinationRemoteKeyRepository {
    override suspend fun getKey(destinationId: Int): DestinationRemoteKeyEntity? =
        roomService.getKey(destinationId)

    override suspend fun insertAll(remoteKeyList: List<DestinationRemoteKeyEntity>): Int =
        roomService.insertAll(remoteKeyList)

    override suspend fun clearKeys(): Int =
        roomService.clearKeys()
}