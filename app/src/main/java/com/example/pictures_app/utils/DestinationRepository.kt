package com.example.pictures_app.utils

import com.example.pictures_app.R

object DestinationRepository {

    data class Destination(
        val name: String,
        val id: Int
    )

    fun getAllowNavDestinations() = listOf<Pair<String, Int>>(
        Pair("imageDetailFragment", R.id.imageDetailFragment),
        Pair("postDetailFragment", R.id.postDetailFragment)
    )


}