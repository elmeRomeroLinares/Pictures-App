package com.example.pictures_app.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pictures_app.PicturesApplication
import com.example.pictures_app.R
import com.example.pictures_app.adapters.PostsRecyclerViewAdapter
import com.example.pictures_app.databinding.FragmentPostsListBinding
import com.example.pictures_app.model.PostModel
import com.example.pictures_app.utils.ActionBarTitleSetter
import com.example.pictures_app.utils.gone
import com.example.pictures_app.utils.toast
import com.example.pictures_app.utils.visible

class PostsListFragment : Fragment() {

    private val postsRecyclerViewAdapter by lazy { PostsRecyclerViewAdapter() }
    private lateinit var binding: FragmentPostsListBinding
    private val repository = PicturesApplication.picturesRepository


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPostsListBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
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
        repository.getUserPosts()
        repository.postsListLiveData.observe(viewLifecycleOwner, { postsList ->
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
}