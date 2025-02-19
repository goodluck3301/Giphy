/*
 * Copyright (c) 2025. Levon M.
 * https://github.com/goodluck3301
 */

package com.goodluck3301.giphy.data.source.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.goodluck3301.giphy.data.source.preferences.interfaces.PreferencesDataStoreWrapper
import com.goodluck3301.giphy.di.DispatcherModule
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PreferencesDataStoreWrapperImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    @DispatcherModule.IoDispatcher private val dispatcher: CoroutineDispatcher,
) : PreferencesDataStoreWrapper {
    private val _preferenceErrors = MutableSharedFlow<Throwable>()
    override val preferenceErrors = _preferenceErrors.asSharedFlow()

    override fun getDataStoreFlow(): Flow<Preferences> = dataStore.data

    override suspend fun <T> updatePreference(key: Preferences.Key<T>, newValue: T) {
        withContext(dispatcher) {
            try {
                dataStore.edit { mutablePreferences ->
                    mutablePreferences[key] = newValue
                }
            } catch (e: Throwable) {
                _preferenceErrors.emit(e)
            }
        }
    }

    override suspend fun clear() {
        withContext(dispatcher) {
            try {
                dataStore.edit { mutablePreferences ->
                    mutablePreferences.clear()
                }
            } catch (e: Throwable) {
                _preferenceErrors.emit(e)
            }
        }
    }
}
