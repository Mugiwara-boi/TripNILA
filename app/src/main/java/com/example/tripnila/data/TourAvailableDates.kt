package com.example.tripnila.data

import java.time.LocalDate

data class TourAvailableDates(
    val availabilityId: String,
    val day: String,
    val date: String,
    val startingTime: String,
    val endingTime: String,
    val description: String,
    val price: Double,
    val localDate: LocalDate,
    val remainingSlot: Int,
)
