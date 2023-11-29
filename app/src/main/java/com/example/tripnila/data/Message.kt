package com.example.tripnila.data

data class Message(
    val messageId: String,
    val senderId: String,
    val content: String,
    val timestamp: Long
)