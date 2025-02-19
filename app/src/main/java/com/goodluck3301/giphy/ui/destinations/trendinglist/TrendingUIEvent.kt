/*
 * Copyright (c) 2025. Levon M.
 * https://github.com/goodluck3301
 */

package com.goodluck3301.giphy.ui.destinations.trendinglist

data class TrendingUIEvent(
    val onRefresh: () -> Unit,
    val onScrolledToTop: () -> Unit,
    val onQueueDownloadSuccess: suspend () -> Unit,
    val onQueueDownloadFailed: suspend () -> Unit,
    val onErrorShown: (errorId: Long) -> Unit,
    val onShowSnackbar: suspend (String) -> Unit,
)
