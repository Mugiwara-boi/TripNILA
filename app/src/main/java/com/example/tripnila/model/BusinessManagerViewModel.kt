package com.example.tripnila.model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tripnila.data.Business
import com.example.tripnila.data.Tour
import com.example.tripnila.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BusinessManagerViewModel(private val repository: UserRepository = UserRepository()) : ViewModel() {

    private val _business = MutableStateFlow(Business()) // Initialize with an empty Host
    val business = _business.asStateFlow()

    private val _isStateRetrieved = MutableStateFlow<Boolean?>(false)
    val isStateRetrieved: StateFlow<Boolean?> = _isStateRetrieved

    fun getSelectedBusiness(businessId: String) {
        viewModelScope.launch {

            val business = repository.getBusinessById(businessId)
            _business.value = business

            Log.d("BusinessManagerViewModel", "${_business.value}")

            _isStateRetrieved.value = true

        }
    }


}

