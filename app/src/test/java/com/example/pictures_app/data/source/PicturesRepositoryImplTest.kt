package com.example.pictures_app.data.source

import android.util.Log
import com.example.pictures_app.data.PicturesAlbumsPostsDataSource
import com.example.pictures_app.database.LocalDataSource
import com.example.pictures_app.model.*
import com.example.pictures_app.networking.NetworkStatusCheckerInterface
import com.example.pictures_app.repository.PicturesRepositoryImplementation
import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class PicturesRepositoryImplTest {
    private val FIRST_ALBUM_LONG: Long = 1L
    private val SECOND_ALBUM_LONG: Long = 2L
    private val USER_ID_1L: Long = 1L

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

    val localDataSourceMk = mockk<LocalDataSource>()

    val remoteDataSourceMk = mockk<PicturesAlbumsPostsDataSource>()

    val networkStatusCheckerMk = mockk<NetworkStatusCheckerInterface>()

    private lateinit var picturesRepositoryImplementation: PicturesRepositoryImplementation

    @Before
    fun createRepository() {
        remotePicturesByAlbums[album1] = listOf(picture1Album1, picture2Album1)
        remotePicturesByAlbums[album2] = listOf(picture1Album2, picture2Album2)

        localPicturesByAlbums[album1] = listOf(picture2Album1)
        localPicturesByAlbums[album2] = listOf(picture2Album2)

        initLocalMock()
        initRemoteMock()
        initNetworkStatusCheckerTrueMock()

        picturesRepositoryImplementation = PicturesRepositoryImplementation(localDataSourceMk,
            remoteDataSourceMk, networkStatusCheckerMk)
    }

    private fun initLocalMock() {

        coEvery { localDataSourceMk.getAlbums(USER_ID_1L) } returns
                Success(localAlbums) andThen  Failure(Throwable())

        coEvery { localDataSourceMk.addAlbumsToDataBase( any() ) } just Runs

        val safeLocalPicturesByAlbums1: List<PictureModel> =
            localPicturesByAlbums[album1]?: listOf(picture2Album1)

        coEvery { localDataSourceMk.getAlbumPhotos(FIRST_ALBUM_LONG) } returns
                Success(safeLocalPicturesByAlbums1) andThen Failure(Throwable())

        coEvery { localDataSourceMk.addPicturesToDataBase( any() ) } just Runs

        coEvery { localDataSourceMk.getPosts(USER_ID_1L) } returns
                Success(localPosts) andThen Failure(Throwable())

        coEvery { localDataSourceMk.addPostsToDataBase( any() ) } just Runs
    }

    private fun initRemoteMock() {

        coEvery { remoteDataSourceMk.getAlbums(USER_ID_1L) } returns
                Success(remoteAlbums) andThen Failure(Throwable())

        val safeRemotePicturesByAlbums1: List<PictureModel> =
            remotePicturesByAlbums[album1]?: listOf(picture1Album1, picture2Album1)

        coEvery { remoteDataSourceMk.getAlbumPhotos(FIRST_ALBUM_LONG) } returns
                Success(safeRemotePicturesByAlbums1) andThen Failure(Throwable())

        coEvery { remoteDataSourceMk.getPosts(USER_ID_1L) } returns
                Success(remotePosts) andThen Failure(Throwable())
    }

    private fun initNetworkStatusCheckerTrueMock() {
        every { networkStatusCheckerMk.hasInternetConnection() } returns true
    }

    private fun networkStatusCheckerFalseMock() {
        every { networkStatusCheckerMk.hasInternetConnection() } returns false
    }

    @Test
    fun `getAllAlbums with network returns remote albums then error returns local albums` () =
        runBlocking {
            var albums =  picturesRepositoryImplementation.getAllAlbums()
            assertEquals(albums, remoteAlbums)

            albums = picturesRepositoryImplementation.getAllAlbums()
            assertEquals(albums, localAlbums)
        }

    @Test
    fun `getAllAlbums without network returns local albums then error returns null` () =
        runBlocking {
            networkStatusCheckerFalseMock()
            var albums = picturesRepositoryImplementation.getAllAlbums()
            assertEquals(albums, localAlbums)

            albums = picturesRepositoryImplementation.getAllAlbums()
            assertEquals(null, albums)
        }

    @Test
    fun `getPicturesFromAlbumId1 with network returns remote pictures from album1 then error returns local pictures from album1` () =
        runBlocking {
            var picturesAlbum1 = picturesRepositoryImplementation.getPicturesFromAlbumId(FIRST_ALBUM_LONG)
            assertEquals(picturesAlbum1, remotePicturesByAlbums[album1])

            picturesAlbum1 = picturesRepositoryImplementation.getPicturesFromAlbumId(FIRST_ALBUM_LONG)
            assertEquals(picturesAlbum1, localPicturesByAlbums[album1])
        }

    @Test
    fun `getPicturesFromAlbumId1 without network returns local pictures from album1 then error returns empty list`() =
        runBlocking {
            networkStatusCheckerFalseMock()
            var picturesAlbum1 = picturesRepositoryImplementation.getPicturesFromAlbumId(FIRST_ALBUM_LONG)
            assertEquals(picturesAlbum1, localPicturesByAlbums[album1])

            picturesAlbum1 = picturesRepositoryImplementation.getPicturesFromAlbumId(FIRST_ALBUM_LONG)
            assertTrue(picturesAlbum1.isEmpty())
        }

    @Test
    fun `getUserPosts with network returns remote posts then error returns local posts` () =
        runBlocking {
            var posts = picturesRepositoryImplementation.getUserPosts()
            assertEquals(posts, remotePosts)

            posts = picturesRepositoryImplementation.getUserPosts()
            assertEquals(posts, localPosts)
        }

    @Test
    fun `getUserPosts without network returns local posts then error returns null` () =
        runBlocking {
            networkStatusCheckerFalseMock()
            var posts = picturesRepositoryImplementation.getUserPosts()
            assertEquals(posts, localPosts)

            posts = picturesRepositoryImplementation.getUserPosts()
            assertEquals(null, posts)
        }

}