package com.example.pictures_app.fragments.image_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pictures_app.PicturesApplication
import com.example.pictures_app.R
import com.example.pictures_app.adapters.PicturesRecyclerViewAdapter
import com.example.pictures_app.databinding.FragmentImagesListBinding
import com.example.pictures_app.fragments.albums.AlbumsViewPagerFragmentDirections
import com.example.pictures_app.model.PictureModel
import com.example.pictures_app.utils.ActionBarTitleSetter
import com.example.pictures_app.utils.gone
import com.example.pictures_app.utils.toast
import com.example.pictures_app.utils.visible
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

private const val ALBUM_ID_KEY = "albumKey"

@AndroidEntryPoint
class ImagesListFragment : Fragment() {

    private var _binding: FragmentImagesListBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var imagesListFragmentViewModelFactory:
            ImagesListFragmentViewModel.ImagesListViewModelAssistedFactory

    private val imageListFragmentViewModel by viewModels<ImagesListFragmentViewModel> {
        val albumIdLong: Long? = arguments?.getLong(ALBUM_ID_KEY)
        ImagesListFragmentViewModel.provideFactory(imagesListFragmentViewModelFactory, albumIdLong)
    }

    @Inject
    lateinit var picturesRecyclerViewAdapter: PicturesRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentImagesListBinding.inflate(inflater, container, false)
        return binding.root
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
        imageListFragmentViewModel.albumId.observe(viewLifecycleOwner, { albumIdLong ->
            setToolbarText(albumIdLong)
            if (albumIdLong != null) {
                setPicturesRecyclerView()
            } else {
                onGetPicturesListFailed()
            }
        })
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
        picturesRecyclerViewAdapter.setOnItemClickListener {
            onItemSelected(it)
        }
        getPicturesList()
    }

    private fun getPicturesList() {
        binding.imagesListProgressBar.visible()
        imageListFragmentViewModel.picturesList.observe(viewLifecycleOwner, { picturesList ->
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
        val pictureId: String? = picture.pictureId?.toString()
        findNavController().navigate(
            AlbumsViewPagerFragmentDirections.openImageDetailFragment(pictureId),
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