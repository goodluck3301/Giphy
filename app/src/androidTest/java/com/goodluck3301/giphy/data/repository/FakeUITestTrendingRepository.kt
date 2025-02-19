/*
 * Copyright (c) 2025. Levon M.
 * https://github.com/goodluck3301
 */

package com.goodluck3301.giphy.data.repository

import com.goodluck3301.giphy.domain.model.GifObject
import com.goodluck3301.giphy.domain.model.Rating
import com.goodluck3301.giphy.domain.repository.TrendingRepository
import javax.inject.Inject

class FakeUITestTrendingRepository @Inject constructor() : TrendingRepository {
    private var trendingResult: Result<List<GifObject>>? = null

    override suspend fun fetchCachedTrending(): Result<List<GifObject>> {
        return trendingResult ?: Result.success(emptyList())
    }

    override suspend fun reloadTrending(limit: Int, rating: Rating): Result<List<GifObject>> {
        return trendingResult ?: Result.success(emptyList())
    }

    fun setTrendingResultForTest(trendingResult: Result<List<GifObject>>?) {
        this.trendingResult = trendingResult
    }
}
