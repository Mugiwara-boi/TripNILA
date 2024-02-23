package com.example.tripnila.data

data class Inbox(
    val chatId: String,
    val image: String,
    val name: String,
    val inboxPreview: String,
    val lastSender: String,
    val timeSent: Long,
    val receiverId: String
)