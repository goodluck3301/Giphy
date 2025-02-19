/*
 * Copyright (c) 2025. Levon M.
 * https://github.com/goodluck3301
 */

package com.goodluck3301.giphy.di

import android.content.Context
import coil.ImageLoader
import com.goodluck3301.giphy.ui.test.FakeImageLoader
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [CoilModule::class],
)
object FakeCoilModule {
    @Singleton
    @Provides
    fun provideFakeCoilImageLoader(@ApplicationContext context: Context): ImageLoader {
        return FakeImageLoader(context = context)
    }
}
