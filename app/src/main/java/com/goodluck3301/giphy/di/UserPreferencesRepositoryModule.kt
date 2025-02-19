/*
 * Copyright (c) 2025. Levon M.
 * https://github.com/goodluck3301
 */

package com.goodluck3301.giphy.di

import com.goodluck3301.giphy.data.repository.UserPreferencesRepositoryImpl
import com.goodluck3301.giphy.data.source.preferences.interfaces.PreferencesDataStoreWrapper
import com.goodluck3301.giphy.domain.repository.UserPreferencesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserPreferencesRepositoryModule {
    @Singleton
    @Provides
    fun provideUserPreferencesRepository(
        preferencesDataStoreWrapper: PreferencesDataStoreWrapper,
        externalCoroutineScope: CoroutineScope,
        @DispatcherModule.MainDispatcher dispatcher: CoroutineDispatcher,
    ): UserPreferencesRepository {
        return UserPreferencesRepositoryImpl(
            preferencesDataStoreWrapper = preferencesDataStoreWrapper,
            externalCoroutineScope = externalCoroutineScope,
            dispatcher = dispatcher,
        )
    }
}
