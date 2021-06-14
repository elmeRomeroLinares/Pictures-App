package com.example.pictures_app.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.pictures_app.fragments.ImagesListFragment
import com.example.pictures_app.model.AlbumPicturesModel

class AlbumsViewPagerAdapter(
    private val albumsList: List<AlbumPicturesModel>,
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
): FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount() = albumsList.size

    override fun createFragment(position: Int): Fragment {
        return ImagesListFragment.newInstance(albumsList[position].albumId)
    }
}