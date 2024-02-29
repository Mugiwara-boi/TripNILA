package com.example.tripnila.model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.tripnila.data.HomePagingItem
import com.example.tripnila.data.Preference
import com.example.tripnila.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.SortedSet

class HomeViewModel(private val repository: UserRepository = UserRepository()) : ViewModel() {


    private val _selectedTab = MutableStateFlow("For You") // Default tab
    val selectedTab: StateFlow<String> get() = _selectedTab

    private val _serviceIdSet = MutableStateFlow<SortedSet<String>>(sortedSetOf())
    val serviceIdSet = _serviceIdSet.asStateFlow()

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _includeStaycation = MutableStateFlow(true)
    val includeStaycation = _includeStaycation.asStateFlow()

    private val _includeTour = MutableStateFlow(true)
    val includeTour = _includeTour.asStateFlow()

    private val _houseSelected = MutableStateFlow(true)
    val houseSelected = _houseSelected.asStateFlow()

    private val _apartmentSelected = MutableStateFlow(true)
    val apartmentSelected = _apartmentSelected.asStateFlow()

    private val _condoSelected = MutableStateFlow(true)
    val condoSelected = _condoSelected.asStateFlow()

    private val _campSelected = MutableStateFlow(true)
    val campSelected = _campSelected.asStateFlow()

    private val _guestHouseSelected = MutableStateFlow(true)
    val guestHouseSelected = _guestHouseSelected.asStateFlow()

    private val _hotelSelected = MutableStateFlow(true)
    val hotelSelected = _hotelSelected.asStateFlow()

    private val _photoTourSelected = MutableStateFlow(true)
    val photoTourSelected = _photoTourSelected.asStateFlow()

    private val _foodTripSelected = MutableStateFlow(true)
    val foodTripSelected = _foodTripSelected.asStateFlow()

    private val _barHoppingSelected = MutableStateFlow(true)
    val barHoppingSelected = _barHoppingSelected.asStateFlow()

    private val _selectedRating = MutableStateFlow(0)
    val selectedRating = _selectedRating.asStateFlow()

    private val _minPrice = MutableStateFlow("")
    val minPrice = _minPrice.asStateFlow()

    private val _maxPrice = MutableStateFlow("")
    val maxPrice = _maxPrice.asStateFlow()

    private val _city = MutableStateFlow("")
    val city = _city.asStateFlow()

    private val _capacity = MutableStateFlow("")
    val capacity = _capacity.asStateFlow()

    private val _bedroomCount = MutableStateFlow("Any")
    val bedroomCount = _bedroomCount.asStateFlow()

    private val _bedCount = MutableStateFlow("Any")
    val bedCount = _bedCount.asStateFlow()

    private val _bathroomCount = MutableStateFlow("Any")
    val bathroomCount = _bathroomCount.asStateFlow()

    private val _checkedAmenities = MutableStateFlow(
        listOf(true, true, true, true, true, true, true, true, true)
    )
    val checkedAmenities = _checkedAmenities.asStateFlow()

    private val _checkedOffers = MutableStateFlow(
        listOf(true, true, true, true)
    )
    val checkedOffers = _checkedOffers.asStateFlow()

    private val _startDate = MutableStateFlow<Long?>(null)
    val startDate = _startDate.asStateFlow()

    private val _endDate = MutableStateFlow<Long?>(null)
    val endDate = _endDate.asStateFlow()

    private val _preferences =
        MutableStateFlow<List<Preference>>(emptyList()) // Initialize with an empty Host
    val preferences = _preferences.asStateFlow()

    private val _touristId = MutableStateFlow("") // Default tab
    val touristId: StateFlow<String> get() = _touristId


    fun updateSearchText(newSearchText: String) {
        _searchText.value = newSearchText

        Log.d("New Text", _searchText.value)
    }

    fun updateIncludeStaycation(newValue: Boolean) {
        _includeStaycation.value = newValue

        _capacity.value = ""

        if (!_includeTour.value) {
            _minPrice.value = ""
            _maxPrice.value = ""
            _city.value = ""
        }
    }

    fun updateIncludeTour(newValue: Boolean) {
        _includeTour.value = newValue
    }

    fun updateHouseSelected(newValue: Boolean) {
        _houseSelected.value = newValue
    }

    fun updateApartmentSelected(newValue: Boolean) {
        _apartmentSelected.value = newValue
    }

    fun updateCondoSelected(newValue: Boolean) {
        _condoSelected.value = newValue
    }

    fun updateCampSelected(newValue: Boolean) {
        _campSelected.value = newValue
    }

    fun updateGuestHouseSelected(newValue: Boolean) {
        _guestHouseSelected.value = newValue
    }

    fun updateHotelSelected(newValue: Boolean) {
        _hotelSelected.value = newValue
    }

    fun updatePhotoTourSelected(newValue: Boolean) {
        _photoTourSelected.value = newValue
    }

    fun updateFoodTripSelected(newValue: Boolean) {
        _foodTripSelected.value = newValue
    }

    fun updateBarHoppingSelected(newValue: Boolean) {
        _barHoppingSelected.value = newValue
    }

    fun updateSelectedRating(newValue: Int) {
        _selectedRating.value = newValue
    }

    fun updateMinPrice(newMinPrice: String) {
        _minPrice.value = newMinPrice
    }

    fun updateMaxPrice(newMaxPrice: String) {
        _maxPrice.value = newMaxPrice
    }

    fun updateCity(newCity: String) {
        _city.value = newCity
    }

    fun updateCapacity(newCapacity: String) {
        _capacity.value = newCapacity
    }

    fun updateBedroomCount(newBedroomCount: String) {
        _bedroomCount.value = newBedroomCount
    }

    fun updateBedCount(newBedCount: String) {
        _bedCount.value = newBedCount
    }

    fun updateBathroomCount(newBathroomCount: String) {
        _bathroomCount.value = newBathroomCount
    }

    fun updateCheckedAmenities(newCheckedAmenities: List<Boolean>) {
        _checkedAmenities.value = newCheckedAmenities
    }

    fun updateCheckedOffers(newCheckedOffers: List<Boolean>) {
        _checkedOffers.value = newCheckedOffers
    }

    fun updateStartDate(newStartDate: Long?) {
        _startDate.value = newStartDate

        Log.d("Start Date(VM)", _startDate.value.toString())
    }

    fun updateEndDate(newEndDate: Long?) {
        _endDate.value = newEndDate

        Log.d("End Date(VM)", _endDate.value.toString())
    }


    fun selectTab(tab: String) {
        _selectedTab.value = tab

        Log.d("Selected Tab (VM)", _selectedTab.value)
    }


