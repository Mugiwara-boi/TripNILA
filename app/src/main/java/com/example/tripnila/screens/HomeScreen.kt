package com.example.tripnila.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.areNavigationBarsVisible
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.twotone.Close
import androidx.compose.material.icons.twotone.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.TopCenter
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.example.tripnila.R
import com.example.tripnila.common.LoadingScreen
import com.example.tripnila.common.Orange
import com.example.tripnila.common.Tag
import com.example.tripnila.common.TouristBottomNavigationBar
import com.example.tripnila.data.HomePagingItem
import com.example.tripnila.model.HomeViewModel
import com.patrykandpatrick.vico.core.extension.mutableListOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter


@SuppressLint("StateFlowValueCalledInComposition", "RememberReturnType")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class,
    ExperimentalLayoutApi::class
)
@Composable
fun HomeScreen(
    touristId: String,
    homeViewModel: HomeViewModel,
    onNavToDetailScreen: (String, String) -> Unit,
    onNavToTourDetails: (String, String) -> Unit,
    navController: NavHostController
){

    LaunchedEffect(touristId) {

        Log.d("TouristID", touristId)

        if (touristId != homeViewModel.touristId.value) {

            homeViewModel.setUserId(touristId)
            homeViewModel.getUserPreference()

            homeViewModel.refreshForYouPagingData()
            homeViewModel.refreshSportsPagingData()
            homeViewModel.refreshFoodTripPagingData()
            homeViewModel.refreshShopPagingData()
            homeViewModel.refreshNaturePagingData()
            homeViewModel.refreshGamingPagingData()
            homeViewModel.refreshKaraokePagingData()
            homeViewModel.refreshHistoryPagingData()
            homeViewModel.refreshClubsPagingData()
            homeViewModel.refreshSightseeingPagingData()
            homeViewModel.refreshSwimmingPagingData()




        }




    //    homeViewModel.getAllTags()

/*
        homeViewModel.refreshForYouPagingData()
        homeViewModel.refreshSportsPagingData()
        homeViewModel.refreshFoodTripPagingData()
        homeViewModel.refreshShopPagingData()
        homeViewModel.refreshNaturePagingData()
        homeViewModel.refreshGamingPagingData()
        homeViewModel.refreshKaraokePagingData()
        homeViewModel.refreshHistoryPagingData()
        homeViewModel.refreshClubsPagingData()
        homeViewModel.refreshSightseeingPagingData()
        homeViewModel.refreshSwimmingPagingData()
*/

        Log.d("Selected Tab", homeViewModel.selectedTab.value)
    }

    var selectedItemIndex by rememberSaveable {
        mutableIntStateOf(0)
    }

    val hasNavigationBar = WindowInsets.areNavigationBarsVisible

    val preferences = listOf("For You", "Sports", "Food Trip", "Shop", "Nature", "Gaming", "Karaoke", "History", "Clubs", "Sightseeing", "Swimming")

    // Observe the selected tab from the HomeViewModel
    val selectedTab by homeViewModel.selectedTab.collectAsState()
 //   val serviceIdSet by homeViewModel.serviceIdSet.collectAsState()

    val fetchedTags by homeViewModel.fetchedTags.collectAsState()


    val pagerState = rememberPagerState(
        initialPage = preferences.indexOf(homeViewModel.selectedTab.value),
        initialPageOffsetFraction = 0f
    ) {
        preferences.size
    }
    val scope = rememberCoroutineScope()

    var openModalBottomSheet by remember { mutableStateOf(false) }
    val modalBottomSheetState = rememberModalBottomSheetState( skipPartiallyExpanded = true)

    val searchText by homeViewModel.searchText.collectAsState()

    val includeStaycation by homeViewModel.includeStaycation.collectAsState()
    val includeTour by homeViewModel.includeTour.collectAsState()

    val houseSelected by homeViewModel.houseSelected.collectAsState()
    val apartmentSelected by homeViewModel.apartmentSelected.collectAsState()
    val condoSelected by homeViewModel.condoSelected.collectAsState()
    val campSelected by homeViewModel.campSelected.collectAsState()
    val guestHouseSelected by homeViewModel.guestHouseSelected.collectAsState()
    val hotelSelected by homeViewModel.hotelSelected.collectAsState()

    val photoTourSelected by homeViewModel.photoTourSelected.collectAsState()
    val foodTripSelected by homeViewModel.foodTripSelected.collectAsState()
    val barHoppingSelected by homeViewModel.barHoppingSelected.collectAsState()

    val minPrice by homeViewModel.minPrice.collectAsState()
    val maxPrice by homeViewModel.maxPrice.collectAsState()

    val selectedRating by homeViewModel.selectedRating.collectAsState()

    val city by homeViewModel.city.collectAsState()
    val capacity by homeViewModel.capacity.collectAsState()

    val bedroomCount by homeViewModel.bedroomCount.collectAsState()
    val bedCount by homeViewModel.bedCount.collectAsState()
    val bathroomCount by homeViewModel.bathroomCount.collectAsState()

    val allAmenities = listOf("Wifi", "TV", "Kitchen", "Washing machine", "Dedicated workspace" , "Pool", "Gym equipment", "Hot tub", "City view")
    val checkedAmenities by homeViewModel.checkedAmenities.collectAsState()
    val onAmenityCheckedChange: (Int, Boolean) -> Unit = { index, isChecked ->
        homeViewModel.updateCheckedAmenities(checkedAmenities.toMutableList().apply { set(index, isChecked) })
    }
    var showAllAmenities by remember { mutableStateOf(false) }
    val visibleAmenities = if (showAllAmenities) allAmenities else allAmenities.take(6)
    val toggleShowAllAmenities: () -> Unit = {
        showAllAmenities = !showAllAmenities
    }

    val allOffers = listOf("Food", "Souvenir", "Transportation", "Drinks")
    val checkedOffers by homeViewModel.checkedOffers.collectAsState()

    val startDate by homeViewModel.startDate.collectAsState()
    val endDate by homeViewModel.endDate.collectAsState()


    val onOfferCheckedChange: (Int, Boolean) -> Unit = { index, isChecked ->
        homeViewModel.updateCheckedOffers(checkedOffers.toMutableList().apply { set(index, isChecked) })
    }

    val dateRangePickerState = rememberDateRangePickerState(
        initialSelectedStartDateMillis = startDate,
        initialSelectedEndDateMillis = endDate
    )


    LaunchedEffect(dateRangePickerState.selectedStartDateMillis, dateRangePickerState.selectedEndDateMillis) {

        Log.d("Start: ", dateRangePickerState.selectedStartDateMillis.toString())
        Log.d("End: ", dateRangePickerState.selectedEndDateMillis.toString())

        homeViewModel.updateStartDate(dateRangePickerState.selectedStartDateMillis)
        homeViewModel.updateEndDate(dateRangePickerState.selectedEndDateMillis)
    }




    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        if (homeViewModel.touristId.value != touristId) {
            LoadingScreen(isLoadingCompleted = false, isLightModeActive = true)
        } else {
            Scaffold(
                bottomBar = {
                    TouristBottomNavigationBar(
                        touristId = touristId,
                        navController = navController,
                        selectedItemIndex = selectedItemIndex,
                        onItemSelected = { newIndex ->
                            selectedItemIndex = newIndex
                        }
                    )
                },
                topBar = {
                    TopAppBar(
                        title = {
                            Text(
                                text = "Home",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Medium
                            )
                        },
                        actions = {
                            IconButton(
                                onClick = { openModalBottomSheet = true },
                            ) {
                                Icon(
                                    imageVector = ImageVector.vectorResource(R.drawable.filter),
                                    contentDescription = "Close",
                                    tint = Color(0xFF333333),
                                    modifier = Modifier.size(40.dp)
                                )
                            }
                        },
                        modifier = Modifier
                            .drawWithContent {
                                drawContent()
                                drawLine(
                                    color = Color(0xFFF8F8F9),
                                    start = Offset(0f, size.height),
                                    end = Offset(size.width, size.height),
                                    strokeWidth = 2f
                                )
                            }
                    )



                }
            ) {

                Column(
                    modifier = Modifier
                        .padding(it)
                        .fillMaxSize()
                ) {
                    //TripNilaIcon(modifier = Modifier.offset(x = (-15).dp, y = (-7).dp))
                    ScrollableTabRow(
                        selectedTabIndex = preferences.indexOf(selectedTab),
                        edgePadding = 3.dp,
                        indicator = { tabPositions ->
                            TabRowDefaults.Indicator(
                                color = Orange,
                                modifier = Modifier.tabIndicatorOffset(tabPositions[preferences.indexOf(selectedTab)])
                            )
                        },
                        divider = { Divider(color = Color.Transparent) }
                    ) {
                        preferences.forEach { tabName ->
                            val isSelected = selectedTab == tabName
                            Tab(
                                selected = isSelected,
                                onClick = {
                                    homeViewModel.selectTab(tabName)
                                    //  homeViewModel.getServicesByTab(selectedTab)

                                    scope.launch {
                                        pagerState.animateScrollToPage(preferences.indexOf(tabName))
                                    }

                                },
                                modifier = Modifier.padding(horizontal = 5.dp)
                            ) {
                                Text(
                                    text = tabName,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = if (isSelected) Orange else Color.Gray,
                                    modifier = Modifier.padding(
                                        top = 12.dp,
                                        bottom = 5.dp,
                                        start = 7.dp,
                                        end = 7.dp
                                    ),
                                )
                            }
                        }
                    }

                    HorizontalPager(
                        state = pagerState,
                        userScrollEnabled = false,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        if (fetchedTags.isNotEmpty()) {
                            val lazyPagingItems = when (selectedTab) {

                                "For You" -> homeViewModel.forYouPagingData.collectAsLazyPagingItems()
                                "Sports" -> homeViewModel.sportsPagingData.collectAsLazyPagingItems()
                                "Food Trip" -> homeViewModel.foodTripPagingData.collectAsLazyPagingItems()
                                "Shop" -> homeViewModel.shopPagingData.collectAsLazyPagingItems()
                                "Nature" -> homeViewModel.naturePagingData.collectAsLazyPagingItems()
                                "Gaming" -> homeViewModel.gamingPagingData.collectAsLazyPagingItems()
                                "Karaoke" -> homeViewModel.karaokePagingData.collectAsLazyPagingItems()
                                "History" -> homeViewModel.historyPagingData.collectAsLazyPagingItems()
                                "Clubs" -> homeViewModel.clubsPagingData.collectAsLazyPagingItems()
                                "Sightseeing" -> homeViewModel.sightseeingPagingData.collectAsLazyPagingItems()
                                "Swimming" -> homeViewModel.swimmingPagingData.collectAsLazyPagingItems()
                                else -> null
                            }

                            if (lazyPagingItems != null) {

                                Column(
                                    modifier = Modifier.fillMaxSize()
                                ) {

                                    Box(modifier = Modifier.fillMaxSize()) {
                                        if (lazyPagingItems.loadState.refresh is LoadState.Loading) {
                                            CircularProgressIndicator(
                                                color = Orange,
                                                modifier = Modifier
                                                    .size(50.dp)
                                                    .padding(16.dp)
                                                    .align(TopCenter)
                                            )
                                        }

                                        LazyVerticalGrid(
                                            state = rememberLazyGridState(),
                                            columns = GridCells.Fixed(2), // Number of columns
                                            contentPadding = PaddingValues(16.dp),
                                            verticalArrangement = Arrangement.spacedBy(16.dp),
                                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                                        ) {
                                            items(lazyPagingItems.itemCount) { index ->
                                                val service = lazyPagingItems[index]
                                                val cardHeight = (150..180).random()
                                                val imageHeight = cardHeight - 55
                                                // Step 2: Create a Composable for a single grid item
                                                if (service != null) {
                                                    ServiceListingCard(
                                                        service = service,
                                                        homeViewModel = homeViewModel,
                                                        imageHeight = imageHeight.dp,
                                                        touristId = touristId,
                                                        scope = scope,
                                                        onItemClick = { serviceType ->
                                                            if (serviceType == "Tour") {
                                                                onNavToTourDetails(
                                                                    touristId,
                                                                    service.serviceId
                                                                )
                                                            } else {
                                                                onNavToDetailScreen(
                                                                    touristId,
                                                                    service.serviceId
                                                                )
                                                            }
                                                        }
                                                    )
                                                }
                                            }

                                            if (lazyPagingItems.loadState.append is LoadState.Loading) {
                                                item(
                                                    span = {
                                                        GridItemSpan(2)
                                                    }
                                                ) {
                                                    Box(
                                                        modifier = Modifier.fillMaxWidth(),
                                                        contentAlignment = Alignment.Center
                                                    ) {
                                                        CircularProgressIndicator(
                                                            color = Orange,
                                                            modifier = Modifier
                                                                .size(50.dp)
                                                                .padding(16.dp)
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                }
            }

            if (openModalBottomSheet) {
                ModalBottomSheet(
                    shape = RoundedCornerShape(20.dp),
                    containerColor = Color.White,
                    dragHandle = {
                        Column {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Start,
                                modifier = Modifier
                                    .padding(start = 16.dp, end = 10.dp) //, top = 3.dp
                                    .fillMaxWidth()
                            ) {
                                Text(
                                    text = "Filter",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium
                                )
                                Spacer(modifier = Modifier.weight(1f))
                                IconButton(
                                    onClick = {
                                        openModalBottomSheet = false
                                    },
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Close,
                                        contentDescription = "Close"
                                    )
                                }
                            }
                            Divider(modifier = Modifier.fillMaxWidth())
                        }
                    },
                    onDismissRequest = { openModalBottomSheet = false },
                    sheetState = modalBottomSheetState,
                    modifier = Modifier
                        .fillMaxHeight(0.9f) //0.693
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .background(Color.White)
                            .padding(horizontal = 16.dp)
                    ) {

                        item {
                            SearchField(
                                initialValue = searchText,
                                onValueChange = {
                                    homeViewModel.updateSearchText(it)
                                },
                                onClear = {
                                    homeViewModel.updateSearchText("")
                                },
                                modifier = Modifier
                                    .padding(top = 10.dp)
                                    .fillMaxWidth()
                            )
                        }

                        item {
                            Text(
                                text = "Type of Service",
                                color = Color.Black,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(top = 10.dp)
                            )
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceAround,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 10.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .height(20.dp)
                                ) {
                                    Checkbox(
                                        checked = includeStaycation,
                                        onCheckedChange = { checked ->
                                            homeViewModel.updateIncludeStaycation(checked)
                                        },
                                        colors = CheckboxDefaults.colors(
                                            checkedColor = Orange
                                        ),
                                    )
                                    Text(
                                        text = "Show Staycations",
                                        color = Color(0xff666666),
                                        fontWeight = FontWeight.Medium,
                                        fontSize = 14.sp,
                                    )
                                }
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .height(20.dp)
                                ) {
                                    Checkbox(
                                        checked = includeTour,
                                        onCheckedChange = { checked ->
                                            homeViewModel.updateIncludeTour(checked)
                                        },
                                        colors = CheckboxDefaults.colors(
                                            checkedColor = Orange
                                        ),
                                    )
                                    Text(
                                        text = "Show Tours",
                                        color = Color(0xff666666),
                                        fontWeight = FontWeight.Medium,
                                        fontSize = 14.sp,
                                    )
                                }
                            }

                        }

                        item {
                            // ------------------------------------------------------
                            // STAYCATION TYPE
                            Text(
                                text = "Type of Staycation",
                                color = Color.Black,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                            )
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    // .padding(top = 5.dp, bottom = 5.dp)
                                    .padding(vertical = 5.dp)
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.Start,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    TypeFilterChip(
                                        text = "House",
                                        enabled = includeStaycation,
                                        selected = if (includeStaycation) houseSelected else false,  //if (includeStaycation) bedroomCount == bedroomCountOption else false,
                                        onSelectedChange = { selected ->
//                                        houseSelected = selected
                                            homeViewModel.updateHouseSelected(selected)
                                        }
                                    )
                                    Spacer(modifier = Modifier.width(5.dp))
                                    TypeFilterChip(
                                        text = "Apartment",
                                        enabled = includeStaycation,
                                        selected = if (includeStaycation) apartmentSelected else false,
                                        onSelectedChange = { selected ->
                                            homeViewModel.updateApartmentSelected(selected)
                                        }
                                    )
                                    Spacer(modifier = Modifier.width(5.dp))
                                    TypeFilterChip(
                                        text = "Condominium",
                                        enabled = includeStaycation,
                                        selected = if (includeStaycation) condoSelected else false,
                                        onSelectedChange = { selected ->
                                            homeViewModel.updateCondoSelected(selected)
                                        }
                                    )
                                }
                                Row(
                                    horizontalArrangement = Arrangement.Start,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    TypeFilterChip(
                                        text = "Camp",
                                        enabled = includeStaycation,
                                        selected = if (includeStaycation) campSelected else false,
                                        onSelectedChange = { selected ->
                                            homeViewModel.updateCampSelected(selected)
                                        }
                                    )
                                    Spacer(modifier = Modifier.width(5.dp))
                                    TypeFilterChip(
                                        text = "Guest House",
                                        enabled = includeStaycation,
                                        selected = if (includeStaycation) guestHouseSelected else false,
                                        onSelectedChange = { selected ->
                                            homeViewModel.updateGuestHouseSelected(selected)
                                        }
                                    )
                                    Spacer(modifier = Modifier.width(5.dp))
                                    TypeFilterChip(
                                        text = "Hotel",
                                        enabled = includeStaycation,
                                        selected = if (includeStaycation) hotelSelected else false,
                                        onSelectedChange = { selected ->
                                            homeViewModel.updateHotelSelected(selected)
                                        }
                                    )
                                }
                            }
                        }

                        item {
                            // ------------------------------------------------------
                            // Tour TYPE
                            Text(
                                text = "Type of Tour",
                                color = Color.Black,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                            )
                            Row(
                                horizontalArrangement = Arrangement.Start,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 5.dp, bottom = 5.dp)
                            ) {

                                TypeFilterChip(
                                    text = "Photo Tour",
                                    enabled = includeTour,
                                    selected = if (includeTour) photoTourSelected else false,
                                    onSelectedChange = { selected ->
                                        // photoTourSelected = selected
                                        homeViewModel.updatePhotoTourSelected(selected)
                                    }
                                )
                                Spacer(modifier = Modifier.width(5.dp))
                                TypeFilterChip(
                                    text = "Food Trip",
                                    enabled = includeTour,
                                    selected = if (includeTour) foodTripSelected else false,
                                    onSelectedChange = { selected ->
                                        homeViewModel.updateFoodTripSelected(selected)
                                    }
                                )
                                Spacer(modifier = Modifier.width(5.dp))
                                TypeFilterChip(
                                    text = "Bar Hopping",
                                    enabled = includeTour,
                                    selected = if (includeTour) barHoppingSelected else false,
                                    onSelectedChange = { selected ->
                                        homeViewModel.updateBarHoppingSelected(selected)
                                    }
                                )
                            }
                        }

                        item {
                            Text(
                                text = "Rating",
                                color = Color.Black,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                            )
                            Row(
                                horizontalArrangement = Arrangement.SpaceAround,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                val ratings = (5 downTo 1) // Create a range from 5 down to 1
                                ratings.forEach { rating ->
                                    RatingFilterChip(
                                        enabled = includeStaycation || includeTour,
                                        text = rating.toString(),
                                        selected = if(includeStaycation || includeTour) selectedRating == rating else false, // Set selected state based on a condition
                                        onSelectedChange = { selected ->
                                            if (selected) {
                                                homeViewModel.updateSelectedRating(rating)
                                            } else {
                                                homeViewModel.updateSelectedRating(0)
                                            }
                                        }
                                    )
                                }
                            }
                        }
                        item {
                            // ------------------------------------------------------
                            // PRICE RANGE
                            Text(
                                text = "Price range",
                                color = Color.Black,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Row(
                                horizontalArrangement = Arrangement.SpaceAround,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier.padding(vertical = 5.dp)
                                ) {
                                    PriceTextField(
                                        value = minPrice,
                                        enabled = includeStaycation || includeTour,
                                        placeholder = "Minimum",
                                        onValueChange = { updatedPrice ->
                                            homeViewModel.updateMinPrice(updatedPrice)
                                        }
                                    )
                                }
                                Column(
                                    modifier = Modifier.padding(vertical = 5.dp)
                                ) {
                                    PriceTextField(
                                        value = maxPrice,
                                        enabled = includeStaycation || includeTour,
                                        placeholder = "Maximum",
                                        onValueChange = { updatedPrice ->
                                            homeViewModel.updateMaxPrice(updatedPrice)
                                        }
                                    )
                                }
                            }

                        }

                        item {
                            // ------------------------------------------------------
                            // LOCATION + CAPACITY

                            Row(
                                horizontalArrangement = Arrangement.SpaceAround,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 5.dp)
                            ) {
                                Column {
                                    Text(
                                        text = "Location",
                                        color = Color.Black,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Medium,
                                        modifier = Modifier.offset(x = (-22).dp)
                                    )
                                    LocationTextField(
                                        initialValue = city,
                                        enabled = includeStaycation || includeTour,
                                        onValueChange = { newCity ->
                                            homeViewModel.updateCity(newCity)
                                        }
                                    )
                                }
                                Column {
                                    Text(
                                        text = "Capacity",
                                        color = Color.Black,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Medium,
                                        modifier = Modifier.offset(x = (-22).dp)
                                    )
                                    NumberTextField(
                                        initialValue = capacity,
                                        enabled = includeStaycation,
                                        onValueChange = { newCapacity ->
                                            homeViewModel.updateCapacity(newCapacity)
                                        }
                                    )
                                }
                            }
                            // ------------------------------------------------------
                            // BEDROOM AND BEDS

                            Text(
                                text = "Bedroom and beds",
                                color = Color.Black,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )

                            // BEDROOM
                            Text(
                                text = "Bedroom",
                                color = Color.Black,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier
                                    .padding(horizontal = 11.dp)
                            )
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                val bedroomCounts = listOf("Any", "1", "2", "3", "4")

                                bedroomCounts.forEachIndexed { index, bedroomCountOption ->
                                    CountFilterChip(
                                        text = bedroomCountOption,
                                        enabled = includeStaycation,
                                        selected = if (includeStaycation) bedroomCount == bedroomCountOption else false,
                                        onSelectedChange = { selected ->
                                            if (selected) {
//                                            bedroomCount = bedroomCountOption
                                                homeViewModel.updateBedroomCount(bedroomCountOption)
                                            }
                                        }
                                    )
                                }
                            }


                            // BEDS
                            Text(
                                text = "Beds",
                                color = Color.Black,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier
                                    .padding(horizontal = 11.dp)
                            )
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                val bedCounts = listOf("Any", "1", "2", "3", "4")

                                bedCounts.forEachIndexed { index, bedCountOption ->
                                    CountFilterChip(
                                        text = bedCountOption,
                                        enabled = includeStaycation,
                                        selected = if (includeStaycation) bedCount == bedCountOption else false,
                                        onSelectedChange = { selected ->
                                            if (selected) {
                                                //  bedCount = bedCountOption
                                                homeViewModel.updateBedCount(bedCountOption)
                                            }
                                        },
                                    )
                                }
                            }

                            // BATHROOM
                            Text(
                                text = "Bathroom",
                                color = Color.Black,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier
                                    .padding(horizontal = 11.dp)
                            )
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                val bathroomCounts = listOf("Any", "1", "2", "3", "4")

                                bathroomCounts.forEachIndexed { index, bathroomCountOption ->
                                    CountFilterChip(
                                        text = bathroomCountOption,
                                        enabled = includeStaycation,
                                        selected = if (includeStaycation) bathroomCount == bathroomCountOption else false,
                                        onSelectedChange = { selected ->
                                            if (selected) {
                                                //  bathroomCount = bathroomCountOption
                                                homeViewModel.updateBathroomCount(bathroomCountOption)
                                            }
                                        }
                                    )
                                }
                            }

                        }


                        item {
                            // ------------------------------------------------------
                            // AMENITIES
                            Text(
                                text = "Staycation Amenities",
                                color = Color.Black,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(2),
                                userScrollEnabled = false,
                                modifier = if (!showAllAmenities) {
                                    Modifier
                                        .fillMaxWidth()
                                        .height(110.dp)
                                } else {
                                    Modifier
                                        .fillMaxWidth()
                                        .height(130.dp)
                                }
                            ) {
                                items(visibleAmenities) { amenity ->
                                    AmenityCheckboxItem(
                                        amenityName = amenity,
                                        checked = if (includeStaycation) checkedAmenities[allAmenities.indexOf(amenity)] else false,
                                        onCheckedChange = { isChecked ->
                                            onAmenityCheckedChange(allAmenities.indexOf(amenity), isChecked)
                                        },
                                        enabled = includeStaycation,
                                        modifier = Modifier.height(25.dp)
                                    )
                                }
                                item {
                                    Text(
                                        text = if (showAllAmenities) "Show Less" else "Show More",
                                        color = Color.Black,
                                        fontSize = 12.sp,
                                        textDecoration = TextDecoration.Underline,
                                        modifier = Modifier
                                            .padding(horizontal = 15.dp)
                                            .padding(top = 5.dp)
                                            .clickable { toggleShowAllAmenities() }
                                    )
                                }
                            }
                        }
                        item {
                            // ------------------------------------------------------
                            // Tour OFFERS
                            Text(
                                text = "Tour Offers",
                                color = Color.Black,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(2),
                                userScrollEnabled = false,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(70.dp),
                            ) {
                                items(allOffers) { offer ->
                                    AmenityCheckboxItem(
                                        amenityName = offer,
                                        checked = if(includeTour) checkedOffers[allOffers.indexOf(offer)] else false,
                                        onCheckedChange = { isChecked ->
                                            onOfferCheckedChange(allOffers.indexOf(offer), isChecked)
                                        },
                                        enabled = includeTour,
                                        modifier = Modifier.height(25.dp)
                                    )
                                }
                            }
                        }

                        item {
                            Text(
                                text = "Choose Dates",
                                color = Color.Black,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )


                            Box(
                                modifier = Modifier
                                    .padding(vertical = 20.dp)
                                    .height(420.dp)
                                    .padding(4.dp)
                                    .border(1.dp, Color.Black, shape = MaterialTheme.shapes.medium)
                                    .background(Color(0xfff8c6a4).copy(alpha = 0.24f))
                            ) {
                                DateRangePicker(
                                    modifier = Modifier.fillMaxSize(),

                                    title = {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(20.dp),
                                            horizontalArrangement = Arrangement.Center,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            if (dateRangePickerState.selectedStartDateMillis != null && dateRangePickerState.selectedEndDateMillis != null) {
                                                Box(
                                                    modifier = Modifier
                                                        .size(24.dp)
                                                        .offset(x = 225.dp, y = (-15).dp),
                                                    contentAlignment = Alignment.CenterEnd
                                                ) {
                                                    IconButton(
                                                        onClick = {
                                                            dateRangePickerState.setSelection(
                                                                null,
                                                                null
                                                            )
                                                        },
                                                    ) {
                                                        Icon(
                                                            imageVector = Icons.Default.Close,
                                                            contentDescription = "Clear date selection",
                                                            tint = Color.Black
                                                        )
                                                    }
                                                }
                                            }



                                            Text(
                                                text = if (dateRangePickerState.selectedStartDateMillis == null && dateRangePickerState.selectedEndDateMillis == null) {
                                                    "Select date range"
                                                } else if(dateRangePickerState.selectedStartDateMillis != null && dateRangePickerState.selectedEndDateMillis == null) {
                                                    "${formatToPhilippineTime(dateRangePickerState.selectedStartDateMillis)}     -     ${formatToPhilippineTime(dateRangePickerState.selectedEndDateMillis)}"
                                                } else if(dateRangePickerState.selectedStartDateMillis != null && dateRangePickerState.selectedEndDateMillis != null) {
                                                    "${formatToPhilippineTime(dateRangePickerState.selectedStartDateMillis)}     -     ${formatToPhilippineTime(dateRangePickerState.selectedEndDateMillis)}"
                                                } else {
                                                    "Select date range"
                                                },
                                                color = Color.Black,
                                                fontSize = 16.sp,
                                                fontWeight = FontWeight.Medium
                                            )
                                        }
                                    },
                                    headline = null,
                                    showModeToggle = false,
                                    state = dateRangePickerState,
                                    colors = DatePickerDefaults.colors(
                                        containerColor = Orange,
                                        dayContentColor = Color.Black,
                                        selectedDayContainerColor = Orange,
                                        dayInSelectionRangeContainerColor = Orange.copy(alpha = 0.3f),
                                        disabledDayContentColor = Color.Black.copy(alpha = 0.3f),
                                        todayDateBorderColor = Orange,
                                        todayContentColor = Color.Black,
                                        weekdayContentColor = Color.Black,
                                    ),
                                    dateValidator = { date ->
                                        val selectedDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(date), ZoneId.systemDefault()).toLocalDate()
                                        val today = LocalDateTime.now().toLocalDate()

                                        selectedDate.isAfter(today)
                                    }

                                )
                            }



                        }


                    }
                    //Spacer(modifier = Modifier.weight(1f))

                    FilterBottomBar(
                        onSearch = {
                            //  onFilterServices = true
                            homeViewModel.refreshForYouPagingData()
                            homeViewModel.refreshSportsPagingData()
                            homeViewModel.refreshFoodTripPagingData()
                            homeViewModel.refreshShopPagingData()

                            homeViewModel.refreshNaturePagingData()
                            homeViewModel.refreshGamingPagingData()
                            homeViewModel.refreshKaraokePagingData()
                            homeViewModel.refreshHistoryPagingData()

                            homeViewModel.refreshClubsPagingData()
                            homeViewModel.refreshSightseeingPagingData()
                            homeViewModel.refreshSwimmingPagingData()

                            openModalBottomSheet = false
                        },
                        onRestore = {
                            homeViewModel.updateSearchText("")
                            homeViewModel.updateIncludeStaycation(true)
                            homeViewModel.updateIncludeTour(true)
                            homeViewModel.updateHouseSelected(true)
                            homeViewModel.updateApartmentSelected(true)
                            homeViewModel.updateCondoSelected(true)
                            homeViewModel.updateCampSelected(true)
                            homeViewModel.updateGuestHouseSelected(true)
                            homeViewModel.updateHotelSelected(true)
                            homeViewModel.updatePhotoTourSelected(true)
                            homeViewModel.updateFoodTripSelected(true)
                            homeViewModel.updateBarHoppingSelected(true)
                            homeViewModel.updateSelectedRating(0)
                            homeViewModel.updateMinPrice("")
                            homeViewModel.updateMaxPrice("")
                            homeViewModel.updateCity("")
                            homeViewModel.updateCapacity("")
                            homeViewModel.updateBedroomCount("Any")
                            homeViewModel.updateBedCount("Any")
                            homeViewModel.updateBathroomCount("Any")

                            homeViewModel.updateCheckedAmenities(List(allAmenities.size) { true })
                            homeViewModel.updateCheckedOffers(List(allOffers.size) { true })
                            dateRangePickerState.setSelection(null, null)
                        },
                        modifier = Modifier
                            .noPaddingIf(hasNavigationBar)
                    )


                    Spacer(modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars))
                }
            }

        }

    }

}

