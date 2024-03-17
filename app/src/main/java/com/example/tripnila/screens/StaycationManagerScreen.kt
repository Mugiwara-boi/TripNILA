package com.example.tripnila.screens

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
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
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.tripnila.common.AdditionalInformationRow
import com.example.tripnila.common.AppOutlinedButton
import com.example.tripnila.common.AppReviewsCard
import com.example.tripnila.common.LoadingScreen
import com.example.tripnila.common.Orange
import com.example.tripnila.common.Tag
import com.example.tripnila.data.ReviewUiState
import com.example.tripnila.data.Transaction
import com.example.tripnila.model.StaycationManagerViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StaycationManagerScreen(
    staycationId: String = "",
    hostId: String = "",
    onNavToEditStaycation: (String, String, String) -> Unit,
    onNavToDashboard: (String) -> Unit,
    staycationManagerViewModel: StaycationManagerViewModel
){

    Log.d("Staycation", "$staycationId $hostId")

    LaunchedEffect(staycationId) {
        staycationManagerViewModel.getSelectedStaycation(staycationId)
    }

    val staycation = staycationManagerViewModel?.staycation?.collectAsState()
    val touristId = hostId.substring(5)

    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f
    ) {
        staycation?.value?.staycationImages?.size ?: 0
    }

    val availableDates = staycationManagerViewModel.staycation.collectAsState().value.availableDates
        .map { it.availableDate }
        .map {
            it?.let { it1 ->
                Instant.ofEpochSecond(it1.seconds, it.nanoseconds.toLong())
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()
            }
        }.sortedBy { it }

    val bookedDates = staycationManagerViewModel.staycation.collectAsState().value.staycationBookings
        .asSequence()
        .filter { it.bookingStatus != "Cancelled" }
        .map { it.getDatesBetween() }
        .flatten()
        .toSet()
        .sortedBy { it }
        .toList()

//    val bookedDates: List<List<LocalDate>> = staycationManagerViewModel.staycation.collectAsState().value.staycationBookings
//        .map { it.getDatesBetween() }


    val transactions = staycation?.value?.staycationBookings?.map {staycationBooking ->
        val formatter = SimpleDateFormat("MMM d", Locale.getDefault())
        val start = formatter.format(staycationBooking.checkInDate)
        val end = formatter.format(staycationBooking.checkOutDate)
        val year = SimpleDateFormat("yyyy", Locale.getDefault()).format(staycationBooking.checkInDate)

        Transaction(
            customerImage = staycationBooking.tourist?.profilePicture ?: "",
            customerName = "${staycationBooking.tourist?.firstName} ${staycationBooking.tourist?.lastName}",
            customerUsername = staycationBooking.tourist?.username ?: "",
            guestsCount = staycationBooking.noOfGuests,
            price = staycationBooking.totalAmount ?: 0.0,
            bookedDate = "$start-$end, $year",
            transactionDate = SimpleDateFormat("MMM dd, yyyy").format(staycationBooking.bookingDate) , //staycationBooking.bookingDate.toString(),
            transactionStatus = staycationBooking.bookingStatus
        )
    }?.sortedByDescending { it.transactionDate } ?: emptyList()

    val staycationReviews = staycation?.value?.staycationBookings?.mapNotNull { it.bookingReview }
    val reviews: List<ReviewUiState> = staycationReviews?.filter { it.bookingId != "" }
        ?.map { review ->
        review.let {
            ReviewUiState(
                rating = it.rating.toDouble() ?: 0.0,
                comment = it.comment ?: "",
                touristImage = it?.reviewer?.profilePicture ?: "https://upload.wikimedia.org/wikipedia/commons/8/89/Portrait_Placeholder.png",
                touristName = "${it?.reviewer?.firstName} ${it.reviewer?.lastName}",
                reviewDate = it.reviewDate.toString()
            )
        }
    } ?: emptyList()

    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = Color(0xFFEFEFEF)
    ) {

            //staycationManagerViewModel?.isStateRetrieved?.collectAsState()?.value == false &&

        if (staycation?.value?.staycationId != staycationId) {
            LoadingScreen(isLoadingCompleted = false, isLightModeActive = true)
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                item {
                    HorizontalPager(
                        state = pagerState, // Specify the count of items in the pager
                        modifier = Modifier.fillMaxSize()
                    ) { page ->
                        val image = staycation?.value!!.staycationImages.sortedBy { it.photoType }.getOrNull(page)
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(240.dp)
                        ) {
                            if (image != null) {
                                AsyncImage(
                                    model = image.photoUrl,
                                    contentDescription = "",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            } else {
                                AsyncImage(
                                    model = "https://upload.wikimedia.org/wikipedia/commons/thumb/3/3f/Placeholder_view_vector.svg/1022px-Placeholder_view_vector.svg.png",
                                    contentDescription = "",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                            ManagerTopBarIcons(
                                onEdit = {
                                    onNavToEditStaycation(staycationId, hostId, "Staycation")
                                },
                                onBack = {
                                    onNavToDashboard(touristId)
                                }
                            )
                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = Color.Black.copy(alpha = 0.69f)
                                ),
                                shape = RoundedCornerShape(20.dp),
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .padding(horizontal = 15.dp, vertical = 25.dp)
                                    .width(30.dp)
                                    .height(20.dp)
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = "${page + 1}/${staycation.value!!.staycationImages.size}",
                                        color = Color.White,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium,
                                    )
                                }

                            }


                        }

                    }

                   /* Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(240.dp)
                    ) {
                        AsyncImage(
                            model = if ( staycation?.value?.staycationImages?.find { it.photoType == "Cover" }?.photoUrl == "") "https://upload.wikimedia.org/wikipedia/commons/thumb/3/3f/Placeholder_view_vector.svg/1022px-Placeholder_view_vector.svg.png" else staycation?.value?.staycationImages?.find { it.photoType == "Cover" }?.photoUrl,//imageLoader,
                            contentDescription = "",
                            contentScale = ContentScale.Crop,
                        )
                        ManagerTopBarIcons(
                            onEdit = {
                                onNavToEditStaycation(staycationId, hostId, "Staycation")
                            },
                            onBack = {
                                onNavToDashboard(touristId)
                            }
                        )
                    }*/
                }
                item {
                    StaycationDescriptionCard1(
                        staycation = staycation?.value,
                        withEditButton = false,
                        modifier = Modifier
                            .offset(y = (-17).dp)
                    )
                }
                item {
                    StaycationDescriptionCard3(
                        staycation = staycation?.value,
                        //amenities = amenities,
                        withEditButton = false,
                        modifier = Modifier
                            .offset(y = (-5).dp)
                            .padding(bottom = 12.dp)
                    )
                }
                item {
                    StaycationAmenitiesCard(
                        staycation = staycation?.value,
                        //amenities = amenities, /*TODO*/
                        withEditButton = false,
                        modifier = Modifier
                            .offset(y = (-5).dp)
                            .padding(bottom = 12.dp)
                    )
                }
                item {
                    AvailabilityAndPricingCard(
                        staycationManagerViewModel =staycationManagerViewModel,
                        availableDates = availableDates,
                        bookedDates = bookedDates,
                        modifier = Modifier
                            .offset(y = (-5).dp)
                            .padding(bottom = 12.dp)
                    )
                }
                item {
                    StaycationManagerAdditionalInformationCard(
                        staycationManagerViewModel = staycationManagerViewModel,
                        withEditButton = false,
                        modifier = Modifier
                            .offset(y = (-5).dp)
                            .padding(bottom = 12.dp) // 7
                    )
                }
                item {
                    AppTransactionsCard(
                        transactions = transactions,
                        modifier = Modifier
                            .offset(y = (-5).dp)
                            .padding(bottom = 12.dp) // 7
                    )
                }
                item {
                    staycation?.value?.averageReviewRating?.let { averageRating ->
                        staycation?.value?.totalReviews?.let { totalReviews ->
                            AppReviewsCard(
                                totalReviews = totalReviews,
                                averageRating = averageRating,
                                onSeeAllReviews = {

                                },
                                reviews = reviews,
                                modifier = Modifier
                                    .offset(y = (-5).dp)
                                    .padding(bottom = 7.dp)
                            )
                        }
                    }
                }
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTransactionsCard(transactions: List<Transaction>, modifier: Modifier = Modifier){


    val seeAll = remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp
        ),
        modifier = modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                //                .padding(horizontal = 25.dp, vertical = 12.dp),
                .padding(
                    horizontal = 25.dp,
                    vertical = 20.dp // 12
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Transactions",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(bottom = 4.dp)
            )

            transactions.take(2).forEach { transaction ->
                TransactionCard(
                    transaction = transaction,
                    modifier = Modifier.padding(top = 7.dp)
                )
            }

            AppOutlinedButton(
                buttonText = "See all transactions",
                onClick = {
                    seeAll.value = true
                },
                modifier = Modifier
                    .padding(top = 12.dp)
            )

        }
    }

    if (seeAll.value) {
        ModalBottomSheet(
            shape = RoundedCornerShape(20.dp),
            containerColor = Color.White,
            dragHandle = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier
                        .padding(start = 3.dp, end = 16.dp) //, top = 3.dp
                        .fillMaxWidth()
                ) {
                    IconButton(
                        onClick = { seeAll.value = false },
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Close"
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))

                }
            },
            onDismissRequest = { seeAll.value = false },
            sheetState = sheetState,
            modifier = Modifier
                .fillMaxHeight(0.8f) //0.693
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    // .padding(horizontal = 25.dp,)
                    .background(Color.White)
            ) {
                Text(
                    text = "All transactions",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 16.dp)
                        .fillMaxWidth()
                )

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    items(transactions) { transaction ->
                        TransactionCard(
                            transaction = transaction,
                            modifier = Modifier.padding(top = 7.dp)
                        )
                    }
                }
            }

        }
    }
}

