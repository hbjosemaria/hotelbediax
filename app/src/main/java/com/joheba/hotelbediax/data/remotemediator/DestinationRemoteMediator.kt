package com.joheba.hotelbediax.data.remotemediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.joheba.hotelbediax.data.di.DataStoreVariableName
import com.joheba.hotelbediax.data.di.DataStoreVariableType
import com.joheba.hotelbediax.data.model.local.DestinationEntity
import com.joheba.hotelbediax.data.model.local.DestinationRemoteKeyEntity
import com.joheba.hotelbediax.data.repository.DataStoreRepository
import com.joheba.hotelbediax.data.repository.DestinationRemoteKeyRepository
import com.joheba.hotelbediax.data.repository.ExternalDestinationRepository
import com.joheba.hotelbediax.data.service.local.HotelBediaXDatabase
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@ExperimentalPagingApi
class DestinationRemoteMediator @Inject constructor(
    private val database: HotelBediaXDatabase,
    private val externalDestinationRepository: ExternalDestinationRepository,
    private val dataStoreRepository: DataStoreRepository,
    private val remoteKeyRepository: DestinationRemoteKeyRepository,
) : RemoteMediator<Int, DestinationEntity>() {

    override suspend fun initialize(): InitializeAction {

        //Added cache timeout in case we need to invalidate the cached data for renewing it.
        // Feel free to adjust the cache timeout timer or set it to a specific daily time
        val cacheTimeoutAmount = TimeUnit.HOURS.toMillis(6)
        val currentTime = System.currentTimeMillis()
        val lastCacheTimeout = dataStoreRepository.loadData<Long>(
            Pair(
                DataStoreVariableType.LongType,
                DataStoreVariableName.CACHE_TIMEOUT.variableName
            )
        )

        //Use this line if you want to force a cache invalidation
//        val lastCacheTimeout : Long? = null

        return when {
            lastCacheTimeout == null -> {
                dataStoreRepository.saveData(
                    dataType = DataStoreVariableType.LongType,
                    dataName = DataStoreVariableName.CACHE_TIMEOUT.variableName,
                    data = currentTime + cacheTimeoutAmount
                )
                InitializeAction.LAUNCH_INITIAL_REFRESH
            }

            currentTime > lastCacheTimeout -> {
                database.withTransaction {
                    dataStoreRepository.saveData(
                        dataType = DataStoreVariableType.LongType,
                        dataName = DataStoreVariableName.CACHE_TIMEOUT.variableName,
                        data = currentTime + cacheTimeoutAmount
                    )
                }
                InitializeAction.LAUNCH_INITIAL_REFRESH
            }

            currentTime <= lastCacheTimeout -> InitializeAction.SKIP_INITIAL_REFRESH
            else -> InitializeAction.LAUNCH_INITIAL_REFRESH
        }
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, DestinationEntity>,
    ): MediatorResult {
        val destinationDao = database.destinationDao()

        val page = when (loadType) {
            LoadType.REFRESH -> 1
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> {
                val lastItem = state.lastItemOrNull()
                lastItem?.let {
                    remoteKeyRepository.getKey(lastItem.id)?.nextKey
                        ?: return MediatorResult.Success(endOfPaginationReached = true)
                } ?: return MediatorResult.Success(endOfPaginationReached = true)
            }
        }

        //You can either test this by using the single page fetching method
        val result = externalDestinationRepository.getAll(page)

        //Or the full list fetching method
//        val result = externalDestinationRepository.getAll()

        val destinationList = result.results.map { destinationDto ->
            destinationDto.toEntity()
        }

        val destinationRemoteKeyList = destinationList.map { destinationEntity ->
            DestinationRemoteKeyEntity(
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (page == result.totalPages) null else result.page + 1,
                destinationId = destinationEntity.id
            )
        }
        database.withTransaction {
            //If you want to prevent data from being wiped after cache invalidation, then comment this conditional
            if (loadType == LoadType.REFRESH) {
                destinationDao.clearDestinations()
                remoteKeyRepository.clearKeys()
            }
            destinationDao.insertAll(destinationList)
            remoteKeyRepository.insertAll(destinationRemoteKeyList)
        }

        return MediatorResult.Success(endOfPaginationReached = page == result.totalPages)
    }
}