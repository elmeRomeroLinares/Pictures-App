package com.example.pictures_app.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.pictures_app.PicturesApplication
import com.example.pictures_app.R
import com.example.pictures_app.adapters.AlbumsViewPagerAdapter
import com.example.pictures_app.databinding.FragmentAlbumsViewPagerBinding
import com.example.pictures_app.model.AlbumPicturesModel
import com.example.pictures_app.utils.toast
import com.google.android.material.tabs.TabLayoutMediator

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
        setAlbumsViewPager(albumsList)
    }

    private fun setAlbumsViewPager(albumsList: List<AlbumPicturesModel>) {
        viewPagerAdapter = AlbumsViewPagerAdapter(
            albumsList,
            childFragmentManager,
            lifecycle
        )
        binding.albumsViewPager.adapter = viewPagerAdapter
        setTabLayout(albumsList)
    }

    private fun setTabLayout(albumsList: List<AlbumPicturesModel>) {
        TabLayoutMediator(binding.albumsTabLayout, binding.albumsViewPager) { tab, position ->
            tab.text = albumsList[position].albumTitle

        }.attach()
    }

    private fun onGetAlbumsListFailed() {
        activity?.toast(context?.getString(R.string.failed_to_get_albums))
    }
}