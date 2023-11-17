package com.example.tripnila.model

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tripnila.data.Staycation
import com.example.tripnila.repository.UserRepository
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.format.TextStyle


class DetailViewModel(private val repository: UserRepository = UserRepository()) : ViewModel() {

    private val _staycation = MutableStateFlow<Staycation?>(null)
    val staycation: StateFlow<Staycation?> get() = _staycation

    private val _startDate = MutableStateFlow<Long?>(null)
    val startDate: StateFlow<Long?> = _startDate

    private val _endDate = MutableStateFlow<Long?>(null)
    val endDate: StateFlow<Long?> = _endDate

    private val _nightsDifference = MutableStateFlow<Long?>(null)
    val nightsDifference: StateFlow<Long?> = _nightsDifference

    private val _formattedDateRange = MutableStateFlow<String?>(null)
    val formattedDateRange: StateFlow<String?> = _formattedDateRange

    private val _guestCount = MutableStateFlow<Int?>(0)
    val guestCount: StateFlow<Int?> = _guestCount

    private val _adultCount = MutableStateFlow<Int?>(1)
    val adultCount: StateFlow<Int?> = _adultCount

    private val _childrenCount = MutableStateFlow<Int?>(0)
    val childrenCount: StateFlow<Int?> = _childrenCount

    private val _infantCount = MutableStateFlow<Int?>(0)
    val infantCount: StateFlow<Int?> = _infantCount

    private val _petCount = MutableStateFlow<Int?>(0)
    val petCount: StateFlow<Int?> = _petCount

    private val _clearTrigger = MutableStateFlow(false)
    val clearTrigger: StateFlow<Boolean> = _clearTrigger

    fun setClearTrigger(value: Boolean) {
        _clearTrigger.value = value
    }

    private fun setGuestCount() {
        _guestCount.value = _childrenCount.value?.let { childrenCount -> _adultCount.value?.plus(childrenCount) }
    }

    fun setAdultCount(count: Int?) {
        _adultCount.value = count
        setGuestCount()
    }

    fun setChildrenCount(count: Int?) {
        _childrenCount.value = count
        setGuestCount()
    }

    fun setInfantCount(count: Int?) {
        _infantCount.value = count
    }

    fun setPetCount(count: Int?) {
        _petCount.value = count
    }

    fun setStartDate(milliseconds: Long?) {
        _startDate.value = milliseconds
        updateFormattedDateRange()
    }

    fun setEndDate(milliseconds: Long?) {
        _endDate.value = milliseconds
        updateFormattedDateRange()
    }

    fun setNightsDifference(nights: Long?) {
        _nightsDifference.value = nights
    }


    fun pluralize(word: String, count: Int): String {
        return if (count == 1) word else "${word}s"
    }

    fun updateFormattedDateRange() {
        val startDateMillis = _startDate.value
        val endDateMillis = _endDate.value

        if (startDateMillis != null && endDateMillis != null) {
            val startDate = Instant.ofEpochMilli(startDateMillis)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()

            val endDate = Instant.ofEpochMilli(endDateMillis)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()

            _formattedDateRange.value = formatDateRange(startDate, endDate)
        } else {
            _formattedDateRange.value = null
        }

    }

    private fun formatDateRange(startDate: LocalDate, endDate: LocalDate): String {
        val startDay = startDate.dayOfMonth
        val endDay = endDate.dayOfMonth
        val startMonth = startDate.month.getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
        val endMonth = endDate.month.getDisplayName(TextStyle.SHORT, Locale.ENGLISH)

        return if (startDate.month == endDate.month) {
            "$startDay-$endDay $startMonth"
        } else {
            "$startDay $startMonth-$endDay $endMonth"
        }
    }

    // Function to fetch and set the Staycation by its ID
    fun getStaycationById(staycationId: String) {
        viewModelScope.launch {
            val staycation = repository.getStaycationById(staycationId)
            _staycation.value = staycation
        }
    }
}