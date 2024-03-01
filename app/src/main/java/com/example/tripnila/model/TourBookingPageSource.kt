package com.example.tripnila.model

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.tripnila.data.StaycationBooking
import com.example.tripnila.data.TourBooking
import com.example.tripnila.repository.UserRepository

class TourBookingPageSource(
    private val userRepository: UserRepository,
    private val touristId: String,
    private val initialLoadSize: Int
): PagingSource<Int, TourBooking>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, TourBooking> {
        return try {
            val currentPageNumber = params.key ?: 0
            val pageSize = params.loadSize

            Log.d("PS", "running")

            val tourBookings = userRepository.getTourBookingsWithPaging(
                touristId = touristId,
                pageNumber = currentPageNumber,
                pageSize = pageSize,
                initialLoadSize = initialLoadSize
            )

            Log.d("PS", tourBookings.toString())

            LoadResult.Page(
                data = tourBookings,
                prevKey = if (currentPageNumber == 0) null else currentPageNumber - 1,
                nextKey = if (tourBookings.isEmpty()) null else currentPageNumber + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, TourBooking>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

}