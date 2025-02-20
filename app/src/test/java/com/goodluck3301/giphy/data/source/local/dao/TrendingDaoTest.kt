/*
 * Copyright (c) 2025. Levon M.
 * https://github.com/goodluck3301
 */

package com.goodluck3301.giphy.data.source.local.dao

import android.content.Context
import android.os.Build
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.goodluck3301.giphy.data.source.local.GiphyDatabase
import com.goodluck3301.giphy.test.testdata.SampleTrendingEntity
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.collections.shouldNotContain
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.P])
class TrendingDaoTest {
    private lateinit var giphyDatabase: GiphyDatabase
    private lateinit var trendingDao: TrendingDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        giphyDatabase = Room.inMemoryDatabaseBuilder(context, GiphyDatabase::class.java)
            .allowMainThreadQueries() // only for testing
            .build()

        trendingDao = giphyDatabase.trendingDao()
    }

    @After
    fun teardown() {
        giphyDatabase.close()
    }

    // Basic test cases: CRUD usages
    // Test function names reviewed by ChatGPT for consistency

    @Test
    fun insertData_WithSingleEntry_ShouldReturnSingleEntry() = runTest {
        trendingDao.insertData(SampleTrendingEntity.case1)
        trendingDao.queryData() shouldContainExactly (listOf(SampleTrendingEntity.case1))
    }

    @Test
    fun insertAllData_WithMultipleEntries_ShouldMatchInsertedEntries() = runTest {
        val testTrendingList = listOf(
            SampleTrendingEntity.case2,
            SampleTrendingEntity.case3,
            SampleTrendingEntity.case4,
        )

        trendingDao.insertAllData(testTrendingList)

        // The order may not be the same due to sorting
        trendingDao.queryData() shouldContainAll (testTrendingList)
    }

    @Test
    fun updateData_WithModifiedEntry_ShouldReflectUpdatedEntry() = runTest {
        val testTrendingList = listOf(
            SampleTrendingEntity.case1,
            SampleTrendingEntity.case2,
            SampleTrendingEntity.case3,
        )
        trendingDao.insertAllData(testTrendingList)

        trendingDao.insertData(SampleTrendingEntity.case2Modified)

        val trendingList = trendingDao.queryData()
        trendingList shouldHaveSize 3
        trendingList shouldContain SampleTrendingEntity.case1
        trendingList shouldNotContain SampleTrendingEntity.case2
        trendingList shouldContain SampleTrendingEntity.case2Modified
        trendingList shouldContain SampleTrendingEntity.case3
    }

    @Test
    fun clearDatabase_WhenCalled_ShouldResultInEmptyDatabase() = runTest {
        val testTrendingList = listOf(
            SampleTrendingEntity.case1,
            SampleTrendingEntity.case2,
            SampleTrendingEntity.case3,
        )
        trendingDao.insertAllData(testTrendingList)

        trendingDao.clear()

        trendingDao.queryData() shouldHaveSize 0
    }

    // Dirty bit test cases
    @Test
    fun markDirty_OnCleanDatabase_ShouldSetAllEntriesAsDirty() = runTest {
        val testTrendingList = listOf(
            SampleTrendingEntity.case1,
            SampleTrendingEntity.case2,
            SampleTrendingEntity.case3,
        )
        trendingDao.insertAllData(testTrendingList)

        trendingDao.markDirty()

        val trendingList = trendingDao.queryData()
        trendingList shouldHaveSize 3
        trendingList[0].dirty shouldBe true
        trendingList[1].dirty shouldBe true
        trendingList[2].dirty shouldBe true
    }

    @Test
    fun deleteDirty_WhenAllEntriesAreDirty_ShouldClearDatabase() = runTest {
        val testTrendingList = listOf(
            SampleTrendingEntity.case1,
            SampleTrendingEntity.case2,
            SampleTrendingEntity.case3,
        )
        trendingDao.insertAllData(testTrendingList)
        trendingDao.markDirty()

        trendingDao.deleteDirty()

        trendingDao.queryData() shouldHaveSize 0
    }

    @Test
    fun deleteDirty_WithMixedCleanAndDirtyEntries_ShouldRemoveOnlyDirtyEntries() = runTest {
        val testTrendingList = listOf(
            SampleTrendingEntity.case1,
            SampleTrendingEntity.case2,
            SampleTrendingEntity.case3,
        )
        trendingDao.insertAllData(testTrendingList)
        trendingDao.markDirty()
        trendingDao.insertData(SampleTrendingEntity.case4)

        trendingDao.deleteDirty()

        trendingDao.queryData() shouldContainExactly (listOf(SampleTrendingEntity.case4))
    }
}
