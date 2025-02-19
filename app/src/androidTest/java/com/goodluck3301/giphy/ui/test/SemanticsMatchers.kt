/*
 * Copyright (c) 2025. Levon M.
 * https://github.com/goodluck3301
 */

package com.goodluck3301.giphy.ui.test

import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.SemanticsMatcher

// https://proandroiddev.com/test-jetpack-compose-layouts-easily-with-role-semanticproperty-dcf19f64130f
fun withRole(role: Role) = SemanticsMatcher("${SemanticsProperties.Role.name} contains '$role'") {
    val roleProperty = it.config.getOrNull(SemanticsProperties.Role) ?: false
    roleProperty == role
}
