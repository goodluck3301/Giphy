/*
 * Copyright (c) 2025. Levon M.
 * https://github.com/goodluck3301
 */

package com.goodluck3301.giphy.data.repository

import com.goodluck3301.giphy.data.repository.mappers.toEntity
import com.goodluck3301.giphy.data.repository.mappers.toGifObject
import com.goodluck3301.giphy.data.source.local.interfaces.DatabaseDataSource
import com.goodluck3301.giphy.data.source.network.dto.TrendingNetworkResponseDto
import com.goodluck3301.giphy.data.source.network.interfaces.NetworkDataSource
import com.goodluck3301.giphy.di.DispatcherModule
import com.goodluck3301.giphy.di.GiphyApiKey
import com.goodluck3301.giphy.domain.exceptions.EmptyGiphyAPIKeyException
import com.goodluck3301.giphy.domain.exceptions.except
import com.goodluck3301.giphy.domain.model.GifObject
import com.goodluck3301.giphy.domain.model.Rating
import com.goodluck3301.giphy.domain.repository.TrendingRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class TrendingRepositoryImpl @Inject constructor(
    private val networkDataSource: NetworkDataSource,
    private val databaseDataSource: DatabaseDataSource,
    @GiphyApiKey private val giphyApiKey: String,
    @DispatcherModule.IoDispatcher private val dispatcher: CoroutineDispatcher,
) : TrendingRepository {
    override suspend fun fetchCachedTrending(): Result<List<GifObject>> {
        return withContext(dispatcher) {
            Result.runCatching {
                databaseDataSource.queryData().map { it.toGifObject() }
            }.except<CancellationException, _>()
        }
    }

    override suspend fun reloadTrending(limit: Int, rating: Rating): Result<List<GifObject>> {
        if (giphyApiKey.isBlank()) {
            @Suppress("UNREACHABLE_CODE")
            // It won't work without an API Key - CI might pass in nothing
            return Result.failure(exception = EmptyGiphyAPIKeyException())
        }

        return withContext(dispatcher) {
            Result.runCatching {
                databaseDataSource.markDirty()
                Timber.tag("refreshTrending").v("Mark dirty: success")

                val trendingNetworkResponse = getTrendingFromNetwork(limit = limit, rating = rating)
                databaseDataSource.insertAllData(data = trendingNetworkResponse.trendingData.map { it.toEntity() })
                Timber.tag("refreshTrending").v("Insertion completed")

                val invalidationResult = invalidateDirtyTrendingDb()
                invalidationResult.fold(
                    onSuccess = {
                        databaseDataSource.queryData().map { it.toGifObject() }
                    },
                    onFailure = { exception ->
                        Timber.tag("invalidationResult").e(exception)
                        throw exception
                    },
                )
            }.except<CancellationException, _>()
        }
    }

    private suspend fun getTrendingFromNetwork(limit: Int, rating: Rating): TrendingNetworkResponseDto {
        return withContext(dispatcher) {
            networkDataSource.getTrending(
                apiKey = giphyApiKey,
                limit = limit,
                offset = (0..5).random(), // Eye candie to make every refresh different
                rating = rating.apiValue,
            )
        }
    }

    private suspend fun invalidateDirtyTrendingDb(): Result<Unit> {
        return withContext(dispatcher) {
            Result.runCatching {
                databaseDataSource.deleteDirty()
                Timber.tag("invalidateDirtyTrendingDb").v("success")
            }.except<CancellationException, _>()
        }
    }
}
