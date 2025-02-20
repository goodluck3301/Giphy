/*
 * Copyright (c) 2025. Levon M.
 * https://github.com/goodluck3301
 */

package com.goodluck3301.giphy.ui.viewmodel

import coil.ImageLoader
import com.goodluck3301.giphy.data.repository.FakeTrendingRepository
import com.goodluck3301.giphy.data.repository.FakeUserPreferencesRepository
import com.goodluck3301.giphy.domain.model.Rating
import com.goodluck3301.giphy.domain.model.UserPreferences
import com.goodluck3301.giphy.test.testdata.SampleGifObjectList
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeSameInstanceAs
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
internal class TrendingViewModelTest {

    private lateinit var viewModel: TrendingViewModel
    private lateinit var fakeTrendingRepository: FakeTrendingRepository
    private lateinit var fakeUserPreferencesRepository: FakeUserPreferencesRepository
    private lateinit var mockImageLoader: ImageLoader

    private fun setupViewModel() {
        viewModel = TrendingViewModel(
            trendingRepository = fakeTrendingRepository,
            userPreferencesRepository = fakeUserPreferencesRepository,
            imageLoader = mockImageLoader,
            dispatcher = UnconfinedTestDispatcher(),
        )
    }

    @Before
    fun setUp() {
        fakeTrendingRepository = FakeTrendingRepository()
        fakeUserPreferencesRepository = FakeUserPreferencesRepository()
        mockImageLoader = mockk(relaxed = true)
    }

    // Test function names reviewed by ChatGPT for consistency

    @Test
    fun initialiseViewModel_ShouldStartWithIsLoadingTrue() {
        setupViewModel()
        assertEquals(true, viewModel.uiState.value.isLoading)
    }

    @Test
    fun initialiseViewModel_ShouldSetUIStateToEmptyAndNotLoading_OnEmptyDataFetch() {
        fakeUserPreferencesRepository.init(
            userPreferences = UserPreferences(
                apiRequestLimit = 100,
                rating = Rating.G,
            ),
        )
        fakeTrendingRepository.setTrendingResultForTest(Result.success(emptyList()))

        setupViewModel()

        viewModel.uiState.value.gifObjects shouldBe emptyList()
        viewModel.uiState.value.isLoading shouldBe false
    }

    @Test
    fun initialiseViewModel_ShouldUpdateUIStateWithError_OnDataFetchFailure() {
        val errorMessage = "some error message"
        fakeUserPreferencesRepository.init(
            userPreferences = UserPreferences(
                apiRequestLimit = 100,
                rating = Rating.G,
            ),
        )
        fakeTrendingRepository.setTrendingResultForTest(Result.failure(Exception(errorMessage)))

        setupViewModel()

        viewModel.uiState.value.errorMessages.any { it.message.contains("Error getting data: $errorMessage") } shouldBe true
        viewModel.uiState.value.isLoading shouldBe false
    }

    @Test
    fun refresh_ShouldUpdateUIWithNewItems_FromRepositorySuccessfully() {
        fakeUserPreferencesRepository.init(
            userPreferences = UserPreferences(
                apiRequestLimit = 100,
                rating = Rating.G,
            ),
        )
        fakeTrendingRepository.setTrendingResultForTest(Result.success(emptyList()))
        setupViewModel()

        fakeTrendingRepository.setTrendingResultForTest(Result.success(SampleGifObjectList.gifObjects))
        viewModel.refresh()

        viewModel.uiState.value.gifObjects shouldBe SampleGifObjectList.gifObjects
        viewModel.uiState.value.isLoading shouldBe false
    }

    @Test
    fun refresh_ShouldDisplayError_WhenUserPreferencesNotFullyConfigured() {
        fakeUserPreferencesRepository.init(
            userPreferences = UserPreferences(
                apiRequestLimit = null,
                rating = null,
            ),
        )
        setupViewModel()

        viewModel.refresh()

        viewModel.uiState.value.errorMessages.any { it.message.contains("Unable to access user preferences. Cannot refresh.") } shouldBe true
        viewModel.uiState.value.isLoading shouldBe false
    }

    @Test
    fun handleUserPreferenceErrors_ShouldAddErrorMessageToUIState() = runTest {
        setupViewModel()
        val errorMessage = "Preference error"

        fakeUserPreferencesRepository.emitError(Exception(errorMessage))

        viewModel.uiState.value.errorMessages.any { it.message.contains(errorMessage) } shouldBe true
    }

    @Test
    fun getImageLoader_ShouldReturnCorrectInstance() {
        setupViewModel()
        val expectedImageLoader = mockImageLoader

        val imageLoader = viewModel.getImageLoader()

        imageLoader shouldBeSameInstanceAs expectedImageLoader
    }

    @Test
    fun requestScrollToTop_ShouldUpdateUIStateCorrectly() {
        setupViewModel()
        val expectedRequestScrollToTop = true

        viewModel.requestScrollToTop(enabled = expectedRequestScrollToTop)
        val uiState = viewModel.uiState.value

        uiState.requestScrollToTop shouldBe expectedRequestScrollToTop
    }

    @Test
    fun errorMessages_ShouldAccumulateErrorMessages_OnMultipleFailures() = runTest {
        val errorMessage1 = "Test error 1"
        val errorMessage2 = "Test error 2"
        fakeUserPreferencesRepository.init(
            userPreferences = UserPreferences(
                apiRequestLimit = 100,
                rating = Rating.G,
            ),
        )
        setupViewModel()

        fakeUserPreferencesRepository.emitError(Exception(errorMessage1))
        viewModel.refresh()
        fakeUserPreferencesRepository.emitError(Exception(errorMessage2))
        viewModel.refresh()

        val uiState = viewModel.uiState.value
        uiState.errorMessages.size shouldBe 2
        uiState.errorMessages[0].message shouldBe errorMessage1
        uiState.errorMessages[1].message shouldBe errorMessage2
    }

    @Test
    fun errorMessages_ShouldNotAccumulateDuplicatedErrorMessages_OnMultipleFailures() = runTest {
        val errorMessage1 = "Test error 1"
        fakeUserPreferencesRepository.init(
            userPreferences = UserPreferences(
                apiRequestLimit = 100,
                rating = Rating.G,
            ),
        )
        setupViewModel()

        repeat(times = 2) {
            fakeUserPreferencesRepository.emitError(Exception(errorMessage1))
            viewModel.refresh()
        }

        val uiState = viewModel.uiState.value
        uiState.errorMessages.size shouldBe 1
        uiState.errorMessages[0].message shouldBe errorMessage1
    }

    @Test
    fun errorShown_ShouldRemoveSpecifiedErrorMessageFromUIState() = runTest {
        setupViewModel()
        fakeUserPreferencesRepository.emitError(Exception("some error message"))
        viewModel.refresh()
        val errorMessages = viewModel.uiState.value.errorMessages

        viewModel.errorShown(errorId = errorMessages.first().id)

        viewModel.uiState.value.errorMessages.size shouldBe errorMessages.size - 1
    }
}
