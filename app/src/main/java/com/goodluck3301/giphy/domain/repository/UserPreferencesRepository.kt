/*
 * Copyright (c) 2025. Levon M.
 * https://github.com/goodluck3301
 */

package com.goodluck3301.giphy.domain.repository

import com.goodluck3301.giphy.domain.model.Rating
import com.goodluck3301.giphy.domain.model.UserPreferences
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface UserPreferencesRepository {
    val userPreferences: StateFlow<UserPreferences>
    val preferenceErrors: SharedFlow<Throwable>

    suspend fun setApiRequestLimit(limit: Int)
    suspend fun setRating(rating: Rating)
}
