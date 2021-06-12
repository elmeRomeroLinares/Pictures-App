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


fun View.visible() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun Context.toast(message: String?, length: Int = Toast.LENGTH_SHORT) {
    message?.let {
        Toast.makeText(this, message, length).show()
    }
}