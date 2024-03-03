package com.example.tripnila.model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tripnila.data.Tour
import com.example.tripnila.data.TourAvailableDates
import com.example.tripnila.data.TourBooking
import com.example.tripnila.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.TimeZone

class TourDetailsViewModel(private val repository: UserRepository = UserRepository()) : ViewModel() {

    private val _tour = MutableStateFlow(Tour())
    val tour = _tour.asStateFlow()

    private val _selectedDate = MutableStateFlow<TourAvailableDates?>(null)
    val selectedDate = _selectedDate.asStateFlow()

    private val _tourBookings = MutableStateFlow<List<TourBooking>>(emptyList())
    val tourBookings = _tourBookings.asStateFlow()

    private val _isUserVerified = MutableStateFlow<Boolean?>(null)
    val isUserVerified = _isUserVerified.asStateFlow()

    private val _personCount = MutableStateFlow(0)
    val personCount = _personCount.asStateFlow()

    private val _tempCount = MutableStateFlow(0)
    val tempCount = _tempCount.asStateFlow()

    private val _isStateRetrieved = MutableStateFlow<Boolean?>(false)
    val isStateRetrieved: StateFlow<Boolean?> = _isStateRetrieved

    private val _bookingResult = MutableStateFlow<String?>(null)
    val bookingResult = _bookingResult.asStateFlow()

    fun setPersonCount(count: Int) {
        _personCount.value = count

    }

    fun setTempCount(count: Int) {
        _tempCount.value = count
    }


    fun incrementCount() {
        _tempCount.value = _tempCount.value + 1
    }

    fun decrementCount() {
        _tempCount.value = _tempCount.value - 1
    }

    fun setSelectedDate(availableDate: TourAvailableDates) {
        _selectedDate.value = availableDate

        Log.d("Selected Schedule", _selectedDate.value.toString())
    }
    fun removeSelectedDate() {
        _selectedDate.value = null

        Log.d("Selected Schedule", _selectedDate.value.toString())
    }

    fun getSelectedTour(tourId: String) {
        viewModelScope.launch {

            val tour = repository.getTourById(tourId)
            _tour.value = tour

            Log.d("Tour", "${_tour.value}")

            _isStateRetrieved.value = true

        }
    }

    fun verifyUser(touristId: String) {
        viewModelScope.launch {
            try {
                val verificationStatus = repository.getTouristVerificationDocument(touristId)

                _isUserVerified.value = verificationStatus

            } catch (e: Exception) {
                e.printStackTrace()
            }


        }
    }

    fun getAllReviewsByTourId(tourId: String) {
        viewModelScope.launch {

            val tourBooking = repository.getAllTourReviewsThroughBookings(tourId)
            _tourBookings.value = tourBooking

        }
    }

    suspend fun incrementViewCount(serviceId: String) {
        try {
            repository.incrementViewCount(serviceId, "Tour")
        } catch (e: Exception) {
            Log.e("HomeViewModel", "Error checking favorite", e)
        }
    }

    suspend fun isFavorite(serviceId: String, userId: String): Boolean {
        return try {
            repository.isFavorite(serviceId, userId)
        } catch (e: Exception) {
            Log.e("HomeViewModel", "Error checking favorite", e)
            false
        }
    }

    // Function to toggle favorite status
    suspend fun toggleFavorite(serviceId: String, userId: String, serviceType: String) {
        viewModelScope.launch {
            repository.toggleFavorite(serviceId, userId, serviceType)
        }
    }



    suspend fun addBooking(touristId: String) {
        try {
            viewModelScope.launch {

                val bookingFee = _tour.value.tourPrice

                val productBookingFee = bookingFee * _personCount.value
                val tripNilaFee = productBookingFee * 0.05
                //
                val totalFee = productBookingFee + tripNilaFee

                Log.d("LOCAL DATE", _selectedDate.value!!.localDate.toString())

                val isSuccess = repository.addTourBooking(
                        tourAvailabilityId = _selectedDate.value!!.availabilityId,
                        tourDate = _selectedDate.value!!.localDate.toString(),
                        startTime = _selectedDate.value!!.startingTime,
                        endTime = _selectedDate.value!!.endingTime,
                        noOfGuests = _personCount.value,
                        tourId = _tour.value.tourId,
                        totalAmount = totalFee,
                        touristId = touristId,
                        commission = tripNilaFee,
                        paymentStatus = "Pending",
                        paymentMethod = "Paypal"
                )


                _bookingResult.value = if (isSuccess) {
                    "Booking successful!"
                } else {
                    "Booking failed: An error occurred"
                }

                Log.d("Result", "$_bookingResult")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


}