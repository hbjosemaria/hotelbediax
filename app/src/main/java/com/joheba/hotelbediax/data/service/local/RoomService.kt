package com.joheba.hotelbediax.data.service.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.Update
import com.joheba.hotelbediax.data.model.local.DestinationEntity
import com.joheba.hotelbediax.data.model.local.DestinationRemoteKeyEntity
import com.joheba.hotelbediax.data.model.local.DestinationTempEntity
import com.joheba.hotelbediax.data.service.local.converters.DestinationTempActionConverter
import com.joheba.hotelbediax.data.service.local.converters.DestinationTypeConverter
import com.joheba.hotelbediax.data.service.local.converters.LastModifyTypeConverter
import com.joheba.hotelbediax.domain.core.DestinationType
import kotlinx.coroutines.flow.Flow

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
    @Query(
        "SELECT * FROM destination WHERE (:id IS NULL OR id = :id) " +
                "AND (:name IS NULL OR name LIKE '%' || :name || '%') " +
                "AND (:description IS NULL OR description LIKE '%' || :description || '%') " +
                "AND (:type IS NULL OR type = :type) " +
                "AND (:countryCode IS NULL OR countryCode = :countryCode) " +
                "AND (:lastModify IS NULL OR lastModify like '%' || :lastModify || '%') ORDER BY id DESC"
    )
    fun getAll(
        id: Int?,
        name: String?,
        description: String?,
        type: DestinationType?,
        countryCode: String?,
        lastModify: String?,
    ): PagingSource<Int, DestinationEntity>

    @Query("SELECT * FROM destination WHERE id = :destinationId")
    suspend fun getDestinationById(destinationId: Int): DestinationEntity

    @Query("DELETE FROM destination WHERE id = :id")
    suspend fun deleteById(id: Int): Int

    @Update
    suspend fun update(destination: DestinationEntity): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun create(destination: DestinationEntity): Long

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

    @Query("select q1.* from destination_temp q1 where q1.`action` = 'CREATE' and q1.id not in (select q2.id from destination_temp q2 where q2.`action` = 'DELETE')")
    suspend fun getCreationOperations(): List<DestinationTempEntity>

    @Query("select q1.* from destination_temp q1 where `action` = 'UPDATE' and q1.id not in (select q2.id from destination_temp q2 where q2.`action` = 'DELETE')")
    suspend fun getUpdateOperations(): List<DestinationTempEntity>

    @Query("select q1.* from destination_temp q1 where q1.`action` = 'DELETE' and q1.id not in (select q2.id from destination_temp q2 where q2.`action` = 'CREATE')")
    suspend fun getDeleteOperations(): List<DestinationTempEntity>

    @Query("delete from destination_temp where `action` = 'CREATE'")
    suspend fun clearCreationOperations()

    @Query("delete from destination_temp where `action` = 'UPDATE'")
    suspend fun clearUpdateOperations()

    @Query("delete from destination_temp where `action` = 'DELETE'")
    suspend fun clearDeleteOperations()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addEnqueuedRecord(destinationTemp: DestinationTempEntity)

    @Query("select count(*) from destination_temp")
    fun pendingTempOperationsNumber(): Flow<Int>

}