@Composable
fun FilterBottomBar(
    modifier: Modifier = Modifier,
    onSearch: () -> Unit,
    onRestore: () -> Unit,
) {
    Surface(
        color = Color.White,
        tonalElevation = 10.dp,
        shadowElevation = 10.dp,
        modifier = modifier
            .height(78.dp)
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(
                    horizontal = 25.dp,
                    //vertical = 25.dp
                ),
            verticalArrangement = Arrangement.Center
            //horizontalAlignment = Alignment.CenterHorizontally
        ){
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                ClickableText(
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                fontSize = 16.sp,
                                textDecoration = TextDecoration.Underline
                            )
                        ) {
                            append("Restore default")
                        }
                    },
                    onClick = {
                        onRestore()
                    },
                    modifier = Modifier.weight(1f)
                )

                BookingFilledButton(
                    buttonText = "Search",
                    onClick = {
                        onSearch()
                    },
                    enabled = true,
                    isLoading = false,
                    modifier = Modifier.width(90.dp)
                )
            }

        }

    }
}

//@Composable
//fun StaggeredGridListing(
//    staycationList: LazyPagingItems<Staycation>,
//    onItemClick: (String) -> Unit
//) {
//
//    val loadState = staycationList.loadState
//
//    Column(
//        Modifier.fillMaxWidth()
//    ) {
//        LazyVerticalStaggeredGrid(
//            columns = StaggeredGridCells.Fixed(2), // Number of columns
//            contentPadding = PaddingValues(16.dp),
//            verticalItemSpacing = 16.dp,
//            horizontalArrangement = Arrangement.spacedBy(16.dp)
//        ) {
//
//            items(staycationList.itemCount) { index ->
//
//                val staycation = staycationList[index]
//
//                if (staycation != null) {
//                    val cardHeight = (160..190).random()
//                    val imageHeight = cardHeight - 70
//
//                    // Step 2: Create a Composable for a single grid item
//                    StaycationListingCard(
//                        staycation = staycation,
//                        cardHeight = cardHeight.dp,
//                        imageHeight = imageHeight.dp,
//                        onItemClick = {
//                            onItemClick.invoke(staycation.staycationId)
//                        }
//                    )
//                }
//            }
//
//        }
//
//        if (loadState.refresh is LoadState.Loading) {
//
//            CircularProgressIndicator(
//                modifier = Modifier
//                    .size(50.dp)
//                    .padding(16.dp)
//                    .align(CenterHorizontally)
//            )
//
//        }
//    }
//
//
//}

