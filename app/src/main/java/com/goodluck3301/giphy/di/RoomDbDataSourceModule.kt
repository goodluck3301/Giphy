/*
 * Copyright (c) 2025. Levon M.
 * https://github.com/goodluck3301
 */

package com.goodluck3301.giphy.di

import com.goodluck3301.giphy.data.source.local.GiphyDatabase
import com.goodluck3301.giphy.data.source.local.RoomDatabaseDataSource
import com.goodluck3301.giphy.data.source.local.interfaces.DatabaseDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object RoomDbDataSourceModule {
    @ViewModelScoped
    @Provides
    fun provideRoomDbDataSource(
        giphyDatabase: GiphyDatabase,
    ): DatabaseDataSource {
        return RoomDatabaseDataSource(giphyDatabase = giphyDatabase)
    }
}
