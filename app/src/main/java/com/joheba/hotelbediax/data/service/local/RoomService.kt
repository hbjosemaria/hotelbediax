package com.joheba.hotelbediax.data.service.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.Update
import com.joheba.hotelbediax.data.model.external.DestinationDto
import com.joheba.hotelbediax.data.model.local.DestinationEntity
import com.joheba.hotelbediax.data.model.local.DestinationRemoteKeyEntity
import com.joheba.hotelbediax.data.model.local.DestinationTempEntity
import com.joheba.hotelbediax.data.repository.DestinationRemoteKeyRepository
import com.joheba.hotelbediax.data.repository.ExternalDestinationTempRepository
import com.joheba.hotelbediax.data.repository.LocalDestinationTempRepository
import com.joheba.hotelbediax.data.repository.LocalDestinationRepository
import com.joheba.hotelbediax.data.service.local.converters.DestinationTempActionConverter
import com.joheba.hotelbediax.data.service.local.converters.DestinationTypeConverter
import com.joheba.hotelbediax.data.service.local.converters.LastModifyTypeConverter

@Database(
    entities = [
        DestinationEntity::class,
        DestinationTempEntity::class,
        DestinationRemoteKeyEntity::class
    ],
    version = 3,
)
@TypeConverters(
    DestinationTypeConverter::class,
    DestinationTempActionConverter::class,
    LastModifyTypeConverter::class
)
abstract class HotelBediaXDatabase : RoomDatabase() {
    abstract fun destinationDao(): DestinationDao
    abstract fun destinationTempDao(): LocalDestinationTempDao
    abstract fun destinationRemoteKeyDao(): DestinationRemoteKeyDao
}

@Dao
interface DestinationDao : LocalDestinationRepository {
    @Query("select * from destination order by id desc")
    override fun getAll(): PagingSource<Int, DestinationEntity>

    @Query("select * from destination where id = :destinationId")
    override suspend fun getDestinationById(destinationId: Int): DestinationEntity

    @Query("delete from destination where id = :id")
    override suspend fun deleteById(id: Int): Int

    @Update
    override suspend fun update(destination: DestinationEntity): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    override suspend fun create(destination: DestinationEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    override suspend fun insertAll(destinationList: List<DestinationEntity>)

    @Query("delete from destination")
    override suspend fun clearDestinations(): Int

    @Query("select id from destination order by id desc limit 1")
    override suspend fun getLastId(): Int
}

@Dao
interface DestinationRemoteKeyDao : DestinationRemoteKeyRepository {

    @Query("select * from destination_remote_key where destinationId = :destinationId")
    override suspend fun getKey(destinationId: Int): DestinationRemoteKeyEntity

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    override suspend fun insertAll(remoteKeyList: List<DestinationRemoteKeyEntity>)

    @Query("delete from destination_remote_key")
    override suspend fun clearKeys(): Int
}

@Dao
interface LocalDestinationTempDao : LocalDestinationTempRepository {

    @Query("select * from destination_temp")
    override suspend fun getAll(): List<DestinationTempEntity>

    @Query("delete from destination_temp")
    override suspend fun clearAll()

    @Insert
    override suspend fun addEnqueuedRecord(destinationTemp: DestinationTempEntity)

}