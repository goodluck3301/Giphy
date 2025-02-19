/*
 * Copyright (c) 2025. Levon M.
 * https://github.com/goodluck3301
 */

package com.goodluck3301.giphy.ui.destinations.search

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.goodluck3301.giphy.MainActivity
import com.goodluck3301.giphy.R
import com.goodluck3301.giphy.data.repository.FakeUITestSearchRepository
import com.goodluck3301.giphy.data.repository.FakeUITestUserPreferencesRepository
import com.goodluck3301.giphy.domain.model.Rating
import com.goodluck3301.giphy.domain.model.UserPreferences
import com.goodluck3301.giphy.domain.repository.SearchRepository
import com.goodluck3301.giphy.domain.repository.UserPreferencesRepository
import com.goodluck3301.giphy.ui.MainActivityTestRobot
import com.goodluck3301.giphy.ui.components.GiphyItemTestRobot
import com.goodluck3301.giphy.ui.test.SampleGifObjectList
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import javax.inject.Inject

@kotlinx.coroutines.ExperimentalCoroutinesApi
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class SearchScreenTest {
    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Inject
    lateinit var searchRepository: SearchRepository

    @Inject
    lateinit var userPreferencesRepository: UserPreferencesRepository

    private lateinit var mainActivityTestRobot: MainActivityTestRobot
    private lateinit var searchTestRobot: SearchTestRobot
    private lateinit var giphyItemTestRobot: GiphyItemTestRobot
    private lateinit var fakeSearchRepository: FakeUITestSearchRepository
    private lateinit var fakeUserPreferencesRepository: FakeUITestUserPreferencesRepository

    @Before
    fun setUp() {
        hiltRule.inject()
        mainActivityTestRobot = MainActivityTestRobot(composeTestRule)
        searchTestRobot = SearchTestRobot(composeTestRule)
        giphyItemTestRobot = GiphyItemTestRobot(composeTestRule)
        fakeSearchRepository = searchRepository as FakeUITestSearchRepository
        fakeUserPreferencesRepository = (userPreferencesRepository as FakeUITestUserPreferencesRepository).apply {
            init(
                userPreferences = UserPreferences(
                    apiRequestLimit = 50,
                    rating = Rating.R,
                ),
            )
        }
    }

    @Test
    fun searchScreenJourneyTest() = runTest {
        with(searchTestRobot) {
            fakeSearchRepository.setSearchResultForTest(
                searchResult = Result.success(emptyList()),
            )
            with(mainActivityTestRobot) {
                checkAppLayoutIsDisplayed()
                navigateToSearchScreen()
            }
            checkEmptySearchScreenIsDisplayed()
            checkSearchBarCanInputAndClearKeyword()
            checkSearchEmptyKeywordShowsNoDataScreen()

            // Error Snackbar
            with(mainActivityTestRobot) {
                val exceptionMessage = "Testing Exception"
                fakeSearchRepository.setSearchResultForTest(Result.failure(IOException(exceptionMessage)))
                checkCanPerformSearchWithKeyword("any test keywords")
                checkSnackbarIsDisplayedAndDismissed(message = "Error getting data: $exceptionMessage")
                checkCanClearSearchKeyword()
            }

            // Happy search flow
            with(giphyItemTestRobot) {
                fakeSearchRepository.setSearchResultForTest(
                    searchResult = Result.success(SampleGifObjectList.gifObjects),
                )
                checkCanPerformSearchWithKeyword("any test keywords")
                checkSearchResultsListIsDisplayed()
                for (index in 0..SampleGifObjectList.gifObjects.lastIndex) {
                    checkCanScrollToSearchResultItem(index = index)
                    checkGiphyItemIsDisplayed(gifObject = SampleGifObjectList.gifObjects[index])
                }

                // Second top should scroll back to the top
                mainActivityTestRobot.secondTapOnSearchTab()
                checkGiphyItemIsDisplayed(gifObject = SampleGifObjectList.gifObjects.first())
            }

            // Reload with only one item for easier testing
            with(giphyItemTestRobot) {
                val lastGiphyItem = SampleGifObjectList.gifObjects.last()
                fakeSearchRepository.setSearchResultForTest(
                    searchResult = Result.success(listOf(lastGiphyItem)),
                )
                checkCanClearSearchKeyword()
                checkCanPerformSearchWithKeyword("any other test keywords")

                checkGiphyItemIsDisplayed(gifObject = lastGiphyItem)

                checkGiphyImageItemButtonsLongClickToolTipAreDisplayed()
                checkOpenInBrowserButton(url = lastGiphyItem.webUrl)
                checkDownloadImageButton(imageUrl = lastGiphyItem.imageUrl)
                mainActivityTestRobot.checkSnackbarIsDisplayedAndDismissed(message = composeTestRule.activity.getString(R.string.image_queued_for_download))
                checkCopyImageLinkButton()
            }

            // Check the search keyword and result can survive navigation
            with(mainActivityTestRobot) {
                val lastSuccessfulSearchKeyword = "last search keyword"
                val lastSuccessfulSearchResult = listOf(SampleGifObjectList.gifObjects[1])

                navigateToTrendingScreen()
                fakeSearchRepository.setLastSuccessfulSearchKeywordForTest(lastSuccessfulSearchKeyword)
                fakeSearchRepository.setLastSuccessfulSearchResultsForTest(lastSuccessfulSearchResult)

                navigateToSearchScreen()
                checkSearchKeywordIsDisplayed(keyword = lastSuccessfulSearchKeyword)
                giphyItemTestRobot.checkGiphyItemIsDisplayed(gifObject = SampleGifObjectList.gifObjects[1])
            }
        }
    }
}
