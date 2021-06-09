package com.example.pictures_app.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.example.pictures_app.PicturesApplication
import com.example.pictures_app.R
import com.example.pictures_app.databinding.FragmentImageDetailBinding
import com.example.pictures_app.databinding.FragmentImagesListBinding
import com.example.pictures_app.glide.GlideApp
import com.example.pictures_app.model.PictureModel
import com.example.pictures_app.utils.loadContentByGlide

class ImageDetailFragment : Fragment() {

    private lateinit var binding: FragmentImageDetailBinding
    private val safeArguments: ImageDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentImageDetailBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
    }

    private fun initUi() {
        setToolbarText(safeArguments.pictureTitle)
        setDetailPicture()
    }

    private fun setToolbarText(toolbarString: String?) {
        binding.toolbarFragmentImageDetail.toolbarTextView.text = toolbarString
    }

    private fun setDetailPicture() {
        val pictureUrlWithExtension = safeArguments.pictureUrl
        binding.detailPictureImageView.loadContentByGlide(pictureUrlWithExtension)
    }
}