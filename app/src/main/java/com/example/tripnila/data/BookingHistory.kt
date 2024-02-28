package com.example.tripnila.data

import java.util.Date

data class BookingHistory(
    val bookingId: String,
    val ownerImage: String,
    val bookedRental: String,
    val date: String,
    val rentalImage: String,
    val rentalLocation: String,
    val bookedDates: String,
    val guestsNo: Int,
    val totalAmount: Double,
    val rentalStatus: String,
    val isReviewed: Boolean = false,
    val staycationPrice: Double,
    val bookingDuration: Int,
    val checkInDate: Date,
    val checkOutDate: Date,
    val noOfGuests: Int,
    val noOfInfants: Int,
    val noOfPets: Int,
    val hostTouristId: String = "",
    val staycationId: String
)
data class MonthTotal(
    val month: Int,
    val totalAmount: Double
)
