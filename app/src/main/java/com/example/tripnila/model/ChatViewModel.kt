package com.example.tripnila.model

import androidx.lifecycle.ViewModel
import com.example.tripnila.data.Business
import com.example.tripnila.data.Message
import com.example.tripnila.data.Tourist
import com.example.tripnila.repository.UserRepository
import com.google.firebase.firestore.auth.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ChatViewModel(private val repository: UserRepository = UserRepository()) : ViewModel() {

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages = _messages.asStateFlow()

    private val _currentUser = MutableStateFlow(Tourist())
    val currentUser = _currentUser.asStateFlow()

    private val _otherUser = MutableStateFlow(Tourist())
    val otherUser = _otherUser.asStateFlow()

    fun sendMessage(content: String) {
        val newMessage = Message(
            messageId = "${System.currentTimeMillis()}",
            senderId = _currentUser.value.touristId,
            content = content,
            timestamp = System.currentTimeMillis()
        )

        val updatedMessages = _messages.value.toMutableList()
        updatedMessages.add(newMessage)

        _messages.value = updatedMessages
    }


}