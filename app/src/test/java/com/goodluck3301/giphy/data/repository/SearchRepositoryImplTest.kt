/*
 * Copyright (c) 2025. Levon M.
 * https://github.com/goodluck3301
 */

package com.goodluck3301.giphy.data.repository

import com.goodluck3301.giphy.data.repository.mappers.toGifObject
import com.goodluck3301.giphy.data.source.network.FakeNetworkDataSource
import com.goodluck3301.giphy.domain.exceptions.EmptyGiphyAPIKeyException
import com.goodluck3301.giphy.domain.model.Rating
import com.goodluck3301.giphy.domain.repository.SearchRepository
import com.goodluck3301.giphy.test.testdata.SampleSearchNetworkResponse
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test

@ExperimentalCoroutinesApi
class SearchRepositoryImplTest {
    private lateinit var searchRepository: SearchRepository
    private lateinit var dispatcher: TestDispatcher
    private lateinit var fakeNetworkDataSource: FakeNetworkDataSource

    private fun setupRepository(giphyApiKey: String = "some-api-key") {
        dispatcher = UnconfinedTestDispatcher()
        fakeNetworkDataSource = FakeNetworkDataSource()
        searchRepository = SearchRepositoryImpl(
            networkDataSource = fakeNetworkDataSource,
            giphyApiKey = giphyApiKey,
            dispatcher = dispatcher,
        )
    }

    // Test function names reviewed by ChatGPT for consistency

    @Test
    fun search_WithNullKeyword_ShouldReturnEmptyResult() = runTest {
        setupRepository()
        fakeNetworkDataSource.searchNetworkResponseDto = SampleSearchNetworkResponse.singleResponse
        val keyword: String? = null

        val searchResult = searchRepository.search(keyword = keyword, limit = 100, rating = Rating.R)

        searchResult.isSuccess shouldBe true
        searchResult.getOrNull() shouldBe emptyList()
    }

    @Test
    fun search_WithEmptyKeyword_ShouldReturnEmptyResult() = runTest {
        setupRepository()
        fakeNetworkDataSource.searchNetworkResponseDto = SampleSearchNetworkResponse.singleResponse
        val keyword: String? = ""

        val searchResult = searchRepository.search(keyword = keyword, limit = 100, rating = Rating.R)

        searchResult.isSuccess shouldBe true
        searchResult.getOrNull() shouldBe emptyList()
    }

    @Test
    fun search_WithBlankKeyword_ShouldReturnEmptyResult() = runTest {
        setupRepository()
        fakeNetworkDataSource.searchNetworkResponseDto = SampleSearchNetworkResponse.singleResponse
        val keyword = "     "

        val searchResult = searchRepository.search(keyword = keyword, limit = 100, rating = Rating.R)

        searchResult.isSuccess shouldBe true
        searchResult.getOrNull() shouldBe emptyList()
    }

    @Test
    fun search_WithValidKeyword_ShouldReturnSuccessfulResult() = runTest {
        setupRepository()
        fakeNetworkDataSource.searchNetworkResponseDto = SampleSearchNetworkResponse.singleResponse
        val keyword = "some keyword"

        val searchResult = searchRepository.search(keyword = keyword, limit = 100, rating = Rating.R)

        searchResult.isSuccess shouldBe true
        searchResult.getOrNull() shouldBe SampleSearchNetworkResponse.singleResponse.trendingData.map { it.toGifObject() }
    }

    @Test
    fun search_WithBlankApiKey_ShouldThrowEmptyGiphyAPIKeyException() = runTest {
        setupRepository(giphyApiKey = "")
        fakeNetworkDataSource.searchNetworkResponseDto = SampleSearchNetworkResponse.singleResponse

        val searchResult = searchRepository.search(keyword = "some-keyword", limit = 100, rating = Rating.R)

        searchResult.isFailure shouldBe true
        searchResult.exceptionOrNull() shouldBe EmptyGiphyAPIKeyException()
    }

    @Test
    fun search_WithNetworkError_ShouldReturnFailure() = runTest {
        setupRepository()
        fakeNetworkDataSource.apiError = Exception()

        val searchResult = searchRepository.search(keyword = "some-keyword", limit = 100, rating = Rating.R)

        searchResult.isFailure shouldBe true
        searchResult.exceptionOrNull() shouldBe Exception()
    }

    // lastSuccessfulSearchResults
    @Test
    fun getLastSuccessfulSearchKeyword_WithoutSuccessfulSearch_ShouldReturnNull() {
        setupRepository()
        val lastSuccessfulSearchKeyword = searchRepository.getLastSuccessfulSearchKeyword()
        lastSuccessfulSearchKeyword shouldBe null
    }

    @Test
    fun getLastSuccessfulSearchResults_WithoutSuccessfulSearch_ShouldReturnNull() {
        setupRepository()
        val lastSuccessfulSearchResults = searchRepository.getLastSuccessfulSearchResults()
        lastSuccessfulSearchResults shouldBe null
    }

    @Test
    fun getLastSuccessfulSearch_WithSuccessfulSearch_ShouldReturnKeywordAndResults() = runTest {
        setupRepository()
        fakeNetworkDataSource.searchNetworkResponseDto = SampleSearchNetworkResponse.singleResponse
        val keyword = "some keyword"
        searchRepository.search(keyword = keyword, limit = 100, rating = Rating.R)

        val lastSuccessfulSearchKeyword = searchRepository.getLastSuccessfulSearchKeyword()
        val lastSuccessfulSearchResults = searchRepository.getLastSuccessfulSearchResults()
        lastSuccessfulSearchKeyword shouldBe keyword
        lastSuccessfulSearchResults shouldBe SampleSearchNetworkResponse.singleResponse.trendingData.map { it.toGifObject() }
    }

    @Test
    fun getLastSuccessfulSearch_AfterFailedSearch_ShouldMaintainPreviousSuccess() = runTest {
        setupRepository()
        fakeNetworkDataSource.searchNetworkResponseDto = SampleSearchNetworkResponse.singleResponse
        val keyword = "some keyword"
        searchRepository.search(keyword = keyword, limit = 100, rating = Rating.R)
        fakeNetworkDataSource.apiError = Exception()
        searchRepository.search(keyword = "failed keyword", limit = 100, rating = Rating.R)

        val lastSuccessfulSearchKeyword = searchRepository.getLastSuccessfulSearchKeyword()
        val lastSuccessfulSearchResults = searchRepository.getLastSuccessfulSearchResults()
        lastSuccessfulSearchKeyword shouldBe keyword
        lastSuccessfulSearchResults shouldBe SampleSearchNetworkResponse.singleResponse.trendingData.map { it.toGifObject() }
    }
}
