/*
 * Copyright (c) 2025. Levon M.
 * https://github.com/goodluck3301
 */

package com.goodluck3301.giphy.ui.destinations.search

data class SearchUIEvent(
    val onFetchLastSuccessfulSearch: () -> Unit,
    val onUpdateKeyword: (keyword: String) -> Unit,
    val onClearKeyword: () -> Unit,
    val onSearch: () -> Unit,
    val onScrolledToTop: () -> Unit,
    val onQueueDownloadSuccess: suspend () -> Unit,
    val onQueueDownloadFailed: suspend () -> Unit,
    val onErrorShown: (errorId: Long) -> Unit,
    val onShowSnackbar: suspend (String) -> Unit,
)
