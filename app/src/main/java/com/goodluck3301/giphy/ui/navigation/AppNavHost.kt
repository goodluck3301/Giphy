/*
 * Copyright (c) 2025. Levon M.
 * https://github.com/goodluck3301
 */

package com.goodluck3301.giphy.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.goodluck3301.giphy.BuildConfig
import com.goodluck3301.giphy.R
import com.goodluck3301.giphy.ui.destinations.search.SearchScreen
import com.goodluck3301.giphy.ui.destinations.search.SearchUIEvent
import com.goodluck3301.giphy.ui.destinations.settings.SettingsScreen
import com.goodluck3301.giphy.ui.destinations.settings.SettingsUIEvent
import com.goodluck3301.giphy.ui.destinations.trendinglist.TrendingListScreen
import com.goodluck3301.giphy.ui.destinations.trendinglist.TrendingUIEvent
import com.goodluck3301.giphy.ui.viewmodel.SearchViewModel
import com.goodluck3301.giphy.ui.viewmodel.SettingsViewModel
import com.goodluck3301.giphy.ui.viewmodel.TrendingViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    isLargeScreen: Boolean,
    navController: NavHostController,
    lastDoubleTappedNavItem: AppNavItem?,
    scrollBehavior: TopAppBarScrollBehavior,
    onShowSnackbar: suspend (String) -> Unit,
    onScrolledToTop: (AppNavItem) -> Unit,
) {
    fun resetScrollBehavior() {
        scrollBehavior.state.heightOffset = 0f
        scrollBehavior.state.contentOffset = 0f
    }

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = "trendingList",
    ) {
        composable(route = "trendingList") {
            val viewModel: TrendingViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val context = LocalContext.current

            LaunchedEffect(lastDoubleTappedNavItem) {
                val enabled = lastDoubleTappedNavItem?.equals(AppNavItem.TrendingList) ?: false
                viewModel.requestScrollToTop(enabled = enabled)
                if (enabled) {
                    resetScrollBehavior()
                }
            }

            TrendingListScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .nestedScroll(scrollBehavior.nestedScrollConnection),
                useCardLayout = isLargeScreen,
                imageLoader = viewModel.getImageLoader(),
                uiState = uiState,
                uiEvent = TrendingUIEvent(
                    onRefresh = { viewModel.refresh() },
                    onErrorShown = { viewModel.errorShown(it) },
                    onScrolledToTop = { onScrolledToTop(AppNavItem.TrendingList) },
                    onQueueDownloadFailed = { onShowSnackbar(context.getString(R.string.failed_to_download_file)) },
                    onQueueDownloadSuccess = { onShowSnackbar(context.getString(R.string.image_queued_for_download)) },
                    onShowSnackbar = onShowSnackbar,
                ),
            )

            LaunchedEffect(Unit) {
                resetScrollBehavior()
            }
        }

        composable(route = "search") {
            val viewModel: SearchViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val context = LocalContext.current

            LaunchedEffect(lastDoubleTappedNavItem) {
                val enabled = lastDoubleTappedNavItem?.equals(AppNavItem.Search) ?: false
                viewModel.requestScrollToTop(enabled = enabled)
                if (enabled) {
                    resetScrollBehavior()
                }
            }

            SearchScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .nestedScroll(scrollBehavior.nestedScrollConnection),
                useCardLayout = isLargeScreen,
                imageLoader = viewModel.getImageLoader(),
                uiState = uiState,
                uiEvent = SearchUIEvent(
                    onFetchLastSuccessfulSearch = { viewModel.fetchLastSuccessfulSearch() },
                    onClearKeyword = { viewModel.clearKeyword() },
                    onUpdateKeyword = { viewModel.updateKeyword(it) },
                    onSearch = {
                        resetScrollBehavior()
                        viewModel.search()
                    },
                    onErrorShown = { viewModel.errorShown(it) },
                    onScrolledToTop = { onScrolledToTop(AppNavItem.Search) },
                    onQueueDownloadFailed = { onShowSnackbar(context.getString(R.string.failed_to_download_file)) },
                    onQueueDownloadSuccess = { onShowSnackbar(context.getString(R.string.image_queued_for_download)) },
                    onShowSnackbar = onShowSnackbar,
                ),
            )

            LaunchedEffect(Unit) {
                resetScrollBehavior()
            }
        }

        composable(route = "settings") {
            val viewModel: SettingsViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            LaunchedEffect(lastDoubleTappedNavItem) {
                val enabled = lastDoubleTappedNavItem?.equals(AppNavItem.Settings) ?: false
                viewModel.requestScrollToTop(enabled = enabled)
                if (enabled) {
                    resetScrollBehavior()
                }
            }

            SettingsScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .nestedScroll(scrollBehavior.nestedScrollConnection),
                isLargeScreen = isLargeScreen,
                apiMinEntries = BuildConfig.API_MIN_ENTRIES.toInt(),
                apiMaxEntries = BuildConfig.API_MAX_ENTRIES.toInt(),
                uiState = uiState,
                uiEvent = SettingsUIEvent(
                    onUpdateApiMaxEntries = { viewModel.setApiRequestLimit(it) },
                    onUpdateRating = { viewModel.setRating(it) },
                    onErrorShown = { viewModel.errorShown(it) },
                    onScrolledToTop = { onScrolledToTop(AppNavItem.Settings) },
                    onShowSnackbar = onShowSnackbar,
                ),
            )

            LaunchedEffect(Unit) {
                resetScrollBehavior()
            }
        }
    }
}
