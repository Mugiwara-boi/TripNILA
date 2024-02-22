package com.example.tripnila.data

data class Chat(
    val chatId: String,
    //val participants: List<String>
    val senderId: String,
    val receiverId: String
)
