/*
 * Copyright (c) 2025. Levon M.
 * https://github.com/goodluck3301
 */

package com.goodluck3301.giphy.domain.repository

import com.goodluck3301.giphy.domain.model.GifObject
import com.goodluck3301.giphy.domain.model.Rating

interface TrendingRepository {

    suspend fun fetchCachedTrending(): Result<List<GifObject>>
    suspend fun reloadTrending(limit: Int, rating: Rating): Result<List<GifObject>>
}
