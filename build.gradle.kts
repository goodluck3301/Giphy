/*
 * Copyright (c) 2025. Levon M.
 * https://github.com/goodluck3301
 */

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
    alias(libs.plugins.hiltAndroidPlugin) apply false
    alias(libs.plugins.devtoolsKsp) apply false
    alias(libs.plugins.kotlinxKover) apply false
    alias(libs.plugins.androidTest) apply false
    alias(libs.plugins.baselineprofile) apply false
    alias(libs.plugins.compose.compiler) apply false
}
