package com.example.tripnila.model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tripnila.data.Staycation
import com.example.tripnila.data.StaycationBooking
import com.example.tripnila.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.TextStyle
import java.util.Locale
import java.util.TimeZone


class DetailViewModel(private val repository: UserRepository = UserRepository()) : ViewModel() {

    private val _staycation = MutableStateFlow<Staycation?>(null)
    val staycation = _staycation.asStateFlow()

    private val _staycationBooking = MutableStateFlow<StaycationBooking?>(null)
    val staycationBooking = _staycationBooking.asStateFlow()

    private val _startDate = MutableStateFlow<Long?>(null)
    val startDate: StateFlow<Long?> = _startDate

    private val _endDate = MutableStateFlow<Long?>(null)
    val endDate: StateFlow<Long?> = _endDate

    private val _nightsDifference = MutableStateFlow<Long?>(null)
    val nightsDifference: StateFlow<Long?> = _nightsDifference

    private val _initialNightsDifference = MutableStateFlow<Long?>(null)
    val initialNightsDifference: StateFlow<Long?> = _initialNightsDifference

    private val _totalBookingAmount = MutableStateFlow<Double?>(0.0)
    val totalBookingAmount: StateFlow<Double?> = _totalBookingAmount

    private val _additionalFee = MutableStateFlow<Double?>(0.0)
    val additionalFee: StateFlow<Double?> = _additionalFee

    private val _loadingState = MutableStateFlow(false)
    val loadingState: StateFlow<Boolean> get() = _loadingState

    private val _bookingResult = MutableStateFlow<String?>(null)
    val bookingResult: StateFlow<String?> = _bookingResult

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

    private val _maxGuest = MutableStateFlow<Int?>(0)
    val maxGuest: StateFlow<Int?> = _maxGuest

    private val _initialGuest = MutableStateFlow<Int?>(0)
    val initialGuest: StateFlow<Int?> = _initialGuest

    private val _allowedGuest = MutableStateFlow<Int?>(0)
    val allowedGuest: StateFlow<Int?> = _allowedGuest

    private val _extraGuest = MutableStateFlow<Int?>(0)
    val extraGuest: StateFlow<Int?> = _extraGuest

    private val _clearTrigger = MutableStateFlow(false)
    val clearTrigger: StateFlow<Boolean> = _clearTrigger

    private val _isAddFee = MutableStateFlow(false)
    val isAddFee: StateFlow<Boolean> = _isAddFee

    private val _selectedPaymentMethod = MutableStateFlow<Int?>(-1)
    val selectedPaymentMethod: StateFlow<Int?> get() = _selectedPaymentMethod//.asStateFlow()

    private val _initialFormattedDateRange = MutableStateFlow<String?>(null)
    val initialFormattedDateRange: StateFlow<String?> = _initialFormattedDateRange

    private val _initialStartDate = MutableStateFlow<Long?>(null)
    val initialStartDate: StateFlow<Long?> = _initialStartDate

    private val _initialEndDate = MutableStateFlow<Long?>(null)
    val initialEndDate: StateFlow<Long?> = _initialEndDate

    private val _initialGuestCount = MutableStateFlow<Int?>(null)
    val initialGuestCount: StateFlow<Int?> = _initialGuestCount

    private val _initialInfantCount = MutableStateFlow<Int?>(null)
    val initialInfantCount: StateFlow<Int?> = _initialInfantCount

    private val _initialPetCount = MutableStateFlow<Int?>(null)
    val initialPetCount: StateFlow<Int?> = _initialPetCount

    fun setMaxGuest(amount: Int){
        _maxGuest.value = amount
    }


    fun setInitialAllowedGuest(initial: Int, allowed: Int){
        _initialGuest.value = initial
        _allowedGuest.value = allowed
        _extraGuest.value = initial - allowed
        Log.d("ViewModel", "${_initialGuest.value}")
        Log.d("ViewModel", "${_allowedGuest.value}")
    }

    fun setSelectedPaymentMethod(index: Int?) {
        _selectedPaymentMethod.value = index
        Log.d("ViewModel", "${_selectedPaymentMethod.value}")
    }

    private val _alertDialogMessage = MutableStateFlow<String?>(null)
    val alertDialogMessage: StateFlow<String?> get() = _alertDialogMessage

    private val _isEnoughBalance = MutableStateFlow(false)
    val isEnoughBalance = _isEnoughBalance.asStateFlow()


    private fun setTotalBookingAmount(totalBookingAmount: Double?) {
        _totalBookingAmount.value = totalBookingAmount
    }
    fun setAlertDialogMessage() {
        _alertDialogMessage.value = when {
            !isNightsDifferenceValid() -> "Please select booking dates."
            !isGuestsValid() -> "Please select booking guests."
            !isEnoughBalanceValid() -> "You have insufficient balance"
            else -> "Are you sure you want to proceed?" // No issues, return null for no alert dialog
        }
        Log.d("EnoughBalanceChecker", "${_isEnoughBalance.value}")
    }
    fun isNightsDifferenceValid(): Boolean {
        return _nightsDifference.value != null
    }
    fun setEnoughBalance(enough: Boolean){
        _isEnoughBalance.value = enough
        Log.d("EnoughBalance", "${_isEnoughBalance.value}")
    }
    fun isEnoughBalanceValid(): Boolean {
        return _isEnoughBalance.value
    }

