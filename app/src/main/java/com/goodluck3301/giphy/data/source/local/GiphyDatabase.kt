/*
 * Copyright (c) 2025. Levon M.
 * https://github.com/goodluck3301
 */

package com.goodluck3301.giphy.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.goodluck3301.giphy.data.source.local.dao.TrendingDao
import com.goodluck3301.giphy.data.source.local.model.TrendingEntity

@Database(entities = [TrendingEntity::class], version = 7, exportSchema = false)
@TypeConverters(InstantTypeConverters::class)
abstract class GiphyDatabase : RoomDatabase() {
    abstract fun trendingDao(): TrendingDao
}
