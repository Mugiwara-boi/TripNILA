package com.example.tripnila.model
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tripnila.data.MonthTotal
import com.example.tripnila.data.Staycation
import com.example.tripnila.data.StaycationBooking
import com.example.tripnila.data.Tour
import com.example.tripnila.repository.UserRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Calendar


class InsightViewModel(private val repository: UserRepository = UserRepository()) : ViewModel() {

    private val _selectedYear = mutableStateOf(2024)
    val selectedYear: State<Int> = _selectedYear

    private val _aggregatedSalesData = MutableStateFlow<List<MonthTotal>>(emptyList())
    val aggregatedSalesData = _aggregatedSalesData.asStateFlow()

    private val _staycationBookings = MutableStateFlow<List<StaycationBooking>>(emptyList()) // Initialize with an empty Host
    val staycationBookings = _staycationBookings.asStateFlow()

    private val _completedStaycationBookings = MutableStateFlow<List<StaycationBooking>>(emptyList()) // Initialize with an empty Host
    val completedStaycationBookings = _completedStaycationBookings.asStateFlow()

    private val _completedMonthlyStaycationBookings = MutableStateFlow<List<StaycationBooking>>(emptyList()) // Initialize with an empty Host
    val completedMonthlyStaycationBookings = _completedMonthlyStaycationBookings.asStateFlow()

    private val _tours = MutableStateFlow<List<Tour>>(emptyList()) // Initialize with an empty Host
    val tours = _tours.asStateFlow()

    private val _staycations = MutableStateFlow<List<Staycation>>(emptyList())
    val staycations = _staycations.asStateFlow()

    private val _selectedStaycation = MutableStateFlow(Staycation()) // Initialize with an empty Host
    val selectedStaycation = _selectedStaycation.asStateFlow()

    private val documentIds = mutableListOf<String>()

    private val _revenue = MutableStateFlow(0)
    val revenue = _revenue.asStateFlow()

    private val _monthlyViews = MutableStateFlow(0)
    val monthlyViews= _monthlyViews.asStateFlow()

    private val _yearlyViews = MutableStateFlow(0)
    val yearlyViews= _yearlyViews.asStateFlow()

    private val _monthlyRevenue = MutableStateFlow(0)
    val monthlyRevenue = _monthlyRevenue.asStateFlow()


