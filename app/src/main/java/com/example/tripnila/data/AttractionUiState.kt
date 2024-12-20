package com.example.tripnila.data

data class AttractionUiState(
    val image: Int,
    val name: String,
    val tag: List<String> = emptyList(),
    val distance: Int,
    val price: Double,
    val openingTime: String,
    val itineraryTime: String? = null,
)
