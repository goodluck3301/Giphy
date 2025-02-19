/*
 * Copyright (c) 2025. Levon M.
 * https://github.com/goodluck3301
 */

package com.goodluck3301.giphy.data.repository

import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.goodluck3301.giphy.BuildConfig
import com.goodluck3301.giphy.data.source.preferences.interfaces.PreferencesDataStoreWrapper
import com.goodluck3301.giphy.di.DispatcherModule
import com.goodluck3301.giphy.domain.model.Rating
import com.goodluck3301.giphy.domain.model.UserPreferences
import com.goodluck3301.giphy.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.max
import kotlin.math.min

internal const val KEY_API_REQUEST_LIMIT = "limit"
internal const val KEY_RATING = "rating"

class UserPreferencesRepositoryImpl @Inject constructor(
    private val preferencesDataStoreWrapper: PreferencesDataStoreWrapper,
    private val defaultApiMinEntries: Int = BuildConfig.API_MIN_ENTRIES.toInt(),
    private val defaultApiMaxEntries: Int = BuildConfig.API_MAX_ENTRIES.toInt(),
    externalCoroutineScope: CoroutineScope,
    @DispatcherModule.IoDispatcher private val dispatcher: CoroutineDispatcher,
) : UserPreferencesRepository {
    private val prefKeyApiRequestLimit = intPreferencesKey(KEY_API_REQUEST_LIMIT)
    private val prefKeyRating = stringPreferencesKey(KEY_RATING)

    private val _userPreferences: MutableStateFlow<UserPreferences> = MutableStateFlow(UserPreferences(apiRequestLimit = null, rating = null))
    override val userPreferences = _userPreferences

    private val _preferenceErrors = MutableSharedFlow<Throwable>()
    override val preferenceErrors = _preferenceErrors.asSharedFlow()

    init {
        externalCoroutineScope.launch(dispatcher) {
            launch {
                preferencesDataStoreWrapper.preferenceErrors.collect { _preferenceErrors.emit(it) }
            }

            launch {
                preferencesDataStoreWrapper.getDataStoreFlow()
                    .catch { exception ->
                        _preferenceErrors.emit(exception)
                    }
                    .collect { prefs ->
                        _userPreferences.update {
                            it.copy(
                                apiRequestLimit = prefs[prefKeyApiRequestLimit] ?: ((defaultApiMaxEntries + defaultApiMinEntries) / 2),
                                rating = Rating.fromApiValue(
                                    apiValue = prefs[prefKeyRating],
                                    defaultValue = Rating.G,
                                ),
                            )
                        }
                    }
            }
        }
    }

    override suspend fun setApiRequestLimit(limit: Int) {
        preferencesDataStoreWrapper.updatePreference(
            key = prefKeyApiRequestLimit,
            newValue = max(min(limit, defaultApiMaxEntries), defaultApiMinEntries),
        )
    }

    override suspend fun setRating(rating: Rating) {
        preferencesDataStoreWrapper.updatePreference(
            key = prefKeyRating,
            newValue = rating.apiValue,
        )
    }
}
