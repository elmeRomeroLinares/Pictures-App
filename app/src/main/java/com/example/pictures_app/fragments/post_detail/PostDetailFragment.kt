package com.example.pictures_app.fragments.post_detail

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.pictures_app.R
import com.example.pictures_app.databinding.FragmentPostDetailBinding
import com.example.pictures_app.model.PostModel
import com.example.pictures_app.utils.toast

class PostDetailFragment : Fragment() {

    private lateinit var postDetailFragmentViewModel: PostDetailFragmentViewModel
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

        postDetailFragmentViewModelFactory =
            PostDetailFragmentViewModelFactory(safeArguments.elementId)
        postDetailFragmentViewModel =
            ViewModelProvider(this, postDetailFragmentViewModelFactory)
                .get(PostDetailFragmentViewModel::class.java)

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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.detail_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.share_with_dynamic_link -> {
                postDetailFragmentViewModel.sharePostDetailByDynamicLink()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
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