@Composable
fun TransactionCard(transaction: Transaction, modifier: Modifier = Modifier){
    OutlinedCard(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, Color(0xff999999)),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        )
    ){
        Row(
            modifier = Modifier
                .padding(
                    horizontal = 25.dp,
                    vertical = 15.dp // 12
                ),
            horizontalArrangement = Arrangement.Center
        ) {
            Column {
                Row {
                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .clip(shape = RoundedCornerShape(50.dp))
                    ) {
                        AsyncImage(
                            model = if (transaction.customerImage == "") "https://upload.wikimedia.org/wikipedia/commons/8/89/Portrait_Placeholder.png" else transaction.customerImage,//imageLoader,
                            contentDescription = "",
                            contentScale = ContentScale.Crop

                        )
                    }
                    Column(
                        modifier = Modifier.padding(start = 5.dp, end = 5.dp, bottom = 5.dp)
                    ) {
                        Text(
                            text = transaction.customerName,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                        )
                        Text(
                            text = transaction.customerUsername,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF999999)
                        )
                    }
                    Tag(tag = transaction.transactionStatus)

                }
                Text(
                    text = "${transaction.guestsCount} guests • ₱ ${"%.2f".format(transaction.price)} • ${transaction.bookedDate}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF999999),
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Column {
                Text(
                    text = transaction.transactionDate,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = Orange,
                    modifier = Modifier.offset(x = (-10).dp)
                )
//                IconButton(
//                    onClick = { /*TODO*/ },
//                    modifier = Modifier
//                        .size(36.dp)
//                        .align(Alignment.End)
//                        .offset(x = 10.dp, y = (-7).dp)
//                ) {
//                    Icon(
//                        imageVector = Icons.Filled.KeyboardArrowRight,
//                        contentDescription = "Expand",
//                        tint = Color(0xFF999999),
//                        modifier = Modifier.size(36.dp)
//                    )
//                }
            }

        }
    }

}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AvailabilityAndPricingCard(
    availableDates: List<LocalDate?>,
    bookedDates: List<LocalDate?>,
    staycationManagerViewModel: StaycationManagerViewModel,
    modifier: Modifier = Modifier
){

    var titleText: MutableState<String?> = remember { mutableStateOf(null) }
    var titleDeleteText: MutableState<String?> = remember { mutableStateOf(null) }

    val availabilityRangePickerState = rememberDateRangePickerState()
    val openAvailabilityBottomSheet = remember { mutableStateOf(false) }
    val availabilityBottomSheetState = rememberModalBottomSheetState( skipPartiallyExpanded = true)

    var hasNavigationBar = WindowInsets.areNavigationBarsVisible
    var enableBottomSaveButton: MutableState<Boolean> = remember { mutableStateOf(false) }
    var isSaveButtonClicked = remember { mutableStateOf(false) }

    val deleteRangePickerState = rememberDateRangePickerState()
    val openDeleteBottomSheet = remember { mutableStateOf(false) }
    val deleteBottomSheetState = rememberModalBottomSheetState( skipPartiallyExpanded = true)

    var enableBottomDeleteButton: MutableState<Boolean> = remember { mutableStateOf(false) }
    var isDeleteButtonClicked = remember { mutableStateOf(false) }
    

    LaunchedEffect(availabilityRangePickerState.selectedEndDateMillis) {
        if (availabilityRangePickerState.selectedEndDateMillis != null) {
            enableBottomSaveButton.value = true
        }
    }

    LaunchedEffect(deleteRangePickerState.selectedEndDateMillis) {
        if (deleteRangePickerState.selectedEndDateMillis != null) {
            enableBottomDeleteButton.value = true
        }
    }




    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp
        ),
        modifier = modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(
                    horizontal = 25.dp,
                    vertical = 20.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = "Availability & Pricing",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .padding(bottom = 4.dp)
                        .weight(1f)
                )

                AppOutlinedButtonWithBadge(
                    buttonLabel = "Add",
                    onClick = { openAvailabilityBottomSheet.value = true },
                    modifier = Modifier
                        .width(70.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                AppFilledButtonWithBadge(
                    buttonLabel = "Remove",
                    onClick = { openDeleteBottomSheet.value = true },
                    modifier = Modifier
                        .width(70.dp)
                )

            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(size = 16.dp)
                        .clip(shape = RoundedCornerShape(30.dp))
                        .background(color = Color(0xff9fffb4))
                )
                Text(
                    text = "Available",
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                )
                Spacer(modifier = Modifier.width(10.dp))
                Box(
                    modifier = Modifier
                        .size(size = 16.dp)
                        .clip(shape = RoundedCornerShape(30.dp))
                        .background(color = Color(0xffffa6a6))
                )
                Text(
                    text = "Booked",
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                )

            }
            CalendarComposable(
                availableDates = availableDates,
                bookedDates = bookedDates
            )

        }
        if (openAvailabilityBottomSheet.value) {
            ModalBottomSheet(
                shape = RoundedCornerShape(20.dp),
                containerColor = Color.White,
                dragHandle = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start,
                        modifier = Modifier
                            .padding(start = 3.dp, end = 16.dp) //, top = 3.dp
                            .fillMaxWidth()
                    ) {
                        IconButton(
                            onClick = {
                                availabilityRangePickerState.setSelection(null, null)
                                openAvailabilityBottomSheet.value = false
                            },
                        ) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = "Close"
                            )
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        ClickableText(
                            text = buildAnnotatedString {
                                withStyle(
                                    style = SpanStyle(
                                        fontSize = 16.sp,
                                        textDecoration = TextDecoration.Underline
                                    )
                                ) {
                                    append("Clear")
                                }
                            },
                            onClick = {
                                availabilityRangePickerState.setSelection(null, null)
//                                isSaveButtonClicked.value = false
                            }

                        )


                    }
                },
                onDismissRequest = { openAvailabilityBottomSheet.value = false },
                sheetState = availabilityBottomSheetState,
                modifier = Modifier
                    .fillMaxHeight(0.8f) //0.693
            ) {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Spacer(modifier = Modifier
                        .weight(1f)
                        //  .height(15.dp)
                    )
                    DateRangePicker(
                        modifier = Modifier
                            .fillMaxHeight(if (hasNavigationBar) 0.8f else 1f), //0.8f

                        title = {
                            titleText.value?.let { text ->
                                Text(
                                    text = text,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier.padding(horizontal = 30.dp)
                                )
                            }
                        },
                        headline = null,
                        showModeToggle = false,
                        state = availabilityRangePickerState,
                        colors = DatePickerDefaults.colors(
                            dayContentColor = Color.Black,
                            selectedDayContainerColor = Orange,
                            dayInSelectionRangeContainerColor = Orange.copy(.3f),
                            disabledDayContentColor = Color.Black.copy(0.3f),
                            //disabledSelectedDayContainerColor = Color.Red,
                            todayDateBorderColor = Orange,
                            todayContentColor = Color.Black
                        ),
                        dateValidator = { date ->
                            val selectedLocalDate = Instant.ofEpochMilli(date).atZone(ZoneId.systemDefault()).toLocalDate()
                            val currentDate = LocalDate.now()
                            bookedDates.map { (it?.toEpochDay() ?: 0) * 24 * 60 * 60 * 1000 }.toList()


                            selectedLocalDate > currentDate && selectedLocalDate !in availableDates && selectedLocalDate !in bookedDates
                        },
                    )

                    Spacer(modifier = Modifier.weight(1f))


                    ConfirmDate(
                        enableButton = enableBottomSaveButton.value,
                        onClickSave = {
                            isSaveButtonClicked.value = true

                            val startDateMillis = availabilityRangePickerState.selectedStartDateMillis
                            val endDateMillis = availabilityRangePickerState.selectedEndDateMillis

                            val startDate = startDateMillis?.let {
                                Instant.ofEpochMilli(it)
                                    .atZone(ZoneId.systemDefault())
                                    .toLocalDate()
                            }

                            val endDate = endDateMillis?.let {
                                Instant.ofEpochMilli(it)
                                    .atZone(ZoneId.systemDefault())
                                    .toLocalDate()
                            }

                            var currentDate = startDate

                            while (!currentDate?.isAfter(endDate)!!) {
                                staycationManagerViewModel.addAvailableDate(currentDate)
                                currentDate = currentDate.plusDays(1)
                            }


                            availabilityRangePickerState.setSelection(null, null)
                            openAvailabilityBottomSheet.value = false
                        },
                        isLoading = staycationManagerViewModel.isLoadingAddAvailability.collectAsState().value == true,
                        modifier = Modifier
                            .noPaddingIf(hasNavigationBar)
                    )
                    Spacer(modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars))
                }

            }
        }

        if (openDeleteBottomSheet.value) {
            ModalBottomSheet(
                shape = RoundedCornerShape(20.dp),
                containerColor = Color.White,
                dragHandle = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start,
                        modifier = Modifier
                            .padding(start = 3.dp, end = 16.dp) //, top = 3.dp
                            .fillMaxWidth()
                    ) {
                        IconButton(
                            onClick = {
                                deleteRangePickerState.setSelection(null, null)
                                openDeleteBottomSheet.value = false
                            },
                        ) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = "Close"
                            )
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        ClickableText(
                            text = buildAnnotatedString {
                                withStyle(
                                    style = SpanStyle(
                                        fontSize = 16.sp,
                                        textDecoration = TextDecoration.Underline
                                    )
                                ) {
                                    append("Clear")
                                }
                            },
                            onClick = {
                                deleteRangePickerState.setSelection(null, null)
//                                isSaveButtonClicked.value = false
                            }
                        )
                    }
                },
                onDismissRequest = { openDeleteBottomSheet.value = false },
                sheetState = deleteBottomSheetState,
                modifier = Modifier
                    .fillMaxHeight(0.8f) //0.693
            ) {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Spacer(modifier = Modifier
                        .weight(1f)
                    )
                    DateRangePicker(
                        modifier = Modifier
                            .fillMaxHeight(if (hasNavigationBar) 0.8f else 1f), //0.8f

                        title = {
                            titleDeleteText.value?.let { text ->
                                Text(
                                    text = text,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier.padding(horizontal = 30.dp)
                                )
                            }
                        },
                        headline = null,
                        showModeToggle = false,
                        state = deleteRangePickerState,
                        colors = DatePickerDefaults.colors(
                            dayContentColor = Color.Black,
                            selectedDayContainerColor = Orange,
                            dayInSelectionRangeContainerColor = Orange.copy(.3f),
                            disabledDayContentColor = Color.Black.copy(0.3f),
                            //disabledSelectedDayContainerColor = Color.Red,
                            todayDateBorderColor = Orange,
                            todayContentColor = Color.Black
                        ),
                        dateValidator = { date ->
                            val selectedLocalDate = Instant.ofEpochMilli(date).atZone(ZoneId.systemDefault()).toLocalDate()
                            bookedDates.map { (it?.toEpochDay() ?: 0) * 24 * 60 * 60 * 1000 }.toList()

                            selectedLocalDate in availableDates && selectedLocalDate !in bookedDates
                        },
                    )

                    Spacer(modifier = Modifier.weight(1f))


                    ConfirmDate(
                        enableButton = enableBottomDeleteButton.value,
                        onClickSave = {
                            isDeleteButtonClicked.value = true

                            val startDateMillis = deleteRangePickerState.selectedStartDateMillis
                            val endDateMillis = deleteRangePickerState.selectedEndDateMillis

                            val startDate = startDateMillis?.let {
                                Instant.ofEpochMilli(it)
                                    .atZone(ZoneId.systemDefault())
                                    .toLocalDate()
                            }

                            val endDate = endDateMillis?.let {
                                Instant.ofEpochMilli(it)
                                    .atZone(ZoneId.systemDefault())
                                    .toLocalDate()
                            }

                            var currentDate = startDate

                            while (!currentDate?.isAfter(endDate)!!) {
                                staycationManagerViewModel.removeAvailableDate(currentDate!!)
                                currentDate = currentDate!!.plusDays(1)
                            }


                            deleteRangePickerState.setSelection(null, null)
                            openDeleteBottomSheet.value = false
                        },
                        isLoading = staycationManagerViewModel.isLoadingDeleteAvailability.collectAsState().value == true,
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
fun ConfirmDate(
    modifier: Modifier = Modifier,
    onClickSave: () -> Unit,
    enableButton: Boolean = false,
    isLoading: Boolean,
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
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {

                }
                BookingFilledButton(
                    buttonText = "Save",
                    onClick = {
                        onClickSave()
                    },
                    enabled = enableButton,
                    isLoading = isLoading,
                    modifier = Modifier.width(90.dp)
                )
            }

        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StaycationEditAdditionalInformationCard(withEditButton: Boolean = false, modifier: Modifier = Modifier){

    val openHouseRulesModal = remember { mutableStateOf(false)}
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(20.dp))
            .background(color = Color.White)
    ) {
        Column(
            modifier = Modifier
                //                .padding(horizontal = 25.dp, vertical = 12.dp),
                .padding(
                    horizontal = 25.dp,
                    vertical = 20.dp // 12
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = "Additional information",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .padding(bottom = 4.dp)
                )
                if (withEditButton) {
                    Spacer(modifier = Modifier.width(8.dp))
                    AppOutlinedButtonWithBadge(
                        buttonLabel = "Edit",
                        modifier = Modifier
                            .width(40.dp)
                    )
                }
            }
            AdditionalInformationRow(textInfo = "House Rules", onClick = {openHouseRulesModal.value = true})
            AdditionalInformationRow(textInfo = "Health & safety", onClick = {/*TODO*/})
            AdditionalInformationRow(textInfo = "Cancellation & reschedule policy", onClick = {/*TODO*/})
            AdditionalInformationRow(textInfo = "Business Information", onClick = {/*TODO*/})

        }
    }

    if(openHouseRulesModal.value){
        ModalBottomSheet(
            shape = RoundedCornerShape(20.dp),
            containerColor = Color.White,
            dragHandle = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier
                        .padding(start = 3.dp, end = 16.dp) //, top = 3.dp
                        .fillMaxWidth()
                ) {
                    IconButton(
                        onClick = { openHouseRulesModal.value = false },
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Close"
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    /*                    ClickableText(
                                            text = buildAnnotatedString {
                                                withStyle(
                                                    style = SpanStyle(
                                                        fontSize = 16.sp,
                                                        textDecoration = TextDecoration.Underline
                                                    )
                                                ) {
                                                    append("Clear")
                                                }
                                            },
                                            onClick = {
                                                dateRangePickerState.setSelection(null, null)
                                                isSaveButtonClicked.value = false
                                            }

                                        )*/


                }
            },
            onDismissRequest = { openHouseRulesModal.value = false },
            sheetState = bottomSheetState,
            modifier = Modifier
                .fillMaxHeight(0.8f) //0.693
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Spacer(modifier = Modifier
                    .weight(1f)
                    //  .height(15.dp)
                )


                Spacer(modifier = Modifier.weight(1f))

                Spacer(modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars))
            }

        }
    }
}


