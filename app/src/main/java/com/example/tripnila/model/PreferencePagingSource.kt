package com.example.tripnila.model

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.tripnila.data.HomePagingItem
import com.example.tripnila.repository.UserRepository
import java.util.SortedSet

class PreferencePagingSource(
    private val hostId: String,
    private val repository: UserRepository,
    private val tag: String,

    private val searchText: String,
    private val includeStaycation: Boolean,
    private val includeTour: Boolean,
    private val houseSelected: Boolean,
    private val apartmentSelected: Boolean,
    private val condoSelected: Boolean,
    private val campSelected: Boolean,
    private val guestHouseSelected: Boolean,
    private val hotelSelected: Boolean,
    private val photoTourSelected: Boolean,
    private val foodTripSelected: Boolean,
    private val barHoppingSelected: Boolean,
    private val selectedRating: Int,
    private val minPrice: String,
    private val maxPrice: String,
    private val city: String,
    private val capacity: String,
    private val bedroomCount: String,
    private val bedCount: String,
    private val bathroomCount: String,
    private val checkedAmenities: List<Boolean>,
    private val checkedOffers: List<Boolean>,
    private val startDate: Long?,
    private val endDate: Long?
) : PagingSource<Int, HomePagingItem>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, HomePagingItem> {
        return try {
            val currentPageNumber = params.key ?: 0
            val pageSize = params.loadSize

           // val services = repository.getAllServicesByTagWithPaging(hostId, tag, currentPageNumber, pageSize)

            val services = repository.getAllServicesByTagWithPaging(
                hostId = hostId,
                tag = tag,
                pageNumber = currentPageNumber,
                pageSize = pageSize,
                searchText = searchText,
                includeStaycation = includeStaycation,
                includeTour = includeTour,
                houseSelected = houseSelected,
                apartmentSelected = apartmentSelected,
                condoSelected = condoSelected,
                campSelected = campSelected,
                guestHouseSelected = guestHouseSelected,
                hotelSelected = hotelSelected,
                photoTourSelected = photoTourSelected,
                foodTripSelected = foodTripSelected,
                barHoppingSelected = barHoppingSelected,
                selectedRating = selectedRating,
                minPrice = minPrice,
                maxPrice = maxPrice,
                city = city,
                capacity = capacity,
                bedroomCount = bedroomCount,
                bedCount = bedCount,
                bathroomCount = bathroomCount,
                checkedAmenities = checkedAmenities,
                checkedOffers = checkedOffers,
                startDate = startDate,
                endDate = endDate
            )


            LoadResult.Page(
                data = services,
                prevKey = if (currentPageNumber == 0) null else currentPageNumber - 1,
                nextKey = if (services.isEmpty()) null else currentPageNumber + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, HomePagingItem>): Int? {
        // We don't need to refresh the data here, so just return null
        return null
    }

}

// 02-27-2024 11:50 am
//class PreferencePagingSource(
//    private val hostId: String,
//    private val repository: UserRepository,
//    private val tag: String,
//) : PagingSource<Int, HomePagingItem>() {
//
//    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, HomePagingItem> {
//        return try {
//            val currentPageNumber = params.key ?: 0
//            val pageSize = params.loadSize
//
//            val services = repository.getAllServicesByTagWithPaging(hostId, tag, currentPageNumber, pageSize)
//
//            LoadResult.Page(
//                data = services,
//                prevKey = if (currentPageNumber == 0) null else currentPageNumber - 1,
//                nextKey = if (services.isEmpty()) null else currentPageNumber + 1
//            )
//        } catch (e: Exception) {
//            LoadResult.Error(e)
//        }
//    }
//
//    override fun getRefreshKey(state: PagingState<Int, HomePagingItem>): Int? {
//        // We don't need to refresh the data here, so just return null
//        return null
//    }
//
//}
//----------------------------------------------------