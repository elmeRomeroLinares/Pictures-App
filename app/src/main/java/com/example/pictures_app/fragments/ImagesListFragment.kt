package com.example.pictures_app.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pictures_app.PicturesApplication
import com.example.pictures_app.R
import com.example.pictures_app.adapters.PicturesRecyclerViewAdapter
import com.example.pictures_app.databinding.FragmentImagesListBinding
import com.example.pictures_app.model.PictureModel
import com.example.pictures_app.utils.ActionBarTitleSetter
import com.example.pictures_app.utils.gone
import com.example.pictures_app.utils.toast
import com.example.pictures_app.utils.visible

private const val ALBUM_ID_KEY = "albumKey"

class ImagesListFragment : Fragment() {

    private val repository = PicturesApplication.picturesRepository
    private lateinit var binding: FragmentImagesListBinding
    private val picturesRecyclerViewAdapter by lazy {
        PicturesRecyclerViewAdapter(::onItemSelected)
    }
    private var albumId: Long? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentImagesListBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            albumId = it.getLong(ALBUM_ID_KEY)
        }
        initUi()
    }

    private fun initUi() {
        setToolbarText(albumId.toString())
        setPicturesRecyclerView()
        getPicturesList()
    }

    private fun setToolbarText(toolbarString: String?) {
        val title: String = toolbarString?: getString(R.string.unknown)
        val albumTitle: String = getString(R.string.album) + title
        (activity as ActionBarTitleSetter).setTitle(albumTitle)
    }

    private fun setPicturesRecyclerView() {
        binding.imagesListRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.imagesListRecyclerView.adapter = picturesRecyclerViewAdapter
    }

    private fun getPicturesList() {
        binding.imagesListProgressBar.visible()
        if (albumId != null) {
            repository.getPicturesFromAlbumId(albumId as Long)
            repository.picturesListLiveData?.observe(viewLifecycleOwner, { picturesList ->
                if (!picturesList.isNullOrEmpty()) {
                    if (isListReceivedAlbumCurrentAlbum(picturesList)) {
                        onPicturesListReceived(picturesList)
                    }
                } else {
                    onGetPicturesListFailed()
                }
            })
        } else {
            onGetPicturesListFailed()
        }
    }

    private fun isListReceivedAlbumCurrentAlbum(picturesList: List<PictureModel>): Boolean {
        return picturesList.firstOrNull()?.pictureAlbumId == albumId
    }

    private fun onPicturesListReceived(picturesList: List<PictureModel>) {
        binding.imagesListProgressBar.gone()
        picturesRecyclerViewAdapter.setRecyclerViewData(picturesList)
    }

    private fun onGetPicturesListFailed() {
        binding.imagesListProgressBar.gone()
        activity?.toast(context?.getString(R.string.failed_to_get_albums))
    }

    private fun onItemSelected(picture: PictureModel) {
        val pictureIdString: String? = if (picture.pictureId != null) {
            picture.pictureId.toString()
        } else {
            null
        }
        findNavController().navigate(
            AlbumsViewPagerFragmentDirections.openImageDetailFragment(pictureIdString),
            navOptions {
                animationBuilder(this)
            }
        )
    }

    private fun animationBuilder(navOptionsBuilder: NavOptionsBuilder) {
        return navOptionsBuilder.anim {
            enter = R.anim.slide_in_right
            exit = R.anim.slide_out_left
            popEnter = R.anim.slide_in_left
            popExit = R.anim.slide_out_right
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(albumId: Long?) =
            ImagesListFragment().apply {
                arguments = Bundle().apply {
                    albumId?.let {
                        putLong(ALBUM_ID_KEY, it)
                    }
                }
            }
    }
}