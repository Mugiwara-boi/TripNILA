package com.example.tripnila.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.tripnila.data.Staycation
import com.example.tripnila.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class StaycationViewModel(private val repository: UserRepository = UserRepository()) : ViewModel() {

    val staycations = Pager(PagingConfig(pageSize = 1)) {
        StaycationPagingSource(repository, "History")
    }.flow.cachedIn(viewModelScope)
}
