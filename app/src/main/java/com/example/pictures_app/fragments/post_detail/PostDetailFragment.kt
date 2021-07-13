package com.example.pictures_app.fragments.post_detail

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.pictures_app.PicturesApplication
import com.example.pictures_app.R
import com.example.pictures_app.databinding.FragmentPostDetailBinding
import com.example.pictures_app.model.PostModel
import com.example.pictures_app.utils.Notifier
import com.example.pictures_app.utils.toast

class PostDetailFragment : Fragment() {

    private val postDetailFragmentViewModel by viewModels<PostDetailFragmentViewModel> {
        PostDetailFragmentViewModelFactory(safeArguments.elementId, PicturesApplication.picturesRepository)
    }
    private lateinit var postDetailFragmentViewModelFactory: PostDetailFragmentViewModelFactory

    private var _binding: FragmentPostDetailBinding? = null
    private val binding get() = _binding!!

    private val safeArguments: PostDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPostDetailBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setHasOptionsMenu(true)

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
        postDetailFragmentViewModel.post.observe(viewLifecycleOwner, { post->
            if (post != null) {
                setDetailPost(post)
            } else {
                onGetPostFailed()
            }
        })
    }

    private fun setDetailPost(post: PostModel) {
        binding.detailPostTitleTv.text = post.postTitle
        binding.detailPostBodyTv.text = post.postBody
    }

    private fun onGetPostFailed() {
        activity?.toast(getString(R.string.error_message))
    }
}