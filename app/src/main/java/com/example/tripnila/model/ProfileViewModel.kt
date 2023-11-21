package com.example.tripnila.model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tripnila.data.Tourist
import com.example.tripnila.repository.UserRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(private val repository: UserRepository = UserRepository()) : ViewModel() {

    private val _currentUser = MutableStateFlow<Tourist?>(null)
    val currentUser: StateFlow<Tourist?> get() = _currentUser

    fun fetchUserData(touristId: String) {
        viewModelScope.launch {
            try {
                val user = repository.getTouristProfile(touristId)
                _currentUser.value = user

                Log.d("ViewModel", "$user")

                Log.d("ViewModel", "${_currentUser.value}")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}