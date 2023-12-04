package com.example.tripnila.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.tripnila.common.Orange
import com.example.tripnila.R
import com.example.tripnila.common.TouristBottomNavigationBar
import com.example.tripnila.data.AttractionUiState
import com.example.tripnila.data.ItineraryUiState
import com.example.tripnila.model.ItineraryViewModel
import java.time.LocalDate
import java.time.ZoneId
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItineraryScreen(
    touristId: String = "",
    navController: NavHostController? = null,
    itineraryViewModel: ItineraryViewModel,
  //  onNavToPopulatedItinerary: (String) -> Unit
){

    var selectedItemIndex by rememberSaveable { mutableIntStateOf(1) }
    var isDialogOpen by remember { mutableStateOf(false) }
    val selectedStaycationBooking = itineraryViewModel.selectedStaycationBooking.collectAsState().value
    val tours = itineraryViewModel.tours.collectAsState().value
    val businesses = itineraryViewModel.businesses.collectAsState().value

    var selectedTabIndex by remember { mutableIntStateOf(0) }

    var selectedItems by remember { mutableStateOf(emptyList<ItineraryUiState>()) }

    val tabs = listOf("Sports", "Food Trip", "Shop", "Nature", "Gaming", "Karaoke", "History", "Clubs", "Sightseeing", "Swimming")

    var openBottomSheet by remember { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    val horizontalPaddingValue = 16.dp
    val verticalPaddingValue = 10.dp

    val backgroundColor = Color.White

    val attractionUiStates = listOf(
        AttractionUiState(
            image = R.drawable.map_image,
            name = "Rainforest Park",
            tag = listOf("Nature"),
            distance = 500,
            price = 1000.00,
            openingTime = "7:30 am",
            itineraryTime = "7:30 am"
        ),
        AttractionUiState(
            image = R.drawable.map_image,
            name = "Pasig Museum",
            tag = listOf("History"),
            distance = 2700,
            price = 1000.00,
            openingTime = "9:00 am",
            itineraryTime = "10:00 am"
        ),
    )

    val businessItinerary: List<ItineraryUiState> = businesses.map { business ->
        ItineraryUiState(
            image = business.businessImages.find { it.photoType == "Cover" }?.photoUrl ?: "https://upload.wikimedia.org/wikipedia/commons/thumb/3/3f/Placeholder_view_vector.svg/1022px-Placeholder_view_vector.svg.png",
            rating = 0.0,
            price = 0,
            itineraryType = business.businessType,
            title = business.businessTitle,
            location = business.businessLocation
        )
    }

    val tourItinerary: List<ItineraryUiState> = tours.map { tour ->
        ItineraryUiState(
            image = tour.tourImages.find { it.photoType == "Cover" }?.photoUrl ?: "https://upload.wikimedia.org/wikipedia/commons/thumb/3/3f/Placeholder_view_vector.svg/1022px-Placeholder_view_vector.svg.png",
            rating = 0.0,
            price = 0,
            itineraryType = tour.tourType,
            title = tour.tourTitle,
            location = tour.tourDuration
        )
    }


//    val businessItinerary = listOf(
//        ItineraryUiState(
//            image = "https://upload.wikimedia.org/wikipedia/commons/thumb/3/3f/Placeholder_view_vector.svg/1022px-Placeholder_view_vector.svg.png",
//            rating = 4.0,
//            price = 2000,
//            itineraryType = "Restaurant",
//            title = "Le's Bar & Grill",
//            location = "2.1 km"
//
//        ),
//        ItineraryUiState(
//            image = "https://upload.wikimedia.org/wikipedia/commons/thumb/3/3f/Placeholder_view_vector.svg/1022px-Placeholder_view_vector.svg.png",
//            rating = 4.0,
//            price = 2000,
//            itineraryType = "Restaurant",
//            title = "Lo's Bar & Grill",
//            location = "2.1 km"
//
//        ),
//        ItineraryUiState(
//            image = "https://upload.wikimedia.org/wikipedia/commons/thumb/3/3f/Placeholder_view_vector.svg/1022px-Placeholder_view_vector.svg.png",
//            rating = 4.0,
//            price = 2000,
//            itineraryType = "Restaurant",
//            title = "Leo' Bar & Grill",
//            location = "2.1 km"
//
//        ),
//        ItineraryUiState(
//            image = "https://upload.wikimedia.org/wikipedia/commons/thumb/3/3f/Placeholder_view_vector.svg/1022px-Placeholder_view_vector.svg.png",
//            rating = 4.0,
//            price = 2000,
//            itineraryType = "Restaurant",
//            title = "eo's Bar & Grill",
//            location = "2.1 km"
//
//        ),
//        ItineraryUiState(
//            image = "https://upload.wikimedia.org/wikipedia/commons/thumb/3/3f/Placeholder_view_vector.svg/1022px-Placeholder_view_vector.svg.png",
//            rating = 4.0,
//            price = 2000,
//            itineraryType = "Restaurant",
//            title = "Leo's Bar & rill",
//            location = "2.1 km"
//
//        )
//    )


    LaunchedEffect(touristId) {
        if (touristId != "") {
            itineraryViewModel.getBookedStaycation(touristId)
            itineraryViewModel.getItinerarySuggestions()
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ){
        Scaffold(
            bottomBar = {
                navController?.let {
                    TouristBottomNavigationBar(
                        touristId = touristId,
                        navController = it,
                        selectedItemIndex = selectedItemIndex,
                        onItemSelected = { newIndex ->
                            selectedItemIndex = newIndex
                        }
                    )
                }
            },
            floatingActionButton = {

                if (selectedStaycationBooking.staycationBookingId != "") {
                    FloatingActionButton(
                        onClick = {
                            openBottomSheet = true
                        },
                        shape = CircleShape,
                        containerColor = Orange,
                        contentColor = Color.White,
                        elevation = FloatingActionButtonDefaults.elevation(
                            defaultElevation = 10.dp
                        ),
                        modifier = Modifier
                            // .padding(start = 10.dp)
                            .size(50.dp)


                    ) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Send",
                            modifier = Modifier.size(30.dp),
                        )
                    }
                }


            }
        ){
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                item {
                    AppTopBar(
                        headerText = "Itinerary Planner",
                        color = Color.White
                    )
                }
                item {

                    if (selectedStaycationBooking.staycationBookingId == "") {
                        ChooseStaycationButton(
                            modifier = Modifier
                                .padding(
                                    horizontal = horizontalPaddingValue,
                                    vertical = verticalPaddingValue
                                )
                                .clickable { isDialogOpen = true }
                        )
                    } else {
                        SelectedStaycationCard(
                            staycation = selectedStaycationBooking.staycation,
                            onChange = {
                             //   itineraryViewModel.clearSelectedStaycation()
                                isDialogOpen = true
                            }
                        )
                    }

                }
                item {
                    BudgetCard(
                        modifier = Modifier
                            .padding(vertical = verticalPaddingValue)
                    )
                }
//                item {
//                    ActivitiesCard(
//                        modifier = Modifier
//                            .padding(vertical = verticalPaddingValue)
//                    )
//                }
                item {
                    YourTripCard(
                        attractions = if (selectedStaycationBooking.staycationBookingId == "") null else attractionUiStates,
                        modifier = Modifier
                            .padding(
                                horizontal = horizontalPaddingValue,
                                vertical = verticalPaddingValue
                            )
                    )
                }
            }
        }
        if (openBottomSheet) {
            ModalBottomSheet(
                modifier = Modifier.fillMaxHeight(.88f),
                onDismissRequest = {
                    openBottomSheet = false
                },
                sheetState = bottomSheetState,
                containerColor = Color.White,
                dragHandle = {

                }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = horizontalPaddingValue)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = verticalPaddingValue),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Text(
                            text = "Add on trip",
                            style = TextStyle(
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium
                            ),
                        )
                    }
                    Card(
                        border = BorderStroke(2.dp, Color(0xff666666).copy(alpha = 0.4f)),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    ) {

                    }

                    Text(
                        text = "Budget",
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium),
                        modifier = Modifier.padding(vertical = verticalPaddingValue),

                        )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        BudgetTextField()
                        Divider(
                            Modifier
                                .width(40.dp)
                                .offset(y = 15.dp))
                        BudgetTextField()
                    }
                    Box(modifier = Modifier.offset()) {
                        Text(
                            text = "Min",
                            color = Color(0xffc2c2c2),
                            style = TextStyle(
                                fontSize = 7.sp,
                                fontWeight = FontWeight.Medium),
                            modifier = Modifier.offset(x = 28.dp, y = (-32).dp)
                        )
                        Text(
                            text = "Max",
                            color = Color(0xffc2c2c2),
                            style = TextStyle(
                                fontSize = 7.sp,
                                fontWeight = FontWeight.Medium),
                            modifier = Modifier.offset(x = 228.dp, y = (-32).dp)
                        )
                    }
                    Text(
                        text = "Activities",
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium),
                        modifier = Modifier.padding(vertical = verticalPaddingValue),

                        )
                    ScrollableTabRow(
                        selectedTabIndex = selectedTabIndex,
                        containerColor = Color.White,
                        edgePadding = 5.dp,
                        divider = {},
                        indicator = {},
                        modifier = Modifier
                            .padding(vertical = 5.dp)
                            .padding(start = 10.dp)
                            .fillMaxWidth(),
                    ) {
                        tabs.forEachIndexed { index, tabLabel ->
                            DayTab(
                                tabLabel = tabLabel,
                                isSelected = index == selectedTabIndex,
                                onTabSelected = { selectedTabIndex = index },
                            )
                        }
                    }
                    LazyRow {
                        items(businessItinerary) { itineraryItem ->
                            ItineraryCard(
                                modifier = Modifier.padding(horizontal = 8.dp),
                                image = itineraryItem.image,
                                rating = itineraryItem.rating,
                                price = itineraryItem.price,
                                itineraryType = itineraryItem.itineraryType,
                                title = itineraryItem.title,
                                location = itineraryItem.location,
                                onSelect = {
                                    selectedItems = if (it) {
                                        selectedItems + itineraryItem
                                    } else {
                                        selectedItems - itineraryItem
                                    }
                                },
                                isSelected = itineraryItem in selectedItems
                            )
                        }
                    }
                    Text(
                        text = "Tours",
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium),
                        modifier = Modifier.padding(vertical = verticalPaddingValue),
                    )
                    LazyRow {
                        items(tourItinerary) { itineraryItem ->
                            ItineraryCard(
                                modifier = Modifier.padding(horizontal = 8.dp),
                                image = itineraryItem.image,
                                rating = itineraryItem.rating,
                                price = itineraryItem.price,
                                itineraryType = itineraryItem.itineraryType,
                                title = itineraryItem.title,
                                location = itineraryItem.location,
                                onSelect = {
                                    selectedItems = if (it) {
                                        selectedItems + itineraryItem
                                    } else {
                                        selectedItems - itineraryItem
                                    }
                                },
                                isSelected = itineraryItem in selectedItems
                            )
                        }
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = horizontalPaddingValue,
                                vertical = verticalPaddingValue
                            )
                    ) {
                        BookingOutlinedButton(
                            buttonText = "Cancel",
                            onClick = { /*TODO*/ },
                            modifier = Modifier.width(120.dp)
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        BookingFilledButton(
                            buttonText = "Add",
                            onClick = { /*TODO*/ },
                            modifier = Modifier.width(120.dp)
                        )
                    }


                }


            }
        }


    }

    ChooseStaycationDialog(
        itineraryViewModel = itineraryViewModel,
        backgroundColor = backgroundColor,
        isDialogOpen = isDialogOpen,
        onDismissRequest = {
            isDialogOpen = false
        },
        onConfirm = {
            isDialogOpen = false
         //   onNavToPopulatedItinerary(touristId)
        }
    )
}


