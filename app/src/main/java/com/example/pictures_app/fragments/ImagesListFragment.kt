package com.example.pictures_app.fragments

import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pictures_app.PicturesApplication
import com.example.pictures_app.R
import com.example.pictures_app.adapters.PicturesRecyclerViewAdapter
import com.example.pictures_app.databinding.FragmentImagesListBinding
import com.example.pictures_app.model.PictureModel
import com.example.pictures_app.model.Success
import com.example.pictures_app.networking.NetworkStatusChecker
import com.example.pictures_app.utils.gone
import com.example.pictures_app.utils.toast
import com.example.pictures_app.utils.visible
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ImagesListFragment(val albumId: Long?) : Fragment() {

    private val repository = PicturesApplication.picturesRepository
    private lateinit var binding: FragmentImagesListBinding
    private val picturesRecyclerViewAdapter by lazy {
        PicturesRecyclerViewAdapter(::onItemSelected)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentImagesListBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
    }

    private fun initUi() {
        setToolbarText(context?.getString(R.string.pictures))
        setPicturesRecyclerView()
        getPicturesList()
    }

    private fun setToolbarText(toolbarString: String?) {
        binding.toolbarFragmentImagesList.toolbarTextView.text = toolbarString
    }

    private fun setPicturesRecyclerView() {
        binding.imagesListRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.imagesListRecyclerView.adapter = picturesRecyclerViewAdapter
    }

    private fun getPicturesList() {
        binding.imagesListProgressBar.visible()
        if (albumId != null) {
            repository.getPicturesFromAlbumId(albumId)
            repository.picturesListLiveData?.observe(viewLifecycleOwner, { picturesList ->
                if (!picturesList.isNullOrEmpty()) {
                    onPicturesListReceived(picturesList)
                } else {
                    onGetPicturesListFailed()
                }
            })
        } else {
            onGetPicturesListFailed()
        }
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
        findNavController().navigate(R.id.action_albumsViewPagerFragment_to_imageDetailFragment)
    }
}