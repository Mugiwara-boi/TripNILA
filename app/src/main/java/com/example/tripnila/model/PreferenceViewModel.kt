package com.example.tripnila.model

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tripnila.data.Tourist
import com.example.tripnila.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PreferenceViewModel(private val repository: UserRepository = UserRepository()) : ViewModel() {

    private val _preferenceUiState = MutableStateFlow(PreferenceUiState())
    val preferenceUiState: MutableStateFlow<PreferenceUiState> = _preferenceUiState

    private val _currentUser = MutableStateFlow<Tourist?>(null)
    val currentUser: StateFlow<Tourist?> get() = _currentUser.asStateFlow()


    private val _selectedPreferences = MutableStateFlow<List<String>>(emptyList())
    val selectedPreferences: StateFlow<List<String>> get() = _selectedPreferences.asStateFlow()

    fun setSelectedPreference(preferences: List<String>) {
        _preferenceUiState.value = _preferenceUiState.value.copy(selectedPreferences = preferences )
        _selectedPreferences.value = preferences
    }

    private fun validatePreferences(): Boolean {
        return _preferenceUiState.value.selectedPreferences.isNotEmpty()
    }

    fun savePreferences(touristId: String) {
        viewModelScope.launch {
            try {
//
//                Log.d("PreferenceViewModel", "Current User Before: ${_currentUser.value}")
//                _currentUser.value = repository.getCurrentUser()
//                Log.d("PreferenceViewModel", "Current User After: ${_currentUser.value}")
//
//                Log.d("PreferenceViewModel", "Saving preferences")

                //val userId = _currentUser.value?.touristId ?: ""
                val selectedPreferences = _preferenceUiState.value.selectedPreferences

                if (!validatePreferences()) {
                    _preferenceUiState.value = _preferenceUiState.value.copy(
                        isPreferenceAdded = false,
                        preferenceError = "Please select at least one preference."
                    )
                    return@launch
                }

                _preferenceUiState.value = _preferenceUiState.value.copy(isLoading = true, preferenceError = null)

                val success = repository.addTouristPreferences(touristId, selectedPreferences)

                _preferenceUiState.value = _preferenceUiState.value.copy(isPreferenceAdded = success)

                _currentUser.value = repository.getCurrentUser()

                Log.d("PreferenceViewModel", "Current User Last: ${_currentUser.value}")

            } catch (e: Exception) {
                _preferenceUiState.value = _preferenceUiState.value.copy(preferenceError = e.localizedMessage)
                e.printStackTrace()
            } finally {
                _preferenceUiState.value = _preferenceUiState.value.copy(isLoading = false)
                Log.d("PreferenceViewModel", "Preferences saved: $_preferenceUiState")
            }
        }
    }
}

data class PreferenceUiState(
    val selectedPreferences: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val isPreferenceAdded: Boolean? = null,
    val preferenceError: String? = null
)