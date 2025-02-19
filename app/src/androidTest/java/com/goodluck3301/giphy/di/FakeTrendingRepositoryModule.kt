/*
 * Copyright (c) 2025. Levon M.
 * https://github.com/goodluck3301
 */

package com.goodluck3301.giphy.di

import com.goodluck3301.giphy.data.repository.FakeUITestTrendingRepository
import com.goodluck3301.giphy.domain.repository.TrendingRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [TrendingRepositoryModule::class],
)
object FakeTrendingRepositoryModule {
    @Singleton
    @Provides
    fun provideTrendingRepository(): TrendingRepository {
        return FakeUITestTrendingRepository()
    }
}
