package com.example.pictures_app.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class AlbumsViewPagerAdapter(
    private val viewPagerFragmentList: List<Fragment>,
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
): FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount() = viewPagerFragmentList.size

    override fun createFragment(position: Int): Fragment {
        return viewPagerFragmentList[position]
    }

}