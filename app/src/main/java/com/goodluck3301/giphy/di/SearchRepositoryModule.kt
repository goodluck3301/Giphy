/*
 * Copyright (c) 2025. Levon M.
 * https://github.com/goodluck3301
 */

package com.goodluck3301.giphy.di

import com.goodluck3301.giphy.data.repository.SearchRepositoryImpl
import com.goodluck3301.giphy.data.source.network.interfaces.NetworkDataSource
import com.goodluck3301.giphy.domain.repository.SearchRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SearchRepositoryModule {
    @Singleton
    @Provides
    fun provideSearchRepository(
        networkDataSource: NetworkDataSource,
        @GiphyApiKey giphyApiKey: String,
        @DispatcherModule.IoDispatcher dispatcher: CoroutineDispatcher,
    ): SearchRepository {
        return SearchRepositoryImpl(
            networkDataSource = networkDataSource,
            giphyApiKey = giphyApiKey,
            dispatcher = dispatcher,
        )
    }
}
