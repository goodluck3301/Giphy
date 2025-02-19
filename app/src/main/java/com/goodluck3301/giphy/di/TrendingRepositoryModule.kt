/*
 * Copyright (c) 2025. Levon M.
 * https://github.com/goodluck3301
 */

package com.goodluck3301.giphy.di

import com.goodluck3301.giphy.data.repository.TrendingRepositoryImpl
import com.goodluck3301.giphy.data.source.local.interfaces.DatabaseDataSource
import com.goodluck3301.giphy.data.source.network.interfaces.NetworkDataSource
import com.goodluck3301.giphy.domain.repository.TrendingRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineDispatcher

@Module
@InstallIn(ViewModelComponent::class)
object TrendingRepositoryModule {
    @ViewModelScoped
    @Provides
    fun provideTrendingRepository(
        networkDataSource: NetworkDataSource,
        databaseDataSource: DatabaseDataSource,
        @GiphyApiKey giphyApiKey: String,
        @DispatcherModule.IoDispatcher dispatcher: CoroutineDispatcher,
    ): TrendingRepository {
        return TrendingRepositoryImpl(
            networkDataSource = networkDataSource,
            databaseDataSource = databaseDataSource,
            giphyApiKey = giphyApiKey,
            dispatcher = dispatcher,
        )
    }
}
