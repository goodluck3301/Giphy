/*
 * Copyright (c) 2025. Levon M.
 * https://github.com/goodluck3301
 */

package com.goodluck3301.giphy.ui.previewparameter

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.goodluck3301.giphy.domain.model.GifObject

class GiphyImageItemProvider : PreviewParameterProvider<GifObject> {
    override val values: Sequence<GifObject>
        get() = sequenceOf(
            GifObject(
                id = "MiBE4zcYUZWYmHnsI7",
                previewUrl = "https://media4.giphy.com/media/MiBE4zcYUZWYmHnsI7/200w.gif",
                previewHeight = 280,
                previewWidth = 480,
                imageUrl = "https://media4.giphy.com/media/MiBE4zcYUZWYmHnsI7/giphy.gif",
                webUrl = "https://giphy.com/gifs/marchmadness-sports-sport-womens-basketball-MiBE4zcYUZWYmHnsI7",
                title = "Womens Basketball Sport GIF by NCAA March Madness",
                type = "gif",
                username = "marchmadness",
            ),
        )
}
