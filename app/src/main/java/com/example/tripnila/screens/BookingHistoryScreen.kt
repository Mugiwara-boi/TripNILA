package com.example.tripnila.screens

import android.util.Log
import android.view.MotionEvent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import coil.compose.AsyncImage
import com.example.tripnila.R
import com.example.tripnila.common.AppConfirmAndPayDivider
import com.example.tripnila.common.AppDropDownFilter
import com.example.tripnila.common.AppYourTripRow
import com.example.tripnila.common.Orange
import com.example.tripnila.common.Tag
import com.example.tripnila.common.TouristBottomNavigationBar
import com.example.tripnila.data.BookingHistory
import com.example.tripnila.data.StaycationBooking
import com.example.tripnila.data.TourBooking
import com.example.tripnila.model.BookingHistoryViewModel
import com.example.tripnila.model.TouristWalletViewModel
import com.google.firebase.Timestamp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.time.temporal.ChronoUnit
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BookingHistoryScreen(
    touristId: String = "",
    touristWalletViewModel: TouristWalletViewModel,
    bookingHistoryViewModel: BookingHistoryViewModel,
    navController: NavHostController? = null,
    onNavToChat: (String, String) -> Unit,
    onBack: () -> Unit,
){

    var selectedItemIndex by rememberSaveable { mutableIntStateOf(3) }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    // val staycationBookingList = bookingHistoryViewModel?.staycationBookingList?.collectAsState()?.value
    val addReviewResult = bookingHistoryViewModel.addReviewResult.collectAsState().value
    val isCancelBookingSuccessful = bookingHistoryViewModel.isSuccessCancelBooking.collectAsState().value
    val selectedTab by bookingHistoryViewModel.selectedTab.collectAsState()
    val logCheckOutDatesResult by bookingHistoryViewModel.logCheckOutDatesResult.collectAsState()




    val staycationBookingFlow = remember { bookingHistoryViewModel.getStaycationBookingsForTourist(touristId) }
    val staycationBookingItems = if (logCheckOutDatesResult != "") staycationBookingFlow.collectAsLazyPagingItems() else null

    val tourBookingFlow = remember { bookingHistoryViewModel.getTourBookingsForTourist(touristId) }
    val tourBookingItems = if (logCheckOutDatesResult != "") tourBookingFlow.collectAsLazyPagingItems() else null

    val tabs = listOf("Staycation", "Tour")

   // var tempCounter by remember { mutableIntStateOf(0) }

    val pagerState = rememberPagerState(
        initialPage = tabs.indexOf(selectedTab),
        initialPageOffsetFraction = 0f
    ) {
        tabs.size
    }

    LaunchedEffect(touristId) {
        bookingHistoryViewModel.updateStaycationBookingsStatus(touristId)

        val currentTimestamp = Timestamp.now()
        val milliseconds = currentTimestamp.seconds * 1000
        Log.d("Current Timestamp", "$milliseconds")
    }

    LaunchedEffect(addReviewResult) {
        if (addReviewResult != null) {
            snackbarHostState.showSnackbar(addReviewResult)
            bookingHistoryViewModel.clearAddReviewResult()
        }
    }

    LaunchedEffect(isCancelBookingSuccessful) {
        if (isCancelBookingSuccessful != null) {
            if (isCancelBookingSuccessful == true) {
                staycationBookingItems?.refresh()
                tourBookingItems?.refresh()
                delay(300)
                snackbarHostState.showSnackbar("Booking Cancelled")

            } else {
                snackbarHostState.showSnackbar("Cancellation Failed")
            }

            bookingHistoryViewModel.clearSuccessCancelBooking()
        }
    }
//
//    LaunchedEffect(tempCounter) {
//        if (tempCounter > 0) {
//            // TEST REFRESH
//            staycationBookingItems.refresh()
//            tourBookingItems.refresh()
//
//            Log.d("tempCounter", tempCounter.toString())
//            //------------------
//        }
//    }


    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Scaffold(
            topBar = {
                Surface(
                    shadowElevation = 10.dp
                ) {
                    BookingHistoryTopBar(
                        onBack = {
                            onBack()
                        }
                    )
                }

            },
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
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        ) {

            Column(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize()
            ) {
                TabRow(
                    modifier = Modifier.fillMaxWidth(),
                    containerColor = Color.White,
                    selectedTabIndex = tabs.indexOf(selectedTab),
               //     edgePadding = 3.dp,
                    indicator = { tabPositions ->
                        TabRowDefaults.Indicator(
                            color = Orange,
                            modifier = Modifier.tabIndicatorOffset(
                                tabPositions[tabs.indexOf(
                                    selectedTab
                                )]
                            )
                        )
                    },
                    divider = { Divider(color = Color.Transparent) }
                ) {
                    tabs.forEach { tabName ->
                        val isSelected = selectedTab == tabName
                        Tab(
                            selected = isSelected,
                            onClick = {
                                bookingHistoryViewModel.selectTab(tabName)
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(tabs.indexOf(tabName))
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
                    when (selectedTab) {
                        "Staycation" -> {
                            if (staycationBookingItems != null) {
                                StaycationPage(
                                    staycationBookingItems = staycationBookingItems,
                                    touristId = touristId,
                                    bookingHistoryViewModel = bookingHistoryViewModel,
                                    touristWalletViewModel = touristWalletViewModel,
                                    coroutineScope = coroutineScope,
                                    onNavToChat = { receiverId ->
                                        onNavToChat(touristId, receiverId)
                                    },
                        //                                onTryTempCounter = {
                        //                                    tempCounter += 1
                        //                                }
                                )
                            }
                        }
                        "Tour" -> {
                            if (tourBookingItems != null) {
                                TourPage(
                                    tourBookingItems = tourBookingItems,
                                    touristId = touristId,
                                    bookingHistoryViewModel = bookingHistoryViewModel,
                                    touristWalletViewModel = touristWalletViewModel,
                                    coroutineScope = coroutineScope,
                                    onNavToChat = { receiverId ->
                                        onNavToChat(touristId, receiverId)
                                    },
                        //                                onTryTempCounter = {
                        //                                    tempCounter += 1
                        //                                }
                                )
                            }
                        }

                    }
                }
            }
        }
    }
}

@Composable
fun TourPage(
    tourBookingItems: LazyPagingItems<TourBooking>,
    touristId: String,
    bookingHistoryViewModel: BookingHistoryViewModel,
    touristWalletViewModel: TouristWalletViewModel,
    coroutineScope: CoroutineScope,

   // onTryTempCounter: () -> Unit,

    onNavToChat: (String) -> Unit
) {

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn (
            modifier = Modifier.fillMaxSize()
        ) {

            if (tourBookingItems.loadState.refresh == LoadState.Loading) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
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

            items(tourBookingItems) { tourBooking ->
                tourBooking?.let {

                    val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())

                    val formatter = DateTimeFormatter.ofPattern("d MMM, EEEE", Locale.ENGLISH)
                    val parsedTourDate = LocalDate.parse(tourBooking.tourDate)
                    val formattedDate = parsedTourDate.format(formatter)


                    val bookingHistory = BookingHistory(
                        bookingId = tourBooking.tourBookingId,
                        //   ownerImage = staycationBooking.staycation?.hostImage
                        ownerImage = tourBooking.tour.host.profilePicture
                            ?: "https://upload.wikimedia.org/wikipedia/commons/8/89/Portrait_Placeholder.png",
                        bookedRental = tourBooking.tour.tourTitle,
                        date = dateFormat.format(tourBooking.bookingDate),
                        rentalImage = tourBooking.tour.tourImages.find { it.photoType == "Cover" }?.photoUrl ?: "https://www.unfe.org/wp-content/uploads/2019/04/SM-placeholder.png",//,
                        rentalLocation = tourBooking.tour.tourLocation,
                        bookedDates = formattedDate,
                        guestsNo = tourBooking.noOfGuests,
                        totalAmount = tourBooking.totalAmount,
                        rentalStatus = tourBooking.bookingStatus,
                        isReviewed = tourBooking.bookingReview != null,
                        bookingDuration = tourBooking.noOfGuests,
                        basePrice = tourBooking.tour.tourPrice,
                        checkInDate = Date(0),
                        checkOutDate = Date(0),
                        startTime = tourBooking.startTime,
                        endTime = tourBooking.endTime,
                        noOfPets = 0,
                        noOfInfants = 0,
                        noOfGuests = tourBooking.noOfGuests,
                        hostTouristId = tourBooking.tour.host.touristId,
                        staycationId = "",
                        tourId = tourBooking.tour.tourId,
                        tourAvailabilityId = tourBooking.tourAvailabilityId,
                        tourDate = tourBooking.tourDate
                    )

                    BookingHistoryCard(
                        touristId = touristId,
                        bookingHistoryViewModel = bookingHistoryViewModel,
                        touristWalletViewModel = touristWalletViewModel,
                        bookingHistory = bookingHistory,
                        coroutineScope = coroutineScope,
                        modifier = Modifier.padding(top = 15.dp),
                        tourBookingItems = tourBookingItems,
                        onChatHost = { receiverId ->
                            onNavToChat(receiverId)
                        },
//                        onTryTempCounter = {
//
//                        }
                    )
                }

            }

        }
//        if (tourBookingItems.loadState.refresh is LoadState.Loading) {
//            CircularProgressIndicator(
//                color = Orange,
//                modifier = Modifier
//                    .size(50.dp)
//                    .padding(16.dp)
//                    .align(Alignment.CenterHorizontally)
//            )
//        }
    }
}



