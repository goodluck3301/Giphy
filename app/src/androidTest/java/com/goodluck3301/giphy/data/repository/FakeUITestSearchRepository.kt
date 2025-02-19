/*
 * Copyright (c) 2025. Levon M.
 * https://github.com/goodluck3301
 */

package com.goodluck3301.giphy.data.repository

import com.goodluck3301.giphy.domain.model.GifObject
import com.goodluck3301.giphy.domain.model.Rating
import com.goodluck3301.giphy.domain.repository.SearchRepository
import javax.inject.Inject

class FakeUITestSearchRepository @Inject constructor() : SearchRepository {
    private var searchResult: Result<List<GifObject>>? = null
    private var lastSuccessfulSearchKeyword: String? = null
    private var lastSuccessfulSearchResults: List<GifObject>? = null

    override suspend fun search(keyword: String?, limit: Int, rating: Rating): Result<List<GifObject>> {
        return searchResult ?: Result.success(emptyList())
    }

    override fun getLastSuccessfulSearchKeyword(): String? = lastSuccessfulSearchKeyword
    override fun getLastSuccessfulSearchResults(): List<GifObject>? = lastSuccessfulSearchResults

    fun setSearchResultForTest(searchResult: Result<List<GifObject>>?) {
        this.searchResult = searchResult
    }

    fun setLastSuccessfulSearchKeywordForTest(lastSuccessfulSearchKeyword: String?) {
        this.lastSuccessfulSearchKeyword = lastSuccessfulSearchKeyword
    }

    fun setLastSuccessfulSearchResultsForTest(lastSuccessfulSearchResults: List<GifObject>?) {
        this.lastSuccessfulSearchResults = lastSuccessfulSearchResults
    }
}
