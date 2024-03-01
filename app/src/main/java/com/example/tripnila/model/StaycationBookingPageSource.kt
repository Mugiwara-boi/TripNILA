package com.example.tripnila.model

import android.util.Log
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
    private val touristId: String,
    private val initialLoadSize: Int
) : PagingSource<Int, StaycationBooking>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, StaycationBooking> {
        return try {
            val currentPageNumber = params.key ?: 0
            val pageSize = params.loadSize

            val staycationBookings = userRepository.getStaycationBookingsWithPaging(
                touristId = touristId,
                pageNumber = currentPageNumber,
                pageSize = pageSize,
                initialLoadSize = initialLoadSize
            )

            LoadResult.Page(
                data = staycationBookings,
                prevKey = if (currentPageNumber == 0) null else currentPageNumber - 1,
                nextKey = if (staycationBookings.isEmpty()) null else currentPageNumber + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, StaycationBooking>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}

//    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, StaycationBooking> {
//        try {
//            val currentPage = params.key ?: userRepository.db
//                .collection("staycation_booking")
//                .whereEqualTo("touristId", touristId)
//                .orderBy("bookingDate", Query.Direction.DESCENDING)
//                .limit(PAGE_SIZE.toLong())
//
//            val snapshot = currentPage.get().await()
//
//            val staycationBookings = mutableListOf<StaycationBooking>()
//
//            for (document in snapshot.documents) {
//                val bookingId = document.id
//                val bookingDate = document.getDate("bookingDate") ?: Date()
//                val bookingStatus = document.getString("bookingStatus") ?: ""
//                val checkInDate = document.getDate("checkInDate") ?: Date()
//                val checkOutDate = document.getDate("checkOutDate") ?: Date()
//                val noOfGuests = document.getLong("noOfGuests")?.toInt() ?: 0
//                val noOfInfants = document.getLong("noOfInfants")?.toInt() ?: 0
//                val noOfPets = document.getLong("noOfPets")?.toInt() ?: 0
//                val staycationId = document.getString("staycationId") ?: ""
//                val totalAmount = document.getLong("totalAmount")?.toDouble() ?: 0.0
//
//                val staycation = userRepository.getStaycationDetailsById(staycationId) ?: Staycation()
//                val review = userRepository.getBookingReview(bookingId, touristId)
//
//                Log.d("Check In Date (DATE)", checkInDate.toString())
//                Log.d("Check Out Date (DATE)", checkOutDate.toString())
//
//                Log.d("Check In Date (TIME)", checkInDate.time.toString())
//                Log.d("Check Out Date (TIME)", checkOutDate.time.toString())
//
//                val staycationBooking = StaycationBooking(
//                    staycationBookingId = bookingId,
//                    bookingDate = bookingDate,
//                    bookingStatus = bookingStatus,
//                    checkInDate = checkInDate,
//                    checkOutDate = checkOutDate,
//                    noOfGuests = noOfGuests,
//                    noOfInfants = noOfInfants,
//                    noOfPets = noOfPets,
//                    staycation = staycation,
//                    totalAmount = totalAmount,
//                    tourist = Tourist(touristId = touristId),
//                    bookingReview = review
//                )
//
//                staycationBookings.add(staycationBooking)
//            }
//
//
//            val nextPage = snapshot.documents.lastOrNull()
//            val nextPageQuery = if (nextPage != null) {
//                currentPage.startAfter(nextPage)
//            } else {
//                null
//            }
//
//            return LoadResult.Page(
//                data = staycationBookings,
//                prevKey = null,
//                nextKey = nextPageQuery
//            )
//        } catch (e: Exception) {
//            return LoadResult.Error(e)
//        }
//    }
//
//    override fun getRefreshKey(state: PagingState<Query, StaycationBooking>): Query? {
//        // Use the closest page to the first item in the list as the refresh key
//        return state.anchorPosition?.let { position ->
//            state.closestPageToPosition(position)?.prevKey
//        }
//    }




// 02-29-2024 8:59 PM
//class StaycationBookingPagingSource(
//    private val userRepository: UserRepository,
//    private val touristId: String
//) : PagingSource<Query, StaycationBooking>() {
//
//    companion object {
//        private const val PAGE_SIZE = 5
//    }
//
//    override suspend fun load(params: LoadParams<Query>): LoadResult<Query, StaycationBooking> {
//        try {
//            val currentPage = params.key ?: userRepository.db
//                .collection("staycation_booking")
//                .whereEqualTo("touristId", touristId)
//                .orderBy("bookingDate", Query.Direction.DESCENDING)
//                .limit(PAGE_SIZE.toLong())
//
//            val snapshot = currentPage.get().await()
//
//            val staycationBookings = mutableListOf<StaycationBooking>()
//
//            for (document in snapshot.documents) {
//                val bookingId = document.id
//                val bookingDate = document.getDate("bookingDate") ?: Date()
//                val bookingStatus = document.getString("bookingStatus") ?: ""
//                val checkInDate = document.getDate("checkInDate") ?: Date()
//                val checkOutDate = document.getDate("checkOutDate") ?: Date()
//                val noOfGuests = document.getLong("noOfGuests")?.toInt() ?: 0
//                val noOfInfants = document.getLong("noOfInfants")?.toInt() ?: 0
//                val noOfPets = document.getLong("noOfPets")?.toInt() ?: 0
//                val staycationId = document.getString("staycationId") ?: ""
//                val totalAmount = document.getLong("totalAmount")?.toDouble() ?: 0.0
//
//                val staycation = userRepository.getStaycationDetailsById(staycationId) ?: Staycation()
//                val review = userRepository.getBookingReview(bookingId, touristId)
//
//                Log.d("Check In Date (DATE)", checkInDate.toString())
//                Log.d("Check Out Date (DATE)", checkOutDate.toString())
//
//                Log.d("Check In Date (TIME)", checkInDate.time.toString())
//                Log.d("Check Out Date (TIME)", checkOutDate.time.toString())
//
//                val staycationBooking = StaycationBooking(
//                    staycationBookingId = bookingId,
//                    bookingDate = bookingDate,
//                    bookingStatus = bookingStatus,
//                    checkInDate = checkInDate,
//                    checkOutDate = checkOutDate,
//                    noOfGuests = noOfGuests,
//                    noOfInfants = noOfInfants,
//                    noOfPets = noOfPets,
//                    staycation = staycation,
//                    totalAmount = totalAmount,
//                    tourist = Tourist(touristId = touristId),
//                    bookingReview = review
//                )
//
//                staycationBookings.add(staycationBooking)
//            }
//
//
//            val nextPage = snapshot.documents.lastOrNull()
//            val nextPageQuery = if (nextPage != null) {
//                currentPage.startAfter(nextPage)
//            } else {
//                null
//            }
//
//            return LoadResult.Page(
//                data = staycationBookings,
//                prevKey = null,
//                nextKey = nextPageQuery
//            )
//        } catch (e: Exception) {
//            return LoadResult.Error(e)
//        }
//    }
//
//    override fun getRefreshKey(state: PagingState<Query, StaycationBooking>): Query? {
//        // Use the closest page to the first item in the list as the refresh key
//        return state.anchorPosition?.let { position ->
//            state.closestPageToPosition(position)?.prevKey
//        }
//    }
//
//
//}