@Composable
fun CalendarComposable(
    bookedDates: List<LocalDate?> = emptyList(),
    availableDates: List<LocalDate?> = emptyList(),
    modifier: Modifier = Modifier
) {

   // var bookedDates by remember { mutableStateOf(bookedDates) }
    var currentDate by remember { mutableStateOf(LocalDate.now()) }

    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(10.dp),
        modifier = modifier

            .fillMaxWidth()
        //.height(450.dp)
    ) {
        Row(
            modifier = Modifier
                .background(color = Orange)
                .padding(horizontal = 10.dp)
                .fillMaxWidth()
                .height(45.dp)
                .clip(
                    shape = RoundedCornerShape(
                        topStart = 10.dp,
                        topEnd = 10.dp
                    )
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            IconButton(onClick = {
                currentDate = currentDate.minusMonths(1)
            }) {
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowLeft,
                    tint = Color.White,
                    modifier = Modifier.size(18.dp),
                    contentDescription = null
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = currentDate.format(DateTimeFormatter.ofPattern("MMMM yyyy")),
                color = Color.White,
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            )
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = { currentDate = currentDate.plusMonths(1) }) {
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowRight,
                    tint = Color.White,
                    modifier = Modifier.size(18.dp),
                    contentDescription = null
                )
            }
        }


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Orange.copy(0.3f))
                .padding(4.dp)
        ) {
            val dayHeaders = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
            dayHeaders.forEach { day ->
                Text(
                    text = day,
                    color = Color.Black,
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .weight(1f)
                        .padding(4.dp)
                )
            }
        }


        val firstDayOfWeek = currentDate.withDayOfMonth(1).dayOfWeek.plus(1).value

        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .background(Orange.copy(0.3f))
        ) {
            val lastDaysOfPrevMonth = getDaysInMonth(currentDate.minusMonths(1)).takeLast(firstDayOfWeek - 1)
            lastDaysOfPrevMonth.forEach { day ->
                item {

                    Text(
                        text = day.toString(),
                        color = Color.Gray,
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        ),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .width(30.dp)
                            .height(30.dp)
                            .padding(horizontal = 8.dp)
                            .padding(vertical = 2.dp)
                            .clip(RoundedCornerShape(10.dp))
                    )
                }
            }

            // Display the days of the month
            items(getDaysInMonth(currentDate)) { day ->
                val containerColor = if (bookedDates.contains(currentDate.withDayOfMonth(day))) {
                    Color(0xffFFA6A6)
                } else if (availableDates.contains(currentDate.withDayOfMonth(day))) {
                    Color(0xff9FFFB4)
                } else {
                    Color.Transparent
                }

                Text(
                    text = day.toString(),
                    color = Color.Black,
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .width(30.dp)
                        .height(30.dp)
                        .padding(horizontal = 8.dp)
                        .padding(vertical = 2.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(containerColor)
                )
            }

            val remainingDays = 7 - (lastDaysOfPrevMonth.size + getDaysInMonth(currentDate).size) % 7
            val firstDaysOfNextMonth = getDaysInMonth(currentDate.plusMonths(1)).take(remainingDays)
            firstDaysOfNextMonth.forEach { day ->
                item {

                    Text(
                        text = day.toString(),
                        color = Color.Gray,
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        ),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .width(30.dp)
                            .height(30.dp)
                            .padding(horizontal = 8.dp)
                            .padding(vertical = 2.dp)
                            .clip(RoundedCornerShape(10.dp))

                    )
                }
            }
        }
    }

}


