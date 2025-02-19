/*
 * Copyright (c) 2025. Levon M.
 * https://github.com/goodluck3301
 */

package com.goodluck3301.giphy.data.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.goodluck3301.giphy.data.source.local.model.TrendingEntity

@Dao
interface TrendingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertData(data: TrendingEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllData(data: List<TrendingEntity>)

    @Query("SELECT * FROM trending ORDER BY trending_datetime DESC, import_datetime DESC")
    suspend fun queryData(): List<TrendingEntity>

    @Query("DELETE FROM trending")
    suspend fun clear()

    // SQLite does not have a boolean data type.
    // Room maps it to an INTEGER column, mapping true to 1 and false to 0.
    @Query("UPDATE trending SET dirty = 1")
    suspend fun markDirty()

    @Query("DELETE FROM trending WHERE dirty = 1")
    suspend fun deleteDirty()
}
