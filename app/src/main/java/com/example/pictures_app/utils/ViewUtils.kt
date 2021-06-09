package com.example.pictures_app.utils

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.bumptech.glide.request.RequestOptions
import com.example.pictures_app.R
import com.example.pictures_app.glide.GlideApp

private const val userAgent =
    "Mozilla/5.0 (Linux; Android 11) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.88 Mobile Safari/537.36"
private val headers: LazyHeaders = LazyHeaders.Builder().addHeader("User-Agent", userAgent).build()

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun ImageView.loadContentByGlide(url: String?) {
    val glideUrl = GlideUrl(url, headers)

    val requestOptions = RequestOptions()
        .placeholder(getCircularProgressDrawable(context))
        .error(R.drawable.ic_baseline_image_not_supported_24)

    GlideApp.with(context)
        .applyDefaultRequestOptions(requestOptions)
        .load(glideUrl)
        .into(this)
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

fun Context.toast(message: String?, length: Int = Toast.LENGTH_SHORT) {
    message?.let {
        Toast.makeText(this, message, length).show()
    }
}