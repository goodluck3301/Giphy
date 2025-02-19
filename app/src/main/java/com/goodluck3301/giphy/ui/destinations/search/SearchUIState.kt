/*
 * Copyright (c) 2025. Levon M.
 * https://github.com/goodluck3301
 */

package com.goodluck3301.giphy.ui.destinations.search

import com.goodluck3301.giphy.domain.model.GifObject
import com.goodluck3301.giphy.ui.model.ErrorMessage

data class SearchUIState(
    val keyword: String = "",
    // ⚠️ null and empty can be handled differently by the UI
    val keywordMaxLength: Int = 0,
    val gifObjects: List<GifObject>? = null,
    val isLoading: Boolean = true,
    val requestScrollToTop: Boolean = false,
    val errorMessages: List<ErrorMessage> = emptyList(),
)
