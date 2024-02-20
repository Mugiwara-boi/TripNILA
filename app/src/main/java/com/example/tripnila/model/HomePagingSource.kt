package com.example.tripnila.model

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.tripnila.data.HomePagingItem
import com.example.tripnila.data.Tag
import com.example.tripnila.repository.UserRepository
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await
import java.util.SortedSet

class HomePagingSource(
    private val repository: UserRepository,
    private val tags: List<String>,
    private val serviceIdSet: SortedSet<String>,
  //  private val initialLoadSize: Int
) : PagingSource<Int, HomePagingItem>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, HomePagingItem> {
        return try {
            val currentPageNumber = params.key ?: 0
            val pageSize = params.loadSize

            Log.d("Page Number(PS)", currentPageNumber.toString())
            Log.d("Page Size(PS)", pageSize.toString())

           // repository.getAllUniqueServiceId()

            val services = repository.getAllServicesByTagsWithPaging(tags, currentPageNumber, pageSize, serviceIdSet)

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