@Composable
fun FavoriteButton(
    modifier: Modifier = Modifier,
    isFavorite: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {

   // var isFavorite by remember { mutableStateOf(false) }

    IconToggleButton(
        modifier = modifier,
        checked = isFavorite,
        onCheckedChange = {
            onCheckedChange(it)
          //  isFavorite = !isFavorite
        }
    ) {
        Image(
            imageVector = ImageVector.vectorResource(id = R.drawable.favorite_off),
            contentDescription = "",
            colorFilter = if(isFavorite) ColorFilter.tint(Color(220, 20, 60)) else null
        )

    }
}


@Composable
fun ServiceListingCard(
    service: HomePagingItem,
    homeViewModel: HomeViewModel,
    imageHeight: Dp,
    scope: CoroutineScope,
    touristId: String,
    onItemClick: (String) -> Unit
){

    val serviceImage = service.serviceCoverPhoto //?: "https://upload.wikimedia.org/wikipedia/commons/thumb/3/3f/Placeholder_view_vector.svg/1022px-Placeholder_view_vector.svg.png"

    var isFavorite by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(touristId) {
        isFavorite = homeViewModel.isFavorite(service.serviceId, touristId)
    }

  //  val is

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onItemClick(service.serviceType)
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )

    ) {
        Column {
            Box {
                AsyncImage(
                    model = serviceImage,
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(imageHeight)

                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 7.dp, end = 7.dp)
                ) {
                    FavoriteButton(
                        isFavorite = isFavorite,
                        onCheckedChange = { isChecked ->

                            scope.launch {
                                homeViewModel.toggleFavorite(
                                    service.serviceId,
                                    touristId,
                                    service.serviceType
                                )
                                isFavorite = isChecked
                            }

                        },
                        modifier = Modifier
                            .offset(y = (-9).dp)
                            .width(16.dp)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Tag(
                        tag = service.serviceType,
                        modifier = Modifier.padding(top = 3.dp)
                    )
                    
                }
            }
            Column(
                modifier = Modifier
                    .padding(top = 7.dp, bottom = 10.dp, start = 8.dp, end = 8.dp)
            ) {
                Row {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth(0.55f),
                        text = service.serviceId,
                        fontWeight = FontWeight.Medium,
                        fontSize = 11.sp,
                        lineHeight = 11.sp,
                        color = Color(0xFF333333)
                    )
                    Row(
                        modifier = Modifier.padding(top = 1.dp, start = 36.dp)
                    ) {
                        Icon(
                            modifier = Modifier.height(13.dp),
                            painter = painterResource(id = R.drawable.star),
                            contentDescription = "Star"
                        )
                        Text(
                            text = service.averageReviewRating.toString(), // PLACEHOLDER
                            fontWeight = FontWeight.Medium,
                            fontSize = 9.sp,
                            color = Color(0xFF333333)
                        )
                    }
                }
                Text(
                    text = service.location,
                    fontWeight = FontWeight.Medium,
                    fontSize = 9.sp,
                    color = Color(0xFF727272)
                )
                if (service.tourDuration != null ) {
                    Text(
                        text = "₱ ${"%.2f".format(service.price)}/head   •   ${service.tourDuration} hours",
                        fontWeight = FontWeight.Medium,
                        fontSize = 9.sp,
                        color = Color(0xFF727272)
                    )
                } else {
                    Text(
                        text = "₱ ${"%.2f".format(service.price)}/night",
                        fontWeight = FontWeight.Medium,
                        fontSize = 9.sp,
                        color = Color(0xFF727272)
                    )
                }

            }


        }
    }
}



