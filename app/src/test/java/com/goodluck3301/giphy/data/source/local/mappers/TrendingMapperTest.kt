/*
 * Copyright (c) 2025. Levon M.
 * https://github.com/goodluck3301
 */

package com.goodluck3301.giphy.data.source.local.mappers

import com.goodluck3301.giphy.data.repository.mappers.toEntity
import com.goodluck3301.giphy.data.repository.mappers.toGifObject
import com.goodluck3301.giphy.test.testdata.SampleTrendingMapper
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import org.junit.Test

internal class TrendingMapperTest {
    /***
     * These tests are to make sure we preserve the exact object conversion.
     * When tests found broken, it suggested that we might have to check if the changes are intended.
     */

    // Test function names reviewed by ChatGPT for consistency

    @Test
    fun toDomainModel_ShouldConvertTrendingEntityToGiphyImageItemCorrectly() {
        val giphyImageItemDomainModel = SampleTrendingMapper.sampleTrendingEntity1.toGifObject()
        giphyImageItemDomainModel shouldBe SampleTrendingMapper.sampleDomainModel1
    }

    @Test
    fun toDomainModelList_ShouldConvertListOfTrendingEntitiesToListOfGiphyImageItemsCorrectly() {
        val trendingEntityList = listOf(
            SampleTrendingMapper.sampleTrendingEntity1,
            SampleTrendingMapper.sampleTrendingEntity2,
            SampleTrendingMapper.sampleTrendingEntity3,
        )
        val expectedGiphyImageItemDomainModelList = listOf(
            SampleTrendingMapper.sampleDomainModel1,
            SampleTrendingMapper.sampleDomainModel2,
            SampleTrendingMapper.sampleDomainModel3,
        )

        val giphyImageItemDomainModelList = trendingEntityList.map { it.toGifObject() }

        giphyImageItemDomainModelList shouldContainExactly expectedGiphyImageItemDomainModelList
    }

    @Test
    fun toTrendingEntity_ShouldConvertTrendingDataToTrendingEntityCorrectly() {
        val trendingEntity = SampleTrendingMapper.sampleTrendingDataDto1.toEntity()
        trendingEntity shouldBe SampleTrendingMapper.sampleTrendingEntity1
    }

    @Test
    fun toTrendingEntityList_ShouldConvertListOfTrendingDataToListOfTrendingEntitiesCorrectly() {
        val mockTrendingDataList = listOf(
            SampleTrendingMapper.sampleTrendingDataDto1,
            SampleTrendingMapper.sampleTrendingDataDto2,
            SampleTrendingMapper.sampleTrendingDataDto3,
        )
        val expectedTrendingEntityList = listOf(
            SampleTrendingMapper.sampleTrendingEntity1,
            SampleTrendingMapper.sampleTrendingEntity2,
            SampleTrendingMapper.sampleTrendingEntity3,
        )

        val trendingEntityList = mockTrendingDataList.map { it.toEntity() }

        trendingEntityList shouldBe expectedTrendingEntityList
    }
}
