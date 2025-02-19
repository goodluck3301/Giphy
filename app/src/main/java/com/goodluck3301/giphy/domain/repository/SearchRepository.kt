/*
 * Copyright (c) 2025. Levon M.
 * https://github.com/goodluck3301
 */

package com.goodluck3301.giphy.domain.repository

import com.goodluck3301.giphy.domain.model.GifObject
import com.goodluck3301.giphy.domain.model.Rating

interface SearchRepository {

    suspend fun search(keyword: String?, limit: Int, rating: Rating): Result<List<GifObject>>
    fun getLastSuccessfulSearchKeyword(): String?
    fun getLastSuccessfulSearchResults(): List<GifObject>?
}