    fun setSelectedYear(year: Int,staycationId: String) {
        viewModelScope.launch {
            _selectedYear.value = year
            getSales(year, staycationId)
        }
    }
    fun setSelectedStaycation(staycationId: String) {
        val currentStaycations = _staycations.value.toMutableList()
        val selectedStaycationInList = currentStaycations.find { it.staycationId == staycationId}
        _selectedStaycation.value = selectedStaycationInList?: Staycation()
        Log.d("ViewModel" , "${_selectedStaycation.value}")
    }
    fun getHostedStaycation(hostId : String){
        val db = FirebaseFirestore.getInstance()
        val collectionName = "staycation"
        db.collection(collectionName)
            .whereEqualTo("hostId", hostId)
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot.documents) {
                    val documentId = document.id
                    documentIds.add(documentId)
                }

            }
            .addOnFailureListener { exception ->
                // Handle any errors that occur during the query
                exception.printStackTrace()
            }
    }
    fun getDocumentIds(): List<String> {
        return documentIds.toList() // Return a copy of the list to prevent modification
    }

    fun getStaycation(hostId: String){
        viewModelScope.launch{
            val staycation = repository.getHostedStaycation(hostId)
            _staycations.value = staycation
        }
    }

    /*fun getStaycationBookings(staycationId: String) {
        viewModelScope.launch {
            val staycationBookings = repository.getStaycationBookingForStaycation(staycationId)
            _staycationBookings.value = staycationBookings

            Log.d("StaycationBookings", "${_staycationBookings.value}")
        }
    }*/
    fun getCompletedStaycationBookings(staycationId: String) {
        viewModelScope.launch {
            val staycationBookings = repository.getCompletedStaycationBookingForStaycation(staycationId)
            _completedStaycationBookings.value = staycationBookings
            Log.d("completedStaycationBookings", "${_completedStaycationBookings.value}")

        }
    }

    fun getCompletedStaycationBookingsForMonth(staycationId: String) {
        viewModelScope.launch {
            val currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1

            // Fetch all completed staycation bookings for the staycationId
            val allCompletedStaycationBookings = repository.getCompletedStaycationBookingForStaycation(staycationId)

            // Filter staycation bookings for the current month
            val staycationBookingsForCurrentMonth = allCompletedStaycationBookings.filter { booking ->
                val bookingCalendar = Calendar.getInstance().apply {
                    time = booking.bookingDate // Assuming bookingDate is a Date type
                }
                bookingCalendar.get(Calendar.MONTH) + 1 == currentMonth
            }

            // Update the StateFlow with the filtered staycation bookings
            _completedMonthlyStaycationBookings.value = staycationBookingsForCurrentMonth

        }
    }
    fun fetchMonthlyViews(serviceId: String, serviceType: String) {
        viewModelScope.launch {
            try {
                // Call the repository to get the current month's view count
                val viewCount = repository.getCurrentMonthViewCount(serviceId, serviceType)
                // Set the value of _monthlyViews
                _monthlyViews.value = viewCount
            } catch (e: Exception) {
                // Handle any errors
                e.printStackTrace()
                // Optionally, set a default value or handle the error condition
            }
        }
    }

    fun fetchAllViews(serviceId: String) {
        viewModelScope.launch {
            try {
                // Call the repository to get the current month's view count
                val viewCount = repository.getAllViewCountForServiceId(serviceId)
                // Set the value of _monthlyViews
                _yearlyViews.value = viewCount
            } catch (e: Exception) {
                // Handle any errors
                e.printStackTrace()
                // Optionally, set a default value or handle the error condition
            }
        }
    }
    fun getMonthlyRevenue(staycationBooking: List<StaycationBooking>){
        viewModelScope.launch {
            val currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1

            // Filter the bookings for the current month
            val currentMonthBookings = staycationBooking.filter { booking ->
                val bookingCalendar = Calendar.getInstance().apply {
                    time = booking.bookingDate // Assuming bookingDate is a Date type
                }
                bookingCalendar.get(Calendar.MONTH) + 1 == currentMonth
            }
            val totalRevenue = currentMonthBookings.sumOf { booking ->
                booking.totalAmount
            }
            _monthlyRevenue.value = totalRevenue.toInt()
            Log.d("totalRevenue", "$totalRevenue")


        }
    }



    fun getYearlyRevenue(staycationBooking: List<StaycationBooking>){
        viewModelScope.launch {
            val currentYear = Calendar.getInstance().get(Calendar.YEAR)

            // Filter the bookings for the current year
            val currentYearBookings = staycationBooking.filter { booking ->
                val bookingCalendar = Calendar.getInstance().apply {
                    time = booking.bookingDate // Assuming bookingDate is a Date type
                }
                bookingCalendar.get(Calendar.YEAR) == currentYear
            }

            // Calculate the total revenue for the year
            val totalRevenue = currentYearBookings.sumOf { booking ->
                booking.totalAmount
            }

            // Update the revenue StateFlow with the total revenue for the year
            _revenue.value = totalRevenue.toInt()

            Log.d("totalRevenue", "$totalRevenue")
        }
    }
    fun aggregateSalesByMonth(staycationBooking: List<StaycationBooking>, year: Int): List<MonthTotal> {
        val salesByMonth = mutableMapOf<Int, Double>()

        // Initialize salesByMonth with zero values for all months of the year
        for (month in 1..12) {
            salesByMonth[month] = 0.0
        }

        // Iterate through the sales data to aggregate by month
        for (document in staycationBooking) {
            val date = document.bookingDate
            val amount = document.totalAmount

            // Extract the month and year from the date
            val calendar = Calendar.getInstance()
            calendar.time = date

            val month = calendar.get(Calendar.MONTH) + 1 // Adjust for 0-based index
            val docYear = calendar.get(Calendar.YEAR)

            if (docYear == year) {
                // Aggregate the amount for the corresponding month
                val currentTotal = salesByMonth[month] ?: 0.0
                salesByMonth[month] = currentTotal + (amount ?: 0.0)
            }
        }

        // Convert the map to a list of MonthTotal objects
        val monthTotalList = mutableListOf<MonthTotal>()
        for ((month, totalAmount) in salesByMonth) {
            monthTotalList.add(MonthTotal(month, totalAmount))
        }

        return monthTotalList
    }

    suspend fun getSales(year : Int, staycationId: String) {
        val salesCollection = repository.getCompletedStaycationBookingForStaycation(staycationId)

            val aggregatedData = aggregateSalesByMonth(salesCollection, year)
            _aggregatedSalesData.value = aggregatedData
            // Now aggregatedData contains a list of MonthTotal objects with the month and total amount
        }
}

