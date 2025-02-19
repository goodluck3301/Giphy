/*
 * Copyright (c) 2025. Levon M.
 * https://github.com/goodluck3301
 */

package com.goodluck3301.giphy.di

import com.goodluck3301.giphy.data.repository.FakeUITestSearchRepository
import com.goodluck3301.giphy.domain.repository.SearchRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [SearchRepositoryModule::class],
)
object FakeSearchRepositoryModule {
    @Singleton
    @Provides
    fun provideFakeSearchRepository(): SearchRepository {
        return FakeUITestSearchRepository()
    }
}
