/*
 * Copyright (c) 2025. Levon M.
 * https://github.com/goodluck3301
 */

package com.goodluck3301.giphy.data.repository

import com.goodluck3301.giphy.data.repository.mappers.toGifObject
import com.goodluck3301.giphy.data.source.local.FakeDatabaseDataSource
import com.goodluck3301.giphy.data.source.network.FakeNetworkDataSource
import com.goodluck3301.giphy.domain.exceptions.EmptyGiphyAPIKeyException
import com.goodluck3301.giphy.domain.model.Rating
import com.goodluck3301.giphy.domain.repository.TrendingRepository
import com.goodluck3301.giphy.test.testdata.SampleTrendingEntityList
import com.goodluck3301.giphy.test.testdata.SampleTrendingNetworkResponse
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test

@ExperimentalCoroutinesApi
class TrendingRepositoryImplTest {
    private lateinit var trendingRepository: TrendingRepository
    private lateinit var dispatcher: TestDispatcher
    private lateinit var fakeRoomDbDataSource: FakeDatabaseDataSource
    private lateinit var fakeNetworkDataSource: FakeNetworkDataSource

    private fun setupRepository(giphyApiKey: String = "some-api-key") {
        dispatcher = UnconfinedTestDispatcher()
        fakeRoomDbDataSource = FakeDatabaseDataSource()
        fakeNetworkDataSource = FakeNetworkDataSource()
        trendingRepository = TrendingRepositoryImpl(
            networkDataSource = fakeNetworkDataSource,
            databaseDataSource = fakeRoomDbDataSource,
            giphyApiKey = giphyApiKey,
            dispatcher = dispatcher,
        )
    }

    // Test function names reviewed by ChatGPT for consistency

    @Test
    fun fetchCachedTrending_ShouldReturnCorrectList_WhenDatabaseQuerySucceeds() = runTest {
        setupRepository()
        fakeRoomDbDataSource.queryDataResponse = SampleTrendingEntityList.singleEntityList

        val result = trendingRepository.fetchCachedTrending()

        result.isSuccess shouldBe true
        result.getOrNull() shouldBe SampleTrendingEntityList.singleEntityList.map { it.toGifObject() }
    }

    @Test
    fun fetchCachedTrending_ShouldReturnFailure_WhenDatabaseQueryFails() = runTest {
        setupRepository()
        fakeRoomDbDataSource.apiError = Exception()

        val result = trendingRepository.fetchCachedTrending()

        result.isFailure shouldBe true
        result.exceptionOrNull() shouldBe Exception()
    }

    @Test
    fun reloadTrending_ShouldReturnCorrectList_WhenNetworkAndDatabaseOperationsSucceed() = runTest {
        setupRepository()
        fakeNetworkDataSource.trendingNetworkResponseDto = SampleTrendingNetworkResponse.singleResponse
        fakeRoomDbDataSource.queryDataResponse = SampleTrendingEntityList.singleEntityList

        val result = trendingRepository.reloadTrending(limit = 100, rating = Rating.R)

        result.isSuccess shouldBe true
        result.getOrNull() shouldBe SampleTrendingEntityList.singleEntityList.map { it.toGifObject() }
    }

    @Test
    fun reloadTrending_ShouldReturnFailure_WhenApiKeyIsBlank() = runTest {
        setupRepository(giphyApiKey = "")
        fakeNetworkDataSource.apiError = Exception()

        val result = trendingRepository.reloadTrending(limit = 100, rating = Rating.R)

        result.isFailure shouldBe true
        result.exceptionOrNull() shouldBe EmptyGiphyAPIKeyException()
    }

    @Test
    fun reloadTrending_ShouldReturnFailure_WhenNetworkCallFails() = runTest {
        setupRepository()
        fakeNetworkDataSource.apiError = Exception()

        val result = trendingRepository.reloadTrending(limit = 100, rating = Rating.R)

        result.isFailure shouldBe true
        result.exceptionOrNull() shouldBe Exception()
    }

    @Test
    fun reloadTrending_ShouldReturnFailure_WhenDatabaseOperationThrowsException() = runTest {
        setupRepository()
        fakeRoomDbDataSource.apiError = Exception()

        val result = trendingRepository.reloadTrending(limit = 100, rating = Rating.R)

        result.isFailure shouldBe true
        result.exceptionOrNull() shouldBe Exception()
    }
}
