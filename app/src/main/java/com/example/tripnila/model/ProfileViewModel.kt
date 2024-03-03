package com.example.tripnila.model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tripnila.data.Tourist
import com.example.tripnila.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(private val repository: UserRepository = UserRepository()) : ViewModel() {

    private val _currentUser = MutableStateFlow<Tourist?>(null)
    val currentUser: StateFlow<Tourist?> get() = _currentUser

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _isUserVerified = MutableStateFlow<Boolean?>(null)
    val isUserVerified = _isUserVerified.asStateFlow()

    private val _userStatus = MutableStateFlow("")


    suspend fun isUserVerified() {
        viewModelScope.launch {
            val result = repository.getTouristVerificationDocument(_currentUser.value!!.touristId)

            Log.d("CALLED", "CALLED")
        }
    }

    fun setToNull() {
        _isUserVerified.value = null
    }

    fun fetchUserData(touristId: String) {
        viewModelScope.launch {

            _isLoading.value = true

            try {
                val user = repository.getTouristProfile(touristId)
                _currentUser.value = user
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }


        }
    }

}