package com.example.pictures_app.fragments.image_detail

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.*
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.pictures_app.ELEMENT_ID
import com.example.pictures_app.R
import com.example.pictures_app.databinding.FragmentImageDetailBinding
import com.example.pictures_app.model.PictureModel
import com.example.pictures_app.utils.*
import com.example.pictures_app.utils.SharePictureUtil.getSharePictureIntent

class ImageDetailFragment : Fragment() {

    private lateinit var imageDetailFragmentViewModel: ImageDetailFragmentViewModel
    private lateinit var imageDetailFragmentViewModelFactory: ImageDetailFragmentViewModelFactory

    private var _binding: FragmentImageDetailBinding? = null
    private val binding get() = _binding!!

    private val safeArguments: ImageDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentImageDetailBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setHasOptionsMenu(true)

        imageDetailFragmentViewModelFactory =
            ImageDetailFragmentViewModelFactory(safeArguments.elementId)
        imageDetailFragmentViewModel =
            ViewModelProvider(this, imageDetailFragmentViewModelFactory)
                .get(ImageDetailFragmentViewModel::class.java)

        createNotification()

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initUi() {
        imageDetailFragmentViewModel.picture.observe(viewLifecycleOwner, { picture ->
            if (picture != null) {
                setToolbarText(picture.pictureTitle)
                setDetailPicture(picture)
            } else {
                onGetPictureFailed()
            }
        })
    }

    private fun setToolbarText(pictureTitle: String?) {
        val title = pictureTitle?: getString(R.string.unknown)
        (activity as ActionBarTitleSetter).setTitle(title)
    }

    private fun setDetailPicture(picture: PictureModel) {
        binding.detailPictureImageView.loadContentByGlide(picture.pictureUrl)
            .observe(viewLifecycleOwner, { bitmap ->
                if (bitmap != null) {
                    imageDetailFragmentViewModel.bitmap = bitmap
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
        if(imageDetailFragmentViewModel.bitmap != null) {
            val sharePictureIntent: Intent? =
                getSharePictureIntent(imageDetailFragmentViewModel.bitmap as Bitmap, requireContext())
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

    private fun createNotification() {
        val arg: Bundle = safeArguments.toBundle()
        val pendingIntent = findNavController()
            .createDeepLink()
            .setDestination(R.id.imageDetailFragment)
            .setArguments(arg)
            .createPendingIntent()

        Notifier.postNotification((safeArguments.elementId as String).toLong(), requireContext(), pendingIntent)
    }
}