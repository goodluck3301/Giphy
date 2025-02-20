/*
 * Copyright (c) 2025. Levon M.
 * https://github.com/goodluck3301
 */

package com.goodluck3301.giphy.ui.utils

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.os.Environment
import androidx.core.net.toUri
import com.goodluck3301.giphy.R
import timber.log.Timber

internal fun Context.startBrowserActivity(url: String) {
    val uri = url.toUri().buildUpon().scheme("https").build()
    val intent = Intent(Intent.ACTION_VIEW).setData(uri)
    startActivity(intent)
}

internal fun Context.downloadImageUsingMediaStore(imageUrl: String): Boolean {
    return try {
        val identifier = imageUrl.substringAfterLast("/media/").substringBeforeLast('/')
        val fileName = "giphy-$identifier.gif"
        val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

        val request = DownloadManager.Request(imageUrl.toUri().buildUpon().scheme("https").build())
            .setTitle(fileName)
            .setDescription(getString(R.string.downloading_image))
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setAllowedOverMetered(true)
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)

        downloadManager.enqueue(request)
        true // Return true indicating the download request was successfully enqueued.
    } catch (ex: Exception) {
        Timber.tag("DownloadManager").e(ex)
        false
    }
}
