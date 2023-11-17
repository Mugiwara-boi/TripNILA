package com.example.tripnila.model

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.tripnila.data.Filter
import com.example.tripnila.data.Staycation
import com.example.tripnila.repository.UserRepository
//class HomePagingResource(
//    private val repository: UserRepository,
//    private val filter: Filter
//) : PagingSource<Int, Staycation>() {
//
//    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Staycation> {
//        try {
//            val page = params.key ?: 0
//            val staycations = repository.getAllStaycations(filter, "")
//
//            // Implement logic to paginate the list based on page size
//            val nextPage = if (staycations.size == params.loadSize) page + 1 else null
//
//            return LoadResult.Page(
//                data = staycations,
//                prevKey = if (page > 0) page - 1 else null,
//                nextKey = nextPage
//            )
//        } catch (e: Exception) {
//            return LoadResult.Error(e)
//        }
//    }
//
//    override fun getRefreshKey(state: PagingState<Int, Staycation>): Int? {
//        // Since your key is of type Int, you can return any constant value here
//        // or you can use the first key of the list, if available
//        return state.anchorPosition?.let { anchorPosition ->
//            state.closestPageToPosition(anchorPosition)?.prevKey
//                ?: state.closestPageToPosition(anchorPosition)?.nextKey
//        }
//    }
//}
