package com.example.tripnila.model

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tripnila.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class VerificationViewModel(private val repository: UserRepository = UserRepository()) : ViewModel() {

    private val _firstValidIdType = MutableStateFlow("")
    val firstValidIdType = _firstValidIdType.asStateFlow()

    private val _secondValidIdType = MutableStateFlow("")
    val secondValidIdType = _secondValidIdType.asStateFlow()

    private val _firstValidId = MutableStateFlow<Uri?>(null)
    val firstValidId = _firstValidId.asStateFlow()

    private val _secondValidId = MutableStateFlow<Uri?>(null)
    val secondValidId = _secondValidId.asStateFlow()

    private val _isUploadSuccessful = MutableStateFlow<Boolean?>(null)
    val isUploadSuccessful = _isUploadSuccessful.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()


    fun setFirstValidIdType(type: String) {
        _firstValidIdType.value = type
    }

    fun setSecondValidIdType(type: String) {
        _secondValidIdType.value = type
    }

    fun setFirstValidId(uri: Uri?) {
        _firstValidId.value = uri
    }

    fun setSecondValidId(uri: Uri?) {
        _secondValidId.value = uri
    }

    fun clearFirstValidId() {
        _firstValidId.value = null
    }

    fun clearSecondValidId() {
        _secondValidId.value = null
    }

    fun uploadIds(
        touristId: String,
    ) {
        viewModelScope.launch {

            _isLoading.value = true


            Log.d("VerificationData", "First Valid ID Type: ${_firstValidIdType.value}")
            Log.d("VerificationData", "First Valid ID Uri: ${_firstValidId.value}")
            Log.d("VerificationData", "Second Valid ID Type: ${_secondValidIdType.value}")
            Log.d("VerificationData", "Second Valid ID Uri: ${_secondValidId.value}")
            Log.d("VerificationData", "Tourist ID: $touristId")
            Log.d("VerificationData", "Verification Status: For Approval")

            val isSuccess = repository.uploadVerificationData(
                firstValidIdType = _firstValidIdType.value,
                firstValidIdUri = _firstValidId.value,
                secondValidIdType = _secondValidIdType.value,
                secondValidIdUri = _secondValidId.value,
                touristId,
                verificationStatus = "For Approval"
            )

            _isUploadSuccessful.value = isSuccess

            _isLoading.value = false
        }

    }

}