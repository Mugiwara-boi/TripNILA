package com.example.tripnila.data

data class Transaction(
    val customerImage: String = "https://upload.wikimedia.org/wikipedia/commons/8/89/Portrait_Placeholder.png",
    val bookedRental: String = "",
    val customerName: String,
    val customerUsername: String,
    val guestsCount: Int,
    val price: Double,
    val bookedDate: String,
    val transactionDate: String,
    val transactionStatus: String
)
