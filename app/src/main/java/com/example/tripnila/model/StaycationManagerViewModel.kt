package com.example.tripnila.model

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tripnila.data.Amenity
import com.example.tripnila.data.Photo
import com.example.tripnila.data.Promotion
import com.example.tripnila.data.Staycation
import com.example.tripnila.data.StaycationAvailability
import com.example.tripnila.repository.UserRepository
import com.google.firebase.Timestamp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date

class StaycationManagerViewModel(private val repository: UserRepository = UserRepository()) : ViewModel() {

    private val _staycation = MutableStateFlow(Staycation()) // Initialize with an empty Host
    val staycation = _staycation.asStateFlow()

    private val _isStateRetrieved = MutableStateFlow<Boolean?>(false)
    val isStateRetrieved: StateFlow<Boolean?> = _isStateRetrieved

    private val _isLoadingAddAvailability = MutableStateFlow<Boolean?>(null)
    val isLoadingAddAvailability: StateFlow<Boolean?> = _isLoadingAddAvailability

    private val _isSuccessAddAvailability = MutableStateFlow(false)
    val isSuccessAddAvailability: StateFlow<Boolean> = _isSuccessAddAvailability

    private val _isLoadingDeleteAvailability = MutableStateFlow<Boolean?>(null)
    val isLoadingDeleteAvailability: StateFlow<Boolean?> = _isLoadingDeleteAvailability

    private val _isSuccessDeleteAvailability = MutableStateFlow(false)
    val isSuccessDeleteAvailability: StateFlow<Boolean> = _isSuccessDeleteAvailability

    fun addAvailableDate(localDate: LocalDate){
        viewModelScope.launch {
            try {

                _isLoadingAddAvailability.value = true

                val newStaycationAvailability = StaycationAvailability(
                    availableDate = Timestamp(Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant()))
                )

                _staycation.value = _staycation.value.copy(availableDates = _staycation.value.availableDates + newStaycationAvailability)

                val staycationId = _staycation.value.staycationId

                repository.addStaycationAvailabilityFromManager(staycationId, newStaycationAvailability)

                _isSuccessAddAvailability.value = true

            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoadingAddAvailability.value = false // Set loading state to false regardless of success or failure
            }
        }
    }


    fun removeAvailableDate(localDate: LocalDate) {
        viewModelScope.launch {
            try {

                _isLoadingDeleteAvailability.value = true

                val updatedAvailableDates = _staycation.value.availableDates.filterNot {
                    val timestamp = it.availableDate
                    val date = timestamp?.toDate()?.toInstant()?.atZone(ZoneId.systemDefault())?.toLocalDate()
                    date == localDate
                }

                _staycation.value = _staycation.value.copy(availableDates = updatedAvailableDates)

                val staycationId = _staycation.value.staycationId

                repository.removeStaycationAvailabilityFromManager(staycationId, localDate)

                _isSuccessDeleteAvailability.value = true

            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoadingDeleteAvailability.value = false // Set loading state to false regardless of success or failure
            }

        }

    }

    fun getSelectedStaycation(staycationId: String) {
        viewModelScope.launch {
            val staycation = repository.getStaycationById(staycationId)
            _staycation.value = staycation ?: Staycation()

            _isStateRetrieved.value = true

        }
    }

}


//    fun addAvailableDate(localDate: LocalDate){
//
//        val newStaycationAvailability = StaycationAvailability(
//            availableDate = Timestamp(Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant()))
//        )
//
//
//        Log.d("DateList", localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
//
//
//        _staycation.value = _staycation.value.copy(availableDates = _staycation.value.availableDates + newStaycationAvailability)
//        Log.d("Staycation", "${_staycation.value.availableDates.map { it.availableDate }}")
//
//    }

//fun removeAvailableDate(localDate: LocalDate) {
//
//    val updatedAvailableDates = _staycation.value.availableDates.filterNot {
//        val timestamp = it.availableDate
//        val date = timestamp?.toDate()?.toInstant()?.atZone(ZoneId.systemDefault())?.toLocalDate()
//        date == localDate
//    }
//
//    _staycation.value = _staycation.value.copy(availableDates = updatedAvailableDates)
//}