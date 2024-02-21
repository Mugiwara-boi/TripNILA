package com.example.tripnila.model

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.tripnila.data.Staycation
import com.example.tripnila.data.StaycationBooking
import com.example.tripnila.data.Tag
import com.example.tripnila.repository.UserRepository
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await

//class StaycationPagingSource(private val repository: UserRepository, private val tab: String) :
//    PagingSource<Int, Staycation>() {
//
//    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Staycation> {
//        return try {
//            Log.d("StaycationPagingSource", "load called. Params: $params")
//
//            val page = params.key ?: 0
//            val staycations = repository.getAllStaycationsByTag(tab, page)
//
//            // Assume you have a unique identifier for each staycation (e.g., staycationId)
//            //val nextPage = if (staycations.isEmpty()) null else page + 1
//
//            val nextPage = if (staycations.isEmpty()) null else page + 1
//            val endOfPaginationReached = staycations.isEmpty()
//
//            LoadResult.Page(
//                data = staycations,
//                prevKey = if (page == 0) null else page - 1,
//                nextKey = nextPage
//            )
//        } catch (e: Exception) {
//            Log.e("StaycationPagingSource", "Error loading data", e)
//            LoadResult.Error(e)
//        }
//    }
//
//    override fun getRefreshKey(state: PagingState<Int, Staycation>): Int? {
//        return state.anchorPosition?.let { anchorPosition ->
//            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
//                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
//        }
//    }
//}
