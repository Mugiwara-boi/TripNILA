package com.example.tripnila.model

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.tripnila.data.HomePagingItem
import com.example.tripnila.data.Inbox
import com.example.tripnila.repository.UserRepository

class InboxPagingSource(
    private val touristId: String,
    private val repository: UserRepository,
) : PagingSource<Int, Inbox>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Inbox> {
        return try {
            val currentPageNumber = params.key ?: 0
            val pageSize = params.loadSize

            var inbox = repository.getAllChatsByUserId(touristId, currentPageNumber, pageSize)

            inbox = inbox.sortedByDescending { it.timeSent }

            LoadResult.Page(
                data = inbox,
                prevKey = if (currentPageNumber == 0) null else currentPageNumber - 1,
                nextKey = if (inbox.isEmpty()) null else currentPageNumber + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Inbox>): Int? {
        // We don't need to refresh the data here, so just return null
        return null
    }




}
