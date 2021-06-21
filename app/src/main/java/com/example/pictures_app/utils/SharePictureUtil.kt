package com.example.pictures_app.utils

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import com.example.pictures_app.BuildConfig
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class SharePictureUtil {

    fun getSharePictureIntent(bitmap: Bitmap, context: Context): Intent? {
        val bitmapUri = getFileUriFromBitmap(bitmap, context)
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.putExtra(Intent.EXTRA_STREAM, bitmapUri)
        shareIntent.type = "image/*"

        return if (bitmapUri != null) {
            shareIntent
        } else {
            null
        }
    }

    private fun getFileUriFromBitmap(bitmap: Bitmap, context: Context): Uri? {
        var bitmapUri: Uri? = null
        try {
            val file = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                "share_image_" + System.currentTimeMillis() + ".png")
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, outputStream)
            outputStream.close()
            bitmapUri = FileProvider.getUriForFile(
                context, BuildConfig.APPLICATION_ID + ".provider", file
            )
        } catch (error: IOException) {
            error.printStackTrace()
        }
        return bitmapUri
    }
}