fun getDaysInMonth(date: LocalDate): List<Int> {
    val firstDay = date.withDayOfMonth(1)
    val lastDay = date.withDayOfMonth(date.lengthOfMonth())
    val days = mutableListOf<Int>()

    var currentDay = firstDay
    while (currentDay.isBefore(lastDay) || currentDay.isEqual(lastDay)) {
        days.add(currentDay.dayOfMonth)
        currentDay = currentDay.plusDays(1)
    }

    return days
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StaycationManagerAdditionalInformationCard(withEditButton: Boolean = false, modifier: Modifier = Modifier, staycationManagerViewModel: StaycationManagerViewModel){

    val phoneNo by remember{ mutableStateOf(staycationManagerViewModel.staycation.value?.phoneNo) }
    val email by remember{ mutableStateOf(staycationManagerViewModel.staycation.value?.email) }
    val noReschedule by remember{ mutableStateOf(staycationManagerViewModel.staycation.value?.noReschedule) }
    val noCancel by remember{ mutableStateOf(staycationManagerViewModel.staycation.value?.noCancel) }
    val hasSecurityCamera by remember{ mutableStateOf(staycationManagerViewModel.staycation.value?.hasSecurityCamera) }
    val hasFirstAid by remember{ mutableStateOf(staycationManagerViewModel.staycation.value?.hasFirstAid) }
    val hasFireExit by remember{ mutableStateOf(staycationManagerViewModel.staycation.value?.hasFireExit) }
    val hasFireExtinguisher by remember{ mutableStateOf(staycationManagerViewModel.staycation.value?.hasFireExtinguisher) }
    val maxGuest by remember{ mutableStateOf(staycationManagerViewModel.staycation.value?.maxNoOfGuests) }
    val noisePolicy by remember{ mutableStateOf(staycationManagerViewModel.staycation.value?.noisePolicy) }
    val guestCount by remember{ mutableStateOf(staycationManagerViewModel.staycation.value?.noOfGuests) }
    val allowPets by remember{ mutableStateOf(staycationManagerViewModel.staycation.value?.allowPets) }
    val allowSmoking by remember{ mutableStateOf(staycationManagerViewModel.staycation.value?.allowSmoking) }
    val additionalInfo by remember{ mutableStateOf(staycationManagerViewModel.staycation.value?.additionalInfo) }
    val staycationDetails by remember{ mutableStateOf(staycationManagerViewModel.staycation.value?.staycationDescription) }
    val openHouseRulesModal = remember { mutableStateOf(false)}
    val openHealthAndSafety = remember { mutableStateOf(false)}
    val openCancelAndReschedule = remember { mutableStateOf(false)}
    val openBusinessInformation = remember { mutableStateOf(false)}
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp
        ),
        modifier = modifier
            .fillMaxWidth()
    ) {

        Column(
            modifier = Modifier
                //                .padding(horizontal = 25.dp, vertical = 12.dp),
                .padding(
                    horizontal = 25.dp,
                    vertical = 20.dp // 12
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = "Additional information",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .padding(bottom = 4.dp)
                )
                if (withEditButton) {
                    Spacer(modifier = Modifier.width(8.dp))
                    AppOutlinedButtonWithBadge(
                        buttonLabel = "Edit",
                        modifier = Modifier
                            .width(40.dp)
                    )
                }
            }
            AdditionalInformationRow(textInfo = "House Rules", onClick = {openHouseRulesModal.value = true})
            AdditionalInformationRow(textInfo = "Health & safety", onClick = {openHealthAndSafety.value = true})
            AdditionalInformationRow(textInfo = "Cancellation & reschedule policy", onClick = {openCancelAndReschedule.value = true})
            AdditionalInformationRow(textInfo = "Business Information", onClick = {openBusinessInformation.value = true})

        }
    }

    if(openHouseRulesModal.value){
        ModalBottomSheet(
            shape = RoundedCornerShape(20.dp),
            containerColor = Color.White,
            dragHandle = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier
                        .padding(start = 3.dp, end = 16.dp) //, top = 3.dp
                        .fillMaxWidth()
                ) {
                    IconButton(
                        onClick = { openHouseRulesModal.value = false },
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Close"
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))

                }
            },
            onDismissRequest = { openHouseRulesModal.value = false },
            sheetState = bottomSheetState,
            modifier = Modifier
                .fillMaxHeight(0.8f) //0.693
        ) {

            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                item {
                    Text(
                        text = "House Rules",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 16.dp)
                            .fillMaxWidth()
                    )
                }
                item {

                    Text(
                        text = "Check-in/Check-out",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(start = 16.dp, end = 16.dp)
                            .fillMaxWidth()
                    )

                    Text(
                        text = "Check-in time is at 2pm, and check-out time is at 12 PM.",
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                            .padding(start = 16.dp, end = 16.dp)
                            .fillMaxWidth()
                    )
                    Divider(
                        color = Color(0xFFDEDEDE),
                        modifier = Modifier.padding(vertical = 10.dp, horizontal = 10.dp)
                    )
                }
                item {

                    if (noisePolicy!!) {
                        Text(
                            text = "Noise Policy",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .padding(start = 16.dp, end = 16.dp)
                                .fillMaxWidth()
                        )

                        Text(
                            text = "Respect quiet hours from 10 PM to 7 AM. Keep noise levels to a minimum during this period to ensure a peaceful environment for all guests.",
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier
                                .padding(start = 16.dp, end = 16.dp)
                                .fillMaxWidth()
                        )
                        Divider(
                            color = Color(0xFFDEDEDE),
                            modifier = Modifier.padding(vertical = 10.dp, horizontal = 10.dp)
                        )

                    }
                }
                item {

                    if (allowSmoking!!) {
                        Text(
                            text = "Smoking Policy",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .padding(start = 16.dp, end = 16.dp)
                                .fillMaxWidth()
                        )

                        Text(
                            text = "Smoking is allowed inside the accommodations. Please make sure to dispose cigarette butts properly.",
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier
                                .padding(start = 16.dp, end = 16.dp)
                                .fillMaxWidth()
                        )
                        Divider(
                            color = Color(0xFFDEDEDE),
                            modifier = Modifier.padding(vertical = 10.dp, horizontal = 10.dp)
                        )

                    } else {
                        Text(
                            text = "No Smoking",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .padding(start = 16.dp, end = 16.dp)
                                .fillMaxWidth()
                        )

                        Text(
                            text = "Smoking is strictly prohibited inside the accommodations. Please use designated outdoor smoking areas and dispose of cigarette butts responsibly.",
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier
                                .padding(start = 16.dp, end = 16.dp)
                                .fillMaxWidth()
                        )
                        Divider(
                            color = Color(0xFFDEDEDE),
                            modifier = Modifier.padding(vertical = 10.dp, horizontal = 10.dp)
                        )
                    }
                }
                item {

                    if (allowPets!!) {
                        Text(
                            text = "Pet Policy",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .padding(start = 16.dp, end = 16.dp)
                                .fillMaxWidth()
                        )

                        Text(
                            text = "Pets are allowed. Please make sure to clean after your pets. Any damage caused by your pet on the property will be subject to repair fees",
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier
                                .padding(start = 16.dp, end = 16.dp)
                                .fillMaxWidth()
                        )
                        Divider(
                            color = Color(0xFFDEDEDE),
                            modifier = Modifier.padding(vertical = 10.dp, horizontal = 10.dp)
                        )

                    } else {
                        Text(
                            text = "Pet Policy",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .padding(start = 16.dp, end = 16.dp)
                                .fillMaxWidth()
                        )

                        Text(
                            text = "Pets are not allowed.",
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier
                                .padding(start = 16.dp, end = 16.dp)
                                .fillMaxWidth()
                        )
                        Divider(
                            color = Color(0xFFDEDEDE),
                            modifier = Modifier.padding(vertical = 10.dp, horizontal = 10.dp)
                        )
                    }
                }
                item {

                    if (maxGuest!! != 0) {
                        Text(
                            text = "Guest Limit",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .padding(start = 16.dp, end = 16.dp)
                                .fillMaxWidth()
                        )

                        Text(
                            text = "The maximum number of guests allowed per accommodation is $guestCount up to $maxGuest, subject for additional fees. Please adhere to this limit for safety and comfort reasons.",
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier
                                .padding(start = 16.dp, end = 16.dp)
                                .fillMaxWidth()
                        )
                        Divider(
                            color = Color(0xFFDEDEDE),
                            modifier = Modifier.padding(vertical = 10.dp, horizontal = 10.dp)
                        )

                    } else {
                        Text(
                            text = "Guest Limit",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .padding(start = 16.dp, end = 16.dp)
                                .fillMaxWidth()
                        )

                        Text(
                            text = "The maximum number of guests allowed per accommodation is $guestCount. Please adhere to this limit for safety and comfort reasons.",
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier
                                .padding(start = 16.dp, end = 16.dp)
                                .fillMaxWidth()
                        )
                        Divider(
                            color = Color(0xFFDEDEDE),
                            modifier = Modifier.padding(vertical = 10.dp, horizontal = 10.dp)
                        )
                    }
                }
                item {

                    Text(
                        text = "Respect for Property",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(start = 16.dp, end = 16.dp)
                            .fillMaxWidth()
                    )

                    Text(
                        text = "Treat the accommodations and amenities with care and respect. Any damages caused by guests will be subject to repair charges.",
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                            .padding(start = 16.dp, end = 16.dp)
                            .fillMaxWidth()
                    )
                    Divider(
                        color = Color(0xFFDEDEDE),
                        modifier = Modifier.padding(vertical = 10.dp, horizontal = 10.dp)
                    )
                }
                item {

                    Text(
                        text = "Security Measures",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(start = 16.dp, end = 16.dp)
                            .fillMaxWidth()
                    )

                    Text(
                        text = "Ensure that doors and windows are securely locked when leaving the accommodation unattended. Report any suspicious activity to management immediately.",
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                            .padding(start = 16.dp, end = 16.dp)
                            .fillMaxWidth()
                    )
                    Divider(
                        color = Color(0xFFDEDEDE),
                        modifier = Modifier.padding(vertical = 10.dp, horizontal = 10.dp)
                    )
                }
                item {

                    Text(
                        text = "Community Guidelines",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(start = 16.dp, end = 16.dp)
                            .fillMaxWidth()
                    )

                    Text(
                        text = "Be courteous and considerate towards other guests and residents. Refrain from disruptive behavior that may disturb the peace and enjoyment of others.",
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                            .padding(start = 16.dp, end = 16.dp)
                            .fillMaxWidth()
                    )
                    Divider(
                        color = Color(0xFFDEDEDE),
                        modifier = Modifier.padding(vertical = 10.dp, horizontal = 10.dp)
                    )
                }
                item {

                    Text(
                        text = "Compliance with Local Laws",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(start = 16.dp, end = 16.dp)
                            .fillMaxWidth()
                    )

                    Text(
                        text = "Guests are expected to comply with all local laws and regulations during their stay.",
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                            .padding(start = 16.dp, end = 16.dp)
                            .fillMaxWidth()
                    )
                    Divider(
                        color = Color(0xFFDEDEDE),
                        modifier = Modifier.padding(vertical = 10.dp, horizontal = 10.dp)
                    )

                }

                item {
                    if(additionalInfo != "") {

                        Text(
                            text = "Additional Information",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .padding(start = 16.dp, end = 16.dp)
                                .fillMaxWidth()
                        )

                        Text(
                            text = additionalInfo!!,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier
                                .padding(start = 16.dp, end = 16.dp)
                                .fillMaxWidth()
                        )
                        Divider(
                            color = Color(0xFFDEDEDE),
                            modifier = Modifier.padding(vertical = 10.dp, horizontal = 10.dp)
                        )

                    }
                    Spacer(modifier = Modifier.weight(1f))

                    Spacer(modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars))
                }


            }
        }


    }


    if(openHealthAndSafety.value){
        ModalBottomSheet(
            shape = RoundedCornerShape(20.dp),
            containerColor = Color.White,
            dragHandle = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier
                        .padding(start = 3.dp, end = 16.dp) //, top = 3.dp
                        .fillMaxWidth()
                ) {
                    IconButton(
                        onClick = { openHealthAndSafety.value = false },
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Close"
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                }
            },
            onDismissRequest = { openHealthAndSafety.value = false },
            sheetState = bottomSheetState,
            modifier = Modifier
                .fillMaxHeight(0.8f) //0.693
        ) {

            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                item {
                    Text(
                        text = "Health & Safety",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 16.dp)
                            .fillMaxWidth()
                    )
                }
                item {

                    Text(
                        text = "Sanitation Protocol",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(start = 16.dp, end = 16.dp)
                            .fillMaxWidth()
                    )

                    Text(
                        text = "Accommodations are thoroughly cleaned and sanitized between guest stays.",
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                            .padding(start = 16.dp, end = 16.dp)
                            .fillMaxWidth()
                    )
                    Divider(
                        color = Color(0xFFDEDEDE),
                        modifier = Modifier.padding(vertical = 10.dp, horizontal = 10.dp)
                    )
                }
                item {

                    if (hasFirstAid!!) {
                        Text(
                            text = "First Aid Kit",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .padding(start = 16.dp, end = 16.dp)
                                .fillMaxWidth()
                        )

                        Text(
                            text = "First aid kits are available in designated areas.",
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier
                                .padding(start = 16.dp, end = 16.dp)
                                .fillMaxWidth()
                        )
                        Divider(
                            color = Color(0xFFDEDEDE),
                            modifier = Modifier.padding(vertical = 10.dp, horizontal = 10.dp)
                        )

                    }
                }
                item {

                    if (hasFireExit!!) {
                        Text(
                            text = "Fire Safety Instructions",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .padding(start = 16.dp, end = 16.dp)
                                .fillMaxWidth()
                        )

                        Text(
                            text = "Evacuation plan are posted on designated areas. Follow marked exits in case of fire alarm.",
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier
                                .padding(start = 16.dp, end = 16.dp)
                                .fillMaxWidth()
                        )
                        Divider(
                            color = Color(0xFFDEDEDE),
                            modifier = Modifier.padding(vertical = 10.dp, horizontal = 10.dp)
                        )

                    }
                }
                item {

                    if (hasFireExtinguisher!!) {
                        Text(
                            text = "Fire Extinguisher",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .padding(start = 16.dp, end = 16.dp)
                                .fillMaxWidth()
                        )

                        Text(
                            text = "Fire extinguishers are available at designated areas. Please use only in case of a fire.",
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier
                                .padding(start = 16.dp, end = 16.dp)
                                .fillMaxWidth()
                        )
                        Divider(
                            color = Color(0xFFDEDEDE),
                            modifier = Modifier.padding(vertical = 10.dp, horizontal = 10.dp)
                        )

                    }
                }

                item {
                    if(hasSecurityCamera!!) {
                        Text(
                            text = "Security Measures",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .padding(start = 16.dp, end = 16.dp)
                                .fillMaxWidth()
                        )

                        Text(
                            text = "Security cameras are in place. Please use provided locks and security devices for safety.",
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier
                                .padding(start = 16.dp, end = 16.dp)
                                .fillMaxWidth()
                        )
                        Divider(
                            color = Color(0xFFDEDEDE),
                            modifier = Modifier.padding(vertical = 10.dp, horizontal = 10.dp)
                        )
                        Spacer(modifier = Modifier.weight(1f))

                        Spacer(modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars))
                    }
                    else{
                        Text(
                            text = "Security Measures",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .padding(start = 16.dp, end = 16.dp)
                                .fillMaxWidth()
                        )

                        Text(
                            text = "Please use provided locks and security devices for safety.",
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier
                                .padding(start = 16.dp, end = 16.dp)
                                .fillMaxWidth()
                        )
                        Divider(
                            color = Color(0xFFDEDEDE),
                            modifier = Modifier.padding(vertical = 10.dp, horizontal = 10.dp)
                        )
                        Spacer(modifier = Modifier.weight(1f))

                        Spacer(modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars))
                    }
                }

            }
        }


    }

    if(openCancelAndReschedule.value){
        ModalBottomSheet(
            shape = RoundedCornerShape(20.dp),
            containerColor = Color.White,
            dragHandle = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier
                        .padding(start = 3.dp, end = 16.dp) //, top = 3.dp
                        .fillMaxWidth()
                ) {
                    IconButton(
                        onClick = { openCancelAndReschedule.value = false },
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Close"
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                }
            },
            onDismissRequest = { openCancelAndReschedule.value = false },
            sheetState = bottomSheetState,
            modifier = Modifier
                .fillMaxHeight(0.8f) //0.693
        ) {

            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                item {
                    Text(
                        text = "Cancellation and Rescheduling Policy",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 16.dp)
                            .fillMaxWidth()
                    )
                }

                item {

                    if (noCancel!!) {
                        Text(
                            text = "Cancellation Policy",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .padding(start = 16.dp, end = 16.dp)
                                .fillMaxWidth()
                        )

                        Text(
                            text = "Guests can only cancel 14 days before the booked appointment for a full refund. Cancellations are not allowed for anytime shorter than 14 days",
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier
                                .padding(start = 16.dp, end = 16.dp)
                                .fillMaxWidth()
                        )
                        Divider(
                            color = Color(0xFFDEDEDE),
                            modifier = Modifier.padding(vertical = 10.dp, horizontal = 10.dp)
                        )

                    } else{
                        Text(
                            text = "Cancellation Policy",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .padding(start = 16.dp, end = 16.dp)
                                .fillMaxWidth()
                        )

                        Text(
                            text = "Guests can only cancel 14 days before the booked appointment for a full refund. For cancellations 7 days before appointed date, an 80% refund will be given. Cancellations are not allowed for anything shorter than 7 days",
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier
                                .padding(start = 16.dp, end = 16.dp)
                                .fillMaxWidth()
                        )
                        Divider(
                            color = Color(0xFFDEDEDE),
                            modifier = Modifier.padding(vertical = 10.dp, horizontal = 10.dp)
                        )
                    }
                }
                item {

                    if (noReschedule!!) {
                        Text(
                            text = "Rescheduling Policy",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .padding(start = 16.dp, end = 16.dp)
                                .fillMaxWidth()
                        )

                        Text(
                            text = "Guests can only reschedule 14 days before the booked appointment. Rescheduling are not allowed for anytime shorter than 14 days",
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier
                                .padding(start = 16.dp, end = 16.dp)
                                .fillMaxWidth()
                        )
                        Divider(
                            color = Color(0xFFDEDEDE),
                            modifier = Modifier.padding(vertical = 10.dp, horizontal = 10.dp)
                        )

                    } else{
                        Text(
                            text = "Rescheduling Policy",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .padding(start = 16.dp, end = 16.dp)
                                .fillMaxWidth()
                        )

                        Text(
                            text = "Guests can reschedule 7 days before the booked appointment. Rescheduling are not allowed for anytime shorter than 7 days",
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier
                                .padding(start = 16.dp, end = 16.dp)
                                .fillMaxWidth()
                        )
                        Divider(
                            color = Color(0xFFDEDEDE),
                            modifier = Modifier.padding(vertical = 10.dp, horizontal = 10.dp)
                        )
                        Spacer(modifier = Modifier.weight(1f))

                        Spacer(modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars))
                    }
                }


            }
        }


    }

    if(openBusinessInformation.value){
        ModalBottomSheet(
            shape = RoundedCornerShape(20.dp),
            containerColor = Color.White,
            dragHandle = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier
                        .padding(start = 3.dp, end = 16.dp) //, top = 3.dp
                        .fillMaxWidth()
                ) {
                    IconButton(
                        onClick = { openBusinessInformation.value = false },
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Close"
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                }
            },
            onDismissRequest = { openBusinessInformation.value = false },
            sheetState = bottomSheetState,
            modifier = Modifier
                .fillMaxHeight(0.8f) //0.693
        ) {

            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                item {
                    Text(
                        text = "Business Information",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 16.dp)
                            .fillMaxWidth()
                    )
                }
                item {

                    Text(
                        text = "About Us",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(start = 16.dp, end = 16.dp)
                            .fillMaxWidth()
                    )

                    Text(
                        text = staycationDetails!!,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                            .padding(start = 16.dp, end = 16.dp)
                            .fillMaxWidth()
                    )
                    Divider(
                        color = Color(0xFFDEDEDE),
                        modifier = Modifier.padding(vertical = 10.dp, horizontal = 10.dp)
                    )
                }
                item {

                    Text(
                        text = "Contact Information",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(start = 16.dp, end = 16.dp)
                            .fillMaxWidth()
                    )

                    Text(
                        text = "Phone: $phoneNo",
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                            .padding(start = 16.dp, end = 16.dp)
                            .fillMaxWidth()
                    )
                    Text(
                        text = "Email: $email",
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                            .padding(start = 16.dp, end = 16.dp)
                            .fillMaxWidth()
                    )
                    Divider(
                        color = Color(0xFFDEDEDE),
                        modifier = Modifier.padding(vertical = 10.dp, horizontal = 10.dp)
                    )
                    Spacer(modifier = Modifier.weight(1f))

                    Spacer(modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars))

                }

            }
        }

    }
}



@Preview
@Composable
private fun StaycationManagerPreview() {


}


@Preview
@Composable
private fun StaycationManagerScreenPreview() {

    val staycationManagerViewModel = viewModel(modelClass = StaycationManagerViewModel::class.java)
    val staycationId = "LxpNxRFdwkQzBxujF3gx"

//
//    LaunchedEffect(staycationId) {
//        staycationManagerViewModel.getSelectedStaycation(staycationId)
//    }

  //  AvailabilityAndPricingCard(staycationManagerViewModel)

    StaycationManagerScreen(
        hostId = "HOST-ITZbCFfF7Fzqf1qPBiwx",
        staycationId = "33014",
        staycationManagerViewModel = staycationManagerViewModel,
        onNavToEditStaycation = { param1, param2, param3 ->
            // TODO: Implement the function body here
        },
        onNavToDashboard = {

        }
    )

}