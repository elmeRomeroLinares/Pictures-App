package com.example.pictures_app.data.source

import com.example.pictures_app.data.PicturesAlbumsPostsDataSource
import com.example.pictures_app.database.LocalDataSource
import com.example.pictures_app.model.AlbumPicturesModel
import com.example.pictures_app.model.PictureModel
import com.example.pictures_app.model.PostModel
import com.example.pictures_app.repository.PicturesRepositoryImplementation
import com.squareup.moshi.JsonAdapter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class PicturesRepositoryImplementationTest {

    private val album1 = AlbumPicturesModel(1,1,"quidem molestiae enim")
    private val album2 = AlbumPicturesModel(1,2,"sunt qui excepturi placeat culpa")

    private val picture1Album1 = PictureModel(1,1,
        "accusamus beatae ad facilis cum similique qui sunt",
        "https://via.placeholder.com/600/92c952",
        "https://via.placeholder.com/150/92c952")

    private val picture2Album1 = PictureModel(1,2,
        "reprehenderit est deserunt velit ipsam",
        "https://via.placeholder.com/600/771796",
        "https://via.placeholder.com/150/771796")

    private val picture1Album2 = PictureModel(2,51,
        "non sunt voluptatem placeat consequuntur rem incidunt",
        "https://via.placeholder.com/600/8e973b",
        "https://via.placeholder.com/150/8e973b")

    private val picture2Album2 = PictureModel(2,52,
        "eveniet pariatur quia nobis reiciendis laboriosam ea",
        "https://via.placeholder.com/600/121fa4",
        "https://via.placeholder.com/150/121fa4")

    private val post1 = PostModel(1,1,
        "sunt aut facere repellat provident occaecati excepturi optio reprehenderit",
        "quia et suscipit\nsuscipit recusandae consequuntur expedita et cum\nreprehenderit molestiae ut ut quas totam\nnostrum rerum est autem sunt rem eveniet architecto")

    private val post2 = PostModel(1,2,
        "qui est esse",
        "est rerum tempore vitae\nsequi sint nihil reprehenderit dolor beatae ea dolores neque\nfugiat blanditiis voluptate porro vel nihil molestiae ut reiciendis\nqui aperiam non debitis possimus qui neque nisi nulla")

    private val remoteAlbums = listOf(album1, album2)
    private val localAlbums = listOf(album1)

    private val remotePictures = listOf(picture1Album1, picture1Album2, picture2Album1)
    private val localPictures = listOf(picture1Album1, picture2Album1)

    private val remotePosts = listOf(post1, post2)
    private val localPosts = listOf(post1)

    private val remotePicturesByAlbums: LinkedHashMap<AlbumPicturesModel, List<PictureModel>> =
        LinkedHashMap()
    private val localPicturesByAlbums: LinkedHashMap<AlbumPicturesModel, List<PictureModel>> =
        LinkedHashMap()

    private lateinit var remoteDataSource: PicturesAlbumsPostsDataSource
    private lateinit var localDataSource: LocalDataSource

    private lateinit var networkStatusChecker: FakeNetworkStatusChecker

    private lateinit var picturesRepositoryImplementation: PicturesRepositoryImplementation

    @Before
    fun createRepository() {
        remotePicturesByAlbums[album1] = listOf(picture1Album1, picture2Album1)
        remotePicturesByAlbums[album2] = listOf(picture1Album2, picture2Album2)

        localPicturesByAlbums[album1] = listOf(picture2Album1)
        localPicturesByAlbums[album2] = listOf(picture2Album2)

        remoteDataSource = FakeRemoteDataSource(remotePicturesByAlbums,
            remotePosts.toMutableList(), remotePictures.toMutableList(), remoteAlbums.toMutableList())

        localDataSource = FakeLocalDataSource(localPicturesByAlbums,
            localPosts.toMutableList(), localPictures.toMutableList(), localAlbums.toMutableList())
    }

    @Test
    fun getAllAlbums_requestsAllAlbumsFromRemoteDataSource() = runBlocking {
        networkStatusChecker = FakeNetworkStatusChecker(true)
        picturesRepositoryImplementation = PicturesRepositoryImplementation(localDataSource,
            remoteDataSource, networkStatusChecker)

        val albums = picturesRepositoryImplementation.getAllAlbums()

        assertEquals(albums, remoteAlbums)
    }

    @Test
    fun getAllAlbums_requestAllAlbumsFromLocalDataSource() = runBlocking {
        networkStatusChecker = FakeNetworkStatusChecker(false)
        picturesRepositoryImplementation = PicturesRepositoryImplementation(localDataSource,
            remoteDataSource, networkStatusChecker)

        val albums = picturesRepositoryImplementation.getAllAlbums()

        assertEquals(albums, localAlbums)
    }

    @Test
    fun errorOnGetAllAlbums_requestsAllAlbumsFromRemoteDataSource() = runBlocking {
        networkStatusChecker = FakeNetworkStatusChecker(true)
        remoteDataSource = FakeRemoteDataSource(null,
            null, null, null)

        picturesRepositoryImplementation = PicturesRepositoryImplementation(localDataSource,
            remoteDataSource, networkStatusChecker)

        val albums = picturesRepositoryImplementation.getAllAlbums()

        assertEquals(albums, localAlbums)
    }

    @Test
    fun errorOnGetAllAlbums_requestsAllAlbumsFromLocalDataSource() = runBlocking {
        networkStatusChecker = FakeNetworkStatusChecker(false)
        localDataSource = FakeLocalDataSource(null,
            null, null, null)

        picturesRepositoryImplementation = PicturesRepositoryImplementation(localDataSource,
            remoteDataSource, networkStatusChecker)

        val albums = picturesRepositoryImplementation.getAllAlbums()
        assertEquals(albums, null)
    }

    @Test
    fun getPicturesFromAlbumId1_requestPicturesFromRemoteSource() = runBlocking {
        networkStatusChecker = FakeNetworkStatusChecker(true)
        picturesRepositoryImplementation = PicturesRepositoryImplementation(localDataSource,
            remoteDataSource, networkStatusChecker)

        val picturesAlbum1 = picturesRepositoryImplementation.getPicturesFromAlbumId(1)
        assertEquals(picturesAlbum1, remotePicturesByAlbums[album1])
    }

    @Test
    fun getPicturesFromAlbumId2_requestPicturesFromRemoteSource() = runBlocking {
        networkStatusChecker = FakeNetworkStatusChecker(true)
        picturesRepositoryImplementation = PicturesRepositoryImplementation(localDataSource,
            remoteDataSource, networkStatusChecker)

        val picturesAlbum2 = picturesRepositoryImplementation.getPicturesFromAlbumId(2)
        assertEquals(picturesAlbum2, remotePicturesByAlbums[album2])
    }

    @Test
    fun errorOnGetPicturesFromAlbumId1_requestPicturesFromRemoteSource() = runBlocking {
        networkStatusChecker = FakeNetworkStatusChecker(true)
        remoteDataSource = FakeRemoteDataSource(null,
            null, null, null)

        picturesRepositoryImplementation = PicturesRepositoryImplementation(localDataSource,
            remoteDataSource, networkStatusChecker)

        val picturesAlbum1 = picturesRepositoryImplementation.getPicturesFromAlbumId(1)

        assertEquals(picturesAlbum1, localPicturesByAlbums[album1])
    }

    @Test
    fun getPicturesFromAlbumId1_requestPicturesFromLocalSource() = runBlocking {
        networkStatusChecker = FakeNetworkStatusChecker(false)
        picturesRepositoryImplementation = PicturesRepositoryImplementation(localDataSource,
            remoteDataSource, networkStatusChecker)

        val picturesAlbum1 = picturesRepositoryImplementation.getPicturesFromAlbumId(1)
        assertEquals(picturesAlbum1, localPicturesByAlbums[album1])
    }

    @Test
    fun errorOnGetPicturesFromAlbumId1_requestPicturesFromLocalSource() = runBlocking {
        networkStatusChecker = FakeNetworkStatusChecker(false)
        localDataSource = FakeLocalDataSource(null,
            null, null, null)

        picturesRepositoryImplementation = PicturesRepositoryImplementation(localDataSource,
            remoteDataSource, networkStatusChecker)

        val picturesAlbum1 = picturesRepositoryImplementation.getPicturesFromAlbumId(1)

        assertTrue(picturesAlbum1.isEmpty())
    }

    @Test
    fun getAllUserPosts_requestPostsFromRemoteSource() = runBlocking {
        networkStatusChecker = FakeNetworkStatusChecker(true)

        picturesRepositoryImplementation = PicturesRepositoryImplementation(localDataSource,
            remoteDataSource, networkStatusChecker)

        val posts = picturesRepositoryImplementation.getUserPosts()

        assertEquals(posts, remotePosts)
    }

    @Test
    fun errorOnGetAllUserPosts_requestPostsFromRemoteSource() = runBlocking {
        networkStatusChecker = FakeNetworkStatusChecker(true)
        remoteDataSource = FakeRemoteDataSource(null,
            null, null, null)

        picturesRepositoryImplementation = PicturesRepositoryImplementation(localDataSource,
            remoteDataSource, networkStatusChecker)

        val posts = picturesRepositoryImplementation.getUserPosts()

        assertEquals(posts, localPosts)
    }

    @Test
    fun getAllUserPosts_requestPostsFromLocalSource() = runBlocking {
        networkStatusChecker = FakeNetworkStatusChecker(false)

        picturesRepositoryImplementation = PicturesRepositoryImplementation(localDataSource,
            remoteDataSource, networkStatusChecker)

        val posts = picturesRepositoryImplementation.getUserPosts()

        assertEquals(posts, localPosts)
    }

}