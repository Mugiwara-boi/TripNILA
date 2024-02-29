package com.example.tripnila.screens

import android.util.Log
import androidx.compose.foundation.BorderStroke
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
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

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

    var staycation = staycationManagerViewModel?.staycation?.collectAsState()
    val touristId = hostId.substring(5)

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
        if (staycationManagerViewModel?.isStateRetrieved?.collectAsState()?.value == false) {
            LoadingScreen(isLoadingCompleted = false, isLightModeActive = true)
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                item {
                    Box(
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
                    }
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
                    StaycationAdditionalInformationCard(
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

@Composable
fun AppTransactionsCard(transactions: List<Transaction>, modifier: Modifier = Modifier){
    Box(
        modifier = modifier
            .fillMaxWidth()
            //.height(height = 110.dp)
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

                },
                modifier = Modifier
                    .padding(top = 12.dp)
            )

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
                IconButton(
                    onClick = { /*TODO*/ },
                    modifier = Modifier
                        .size(36.dp)
                        .align(Alignment.End)
                        .offset(x = 10.dp, y = (-7).dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.KeyboardArrowRight,
                        contentDescription = "Expand",
                        tint = Color(0xFF999999),
                        modifier = Modifier.size(36.dp)
                    )
                }
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
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(20.dp),
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
fun StaycationManagerAdditionalInformationCard(withEditButton: Boolean = false, modifier: Modifier = Modifier){

    val openHouseRulesModal = remember { mutableStateOf(false)}
    val openHealthSafetyModal = remember { mutableStateOf(false)}
    val openCancellationModal = remember { mutableStateOf(false)}
    val openBusinessInformationModal = remember { mutableStateOf(false)}
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
            AdditionalInformationRow(textInfo = "Health & safety", onClick = {openHealthSafetyModal.value = true})
            AdditionalInformationRow(textInfo = "Cancellation & reschedule policy", onClick = {openCancellationModal.value = true})
            AdditionalInformationRow(textInfo = "Business Information", onClick = {openBusinessInformationModal.value = true})

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

    if(openHealthSafetyModal.value){
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

    if(openCancellationModal.value){
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

    if(openBusinessInformationModal.value){
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



@Preview
@Composable
private fun StaycationManagerPreview() {


}


@Preview
@Composable
private fun StaycationManagerScreenPreview() {

    val staycationManagerViewModel = viewModel(modelClass = StaycationManagerViewModel::class.java)
    var staycationId = "LxpNxRFdwkQzBxujF3gx"


    LaunchedEffect(staycationId) {
        staycationManagerViewModel.getSelectedStaycation(staycationId)
    }

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