package com.example.pictures_app

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class MainActivityViewModel : ViewModel() {
    val dynamicLinkData: MutableLiveData<Uri> by lazy {
        MutableLiveData()
    }
}