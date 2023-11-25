package com.example.tripnila.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tripnila.data.Staycation
import com.example.tripnila.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class StaycationManagerViewModel(private val repository: UserRepository = UserRepository()) : ViewModel() {

    private val _staycation = MutableStateFlow(Staycation()) // Initialize with an empty Host
    val staycation = _staycation.asStateFlow()



    fun getSelectedStaycation(staycationId: String) {
        viewModelScope.launch {
            val staycation = repository.getStaycationById(staycationId)
            _staycation.value = staycation ?: Staycation()
        }
    }

}