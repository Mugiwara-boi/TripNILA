package com.example.tripnila.model

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.tripnila.data.Staycation
import com.example.tripnila.data.StaycationBooking
import com.example.tripnila.data.Tourist
import com.example.tripnila.repository.UserRepository
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await
import java.util.Date

class BookingHistoryPagingSource(
    private val repository: UserRepository,
    private val touristId: String
) : PagingSource<Int, StaycationBooking>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, StaycationBooking> {
        return try {
            val page = params.key ?: 0
            val pageSize = params.loadSize
            val startAt = page * pageSize

            val staycationBookings = repository.getStaycationBookingsForTourist(touristId, startAt, pageSize)

            val prevKey = if (page == 0) null else page - 1
            val nextKey = if (staycationBookings.size < pageSize) null else page + 1

            LoadResult.Page(
                data = staycationBookings,
                prevKey = prevKey,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            Log.e("BookingPagingSource", "Error loading data", e)
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, StaycationBooking>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}
//package com.example.tripnila.model
//
//import android.util.Log
//import androidx.paging.PagingSource
//import androidx.paging.PagingState
//import com.example.tripnila.data.Staycation
//import com.example.tripnila.data.StaycationBooking
//import com.example.tripnila.data.Tourist
//import com.example.tripnila.repository.UserRepository
//
//class BookingHistoryPagingSource(private val repository: UserRepository, private val touristId: String) :
//    PagingSource<Int, StaycationBooking>() {
//
//    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, StaycationBooking> {
//        return try {
//            Log.d("BookingPagingSource", "load called. Params: $params")
////            repository.updateBookingStatusToOngoingIfCheckInDatePassed()
////
////            repository.updateBookingStatusToFinishedIfExpired()
//
//            val page = params.key ?: 0
//            val bookings = repository.getStaycationBookingsForTourist(touristId)
//
//            // Assume you have a unique identifier for each staycation (e.g., staycationId)
//            //val nextPage = if (staycations.isEmpty()) null else page + 1
//
//            val nextPage = if (bookings.isEmpty()) null else page + 1
//            val endOfPaginationReached = bookings.isEmpty()
//
//            LoadResult.Page(
//                data = bookings,
//                prevKey = if (page == 0) null else page - 1,
//                nextKey = nextPage
//            )
//        } catch (e: Exception) {
//            Log.e("BookingPagingSource", "Error loading data", e)
//            LoadResult.Error(e)
//        }
//    }
//
//    override fun getRefreshKey(state: PagingState<Int, StaycationBooking>): Int? {
//        return state.anchorPosition?.let { anchorPosition ->
//            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
//                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
//        }
//    }
//
//}

