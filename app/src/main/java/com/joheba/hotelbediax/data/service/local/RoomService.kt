package com.joheba.hotelbediax.data.service.local

import androidx.paging.PagingSource
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.Update
import com.joheba.hotelbediax.data.model.local.DestinationEntity
import com.joheba.hotelbediax.data.model.local.DestinationRemoteKeyEntity
import com.joheba.hotelbediax.data.repository.DestinationRemoteKeyRepository
import com.joheba.hotelbediax.data.repository.LocalDestinationRepository

@Database(
    entities = [
        DestinationEntity::class,
        DestinationRemoteKeyEntity::class
    ],
    version = 1,
    autoMigrations = []
)

abstract class HotelBediaXDatabase : RoomDatabase() {
    abstract fun destinationDao(): DestinationDao
    abstract fun destinationRemoteKeyDao(): DestinationRemoteKeyDao
}

interface DestinationDao : LocalDestinationRepository {
    @Query("select * from destination order by id desc")
    override suspend fun getAll(): PagingSource<Int, DestinationEntity>

    @Delete
    override suspend fun deleteById(id: Int): Int

    @Update
    override suspend fun update(destination: DestinationEntity): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    override suspend fun create(destination: DestinationEntity): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    override suspend fun insertAll(destinationList: List<DestinationEntity>): Int

    @Query("delete from destination")
    override suspend fun clearDestinations(): Int
}

interface DestinationRemoteKeyDao : DestinationRemoteKeyRepository {

    @Query("select * from destination_remote_key where destinationId = :destinationId")
    override suspend fun getKey(destinationId: Int): DestinationRemoteKeyEntity?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    override suspend fun insertAll(remoteKeyList: List<DestinationRemoteKeyEntity>): Int

    @Query("delete from destination_remote_key")
    override suspend fun clearKeys(): Int
}