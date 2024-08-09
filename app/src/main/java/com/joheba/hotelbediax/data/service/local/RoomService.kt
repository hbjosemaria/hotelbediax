package com.joheba.hotelbediax.data.service.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.Update
import androidx.sqlite.db.SimpleSQLiteQuery
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
import com.joheba.hotelbediax.domain.core.DestinationType
import java.time.LocalDateTime

@Database(
    entities = [
        DestinationEntity::class,
        DestinationTempEntity::class,
        DestinationRemoteKeyEntity::class
    ],
    version = 4,
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
interface DestinationDao {
    @Query("SELECT * FROM destination WHERE (:id IS NULL OR id = :id) " +
            "AND (:name IS NULL OR name LIKE '%' || :name || '%') " +
            "AND (:description IS NULL OR description LIKE '%' || :description || '%') " +
            "AND (:type IS NULL OR type = :type) " +
            "AND (:countryCode IS NULL OR countryCode = :countryCode) " +
            "AND (:lastModify IS NULL OR lastModify = :lastModify) ORDER BY id DESC")
    fun getAll(
        id: Int?,
        name: String?,
        description: String?,
        type: DestinationType?,
        countryCode: String?,
        lastModify: LocalDateTime?
    ): PagingSource<Int, DestinationEntity>

    @Query("SELECT * FROM destination WHERE id = :destinationId")
    suspend fun getDestinationById(destinationId: Int): DestinationEntity

    @Query("DELETE FROM destination WHERE id = :id")
    suspend fun deleteById(id: Int): Int

    @Update
    suspend fun update(destination: DestinationEntity): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun create(destination: DestinationEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(destinationList: List<DestinationEntity>)

    @Query("DELETE FROM destination")
    suspend fun clearDestinations(): Int

    @Query("SELECT id FROM destination ORDER BY id DESC LIMIT 1")
    suspend fun getLastId(): Int
}

@Dao
interface DestinationRemoteKeyDao {

    @Query("SELECT * FROM destination_remote_key WHERE destinationId = :destinationId")
    suspend fun getKey(destinationId: Int): DestinationRemoteKeyEntity

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(remoteKeyList: List<DestinationRemoteKeyEntity>)

    @Query("DELETE FROM destination_remote_key")
    suspend fun clearKeys(): Int
}

@Dao
interface LocalDestinationTempDao {

    @Query("SELECT * FROM destination_temp")
    suspend fun getAll(): List<DestinationTempEntity>

    @Query("DELETE FROM destination_temp")
    suspend fun clearAll()

    @Insert
    suspend fun addEnqueuedRecord(destinationTemp: DestinationTempEntity)

}