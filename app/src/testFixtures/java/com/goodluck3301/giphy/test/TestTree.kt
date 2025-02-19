/*
 * Copyright (c) 2025. Levon M.
 * https://github.com/goodluck3301
 */

package com.goodluck3301.giphy.test

import android.util.Log
import timber.log.Timber

class TestTree : Timber.Tree() {
    override fun log(
        priority: Int,
        tag: String?,
        message: String,
        t: Throwable?,
    ) {
        when (priority) {
            Log.DEBUG -> println("DEBUG: $tag: $message")
            Log.INFO -> println("INFO: $tag: $message")
            Log.WARN -> println("WARN: $tag: $message")
            Log.ERROR -> println("ERROR: $tag: $message")
            Log.ASSERT -> println("ASSERT: $tag: $message")
        }
    }
}
