package com.example.tripnila.model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tripnila.data.Comment
import com.example.tripnila.data.Like
import com.example.tripnila.data.Staycation
import com.example.tripnila.data.StaycationBooking
import com.example.tripnila.data.TourBooking
import com.example.tripnila.data.Tourist
import com.example.tripnila.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ReviewViewModel (private val repository: UserRepository = UserRepository()) : ViewModel() {

    private val _staycationBookings = MutableStateFlow<List<StaycationBooking>>(emptyList())
    val staycationBookings: StateFlow<List<StaycationBooking>> = _staycationBookings

    private val _tourBookings = MutableStateFlow<List<TourBooking>>(emptyList())
    val tourBookings = _tourBookings.asStateFlow()

    private val _currentUserProfile = MutableStateFlow(Tourist())
    val currentUserProfile = _currentUserProfile.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    fun getAllReviewsByStaycationId(staycationId: String) {
        viewModelScope.launch {
            _isLoading.value = true

            val staycationBookings = repository.getAllStaycationReviewsThroughBookings(staycationId)
            _staycationBookings.value = staycationBookings

            _isLoading.value = false
        }
    }

    fun getAllReviewsByTourId(tourId: String) {
        viewModelScope.launch {
            _isLoading.value = true

            val tourBooking = repository.getAllTourReviewsThroughBookings(tourId)
            _tourBookings.value = tourBooking

            _isLoading.value = false
        }
    }

    suspend fun setCurrentUser(touristId: String) {

        val currentUser = repository.getTouristProfile(touristId)
        _currentUserProfile.value = currentUser ?: Tourist()

        Log.d("Current user", _currentUserProfile.value.toString())

    }

    fun toggleLike(reviewId: String, serviceType: String) {
        viewModelScope.launch {
            val touristId = repository.toggleLike(reviewId, _currentUserProfile.value.touristId)

            if (serviceType == "Staycation") {
                _staycationBookings.value = _staycationBookings.value.map { staycationBooking ->
                    // Check if the current StaycationBooking contains the review
                    if (staycationBooking.bookingReview?.reviewId == reviewId) {
                        // Create a copy of the StaycationBooking with the updated review
                        staycationBooking.copy(
                            bookingReview = staycationBooking.bookingReview.copy(
                                likes = if (staycationBooking.bookingReview.likes.any { it.touristId == touristId }) {
                                    // Remove the like if it exists
                                    staycationBooking.bookingReview.likes.filterNot { it.touristId == touristId }
                                } else {
                                    // Add the like
                                    staycationBooking.bookingReview.likes + Like("", reviewId, touristId)
                                }
                            )
                        )
                    } else {
                        // Return the original StaycationBooking object if it doesn't contain the review
                        staycationBooking
                    }
                }

            } else {
                _tourBookings.value = _tourBookings.value.map { tourBooking ->
                    // Check if the current StaycationBooking contains the review
                    if (tourBooking.bookingReview?.reviewId == reviewId) {
                        // Create a copy of the StaycationBooking with the updated review
                        tourBooking.copy(
                            bookingReview = tourBooking.bookingReview.copy(
                                likes = if (tourBooking.bookingReview.likes.any { it.touristId == touristId }) {
                                    // Remove the like if it exists
                                    tourBooking.bookingReview.likes.filterNot { it.touristId == touristId }
                                } else {
                                    // Add the like
                                    tourBooking.bookingReview.likes + Like("", reviewId, touristId)
                                }
                            )
                        )
                    } else {
                        // Return the original StaycationBooking object if it doesn't contain the review
                        tourBooking
                    }
                }
            }

        }
    }

    fun addComment(reviewId: String, comment: String, serviceType: String) {
        viewModelScope.launch {

            val newComment = repository.replyToReview(
                reviewId = reviewId,
                comment = comment,
                touristId = _currentUserProfile.value.touristId
            )

            val modifiedComment = newComment.copy(
                commenter = _currentUserProfile.value
            )

            if (serviceType == "Staycation") {
                val updatedStaycationBookings = _staycationBookings.value.map { staycationBooking ->
                    // Check if the current StaycationBooking contains the review
                    if (staycationBooking.bookingReview?.reviewId == reviewId) {
                        // Create a copy of the StaycationBooking with the updated review
                        staycationBooking.copy(
                            bookingReview = staycationBooking.bookingReview.copy(
                                comments = staycationBooking.bookingReview.comments + modifiedComment
                            )
                        )
                    } else {
                        // Return the original StaycationBooking object if it doesn't contain the review
                        staycationBooking
                    }
                }

                _staycationBookings.value = updatedStaycationBookings

            } else {
                val updatedTourBookings = _tourBookings.value.map { tourBooking ->
                    // Check if the current StaycationBooking contains the review
                    if (tourBooking.bookingReview?.reviewId == reviewId) {
                        // Create a copy of the StaycationBooking with the updated review
                        tourBooking.copy(
                            bookingReview = tourBooking.bookingReview.copy(
                                comments = tourBooking.bookingReview.comments + modifiedComment
                            )
                        )
                    } else {
                        // Return the original StaycationBooking object if it doesn't contain the review
                        tourBooking
                    }
                }

                _tourBookings.value = updatedTourBookings
            }

        }
    }


}