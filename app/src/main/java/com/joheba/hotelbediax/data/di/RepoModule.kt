package com.joheba.hotelbediax.data.di

import com.joheba.hotelbediax.data.repository.ExternalDestinationRepository
import com.joheba.hotelbediax.data.repository.ExternalDestinationRepositoryImpl
import com.joheba.hotelbediax.data.repository.LocalDestinationRepository
import com.joheba.hotelbediax.data.repository.LocalDestinationRepositoryImpl
import com.joheba.hotelbediax.data.service.external.ApiDestinationService
import com.joheba.hotelbediax.data.service.local.DestinationDao
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
    fun provideExternalDestinationRepository(
        apiService: ApiDestinationService
    ) : ExternalDestinationRepository =
        ExternalDestinationRepositoryImpl(apiService)
}