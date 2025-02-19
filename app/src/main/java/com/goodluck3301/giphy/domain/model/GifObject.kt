/*
 * Copyright (c) 2025. Levon M.
 * https://github.com/goodluck3301
 */

package com.goodluck3301.giphy.domain.model

data class GifObject(
    val id: String,
    val previewUrl: String,
    val previewHeight: Int,
    val previewWidth: Int,
    val imageUrl: String,
    val webUrl: String,
    val title: String,
    val type: String,
    val username: String,
)
