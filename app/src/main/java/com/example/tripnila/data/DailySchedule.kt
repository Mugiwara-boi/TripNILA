package com.example.tripnila.data

data class DailySchedule (
    val day: String = "",
    val openingTime: String = "00:00 AM",
    val closingTime: String? = "00:00 PM",
    val isOpen: Boolean = false
)
