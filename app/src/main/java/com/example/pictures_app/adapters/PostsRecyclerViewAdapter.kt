package com.example.pictures_app.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.RecyclerView
import com.example.pictures_app.databinding.ItemPostsRecyclerViewBinding
import com.example.pictures_app.model.PostModel

class PostsRecyclerViewAdapter(
    private val onItemClickListener: (PostModel) -> Unit
) : RecyclerView.Adapter<PostItemHolder>() {

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
        holder.bindPostData(recyclerViewData[position], onItemClickListener)
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
    fun bindPostData(post: PostModel, onItemClickListener: (PostModel) -> Unit) {
        binding.root.setOnClickListener {
            onItemClickListener(post)
        }
        binding.postTitleTv.text = post.postTitle
        binding.postBodyTv.text = post.postBody
    }
}