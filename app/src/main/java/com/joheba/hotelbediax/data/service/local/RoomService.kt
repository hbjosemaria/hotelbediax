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
import com.joheba.hotelbediax.data.repository.LocalDestinationRepository

@Database(
    entities = [DestinationEntity::class],
    version = 1,
    autoMigrations = []
)

abstract class HotelBediaXDatabase : RoomDatabase() {
    abstract fun destinationDao(): DestinationDao
}

interface DestinationDao : LocalDestinationRepository {
    @Query("select * from destination order by id desc")
    override suspend fun getAll(): PagingSource<Int, DestinationEntity>

    @Delete
    override suspend fun deleteById(id: Int): Int

    @Update
    override suspend fun update(destination: DestinationEntity): Int

    @Insert(onConflict = OnConflictStrategy.ABORT)
    override suspend fun create(destination: DestinationEntity): Int
}