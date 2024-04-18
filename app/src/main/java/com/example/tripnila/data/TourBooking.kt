package com.example.tripnila.data

import java.util.Date

data class TourBooking(
    val tourBookingId: String,
    val tourist: Tourist,
    val bookingDate: Date,
    val startTime: String,
    val endTime: String,
    val noOfGuests: Int,
    val totalAmount: Double,
    val bookingStatus: String,
    val bookingReview: Review? = null,
    val tour: Tour,
    val tourAvailabilityId: String,
    val tourDate: String
)

data class TourBooking1(
    val host: Tourist,
    val tourist: Tourist,
    val tour: Tour1,
    val noOfGuests: Int,
    val startTime: String,
    val endTime: String,
    val tourDate: String,
    val totalAmount: Double,
    val commission: Double,
    val bookingStatus: String,
    val bookingDate: Date
)