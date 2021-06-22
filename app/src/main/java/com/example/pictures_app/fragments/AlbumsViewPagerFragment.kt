package com.example.pictures_app.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.pictures_app.PicturesApplication
import com.example.pictures_app.R
import com.example.pictures_app.adapters.AlbumsViewPagerAdapter
import com.example.pictures_app.databinding.FragmentAlbumsViewPagerBinding
import com.example.pictures_app.databinding.ToolbarPicturesAppBinding
import com.example.pictures_app.model.AlbumPicturesModel
import com.example.pictures_app.utils.ActionBarTitleSetter
import com.example.pictures_app.utils.toast
import com.google.android.material.tabs.TabLayoutMediator

const val VIEW_PAGER_LAST_VISITED_ITEM = "VPLastVisitedItem"

class AlbumsViewPagerFragment : Fragment() {

    private lateinit var binding: FragmentAlbumsViewPagerBinding
    private lateinit var viewPagerAdapter: AlbumsViewPagerAdapter
    private val repository = PicturesApplication.picturesRepository
    private var lastVisitedPosition: Int = 0
    private val onPicturesAlbumsChangeListener =  OnPicturesAlbumChangeListener()
    private lateinit var viewPagerAlbumsList: List<AlbumPicturesModel>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAlbumsViewPagerBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        savedInstanceState?.let {
            lastVisitedPosition = it.getInt(VIEW_PAGER_LAST_VISITED_ITEM)
        }
        initUi()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(VIEW_PAGER_LAST_VISITED_ITEM, lastVisitedPosition)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.albumsViewPager.unregisterOnPageChangeCallback(onPicturesAlbumsChangeListener)
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
        viewPagerAlbumsList = albumsList
        setAlbumsViewPager()
    }

    private fun setAlbumsViewPager() {
        viewPagerAdapter = AlbumsViewPagerAdapter(
            viewPagerAlbumsList,
            childFragmentManager,
            lifecycle
        )
        binding.albumsViewPager.adapter = viewPagerAdapter
        setTabLayout()
        binding.albumsViewPager.registerOnPageChangeCallback(onPicturesAlbumsChangeListener)
        binding.albumsViewPager.currentItem = lastVisitedPosition
    }

    private fun setTabLayout() {
        TabLayoutMediator(binding.albumsTabLayout, binding.albumsViewPager) { tab, position ->
            tab.text = viewPagerAlbumsList[position].albumTitle
        }.attach()
    }

    private fun onGetAlbumsListFailed() {
        activity?.toast(context?.getString(R.string.failed_to_get_albums))
    }

    inner class OnPicturesAlbumChangeListener : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            lastVisitedPosition = position
            val albumIdString: String = viewPagerAlbumsList[position].albumId.toString()
            val albumTitle: String = getString(R.string.album) + albumIdString
            (activity as ActionBarTitleSetter).setTitle(albumTitle)
        }
    }
}

