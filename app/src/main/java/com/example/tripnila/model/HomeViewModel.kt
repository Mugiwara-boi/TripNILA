package com.example.tripnila.model

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.cachedIn
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.tripnila.data.Filter
import com.example.tripnila.data.Staycation
import com.example.tripnila.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: UserRepository = UserRepository()) : ViewModel() {

    private val _selectedTab = MutableStateFlow("For You") // Default tab
    val selectedTab: StateFlow<String> get() = _selectedTab

    fun selectTab(tab: String) {
        _selectedTab.value = tab
    }

    fun getStaycationsByTab(tab: String): Flow<PagingData<Staycation>> {
        val pagingConfig = PagingConfig(
            pageSize = 10, // Adjust as needed
            enablePlaceholders = false
        )

        return Pager(
            config = pagingConfig,
            pagingSourceFactory = { StaycationPagingSource(repository, tab) }
        ).flow.cachedIn(viewModelScope)
    }

}


//    private var currentFilter: Filter by mutableStateOf(Filter()) // Initial empty filter
//
//    // Use a SharedFlow to emit the selected tab
//    private val _selectedTab = MutableSharedFlow<String>()
//    val selectedTab: SharedFlow<String> = _selectedTab
//
//    // State variable to hold the list of staycations
//    private val _staycations = mutableStateOf<List<Staycation>>(emptyList())
//    val staycations: List<Staycation> get() = _staycations.value
//
//    val staycationsPaging: Flow<PagingData<Staycation>> = selectedTab.flatMapLatest { tab ->
//        Pager(PagingConfig(pageSize = 10)) {
//            when (tab) {
//                "For You" -> HomePagingResource(repository, currentFilter)
//                "Gaming" -> HomePagingResource(repository, currentFilter)
//                "Nature" -> HomePagingResource(repository, currentFilter)
//                "History" -> HomePagingResource(repository, currentFilter)
//                else -> throw IllegalArgumentException("Unsupported tab: $tab")
//            }
//        }.flow.cachedIn(viewModelScope)
//    }
//
//    // Function to update the list of staycations
//    private fun updateStaycations(newStaycations: List<Staycation>) {
//        _staycations.value = newStaycations
//    }
//
//    fun setFilter(filter: Filter) {
//        currentFilter = filter
//    }
//
//    fun selectTab(tab: String) {
//        viewModelScope.launch {
//            _selectedTab.emit(tab)
//        }
//    }
//
//    // Fetch and update staycations when the tab changes
//    init {
//        viewModelScope.launch {
//            selectedTab.collect { tab ->
//                val staycations = repository.getAllStaycations(currentFilter, tab)
//                updateStaycations(staycations)
//                Log.d("HomeViewModel", "$staycations")
//            }
//        }
//    }


//class HomeViewModel(private val repository: UserRepository) : ViewModel() {
//
//    private var currentFilter: Filter by mutableStateOf(Filter()) // Initial empty filter
//
//
//
//    private val _selectedTab = MutableSharedFlow<String>()
//    val selectedTab: SharedFlow<String> = _selectedTab
//
////    val staycations: Flow<PagingData<Staycation>> = Pager(PagingConfig(pageSize = 10)) {
////        HomePagingResource(repository, currentFilter)
////    }.flow.cachedIn(viewModelScope)
//
//    val staycations: Flow<PagingData<Staycation>> = selectedTab.flatMapLatest { tab ->
//        Pager(PagingConfig(pageSize = 10)) {
//            when (tab) {
//                "For You" -> HomePagingResource(repository, currentFilter)
//                "Gaming" -> HomePagingResource(repository, currentFilter)
//                "Nature" -> HomePagingResource(repository, currentFilter)
//                "History" -> HomePagingResource(repository, currentFilter)
//                else -> throw IllegalArgumentException("Unsupported tab: $tab")
//            }
//        }.flow.cachedIn(viewModelScope)
//    }
//
//    fun setFilter(filter: Filter) {
//        currentFilter = filter
//    }
//
//    fun selectTab(tab: String) {
//        viewModelScope.launch {
//            _selectedTab.emit(tab)
//        }
//    }
//}
//

