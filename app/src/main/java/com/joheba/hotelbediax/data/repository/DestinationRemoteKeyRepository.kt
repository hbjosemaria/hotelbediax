package com.joheba.hotelbediax.data.repository

import com.joheba.hotelbediax.data.model.local.DestinationRemoteKeyEntity
import com.joheba.hotelbediax.data.service.local.DestinationRemoteKeyDao
import javax.inject.Inject

interface DestinationRemoteKeyRepository {
    suspend fun getKey(destinationId: Int): DestinationRemoteKeyEntity?
    suspend fun insertAll(remoteKeyList: List<DestinationRemoteKeyEntity>)
    suspend fun clearKeys(): Int
}

class DestinationRemoteKeyRepositoryImpl @Inject constructor(
    private val roomService: DestinationRemoteKeyDao
) : DestinationRemoteKeyRepository {
    override suspend fun getKey(destinationId: Int): DestinationRemoteKeyEntity? =
        roomService.getKey(destinationId)

    override suspend fun insertAll(remoteKeyList: List<DestinationRemoteKeyEntity>) =
        roomService.insertAll(remoteKeyList)

    override suspend fun clearKeys(): Int =
        roomService.clearKeys()
}