package com.example.tripnila.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tripnila.data.Host
import com.example.tripnila.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HostDashboardViewModel(private val repository: UserRepository = UserRepository()) : ViewModel() {

    private val _host = MutableStateFlow(Host()) // Initialize with an empty Host
    val host = _host.asStateFlow()

    // Setter function to update the host
    fun updateHost(newHost: Host) {
        _host.value = newHost
    }

    fun getHostDetailsByTouristId(touristId: String) {
        viewModelScope.launch {
            try {
                val hostDetails = repository.getHostProfile(touristId)

                if (hostDetails != null) {
                    updateHost(hostDetails)
                } else {
                    Host()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


}