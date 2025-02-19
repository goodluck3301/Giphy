/*
 * Copyright (c) 2025. Levon M.
 * https://github.com/goodluck3301
 */

package com.goodluck3301.giphy.di

import android.content.Context
import androidx.room.Room
import com.goodluck3301.giphy.BuildConfig
import com.goodluck3301.giphy.data.source.local.GiphyDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object GiphyDatabaseModule {
    @ViewModelScoped
    @Provides
    fun provideDatabase(@ApplicationContext applicationContext: Context): GiphyDatabase {
        return Room.databaseBuilder(
            applicationContext,
            GiphyDatabase::class.java,
            BuildConfig.DATABASE_NAME,
        ).fallbackToDestructiveMigration().build()
    }
}
