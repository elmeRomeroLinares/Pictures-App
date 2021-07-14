package com.example.pictures_app.fragments.posts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pictures_app.PicturesApplication
import com.example.pictures_app.R
import com.example.pictures_app.adapters.PostsRecyclerViewAdapter
import com.example.pictures_app.databinding.FragmentPostsListBinding
import com.example.pictures_app.model.PostModel
import com.example.pictures_app.utils.gone
import com.example.pictures_app.utils.toast
import com.example.pictures_app.utils.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostsListFragment : Fragment() {

    private val postsListFragmentViewModel by viewModels<PostsListFragmentViewModel> {
        PostsListFragmentViewModelFactory(PicturesApplication.picturesRepository)
    }
    private var _binding: FragmentPostsListBinding? = null
    private val binding get() = _binding!!

    private val postsRecyclerViewAdapter by lazy {
        PostsRecyclerViewAdapter(::onPostSelected)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPostsListBinding.inflate(inflater, container, false)
        val root: View = binding.root

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
        setPostsRecyclerView()
        getPostsList()
    }

    private fun setPostsRecyclerView() {
        binding.postsRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.postsRecyclerView.adapter = postsRecyclerViewAdapter
    }

    private fun getPostsList() {
        binding.postsListProgressBar.visible()
        postsListFragmentViewModel.postsList.observe(viewLifecycleOwner, { postsList ->
            if(!postsList.isNullOrEmpty()) {
                onPostsListReceived(postsList)
            } else {
                onGetPostsListFailed()
            }
        })
    }

    private fun onPostsListReceived(postsList: List<PostModel>) {
        binding.postsListProgressBar.gone()
        postsRecyclerViewAdapter.setRecyclerViewData(postsList)
    }

    private fun onGetPostsListFailed() {
        binding.postsListProgressBar.gone()
        activity?.toast(context?.getString(R.string.failed_to_get_posts))
    }

    private fun onPostSelected(post: PostModel) {
        findNavController().navigate(
            PostsListFragmentDirections
                .actionPostsListFragmentToPostDetailFragment(post.postId.toString())
        )
    }
}