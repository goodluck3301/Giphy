/*
 * Copyright (c) 2025. Levon M.
 * https://github.com/goodluck3301
 */

package com.goodluck3301.giphy.data.source.network

import com.goodluck3301.giphy.data.source.network.dto.MetaDto
import com.goodluck3301.giphy.data.source.network.dto.PaginationDto
import com.goodluck3301.giphy.data.source.network.dto.SearchNetworkResponseDto
import com.goodluck3301.giphy.data.source.network.dto.TrendingNetworkResponseDto
import com.goodluck3301.giphy.data.source.network.interfaces.NetworkDataSource

class FakeNetworkDataSource : NetworkDataSource {
    var apiError: Throwable? = null
    var trendingNetworkResponseDto: TrendingNetworkResponseDto? = null
    var searchNetworkResponseDto: SearchNetworkResponseDto? = null

    override suspend fun getTrending(
        apiKey: String,
        limit: Int,
        offset: Int,
        rating: String,
    ): TrendingNetworkResponseDto {
        apiError?.run { throw this }
        return trendingNetworkResponseDto ?: TrendingNetworkResponseDto(
            metaDto = MetaDto(
                msg = "OK",
                responseId = "g52knjbgx1u2x935oeod0s77dq4wd8dozlcs1xgm",
                status = 200,
            ),
            trendingData = emptyList(),
            pagination = PaginationDto(
                count = 50,
                offset = 0,
                totalCount = 2809,
            ),
        )
    }

    override suspend fun getSearch(apiKey: String, keyword: String, limit: Int, offset: Int, rating: String): SearchNetworkResponseDto {
        apiError?.run { throw this }
        return searchNetworkResponseDto ?: SearchNetworkResponseDto(
            metaDto = MetaDto(
                msg = "OK",
                responseId = "g52knjbgx1u2x935oeod0s77dq4wd8dozlcs1xgm",
                status = 200,
            ),
            trendingData = emptyList(),
            pagination = PaginationDto(
                count = 50,
                offset = 0,
                totalCount = 2809,
            ),
        )
    }
}
