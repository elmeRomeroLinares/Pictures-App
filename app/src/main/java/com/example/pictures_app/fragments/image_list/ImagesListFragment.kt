package com.example.pictures_app.fragments.image_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pictures_app.R
import com.example.pictures_app.adapters.PicturesRecyclerViewAdapter
import com.example.pictures_app.databinding.FragmentImagesListBinding
import com.example.pictures_app.fragments.albums.AlbumsViewPagerFragmentDirections
import com.example.pictures_app.model.PictureModel
import com.example.pictures_app.utils.ActionBarTitleSetter
import com.example.pictures_app.utils.gone
import com.example.pictures_app.utils.toast
import com.example.pictures_app.utils.visible

private const val ALBUM_ID_KEY = "albumKey"

class ImagesListFragment : Fragment() {

    private var imageListFragmentViewModel: ImagesListFragmentViewModel? = null
    private var imagesListFragmentViewModelFactory: ImagesListFragmentViewModelFactory? = null

    private var _binding: FragmentImagesListBinding? = null
    private val binding get() = _binding!!

    private val picturesRecyclerViewAdapter by lazy {
        PicturesRecyclerViewAdapter(::onItemSelected)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentImagesListBinding.inflate(inflater, container, false)
        val root: View = binding.root

        arguments?.let {
            imagesListFragmentViewModelFactory =
                ImagesListFragmentViewModelFactory(it.getLong(ALBUM_ID_KEY))

            imageListFragmentViewModel = try {
                ViewModelProvider(
                    this,
                    imagesListFragmentViewModelFactory as ImagesListFragmentViewModelFactory
                ).get(ImagesListFragmentViewModel::class.java)
            } catch (e: Throwable) {
                onGetPicturesListFailed()
                null
            }
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initUi() {
        if (imageListFragmentViewModel != null) {
            (imageListFragmentViewModel as ImagesListFragmentViewModel)
                .albumId.observe(viewLifecycleOwner, { albumIdLong ->
                    setToolbarText(albumIdLong)
                    if (albumIdLong != null) {
                        setPicturesRecyclerView()
                    } else {
                        onGetPicturesListFailed()
                    }
                })
        } else {
            onGetPicturesListFailed()
        }
    }

    private fun setToolbarText(albumIdLong: Long?) {
        val toolbarString: String? = albumIdLong?.toString()
        val title: String = toolbarString ?: getString(R.string.unknown)
        val albumTitle: String = getString(R.string.album) + title
        (activity as ActionBarTitleSetter).setTitle(albumTitle)
    }

    private fun setPicturesRecyclerView() {
        binding.imagesListRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.imagesListRecyclerView.adapter = picturesRecyclerViewAdapter
        getPicturesList()
    }

    private fun getPicturesList() {
        binding.imagesListProgressBar.visible()
        imageListFragmentViewModel?.picturesList?.observe(viewLifecycleOwner, { picturesList ->
            if (picturesList.isNotEmpty()) {
                onPicturesListReceived(picturesList)
            } else {
                onGetPicturesListFailed()
            }
        })
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