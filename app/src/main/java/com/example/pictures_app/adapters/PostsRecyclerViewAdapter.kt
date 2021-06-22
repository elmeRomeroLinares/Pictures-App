package com.example.pictures_app.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pictures_app.databinding.ItemPostsRecyclerViewBinding
import com.example.pictures_app.model.PostModel

class PostsRecyclerViewAdapter : RecyclerView.Adapter<PostItemHolder>() {

    private val recyclerViewData: MutableList<PostModel> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostItemHolder {
        val itemBinding = ItemPostsRecyclerViewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PostItemHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: PostItemHolder, position: Int) {
        holder.bindPostData(recyclerViewData[position])
    }

    override fun getItemCount() = recyclerViewData.size

    fun setRecyclerViewData(data: List<PostModel>) {
        recyclerViewData.clear()
        recyclerViewData.addAll(data)
        notifyDataSetChanged()
    }

}

class PostItemHolder(
    private val binding: ItemPostsRecyclerViewBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bindPostData(post: PostModel) {
        binding.postTitleTv.text = post.postTitle
        binding.postBodyTv.text = post.postBody
    }
}