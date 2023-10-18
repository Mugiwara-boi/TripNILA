package com.example.tripnila.data

data class Business (
    val businessName: String,
    val businessImage: Int,
    val businessDescription: String,
    val ownerName: String,
    val tags: List<String>,
    val offers: List<String>,
    val menu: Int,
    val promos: List<String>,
    val schedule: List<DailySchedule>,
    val location: String,
    val locationImage: Int,
    val locationDescription: String,
    val review: Review
)


