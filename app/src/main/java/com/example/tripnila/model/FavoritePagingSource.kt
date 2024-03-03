package com.example.tripnila.model

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.tripnila.data.HomePagingItem
import com.example.tripnila.repository.UserRepository

class FavoritePagingSource(
    private val touristId: String,
    private val repository: UserRepository,
    private val initialLoadSize: Int
) : PagingSource<Int, HomePagingItem>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, HomePagingItem> {
        return try {
            val currentPageNumber = params.key ?: 0
            val pageSize = params.loadSize

            val services = repository.getAllFavoriteServicesByTouristId(
                touristId = touristId,
                pageNumber = currentPageNumber,
                pageSize = pageSize,
                initialLoadSize = initialLoadSize,
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
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

}