package com.example.pictures_app.fragments

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.pictures_app.PicturesApplication
import com.example.pictures_app.R
import com.example.pictures_app.databinding.FragmentImageDetailBinding
import com.example.pictures_app.model.PictureModel
import com.example.pictures_app.utils.*
import com.example.pictures_app.utils.SharePictureUtil.getSharePictureIntent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ImageDetailFragment : Fragment() {

    private lateinit var binding: FragmentImageDetailBinding
    private val safeArguments: ImageDetailFragmentArgs by navArgs()
    private val repository = PicturesApplication.picturesRepository
    private var bitmap: Bitmap? = null
    private var picture: PictureModel? = null

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
        getPicture()
    }

    private fun getPicture() {
        if (safeArguments.pictureId != null) {
            val pictureIdLong = (safeArguments.pictureId as String).toLong()
            GlobalScope.launch(Dispatchers.Main) {
                picture = repository.getPictureById(pictureIdLong)
                setToolbarText(picture?.pictureTitle)
                setDetailPicture()
            }
        } else {
            onGetPictureFailed()
        }
    }

    private fun setToolbarText(pictureTitle: String?) {
        val title = pictureTitle?: getString(R.string.unknown)
        (activity as ActionBarTitleSetter).setTitle(title)
    }

    private fun setDetailPicture() {
        picture?.let{
            binding.detailPictureImageView.loadContentByGlide(it.pictureUrl)
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
                getSharePictureIntent(bitmap as Bitmap, requireContext())
            if (sharePictureIntent != null) {
                startActivity(Intent.createChooser(sharePictureIntent, getString(R.string.share_image)))
            } else {
                unableToShareContent()
            }
        } else {
            unableToShareContent()
        }
    }

    private fun onGetPictureFailed() {
        activity?.toast(getString(R.string.error_message))
        setToolbarText(getString(R.string.error_message))
    }

    private fun unableToShareContent() {
        activity?.toast(getString(R.string.unable_to_share))
    }
}