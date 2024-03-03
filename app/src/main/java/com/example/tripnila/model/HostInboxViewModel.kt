package com.example.tripnila.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.tripnila.data.Inbox
import com.example.tripnila.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class HostInboxViewModel(private val repository: UserRepository = UserRepository()) : ViewModel() {

//    private val _inboxData = MutableStateFlow<List<Inbox>>(emptyList())
//    val inboxData = _inboxData.asStateFlow()

    private val _currentUserId = MutableStateFlow("")
    val currentUserId = _currentUserId.asStateFlow()

    private val _isLoading = MutableStateFlow(false)  // Add this line
    val isLoading = _isLoading.asStateFlow()

    fun setCurrentUser(touristId: String) {

        _isLoading.value = true

        _currentUserId.value = touristId
    }

    val inboxPagingData: Flow<PagingData<Inbox>> = Pager(PagingConfig(pageSize = 6)) {
        HostInboxPagingSource(
            _currentUserId.value,
            repository,
        ) // Initial tags
    }.flow.cachedIn(viewModelScope)

}