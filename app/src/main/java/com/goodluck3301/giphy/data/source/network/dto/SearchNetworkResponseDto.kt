/*
 * Copyright (c) 2025. Levon M.
 * https://github.com/goodluck3301
 */

package com.goodluck3301.giphy.data.source.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchNetworkResponseDto(
    @SerialName(value = "data")
    val trendingData: List<TrendingDataDto>,
    @SerialName(value = "meta")
    val metaDto: MetaDto,
    @SerialName(value = "pagination")
    val pagination: PaginationDto,
)
