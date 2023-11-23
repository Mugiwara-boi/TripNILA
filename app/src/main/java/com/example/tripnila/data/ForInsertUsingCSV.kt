package com.example.tripnila.data

import com.google.firebase.Timestamp

data class ForInsertUsingCSV(
    val tourist: Tourist = Tourist(),
    val hasDangerousAnimal: Boolean = false,
    val hasSecurityCamera: Boolean = false,
    val hasWeapon: Boolean = false,
    val noOfBathrooms: Int = 1,
    val noOfBedrooms: Int = 1,
    val noOfBeds: Int = 1,
    val noOfGuests: Int = 1,
    val staycationDescription: String = "",
    val staycationLocation: String = "",
    val staycationPrice: Int = 0,
    val staycationSpace: String = "",
    val staycationTitle: String = "",
    val staycationType: String = "",
    val amenities: List<String> = emptyList(),
    val photos: List<Photo> = emptyList(),
    val availableDates: List<Timestamp> = emptyList(),
    val promotions: List<Promotion> = emptyList(),
    val nearbyAttractions: List<String> = emptyList(),
    val staycationTags: List<String> = emptyList()
)
