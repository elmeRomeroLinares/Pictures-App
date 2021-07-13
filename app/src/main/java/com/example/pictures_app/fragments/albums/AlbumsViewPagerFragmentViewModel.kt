package com.example.pictures_app.fragments.albums

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pictures_app.PicturesApplication
import com.example.pictures_app.model.AlbumPicturesModel
import com.example.pictures_app.repository.PicturesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AlbumsViewPagerFragmentViewModel (
    private val repository: PicturesRepository
) : ViewModel() {

    val lastVisitedPosition: MutableLiveData<Int> = MutableLiveData(0)
    val viewPagerAlbumsList: MutableLiveData<List<AlbumPicturesModel>> =
        MutableLiveData()

    init {
        getAlbums()
    }

    private fun getAlbums() {
        GlobalScope.launch(Dispatchers.IO) {
            viewPagerAlbumsList.postValue(repository.getAllAlbums())
        }
    }

}