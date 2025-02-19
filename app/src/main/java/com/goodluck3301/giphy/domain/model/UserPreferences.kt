/*
 * Copyright (c) 2025. Levon M.
 * https://github.com/goodluck3301
 */

package com.goodluck3301.giphy.domain.model

data class UserPreferences(
    val apiRequestLimit: Int?,
    val rating: Rating?,
) {
    fun isFullyConfigured(): Boolean {
        return apiRequestLimit != null && rating != null
    }
}
