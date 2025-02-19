/*
 * Copyright (c) 2025. Levon M.
 * https://github.com/goodluck3301
 */

package com.goodluck3301.giphy.data.source.local.interfaces

import com.goodluck3301.giphy.data.source.local.model.TrendingEntity

interface DatabaseDataSource {
    suspend fun insertData(data: TrendingEntity)
    suspend fun insertAllData(data: List<TrendingEntity>)
    suspend fun queryData(): List<TrendingEntity>
    suspend fun clear()
    suspend fun markDirty()
    suspend fun deleteDirty()
}