@Composable
fun ChooseStaycationButton(modifier: Modifier = Modifier) {

    val stroke = Stroke(width = 10f,
        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
    )

    Card(
        colors = CardDefaults.outlinedCardColors(
            containerColor = Color.Transparent
        ),
        shape = RoundedCornerShape(20.dp),
        modifier = modifier
            .fillMaxWidth()
            .height(height = 90.dp)
            .drawBehind {
                drawRoundRect(
                    color = Color(0xff7d7d7d),
                    style = stroke,
                    cornerRadius = CornerRadius(20.dp.toPx())
                )
            }
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = "Choose Staycation",
                color = Color(0xff7d7d7d),
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun BudgetCard(
    modifier: Modifier = Modifier
){

    var checkedState by remember {
        mutableStateOf(false)
    }

    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp
        ),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 25.dp,
                    end = 25.dp,
                    top = 15.dp
                ),
        ) {
            Text(
                text = "Budget",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(top = 6.dp)
            )
            BudgetTextField()
            Checkbox(
                checked = checkedState,
                onCheckedChange = { checkedState = it },
                colors = CheckboxDefaults.colors(
                    checkedColor = Orange
                ),
                modifier = Modifier.offset(y = (-5).dp)
            )
            Text(
                text = "No entrance fee",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(top = 6.dp)
            )
        }

    }
}

