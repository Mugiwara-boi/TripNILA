package com.example.tripnila.data

import com.example.tripnila.R

/*TODO*/ // RENAME TO FACILITY
data class AmenityBrief(
    val image: Int,
    val count: Int? = null,
    val name: String
)

fun setImageForAmenity(amenityName: String): Int {
    return when (amenityName) {
        "Gym" -> R.drawable.gym
      //  "Parking" -> R.drawable.
        "Kitchen" -> R.drawable.kitchen
        "Drinks" -> R.drawable.drinks
        "Wifi" -> R.drawable.wifi
        // Add more cases for other amenities
        else -> R.drawable.resource_default // Default image if the amenity name doesn't match
    }
}