@Composable
fun StaycationPage(
    staycationBookingItems: LazyPagingItems<StaycationBooking>,
    touristId: String,
    bookingHistoryViewModel: BookingHistoryViewModel,
    touristWalletViewModel: TouristWalletViewModel,
    coroutineScope: CoroutineScope,

  //  onTryTempCounter: () -> Unit,


    onNavToChat: (String) -> Unit
) {

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {

            if (staycationBookingItems.loadState.refresh == LoadState.Loading) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
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

            items(staycationBookingItems) { staycationBooking ->
                staycationBooking?.let {
                    val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
                    dateFormat.timeZone = TimeZone.getTimeZone("Asia/Manila") // Set the timezone to Manila

                    val manilaZoneId = ZoneId.of("Asia/Manila")

                    val formattedBookedDates = formatDateRange(
                        staycationBooking.checkInDate!!.toInstant()
                            .atZone(manilaZoneId)
                            .toLocalDate(),
                        staycationBooking.checkOutDate!!.toInstant()
                            .atZone(manilaZoneId)
                            .toLocalDate()
                    )


                    val bookingHistory = BookingHistory(
                        bookingId = staycationBooking.staycationBookingId,
                        //   ownerImage = staycationBooking.staycation?.hostImage
                        ownerImage = staycationBooking.staycation?.host?.profilePicture
                            ?: "https://upload.wikimedia.org/wikipedia/commons/8/89/Portrait_Placeholder.png",
                        bookedRental = staycationBooking.staycation?.staycationTitle ?: "",
                        date = dateFormat.format(staycationBooking.bookingDate ?: Date()),
                        rentalImage = staycationBooking.staycation?.staycationImages?.find { it.photoType == "Cover" }?.photoUrl ?: "https://www.unfe.org/wp-content/uploads/2019/04/SM-placeholder.png",//,
                        rentalLocation = staycationBooking.staycation?.staycationLocation ?: "",
                        bookedDates = formattedBookedDates,
                        guestsNo = staycationBooking.noOfGuests,
                        totalAmount = staycationBooking.totalAmount,
                        rentalStatus = staycationBooking.bookingStatus,
                        isReviewed = staycationBooking.bookingReview != null,
                        bookingDuration = staycationBooking.getDaysDifference().toInt(),
                        basePrice = staycationBooking.staycation?.staycationPrice ?: 0.0,
                        checkInDate = staycationBooking.checkInDate,
                        checkOutDate = staycationBooking.checkOutDate,
                        noOfPets = staycationBooking.noOfPets,
                        noOfInfants = staycationBooking.noOfInfants,
                        noOfGuests = staycationBooking.noOfGuests,
                        hostTouristId = staycationBooking.staycation?.host?.touristId ?: "",
                        staycationId = staycationBooking.staycation?.staycationId ?: "",
                        tourId = "",
                        tourAvailabilityId = "",
                        tourDate = "",
                        startTime = "",
                        endTime = ""


                    )

                    BookingHistoryCard(
                        touristId = touristId,
                        bookingHistoryViewModel = bookingHistoryViewModel,
                        touristWalletViewModel = touristWalletViewModel,
                        bookingHistory = bookingHistory,
                        coroutineScope = coroutineScope,
                        modifier = Modifier.padding(top = 15.dp),
                        staycationBookingItems = staycationBookingItems,
                        onChatHost = { receiverId ->
                            onNavToChat(receiverId)
                        },
//                        onTryTempCounter = {
//                            onTryTempCounter()
//                        }
                    )
                }

            }

        }
//        if (staycationBookingItems.loadState.refresh is LoadState.Loading) {
//            CircularProgressIndicator(
//                color = Orange,
//                modifier = Modifier
//                    .size(50.dp)
//                    .padding(16.dp)
//                    .align(Alignment.CenterHorizontally)
//            )
//        }
    }
}





