/*
 * Copyright (c) 2025. Levon M.
 * https://github.com/goodluck3301
 */

package com.goodluck3301.giphy.ui.destinations.settings

import com.goodluck3301.giphy.domain.model.Rating
import com.goodluck3301.giphy.ui.model.ErrorMessage

data class SettingsUIState(
    val apiRequestLimit: Int? = null,
    val rating: Rating? = null,
    val isLoading: Boolean = true,
    val requestScrollToTop: Boolean = false,
    val errorMessages: List<ErrorMessage> = emptyList(),
)