@Composable
fun ActivitiesCard(modifier: Modifier = Modifier){
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp
        ),
        shape = RoundedCornerShape(20.dp),
        modifier = modifier.height(200.dp)
    ){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 25.dp,
                    vertical = 15.dp
                ),
        ) {
            Text(
                text = "Activities",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                //modifier = Modifier.padding(top = 6.dp)
            )
            ActivitiesTabRow()

        }
    }
}

@Composable
fun YourTripCard(
    modifier: Modifier = Modifier,
    attractions: List <AttractionUiState>? = null,
    onClick: (() -> Unit)? = null,

){

    var selectedTabIndex by remember { mutableIntStateOf(0) }
    var selectedItems by remember { mutableStateOf(emptyList<AttractionUiState>()) }
    val tabs = listOf("Day 1", "Day 2", "Day 3")

    Column {
        Text(
            text = "Your trip",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            modifier = modifier
                .padding(horizontal = 9.dp)


        )

        if (attractions.isNullOrEmpty()) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {

                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.no_activities),
                        contentDescription = "No activities",
                        tint = Orange
                    )
                    Text(
                        text = "No activities yet",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF999999),
                        modifier = modifier
                            .padding(horizontal = 9.dp)

                    )

            }
        } else {

            Row(
                modifier = Modifier
                    .padding(vertical = 5.dp)
                    .padding(start = 10.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                tabs.forEachIndexed { index, tabLabel ->
                    DayTab(
                        tabLabel = tabLabel,
                        isSelected = index == selectedTabIndex,
                        onTabSelected = {
                            //FILTER BY TAB
                            selectedTabIndex = index
                        },
                    )
                }
                FloatingActionButton(
                    onClick = {
                        if (onClick != null) {
                            onClick()
                        }
                    },
                    shape = CircleShape,
                    containerColor = Orange,
                    contentColor = Color.White,
                    elevation = FloatingActionButtonDefaults.elevation(
                        defaultElevation = 10.dp
                    ),
                    modifier = Modifier
                        // .padding(start = 10.dp)
                        .size(30.dp)
                        .offset(y = (-5).dp)

                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Send",
                        modifier = Modifier.size(18.dp),
                    )
                }
            }
            Card(
                border = BorderStroke(2.dp, Color(0xff666666).copy(alpha = 0.4f)),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .padding(horizontal = 9.dp)
                    .padding(bottom = 10.dp)
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                // MAP PLACEHOLDER
            }


            attractions.forEach { attraction ->
                AttractionCard(
                    attraction = attraction,
                    onSelectedChange = {
                        selectedItems = if (it) {
                            selectedItems + attraction
                        } else {
                            selectedItems - attraction
                        }

                    },
                    selected = attraction in selectedItems,
                    modifier = Modifier
                        .padding(bottom = 10.dp)
                        .padding(horizontal = 9.dp),
                )
            }
        }


    }
}


