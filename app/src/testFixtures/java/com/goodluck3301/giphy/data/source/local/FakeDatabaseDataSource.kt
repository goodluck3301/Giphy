/*
 * Copyright (c) 2025. Levon M.
 * https://github.com/goodluck3301
 */

package com.goodluck3301.giphy.data.source.local

import com.goodluck3301.giphy.data.source.local.interfaces.DatabaseDataSource
import com.goodluck3301.giphy.data.source.local.model.TrendingEntity

class FakeDatabaseDataSource : DatabaseDataSource {
    var apiError: Throwable? = null
    var queryDataResponse: List<TrendingEntity>? = null

    override suspend fun insertData(data: TrendingEntity) {
        apiError?.run { throw this }
        queryDataResponse = listOf(data)
    }

    override suspend fun insertAllData(data: List<TrendingEntity>) {
        apiError?.run { throw this }
        queryDataResponse = data
    }

    override suspend fun queryData(): List<TrendingEntity> {
        apiError?.run { throw this }
        return queryDataResponse ?: emptyList()
    }

    override suspend fun clear() {
        apiError?.run { throw this }
        queryDataResponse = emptyList()
    }

    override suspend fun markDirty() {
        apiError?.run { throw this }
    }

    override suspend fun deleteDirty() {
        apiError?.run { throw this }
    }
}
