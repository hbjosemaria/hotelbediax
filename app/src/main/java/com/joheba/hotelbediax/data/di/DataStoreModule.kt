package com.joheba.hotelbediax.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private const val PREFERENCES = "preferences"
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = PREFERENCES)

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> =
        context.dataStore

}

sealed class DataStoreVariableType {
    data object LongType : DataStoreVariableType()

}

enum class DataStoreVariableName(val variableName: String) {
    CACHE_TIMEOUT("cache_timeout")
}
