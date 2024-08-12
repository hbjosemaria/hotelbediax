package com.joheba.hotelbediax.data.di

import android.content.Context
import com.google.gson.GsonBuilder
import com.joheba.hotelbediax.BuildConfig
import com.joheba.hotelbediax.data.service.external.ApiDestinationService
import com.joheba.hotelbediax.data.service.external.ApiUrl
import com.joheba.hotelbediax.data.service.external.FakeBackendInterceptor
import com.joheba.hotelbediax.data.service.external.LocalDateTimeTypeAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDateTime
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    val gson = GsonBuilder()
        .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeTypeAdapter())
        .create()

    @Provides
    @Singleton
    fun provideOkHttpClient(@ApplicationContext context: Context): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(FakeBackendInterceptor(context))
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
    ): Retrofit {
        val retrofit = Retrofit.Builder()
            .baseUrl(ApiUrl.BASE.url)
            .addConverterFactory(GsonConverterFactory.create(gson))

        if (BuildConfig.DEBUG) {
            retrofit.client(okHttpClient)
        }

        return retrofit
            .build()
    }

    @Provides
    @Singleton
    fun provideDestinationService(
        retrofit: Retrofit,
    ): ApiDestinationService =
        retrofit.create(ApiDestinationService::class.java)
}