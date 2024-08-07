package com.joheba.hotelbediax.data.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.joheba.hotelbediax.data.service.local.DestinationDao
import com.joheba.hotelbediax.data.service.local.DestinationRemoteKeyDao
import com.joheba.hotelbediax.data.service.local.HotelBediaXDatabase
import com.joheba.hotelbediax.data.service.local.LocalDestinationTempDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Provides
    @Singleton
    fun provideHotelBediaXDatabase(@ApplicationContext context: Context): HotelBediaXDatabase =
        Room
            .databaseBuilder(
                context = context,
                klass = HotelBediaXDatabase::class.java,
                name = "HotelBediaX"
            ).fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun provideDestinationDao(database: HotelBediaXDatabase): DestinationDao =
        database.destinationDao()

    @Provides
    @Singleton
    fun provideDestinationRemoteKeyDao(database: HotelBediaXDatabase): DestinationRemoteKeyDao =
        database.destinationRemoteKeyDao()

    @Provides
    @Singleton
    fun provideDestinationTempDao(database: HotelBediaXDatabase): LocalDestinationTempDao =
        database.destinationTempDao()

}