/*
 * Copyright (c) 2025. Levon M.
 * https://github.com/goodluck3301
 */

package com.goodluck3301.giphy.di

import com.goodluck3301.giphy.data.source.network.CustomInstantSerializer
import com.goodluck3301.giphy.data.source.network.KtorNetworkDataSource
import com.goodluck3301.giphy.data.source.network.interfaces.NetworkDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkDataSourceModule {
    @Singleton
    @Provides
    fun provideNetworkDataSource(
        httpClient: HttpClient,
        @DispatcherModule.IoDispatcher dispatcher: CoroutineDispatcher,
    ): NetworkDataSource {
        return KtorNetworkDataSource(
            httpClient = httpClient,
            dispatcher = dispatcher,
        )
    }

    @Singleton
    @Provides
    fun provideKtorEngine(): HttpClientEngine {
        return CIO.create()
    }

    @Singleton
    @Provides
    fun provideHttpClient(engine: HttpClientEngine): HttpClient {
        return HttpClient(engine) {
            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                        prettyPrint = true
                        isLenient = true
                        serializersModule = SerializersModule {
                            contextual(CustomInstantSerializer)
                        }
                    },
                )
            }
        }
    }
}
