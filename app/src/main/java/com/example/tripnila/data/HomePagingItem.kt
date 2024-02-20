package com.example.tripnila.data

//data class StaycationItem(
//    val staycationImage: String = "",
//    val hostFirstName: String,
//    val averageReviewRating
//)
//

data class HomePagingItem(
    val serviceId: String = "",
    val serviceCoverPhoto: String = "",
    val serviceTitle: String,
    val averageReviewRating: Double,
    val location: String,
    val price: Double,
    val tourDuration: Int? = null
)