    fun getUserPreference(touristId: String) {
        viewModelScope.launch {
            try {
                val preferences = repository.getTouristPreferences(touristId)
                _preferences.value = preferences

                _touristId.value = touristId
                Log.d("TouristId", _touristId.value)
//                // temp
//                val preferencesList = listOf(
//                    Preference("Sports"),
//                    Preference("Food Trip"),
//                    Preference("Shop"),
//                    Preference("Nature"),
//                    Preference("Gaming"),
//                    Preference("Karaoke"),
//                    Preference("History"),
//                    Preference("Clubs"),
//                    Preference("Sightseeing"),
//                    Preference("Swimming")
//                )
//                _preferences.value = preferencesList

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getUniqueServiceIds() {
        viewModelScope.launch {
            try {
                val uniqueServiceIds = repository.getAllUniqueServiceId().toSortedSet()
                _serviceIdSet.value = uniqueServiceIds
                Log.d("Service IDs", _serviceIdSet.value.toString())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private var forYouPager: Pager<Int, HomePagingItem>? = null
    private var sportsPager: Pager<Int, HomePagingItem>? = null
    private var foodTripPager: Pager<Int, HomePagingItem>? = null
    private var shopPager: Pager<Int, HomePagingItem>? = null
    private var naturePager: Pager<Int, HomePagingItem>? = null
    private var gamingPager: Pager<Int, HomePagingItem>? = null
    private var karaokePager: Pager<Int, HomePagingItem>? = null
    private var historyPager: Pager<Int, HomePagingItem>? = null
    private var clubsPager: Pager<Int, HomePagingItem>? = null
    private var sightseeingPager: Pager<Int, HomePagingItem>? = null
    private var swimmingPager: Pager<Int, HomePagingItem>? = null

    fun refreshForYouPagingData() {
        forYouPager = createForYouPager()
        forYouPagingData = forYouPager!!.flow.cachedIn(viewModelScope)
    }
    fun refreshSportsPagingData() {
        sportsPager = createSportsPager()
        sportsPagingData = sportsPager!!.flow.cachedIn(viewModelScope)
    }

    fun refreshFoodTripPagingData() {
        foodTripPager = createFoodTripPager()
        foodTripPagingData = foodTripPager!!.flow.cachedIn(viewModelScope)
    }
    fun refreshShopPagingData() {
        shopPager = createShopPager()
        shopPagingData = shopPager!!.flow.cachedIn(viewModelScope)
    }

    fun refreshNaturePagingData() {
        naturePager = createNaturePager()
        naturePagingData = naturePager!!.flow.cachedIn(viewModelScope)
    }

    fun refreshGamingPagingData() {
        gamingPager = createGamingPager()
        gamingPagingData = gamingPager!!.flow.cachedIn(viewModelScope)
    }

    fun refreshKaraokePagingData() {
        karaokePager = createKaraokePager()
        karaokePagingData = karaokePager!!.flow.cachedIn(viewModelScope)
    }

    fun refreshHistoryPagingData() {
        historyPager = createHistoryPager()
        historyPagingData = historyPager!!.flow.cachedIn(viewModelScope)
    }

    fun refreshClubsPagingData() {
        clubsPager = createClubsPager()
        clubsPagingData = clubsPager!!.flow.cachedIn(viewModelScope)
    }

    fun refreshSightseeingPagingData() {
        sightseeingPager = createSightseeingPager()
        sightseeingPagingData = sightseeingPager!!.flow.cachedIn(viewModelScope)
    }

    fun refreshSwimmingPagingData() {
        swimmingPager = createSwimmingPager()
        swimmingPagingData = swimmingPager!!.flow.cachedIn(viewModelScope)
    }

    private val pageSize = 5
    private val initialLoadSize = 10

    private fun createForYouPager(): Pager<Int, HomePagingItem> {
        return Pager(PagingConfig(pageSize = pageSize, initialLoadSize = initialLoadSize)) {
            ForYouPagingSource(
                hostId = "HOST-${_touristId.value}",
                repository = repository,
                tags = _preferences.value.map { it.preference },
                serviceIdSet = _serviceIdSet.value,
                searchText = _searchText.value,
                includeStaycation = _includeStaycation.value,
                includeTour = _includeTour.value,
                houseSelected = _houseSelected.value,
                apartmentSelected = _apartmentSelected.value,
                condoSelected = _condoSelected.value,
                campSelected = _campSelected.value,
                guestHouseSelected = _guestHouseSelected.value,
                hotelSelected = _hotelSelected.value,
                photoTourSelected = _photoTourSelected.value,
                foodTripSelected = _foodTripSelected.value,
                barHoppingSelected = _barHoppingSelected.value,
                selectedRating = _selectedRating.value,
                minPrice = _minPrice.value,
                maxPrice = _maxPrice.value,
                city = _city.value,
                capacity = _capacity.value,
                bedroomCount = _bedroomCount.value,
                bedCount = _bedCount.value,
                bathroomCount = _bathroomCount.value,
                checkedAmenities = _checkedAmenities.value,
                checkedOffers = _checkedOffers.value,
                startDate = _startDate.value,
                endDate = _endDate.value,
                initialLoadSize = initialLoadSize
            )
        }
    }

    private fun createSportsPager(): Pager<Int, HomePagingItem> {
        return Pager(PagingConfig(pageSize = pageSize, initialLoadSize = initialLoadSize)) {
            PreferencePagingSource(
                hostId = "HOST-${_touristId.value}",
                repository = repository,
                tag = "Sports",
                searchText = _searchText.value,
                includeStaycation = _includeStaycation.value,
                includeTour = _includeTour.value,
                houseSelected = _houseSelected.value,
                apartmentSelected = _apartmentSelected.value,
                condoSelected = _condoSelected.value,
                campSelected = _campSelected.value,
                guestHouseSelected = _guestHouseSelected.value,
                hotelSelected = _hotelSelected.value,
                photoTourSelected = _photoTourSelected.value,
                foodTripSelected = _foodTripSelected.value,
                barHoppingSelected = _barHoppingSelected.value,
                selectedRating = _selectedRating.value,
                minPrice = _minPrice.value,
                maxPrice = _maxPrice.value,
                city = _city.value,
                capacity = _capacity.value,
                bedroomCount = _bedroomCount.value,
                bedCount = _bedCount.value,
                bathroomCount = _bathroomCount.value,
                checkedAmenities = _checkedAmenities.value,
                checkedOffers = _checkedOffers.value,
                startDate = _startDate.value,
                endDate = _endDate.value,
                initialLoadSize = initialLoadSize
            )
        }
    }

    private fun createFoodTripPager(): Pager<Int, HomePagingItem> {
        return Pager(PagingConfig(pageSize = pageSize, initialLoadSize = initialLoadSize)) {
            PreferencePagingSource(
                hostId = "HOST-${_touristId.value}",
                repository = repository,
                tag = "Food Trip",
                searchText = _searchText.value,
                includeStaycation = _includeStaycation.value,
                includeTour = _includeTour.value,
                houseSelected = _houseSelected.value,
                apartmentSelected = _apartmentSelected.value,
                condoSelected = _condoSelected.value,
                campSelected = _campSelected.value,
                guestHouseSelected = _guestHouseSelected.value,
                hotelSelected = _hotelSelected.value,
                photoTourSelected = _photoTourSelected.value,
                foodTripSelected = _foodTripSelected.value,
                barHoppingSelected = _barHoppingSelected.value,
                selectedRating = _selectedRating.value,
                minPrice = _minPrice.value,
                maxPrice = _maxPrice.value,
                city = _city.value,
                capacity = _capacity.value,
                bedroomCount = _bedroomCount.value,
                bedCount = _bedCount.value,
                bathroomCount = _bathroomCount.value,
                checkedAmenities = _checkedAmenities.value,
                checkedOffers = _checkedOffers.value,
                startDate = _startDate.value,
                endDate = _endDate.value,
                initialLoadSize = initialLoadSize
            )
        }
    }

    private fun createShopPager(): Pager<Int, HomePagingItem> {
        return Pager(PagingConfig(pageSize = pageSize, initialLoadSize = initialLoadSize)) {
            PreferencePagingSource(
                hostId = "HOST-${_touristId.value}",
                repository = repository,
                tag = "Shop",
                searchText = _searchText.value,
                includeStaycation = _includeStaycation.value,
                includeTour = _includeTour.value,
                houseSelected = _houseSelected.value,
                apartmentSelected = _apartmentSelected.value,
                condoSelected = _condoSelected.value,
                campSelected = _campSelected.value,
                guestHouseSelected = _guestHouseSelected.value,
                hotelSelected = _hotelSelected.value,
                photoTourSelected = _photoTourSelected.value,
                foodTripSelected = _foodTripSelected.value,
                barHoppingSelected = _barHoppingSelected.value,
                selectedRating = _selectedRating.value,
                minPrice = _minPrice.value,
                maxPrice = _maxPrice.value,
                city = _city.value,
                capacity = _capacity.value,
                bedroomCount = _bedroomCount.value,
                bedCount = _bedCount.value,
                bathroomCount = _bathroomCount.value,
                checkedAmenities = _checkedAmenities.value,
                checkedOffers = _checkedOffers.value,
                startDate = _startDate.value,
                endDate = _endDate.value,
                initialLoadSize = initialLoadSize
            )
        }
    }

    private fun createNaturePager(): Pager<Int, HomePagingItem> {
        return Pager(PagingConfig(pageSize = pageSize, initialLoadSize = initialLoadSize)) {
            PreferencePagingSource(
                hostId = "HOST-${_touristId.value}",
                repository = repository,
                tag = "Nature",
                searchText = _searchText.value,
                includeStaycation = _includeStaycation.value,
                includeTour = _includeTour.value,
                houseSelected = _houseSelected.value,
                apartmentSelected = _apartmentSelected.value,
                condoSelected = _condoSelected.value,
                campSelected = _campSelected.value,
                guestHouseSelected = _guestHouseSelected.value,
                hotelSelected = _hotelSelected.value,
                photoTourSelected = _photoTourSelected.value,
                foodTripSelected = _foodTripSelected.value,
                barHoppingSelected = _barHoppingSelected.value,
                selectedRating = _selectedRating.value,
                minPrice = _minPrice.value,
                maxPrice = _maxPrice.value,
                city = _city.value,
                capacity = _capacity.value,
                bedroomCount = _bedroomCount.value,
                bedCount = _bedCount.value,
                bathroomCount = _bathroomCount.value,
                checkedAmenities = _checkedAmenities.value,
                checkedOffers = _checkedOffers.value,
                startDate = _startDate.value,
                endDate = _endDate.value,
                initialLoadSize = initialLoadSize
            )
        }
    }

    private fun createGamingPager(): Pager<Int, HomePagingItem> {
        return Pager(PagingConfig(pageSize = pageSize, initialLoadSize = initialLoadSize)) {
            PreferencePagingSource(
                hostId = "HOST-${_touristId.value}",
                repository = repository,
                tag = "Gaming",
                searchText = _searchText.value,
                includeStaycation = _includeStaycation.value,
                includeTour = _includeTour.value,
                houseSelected = _houseSelected.value,
                apartmentSelected = _apartmentSelected.value,
                condoSelected = _condoSelected.value,
                campSelected = _campSelected.value,
                guestHouseSelected = _guestHouseSelected.value,
                hotelSelected = _hotelSelected.value,
                photoTourSelected = _photoTourSelected.value,
                foodTripSelected = _foodTripSelected.value,
                barHoppingSelected = _barHoppingSelected.value,
                selectedRating = _selectedRating.value,
                minPrice = _minPrice.value,
                maxPrice = _maxPrice.value,
                city = _city.value,
                capacity = _capacity.value,
                bedroomCount = _bedroomCount.value,
                bedCount = _bedCount.value,
                bathroomCount = _bathroomCount.value,
                checkedAmenities = _checkedAmenities.value,
                checkedOffers = _checkedOffers.value,
                startDate = _startDate.value,
                endDate = _endDate.value,
                initialLoadSize = initialLoadSize
            )
        }
    }

    private fun createKaraokePager(): Pager<Int, HomePagingItem> {
        return Pager(PagingConfig(pageSize = pageSize, initialLoadSize = initialLoadSize)) {
            PreferencePagingSource(
                hostId = "HOST-${_touristId.value}",
                repository = repository,
                tag = "Karaoke",
                searchText = _searchText.value,
                includeStaycation = _includeStaycation.value,
                includeTour = _includeTour.value,
                houseSelected = _houseSelected.value,
                apartmentSelected = _apartmentSelected.value,
                condoSelected = _condoSelected.value,
                campSelected = _campSelected.value,
                guestHouseSelected = _guestHouseSelected.value,
                hotelSelected = _hotelSelected.value,
                photoTourSelected = _photoTourSelected.value,
                foodTripSelected = _foodTripSelected.value,
                barHoppingSelected = _barHoppingSelected.value,
                selectedRating = _selectedRating.value,
                minPrice = _minPrice.value,
                maxPrice = _maxPrice.value,
                city = _city.value,
                capacity = _capacity.value,
                bedroomCount = _bedroomCount.value,
                bedCount = _bedCount.value,
                bathroomCount = _bathroomCount.value,
                checkedAmenities = _checkedAmenities.value,
                checkedOffers = _checkedOffers.value,
                startDate = _startDate.value,
                endDate = _endDate.value,
                initialLoadSize = initialLoadSize
            )
        }
    }

    private fun createHistoryPager(): Pager<Int, HomePagingItem> {
        return Pager(PagingConfig(pageSize = pageSize, initialLoadSize = initialLoadSize)) {
            PreferencePagingSource(
                hostId = "HOST-${_touristId.value}",
                repository = repository,
                tag = "History",
                searchText = _searchText.value,
                includeStaycation = _includeStaycation.value,
                includeTour = _includeTour.value,
                houseSelected = _houseSelected.value,
                apartmentSelected = _apartmentSelected.value,
                condoSelected = _condoSelected.value,
                campSelected = _campSelected.value,
                guestHouseSelected = _guestHouseSelected.value,
                hotelSelected = _hotelSelected.value,
                photoTourSelected = _photoTourSelected.value,
                foodTripSelected = _foodTripSelected.value,
                barHoppingSelected = _barHoppingSelected.value,
                selectedRating = _selectedRating.value,
                minPrice = _minPrice.value,
                maxPrice = _maxPrice.value,
                city = _city.value,
                capacity = _capacity.value,
                bedroomCount = _bedroomCount.value,
                bedCount = _bedCount.value,
                bathroomCount = _bathroomCount.value,
                checkedAmenities = _checkedAmenities.value,
                checkedOffers = _checkedOffers.value,
                startDate = _startDate.value,
                endDate = _endDate.value,
                initialLoadSize = initialLoadSize
            )
        }
    }

    private fun createClubsPager(): Pager<Int, HomePagingItem> {
        return Pager(PagingConfig(pageSize = pageSize, initialLoadSize = initialLoadSize)) {
            PreferencePagingSource(
                hostId = "HOST-${_touristId.value}",
                repository = repository,
                tag = "Clubs",
                searchText = _searchText.value,
                includeStaycation = _includeStaycation.value,
                includeTour = _includeTour.value,
                houseSelected = _houseSelected.value,
                apartmentSelected = _apartmentSelected.value,
                condoSelected = _condoSelected.value,
                campSelected = _campSelected.value,
                guestHouseSelected = _guestHouseSelected.value,
                hotelSelected = _hotelSelected.value,
                photoTourSelected = _photoTourSelected.value,
                foodTripSelected = _foodTripSelected.value,
                barHoppingSelected = _barHoppingSelected.value,
                selectedRating = _selectedRating.value,
                minPrice = _minPrice.value,
                maxPrice = _maxPrice.value,
                city = _city.value,
                capacity = _capacity.value,
                bedroomCount = _bedroomCount.value,
                bedCount = _bedCount.value,
                bathroomCount = _bathroomCount.value,
                checkedAmenities = _checkedAmenities.value,
                checkedOffers = _checkedOffers.value,
                startDate = _startDate.value,
                endDate = _endDate.value,
                initialLoadSize = initialLoadSize
            )
        }
    }

    private fun createSightseeingPager(): Pager<Int, HomePagingItem> {
        return Pager(PagingConfig(pageSize = pageSize, initialLoadSize = initialLoadSize)) {
            PreferencePagingSource(
                hostId = "HOST-${_touristId.value}",
                repository = repository,
                tag ="Sightseeing",
                searchText = _searchText.value,
                includeStaycation = _includeStaycation.value,
                includeTour = _includeTour.value,
                houseSelected = _houseSelected.value,
                apartmentSelected = _apartmentSelected.value,
                condoSelected = _condoSelected.value,
                campSelected = _campSelected.value,
                guestHouseSelected = _guestHouseSelected.value,
                hotelSelected = _hotelSelected.value,
                photoTourSelected = _photoTourSelected.value,
                foodTripSelected = _foodTripSelected.value,
                barHoppingSelected = _barHoppingSelected.value,
                selectedRating = _selectedRating.value,
                minPrice = _minPrice.value,
                maxPrice = _maxPrice.value,
                city = _city.value,
                capacity = _capacity.value,
                bedroomCount = _bedroomCount.value,
                bedCount = _bedCount.value,
                bathroomCount = _bathroomCount.value,
                checkedAmenities = _checkedAmenities.value,
                checkedOffers = _checkedOffers.value,
                startDate = _startDate.value,
                endDate = _endDate.value,
                initialLoadSize = initialLoadSize
            )
        }
    }

    private fun createSwimmingPager(): Pager<Int, HomePagingItem> {
        return Pager(PagingConfig(pageSize = pageSize, initialLoadSize = initialLoadSize)) {
            PreferencePagingSource(
                hostId = "HOST-${_touristId.value}",
                repository = repository,
                tag ="Swimming",
                searchText = _searchText.value,
                includeStaycation = _includeStaycation.value,
                includeTour = _includeTour.value,
                houseSelected = _houseSelected.value,
                apartmentSelected = _apartmentSelected.value,
                condoSelected = _condoSelected.value,
                campSelected = _campSelected.value,
                guestHouseSelected = _guestHouseSelected.value,
                hotelSelected = _hotelSelected.value,
                photoTourSelected = _photoTourSelected.value,
                foodTripSelected = _foodTripSelected.value,
                barHoppingSelected = _barHoppingSelected.value,
                selectedRating = _selectedRating.value,
                minPrice = _minPrice.value,
                maxPrice = _maxPrice.value,
                city = _city.value,
                capacity = _capacity.value,
                bedroomCount = _bedroomCount.value,
                bedCount = _bedCount.value,
                bathroomCount = _bathroomCount.value,
                checkedAmenities = _checkedAmenities.value,
                checkedOffers = _checkedOffers.value,
                startDate = _startDate.value,
                endDate = _endDate.value,
                initialLoadSize = initialLoadSize
            )
        }
    }

    var forYouPagingData: Flow<PagingData<HomePagingItem>> =
        createForYouPager().flow.cachedIn(viewModelScope)

    var sportsPagingData: Flow<PagingData<HomePagingItem>> =
        createSportsPager().flow.cachedIn(viewModelScope)

    var foodTripPagingData: Flow<PagingData<HomePagingItem>> =
        createFoodTripPager().flow.cachedIn(viewModelScope)

    var shopPagingData: Flow<PagingData<HomePagingItem>> =
        createShopPager().flow.cachedIn(viewModelScope)

    var naturePagingData: Flow<PagingData<HomePagingItem>> =
        createNaturePager().flow.cachedIn(viewModelScope)

    var gamingPagingData: Flow<PagingData<HomePagingItem>> =
        createGamingPager().flow.cachedIn(viewModelScope)

    var karaokePagingData: Flow<PagingData<HomePagingItem>> =
        createKaraokePager().flow.cachedIn(viewModelScope)

    var historyPagingData: Flow<PagingData<HomePagingItem>> =
        createHistoryPager().flow.cachedIn(viewModelScope)

    var clubsPagingData: Flow<PagingData<HomePagingItem>> =
        createClubsPager().flow.cachedIn(viewModelScope)

    var sightseeingPagingData: Flow<PagingData<HomePagingItem>> =
        createSightseeingPager().flow.cachedIn(viewModelScope)

    var swimmingPagingData: Flow<PagingData<HomePagingItem>> =
        createSwimmingPager().flow.cachedIn(viewModelScope)


}


//    val forYouPagingData: Flow<PagingData<HomePagingItem>> = Pager(PagingConfig(pageSize = 15)) {
//        ForYouPagingSource(
//            hostId = "HOST-${_touristId.value}",
//            repository = repository,
//            tags = _preferences.value.map { it.preference },
//            serviceIdSet = _serviceIdSet.value,
//            searchText = _searchText.value,
//            includeStaycation = _includeStaycation.value,
//            includeTour = _includeTour.value,
//            houseSelected = _houseSelected.value,
//            apartmentSelected = _apartmentSelected.value,
//            condoSelected = _condoSelected.value,
//            campSelected = _campSelected.value,
//            guestHouseSelected = _guestHouseSelected.value,
//            hotelSelected = _hotelSelected.value,
//            photoTourSelected = _photoTourSelected.value,
//            foodTripSelected = _foodTripSelected.value,
//            barHoppingSelected = _barHoppingSelected.value,
//            selectedRating = _selectedRating.value,
//            minPrice = _minPrice.value,
//            maxPrice = _maxPrice.value,
//            city = _city.value,
//            capacity = _capacity.value,
//            bedroomCount = _bedroomCount.value,
//            bedCount = _bedCount.value,
//            bathroomCount = _bathroomCount.value,
//            checkedAmenities = _checkedAmenities.value,
//            checkedOffers = _checkedOffers.value,
//            startDate = _startDate.value,
//            endDate = _endDate.value
//        )
//    }.flow.cachedIn(viewModelScope)


//    val sportsPagingData: Flow<PagingData<HomePagingItem>> = Pager(PagingConfig(pageSize = 15)) {
//        PreferencePagingSource(
//            hostId = "HOST-${_touristId.value}",
//            repository = repository,
//            tag ="Sports",
//            searchText = _searchText.value,
//            includeStaycation = _includeStaycation.value,
//            includeTour = _includeTour.value,
//            houseSelected = _houseSelected.value,
//            apartmentSelected = _apartmentSelected.value,
//            condoSelected = _condoSelected.value,
//            campSelected = _campSelected.value,
//            guestHouseSelected = _guestHouseSelected.value,
//            hotelSelected = _hotelSelected.value,
//            photoTourSelected = _photoTourSelected.value,
//            foodTripSelected = _foodTripSelected.value,
//            barHoppingSelected = _barHoppingSelected.value,
//            selectedRating = _selectedRating.value,
//            minPrice = _minPrice.value,
//            maxPrice = _maxPrice.value,
//            city = _city.value,
//            capacity = _capacity.value,
//            bedroomCount = _bedroomCount.value,
//            bedCount = _bedCount.value,
//            bathroomCount = _bathroomCount.value,
//            checkedAmenities = _checkedAmenities.value,
//            checkedOffers = _checkedOffers.value,
//            startDate = _startDate.value,
//            endDate = _endDate.value
//        ) // Initial tags
//    }.flow.cachedIn(viewModelScope)




//    private val _forYouPagingData = MutableStateFlow<PagingData<HomePagingItem>?>(null)
//    val forYouPagingData: StateFlow<PagingData<HomePagingItem>?> = _forYouPagingData
//
//    suspend fun collectForYouPagingData() {
//        val pager = Pager(PagingConfig(pageSize = 15)) {
//            ForYouPagingSource(
//                hostId = "HOST-${_touristId.value}",
//                repository = repository,
//                tags = _preferences.value.map { it.preference },
//                serviceIdSet = _serviceIdSet.value,
//                searchText = _searchText.value,
//                includeStaycation = _includeStaycation.value,
//                includeTour = _includeTour.value,
//                houseSelected = _houseSelected.value,
//                apartmentSelected = _apartmentSelected.value,
//                condoSelected = _condoSelected.value,
//                campSelected = _campSelected.value,
//                guestHouseSelected = _guestHouseSelected.value,
//                hotelSelected = _hotelSelected.value,
//                photoTourSelected = _photoTourSelected.value,
//                foodTripSelected = _foodTripSelected.value,
//                barHoppingSelected = _barHoppingSelected.value,
//                selectedRating = _selectedRating.value,
//                minPrice = _minPrice.value,
//                maxPrice = _maxPrice.value,
//                city = _city.value,
//                capacity = _capacity.value,
//                bedroomCount = _bedroomCount.value,
//                bedCount = _bedCount.value,
//                bathroomCount = _bathroomCount.value,
//                checkedAmenities = _checkedAmenities.value,
//                checkedOffers = _checkedOffers.value,
//                startDate = _startDate.value,
//                endDate = _endDate.value
//            )
//        }
//        _forYouPagingData.value = pager.flow.cachedIn(viewModelScope).first()
//    }


// 02-27-2024 8:29 am
//class HomeViewModel(private val repository: UserRepository = UserRepository()) : ViewModel() {
//
//    private val _selectedTab = MutableStateFlow("For You") // Default tab
//    val selectedTab: StateFlow<String> get() = _selectedTab
//
//    private val _serviceIdSet = MutableStateFlow<SortedSet<String>>(sortedSetOf())
//    val serviceIdSet = _serviceIdSet.asStateFlow()
//
//    private val _preferences =
//        MutableStateFlow<List<Preference>>(emptyList()) // Initialize with an empty Host
//    val preferences = _preferences.asStateFlow()
//
//    private val _touristId = MutableStateFlow("") // Default tab
//    val touristId: StateFlow<String> get() = _touristId
//
//
//    fun selectTab(tab: String) {
//        _selectedTab.value = tab
//
//        Log.d("Selected Tab (VM)", _selectedTab.value)
//    }
//
//    fun getUserPreference(touristId: String) {
//        viewModelScope.launch {
//            try {
//                val preferences = repository.getTouristPreferences(touristId)
//                _preferences.value = preferences
//
//                _touristId.value = touristId
//                Log.d("TouristId", _touristId.value)
////                // temp
////                val preferencesList = listOf(
////                    Preference("Sports"),
////                    Preference("Food Trip"),
////                    Preference("Shop"),
////                    Preference("Nature"),
////                    Preference("Gaming"),
////                    Preference("Karaoke"),
////                    Preference("History"),
////                    Preference("Clubs"),
////                    Preference("Sightseeing"),
////                    Preference("Swimming")
////                )
////                _preferences.value = preferencesList
//
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }
//    }
//
//    fun getUniqueServiceIds() {
//        viewModelScope.launch {
//            try {
//                val uniqueServiceIds = repository.getAllUniqueServiceId().toSortedSet()
//                _serviceIdSet.value = uniqueServiceIds
//                Log.d("Service IDs", _serviceIdSet.value.toString())
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }
//    }
//
//    val forYouPagingData: Flow<PagingData<HomePagingItem>> = Pager(PagingConfig(pageSize = 15)) {
//        ForYouPagingSource(
//            "HOST-${_touristId.value}",
//            repository,
//            _preferences.value.map { it.preference },
//            _serviceIdSet.value,
//        ) // Initial tags
//    }.flow.cachedIn(viewModelScope)
//
//    val sportsPagingData: Flow<PagingData<HomePagingItem>> = Pager(PagingConfig(pageSize = 15)) {
//        PreferencePagingSource(
//            "HOST-${_touristId.value}",
//            repository,
//            "Sports",
//        ) // Initial tags
//    }.flow.cachedIn(viewModelScope)
//
//    val foodTripPagingData: Flow<PagingData<HomePagingItem>> = Pager(PagingConfig(pageSize = 15)) {
//        PreferencePagingSource(
//            "HOST-${_touristId.value}",
//            repository,
//            "Food Trip",
//        ) // Initial tags
//    }.flow.cachedIn(viewModelScope)
//
//    val shopPagingData: Flow<PagingData<HomePagingItem>> = Pager(PagingConfig(pageSize = 15)) {
//        PreferencePagingSource(
//            "HOST-${_touristId.value}",
//            repository,
//            "Shop",
//        ) // Initial tags
//    }.flow.cachedIn(viewModelScope)
//
//    val naturePagingData: Flow<PagingData<HomePagingItem>> = Pager(PagingConfig(pageSize = 15)) {
//        PreferencePagingSource(
//            "HOST-${_touristId.value}",
//            repository,
//            "Nature",
//        ) // Initial tags
//    }.flow.cachedIn(viewModelScope)
//
//    val gamingPagingData: Flow<PagingData<HomePagingItem>> = Pager(PagingConfig(pageSize = 15)) {
//        PreferencePagingSource(
//            "HOST-${_touristId.value}",
//            repository,
//            "Gaming",
//        ) // Initial tags
//    }.flow.cachedIn(viewModelScope)
//
//    val karaokePagingData: Flow<PagingData<HomePagingItem>> = Pager(PagingConfig(pageSize = 15)) {
//        PreferencePagingSource(
//            "HOST-${_touristId.value}",
//            repository,
//            "Karaoke",
//        ) // Initial tags
//    }.flow.cachedIn(viewModelScope)
//
//    val historyPagingData: Flow<PagingData<HomePagingItem>> = Pager(PagingConfig(pageSize = 15)) {
//        PreferencePagingSource(
//            "HOST-${_touristId.value}",
//            repository,
//            "History",
//        ) // Initial tags
//    }.flow.cachedIn(viewModelScope)
//
//    val clubsPagingData: Flow<PagingData<HomePagingItem>> = Pager(PagingConfig(pageSize = 15)) {
//        PreferencePagingSource(
//            "HOST-${_touristId.value}",
//            repository,
//            "Clubs",
//        ) // Initial tags
//    }.flow.cachedIn(viewModelScope)
//
//    val sightseeingPagingData: Flow<PagingData<HomePagingItem>> = Pager(PagingConfig(pageSize = 15)) {
//        PreferencePagingSource(
//            "HOST-${_touristId.value}",
//            repository,
//            "Sightseeing",
//        ) // Initial tags
//    }.flow.cachedIn(viewModelScope)
//
//    val swimmingPagingData: Flow<PagingData<HomePagingItem>> = Pager(PagingConfig(pageSize = 15)) {
//        PreferencePagingSource(
//            "HOST-${_touristId.value}",
//            repository,
//            "Swimming",
//        ) // Initial tags
//    }.flow.cachedIn(viewModelScope)
//
//}
// --------------------------------------------------------------------------------------

// 02-26-2024 4:24 PM
//class HomeViewModel(private val repository: UserRepository = UserRepository()) : ViewModel() {
//
//    private val _selectedTab = MutableStateFlow("For You") // Default tab
//    val selectedTab: StateFlow<String> get() = _selectedTab
//
//    private val _serviceIdSet = MutableStateFlow<SortedSet<String>>(sortedSetOf())
//    val serviceIdSet = _serviceIdSet.asStateFlow()
//
//    private val _preferences =
//        MutableStateFlow<List<Preference>>(emptyList()) // Initialize with an empty Host
//    val preferences = _preferences.asStateFlow()
//
//    private val _touristId = MutableStateFlow("") // Default tab
//    val touristId: StateFlow<String> get() = _touristId
//
//
//    fun selectTab(tab: String) {
//        _selectedTab.value = tab
//
//        Log.d("Selected Tab (VM)", _selectedTab.value)
//    }
//
//    fun getUserPreference(touristId: String) {
//        viewModelScope.launch {
//            try {
//                val preferences = repository.getTouristPreferences(touristId)
//                _preferences.value = preferences
//
//                _touristId.value = touristId
//                Log.d("TouristId", _touristId.value)
//////                // temp
////                val preferencesList = listOf(
////                    Preference("Sports"),
////                    Preference("Food Trip"),
////                    Preference("Shop"),
////                    Preference("Nature"),
////                    Preference("Gaming"),
////                    Preference("Karaoke"),
////                    Preference("History"),
////                    Preference("Clubs"),
////                    Preference("Sightseeing"),
////                    Preference("Swimming")
////                )
////                _preferences.value = preferencesList
//
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }
//    }
//
//    fun getUniqueServiceIds() {
//        viewModelScope.launch {
//            try {
//                val uniqueServiceIds = repository.getAllUniqueServiceId().toSortedSet()
//                _serviceIdSet.value = uniqueServiceIds
//                Log.d("Service IDs", _serviceIdSet.value.toString())
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }
//    }
//
//    val forYouPagingData: Flow<PagingData<HomePagingItem>> = Pager(PagingConfig(pageSize = 6)) {
//        ForYouPagingSource(
//            "HOST-${_touristId.value}",
//            repository,
//            _preferences.value.map { it.preference },
//            _serviceIdSet.value,
//        ) // Initial tags
//    }.flow.cachedIn(viewModelScope)
//
//    val sportsPagingData: Flow<PagingData<HomePagingItem>> = Pager(PagingConfig(pageSize = 6)) {
//        PreferencePagingSource(
//            "HOST-${_touristId.value}",
//            repository,
//            "Sports",
//        ) // Initial tags
//    }.flow.cachedIn(viewModelScope)
//
//    val foodTripPagingData: Flow<PagingData<HomePagingItem>> = Pager(PagingConfig(pageSize = 6)) {
//        PreferencePagingSource(
//            "HOST-${_touristId.value}",
//            repository,
//            "Food Trip",
//        ) // Initial tags
//    }.flow.cachedIn(viewModelScope)
//
//    val shopPagingData: Flow<PagingData<HomePagingItem>> = Pager(PagingConfig(pageSize = 6)) {
//        PreferencePagingSource(
//            "HOST-${_touristId.value}",
//            repository,
//            "Shop",
//        ) // Initial tags
//    }.flow.cachedIn(viewModelScope)
//
//    val naturePagingData: Flow<PagingData<HomePagingItem>> = Pager(PagingConfig(pageSize = 6)) {
//        PreferencePagingSource(
//            "HOST-${_touristId.value}",
//            repository,
//            "Nature",
//        ) // Initial tags
//    }.flow.cachedIn(viewModelScope)
//
//    val gamingPagingData: Flow<PagingData<HomePagingItem>> = Pager(PagingConfig(pageSize = 6)) {
//        PreferencePagingSource(
//            "HOST-${_touristId.value}",
//            repository,
//            "Gaming",
//        ) // Initial tags
//    }.flow.cachedIn(viewModelScope)
//
//    val karaokePagingData: Flow<PagingData<HomePagingItem>> = Pager(PagingConfig(pageSize = 6)) {
//        PreferencePagingSource(
//            "HOST-${_touristId.value}",
//            repository,
//            "Karaoke",
//        ) // Initial tags
//    }.flow.cachedIn(viewModelScope)
//
//    val historyPagingData: Flow<PagingData<HomePagingItem>> = Pager(PagingConfig(pageSize = 6)) {
//        PreferencePagingSource(
//            "HOST-${_touristId.value}",
//            repository,
//            "History",
//        ) // Initial tags
//    }.flow.cachedIn(viewModelScope)
//
//    val clubsPagingData: Flow<PagingData<HomePagingItem>> = Pager(PagingConfig(pageSize = 6)) {
//        PreferencePagingSource(
//            "HOST-${_touristId.value}",
//            repository,
//            "Clubs",
//        ) // Initial tags
//    }.flow.cachedIn(viewModelScope)
//
//    val sightseeingPagingData: Flow<PagingData<HomePagingItem>> = Pager(PagingConfig(pageSize = 6)) {
//        PreferencePagingSource(
//            "HOST-${_touristId.value}",
//            repository,
//            "Sightseeing",
//        ) // Initial tags
//    }.flow.cachedIn(viewModelScope)
//
//    val swimmingPagingData: Flow<PagingData<HomePagingItem>> = Pager(PagingConfig(pageSize = 6)) {
//        PreferencePagingSource(
//            "HOST-${_touristId.value}",
//            repository,
//            "Swimming",
//        ) // Initial tags
//    }.flow.cachedIn(viewModelScope)
//
//}
// ---------------------------------------------------------------------------------------------------


//
//// TEMP
//private val _itemsForYou =
//    MutableStateFlow<List<HomePagingItem>>(emptyList())
//val itemsForYou = _itemsForYou.asStateFlow()
//
//private val _itemsSports =
//    MutableStateFlow<List<HomePagingItem>>(emptyList())
//val itemsSports = _itemsSports.asStateFlow()
//
//private val _itemsFoodTrip =
//    MutableStateFlow<List<HomePagingItem>>(emptyList())
//val itemsFoodTrip = _itemsFoodTrip.asStateFlow()
//
//private val _itemsShop =
//    MutableStateFlow<List<HomePagingItem>>(emptyList())
//val itemsShop = _itemsShop.asStateFlow()
//
//private val _itemsNature =
//    MutableStateFlow<List<HomePagingItem>>(emptyList())
//val itemsNature = _itemsNature.asStateFlow()
//
//private val _itemsGaming =
//    MutableStateFlow<List<HomePagingItem>>(emptyList())
//val itemsGaming = _itemsGaming.asStateFlow()
//
//private val _itemsKaraoke =
//    MutableStateFlow<List<HomePagingItem>>(emptyList())
//val itemsKaraoke = _itemsKaraoke.asStateFlow()
//
//private val _itemsHistory =
//    MutableStateFlow<List<HomePagingItem>>(emptyList())
//val itemsHistory = _itemsHistory.asStateFlow()
//
//private val _itemsClubs =
//    MutableStateFlow<List<HomePagingItem>>(emptyList())
//val itemsClubs = _itemsClubs.asStateFlow()
//
//private val _itemsSightseeing =
//    MutableStateFlow<List<HomePagingItem>>(emptyList())
//val itemsSightseeing = _itemsSightseeing.asStateFlow()
//
//private val _itemsSwimming =
//    MutableStateFlow<List<HomePagingItem>>(emptyList())
//val itemsSwimming = _itemsSwimming.asStateFlow()
//
//
//fun getServicesByTab(tab: String) {
//    viewModelScope.launch {
//        try {
//
//            Log.d("Current Tab(VM Fun):", _selectedTab.value)
//
//            when (_selectedTab.value) {
////                    "For You" -> {
////                        val tabs = _preferences.value.map { it.preference }
////                        _itemsForYou.value = repository.getAllServicesByTags(tabs)
////                        _itemsForYou.value = _itemsForYou.value.distinctBy { it.serviceId }
////                    }
////                    "Sports" -> { _itemsSports.value = repository.getAllServicesByTag(_selectedTab.value) }
////                    "Food Trip" -> { _itemsFoodTrip.value = repository.getAllServicesByTag(_selectedTab.value) }
////                    "Shop" -> { _itemsShop.value = repository.getAllServicesByTag(_selectedTab.value) }
////                    "Nature" -> { _itemsNature.value = repository.getAllServicesByTag(_selectedTab.value) }
////                    "Gaming" -> { _itemsGaming.value = repository.getAllServicesByTag(_selectedTab.value) }
////                    "Karaoke" -> { _itemsKaraoke.value = repository.getAllServicesByTag(_selectedTab.value) }
////                    "History" -> { _itemsHistory.value = repository.getAllServicesByTag(_selectedTab.value) }
////                    "Clubs" -> { _itemsClubs.value = repository.getAllServicesByTag(_selectedTab.value) }
////                    "Sightseeing" -> { _itemsSightseeing.value = repository.getAllServicesByTag(_selectedTab.value) }
////                    "Swimming" -> { _itemsSwimming.value = repository.getAllServicesByTag(_selectedTab.value) }
//                else -> {
//                    Log.d("getServicesByTab Error", "${_preferences.value}")
//                }
//            }
//
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//    }
//}


//    fun fetchServicesByTab(touristId: String): Flow<PagingData<HomePagingItem>> {
//        return flow {
////            val preferences = repository.getTouristPreferences(touristId)
////            _preferences.value = preferences
//
//            val preferencesList = listOf(
//                Preference("Sports"),
//                Preference("Food Trip"),
//                Preference("Shop"),
//                Preference("Nature"),
//                Preference("Gaming"),
//                Preference("Karaoke"),
//                Preference("History"),
//                Preference("Clubs"),
//                Preference("Sightseeing"),
//                Preference("Swimming")
//            )
//            _preferences.value = preferencesList
//
//            val pagingConfig = PagingConfig(
//                pageSize = 5, // Adjust as needed
//                enablePlaceholders = false
//            )
//
//            val pagingDataFlow = Pager(
//                config = pagingConfig,
//                pagingSourceFactory = { HomePagingSource(repository, _preferences.value.map { it.preference }) }
//            ).flow
//
//            emitAll(pagingDataFlow)
//        }.cachedIn(viewModelScope)
//    }

//    fun fetchServicesByTab(touristId: String): Flow<PagingData<HomePagingItem>> {
//        viewModelScope.launch {
//            val preferences = repository.getTouristPreferences(touristId)
//            _preferences.value = preferences
//
//            val pagingConfig = PagingConfig(
//                pageSize = 10, // Adjust as needed
//                enablePlaceholders = false
//            )
//
//            return@launch Pager(
//                config = pagingConfig,
//                pagingSourceFactory = { HomePagingSource(repository, _preferences.value.map { it.preference }) }
//            ).flow.cachedIn(viewModelScope)
//        }
//
//
//    }

//    fun getServicesByTab(tab: String) {
//        viewModelScope.launch {
//            try {
//
//                Log.d("Current Tab(VM Fun):", _selectedTab.value)
//
//                when (_selectedTab.value) {
//                    "For You" -> {
//                        val tabs = _preferences.value.map { it.preference }
//                        _itemsForYou.value = repository.getAllServicesByTags(tabs)
//                        _itemsForYou.value = _itemsForYou.value.distinctBy { it.serviceId }
//                    }
//                    "Sports" -> { _itemsSports.value = repository.getAllServicesByTag(_selectedTab.value) }
//                    "Food Trip" -> { _itemsFoodTrip.value = repository.getAllServicesByTag(_selectedTab.value) }
//                    "Shop" -> { _itemsShop.value = repository.getAllServicesByTag(_selectedTab.value) }
//                    "Nature" -> { _itemsNature.value = repository.getAllServicesByTag(_selectedTab.value) }
//                    "Gaming" -> { _itemsGaming.value = repository.getAllServicesByTag(_selectedTab.value) }
//                    "Karaoke" -> { _itemsKaraoke.value = repository.getAllServicesByTag(_selectedTab.value) }
//                    "History" -> { _itemsHistory.value = repository.getAllServicesByTag(_selectedTab.value) }
//                    "Clubs" -> { _itemsClubs.value = repository.getAllServicesByTag(_selectedTab.value) }
//                    "Sightseeing" -> { _itemsSightseeing.value = repository.getAllServicesByTag(_selectedTab.value) }
//                    "Swimming" -> { _itemsSwimming.value = repository.getAllServicesByTag(_selectedTab.value) }
//                    else -> {
//                        Log.d("getServicesByTab Error", "${_preferences.value}")
//                    }
//                }
//
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }
//    }

//    fun getServicesByTab(tab: String) {
//        viewModelScope.launch {
//            try {
//                if (_selectedTab.value == "For You") {
//                    val tabs = _preferences.value.map { it.preference }
//                    Log.d("getServicesByTab function", "For You")
//                    Log.d("preferences", "${_preferences.value}")
//
//                    _itemsForYou.value = repository.getAllServicesByTags(tabs)
//                } else {
//                    Log.d("getServicesByTab function", "${_preferences.value}")
//
//                    _items.value = repository.getAllServicesByTag(tab)
//                }
//
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }
//    }

