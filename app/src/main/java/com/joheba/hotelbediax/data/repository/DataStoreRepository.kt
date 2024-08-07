package com.joheba.hotelbediax.data.repository

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import com.joheba.hotelbediax.data.di.DataStoreVariableType
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface DataStoreRepository {
    suspend fun <T> saveData(dataType: DataStoreVariableType, dataName: String, data: T)
    suspend fun <T> loadData(dataRequest: Pair<DataStoreVariableType, String>): T?
}

@Suppress("UNCHECKED_CAST")
class DataStoreRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) : DataStoreRepository {

    override suspend fun <T> saveData(dataType: DataStoreVariableType, dataName: String, data: T) {
        dataStore.edit { preferences ->
            when (dataType) {
                DataStoreVariableType.LongType -> {
                    preferences[longPreferencesKey(dataName)] = data as Long
                }
            }
        }
    }

    override suspend fun <T> loadData(dataRequest: Pair<DataStoreVariableType, String>): T? {
        val (dataType, dataName) = dataRequest
        return dataStore.data.map { preferences ->
            when (dataType) {
                DataStoreVariableType.LongType -> {
                    preferences[longPreferencesKey(dataName)] as T?
                }
            }
        }
            .first()
    }
}