/*
 * Copyright (c) 2025. Levon M.
 * https://github.com/goodluck3301
 */

package com.goodluck3301.giphy.data.source.local

import com.goodluck3301.giphy.data.source.local.interfaces.DatabaseDataSource
import com.goodluck3301.giphy.data.source.local.model.TrendingEntity
import javax.inject.Inject

class RoomDatabaseDataSource @Inject constructor(
    private val giphyDatabase: GiphyDatabase,
) : DatabaseDataSource {
    override suspend fun insertData(data: TrendingEntity) {
        return giphyDatabase.trendingDao().insertData(data = data)
    }

    override suspend fun insertAllData(data: List<TrendingEntity>) {
        return giphyDatabase.trendingDao().insertAllData(data = data)
    }

    override suspend fun queryData(): List<TrendingEntity> {
        return giphyDatabase.trendingDao().queryData()
    }

    override suspend fun clear() {
        giphyDatabase.trendingDao().clear()
    }

    override suspend fun markDirty() {
        giphyDatabase.trendingDao().markDirty()
    }

    override suspend fun deleteDirty() {
        giphyDatabase.trendingDao().deleteDirty()
    }
}
