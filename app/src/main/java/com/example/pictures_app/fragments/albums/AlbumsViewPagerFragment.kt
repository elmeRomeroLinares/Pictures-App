package com.example.pictures_app.fragments.albums

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.pictures_app.R
import com.example.pictures_app.adapters.AlbumsViewPagerAdapter
import com.example.pictures_app.databinding.FragmentAlbumsViewPagerBinding
import com.example.pictures_app.model.AlbumPicturesModel
import com.example.pictures_app.utils.ActionBarTitleSetter
import com.example.pictures_app.utils.toast
import com.google.android.material.tabs.TabLayoutMediator

class AlbumsViewPagerFragment : Fragment() {

    private lateinit var albumsViewPagerFragmentViewModel: AlbumsViewPagerFragmentViewModel

    private var _binding: FragmentAlbumsViewPagerBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewPagerAdapter: AlbumsViewPagerAdapter

    private val onPicturesAlbumsChangeListener =  OnPicturesAlbumChangeListener()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAlbumsViewPagerBinding.inflate(inflater, container, false)
        val root: View = binding.root

        albumsViewPagerFragmentViewModel =
            ViewModelProvider(this).get(AlbumsViewPagerFragmentViewModel::class.java)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.albumsViewPager.unregisterOnPageChangeCallback(onPicturesAlbumsChangeListener)
        _binding = null
    }

    private fun initUi() {
        getAlbumsList()
    }

    private fun getAlbumsList() {
        albumsViewPagerFragmentViewModel.viewPagerAlbumsList
            .observe(viewLifecycleOwner, { albumsList ->
                if (!albumsList.isNullOrEmpty()) {
                    setAlbumsViewPager(albumsList)
                } else {
                    onGetAlbumsListFailed()
                }
            })
    }

    private fun setAlbumsViewPager(albumsList: List<AlbumPicturesModel>) {
        viewPagerAdapter = AlbumsViewPagerAdapter(
            albumsList,
            childFragmentManager,
            lifecycle
        )
        binding.albumsViewPager.adapter = viewPagerAdapter
        setTabLayout(albumsList)
        binding.albumsViewPager.registerOnPageChangeCallback(onPicturesAlbumsChangeListener)
        albumsViewPagerFragmentViewModel.lastVisitedPosition
            .observe(viewLifecycleOwner, { lastVisitedPosition ->
                binding.albumsViewPager.currentItem = lastVisitedPosition
            })
    }

    private fun setTabLayout(albumsList: List<AlbumPicturesModel>) {
        TabLayoutMediator(binding.albumsTabLayout, binding.albumsViewPager) { tab, position ->
            tab.text = albumsList[position].albumTitle
        }.attach()
    }

    private fun onGetAlbumsListFailed() {
        activity?.toast(context?.getString(R.string.failed_to_get_albums))
    }

    private fun setActionBarTitle(pagerPosition: Int) {
        var albumIdString: String = getString(R.string.unknown)
        albumsViewPagerFragmentViewModel.viewPagerAlbumsList
            .observe(viewLifecycleOwner, { albumsList ->
                if (!albumsList.isNullOrEmpty()) {
                    albumIdString = albumsList[pagerPosition].albumId.toString()
                }
            })
        val albumTitle: String = getString(R.string.album) + albumIdString
        (activity as ActionBarTitleSetter).setTitle(albumTitle)
    }

    inner class OnPicturesAlbumChangeListener : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            albumsViewPagerFragmentViewModel.lastVisitedPosition.postValue(position)
            setActionBarTitle(position)
        }
    }
}

