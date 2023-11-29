package com.example.tripnila.model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tripnila.data.Business
import com.example.tripnila.data.Host
import com.example.tripnila.data.Staycation
import com.example.tripnila.data.Tour
import com.example.tripnila.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HostDashboardViewModel(private val repository: UserRepository = UserRepository()) : ViewModel() {

    private val _host = MutableStateFlow(Host()) // Initialize with an empty Host
    val host = _host.asStateFlow()

    private val _staycations = MutableStateFlow<List<Staycation>>(emptyList())
    val staycations  = _staycations.asStateFlow()

    private val _tours = MutableStateFlow<List<Tour>>(emptyList())
    val tours  = _tours.asStateFlow()

    private val _businesses = MutableStateFlow<List<Business>>(emptyList())
    val businesses  = _businesses.asStateFlow()

    private val _isStateRetrieved = MutableStateFlow<Boolean?>(false)
    val isStateRetrieved: StateFlow<Boolean?> = _isStateRetrieved

    private val _isLoadingState = MutableStateFlow<Boolean?>(null)
    val isLoadingState: StateFlow<Boolean?> = _isLoadingState





    fun getHostDetailsByTouristId(touristId: String) {
        viewModelScope.launch {
            try {
                val hostDetails = repository.getHostProfile(touristId)
                val staycations = hostDetails?.let { repository.getStaycationsByHostId(it) }
                val businesses = hostDetails?.let { repository.getBusinessesByHostId(it) }
                val tours = hostDetails?.let { repository.getToursByHostId(it) }

                _host.value = hostDetails!!
                _staycations.value = staycations!!
                _businesses.value = businesses!!
                _tours.value = tours!!

                Log.d("Staycation", "${_staycations.value}")

                _isStateRetrieved.value = true


            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }




}