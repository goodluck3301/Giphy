/*
 * Copyright (c) 2025. Levon M.
 * https://github.com/goodluck3301
 */

package com.goodluck3301.giphy.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.goodluck3301.giphy.R
import com.goodluck3301.giphy.domain.model.GifObject
import com.goodluck3301.giphy.ui.previewparameter.GiphyImageItemProvider
import com.goodluck3301.giphy.ui.theme.GiphyTrendingTheme
import com.goodluck3301.giphy.ui.theme.getDimension

@Composable
fun GiphyItem(
    modifier: Modifier = Modifier,
    gifObject: GifObject,
    showBottomDivider: Boolean,
    imageLoader: ImageLoader,
    onClickToDownload: (imageUrl: String) -> Unit,
    onClickToOpen: (url: String) -> Unit,
    onClickToShare: (url: String) -> Unit,
) {
    val dimension = LocalConfiguration.current.getDimension()
    Column(
        modifier = modifier,
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = dimension.minListItemHeight)
                .padding(
                    horizontal = dimension.defaultFullPadding,
                    vertical = dimension.defaultHalfPadding,
                ),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            text = gifObject.title,
        )

        AsyncImage(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(ratio = (gifObject.previewWidth / gifObject.previewHeight.toFloat()))
                .background(color = MaterialTheme.colorScheme.surfaceDim.copy(alpha = 0.3f)),
            model = ImageRequest
                .Builder(LocalContext.current)
                .data(gifObject.previewUrl)
                .crossfade(true)
                .build(),
            fallback = painterResource(R.drawable.twotone_insert_photo_24),
            error = painterResource(R.drawable.twotone_insert_photo_24),
            placeholder = painterResource(R.drawable.twotone_insert_photo_24),
            contentDescription = gifObject.title,
            contentScale = ContentScale.FillWidth,
            imageLoader = imageLoader,
        )

        val imageTypeBadgeBackground = MaterialTheme.colorScheme.tertiaryContainer
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .requiredHeight(height = dimension.minListItemHeight),
        ) {
            val currentDensity = LocalDensity.current
            CompositionLocalProvider(
                LocalDensity provides Density(currentDensity.density, fontScale = 1f),
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(horizontal = dimension.defaultFullPadding, vertical = dimension.grid_1)
                        .aspectRatio(ratio = 1f)
                        .clip(shape = CircleShape)
                        .wrapContentHeight(align = Alignment.CenterVertically)
                        .drawBehind {
                            drawRect(color = imageTypeBadgeBackground)
                        },
                    style = MaterialTheme.typography.labelSmall,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                    maxLines = 1,
                    overflow = TextOverflow.Clip,
                    text = gifObject.type.uppercase(),
                )

                if (gifObject.username.isNotBlank()) {
                    Text(
                        modifier = Modifier
                            .wrapContentHeight()
                            .weight(1.0f, fill = true)
                            .fillMaxWidth(fraction = 1.0f)
                            .width(intrinsicSize = IntrinsicSize.Min)
                            .padding(horizontal = dimension.grid_1)
                            .align(alignment = Alignment.CenterVertically),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        text = "@${gifObject.username}",
                    )
                } else {
                    Spacer(modifier = Modifier.weight(weight = 1.0f, fill = true))
                }
            }

            if (gifObject.imageUrl.isNotEmpty()) {
                IconButtonWithToolTip(
                    tooltipText = stringResource(R.string.content_description_download_image),
                    painter = painterResource(id = R.drawable.baseline_file_download_24),
                    onClick = { onClickToDownload(gifObject.imageUrl) },
                )
            }

            if (gifObject.webUrl.isNotEmpty()) {
                IconButtonWithToolTip(
                    tooltipText = stringResource(R.string.content_description_open_in_browser),
                    painter = painterResource(id = R.drawable.ic_baseline_open_in_browser_24),
                    onClick = { onClickToOpen(gifObject.webUrl) },
                )
            }

            if (gifObject.imageUrl.isNotEmpty()) {
                IconButtonWithToolTip(
                    tooltipText = stringResource(R.string.content_description_copy_image_link),
                    painter = painterResource(id = R.drawable.ic_baseline_content_copy_24),
                    onClick = { onClickToShare(gifObject.imageUrl) },
                )
            }
        }

        if (showBottomDivider) {
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = dimension.grid_0_5),
                thickness = 1.dp,
            )
        }
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
            GiphyItem(
                modifier = Modifier.fillMaxWidth(),
                gifObject = gifObject,
                showBottomDivider = false,
                imageLoader = ImageLoader(LocalContext.current),
                onClickToDownload = {},
                onClickToShare = {},
                onClickToOpen = {},
            )
        }
    }
}
