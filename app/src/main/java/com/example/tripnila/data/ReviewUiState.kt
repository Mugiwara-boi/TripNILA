package com.example.tripnila.data

data class ReviewUiState(
    val rating: Double,
    val comment: String,
    val touristImage: String? = null,
    val touristName: String,
    val reviewDate: String
)
