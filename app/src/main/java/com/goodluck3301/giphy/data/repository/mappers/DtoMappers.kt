/*
 * Copyright (c) 2025. Levon M.
 * https://github.com/goodluck3301
 */

package com.goodluck3301.giphy.data.repository.mappers

import com.goodluck3301.giphy.data.source.local.model.TrendingEntity
import com.goodluck3301.giphy.data.source.network.dto.TrendingDataDto
import com.goodluck3301.giphy.domain.model.GifObject

fun TrendingDataDto.toEntity() = TrendingEntity(
    id = this.id,
    previewUrl = urlCleanUp(this.images.fixedWidth.url),
    previewHeight = this.images.fixedWidth.height.toInt(),
    previewWidth = this.images.fixedWidth.width.toInt(),
    imageUrl = urlCleanUp(this.images.original.url),
    webUrl = this.url,
    title = this.title,
    type = this.type,
    username = this.username,
    trendingDateTime = this.trendingDatetime,
    importDateTime = this.importDatetime,
)

fun TrendingDataDto.toGifObject() = GifObject(
    id = this.id,
    previewUrl = urlCleanUp(this.images.fixedWidth.url),
    previewHeight = this.images.fixedWidth.height.toInt(),
    previewWidth = this.images.fixedWidth.width.toInt(),
    imageUrl = urlCleanUp(this.images.original.url),
    webUrl = this.url,
    title = this.title,
    type = this.type,
    username = this.username,
)

/**
 * The image URL returned by the server contains tracking code.
 * Trying to remove it to avoid unnecessary cache invalidation (experimental)
 */
private fun urlCleanUp(url: String): String {
    return url.substringBefore("?")
}
