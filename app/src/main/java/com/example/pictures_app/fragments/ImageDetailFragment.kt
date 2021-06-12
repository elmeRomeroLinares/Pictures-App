package com.example.pictures_app.fragments

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.example.pictures_app.PicturesApplication
import com.example.pictures_app.R
import com.example.pictures_app.databinding.FragmentImageDetailBinding
import com.example.pictures_app.glide.GlideApp
import com.example.pictures_app.utils.isResourceReady
import com.example.pictures_app.utils.loadContentByGlide
import com.example.pictures_app.utils.toast
import com.example.pictures_app.utils.visible
import okhttp3.internal.wait

class ImageDetailFragment : Fragment() {

    private lateinit var binding: FragmentImageDetailBinding
    private val safeArguments: ImageDetailFragmentArgs by navArgs()
    private val repository = PicturesApplication.picturesRepository
    private var bitmap: Bitmap? = null

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

    override fun onDestroy() {
        super.onDestroy()
        bitmap = null
    }

    private fun initUi() {
        setToolbarText(safeArguments.pictureTitle)
        setDetailPicture()
    }

    private fun setToolbarText(toolbarString: String?) {
        binding.toolbarFragmentImageDetail.toolbarTextView.text = toolbarString
    }

    private fun setDetailPicture() {
        binding.detailPictureImageView.loadContentByGlide(safeArguments.pictureUrl)
            .observe(viewLifecycleOwner, { bitmap ->
                if (bitmap != null) {
                    this.bitmap = bitmap
                }
        })
        isResourceReady.observe(viewLifecycleOwner, { isGlideRequestReady ->
            if (isGlideRequestReady) {
                showShareButton()
            }
        })
    }
    
    private fun showShareButton() {
        binding.shareButton.visible()
        binding.shareButton.setOnClickListener {
            onShareButtonPress()
        }
    }

    private fun onShareButtonPress() {
        if(bitmap != null) {
            val sharePictureIntent: Intent? =
                repository.getSharePictureIntent(bitmap as Bitmap)
            if (sharePictureIntent != null) {
                startActivity(Intent.createChooser(sharePictureIntent, getString(R.string.share_image)))
            } else {
                unableToShareContent()
            }
        } else {
            unableToShareContent()
        }
    }

    private fun unableToShareContent() {
        activity?.toast(getString(R.string.unable_to_share))
    }
}