package com.example.tripnila.model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.tripnila.data.HomePagingItem
import com.example.tripnila.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FavoriteViewModel(private val repository: UserRepository = UserRepository()) : ViewModel() {


    private val _touristId = MutableStateFlow("")
    val touristId = _touristId.asStateFlow()

    fun setTouristId (touristId: String) {
        _touristId.value = touristId
        Log.d("First", "SETTER")
    }

    suspend fun isFavorite(serviceId: String, userId: String): Boolean {
        return try {
            repository.isFavorite(serviceId, userId)
        } catch (e: Exception) {
            Log.e("FavoriteViewModel", "Error checking favorite", e)
            false
        }
    }

    // Function to toggle favorite status
    suspend fun toggleFavorite(serviceId: String, userId: String, serviceType: String) {
        viewModelScope.launch {
            repository.toggleFavorite(serviceId, userId, serviceType)
        }
    }

    private var favoritePager: Pager<Int, HomePagingItem>? = null

    fun refreshFavoritePagingData() {
        favoritePager = createFavoritePager()
        favoritePagingData = favoritePager!!.flow.cachedIn(viewModelScope)
    }

    private val pageSize = 6
    private val initialLoadSize = 8
    private fun createFavoritePager(): Pager<Int, HomePagingItem> {
        return Pager(PagingConfig(pageSize = pageSize, initialLoadSize = initialLoadSize)) {
            FavoritePagingSource(
                touristId = _touristId.value,
                repository = repository,
                initialLoadSize = initialLoadSize
            )
        }
    }

    var favoritePagingData: Flow<PagingData<HomePagingItem>> =
        createFavoritePager().flow.cachedIn(viewModelScope)


}