package com.example.pictures_app.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.example.pictures_app.R
import com.example.pictures_app.databinding.ItemPicturesRecyclerViewBinding
import com.example.pictures_app.glide.GlideApp
import com.example.pictures_app.model.PictureModel
import com.example.pictures_app.utils.loadContentByGlide

class PicturesRecyclerViewAdapter(
    private val onItemClickListener: (PictureModel) -> Unit
) : RecyclerView.Adapter<PicturesItemHolder>() {

    private val recyclerViewData: MutableList<PictureModel> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PicturesItemHolder {
        val itemBinding = ItemPicturesRecyclerViewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PicturesItemHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: PicturesItemHolder, position: Int) {
        holder.bindPictureData(recyclerViewData[position], onItemClickListener)
    }

    override fun getItemCount() = recyclerViewData.size

    fun setRecyclerViewData(data: List<PictureModel>) {
        recyclerViewData.clear()
        recyclerViewData.addAll(data)
        notifyDataSetChanged()
    }
}

class PicturesItemHolder(
    private val itemBinding: ItemPicturesRecyclerViewBinding
) : RecyclerView.ViewHolder(itemBinding.root) {
    fun bindPictureData(picture: PictureModel, onItemClickListener: (PictureModel) -> Unit) {
        itemBinding.root.setOnClickListener {
            onItemClickListener(picture)
        }
        itemBinding.picturesThumbnailImageView.loadContentByGlide(picture.pictureThumbnailUrl)
        itemBinding.picturesTitleTextView.text = picture.pictureTitle?: "Unknown"
    }
}