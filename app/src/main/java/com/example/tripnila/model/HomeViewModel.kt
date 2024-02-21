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
import kotlinx.coroutines.launch
import java.util.SortedSet

class HomeViewModel(private val repository: UserRepository = UserRepository()) : ViewModel() {

    private val _selectedTab = MutableStateFlow("For You") // Default tab
    val selectedTab: StateFlow<String> get() = _selectedTab

    private val _serviceIdSet = MutableStateFlow<SortedSet<String>>(sortedSetOf())
    val serviceIdSet = _serviceIdSet.asStateFlow()

    private val _preferences =
        MutableStateFlow<List<Preference>>(emptyList()) // Initialize with an empty Host
    val preferences = _preferences.asStateFlow()

    private val _touristId = MutableStateFlow("") // Default tab
    val touristId: StateFlow<String> get() = _touristId


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
                Log.d("HostId", "HOST-${_touristId.value}")
                Log.d("TouristId", _touristId.value)
////                // temp
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

    val forYouPagingData: Flow<PagingData<HomePagingItem>> = Pager(PagingConfig(pageSize = 6)) {
        ForYouPagingSource(
            "HOST-${_touristId.value}",
            repository,
            _preferences.value.map { it.preference },
            _serviceIdSet.value,
        ) // Initial tags
    }.flow.cachedIn(viewModelScope)

    val sportsPagingData: Flow<PagingData<HomePagingItem>> = Pager(PagingConfig(pageSize = 6)) {
        PreferencePagingSource(
            "HOST-${_touristId.value}",
            repository,
            "Sports",
        ) // Initial tags
    }.flow.cachedIn(viewModelScope)

    val foodTripPagingData: Flow<PagingData<HomePagingItem>> = Pager(PagingConfig(pageSize = 6)) {
        PreferencePagingSource(
            "HOST-${_touristId.value}",
            repository,
            "Food Trip",
        ) // Initial tags
    }.flow.cachedIn(viewModelScope)

    val shopPagingData: Flow<PagingData<HomePagingItem>> = Pager(PagingConfig(pageSize = 6)) {
        PreferencePagingSource(
            "HOST-${_touristId.value}",
            repository,
            "Shop",
        ) // Initial tags
    }.flow.cachedIn(viewModelScope)

    val naturePagingData: Flow<PagingData<HomePagingItem>> = Pager(PagingConfig(pageSize = 6)) {
        PreferencePagingSource(
            "HOST-${_touristId.value}",
            repository,
            "Nature",
        ) // Initial tags
    }.flow.cachedIn(viewModelScope)

    val gamingPagingData: Flow<PagingData<HomePagingItem>> = Pager(PagingConfig(pageSize = 6)) {
        PreferencePagingSource(
            "HOST-${_touristId.value}",
            repository,
            "Gaming",
        ) // Initial tags
    }.flow.cachedIn(viewModelScope)

    val karaokePagingData: Flow<PagingData<HomePagingItem>> = Pager(PagingConfig(pageSize = 6)) {
        PreferencePagingSource(
            "HOST-${_touristId.value}",
            repository,
            "Karaoke",
        ) // Initial tags
    }.flow.cachedIn(viewModelScope)

    val historyPagingData: Flow<PagingData<HomePagingItem>> = Pager(PagingConfig(pageSize = 6)) {
        PreferencePagingSource(
            "HOST-${_touristId.value}",
            repository,
            "History",
        ) // Initial tags
    }.flow.cachedIn(viewModelScope)

    val clubsPagingData: Flow<PagingData<HomePagingItem>> = Pager(PagingConfig(pageSize = 6)) {
        PreferencePagingSource(
            "HOST-${_touristId.value}",
            repository,
            "Clubs",
        ) // Initial tags
    }.flow.cachedIn(viewModelScope)

    val sightseeingPagingData: Flow<PagingData<HomePagingItem>> = Pager(PagingConfig(pageSize = 6)) {
        PreferencePagingSource(
            "HOST-${_touristId.value}",
            repository,
            "Sightseeing",
        ) // Initial tags
    }.flow.cachedIn(viewModelScope)

    val swimmingPagingData: Flow<PagingData<HomePagingItem>> = Pager(PagingConfig(pageSize = 6)) {
        PreferencePagingSource(
            "HOST-${_touristId.value}",
            repository,
            "Swimming",
        ) // Initial tags
    }.flow.cachedIn(viewModelScope)

}


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

