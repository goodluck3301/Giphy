/*
 * Copyright (c) 2025. Levon M.
 * https://github.com/goodluck3301
 */

package com.goodluck3301.giphy.data.source.network.interfaces

import com.goodluck3301.giphy.data.source.network.dto.SearchNetworkResponseDto
import com.goodluck3301.giphy.data.source.network.dto.TrendingNetworkResponseDto

interface NetworkDataSource {
    suspend fun getTrending(apiKey: String, limit: Int, offset: Int, rating: String): TrendingNetworkResponseDto
    suspend fun getSearch(apiKey: String, keyword: String, limit: Int, offset: Int, rating: String): SearchNetworkResponseDto
}