//fun getStaycationsByTab(tab: String): Flow<PagingData<Staycation>> {
//    val pagingConfig = PagingConfig(
//        pageSize = 10, // Adjust as needed
//        enablePlaceholders = false
//    )
//
//    return if (tab == "For You") {
//        val tabs = _preferences.value.map { it.preference }
//
//        Pager(
//            config = pagingConfig,
//            pagingSourceFactory = { ForYouPagingSource(repository, tabs) }
//        ).flow.cachedIn(viewModelScope)
//
//    } else {
//        Pager(
//            config = pagingConfig,
//            pagingSourceFactory = { StaycationPagingSource(repository, tab) }
//        ).flow.cachedIn(viewModelScope)
//    }
//
//}

//    suspend fun getServicesByTab(tab: String): List<HomePagingItem> {
//        return try {
//            if (_selectedTab.value == "For You") {
//                val tabs = _preferences.value.map { it.preference }
//                Log.d("getServicesByTab function", "For You")
//                Log.d("preferences", "${_preferences.value}")
//
//                _items.value = repository.getAllServicesByTags(tabs)
//            } else {
//                Log.d("getServicesByTab function", "${_preferences.value}")
//
//                repository.getAllServicesByTag(tab)
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//            emptyList() // Return an empty list or handle the error case as needed
//        }
//    }


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

