package com.example.pictures_app.repository

import android.content.Context
import android.net.ConnectivityManager
import android.provider.Settings
import androidx.lifecycle.MutableLiveData
import com.example.pictures_app.PicturesApplication
import com.example.pictures_app.database.dao.PicturesDao
import com.example.pictures_app.model.PictureModel
import com.example.pictures_app.model.Success
import com.example.pictures_app.networking.NetworkStatusChecker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class PicturesRepositoryImplementation(
    private val picturesDao: PicturesDao,
    context: Context
) : PicturesRepository {

    private val remoteApi = PicturesApplication.remoteApi
    private val networkStatusChecker by lazy {
        NetworkStatusChecker(context.getSystemService(ConnectivityManager::class.java))
    }
    override val picturesListLiveData: MutableLiveData<List<PictureModel>> = MutableLiveData()

    override fun getPictures() {
        GlobalScope.launch(Dispatchers.IO) {
            if (getAllLocalPictures().isEmpty()) {
                if (networkStatusChecker.hasInternetConnection()) {
                    val result = remoteApi.remoteApiGetAlbum1Photos()
                    if(result is Success) {
                        onServerPicturesListReceived(result.data)
                    } else {
                        onGetPicturesListFailed()
                    }
                }
            } else {
                picturesListLiveData.postValue(getAllLocalPictures())
            }
        }
    }

    private suspend fun getAllLocalPictures(): List<PictureModel> = picturesDao.getLocalPictures()

    private suspend fun addPicturesToDataBase(pictures: List<PictureModel>) =
        picturesDao.addLocalPictures(pictures)

    private suspend fun onServerPicturesListReceived(picturesList: List<PictureModel>) {
        val choppedPicturesList = picturesList.dropLast(25)
        picturesListLiveData.postValue(choppedPicturesList)
        addPicturesToDataBase(choppedPicturesList)
    }

    private fun onGetPicturesListFailed() {
        picturesListLiveData.postValue(null)
    }
}