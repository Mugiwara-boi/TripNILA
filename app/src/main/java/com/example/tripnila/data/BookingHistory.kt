package com.example.tripnila.data

data class BookingHistory(
    val ownerImage: Int,
    val bookedRental: String,
    val date: String,
    val rentalImage: Int,
    val rentalLocation: String,
    val bookedDates: String,
    val guestsNo: Int,
    val totalAmount: Double,
    val rentalStatus: String
)
