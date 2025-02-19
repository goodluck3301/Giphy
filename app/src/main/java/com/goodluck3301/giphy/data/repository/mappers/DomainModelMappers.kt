/*
 * Copyright (c) 2025. Levon M.
 * https://github.com/goodluck3301
 */

package com.goodluck3301.giphy.data.repository.mappers

import com.goodluck3301.giphy.data.source.local.model.TrendingEntity
import com.goodluck3301.giphy.domain.model.GifObject

fun TrendingEntity.toGifObject() = GifObject(
    id = this.id,
    previewUrl = this.previewUrl,
    previewHeight = this.previewHeight,
    previewWidth = this.previewWidth,
    imageUrl = this.imageUrl,
    webUrl = this.webUrl,
    title = this.title,
    type = this.type,
    username = this.username,
)
