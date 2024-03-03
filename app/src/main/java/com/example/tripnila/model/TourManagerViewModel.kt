package com.example.tripnila.model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tripnila.data.Staycation
import com.example.tripnila.data.Tour
import com.example.tripnila.data.TourBooking
import com.example.tripnila.data.TourSchedule
import com.example.tripnila.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId

class TourManagerViewModel(private val repository: UserRepository = UserRepository()) : ViewModel() {

    private val _tour = MutableStateFlow(Tour()) // Initialize with an empty Host
    val tour = _tour.asStateFlow()

    private val _isStateRetrieved = MutableStateFlow<Boolean?>(false)
    val isStateRetrieved: StateFlow<Boolean?> = _isStateRetrieved

    private val _tourBookings = MutableStateFlow<List<TourBooking>>(emptyList())
    val tourBookings = _tourBookings.asStateFlow()


    fun getAllReviewsByTourId(tourId: String) {
        viewModelScope.launch {

            val tourBooking = repository.getAllTourReviewsThroughBookings(tourId)
            _tourBookings.value = tourBooking

        }
    }

    fun setSchedule(tourSchedule: TourSchedule){
        viewModelScope.launch {
            try {
                _tour.value = _tour.value.copy(schedule = _tour.value.schedule + tourSchedule)


                val tourId = _tour.value.tourId

                repository.addTourAvailabilityFromManager(tourId, tourSchedule)

                Log.d("Sched", "${_tour.value.schedule}")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun removeSchedule(tourSchedule: TourSchedule){
        viewModelScope.launch {
            try {
                _tour.value = _tour.value.copy(schedule = _tour.value.schedule - tourSchedule)

                val tourId = _tour.value.tourId

                repository.deleteTourAvailabilityFromManager(tourId, tourSchedule)

                Log.d("Sched", "${_tour.value.schedule}")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getSelectedTour(tourId: String) {
        viewModelScope.launch {

            val tour = repository.getTourById(tourId)
            _tour.value = tour

            Log.d("Tour", "${_tour.value}")

            _isStateRetrieved.value = true

        }
    }
}