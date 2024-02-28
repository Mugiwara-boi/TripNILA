package com.example.tripnila.model

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.tripnila.data.BookingHistory
import com.example.tripnila.data.Review
import com.example.tripnila.data.ReviewPhoto
import com.example.tripnila.data.StaycationBooking
import com.example.tripnila.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date

class BookingHistoryViewModel(private val repository: UserRepository = UserRepository()) : ViewModel() {

    private val _staycationBookingList = MutableStateFlow<List<StaycationBooking?>>(emptyList())
    val staycationBookingList: StateFlow<List<StaycationBooking?>> = _staycationBookingList

    private val _selectedStaycationBooking = MutableStateFlow<StaycationBooking?>(null)
    val selectedStaycationBooking: StateFlow<StaycationBooking?> = _selectedStaycationBooking

    private val _selectedBookingHistory = MutableStateFlow<BookingHistory?>(null)
    val selectedBookingHistory: StateFlow<BookingHistory?> = _selectedBookingHistory


    private val _hostId = MutableStateFlow("")
    val hostId = _hostId.asStateFlow()

    fun getStaycationBookingsForTourist(touristId: String): Flow<PagingData<StaycationBooking>> {
        return Pager(
            config = PagingConfig(pageSize = 5, enablePlaceholders = false),
            pagingSourceFactory = { StaycationBookingPagingSource(repository, touristId) }
        ).flow.cachedIn(viewModelScope)
    }

    private val _selectedImageUris = MutableStateFlow<List<Uri>>(emptyList())
    val selectedImageUris: StateFlow<List<Uri>> = _selectedImageUris

    private val _userInputReview = MutableStateFlow<Review>(Review())
    val userInputReview: StateFlow<Review> = _userInputReview

    private val _logCheckOutDatesResult = MutableStateFlow<String>("")
    val logCheckOutDatesResult: StateFlow<String> = _logCheckOutDatesResult

    private val _isLoadingAddReview = MutableStateFlow(false)
    val isLoadingAddReview: StateFlow<Boolean> = _isLoadingAddReview

    private val _isSuccessAddReview = MutableStateFlow<Boolean?>(null)
    val isSuccessAddReview: StateFlow<Boolean?> = _isSuccessAddReview

    private val _addReviewResult = MutableStateFlow<String?>(null)
    val addReviewResult: StateFlow<String?> = _addReviewResult


    private val _alertDialogMessage = MutableStateFlow<String?>(null)
    val alertDialogMessage: StateFlow<String?> get() = _alertDialogMessage

    private val _completedBookings = MutableStateFlow<Map<String, Pair<String, Double>>>(emptyMap())
    val completedBookings = _completedBookings.asStateFlow()

    private val _isLoadingCancelBooking = MutableStateFlow(false)
    val isLoadingCancelBooking: StateFlow<Boolean> = _isLoadingCancelBooking

    private val _isSuccessCancelBooking = MutableStateFlow<Boolean?>(null)
    val isSuccessCancelBooking: StateFlow<Boolean?> = _isSuccessCancelBooking

    fun clearSuccessCancelBooking() {
        _isSuccessCancelBooking.value = null
    }

    fun clearIsSuccessAddReview() {
        _isSuccessAddReview.value = null
    }

    fun clearAddReviewResult() {
        _addReviewResult.value = null
    }

    fun setAlertDialogMessage() {
        _alertDialogMessage.value = "Are you sure you want to proceed?" // No issues, return null for no alert dialog

    }

    fun setSelectedImageUris(uris: List<Uri>) {
      //  _userInputReview.value = _userInputReview.value.copy(reviewPhotos = ReviewPhoto(reviewUri = uris))
        _selectedImageUris.value = uris
        Log.d("Image", "${_selectedImageUris.value}")
    }


    fun setRating(rating: Int) {
        _userInputReview.value?.let {
            val updatedReview = it.copy(rating = rating)
            _userInputReview.value = updatedReview
        }
        Log.d("Comment", "${_userInputReview.value?.rating}")
    }

    fun setComment(comment: String) {
        _userInputReview.value = _userInputReview.value.copy(comment = comment)
        Log.d("Comment", "${_userInputReview.value.comment}")
    }

    fun setPhotos(photos: List<ReviewPhoto>) {
        _userInputReview.value?.let {
            val updatedReview = it.copy(reviewPhotos = photos)
            _userInputReview.value = updatedReview
        }
        Log.d("Comment", "${_userInputReview.value}")
    }

    fun updateStaycationBookingsStatus() {
        viewModelScope.launch {
            try {
                repository.updateBookingStatusToOngoingIfCheckInDatePassed()
                _logCheckOutDatesResult.value = "Fetched successfully."

                val completedBooking = repository.updateBookingStatusToFinishedIfExpired()
                _logCheckOutDatesResult.value = "Fetched successfully."
                _completedBookings.value = repository.processCompletedBookings(completedBooking)
                val hostMap = extractHostTotalMap(_completedBookings.value)
                repository.updatePendingBalance(hostMap)

//                val bookings = repository.getStaycationBookingsForTourist(touristId)
//                _staycationBookingList.value = bookings
                Log.d("Update", "${_completedBookings.value}")
                Log.d("Update", "$hostMap")
            } catch (e: Exception) {
                e.printStackTrace()
                _logCheckOutDatesResult.value = "Fetch failed: ${e.message}"
            }
        }
    }