//@Composable
//fun StaycationListingCard(
//    staycation: Staycation,
//    cardHeight: Dp,
//    imageHeight: Dp,
//    onItemClick: () -> Unit
//){
//
//    val staycationImage = staycation.staycationImages.find { it.photoType == "Cover" }?.photoUrl ?: "https://upload.wikimedia.org/wikipedia/commons/thumb/3/3f/Placeholder_view_vector.svg/1022px-Placeholder_view_vector.svg.png"
//
//    Card(
//        modifier = Modifier
//            //.height(165.dp)
//            .height(cardHeight)
//            .fillMaxWidth()
//            // .width(171.dp)
//            .clickable { onItemClick.invoke() },
//        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
//        colors = CardDefaults.cardColors(
//            containerColor = Color.White
//        )
//
//    ) {
//        Column {
//            Box {
//                AsyncImage(
//                    model = staycationImage,
//                    contentDescription = "",
//                    contentScale = ContentScale.Crop,
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(imageHeight)
//
//                )
////                Image(
////                    modifier = Modifier
////                        //.height(103.dp)
////                        .height(imageHeight)
////                        .fillMaxWidth(),
////                    painter = painterResource(id = R.drawable.image_placeholder),
////                    contentDescription = "Staycation Unit",
////                    contentScale = ContentScale.Crop
////                )
////                FavoriteButton(
////                    Modifier
////                        .offset(x = (-7).dp, y = (-9).dp)
////                        .width(14.dp)
////                )
//            }
//            Column(
//                modifier = Modifier
//                    .padding(vertical = 3.dp, horizontal = 8.dp)
//            ) {
//                Row {
//                    Text(
//                        modifier = Modifier
//                            .fillMaxWidth(0.55f),
//                        text = "Staycation hosted by " + staycation.host.firstName,
//                        fontWeight = FontWeight.Medium,
//                        fontSize = 11.sp,
//                        lineHeight = 11.sp,
//                        color = Color(0xFF333333)
//                    )
//                    Row(
//                        modifier = Modifier.padding(top = 1.dp, start = 36.dp)
//                    ) {
//                        Icon(
//                            modifier = Modifier.height(13.dp),
//                            painter = painterResource(id = R.drawable.star),
//                            contentDescription = "Star"
//                        )
//                        Text(
//                            text = staycation.averageReviewRating.toString(), // PLACEHOLDER
//                            fontWeight = FontWeight.Medium,
//                            fontSize = 9.sp,
//                            color = Color(0xFF333333)
//                        )
//                    }
//                }
//                Text(
//                    text = staycation.staycationLocation,
//                    fontWeight = FontWeight.Medium,
//                    fontSize = 9.sp,
//                    color = Color(0xFF727272)
//                )
//                Text(
//                    text = "₱ ${"%.2f".format(staycation.staycationPrice)}/night",
//                    fontWeight = FontWeight.Medium,
//                    fontSize = 9.sp,
//                    color = Color(0xFF727272)
//                )
//            }
//
//
//        }
//    }
//}

