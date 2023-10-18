package com.example.tripnila.data

import com.example.tripnila.R

data class FullReview(
    val image: Int,
    val firstName: String,
    val reviewDate: String,
    val rating: Int,
    val reviewComment: String,
    val reviewImage1: Int,
    val reviewImage2: Int,
    val isAlreadyLiked: Boolean,
    val initialLikesCount: Int,
    val initialCommentsCount: Int,
)
