package com.example.tripnila.data

data class Transaction(
    val customerImage: Int,
    val customerName: String,
    val customerUsername: String,
    val guestsCount: Int,
    val price: Double,
    val bookedDate: String,
    val transactionDate: String,
    val transactionStatus: String
)