private fun formatDateRange(startDate: LocalDate, endDate: LocalDate): String {
    val startDay = startDate.dayOfMonth
    val endDay = endDate.dayOfMonth
    val startMonth = startDate.month.getDisplayName(TextStyle.SHORT, Locale.getDefault())
    val endMonth = endDate.month.getDisplayName(TextStyle.SHORT, Locale.getDefault())

    return if (startDate.month == endDate.month) {
        "$startDay-$endDay $startMonth"
    } else {
        "$startDay $startMonth-$endDay $endMonth"
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingHistoryCard(
    modifier: Modifier = Modifier,
    touristId: String,
    bookingHistoryViewModel: BookingHistoryViewModel,
    touristWalletViewModel: TouristWalletViewModel,
    bookingHistory: BookingHistory,
    coroutineScope: CoroutineScope,
    onChatHost: (String) -> Unit,

   // onTryTempCounter: () -> Unit,

    staycationBookingItems: LazyPagingItems<StaycationBooking>? = null,
    tourBookingItems: LazyPagingItems<TourBooking>? = null,
){
    val isSuccessAddingReview = bookingHistoryViewModel.isSuccessAddReview.collectAsState().value
    val isCancelBookingSuccessful = bookingHistoryViewModel.isSuccessCancelBooking.collectAsState().value

    var openReviewCard by remember { mutableStateOf(false) }
    val openAlertDialog = remember{ mutableStateOf(false)}
    var isModalBottomSheetVisible by remember { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = {
            true
        }
    )
    val bookingFee = bookingHistory.basePrice * bookingHistory.bookingDuration
    val tripnilaFee = bookingFee * 0.05


    touristWalletViewModel.getHostWallet(bookingHistory.hostTouristId)
    LaunchedEffect(isSuccessAddingReview) {
        if (isSuccessAddingReview == true) {

            delay(600)

            staycationBookingItems?.refresh()
            tourBookingItems?.refresh()
            openReviewCard = false
            bookingHistoryViewModel.clearIsSuccessAddReview()
        }
    }

    LaunchedEffect(isCancelBookingSuccessful) {
        if (isCancelBookingSuccessful != null) {
            if (isCancelBookingSuccessful == true) {
                isModalBottomSheetVisible = false
            }
        }
    }


    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp
        ),
        modifier = modifier
            .fillMaxWidth()
    ){
        Column(
        modifier = Modifier
            .padding(
                start = 20.dp,
                end = 20.dp,
                top = 10.dp,
                bottom = 10.dp
            ),
        ){
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(100))
                        .size(25.dp)
                ){
                    AsyncImage(
                        model = bookingHistory.ownerImage,
                        contentDescription = "",
                        contentScale = ContentScale.Crop
                    )
                }
                Column(
                    modifier = Modifier.padding(horizontal = 5.dp)
                ) {
                    Text(
                        text = bookingHistory.bookedRental,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,

                    )
                    Text(
                        text = bookingHistory.date,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF999999)
                    )
                    // TEMP
                }
                Tag(tag = bookingHistory.rentalStatus)
              //  bookingHistoryViewModel.rentalStatus.collectAsState().value?.let { Tag(tag = it) }
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.more_vertical),
                    contentDescription = "More",
                    modifier = Modifier
                        .size(24.dp)
                        .offset(x = 10.dp)
                )

            }
            Divider(
                color = Color(0xFFDEDEDE),
                modifier = Modifier.padding(top = 10.dp, bottom = 8.dp)
            )


            if (openReviewCard){
                WriteReviewComposable(
                    bookingHistoryViewModel = bookingHistoryViewModel,
                    bookingId = bookingHistory.bookingId,
                    staycationId = bookingHistory.staycationId,
                    tourId = bookingHistory.tourId,
                    coroutineScope = coroutineScope,
                    onCancelClick = {
                        openReviewCard = false
                    },
                )

            }
            else{
                Row {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(0.45f)
                            .padding(start = 10.dp)
                    ) {
                        Text(
                            text = "Location",
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = bookingHistory.rentalLocation,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF999999)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = if(bookingHistory.tourId != "") "Schedule" else "Dates",
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = bookingHistory.bookedDates,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF999999)
                        )
                        if (bookingHistory.tourId != "") {
                            Text(
                                text = "${bookingHistory.startTime} - ${bookingHistory.endTime}" ,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF999999)
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Guests",
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = bookingHistory.guestsNo.toString(),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF999999)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Total",
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "₱ ${"%.2f".format(bookingHistory.totalAmount)}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = Orange
                        )
                    }
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        ElevatedCard(
                            shape = RoundedCornerShape(10.dp),
                            elevation = CardDefaults.elevatedCardElevation(
                                defaultElevation = 10.dp
                            ),
                            modifier = Modifier
                                .height(120.dp)
                                .width(140.dp)
                                .align(Alignment.End),
                        ) {

                            AsyncImage(
                                model = bookingHistory.rentalImage,
                                contentDescription = null,
                                contentScale = ContentScale.Crop
                            )
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp)
                        ) {
                            if (bookingHistory.rentalStatus == "Pending" || bookingHistory.rentalStatus == "Ongoing"){
                                if (bookingHistory.rentalStatus == "Ongoing") {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                                BookingOutlinedButton(
                                    buttonText = "Chat host",
                                    onClick = {
                                        val receiverId = bookingHistory.hostTouristId
                                        Log.d("HOst Id" , receiverId)
                                        onChatHost(receiverId)
                                    },
                                    modifier = Modifier.width(95.dp)
                                )
                                if (bookingHistory.rentalStatus != "Ongoing") {
                                    Spacer(modifier = Modifier.weight(1f))
                                    BookingFilledButton(
                                        buttonText = "Cancel",
                                        onClick = {
                                            //  isOpen.value = true
                                            isModalBottomSheetVisible = true
                                            //    bookingHistoryViewModel.setSelectedBookingHistory(bookingHistory)
                                        },
                                        modifier = Modifier.width(95.dp)
                                    )
                                }

                            }
                            else{
                                Spacer(modifier = Modifier.weight(1f))
                                BookingOutlinedButton(
                                    enableButton = !bookingHistory.isReviewed && bookingHistory.rentalStatus == "Completed", //|| bookingHistory.rentalStatus != "Cancelled"

                                    buttonText = "Review",
                                    onClick = {
                                        openReviewCard = true
                                    },
                                    modifier = Modifier.width(95.dp),
                                )
                            }
                        }
                    }
                }

            }


            if (isModalBottomSheetVisible) {
                // selectedBookingHistory?.let { selectedHistory ->
                ModalBottomSheet(
                    modifier = Modifier.fillMaxHeight(0.75f),
                    sheetState = bottomSheetState,
                    onDismissRequest = {
                        //bookingHistoryViewModel.clearBookingHistory()
                        isModalBottomSheetVisible = false
                    },
                    dragHandle = {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            //horizontalAlignment = Alignment.End
                        ) {
                            IconButton(
                                onClick = {
                                    isModalBottomSheetVisible = false
                                },
                                modifier = Modifier
                                    .padding(end = 12.dp, top = 8.dp)
                                    .size(30.dp)
                                    .align(Alignment.End)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Close,
                                    contentDescription = "Close",
                                    tint = Color(0xFFCECECE),
                                    modifier = Modifier.size(30.dp)
                                )
                            }
                        }
                    },
                    containerColor = Color.White
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(
                                horizontal = 25.dp,
//                vertical = 20.dp // 12
                            )
                            .background(Color.White)
                    ) {
                        Text(
                            text = "Cancel booking",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                        if (bookingHistory.staycationId != "") {
                            AppConfirmAndPayDivider(
                                image = bookingHistory.rentalImage,//R.drawable.staycation1,
                                itinerary = bookingHistory.bookedRental,
                                price = bookingHistory.basePrice,
                                unit = "night"
                            )
                        } else {
                            AppConfirmAndPayDivider(
                                image = bookingHistory.rentalImage,//R.drawable.staycation1,
                                itinerary = bookingHistory.bookedRental,
                                price = bookingHistory.basePrice,
                                unit = "person"
                            )
                        }



                        if (bookingHistory != null) {
                            YourTripCancellationDivider(bookingHistory)
                        }

                        // EDIT HERE
                        val checkInLocalDate = if (bookingHistory.staycationId != "") bookingHistory.checkInDate.let { date ->
                                                        Instant.ofEpochMilli(date.time).atZone(ZoneId.systemDefault()).toLocalDate()
                                                    } else {
                                                        LocalDate.parse(bookingHistory.tourDate)
                                                    }
                        val daysBeforeCheckIn: Int = checkInLocalDate?.let {
                            ChronoUnit.DAYS.between(LocalDate.now(), it).toInt()
                        } ?: 0

                        AppPaymentDivider(
                            touristId = touristId,
                            forCancelBooking = true,
                            bookingHistoryViewModel = bookingHistoryViewModel,
                            bookingFee = bookingHistory.basePrice,
                            bookingDuration = if (bookingHistory.staycationId != "") bookingHistory.bookingDuration else bookingHistory.noOfGuests,
                            maintenanceFee = if (bookingHistory.staycationId != "") bookingHistory.basePrice.times(0.10) else null,
                       //     tripnilaFee = bookingHistory?.staycationPrice?.times(0.05) ?: 0.0,
                            daysBeforeCheckIn = daysBeforeCheckIn,
                            touristWalletViewModel = touristWalletViewModel
                        )
                        CancellationAgreementText()
                        Spacer(modifier = Modifier.height(15.dp))
                        BookingFilledButton(
                            isLoading = bookingHistoryViewModel.isLoadingCancelBooking.collectAsState().value,
                            buttonText = "Cancel booking",
                            onClick = {
                                bookingHistoryViewModel.setAlertDialogMessage()
                                openAlertDialog.value = true
                                Log.d("Clicked CardId", bookingHistory.bookingId)
                                Log.d("Staycation Id", bookingHistory.staycationId)
                                Log.d("Tour Id", bookingHistory.tourId)
                            },
                            modifier = Modifier
                                .padding(horizontal = 10.dp)
                                .fillMaxWidth()
                        )
                    }
                }
            }

        }
    }

    if (openAlertDialog.value) {
        Dialog(onDismissRequest = { openAlertDialog.value = false }) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 20.dp
                ),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    //.height(200.dp)
                    .padding(20.dp),
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                ) {
                    Text(
                        text = bookingHistoryViewModel.alertDialogMessage.collectAsState().value ?: "",
                        color = Color.Black,
                        fontSize = 18.sp,
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                            .padding(top = 25.dp),
                    )
                    Spacer(modifier = Modifier.height(30.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        //   horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        if (bookingHistoryViewModel.alertDialogMessage.collectAsState().value != "Are you sure you want to proceed?") {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                        TextButton(
                            onClick = {
                                openAlertDialog.value = false
                             //   onTryTempCounter()
                            },
                            modifier = Modifier.padding(horizontal = 10.dp),
                        ) {
                            Text("Dismiss", color = Color.Black)
                        }
                        if (bookingHistoryViewModel.alertDialogMessage.collectAsState().value == "Are you sure you want to proceed?") {
                            Spacer(modifier = Modifier.weight(1f))
                            TextButton(
                                onClick = {

                                    coroutineScope.launch {
                                        openAlertDialog.value = false


                                        // EDIT HERE
                                        if (bookingHistory.staycationId != "") {

                                            bookingHistoryViewModel.cancelStaycationBooking(
                                                bookingId = bookingHistory.bookingId,
                                                staycationId = bookingHistory.staycationId,
                                                checkInDate = bookingHistory.checkInDate,
                                                checkOutDate = bookingHistory.checkOutDate
                                            )

                                        } else {
                                            bookingHistoryViewModel.cancelTourBooking(
                                                bookingId = bookingHistory.bookingId,
                                                tourAvailabilityId = bookingHistory.tourAvailabilityId,
                                                guestCount = bookingHistory.noOfGuests
                                            )

                                        }

                                        touristWalletViewModel.setRefundedBalance(touristId = touristId, hostWalletId = bookingHistory.hostTouristId,tripnilaFee = tripnilaFee)

                                    }


                                },
                                modifier = Modifier.padding(horizontal = 10.dp),
                            ) {
                                Text("Confirm", color = Color.Black)
                            }
                        }
                    }
                }
            }
        }
    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingHistoryTopBar(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
){

    val filter = listOf("Relevance", "Recent", "Top rated")

    TopAppBar(
        navigationIcon = {
            IconButton(onClick = {
                onBack()
            }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color(0xFF999999)
                )
            }

        },
        title = {
            Row(
                modifier = Modifier.padding(horizontal = 10.dp),
                verticalAlignment = Alignment.CenterVertically

            ) {
                Text(
                    text = "History",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.weight(1f))
                AppDropDownFilter(options = filter)
            }
        },
        modifier = modifier
    )
}