@Composable
fun SearchField(
    modifier: Modifier = Modifier,
    initialValue: String,
    onValueChange: (String) -> Unit,
    onClear: () -> Unit
) {

    val localFocusManager = LocalFocusManager.current

    TextField(
        value = initialValue,
        onValueChange = { newValue ->
            onValueChange(newValue)
        },
        singleLine = true,
        leadingIcon = {
            Icon(
                imageVector = Icons.TwoTone.Search,
                contentDescription = "Search",
                tint = Color(0xFF333333),
            )
        },
        placeholder = { Text(text = "Search") },
        keyboardActions = KeyboardActions {
            localFocusManager.clearFocus()
        },
        trailingIcon = {
            if (initialValue.isNotEmpty()) {
                IconButton(onClick = onClear) {
                    Icon(
                        imageVector = Icons.TwoTone.Close,
                        contentDescription = "Close",
                        tint = Color(0xFF333333),
                    )
                }
            }
        },
        shape = RoundedCornerShape(10.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color(0xFFDFDFDF),
            unfocusedContainerColor = Color(0xFFDFDFDF).copy(0.5f),
            disabledContainerColor = Color(0xFFDFDFDF).copy(0.2f),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        ),
        modifier = modifier
    )
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RatingFilterChip(
    modifier: Modifier = Modifier,
    enabled: Boolean,
    text: String,
    selected: Boolean,
    onSelectedChange: (Boolean) -> Unit
) {

    FilterChip(
        enabled = enabled,
        onClick = {
            onSelectedChange(!selected)
        },
        label = {
            if (text.toInt() < 5) {
                Text(text = "≥")
            }
            Text(text = text)
            Icon(
                modifier = Modifier.height(20.dp),
                painter = painterResource(id = R.drawable.star),
                contentDescription = "Star",
                tint = Orange
            )


            //            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .offset(x = (-3).dp),
//                horizontalArrangement = Arrangement.Center
//            ) {
//                Text(text = text)
//            }
        },
        colors = FilterChipDefaults.filterChipColors(
            containerColor = Color.White,
            selectedLabelColor = Orange,
            selectedLeadingIconColor = Color.White,
            selectedContainerColor = Color.White,
        ),
        selected = selected,
        shape = RoundedCornerShape(30.dp),
        border = FilterChipDefaults.filterChipBorder(
            borderColor = Color(0xffC2C2C2),
            selectedBorderColor = Orange,
            borderWidth = 1.dp,
            selectedBorderWidth = 1.5.dp
        ),
        modifier = modifier.width(60.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountFilterChip(
    modifier: Modifier = Modifier,
    enabled: Boolean,
    text: String,
    selected: Boolean,
    onSelectedChange: (Boolean) -> Unit
) {

    FilterChip(
        enabled = enabled,
        onClick = {
            onSelectedChange(!selected)
        },
        label = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(x = (-3).dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(text = text)
            }
        },
        colors = FilterChipDefaults.filterChipColors(
            containerColor = Color.White,
            selectedLabelColor = Orange,
            selectedLeadingIconColor = Color.White,
            selectedContainerColor = Color.White,

        ),
        selected = selected,
        shape = RoundedCornerShape(30.dp),
        border = FilterChipDefaults.filterChipBorder(
            borderColor = Color(0xffC2C2C2),
            selectedBorderColor = Orange,
            borderWidth = 1.dp,
            selectedBorderWidth = 1.5.dp
        ),
        modifier = modifier.width(60.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TypeFilterChip(
    text: String,
    selected: Boolean,
    enabled: Boolean,
    onSelectedChange: (Boolean) -> Unit
) {
//    var selected by remember { mutableStateOf(false) }

    FilterChip(
        enabled = enabled,
        onClick = {
           // selected = !selected
            onSelectedChange(!selected) // Call the lambda to return the selected state
        },
        label = {
            Text(text) // Use the provided text label
        },
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = Orange,
            selectedLabelColor = Color.White,
            selectedLeadingIconColor = Color.White
        ),
        selected = selected,
        leadingIcon = if (selected) {
            {
                Icon(
                    imageVector = Icons.Filled.Done,
                    contentDescription = "Done icon",
                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                )
            }
        } else {
            null
        },
    )
}

@Composable
fun PriceTextField(
    modifier: Modifier = Modifier,
    enabled: Boolean,
    value: String,
    placeholder: String,
    onValueChange: (String) -> Unit
) {

    var isFocused by remember { mutableStateOf(false) }
    val localFocusManager = LocalFocusManager.current


    BasicTextField(
        value = value,
        enabled = enabled,
        onValueChange = { newValue ->
            onValueChange(newValue.filter { it.isDigit() })
        },
        textStyle = TextStyle(
            fontWeight = FontWeight.Medium,
            color = Color.Black
        ),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Number
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                localFocusManager.clearFocus()
            }
        ),
        singleLine = true,
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier
                    .background(color = Color.White, shape = RoundedCornerShape(size = 10.dp))
                    .border(
                        width = 1.5.dp,
                        color = if (isFocused) {
                            Orange
                        } else if (enabled) {
                            Color.Black.copy(0.6f)
                        } else {
                            Color(0xFFC2C2C2)
                        },
                        shape = RoundedCornerShape(size = 10.dp)
                    )
                    .padding(all = 8.dp), // inner padding
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (value.isEmpty()) {
                    Text(
                        text = placeholder,
                        color = Color(0xFFC2C2C2),
                        fontWeight = FontWeight.Medium,
                    )
                } else if(value.isNotEmpty()) {
                    Text(
                        text = "₱ ",
                        fontWeight = FontWeight.Medium,
                    )
                }


                innerTextField()
            }
        },
        modifier = modifier
            .width(128.dp)
            .onFocusChanged { focusState ->
                isFocused = focusState.isFocused
            }
    )
}

@Composable
fun LocationTextField(
    modifier: Modifier = Modifier,
    initialValue: String,
    enabled: Boolean,
    onValueChange: (String) -> Unit
) {

    var isFocused by remember { mutableStateOf(false) }
    val localFocusManager = LocalFocusManager.current

    BasicTextField(
        value = initialValue,
        enabled = enabled,
        onValueChange = { newValue ->
            onValueChange(newValue)
        },
        textStyle = TextStyle(
            fontWeight = FontWeight.Medium,
            color = Color.Black
        ),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Text
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                localFocusManager.clearFocus()
            }
        ),
        singleLine = true,
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier
                    .background(color = Color.White, shape = RoundedCornerShape(size = 10.dp))
                    .border(
                        width = 1.5.dp,
                        color = if (isFocused) {
                            Orange
                        } else if (enabled) {
                            Color.Black.copy(0.6f)
                        } else {
                            Color(0xFFC2C2C2)
                        },
                        shape = RoundedCornerShape(size = 10.dp)
                    )
                    .padding(all = 8.dp), // inner padding
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (initialValue.isEmpty()) {
                    Text(
                        text = "City",
                        color = Color(0xFFC2C2C2),
                        fontWeight = FontWeight.Medium,
                    )
                }
                innerTextField()
            }
        },
        modifier = modifier
            .width(128.dp)
            .onFocusChanged { focusState ->
                isFocused = focusState.isFocused
            }
    )
}