    fun extractHostTotalMap(staycationDetailsMap: Map<String, Pair<String, Double>>): Map<String, Double> {
        val hostTotalMap = mutableMapOf<String, Double>()

        for ((_, pair) in staycationDetailsMap) {
            val (hostId, totalAmount) = pair
            val cleanedHostId = hostId.removePrefix("HOST-")
            hostTotalMap[cleanedHostId] = hostTotalMap.getOrDefault(cleanedHostId, 0.0) + totalAmount
        }

        return hostTotalMap
    }



//    fun fetchStaycationBookingsForTouristWihPaging(touristId: String): Flow<PagingData<StaycationBooking>> {
//        val pagingConfig = PagingConfig(
//            pageSize = 1, // Adjust as needed
//            enablePlaceholders = false
//        )
//
//        return Pager(
//            config = pagingConfig,
//            pagingSourceFactory = { BookingHistoryPagingSource(repository,touristId) }
//        ).flow.cachedIn(viewModelScope)                                                               //.cachedIn(viewModelScope)
//    }


    

    fun cancelStaycationBooking(bookingId: String, staycationId: String, checkInDate: Date, checkOutDate: Date) {

        viewModelScope.launch {
            try {

                _isLoadingCancelBooking.value = true

                repository.cancelStaycationBooking(bookingId)

                Log.d("CANCELLING", "$staycationId ${checkInDate.time} ${checkOutDate.time}")

                repository.makeAvailabilityInRange(staycationId, checkInDate.time, checkOutDate.time)

                _isSuccessCancelBooking.value = true

            } catch (e: Exception) {
                e.printStackTrace()

                _isSuccessCancelBooking.value = false
            } finally {
                _isLoadingCancelBooking.value = false
            }
        }
    }

    fun getHostId(staycationId:String){
        viewModelScope.launch {
            _hostId.value = repository.getHostIdFromStaycation(staycationId)!!
        }
    }


    fun setStaycationReview(bookingId: String) {
        viewModelScope.launch {
            try {
                _isLoadingAddReview.value = true

                _userInputReview.value?.let { userInputReview ->
                    // Validate that the rating is not empty
                    if (userInputReview.rating <= 0) {
                        _addReviewResult.value = "Please provide a valid rating."
                        _isSuccessAddReview.value = false
                    } else {
                        // Manually assign fields from _userInputReview to parameters
                        val bookingId = bookingId
                        val reviewComment = userInputReview.comment
                        val reviewRating = userInputReview.rating
                        val serviceType = "Staycation"
                        val reviewPhotos = _selectedImageUris.value

                        // Call addStaycationReview with the assigned values
                        val success = reviewComment?.let {
                            repository.addStaycationReview(
                                bookingId = bookingId,
                                reviewComment = it,
                                reviewRating = reviewRating,
                                serviceType = serviceType,
                                reviewPhotos = reviewPhotos
                            )
                        }

                        if (success == true) {
                            _addReviewResult.value = "Review added successfully."
                            _isSuccessAddReview.value = true
                        } else {
                            _addReviewResult.value = "Failed to add review."
                            _isSuccessAddReview.value = false
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _addReviewResult.value = "Error adding review: ${e.message}"
                _isSuccessAddReview.value = false
            } finally {
                _isLoadingAddReview.value = false
            }
        }
    }

}

//fun setStaycationReview(bookingId: String) {
//    viewModelScope.launch {
//        try {
//            _isLoadingAddReview.value = true
//
//            _userInputReview.value?.let { userInputReview ->
//                // Manually assign fields from _userInputReview to parameters
//                val bookingId = _staycationBookingId.value
//                val reviewComment = userInputReview.comment
//                val reviewRating = userInputReview.rating
//                val serviceType = userInputReview.serviceType
//                val reviewPhotos = _selectedImageUris.value
//
//                // Call addStaycationReview with the assigned values
//                val success = reviewComment?.let {
//                    repository.addStaycationReview(
//                        bookingId = bookingId,
//                        reviewComment = it,
//                        reviewRating = reviewRating,
//                        serviceType = serviceType,
//                        reviewPhotos = reviewPhotos
//                    )
//                }
//
//                if (success == true) {
//                    _addReviewResult.value = "Review added successfully."
//                } else {
//                    _addReviewResult.value = "Failed to add review."
//                }
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//            _addReviewResult.value = "Error adding review: ${e.message}"
//        } finally {
//            _isLoadingAddReview.value = false
//        }
//    }
//}

//    fun setStaycationReview() {
//        viewModelScope.launch {
//            try {
//                _userInputReview.value?.let { userInputReview ->
//                    val success = repository.addStaycationReview(userInputReview)
//
//                    if (success) {
//                        _addReviewResult.value = "Review added successfully."
//                    } else {
//                        _addReviewResult.value = "Failed to add review."
//                    }
//                } ?: run {
//                    _addReviewResult.value = "No review data provided."
//                }
//            } catch (e: Exception) {
//                e.printStackTrace()
//                _addReviewResult.value = "Error adding review: ${e.message}"
//            }
//        }
//    }