@Composable
fun YourTripCancellationDivider(
    bookingHistory: BookingHistory,
    modifier: Modifier = Modifier,
) {

    val checkInLocalDate= bookingHistory.checkInDate.let { date ->
        Instant.ofEpochMilli(date.time).atZone(ZoneId.systemDefault()).toLocalDate()
    }
    val checkOutLocalDate= bookingHistory.checkOutDate.let { date ->
        Instant.ofEpochMilli(date.time).atZone(ZoneId.systemDefault()).toLocalDate()
    }

    val startDay = checkInLocalDate?.dayOfMonth
    val endDay = checkOutLocalDate?.dayOfMonth
    val startMonth = checkInLocalDate?.month?.getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
    val endMonth = checkOutLocalDate?.month?.getDisplayName(TextStyle.SHORT, Locale.ENGLISH)

    val formattedDateRange = if (checkInLocalDate?.month == checkOutLocalDate?.month) "$startDay-$endDay $startMonth" else "$startDay $startMonth-$endDay $endMonth"

    val guestCount = bookingHistory.noOfGuests
    val infantCount = bookingHistory.noOfInfants
    val petCount = bookingHistory.noOfPets

    val guestInfoText = buildAnnotatedString {
        append("$guestCount ${
            guestCount.let {
                if (it == 1) "guest" else "guests"
            }
        }")

        if (infantCount > 0) {
            append(", $infantCount ${
                infantCount.let {
                    if (it == 1) "infant" else "infants"
                }
            }")
        }

        if (petCount > 0) {
            append(", $petCount ${
                petCount.let {
                    if (it == 1) "pet" else "pets"
                }
            }")
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(all = 10.dp)

    ){
        Text(
            text = "Your trip",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
        val dateRowText = formattedDateRange.takeIf { it.isNotBlank() } ?: "Select dates*"
        val dateRowTextColor = if (formattedDateRange.isNotBlank()) Color(0xFF999999) else Color.Red


        val formatter = DateTimeFormatter.ofPattern("d MMM, EEEE", Locale.ENGLISH)

        AppYourTripRow(
            rowLabel = "Dates",
            rowText = if (bookingHistory.tourId != "") "${LocalDate.parse(bookingHistory.tourDate).format(formatter)} ${bookingHistory.startTime} - ${bookingHistory.endTime}" else dateRowText,
            fontColor = dateRowTextColor,
            forCancelBooking = true

        )

        // Display a prompt if guestCount is null or zero
        val guestCountText = when {
            guestCount <= 0 -> "Select guests*"
            else -> "$guestInfoText"
        }

        val guestColor = when {
            guestCount <= 0 -> Color.Red
            else -> Color(0xFF999999)
        }
        AppYourTripRow(
            rowLabel = "Guests",
            fontColor = guestColor,
            rowText = guestCountText,
            forCancelBooking = true
        )
    }
    Divider(
        color = Color(0xFF999999),
        modifier = Modifier.padding(top = 5.dp) // 5.dp
    )
}



@Composable
fun WriteReviewComposable(
    bookingHistoryViewModel: BookingHistoryViewModel,
    coroutineScope: CoroutineScope,
    bookingId: String,
    tourId: String,
    staycationId: String,
    onCancelClick: () -> Unit,
){

    val reviewInput = bookingHistoryViewModel.userInputReview.collectAsState().value

    val localFocusManager = LocalFocusManager.current
    //var comment by remember { mutableStateOf("") }
    var comment = reviewInput.comment
    val maxCharacterLimit = 100
    val remainingCharacters = maxCharacterLimit - comment.length


    var selectedImageUris = bookingHistoryViewModel.selectedImageUris.collectAsState().value

    val multiplePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia()
    )
    //onResult = { uris -> selectedImageUris = uris }
    { uris ->
        selectedImageUris = uris
        bookingHistoryViewModel.setSelectedImageUris(selectedImageUris)
    }


    Text(
        text = "Write a review",
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp
    )
    AppRatingBar(bookingHistoryViewModel = bookingHistoryViewModel, initialRating = bookingHistoryViewModel.userInputReview.collectAsState().value.rating)
    Spacer(modifier = Modifier.height(8.dp))
    comment.let {
        BasicTextField(
        value = it,
        onValueChange = {
            if (it.length <= maxCharacterLimit) {
                comment = it
                bookingHistoryViewModel.setComment(comment)
            }

        },
        textStyle = androidx.compose.ui.text.TextStyle(fontSize = 10.sp, color = Color(0xFF6B6B6B)),
        modifier = Modifier
            .fillMaxWidth()
            .height(65.dp)
            .border(1.dp, Color(0xFF999999), shape = RoundedCornerShape(10.dp))
        ,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions {
            localFocusManager.clearFocus()
        },
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier
                    .padding(vertical = 4.dp, horizontal = 6.dp)
                    .fillMaxWidth()
                // .fillMaxHeight()
            ){
                Row(
                    // modifier =Modifier.fillMaxHeight(),
                    verticalAlignment = Alignment.Top
                ) {
                    if (comment.isEmpty()) {
                        Text(
                            text = "Write something here...",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color(0xFF6B6B6B),
                            modifier = Modifier.weight(1f)
                        )
                    }

                }
                innerTextField()
            }
        }
    )
    }
    Text(
        text = buildAnnotatedString {
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                append("$remainingCharacters")
            }
            withStyle(style = SpanStyle(fontWeight = FontWeight.Medium)) {
                append(" characters available")
            }
        },
        fontSize = 8.sp,
        color = Color.Gray
    )
    Spacer(modifier = Modifier.height(8.dp))
    Text(
        text = "Add photos",
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
    ) {
        val itemsPerRow = 2
        val rows = (selectedImageUris.size + itemsPerRow - 1) / itemsPerRow
        var lastIndex = 0

        for (row in 0 until rows) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                val startIndex = row * itemsPerRow
                val endIndex = minOf(startIndex + itemsPerRow, selectedImageUris.size)

                for (i in startIndex until endIndex) {
                    val uri = selectedImageUris[i]
                    AsyncImage(
                        model = uri,
                        contentDescription = null,
                        modifier = Modifier
                            .width(140.dp)
                            .height(80.dp)
                            .padding(vertical = 5.dp)
                             ,
                        contentScale = ContentScale.Crop
                    )

                    lastIndex = i

                    if (i == startIndex) {
                        Spacer(modifier = Modifier.weight(1f)) // Adjust the width as needed
                    }
                }


                if (lastIndex == selectedImageUris.size - 1 && selectedImageUris.size % itemsPerRow != 0) {
                    AppChoosePhoto(
                        modifier = Modifier
                            .padding(vertical = 5.dp)
                            .width(125.dp)
                            .height(70.dp),
                        onClick = {
                            multiplePhotoPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        }
                    )
                }
            }
        }

        if (selectedImageUris.size % itemsPerRow == 0) {
            // AddMorePhoto if the last row is fully filled
            Row(
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                AppChoosePhoto(
                    modifier = Modifier
                        //.padding(top = 8.dp, bottom = 12.dp)
                        .padding(vertical = 5.dp)
                        .width(125.dp)
                        .height(70.dp),
                    onClick = {
                        multiplePhotoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }
                )
            }
        }
    }



    val isLoading = bookingHistoryViewModel.isLoadingAddReview.collectAsState().value


    Row {
        BookingOutlinedButton(
            buttonText = "Cancel",
            onClick = {
                bookingHistoryViewModel.setSelectedImageUris(emptyList())
                bookingHistoryViewModel.setComment("")
                bookingHistoryViewModel.setRating(0)
                onCancelClick()
            },
            modifier = Modifier.width(90.dp)

        )
        Spacer(modifier = Modifier.weight(1f))
        BookingFilledButton(
            isLoading = isLoading,
            buttonText = "Confirm",
            onClick = {
                coroutineScope.launch {
                    if (staycationId != "") {
                        bookingHistoryViewModel.setServiceReview(bookingId, "Staycation")
                    } else {
                        bookingHistoryViewModel.setServiceReview(bookingId, "Tour")
                    }
                }
            },
            modifier = Modifier.width(90.dp)
        )
    }
}

