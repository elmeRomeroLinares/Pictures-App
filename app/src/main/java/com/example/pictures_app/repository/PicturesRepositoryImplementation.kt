package com.example.pictures_app.repository

import android.content.Context
import android.net.ConnectivityManager
import android.provider.Settings
import androidx.lifecycle.MutableLiveData
import com.example.pictures_app.PicturesApplication
import com.example.pictures_app.database.dao.AlbumsDao
import com.example.pictures_app.database.dao.PicturesDao
import com.example.pictures_app.model.AlbumPicturesModel
import com.example.pictures_app.model.PictureModel
import com.example.pictures_app.model.Success
import com.example.pictures_app.networking.NetworkStatusChecker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


private const val USER_ID: Long = 1

class PicturesRepositoryImplementation(
    private val picturesDao: PicturesDao,
    private val albumsDao: AlbumsDao,
    context: Context
) : PicturesRepository {

    private val remoteApi = PicturesApplication.remoteApi
    private val networkStatusChecker by lazy {
        NetworkStatusChecker(context.getSystemService(ConnectivityManager::class.java))
    }
    override val picturesListLiveData: MutableLiveData<List<PictureModel>> = MutableLiveData()
    override val albumsListLiveData: MutableLiveData<List<AlbumPicturesModel>> = MutableLiveData()

    override fun getAllAlbums() {
        GlobalScope.launch(Dispatchers.IO) {
            if (networkStatusChecker.hasInternetConnection()) {
                val result = remoteApi.remoteApiGetAlbums(USER_ID)
                if (result is Success) {
                    onServerAlbumsListReceived(result.data)
                } else {
                    onGetServerAlbumsListFailed()
                }
            } else if (getAllLocalAlbums().isEmpty()) {
                onGetServerAlbumsListFailed()
            } else {
                albumsListLiveData.postValue(getAllLocalAlbums())
            }
        }
    }

    override fun getPicturesFromAlbumId(albumId: Long){
        //call method from Dao or call method from retrofit
        GlobalScope.launch(Dispatchers.IO) {
            if (networkStatusChecker.hasInternetConnection()){
                //TODO get actual server data and update database
                val result = remoteApi.remoteApiGetAlbumPhotos(albumId = albumId)
                if (result is Success) {
                    onServerPicturesListReceived(result.data)
                } else {
                    onGetPicturesListFailed()
                }
            } else if (getAllLocalPicturesByAlbumId(albumId).isEmpty()) {
                onGetPicturesListFailed()
            } else {
                picturesListLiveData.postValue(getAllLocalPicturesByAlbumId(albumId))
            }
        }
    }

//    override fun getPictures() {
//        GlobalScope.launch(Dispatchers.IO) {
//            if (getAllLocalPictures().isEmpty()) {
//                if (networkStatusChecker.hasInternetConnection()) {
//                    val result = remoteApi.remoteApiGetAlbum1Photos()
//                    if(result is Success) {
//                        onServerPicturesListReceived(result.data)
//                    } else {
//                        onGetPicturesListFailed()
//                    }
//                }
//            } else {
//                picturesListLiveData.postValue(getAllLocalPictures())
//            }
//        }
//    }
    private suspend fun getAllLocalAlbums(): List<AlbumPicturesModel> = albumsDao.getLocalAlbums()

    private suspend fun addAlbumsToDataBase(albums: List<AlbumPicturesModel>) =
        albumsDao.addLocalAlbums(albums)

    private suspend fun onServerAlbumsListReceived(albumsList: List<AlbumPicturesModel>) {
        val choppedAlbumsList = albumsList.dropLast(albumsList.size - 2)
        albumsListLiveData.postValue(choppedAlbumsList)
        addAlbumsToDataBase(choppedAlbumsList)
    }

    private fun onGetServerAlbumsListFailed() {
        albumsListLiveData.postValue(null)
    }

    private suspend fun getAllLocalPicturesByAlbumId(albumId: Long): List<PictureModel> {
        val picturesFromAlbum = albumsDao.getPhotosByAlbum(albumId).pictures
        return picturesFromAlbum?: emptyList()
    }

    private suspend fun addPicturesToDataBase(pictures: List<PictureModel>) =
        picturesDao.addLocalPictures(pictures)

    private suspend fun onServerPicturesListReceived(picturesList: List<PictureModel>) {
        val choppedPicturesList = picturesList.dropLast(picturesList.size - 25)
        picturesListLiveData.postValue(choppedPicturesList)
        addPicturesToDataBase(choppedPicturesList)
    }

    private fun onGetPicturesListFailed() {
        picturesListLiveData.postValue(null)
    }
}