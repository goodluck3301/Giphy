/*
 * Copyright (c) 2025. Levon M.
 * https://github.com/goodluck3301
 */

package com.goodluck3301.giphy.data.source.preferences.interfaces

import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow

interface PreferencesDataStoreWrapper {
    val preferenceErrors: SharedFlow<Throwable>
    fun getDataStoreFlow(): Flow<Preferences>

    suspend fun <T> updatePreference(key: Preferences.Key<T>, newValue: T)

    suspend fun clear()
}