@Composable
fun NumberTextField(
    modifier: Modifier = Modifier,
    initialValue: String,
    enabled: Boolean,
    onValueChange: (String) -> Unit
) {

    //var text by remember { mutableStateOf(initialValue) }
    var isFocused by remember { mutableStateOf(false) }
    val localFocusManager = LocalFocusManager.current

    BasicTextField(
        value = initialValue,
        enabled = enabled,
        onValueChange = { newValue ->

            onValueChange(newValue)
        },
        textStyle = TextStyle(
            fontWeight = FontWeight.Medium,
            color = Color.Black
        ),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Number
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                localFocusManager.clearFocus()
            }
        ),
        singleLine = true,
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier
                    .background(color = Color.White, shape = RoundedCornerShape(size = 10.dp))
                    .border(
                        width = 1.5.dp,
                        color = if (isFocused) {
                            Orange
                        } else if (enabled) {
                            Color.Black.copy(0.6f)
                        } else {
                            Color(0xFFC2C2C2)
                        },
                        shape = RoundedCornerShape(size = 10.dp)
                    )
                    .padding(all = 8.dp), // inner padding
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (initialValue.isNullOrEmpty()) {
                    Text(
                        text = "Person",
                        color = Color(0xFFC2C2C2),
                        fontWeight = FontWeight.Medium,
                    )
                }
                innerTextField()
            }
        },
        modifier = modifier
            .width(128.dp)
            .onFocusChanged { focusState ->
                isFocused = focusState.isFocused
            }
    )
}

@Composable
fun AmenityCheckboxItem(
    modifier: Modifier = Modifier,
    enabled: Boolean,
    amenityName: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .height(20.dp)
    ) {
        Checkbox(
            enabled = enabled,
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(
                checkedColor = Orange
            ),
        )
        Text(
            text = amenityName,
            color = if (enabled) Color(0xff666666) else Color(0xff666666).copy(0.5f),
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp,
        )
    }
}

fun formatToPhilippineTime(millis: Long?): String {
    return if (millis != null) {
        val dateTime = Instant.ofEpochMilli(millis).atZone(ZoneId.of("Asia/Manila")).toLocalDateTime()
        val formatter = DateTimeFormatter.ofPattern("MMM d")
        dateTime.format(formatter)
    } else {
        ""
    }
}



@Preview
@Composable
private fun HomeScreenPreview() {

    val homeViewModel = viewModel(modelClass = HomeViewModel::class.java)


    HomeScreen("mgPPHdYnYlJXMFxCaJOj", homeViewModel, { a,b -> }, { c,d ->}, rememberNavController())

}




