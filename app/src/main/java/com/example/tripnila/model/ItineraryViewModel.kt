package com.example.tripnila.model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tripnila.data.Business
import com.example.tripnila.data.Staycation
import com.example.tripnila.data.StaycationBooking
import com.example.tripnila.data.Tour
import com.example.tripnila.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ItineraryViewModel(private val repository: UserRepository = UserRepository()) : ViewModel() {

    private val _staycationBookings = MutableStateFlow<List<StaycationBooking>>(emptyList()) // Initialize with an empty Host
    val staycationBookings = _staycationBookings.asStateFlow()

    private val _businesses = MutableStateFlow<List<Business>>(emptyList()) // Initialize with an empty Host
    val businesses = _businesses.asStateFlow()

    private val _tours = MutableStateFlow<List<Tour>>(emptyList()) // Initialize with an empty Host
    val tours = _tours.asStateFlow()

    private val _selectedStaycationBooking = MutableStateFlow(StaycationBooking()) // Initialize with an empty Host
    val selectedStaycationBooking = _selectedStaycationBooking.asStateFlow()

    private val _budget = MutableStateFlow(0)
    val budget = _budget.asStateFlow()

    fun setSelectedStaycation(bookingId: String) {
        _selectedStaycationBooking.value = _selectedStaycationBooking.value.copy(staycationBookingId = bookingId)
        Log.d("ViewModel" , "${_selectedStaycationBooking.value}")
    }

    fun clearSelectedStaycation() {
        _selectedStaycationBooking.value = StaycationBooking()
        Log.d("ViewModel" , "${_selectedStaycationBooking.value}")
    }



    fun getItinerarySuggestions() {
        viewModelScope.launch {
            try {

                val businesses = repository.getAllBusinesses()
                val tours = repository.getAllTours()

                _businesses.value = businesses
                _tours.value = tours

            } catch (e: Exception) {

            }
        }
    }


    fun getBookedStaycation(touristId: String) {
        viewModelScope.launch {
            val staycationBookings = repository.getStaycationBookingForItinerary(touristId)
            _staycationBookings.value = staycationBookings

            Log.d("StaycationBookings", "${_staycationBookings.value}")
        }
    }

}
