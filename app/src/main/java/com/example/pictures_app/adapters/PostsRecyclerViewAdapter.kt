package com.example.pictures_app.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.RecyclerView
import com.example.pictures_app.databinding.ItemPostsRecyclerViewBinding
import com.example.pictures_app.model.PostModel
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject
import javax.inject.Singleton

@FragmentScoped
class PostsRecyclerViewAdapter @Inject constructor(
): RecyclerView.Adapter<PostItemHolder>() {

    private val recyclerViewData: MutableList<PostModel> = mutableListOf()
    private var onItemClickListener: ((PostModel) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostItemHolder {
        val itemBinding = ItemPostsRecyclerViewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PostItemHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: PostItemHolder, position: Int) {
        holder.bindPostData(recyclerViewData[position], onItemClickListener)
    }

    override fun getItemCount() = recyclerViewData.size

    fun setRecyclerViewData(data: List<PostModel>) {
        recyclerViewData.clear()
        recyclerViewData.addAll(data)
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(listener: (PostModel) -> Unit) {
        onItemClickListener = listener
    }

}

class PostItemHolder(
    private val binding: ItemPostsRecyclerViewBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bindPostData(post: PostModel, onItemClickListener: ((PostModel) -> Unit)?) {
        binding.root.setOnClickListener {
            onItemClickListener?.let {
                it(post)
            }
        }
        binding.postTitleTv.text = post.postTitle
        binding.postBodyTv.text = post.postBody
    }
}