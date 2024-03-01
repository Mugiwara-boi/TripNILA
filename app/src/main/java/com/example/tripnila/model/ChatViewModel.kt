package com.example.tripnila.model

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tripnila.data.Business
import com.example.tripnila.data.Chat
import com.example.tripnila.data.Message
import com.example.tripnila.data.Photo
import com.example.tripnila.data.Tourist
import com.example.tripnila.repository.UserRepository
import com.google.firebase.firestore.auth.User
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class ChatViewModel(private val repository: UserRepository = UserRepository()) : ViewModel() {

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages = _messages.asStateFlow()

    private val _chatId = MutableStateFlow("")
    val chatId = _chatId.asStateFlow()

    private val _currentUser = MutableStateFlow(Tourist())
    val currentUser = _currentUser.asStateFlow()

    private val _otherUser = MutableStateFlow(Tourist())
    val otherUser = _otherUser.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    suspend fun setUsers(senderTouristId: String, receiverTouristId: String) {
        _isLoading.value = true
        _messages.value = emptyList()

        _currentUser.value = _currentUser.value.copy(touristId = senderTouristId)

        val receiver = repository.getTouristProfile(receiverTouristId)
        _otherUser.value = receiver ?: Tourist()

        getChatByUserIds()

    }

    private fun getChatByUserIds() {
        viewModelScope.launch {
            try {
                val chat = repository.getChatByUserIds(_currentUser.value.touristId, _otherUser.value.touristId)
                _chatId.value = chat.chatId

                val messages = repository.getMessages(_chatId.value)
                _messages.value = messages

                delay(500)

                _isLoading.value = false
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    fun sendMessage(content: String, imageUris: List<Uri>) {

        viewModelScope.launch {

            val photos = imageUris.map { uri ->
                Photo(photoUri = uri)
            }

            if (_chatId.value == "") {
                _chatId.value = repository.addNewChat(_currentUser.value.touristId, _otherUser.value.touristId)

            }

            val newMessage = repository.sendMessage(_chatId.value, _currentUser.value.touristId, content, photos)

            Log.d("New Message", newMessage.toString())

            val updatedMessages = _messages.value.toMutableList()
            if (newMessage != null) {
                updatedMessages.add(newMessage)
            }

            _messages.value = updatedMessages
        }

    }
//
//    fun setReceiverInfo(touristId: String) {
//        viewModelScope.launch {
//            try {
//
//                val receiver = repository.getTouristProfile(touristId)
//                _otherUser.value = receiver ?: Tourist()
//
//                getChatByUserIds()
//
//
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }
//    }


//    fun getMessages(chatId: String) {
//        viewModelScope.launch {
//            try {
//
//                val messages = repository.getMessages(chatId)
//                _messages.value = messages
//
//
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }
//    }


}
// 02-22-2024 - 9:59 am
// -----------------------------------------------------------------------------
//class ChatViewModel(private val repository: UserRepository = UserRepository()) : ViewModel() {
//
//    private val _messages = MutableStateFlow<List<Message>>(emptyList())
//    val messages = _messages.asStateFlow()
//
//    private val _chatId = MutableStateFlow("")
//    val chatId = _chatId.asStateFlow()
//
//    private val _currentUser = MutableStateFlow(Tourist())
//    val currentUser = _currentUser.asStateFlow()
//
//    private val _otherUser = MutableStateFlow(Tourist())
//    val otherUser = _otherUser.asStateFlow()
//
//    private val _isLoading = MutableStateFlow(false)  // Add this line
//    val isLoading = _isLoading.asStateFlow()
//
//    fun setCurrentUser(touristId: String) {
//
//        _isLoading.value = true
//
//        _currentUser.value = _currentUser.value.copy(touristId = touristId)
//    }
//
//    fun getChatByUserIds() {
//        viewModelScope.launch {
//            try {
//                val chat = repository.getChatByUserIds(_currentUser.value.touristId, _otherUser.value.touristId) ?: Chat("", emptyList())
//                _chatId.value = chat.chatId
//
//                val messages = repository.getMessages(_chatId.value)
//                _messages.value = messages
//
//            } catch (e: Exception) {
//                e.printStackTrace()
//            } finally {
//                _isLoading.value = false
//            }
//        }
//    }
//
//    fun sendMessage(content: String) {
//
//        viewModelScope.launch() {
//
//            val newMessage =
//                repository.sendMessage(_chatId.value, _currentUser.value.touristId, content)
//
//            val updatedMessages = _messages.value.toMutableList()
//            if (newMessage != null) {
//                updatedMessages.add(newMessage)
//            }
//
//            _messages.value = updatedMessages
//        }
//
//    }
//
//    fun setReceiverInfo(touristId: String) {
//        viewModelScope.launch {
//            try {
//                val receiver = repository.getTouristProfile(touristId)
//                _otherUser.value = receiver ?: Tourist()
//
//
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }
//    }
//
//
////    fun getMessages(chatId: String) {
////        viewModelScope.launch {
////            try {
////
////                val messages = repository.getMessages(chatId)
////                _messages.value = messages
////
////
////            } catch (e: Exception) {
////                e.printStackTrace()
////            }
////        }
////    }
//
//
//}
// -----------------------------------------------------------------------------

//                      02-22-2024
// -----------------------------------------------------------------------------
//class ChatViewModel(private val repository: UserRepository = UserRepository()) : ViewModel() {
//
//    private val _messages = MutableStateFlow<List<Message>>(emptyList())
//    val messages = _messages.asStateFlow()
//
//    private val _chatId = MutableStateFlow("")
//    val chatId = _chatId.asStateFlow()
//
//    private val _currentUser = MutableStateFlow(Tourist())
//    val currentUser = _currentUser.asStateFlow()
//
//    private val _otherUser = MutableStateFlow(Tourist())
//    val otherUser = _otherUser.asStateFlow()
//
//    private val _isLoading = MutableStateFlow(false)  // Add this line
//    val isLoading = _isLoading.asStateFlow()
//
//    fun setCurrentUser(touristId: String) {
//
//        _isLoading.value = true
//
//        _currentUser.value = _currentUser.value.copy(touristId = touristId)
//    }
//
//    fun getChatByUserIds() {
//        viewModelScope.launch {
//            try {
//                val chat = repository.getChatByUserIds(_currentUser.value.touristId, _otherUser.value.touristId) ?: Chat("", emptyList())
//                _chatId.value = chat.chatId
//
//                val messages = repository.getMessages(_chatId.value)
//                _messages.value = messages
//
//            } catch (e: Exception) {
//                e.printStackTrace()
//            } finally {
//                _isLoading.value = false
//            }
//        }
//    }
//
//    fun sendMessage(content: String) {
//
//        viewModelScope.launch() {
//
//            val newMessage = repository.sendMessage(_chatId.value, _currentUser.value.touristId, content) ?: null
//
//            val updatedMessages = _messages.value.toMutableList()
//            if (newMessage != null) {
//                updatedMessages.add(newMessage)
//            }
//
//            _messages.value = updatedMessages
//        }
//
//    }
//
//    fun setReceiverInfo(touristId: String) {
//        viewModelScope.launch {
//            try {
//                val receiver = repository.getTouristProfile(touristId)
//                _otherUser.value = receiver ?: Tourist()
//
//
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }
//    }
//
//
//}
// -----------------------------------------------------------------------------