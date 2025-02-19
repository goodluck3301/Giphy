/*
 * Copyright (c) 2025. Levon M.
 * https://github.com/goodluck3301
 */

package com.goodluck3301.giphy.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import com.goodluck3301.giphy.domain.model.GifObject
import com.goodluck3301.giphy.ui.previewparameter.GiphyImageItemProvider
import com.goodluck3301.giphy.ui.theme.GiphyTrendingTheme
import com.goodluck3301.giphy.ui.theme.getDimension

@Composable
fun GiphyItemCard(
    modifier: Modifier = Modifier,
    gifObject: GifObject,
    imageLoader: ImageLoader,
    onClickToDownload: (imageUrl: String) -> Unit,
    onClickToOpen: (url: String) -> Unit,
    onClickToShare: (url: String) -> Unit,
) {
    val dimension = LocalConfiguration.current.getDimension()

    Card(
        modifier = modifier.background(color = MaterialTheme.colorScheme.surface),
    ) {
        GiphyItem(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = dimension.defaultHalfPadding),
            gifObject = gifObject,
            showBottomDivider = false,
            imageLoader = imageLoader,
            onClickToDownload = { onClickToDownload(it) },
            onClickToOpen = { onClickToOpen(it) },
            onClickToShare = { onClickToShare(it) },
        )
    }
}

@PreviewLightDark
@PreviewFontScale
@Composable
private fun Preview(
    @PreviewParameter(GiphyImageItemProvider::class) gifObject: GifObject,
) {
    GiphyTrendingTheme {
        Surface {
            GiphyItemCard(
                modifier = Modifier
                    .padding(all = 24.dp)
                    .fillMaxWidth(),
                gifObject = gifObject,
                imageLoader = ImageLoader(LocalContext.current),
                onClickToDownload = {},
                onClickToShare = {},
                onClickToOpen = {},
            )
        }
    }
}
