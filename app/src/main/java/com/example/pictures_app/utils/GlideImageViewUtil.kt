package com.example.pictures_app.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.media.Image
import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.SingleRequest
import com.bumptech.glide.request.target.*
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.example.pictures_app.R
import com.example.pictures_app.glide.GlideApp
import com.example.pictures_app.glide.GlideRequest

private const val userAgent =
    "Mozilla/5.0 (Linux; Android 11) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.88 Mobile Safari/537.36"
private val headers: LazyHeaders = LazyHeaders.Builder().addHeader("User-Agent", userAgent).build()

val isResourceReady: MutableLiveData<Boolean> = MutableLiveData()

fun ImageView.loadContentByGlide(url: String?): LiveData<Bitmap> {
    val pictureBitmap: MutableLiveData<Bitmap> = MutableLiveData()
    val glideUrl = GlideUrl(url, headers)
    val imageView: ImageView = this

    GlideApp.with(context)
        .asBitmap()
        .load(glideUrl)
        .placeholder(getCircularProgressDrawable(context))
        .error(R.drawable.ic_baseline_image_not_supported_24)
        .diskCacheStrategy(DiskCacheStrategy.DATA)
        .listener(glideRequestListener)
        .into(object: CustomTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                imageView.setImageBitmap(resource)
                pictureBitmap.postValue(resource)
            }

            override fun onLoadCleared(placeholder: Drawable?) {
                pictureBitmap.postValue(null)
            }
        })

    return pictureBitmap
}

private val glideRequestListener = object: RequestListener<Bitmap> {
    override fun onLoadFailed(
        e: GlideException?,
        model: Any?,
        target: Target<Bitmap>?,
        isFirstResource: Boolean
    ): Boolean {
        isResourceReady.postValue(false)
        return false
    }

    override fun onResourceReady(
        resource: Bitmap?,
        model: Any?,
        target: Target<Bitmap>?,
        dataSource: DataSource?,
        isFirstResource: Boolean
    ): Boolean {
        isResourceReady.postValue(true)
        return false
    }
}

private fun getCircularProgressDrawable(context: Context) : CircularProgressDrawable {
    val circularProgressDrawable = CircularProgressDrawable(context)
    circularProgressDrawable.let {
        it.strokeWidth = 5f
        it.centerRadius = 30f
        it.start()
    }
    return circularProgressDrawable
}