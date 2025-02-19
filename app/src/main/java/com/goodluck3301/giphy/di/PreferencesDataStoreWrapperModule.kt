/*
 * Copyright (c) 2025. Levon M.
 * https://github.com/goodluck3301
 */

package com.goodluck3301.giphy.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.goodluck3301.giphy.data.source.preferences.PreferencesDataStoreWrapperImpl
import com.goodluck3301.giphy.data.source.preferences.interfaces.PreferencesDataStoreWrapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PreferencesDataStoreWrapperModule {
    @Singleton
    @Provides
    fun providePreferenceDataStoreWrapper(
        dataStore: DataStore<Preferences>,
        @DispatcherModule.IoDispatcher dispatcher: CoroutineDispatcher,
    ): PreferencesDataStoreWrapper {
        return PreferencesDataStoreWrapperImpl(
            dataStore = dataStore,
            dispatcher = dispatcher,
        )
    }
}
