/*
 * Copyright (c) 2025. Levon M.
 * https://github.com/goodluck3301
 */

package com.goodluck3301.giphy.ui.destinations.settings

import com.goodluck3301.giphy.domain.model.Rating

data class SettingsUIEvent(
    val onUpdateApiMaxEntries: (maxApiEntries: Int) -> Unit,
    val onUpdateRating: (rating: Rating) -> Unit,
    val onScrolledToTop: () -> Unit,
    val onErrorShown: (errorId: Long) -> Unit,
    val onShowSnackbar: suspend (String) -> Unit,
)
