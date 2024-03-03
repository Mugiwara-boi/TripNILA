package com.example.tripnila.data

import com.example.tripnila.R

data class FullReview(
    val image: String,
    val firstName: String,
    val reviewDate: String,
    val rating: Int,
    val reviewComment: String,
    val reviewImages: List<String>,
    val isAlreadyLiked: Boolean,
    val initialLikesCount: Int,
    val initialCommentsCount: Int,
    val comments: List<Comment>,
    val reviewId: String
)
