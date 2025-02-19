/*
 * Copyright (c) 2025. Levon M.
 * https://github.com/goodluck3301
 */

package com.goodluck3301.giphy.domain.exceptions

/***
 * Workaround to rethrow CancellationException
 * Reference: https://github.com/Kotlin/kotlinx.coroutines/issues/1814
 */

inline fun <reified T : Throwable, R> Result<R>.except(): Result<R> =
    onFailure { if (it is T) throw it }
