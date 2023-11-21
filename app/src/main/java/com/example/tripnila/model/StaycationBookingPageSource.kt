package com.example.tripnila.model

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.tripnila.data.Staycation
import com.example.tripnila.data.StaycationBooking
import com.example.tripnila.data.Tourist
import com.example.tripnila.repository.UserRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import java.util.Date

class StaycationBookingPagingSource(
    private val userRepository: UserRepository,
    private val touristId: String

) : PagingSource<Query, StaycationBooking>() {

    companion object {
        private const val PAGE_SIZE = 5
    }

    override suspend fun load(params: LoadParams<Query>): LoadResult<Query, StaycationBooking> {
        try {
            val currentPage = params.key ?: userRepository.db
                .collection("staycation_booking")
                .whereEqualTo("touristId", touristId)
                .orderBy("bookingDate", Query.Direction.DESCENDING)
                .limit(PAGE_SIZE.toLong())

            val snapshot = currentPage.get().await()

          //  val staycationBookings = snapshot.toObjects(StaycationBooking::class.java)

            val staycationBookings = mutableListOf<StaycationBooking>()

            for (document in snapshot.documents) {
                val bookingId = document.id
                val bookingDate = document.getDate("bookingDate") ?: Date()
                val bookingStatus = document.getString("bookingStatus") ?: ""
                val checkInDate = document.getDate("checkInDate") ?: Date()
                val checkOutDate = document.getDate("checkOutDate") ?: Date()
                val noOfGuests = document.getLong("noOfGuests")?.toInt() ?: 0
                val noOfInfants = document.getLong("noOfInfants")?.toInt() ?: 0
                val noOfPets = document.getLong("noOfPets")?.toInt() ?: 0
                val staycationId = document.getString("staycationId") ?: ""
                val totalAmount = document.getLong("totalAmount")?.toDouble() ?: 0.0

                val staycation = userRepository.getStaycationDetailsById(staycationId) ?: Staycation()
                val review = userRepository.getBookingReview(bookingId, touristId)

                val staycationBooking = StaycationBooking(
                    staycationBookingId = bookingId,
                    bookingDate = bookingDate,
                    bookingStatus = bookingStatus,
                    checkInDate = checkInDate,
                    checkOutDate = checkOutDate,
                    noOfGuests = noOfGuests,
                    noOfInfants = noOfInfants,
                    noOfPets = noOfPets,
                    staycation = staycation,
                    totalAmount = totalAmount,
                    tourist = Tourist(touristId = touristId),
                    bookingReview = review
                )

                staycationBookings.add(staycationBooking)
            }


            val nextPage = snapshot.documents.lastOrNull()
            val nextPageQuery = if (nextPage != null) {
                currentPage.startAfter(nextPage)
            } else {
                null
            }

            return LoadResult.Page(
                data = staycationBookings,
                prevKey = null,
                nextKey = nextPageQuery
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Query, StaycationBooking>): Query? {
        // Use the closest page to the first item in the list as the refresh key
        return state.anchorPosition?.let { position ->
            state.closestPageToPosition(position)?.prevKey
        }
    }


}