/*
 * Copyright (c) 2025. Levon M.
 * https://github.com/goodluck3301
 */

package com.goodluck3301.giphy.ui.destinations.trendinglist

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.goodluck3301.giphy.MainActivity
import com.goodluck3301.giphy.R
import com.goodluck3301.giphy.data.repository.FakeUITestTrendingRepository
import com.goodluck3301.giphy.data.repository.FakeUITestUserPreferencesRepository
import com.goodluck3301.giphy.domain.model.Rating
import com.goodluck3301.giphy.domain.model.UserPreferences
import com.goodluck3301.giphy.domain.repository.TrendingRepository
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
class TrendingListScreenTest {
    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Inject
    lateinit var trendingRepository: TrendingRepository

    @Inject
    lateinit var userPreferencesRepository: UserPreferencesRepository

    private lateinit var mainActivityTestRobot: MainActivityTestRobot
    private lateinit var trendingListTestRobot: TrendingListTestRobot
    private lateinit var giphyItemTestRobot: GiphyItemTestRobot
    private lateinit var fakeTrendingRepository: FakeUITestTrendingRepository
    private lateinit var fakeUserPreferencesRepository: FakeUITestUserPreferencesRepository

    @Before
    fun setUp() {
        hiltRule.inject()
        mainActivityTestRobot = MainActivityTestRobot(composeTestRule)
        trendingListTestRobot = TrendingListTestRobot(composeTestRule)
        giphyItemTestRobot = GiphyItemTestRobot(composeTestRule)
        fakeTrendingRepository = trendingRepository as FakeUITestTrendingRepository
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
    fun trendingListScreenJourneyTest() = runTest {
        with(trendingListTestRobot) {
            fakeTrendingRepository.setTrendingResultForTest(
                trendingResult = Result.success(emptyList()),
            )

            with(mainActivityTestRobot) {
                checkAppLayoutIsDisplayed()
                navigateToTrendingScreen()
                checkNoDataScreenIsDisplayed()
            }

            // Pull to refresh with some data returned

            with(giphyItemTestRobot) {
                fakeTrendingRepository.setTrendingResultForTest(
                    trendingResult = Result.success(SampleGifObjectList.gifObjects),
                )
                performPullToRefresh()
                checkTrendingListIsDisplayed()
                for (index in 0..SampleGifObjectList.gifObjects.lastIndex) {
                    checkCanScrollToTrendingListItem(index = index)
                    checkGiphyItemIsDisplayed(gifObject = SampleGifObjectList.gifObjects[index])
                }

                // Second top should scroll back to the top
                mainActivityTestRobot.secondTapOnTrendingTab()
                checkGiphyItemIsDisplayed(gifObject = SampleGifObjectList.gifObjects.first())
            }

            // Error Snackbar
            with(mainActivityTestRobot) {
                val exceptionMessage = "Testing Exception"
                fakeTrendingRepository.setTrendingResultForTest(Result.failure(IOException(exceptionMessage)))
                performPullToRefresh()
                checkSnackbarIsDisplayedAndDismissed(message = "Error getting data: $exceptionMessage")
            }

            // Reload with only one item for easier testing
            with(giphyItemTestRobot) {
                val lastGiphyItem = SampleGifObjectList.gifObjects.last()
                fakeTrendingRepository.setTrendingResultForTest(
                    trendingResult = Result.success(listOf(lastGiphyItem)),
                )
                performPullToRefresh()

                checkGiphyItemIsDisplayed(gifObject = lastGiphyItem)

                composeTestRule.mainClock.advanceTimeBy(milliseconds = 5_000)
                checkGiphyImageItemButtonsLongClickToolTipAreDisplayed()
                checkOpenInBrowserButton(url = lastGiphyItem.webUrl)
                checkDownloadImageButton(imageUrl = lastGiphyItem.imageUrl)
                mainActivityTestRobot.checkSnackbarIsDisplayedAndDismissed(message = composeTestRule.activity.getString(R.string.image_queued_for_download))
                checkCopyImageLinkButton()
            }
        }
    }
}
