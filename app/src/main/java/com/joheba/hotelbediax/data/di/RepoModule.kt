package com.joheba.hotelbediax.data.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.joheba.hotelbediax.data.repository.DataStoreRepository
import com.joheba.hotelbediax.data.repository.DataStoreRepositoryImpl
import com.joheba.hotelbediax.data.repository.DestinationRemoteKeyRepository
import com.joheba.hotelbediax.data.repository.DestinationRemoteKeyRepositoryImpl
import com.joheba.hotelbediax.data.repository.DestinationTempRepository
import com.joheba.hotelbediax.data.repository.DestinationTempRepositoryImpl
import com.joheba.hotelbediax.data.repository.ExternalDestinationRepository
import com.joheba.hotelbediax.data.repository.ExternalDestinationRepositoryImpl
import com.joheba.hotelbediax.data.repository.LocalDestinationRepository
import com.joheba.hotelbediax.data.repository.LocalDestinationRepositoryImpl
import com.joheba.hotelbediax.data.repository.LocalDestinationTempRepository
import com.joheba.hotelbediax.data.service.external.ApiDestinationService
import com.joheba.hotelbediax.data.service.local.DestinationDao
import com.joheba.hotelbediax.data.service.local.DestinationRemoteKeyDao
import com.joheba.hotelbediax.data.service.local.LocalDestinationTempDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepoModule {

    @Provides
    @Singleton
    fun provideLocalDestinationRepository(
        roomService: DestinationDao
    ) : LocalDestinationRepository =
        LocalDestinationRepositoryImpl(roomService)

    @Provides
    @Singleton
    fun provideDestinationTempRepository(
        apiService: ApiDestinationService,
        roomService: LocalDestinationTempDao
    ) : DestinationTempRepository =
        DestinationTempRepositoryImpl(roomService, apiService)

    @Provides
    @Singleton
    fun provideLocalDestinationTempRepository(
        apiService: ApiDestinationService,
        roomService: LocalDestinationTempDao
    ) : LocalDestinationTempRepository =
        DestinationTempRepositoryImpl(roomService, apiService)

    @Provides
    @Singleton
    fun provideExternalDestinationRepository(
        apiService: ApiDestinationService
    ) : ExternalDestinationRepository =
        ExternalDestinationRepositoryImpl(apiService)

    @Provides
    @Singleton
    fun provideDataStoreRepository(
        dataStore: DataStore<Preferences>
    ) : DataStoreRepository =
        DataStoreRepositoryImpl(dataStore)

    @Provides
    @Singleton
    fun provideDestinationRemoteKeyRepository(
        roomService: DestinationRemoteKeyDao
    ) : DestinationRemoteKeyRepository =
        DestinationRemoteKeyRepositoryImpl(roomService)

}