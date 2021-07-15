package com.example.pictures_app.fragments.albums

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pictures_app.PicturesApplication
import com.example.pictures_app.model.AlbumPicturesModel
import com.example.pictures_app.repository.PicturesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumsViewPagerFragmentViewModel @Inject constructor(
    private val repository: PicturesRepository
) : ViewModel() {

    val lastVisitedPosition: MutableLiveData<Int> = MutableLiveData(0)
    val viewPagerAlbumsList: MutableLiveData<List<AlbumPicturesModel>> =
        MutableLiveData()

    init {
        Log.d("Repository", repository.toString())
        getAlbums()
    }

    private fun getAlbums() {
        GlobalScope.launch(Dispatchers.IO) {
            viewPagerAlbumsList.postValue(repository.getAllAlbums())
        }
    }

}