@Composable
fun CancellationAgreementText(modifier: Modifier = Modifier) {
    Text(
        text = "This process cannot be undone. Please check all the information. Any amount to be refunded will be sent to your chosen payment method.",
        color = Color.Black,
        fontSize = 10.sp,
        modifier = modifier
            .padding(horizontal = 10.dp, vertical = 10.dp)
            .fillMaxWidth()
    )
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AppRatingBar(
    bookingHistoryViewModel: BookingHistoryViewModel,
    modifier: Modifier = Modifier,
    initialRating: Int
) {
    var ratingState by remember {
        mutableStateOf(initialRating)
    }

  // var ratingState = bookingHistoryViewModel.userInputReview.collectAsState().value.rating

    var selected by remember {
        mutableStateOf(false)
    }
    val size by animateDpAsState(
        targetValue = if (selected) 12.dp else 12.dp,
        spring(Spring.DampingRatioMediumBouncy)
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 1..5) {
            Icon(
                painter = painterResource(id = R.drawable.star_rating),
                contentDescription = "star",
                modifier = modifier
                    .padding(2.dp)
                    .width(size)
                    .height(size)

                    .pointerInteropFilter {
                        when (it.action) {
                            MotionEvent.ACTION_DOWN -> {
                                selected = true
                                ratingState = i
                            }

                            MotionEvent.ACTION_UP -> {
                                selected = false
                                bookingHistoryViewModel.setRating(ratingState)
                            }


                        }
                        true
                    },
                tint = if (i <= ratingState) Orange else Color(0xFFA2ADB1)
            )
        }
    }
}


@Preview
@Composable
private fun BookingHistoryScreenPreview(){

    val bookingHistoryViewModel = viewModel(modelClass = BookingHistoryViewModel::class.java)

     BookingHistoryScreen(
         touristId = "n7r1JjE18t5iCP32GXjt",
         touristWalletViewModel = TouristWalletViewModel(),
         bookingHistoryViewModel = bookingHistoryViewModel,
         onNavToChat = { _, _ ->

         },
         onBack = {

         }

     )

//    val manilaZoneId = ZoneId.of("Asia/Manila")
//    val currentTimeInManila = ZonedDateTime.now(manilaZoneId)
//    println("${currentTimeInManila.toInstant().toEpochMilli()}")

}





