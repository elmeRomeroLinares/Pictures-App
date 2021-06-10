package com.example.pictures_app.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.example.pictures_app.PicturesApplication
import com.example.pictures_app.R
import com.example.pictures_app.adapters.AlbumsViewPagerAdapter
import com.example.pictures_app.databinding.FragmentAlbumsViewPagerBinding
import com.example.pictures_app.model.AlbumPicturesModel
import com.example.pictures_app.utils.toast

class AlbumsViewPagerFragment : Fragment() {

    private lateinit var binding: FragmentAlbumsViewPagerBinding
    private lateinit var viewPagerAdapter: AlbumsViewPagerAdapter
    private val repository = PicturesApplication.picturesRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAlbumsViewPagerBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
    }

    private fun initUi() {
        //set
        getAlbumsList()
    }

    private fun getAlbumsList() {
        repository.getAllAlbums()
        repository.albumsListLiveData?.observe(viewLifecycleOwner, { albumsList ->
            if (!albumsList.isNullOrEmpty()) {
                onAlbumsListReceived(albumsList)
            } else {
                onGetAlbumsListFailed()
            }
        })
    }

    private fun onAlbumsListReceived(albumsList: List<AlbumPicturesModel>) {
        val fragmentList = mutableListOf<Fragment>()
        for (album in albumsList) {
            fragmentList.add(ImagesListFragment(album.albumId))
        }
        setAlbumsViewPager(fragmentList)
    }

    private fun setAlbumsViewPager(fragmentList: List<Fragment>) {
        viewPagerAdapter = AlbumsViewPagerAdapter(
            fragmentList,
            requireActivity().supportFragmentManager,
            lifecycle
        )
        binding.albumsViewPager.adapter = viewPagerAdapter
    }

    private fun onGetAlbumsListFailed() {
        activity?.toast(context?.getString(R.string.failed_to_get_albums))
    }
}