package com.example.tripnila.data

data class DailySchedule (
    val day: String = "",
    val openingTime: String? = null,
    val closingTime: String? = null,
    val isOpen: Boolean = false
)