@Composable
fun DayTab(
    tabLabel: String,
    isSelected: Boolean,
    onTabSelected: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onTabSelected,
        border = BorderStroke(width = 1.dp, if (isSelected) Orange else Color.White),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) Orange else Color.White
        ),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
        modifier = modifier.height(22.dp)
    ) {
        Text(
            text = tabLabel,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = if (isSelected) Color.White else Color.Black,
        )
    }
}




@Composable
fun ChooseStaycationDialog(
    modifier: Modifier = Modifier,
    backgroundColor: Color,
    isDialogOpen: Boolean,
    itineraryViewModel: ItineraryViewModel,
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit
) {

   // var selectedCard by remember { mutableStateOf(-1) }

    var selectedCard by remember { mutableStateOf("") }
    val staycationBookings = itineraryViewModel.staycationBookings.collectAsState().value

    if (isDialogOpen) {
        Dialog(
            onDismissRequest = onDismissRequest,
            properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
        ) {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = backgroundColor
                ),
                modifier = modifier
                    //.height(500.dp)
                    .width(300.dp),

            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 8.dp, // 20
                            vertical = 15.dp
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Choose Staycation",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                            .align(Alignment.Start)
                            .padding(start = 10.dp, end = 10.dp, bottom = 15.dp)
                    )
                    staycationBookings.forEach { booking ->
                        val startLocalDate = booking.checkInDate
                            ?.toInstant()
                            ?.atZone(ZoneId.systemDefault())
                            ?.toLocalDate()

                        val endLocalDate = booking.checkOutDate
                            ?.toInstant()
                            ?.atZone(ZoneId.systemDefault())
                            ?.toLocalDate()

                        val bookingId = booking.staycationBookingId

                        ChooseStaycationCard(
                            staycationName = booking.staycation?.staycationTitle ?: "Greenhills Staycation",
                            staycationStatus = booking.bookingStatus,
                            staycationDate = formatDateRange(startLocalDate ?: LocalDate.now(), endLocalDate ?: LocalDate.now()),
                            bookingId = bookingId,
                            isSelected = selectedCard == booking.staycationBookingId,
                            onSelectedChange = {
                                selectedCard = it
                            },
                            modifier = Modifier.padding(vertical = 3.dp)
                        )
                    }
                    Row(
                        modifier = Modifier.padding(top = 15.dp)
                    ) {
                        BookingOutlinedButton(
                            buttonText = "Cancel",
                            onClick = {
                                selectedCard = ""
                                onDismissRequest()
                            }
                        )
                        Spacer(modifier = Modifier.width(40.dp))
                        BookingFilledButton(
                            buttonText = "Confirm",
                            onClick = {
                                if (selectedCard.isNotBlank()) {
                                    itineraryViewModel.setSelectedStaycation(selectedCard)
                                    onConfirm()
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

private fun formatDateRange(startDate: LocalDate, endDate: LocalDate): String {
    val startDay = startDate.dayOfMonth
    val endDay = endDate.dayOfMonth
    val startMonth = startDate.month.getDisplayName(java.time.format.TextStyle.SHORT, Locale.ENGLISH)
    val endMonth = endDate.month.getDisplayName(java.time.format.TextStyle.SHORT, Locale.ENGLISH)

    return if (startDate.month == endDate.month) {
        "$startDay-$endDay $startMonth"
    } else {
        "$startDay $startMonth-$endDay $endMonth"
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChooseStaycationCard(

    staycationName: String,
    staycationStatus: String,
    staycationDate: String,
    bookingId: String,
    isSelected: Boolean,
    onSelectedChange: (String) -> Unit,
    modifier: Modifier = Modifier,
){

   // val staycationBookingId = bookingId

    Card(
        onClick = {
            onSelectedChange(bookingId)
        },
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(20),
        border = if(isSelected) BorderStroke(2.dp, Color(0xfff9a664)) else null,
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp,
           // pressedElevation = 2.dp
        ),
        modifier = modifier
            .fillMaxWidth()

    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 10.dp)
        ) {
            Row {
                Text(
                    text = staycationName,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = staycationStatus,
                    color = Color.White,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(Orange)
                        .padding(vertical = 2.dp, horizontal = 4.dp)

                )
            }
            Text(
                text = staycationDate,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF999999)
            )
        }
    }
}

@Composable
fun ActivitiesTabRow(){

    val preferences = listOf("Sports", "Food Trip", "Shop", "Nature", "Gaming", "Karaoke", "History", "Clubs", "Sightseeing", "Swimming")
    val selectedTab = remember { mutableIntStateOf(0) }

    ScrollableTabRow(
        selectedTabIndex = selectedTab.intValue,
        //contentColor = Color.Gray,
        containerColor = Color.White,
        edgePadding = 3.dp,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                color = Orange,
                modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab.intValue])
            )
        },
        divider = { Divider(color = Color.Transparent) }


    ) {
        preferences.forEachIndexed { index, text ->
            val isSelected = selectedTab.intValue == index
            Tab(
                selected = isSelected,
                onClick = {
                    selectedTab.intValue = index
                },
                modifier = Modifier.padding(horizontal = 3.dp)
            ) {
                Text(
                    text = text,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (isSelected) Orange else Color.Gray,
                    modifier = Modifier.padding(top = 8.dp, bottom = 5.dp, start = 3.dp, end = 3.dp),
                )

            }
        }
    }

}

