package com.example.tripnila.model

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.tripnila.data.HomePagingItem
import com.example.tripnila.repository.UserRepository

class ForYouPagingSource(
    private val hostId: String,
    private val repository: UserRepository,
    private val tags: List<String>,
    private val initialLoadSize: Int,

    private val serviceIds: List<String>,

    private val searchText: String,

    private val includeStaycation: Boolean,
    private val includeTour: Boolean,
    private val ecoFriendlyOnly: Boolean,

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

            Log.d("Page Number(PS)", currentPageNumber.toString())
            Log.d("Page Size(PS)", pageSize.toString())
            Log.d("hostId(PS)", hostId)
            Log.d("tags(PS)", tags.toString())

            val tourList = mutableListOf<String>()
            val staycationList = mutableListOf<String>()

            for (id in serviceIds) {
                if (id.startsWith("2")) {
                    tourList.add(id)
                } else {
                    staycationList.add(id)
                }
            }

            Log.d("includeStaycation", includeStaycation.toString())
            Log.d("includeTour", includeTour.toString())

            val filteredServiceIds = when {
                includeStaycation && includeTour -> {
                    Log.d("Include Both", "Include Both")
                    serviceIds
                }
                includeStaycation -> {
                    Log.d("Staycation Only", "Staycation Only")
                    staycationList
                }
                ecoFriendlyOnly -> {
                    Log.d("Staycation Only", "Staycation Only")
                    staycationList
                }
                includeTour -> {
                    Log.d("Tour Only", "Tour Only")
                    tourList
                }
                else -> {
                    Log.d("Empty", "Empty")
                    emptyList()
                }
            }




            val services = repository.getAllServicesFromFilteredList(
                hostId = hostId,
                pageNumber = currentPageNumber,
                pageSize = pageSize,
                initialLoadSize = initialLoadSize,
                serviceIds = filteredServiceIds,
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
                endDate = endDate,
                ecoFriendlyOnly = ecoFriendlyOnly
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
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

}

// 02-26-2024 4:23 PM
//class ForYouPagingSource(
//    private val hostId: String,
//    private val repository: UserRepository,
//    private val tags: List<String>,
//    private val serviceIdSet: SortedSet<String>,
//) : PagingSource<Int, HomePagingItem>() {
//
//    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, HomePagingItem> {
//        return try {
//            val currentPageNumber = params.key ?: 0
//            val pageSize = params.loadSize
//
//            Log.d("Page Number(PS)", currentPageNumber.toString())
//            Log.d("Page Size(PS)", pageSize.toString())
//
//            val services = repository.getAllServicesByTagsWithPaging(hostId, tags, currentPageNumber, pageSize, serviceIdSet)
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
// ---------------------------------------------------------------------------------------------------

  //  override suspend fun load(params: LoadParams<Int>): LoadResult<Int, HomePagingItem> {
//        return try {
//            val currentPageNumber = params.key ?: 1
//            val pageSize = params.loadSize
//
//            // Get the list of services for the current page
//            val services = repository.getAllServicesByTagsWithPaging(tags, currentPageNumber, pageSize)
//
////            val users = userRepository.getUsers(currentPageNumber, pageSize)
//
//            LoadResult.Page(
//                data = services,
//                prevKey = if (currentPageNumber > 1) currentPageNumber - 1 else null,
//                nextKey = if (services.isNotEmpty()) currentPageNumber + 1 else null
//            )
//        } catch (e: Exception) {
//            LoadResult.Error(e)
//        }
//    }

    // YOUTUBE
//    override fun getRefreshKey(state: PagingState<Int, HomePagingItem>): Int? {
//        return state.anchorPosition?.let { position ->
//            val page = state.closestPageToPosition(position)
//            page?.prevKey?.minus(1) ?: page?.nextKey?.plus(1)
//        }
//    }
//    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, HomePagingItem> {
//        return try {
//
//            val page = params.key ?: 1
//            val (services, nextPage) = repository
//
//
//            LoadResult.Page(
//                data = services.map {
//
//                },
//                prevKey = if (page > 1) page - 1 else null,
//                nextKey = nextPage
//            )
//        } catch (e: Exception) {
//            LoadResult.Error(e)
//        }
//    }




//    override fun getRefreshKey(state: PagingState<Int, HomePagingItem>): Int? {
//        return state.anchorPosition?.let { position ->
//            state.closestPageToPosition(position)?.prevKey?.plus(1)
//                ?: state.closestPageToPosition(position)?.nextKey?.minus(1)
//        }
//    }

//    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, HomePagingItem> {
//        return try {
//            val currentPage = params.key ?: 0
//            val nextPage = currentPage + 1
//
//            val services = repository.getServiceForHome()
//
//            LoadResult.Page(
//                data = services,
//                prevKey = if (currentPage == 0) null else currentPage - 1,
//                nextKey = nextPage
//            )
//        } catch (e: Exception) {
//            LoadResult.Error(e)
//        }
//    }

//override fun getRefreshKey(state: PagingState<QuerySnapshot, HomePagingItem>): QuerySnapshot? {
//    TODO("Not yet implemented")
//}
//
//override suspend fun load(params: PagingSource.LoadParams<QuerySnapshot>): PagingSource.LoadResult<QuerySnapshot, HomePagingItem> {
//    return try {
//        val currentPage = params.key ?: repository.db.collection("service")
//            .orderBy("serviceType", Query.Direction.ASCENDING)
//            .limit(params.loadSize.toLong())
//            .get()
//            .await()
//
//        val lastDocumentSnapshot = currentPage.documents[currentPage.size() - 1]
//        val nextPage = repository.db.collection("service")
//            .orderBy("serviceType", Query.Direction.ASCENDING)
//            .startAfter(lastDocumentSnapshot)
//            .limit(params.loadSize.toLong())
//            .get()
//            .await()
//
//        val staycationList = mutableListOf<HomePagingItem>()
//        for (doc in currentPage) {
//            val serviceId = doc.getString("serviceId") ?: ""
//            val staycationDoc = repository.db.collection("staycation").document(serviceId).get().await()
//
//            val staycation = HomePagingItem(
//                serviceId = serviceId,
//                serviceTitle = staycationDoc.getString("staycationTitle") ?: "",
//                averageReviewRating = staycationDoc.getDouble("averageReviewRating") ?: 0.0,
//                location = staycationDoc.getString("staycationLocation") ?: "",
//                price = staycationDoc.getDouble("staycationPrice") ?: 0.0,
//            )
//
//            staycationList.add(staycation)
//        }
//
//        PagingSource.LoadResult.Page(
//            data = staycationList,
//            prevKey = null,
//            nextKey = nextPage
//        )
//    } catch (e: Exception) {
//        PagingSource.LoadResult.Error(e)
//    }
//}