package com.example.tripnila.data

data class Message(
    val messageId: String,
    val chatId: String,
    val senderId: String,
    val content: String? = null,
    val images: List<Photo>? = null,
    var timestamp: Long
)
