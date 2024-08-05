package com.joheba.hotelbediax.data.di

import com.joheba.hotelbediax.data.service.external.ApiDestinationService
import com.joheba.hotelbediax.data.service.external.ApiUrl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl(ApiUrl.BASE.url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideDestinationService(
        retrofit: Retrofit
    ): ApiDestinationService =
        retrofit.create(ApiDestinationService::class.java)

}