@Composable
fun BudgetTextField(modifier: Modifier = Modifier) {
    var text by remember {
        mutableStateOf("")
    }

    var isFocused by remember { mutableStateOf(false) }

    val localFocusManager = LocalFocusManager.current

    BasicTextField(
        value = text,
        onValueChange = {
            text = it
        },
        textStyle = TextStyle(
            //fontSize = 12.sp,
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
                    .padding(horizontal = 5.dp) // margin left and right
                    .background(color = Color.White, shape = RoundedCornerShape(size = 10.dp))
                    .border(
                        width = 2.dp,
                        color = if (isFocused) Orange else Color(0xFFC2C2C2),
                        shape = RoundedCornerShape(size = 10.dp)
                    )
                    .padding(all = 8.dp), // inner padding
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (text.isEmpty()) {
                    Text(
                        text = "₱ ",
                        //fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFFC2C2C2),

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


//@Preview
//@Composable
//private fun Group24Preview() {
//    ActivitiesCard()
//}

@Preview
@Composable
fun ItineraryScreenPreview(){

    val itineraryViewModel = viewModel(modelClass = ItineraryViewModel::class.java)
    val touristId = "ITZbCFfF7Fzqf1qPBiwx"

    LaunchedEffect(touristId) {
        if (touristId != "") {
            itineraryViewModel.getBookedStaycation(touristId)
        }
    }

    ItineraryScreen(
        touristId = touristId,
        itineraryViewModel = itineraryViewModel

    )


}