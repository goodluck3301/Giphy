/*
 * Copyright (c) 2025. Levon M.
 * https://github.com/goodluck3301
 */

package com.goodluck3301.giphy.di

import com.goodluck3301.giphy.data.repository.FakeUITestUserPreferencesRepository
import com.goodluck3301.giphy.domain.repository.UserPreferencesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [UserPreferencesRepositoryModule::class],
)
object FakeUserPreferencesRepositoryModule {
    @Singleton
    @Provides
    fun provideFakeUserPreferencesRepository(): UserPreferencesRepository {
        return FakeUITestUserPreferencesRepository()
    }
}