    fun isGuestsValid(): Boolean {
        return _guestCount.value != 0
    }

    fun isPaymentMethodSelected(): Boolean {
        return _selectedPaymentMethod.value!! >= 0
    }

    fun setClearTrigger(value: Boolean) {
        _clearTrigger.value = value
    }

    private fun setGuestCount() {
        _guestCount.value = _childrenCount.value?.let { childrenCount -> _adultCount.value?.plus(childrenCount) }
        if(_guestCount.value!! > _allowedGuest.value!!){
            _isAddFee.value = true
        } else{
            _isAddFee.value = false
        }
        Log.d("Set Start Date", _guestCount.value.toString())
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

        Log.d("Set Start Date", milliseconds.toString())

        updateFormattedDateRange()
    }

    fun setEndDate(milliseconds: Long?) {
        _endDate.value = milliseconds

        Log.d("Set End Date", milliseconds.toString())

        updateFormattedDateRange()
    }

    fun setNightsDifference(nights: Long?) {
        _nightsDifference.value = nights
    }

    fun pluralize(word: String, count: Int): String {
        return if (count == 1) word else "${word}s"
    }

    private fun updateFormattedDateRange() {
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

    fun calculateTotalAmount(): Double? {
        val duration = _nightsDifference.value?.toInt()
        val bookingFee = _staycation.value?.staycationPrice

        val maintenanceFee = bookingFee?.times(0.02)
        val tripnilaFee = bookingFee?.times(0.05)

        val totalAmount = bookingFee?.let { bookingFee ->
            duration?.let { duration ->
                maintenanceFee?.let { maintenanceFee ->
                    tripnilaFee?.let { tripnilaFee ->
                        (bookingFee * duration) + maintenanceFee + tripnilaFee
                    }
                }
            }
        }

        return totalAmount
    }

    fun calculateCommission(): Double? {
        val bookingFee = _staycation.value?.staycationPrice

        return bookingFee?.times(0.05)
    }

    fun getStaycationById(staycationId: String) {
        viewModelScope.launch {
            val staycation = repository.getStaycationById(staycationId)
            _staycation.value = staycation
        }
    }

    fun getStaycationBookingByBookingId(bookingId: String) {
        viewModelScope.launch {

            val staycationBooking = repository.getStaycationBookingByBookingId(bookingId)
            _staycationBooking.value = staycationBooking

            _staycation.value = _staycationBooking.value?.staycation

            _nightsDifference.value = _staycationBooking.value?.getDaysDifference()
            _initialNightsDifference.value = _staycationBooking.value?.getDaysDifference()

            _startDate.value = _staycationBooking.value?.checkInDate?.time
            _endDate.value = _staycationBooking.value?.checkOutDate?.time

            _initialStartDate.value = _staycationBooking.value?.checkInDate?.time
            _initialEndDate.value = _staycationBooking.value?.checkOutDate?.time

            if (_startDate.value != null && _endDate.value != null) {
                val startDate = Instant.ofEpochMilli(_startDate.value!!)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()

                val endDate = Instant.ofEpochMilli(_endDate.value!!)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()

                _initialFormattedDateRange.value = formatDateRange(startDate, endDate)
            } else {
                _initialFormattedDateRange.value = null
            }

           // _adultCount.value = _staycationBooking.value?.noOfGuests
            _initialGuestCount.value = _staycationBooking.value?.noOfGuests
            _guestCount.value = _staycationBooking.value?.noOfGuests

            _infantCount.value = _staycationBooking.value?.noOfInfants
            _initialInfantCount.value = _staycationBooking.value?.noOfInfants

            _petCount.value = _staycationBooking.value?.noOfPets
            _initialPetCount.value = _staycationBooking.value?.noOfPets

        }
    }

    suspend fun rescheduleBooking(addFee: Double) {
        _loadingState.value = true
        try {
            viewModelScope.launch {


                val maintenanceFee = _staycation.value?.staycationPrice?.times(0.10)

                val initialProductBookingFee = (_staycation.value?.staycationPrice)?.times(_initialNightsDifference.value!!)
                val initialTripNilaFee = initialProductBookingFee?.times(0.05)
                val initialTotalFeeState = (initialProductBookingFee?.plus(maintenanceFee!!))?.plus(initialTripNilaFee!!)

                val productBookingFee = (_staycation.value?.staycationPrice)?.times(_nightsDifference.value!!)
                val tripNilaFee = productBookingFee?.times(0.05)
                val totalFeeState = (productBookingFee?.plus(maintenanceFee!!))?.plus(tripNilaFee!!)?.plus(addFee)

                val totalFeeDifference = totalFeeState?.let { initialTotalFeeState?.minus(it) }

                val amountRefunded = if (totalFeeDifference!! < 0) {
                    0
                } else {
                    totalFeeDifference
                }

                Log.d("Booking Update", "bookingId: ${_staycationBooking.value?.staycationBookingId ?: ""}")
                Log.d("Booking Update", "staycationId: ${_staycationBooking.value?.staycation?.staycationId ?: ""}")
                Log.d("Booking Update", "initialCheckInDateMillis: ${_initialStartDate.value ?: 0}")
                Log.d("Booking Update", "initialCheckOutDateMillis: ${_initialEndDate.value ?: 0}")
                Log.d("Booking Update", "newCheckInDateMillis: ${_startDate.value ?: 0}")
                Log.d("Booking Update", "newCheckOutDateMillis: ${_endDate.value ?: 0}")
                Log.d("Booking Update", "commission: $tripNilaFee")
                Log.d("Booking Update", "amountRefunded: $amountRefunded")
                Log.d("Booking Update", "newTotalAmount: $totalFeeState")
                Log.d("Booking Update", "newNoOfGuests: ${_guestCount.value ?: 0}")
                Log.d("Booking Update", "newNoOfPets: ${_petCount.value ?: 0}")
                Log.d("Booking Update", "newNoOfInfants: ${_infantCount.value ?: 0}")
//
                val isSuccess = repository.updateStaycationBooking(
                    bookingId = _staycationBooking.value?.staycationBookingId ?: "",
                    staycationId = _staycationBooking.value?.staycation?.staycationId!!,
                    initialCheckInDateMillis = _initialStartDate.value ?: 0,
                    initialCheckOutDateMillis = _initialEndDate.value ?: 0,
                    newCheckInDateMillis = _startDate.value ?: 0,
                    newCheckOutDateMillis = _endDate.value ?: 0,
                    additionalFee = addFee,
                    commission = tripNilaFee ?: 0.0,
                    amountRefunded = amountRefunded.toDouble(),
                    newTotalAmount = totalFeeState ,
                    newNoOfGuests = _guestCount.value ?: 0,
                    newNoOfPets = _petCount.value ?: 0,
                    newNoOfInfants = _infantCount.value ?: 0
                )


                _bookingResult.value = if (isSuccess) {
                    "Rescheduling successful!"
                } else {
                    "Rescheduling failed: An error occurred" // Customize error message
                }

                Log.d("Result", "$_bookingResult")


            }

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            _loadingState.value = false // Set loading state to false, whether successful or not
        }
    }


    suspend fun addBooking(touristId: String) {
        _loadingState.value = true
        try {
            viewModelScope.launch {


                val bookingFee = _staycation.value?.staycationPrice ?: 0.0

                val productBookingFee = bookingFee * _nightsDifference.value!!
                val tripNilaFee = productBookingFee.times(0.05)
                val maintenanceFee = _staycation.value?.staycationPrice?.times(0.10)

                val totalFee = productBookingFee + maintenanceFee!! + tripNilaFee

                Log.d("Total Fee", totalFee.toString())
                Log.d("Maintenance Fee", maintenanceFee.toString())
                Log.d("TripNila Fee", tripNilaFee.toString())
                Log.d("Guest x Booking Fee", productBookingFee.toString())
                Log.d("Booking Fee", bookingFee.toString())

                val bookingStatus = "Pending" // Set the status accordingly
                val checkInDateMillis = _startDate.value ?: return@launch
                val checkOutDateMillis = _endDate.value ?: return@launch
                val timeZone = TimeZone.getTimeZone("Asia/Manila")
                val noOfGuests = _guestCount.value ?: return@launch
                val noOfInfants = _infantCount.value ?: return@launch
                val noOfPets = _petCount.value ?: return@launch
                val staycationId = _staycation.value?.staycationId ?: return@launch
//                val totalAmount = totalFee // Implement your own logic
//                val commission = maintenanceFee // Implement your own logic
                val paymentStatus = "Pending" // Set the initial payment status
                val paymentMethod = when (_selectedPaymentMethod?.value) {
                    0 -> {
                        "Paypal"
                    }
                    1 -> {
                        "Gcash"
                    }
                    2 -> {
                        "Maya"
                    }
                    else -> {
                        "Others"
                    }
                }

                val isSuccess = repository.addStaycationBooking(
                    bookingStatus = bookingStatus,
                    checkInDateMillis = checkInDateMillis,
                    checkOutDateMillis = checkOutDateMillis,
                    timeZone = timeZone,
                    noOfGuests = noOfGuests,
                    noOfPets = noOfPets,
                    noOfInfants = noOfInfants,
                    staycationId = staycationId,
                    totalAmount = totalFee,
                    touristId = touristId,
                    commission = tripNilaFee,
                    paymentStatus = paymentStatus,
                    paymentMethod = paymentMethod
                )

                _bookingResult.value = if (isSuccess) {
                    "Booking successful!"
                } else {
                    "Booking failed: An error occurred" // Customize error message
                }

                Log.d("Result", "$_bookingResult")
            }
        } catch (e: Exception) {
            // Handle exceptions as needed
        } finally {
            _loadingState.value = false // Set loading state to false, whether successful or not
        }
    }
}
