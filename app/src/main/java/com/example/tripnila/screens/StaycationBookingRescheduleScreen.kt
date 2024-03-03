package com.example.tripnila.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.tripnila.common.AppConfirmAndPayDivider
import com.example.tripnila.common.Orange
import com.example.tripnila.data.Host
import com.example.tripnila.model.DetailViewModel
import com.example.tripnila.model.TouristWalletViewModel
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId


@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun StaycationBookingRescheduleScreen(
    touristId: String,
    staycationBookingId: String,
    detailViewModel: DetailViewModel,
    onBack: () -> Unit,
    touristWalletViewModel: TouristWalletViewModel,
){

    val staycationBooking = detailViewModel.staycationBooking.collectAsState()
    val staycation = detailViewModel.staycation.collectAsState()
    val duration = detailViewModel.nightsDifference.collectAsState()
    val selectedStart = detailViewModel.startDate.collectAsState()
    val selectedEnd = detailViewModel.endDate.collectAsState()
    val guestCount = detailViewModel.guestCount.collectAsState()
    val initialNightsCount = detailViewModel.initialNightsDifference.collectAsState()

    val dateRangePickerState = rememberDateRangePickerState()

    //var totalOccupancyLimit = staycation?.value?.noOfGuests
    val infantOccupancyLimit = 5 // /*TODO*/
    val petOccupancyLimit = 0 // /*TODO*/
    val tripnilaFee by touristWalletViewModel.tripnilaFee.collectAsState()
    val host = staycation.value?.host?: Host()
    val hostId = host.hostId
    val hostWalletId = hostId.removePrefix("HOST-")
    val totalFee by touristWalletViewModel.totalFee.collectAsState()
    val initialTotalFee by touristWalletViewModel.initialTotalFee.collectAsState()
    val initialTripnilaFee by touristWalletViewModel.initialTripnilaFee.collectAsState()

    val coroutineScope = rememberCoroutineScope()
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val snackbarHostState = remember { SnackbarHostState() }
    val openCalendarBottomSheet = remember { mutableStateOf(false) }
    val openGuestBottomSheet = remember { mutableStateOf(false) }
    val isSaveButtonClicked = remember { mutableStateOf(true) }

    val titleText: MutableState<String?> = remember { mutableStateOf(null) }
    val enableBottomSaveButton: MutableState<Boolean> = remember { mutableStateOf(true) }
    val openAlertDialog = remember { mutableStateOf(false) }

    val nights = remember { mutableStateOf(0) }
    val hasNavigationBar = WindowInsets.areNavigationBarsVisible


    touristWalletViewModel.getHostWallet(hostWalletId)
//    touristWalletViewModel.setWallet(touristId)


    LaunchedEffect(staycationBookingId) {
        detailViewModel.getStaycationBookingByBookingId(staycationBookingId)
    }

    LaunchedEffect(selectedEnd.value) {
        if (selectedEnd.value != null) {
            dateRangePickerState.setSelection(selectedStart.value, selectedEnd.value)
            detailViewModel.setAdultCount(guestCount.value)

            Log.d("Selected End Inside", selectedEnd.value.toString())
        }


        Log.d("Selected End", selectedEnd.value.toString())
    }

    LaunchedEffect(detailViewModel.bookingResult.collectAsState().value) {
        if (detailViewModel.bookingResult.value != null) {
            snackbarHostState.showSnackbar(detailViewModel.bookingResult.value!!)
        }

    }


    LaunchedEffect(
        dateRangePickerState.selectedStartDateMillis,
        dateRangePickerState.selectedEndDateMillis
    ) {
        val countNights = if (dateRangePickerState.selectedStartDateMillis != null && dateRangePickerState.selectedEndDateMillis != null) {
            calculateNights(dateRangePickerState.selectedStartDateMillis!!,
                dateRangePickerState.selectedEndDateMillis!!
            )
        } else {
            null
        }

        val todayMillis = System.currentTimeMillis()

        if (dateRangePickerState.selectedStartDateMillis != null && dateRangePickerState.selectedStartDateMillis!! < todayMillis) {
            dateRangePickerState.setSelection(startDateMillis = null, endDateMillis = null)
        }

        if (countNights != null && countNights <= 0) {
            dateRangePickerState.setSelection(startDateMillis = null, endDateMillis = null)
        }

        Log.d("Start:", dateRangePickerState.selectedStartDateMillis.toString())

        Log.d("End:", dateRangePickerState.selectedEndDateMillis.toString())

        titleText.value = countNights?.let { "$it nights" } ?: "Select date"
        nights.value = countNights?.toInt() ?: 0
        enableBottomSaveButton.value = countNights != null && countNights > 0
        detailViewModel.setNightsDifference(countNights)
        detailViewModel.setStartDate(dateRangePickerState.selectedStartDateMillis)
        detailViewModel.setEndDate(dateRangePickerState.selectedEndDateMillis)

    }

    LaunchedEffect(
        openCalendarBottomSheet.value,
        isSaveButtonClicked.value
    ){
        if (!openCalendarBottomSheet.value && !isSaveButtonClicked.value) {
            dateRangePickerState.setSelection(startDateMillis = null, endDateMillis = null)
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                AsyncImage(
                    model = staycation.value?.staycationImages?.find { it.photoType == "Cover" }?.photoUrl ?: "https://upload.wikimedia.org/wikipedia/commons/thumb/3/3f/Placeholder_view_vector.svg/1022px-Placeholder_view_vector.svg.png",
                    contentDescription = "Staycation Image",
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier.fillMaxWidth()
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .background(color = Color(0xff1a1a1a).copy(alpha = 0.42f))
                )
            }
            Box(
                modifier = Modifier
                    .padding(top = 160.dp) // 170
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .clip(shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                    .background(color = Color.White)
            ){
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            horizontal = 25.dp,
                            vertical = 20.dp // 12
                        )
                        .padding(it)
                        .background(Color.White)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Reschedule booking",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "Close",
                            tint = Color(0xFFCECECE),
                            modifier = Modifier
                                .size(30.dp)
                                .offset(x = 5.dp, y = (-5).dp)
                                .clickable {
                                    onBack()
                                }
                        )
                    }

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        item {
                            AppConfirmAndPayDivider(
                                image = staycation.value?.staycationImages?.find { it.photoType == "Cover" }?.photoUrl ?: "https://upload.wikimedia.org/wikipedia/commons/thumb/3/3f/Placeholder_view_vector.svg/1022px-Placeholder_view_vector.svg.png",
                                itinerary = staycation.value?.staycationTitle ?: "",
                                price = staycation.value?.staycationPrice ?: 0.0,
                                unit = "night"
                            )
                        }

                        item {
                            YourTripRescheduleDivider(
                                detailViewModel = detailViewModel,
                                onClickEditDate = {
                                    openCalendarBottomSheet.value = true
                                },
                                onClickEditGuest = {
                                    openGuestBottomSheet.value = true

                                }
                            )
                        }

                        item {
                            AppPaymentDivider(
                                touristId = touristId,
                                forRescheduling = true,
                                initialBookingDuration = initialNightsCount.value?.toInt() ?: 0,
                                detailViewModel = detailViewModel,
                                touristWalletViewModel = touristWalletViewModel,
                                bookingFee = staycation.value?.staycationPrice ?: 2500.00,
                                bookingDuration = duration.value?.toInt() ?: 5,
                                maintenanceFee = staycation.value?.staycationPrice?.times(0.10)
                                    ?: 250.00,
                                //  tripnilaFee = staycation?.value?.staycationPrice?.times(0.05) ?: 625.00
                            )
                        }
                        item {
                            PaymentAgreementText()
                            Spacer(modifier = Modifier.padding(bottom = 15.dp))
                        }

                    }


                    BookingFilledButton(
                        buttonText = "Confirm and pay",
                        onClick = {
                            detailViewModel.setAlertDialogMessage()
                            openAlertDialog.value = true

                        },
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                            .fillMaxWidth()
                    )
                }

            }
        }

        if (openCalendarBottomSheet.value) {
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
                            onClick = { openCalendarBottomSheet.value = false },
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
                                dateRangePickerState.setSelection(null, null)
                                isSaveButtonClicked.value = false
                            }
                        )
                    }
                },
                onDismissRequest = { openCalendarBottomSheet.value = false },
                sheetState = bottomSheetState,
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
                        state = dateRangePickerState,
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

                            val selectedDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(date), ZoneId.systemDefault()).toLocalDate()
                            val today = LocalDateTime.now().toLocalDate()


                            val adjustedDates = staycation.value?.availableDates?.map {
                                it.availableDate?.toDate()?.time?.plus(28800000) ?: 0
                            } ?: emptyList()

                            val adjustedCheckInDate =
                                staycationBooking.value?.checkInDate?.time?.minus(50400000)?.plus(28800000)
                            val adjustedCheckOutDate =
                                staycationBooking.value?.checkOutDate?.time?.minus(43200000)?.plus(28800000)


                            Log.d("adjustedCheckInDate", adjustedCheckInDate.toString())
                            Log.d("adjustedCheckOutDate", adjustedCheckOutDate.toString())

                            val daysBetweenDates = mutableListOf<Long>()

                            if (adjustedCheckInDate != null && adjustedCheckOutDate != null) {
                                var currentDate = adjustedCheckInDate
                                while (currentDate <= adjustedCheckOutDate) {
                                    daysBetweenDates.add(currentDate)
                                    currentDate += 86400000 // Adding 1 day to the current date
                                }
                            }

                            (adjustedDates.contains(date) || daysBetweenDates.contains(date)) && selectedDate.isAfter(today)

                        },
                    )

                    Spacer(modifier = Modifier.weight(1f))
                    ConfirmCalendar(
                        nights = nights.value,
                        staycation = staycation.value,
                        enableButton = enableBottomSaveButton.value,
                        onClickSave = {
                            isSaveButtonClicked.value = true
                            openCalendarBottomSheet.value = false
                        },
                        modifier = Modifier
                            .noPaddingIf(hasNavigationBar)
                    )
                    Spacer(modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars))
                }

            }
        }

        if (openGuestBottomSheet.value) {
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
                            onClick = { openGuestBottomSheet.value = false },
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

                                detailViewModel.setClearTrigger(!detailViewModel.clearTrigger.value)

                                detailViewModel.setAdultCount(1)
                                detailViewModel.setChildrenCount(0)
                                detailViewModel.setInfantCount(0)
                                detailViewModel.setPetCount(0)
                            }

                        )


                    }
                },
                onDismissRequest = { openGuestBottomSheet.value = false },
                sheetState = bottomSheetState,
                modifier = Modifier
                    .fillMaxHeight(0.8f) //0.693
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()

                ) {

                    val clearTrigger = detailViewModel.clearTrigger.collectAsState().value

                    GuestsCounter(
                        detailViewModel = detailViewModel,
                        label = "Adults",
                        count = detailViewModel.adultCount.collectAsState().value ?: 0,
                        onCountChange = { count -> detailViewModel.setAdultCount(count) },
                        totalOccupancyLimit = detailViewModel.staycation.collectAsState().value?.noOfGuests
                            ?: 0,
                        clearTrigger = clearTrigger,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    GuestsCounter(
                        detailViewModel = detailViewModel,
                        label = "Children",
                        count = detailViewModel.childrenCount.collectAsState().value ?: 0,
                        onCountChange = { count -> detailViewModel.setChildrenCount(count) },
                        totalOccupancyLimit = detailViewModel.staycation.collectAsState().value?.noOfGuests
                            ?: 0,
                        clearTrigger = clearTrigger,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    GuestsCounter(
                        detailViewModel = detailViewModel,
                        label = "Infant",
                        count =  detailViewModel.infantCount.collectAsState().value ?: 0,
                        onCountChange = { count -> detailViewModel.setInfantCount(count) },
                        totalOccupancyLimit = infantOccupancyLimit,
                        clearTrigger = clearTrigger,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    GuestsCounter(
                        detailViewModel = detailViewModel,
                        label = "Pets",
                        count =  detailViewModel.petCount.collectAsState().value ?: 0,
                        onCountChange = { count -> detailViewModel.setPetCount(count) },
                        totalOccupancyLimit = petOccupancyLimit,
                        clearTrigger = clearTrigger,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    Spacer(modifier = Modifier.weight(1f))
                    ConfirmGuest(
                        onClickSave = {
                            openGuestBottomSheet.value = false
                        },
                        enableButton = true,
                        detailViewModel = detailViewModel
                    )
                    Spacer(modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars))

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

                        //   var text =

                        Text(
                            text = detailViewModel.alertDialogMessage.collectAsState().value ?: "",
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
                            if (detailViewModel.alertDialogMessage.collectAsState().value != "Are you sure you want to proceed?") {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                            TextButton(
                                onClick = { openAlertDialog.value = false },
                                modifier = Modifier.padding(horizontal = 10.dp),
                            ) {
                                Text("Dismiss", color = Color.Black)
                            }
                            if (detailViewModel.alertDialogMessage.collectAsState().value == "Are you sure you want to proceed?") {
                                Spacer(modifier = Modifier.weight(1f))
                                TextButton(
                                    onClick = {

                                        coroutineScope.launch {
                                            openAlertDialog.value = false

                                            detailViewModel.rescheduleBooking()
                                            touristWalletViewModel.setReschedulePayment(totalFee,touristId,initialTotalFee)
                                            touristWalletViewModel.setReschedulePendingAmount(
                                                totalFee = totalFee,
                                                hostWalletId = hostWalletId,
                                                tripnilaFee = tripnilaFee,
                                                initialTotalFee = initialTotalFee,
                                                initialTripnilaFee = initialTripnilaFee,)
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
}

@Composable
fun YourTripRescheduleDivider(
    detailViewModel: DetailViewModel,
    modifier: Modifier = Modifier,
    onClickEditDate: () -> Unit,
    onClickEditGuest: () -> Unit
) {
    val formattedDateRange = detailViewModel.formattedDateRange.collectAsState().value
    val initialFormattedRange = detailViewModel.initialFormattedDateRange.collectAsState().value
    // val guestCount = detailViewModel.guestCount.collectAsState().value

    val guestCount = detailViewModel.guestCount.collectAsState().value
    val infantCount = detailViewModel.infantCount.collectAsState().value
    val petCount = detailViewModel.petCount.collectAsState().value

    val initialGuestCount = detailViewModel.initialGuestCount.collectAsState().value
    val initialInfantCount = detailViewModel.initialInfantCount.collectAsState().value
    val initialPetCount = detailViewModel.initialPetCount.collectAsState().value

    val initialInfoText = buildAnnotatedString {
        append("$initialGuestCount ${initialGuestCount?.let { detailViewModel.pluralize("guest", it) }}")

        if (initialInfantCount != null) {
            if (initialInfantCount > 0) {
                append(", $initialInfantCount ${detailViewModel.pluralize("infant", initialInfantCount)}")
            }
        }

        if (initialPetCount != null) {
            if (initialPetCount > 0) {
                append(", $initialPetCount ${detailViewModel.pluralize("pet", initialPetCount)}")
            }
        }
    }

    val guestInfoText = buildAnnotatedString {
        append("$guestCount ${guestCount?.let { detailViewModel.pluralize("guest", it) }}")

        if (infantCount != null) {
            if (infantCount > 0) {
                append(", $infantCount ${detailViewModel.pluralize("infant", infantCount)}")
            }
        }

        if (petCount != null) {
            if (petCount > 0) {
                append(", $petCount ${detailViewModel.pluralize("pet", petCount)}")
            }
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

        val dateRowText = formattedDateRange.takeIf { it?.isNotBlank() == true } ?: "Select dates*"
        val dateRowTextColor = if (formattedDateRange?.isNotBlank() == true) Color(0xFF999999) else Color.Red


        AppYourRescheduleTripRow(
            rowLabel = "Dates",
            rowText = dateRowText,
            initialValue = initialFormattedRange ?: "...",
            fontColor = dateRowTextColor,
            onClickEdit = {
                onClickEditDate()
            }
        )

        // Display a prompt if guestCount is null or zero
        val guestCountText = when {
            guestCount == null || guestCount <= 0 -> "Select guests*"
            else -> "$guestInfoText"
        }

        val guestColor = when {
            guestCount == null || guestCount <= 0 -> Color.Red
            else -> Color(0xFF999999)
        }
        AppYourRescheduleTripRow(
            rowLabel = "Guests",
            fontColor = guestColor,
            initialValue = if (guestCount == null || guestCount == 0) "..." else "$initialInfoText",
            rowText = guestCountText,
            onClickEdit = {
                onClickEditGuest()
            }
        )

    }
    Divider(
        color = Color(0xFF999999),
        modifier = Modifier.padding(top = 5.dp) // 5.dp
    )
}

@Composable
fun AppYourRescheduleTripRow(
    modifier: Modifier = Modifier,
    initialValue: String,
    rowLabel: String,
    rowText: String,
    fontColor: Color = Color(0xFF999999),
    onClickEdit: (() -> Unit?)? = null
){
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 5.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Text(
                text = rowLabel,
                fontWeight = FontWeight.SemiBold
            )
            Row {
                Text(
                    text = initialValue,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF999999).copy(alpha = .4f),
                    textDecoration = TextDecoration.LineThrough

                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = rowText,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = fontColor,

                )
            }
        }

        Text(
            text = "Edit",
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            textDecoration = TextDecoration.Underline,
            modifier = Modifier
                .padding(top = 8.dp)
                .clickable {
                    if (onClickEdit != null) {
                        onClickEdit()
                    }
                }
        )

    }
}


@Preview
@Composable
private fun StaycationBookingReschedulePreview() {

    val detailViewModel = viewModel(modelClass = DetailViewModel::class.java)

    StaycationBookingRescheduleScreen(
        touristId = "n7r1JjE18t5iCP32GXjt",
        staycationBookingId = "gNIKStc4vQf6d1maJcz4",
        onBack = { /*TODO*/ },
        touristWalletViewModel = TouristWalletViewModel(),
        detailViewModel = detailViewModel,
    )
}