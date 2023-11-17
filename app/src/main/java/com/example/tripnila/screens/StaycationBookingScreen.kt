package com.example.tripnila.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tripnila.R
import com.example.tripnila.common.AppConfirmAndPayDivider
import com.example.tripnila.common.AppYourTripRow
import com.example.tripnila.common.Orange
import com.example.tripnila.data.Staycation
import com.example.tripnila.model.DetailViewModel
import kotlinx.coroutines.flow.firstOrNull
import java.text.DecimalFormat
import java.text.NumberFormat

@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun StaycationBookingScreen(
    touristId: String,
    staycationId: String,
    detailViewModel: DetailViewModel? = null,
){

    var staycation = detailViewModel?.staycation?.collectAsState()
    var duration = detailViewModel?.nightsDifference?.collectAsState()
    var selectedStart = detailViewModel?.startDate?.collectAsState()
    var selectedEnd = detailViewModel?.endDate?.collectAsState()

    //var totalOccupancyLimit = staycation?.value?.noOfGuests
    var infantOccupancyLimit = 5 // /*TODO*/
    var petOccupancyLimit = 0 // /*TODO*/


    val dateRangePickerState = rememberDateRangePickerState()

    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    var openCalendarBottomSheet = remember { mutableStateOf(false) }
    var openGuestBottomSheet = remember { mutableStateOf(false) }
    var isSaveButtonClicked = remember { mutableStateOf(true) }
    var isClearButtonClicked = remember { mutableStateOf(false) }
    var titleText: MutableState<String?> = remember { mutableStateOf(null) }
    var enableBottomSaveButton: MutableState<Boolean> = remember { mutableStateOf(true) }

    var nights = remember { mutableStateOf(0) }
    var hasNavigationBar = WindowInsets.areNavigationBarsVisible

    var isInitial = remember {
        mutableStateOf(true)
    }

    var isSaveGuestClicked = remember { mutableStateOf(false) }


    LaunchedEffect(touristId) {
        // Set initial values for dateRangePickerState
        dateRangePickerState.setSelection(selectedStart?.value, selectedEnd?.value)
        detailViewModel?.setAdultCount(1)
        isInitial.value = true

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

        isSaveButtonClicked.value = false

        titleText.value = countNights?.let { "$it nights" } ?: "Select date"
        nights.value = countNights?.toInt() ?: 0
        enableBottomSaveButton.value = countNights != null && countNights > 0
        detailViewModel?.setNightsDifference(countNights)
        detailViewModel?.setStartDate(dateRangePickerState.selectedStartDateMillis)
        detailViewModel?.setEndDate(dateRangePickerState.selectedEndDateMillis)

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

        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.staycation1),
                    contentDescription = "Staycation Image",
                    contentScale = ContentScale.FillWidth
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
                    .padding(top = 170.dp) // 160
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
                    Text(
                        text = "Confirm and pay",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                    AppConfirmAndPayDivider(
                        image = R.drawable.staycation1, // PLACEHOLDER
                        itinerary = staycation?.value?.staycationTitle ?: "",
                        price = staycation?.value?.staycationPrice ?: 0.0,
                        unit = "night"
                    )
                    detailViewModel?.let { detailViewModel ->
                        YourTripDivider(
                            detailViewModel = detailViewModel,
                            onClickEditDate = {
                                openCalendarBottomSheet.value = true
                            },
                            onClickEditGuest = {
                                openGuestBottomSheet.value = true

                            }
                        )
                    }
                    AppPaymentDivider(
                        bookingFee = staycation?.value?.staycationPrice ?: 2500.00,
                        bookingDuration = duration?.value?.toInt() ?: 5,
                        maintenanceFee = staycation?.value?.staycationPrice?.times(0.02) ?: 250.00,
                        tripnilaFee = staycation?.value?.staycationPrice?.times(0.05) ?: 625.00

                    )
                    PaymentAgreementText()
                    Spacer(modifier = Modifier.padding(vertical = 15.dp))
                    BookingFilledButton(
                        buttonText = "Confirm and pay",
                        onClick = {

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
                            val adjustedDates = staycation?.value?.availableDates?.map {
                                it.availableDate?.toDate()?.time?.plus(28800000) ?: 0
                            } ?: emptyList()

                            val threshold = 60000
                            adjustedDates.any { Math.abs(date - it) <= threshold }
                        },
                    )

                    Spacer(modifier = Modifier.weight(1f))
                    ConfirmCalendar(
                        nights = nights.value,
                        staycation = staycation?.value,
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

                                detailViewModel?.setClearTrigger(!detailViewModel?.clearTrigger?.value!!)

                                detailViewModel?.setAdultCount(1)
                                detailViewModel?.setChildrenCount(0)
                                detailViewModel?.setInfantCount(0)
                                detailViewModel?.setPetCount(0)
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

                    val clearTrigger = detailViewModel?.clearTrigger?.collectAsState()?.value

                    if (detailViewModel != null) {
                        if (clearTrigger != null) {
                            GuestsCounter(
                                detailViewModel = detailViewModel,
                                label = "Adults",
                                count = detailViewModel?.adultCount?.collectAsState()?.value ?: 0,
                                onCountChange = { count -> detailViewModel?.setAdultCount(count) },
                                totalOccupancyLimit = detailViewModel.staycation.collectAsState().value?.noOfGuests
                                    ?: 0,
                                clearTrigger = clearTrigger,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                        }
                    }
                    if (detailViewModel != null) {
                        if (clearTrigger != null) {
                            GuestsCounter(
                                detailViewModel = detailViewModel,
                                label = "Children",
                                count = detailViewModel?.childrenCount?.collectAsState()?.value ?: 0,
                                onCountChange = { count -> detailViewModel?.setChildrenCount(count) },
                                totalOccupancyLimit = detailViewModel.staycation.collectAsState().value?.noOfGuests
                                    ?: 0,
                                clearTrigger = clearTrigger,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                        }
                    }
                    if (detailViewModel != null) {
                        if (clearTrigger != null) {
                            GuestsCounter(
                                detailViewModel = detailViewModel,
                                label = "Infant",
                                count =  detailViewModel?.infantCount?.collectAsState()?.value ?: 0,
                                onCountChange = { count -> detailViewModel?.setInfantCount(count) },
                                totalOccupancyLimit = infantOccupancyLimit,
                                clearTrigger = clearTrigger,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                        }
                    }

                    if (detailViewModel != null) {
                        if (clearTrigger != null) {
                            GuestsCounter(
                                detailViewModel = detailViewModel,
                                label = "Pets",
                                count =  detailViewModel?.petCount?.collectAsState()?.value ?: 0,
                                onCountChange = { count -> detailViewModel?.setPetCount(count) },
                                totalOccupancyLimit = petOccupancyLimit,
                                clearTrigger = clearTrigger,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.weight(1f))
                    if (detailViewModel != null) {
                        ConfirmGuest(
                            onClickSave = {
                                openGuestBottomSheet.value = false
                            },
                            enableButton = true,
                            detailViewModel = detailViewModel
                        )
                    }
                    Spacer(modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars))

                }

            }

        }

    }
}

@Composable
fun ConfirmGuest(
    modifier: Modifier = Modifier,
    onClickSave: () -> Unit,
    enableButton: Boolean = false,
    detailViewModel: DetailViewModel,
) {

    var guestCount = detailViewModel.guestCount.collectAsState().value
    var infantCount = detailViewModel.infantCount.collectAsState().value
    var petCount = detailViewModel.petCount.collectAsState().value

    val guestInfoText = buildAnnotatedString {
        append("$guestCount ${guestCount?.let { detailViewModel.pluralize("Guest", it) }}")

        if (infantCount!! > 0) {
            append(", $infantCount ${detailViewModel.pluralize("Infant", infantCount)}")
        }

        if (petCount!! > 0) {
            append(", $petCount ${detailViewModel.pluralize("Pet", petCount)}")
        }
    }

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

                    Text(
                        text = guestInfoText
                    )

                }
                BookingFilledButton(
                    buttonText = "Save",
                    onClick = {
                        onClickSave()
                    },
                    enabled = enableButton,
                    modifier = Modifier.width(90.dp)
                )
            }

        }

    }
}




@Composable
fun YourTripDivider(
    detailViewModel: DetailViewModel,
    modifier: Modifier = Modifier,
    onClickEditDate: () -> Unit,
    onClickEditGuest: () -> Unit
) {
   val formattedDateRange = detailViewModel.formattedDateRange.collectAsState().value
   // val guestCount = detailViewModel.guestCount.collectAsState().value

    var guestCount = detailViewModel.guestCount.collectAsState().value
    var infantCount = detailViewModel.infantCount.collectAsState().value
    var petCount = detailViewModel.petCount.collectAsState().value

    val guestInfoText = buildAnnotatedString {
        append("$guestCount ${guestCount?.let { detailViewModel.pluralize("guest", it) }}")

        if (infantCount!! > 0) {
            append(", $infantCount ${detailViewModel.pluralize("infant", infantCount)}")
        }

        if (petCount!! > 0) {
            append(", $petCount ${detailViewModel.pluralize("pet", petCount)}")
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
        val dateRowTextColor = if (formattedDateRange?.isNotBlank() == true) Color.Black else Color(0xFF999999)


        AppYourTripRow(
            rowLabel = "Dates",
            rowText = dateRowText,
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
        AppYourTripRow(
            rowLabel = "Guests",
            fontColor = guestColor,
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
fun AppPaymentDivider(
    bookingFee: Double,
    bookingDuration: Int,
    maintenanceFee: Double? = null,
    tripnilaFee: Double,
    forCancelBooking: Boolean = false,
    modifier: Modifier = Modifier
) {
    val formattedNumber = NumberFormat.getNumberInstance()
    val formattedNumberWithDecimalFormat = NumberFormat.getNumberInstance() as DecimalFormat
    formattedNumberWithDecimalFormat.apply {
        maximumFractionDigits = 2
        minimumFractionDigits = 2
    }

    val productBookingFee = bookingFee * bookingDuration
    val totalFee = productBookingFee + (maintenanceFee ?: 0.0) + tripnilaFee

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(all = 10.dp)
    ){
        Text(
            text = "Payment",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .padding(bottom = 5.dp)
        )
        PaymentRow(
            feeLabel = "₱ ${formattedNumber.format(bookingFee)} x $bookingDuration nights",
            feePrice = productBookingFee
        )

        if (maintenanceFee != null) {
            PaymentRow(
                feeLabel = "Maintenance fee",
                feePrice = maintenanceFee
            )
        }

        PaymentRow(
            feeLabel = "Tripnila service fee",
            feePrice = tripnilaFee
        )
        Divider(
            color = Color(0xFFDEDEDE),
            modifier = Modifier.padding(vertical = 3.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 3.dp)
        ) {
            Text(
                text = if (forCancelBooking) "Total paid" else "Total",
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .weight(1f)
            )
            Text(
                text = "₱ ${formattedNumberWithDecimalFormat.format(totalFee)}",
                fontWeight = FontWeight.SemiBold
            )
        }
        if (forCancelBooking) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()

            ) {
                Text(
                    text = "Amount refunded",
                    fontWeight = FontWeight.SemiBold,
                    color = Orange,
                    modifier = Modifier
                        .weight(1f)
                )
                Text(
                    text = "₱ ${formattedNumberWithDecimalFormat.format(totalFee * 0.80)}",
                    fontWeight = FontWeight.SemiBold,
                    color = Orange
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = "4 days before check in",
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.sp,
                    color = Color(0xFF999999),
                    modifier = Modifier
                        .weight(1f)
                )
                Text(
                    text = "80%",
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.sp,
                    color = Color(0xFF999999),
                )
            }
        }
        else {
            Text(
                text = "Payment method",
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .padding(vertical = 4.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                PaymentMethodCard(logoId = R.drawable.paypal)
                PaymentMethodCard(logoId = R.drawable.gcash)
                PaymentMethodCard(
                    logoId = R.drawable.paymaya,
                    modifier = Modifier
                        //                    .fillMaxHeight()
                        .height(40.dp)
                        .width(80.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
                PaymentOutlinedButton(
                    buttonText = "Add",
                    cornerSize = 20.dp,
                    modifier = Modifier
                        .padding(top = 5.dp)
                        .height(30.dp)
                )
            }
        }
    }
    Divider(
        color = Color(0xFF999999),
        modifier = Modifier.padding(top = 5.dp) // 10.dp
    )
}


@Composable
fun PaymentRow(feeLabel: String, feePrice: Double, modifier: Modifier = Modifier){

    val formattedNumberWithDecimalFormat = NumberFormat.getNumberInstance() as DecimalFormat
    formattedNumberWithDecimalFormat.apply {
        maximumFractionDigits = 2
        minimumFractionDigits = 2
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 3.dp)
    ) {
        Text(
            text = feeLabel,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF999999),
            modifier = Modifier
                .weight(1f)
        )
        Text(
            text = "₱ ${formattedNumberWithDecimalFormat.format(feePrice)}",
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF999999)
        )
    }
}

@Composable
fun PaymentMethodCard(logoId: Int, modifier: Modifier = Modifier) {

    var isClicked by remember { mutableStateOf(false) }
    val borderColor = if (isClicked) Orange else Color(0xFFF3F3F3)

    val interactionSource = remember { MutableInteractionSource() }
    val clickableModifier = Modifier.clickable(
        interactionSource = interactionSource,
        indication = null
    ) {
        isClicked = !isClicked
    }

    ElevatedCard(
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 10.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF3F3F3)
        ),
        modifier = modifier
            .height(40.dp)
            .width(80.dp)
            .padding(vertical = 5.dp, horizontal = 5.dp)
            .border(1.dp, borderColor, RoundedCornerShape(10.dp))
            .then(clickableModifier)
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = logoId),
                contentDescription = "",
                contentScale = ContentScale.FillWidth,
                modifier = modifier
            )
        }
    }
}

@Composable
fun PaymentOutlinedButton(buttonText: String, modifier: Modifier = Modifier, cornerSize: Dp = 10.dp){
    OutlinedButton(
        shape = RoundedCornerShape(cornerSize),
        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Orange),
        onClick = { /*TODO*/ },
        modifier = modifier
    ) {
        Text(
            text = buttonText,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = Orange
        )
    }
}

@Composable
fun PaymentAgreementText(modifier: Modifier = Modifier) {
    Text(
        text = buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    color = Color.Black,
                    fontSize = 8.sp
                )
            ) {
                append("By selecting the button below, I agree to the ")
            }
            withStyle(
                style = SpanStyle(
                    color = Color.Black,
                    fontSize = 8.sp, // 10
                    fontWeight = FontWeight.Medium,
                    textDecoration = TextDecoration.Underline
                )
            ) {
                append("Host’s House Rules")
            }
            withStyle(
                style = SpanStyle(
                    color = Color.Black,
                    fontSize = 8.sp
                )
            ) {append(", ")
            }
            withStyle(
                style = SpanStyle(
                    fontSize = 8.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Medium,
                    textDecoration = TextDecoration.Underline
                )
            ) {
                append("Ground rules for guests")
            }
            withStyle(
                style = SpanStyle(
                    color = Color.Black,
                    fontSize = 8.sp
                )
            ) {
                append(", ")
            }
            withStyle(
                style = SpanStyle(
                    color = Color.Black,
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Medium,
                    textDecoration = TextDecoration.Underline
                )
            ) {
                append("Tripnila’s Rebooking and Refund Policy")
            }
            withStyle(
                style = SpanStyle(
                    color = Color.Black,
                    fontSize = 8.sp
                )
            ) {append(" and Tripnila can charge my payment method if I’m responsible for any damage on the property")
            }
        },
        modifier = modifier
            .padding(
                horizontal = 10.dp,
                vertical = 10.dp // 12
            )
            .fillMaxWidth()

    )
}


@Composable
fun GuestsCounter(
    detailViewModel: DetailViewModel,
    label: String,
    count: Int = 0,
    onCountChange: (Int) -> Unit,
    totalOccupancyLimit: Int,
    clearTrigger: Boolean,
    modifier: Modifier = Modifier
) {

    var count by remember(clearTrigger) { mutableIntStateOf(count) }


  // var count by remember { mutableIntStateOf(count) }

    var adultsCount = detailViewModel.adultCount.collectAsState().value
    var childrenCount = detailViewModel.childrenCount.collectAsState().value

    Row(
        modifier = modifier
            .background(Color.White)
            .fillMaxWidth()
            .padding(top = 5.dp, bottom = 20.dp)
    ) {
        Text(
            text = label,
            color = Color(0xff333333),
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.weight(1f))
        IconButton(
            onClick = {
                if (count > 0) {
                    count--
                    onCountChange(count)
                }
            },
            enabled = count > 0,
            modifier = Modifier.size(17.dp)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.subtract_circle),
                contentDescription = "Subtract",
                tint = if (count > 0) Color(0xFF999999) else Color(0xFFDEDEDE)
            )
        }
        Text(
            text = count.toString(),
            color = Color(0xff333333),
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 15.dp)
        )
        IconButton(
            onClick = {
                if (count < totalOccupancyLimit &&
                    (label != "Children" || count + adultsCount!! < totalOccupancyLimit) &&
                    (label != "Adults" || count + childrenCount!! < totalOccupancyLimit)
                ) {
                    count++
                    onCountChange(count)
                }
            },
            enabled = count < totalOccupancyLimit &&
                    (label != "Children" || count + adultsCount!! < totalOccupancyLimit) &&
                    (label != "Adults" || count + childrenCount!! < totalOccupancyLimit),
            modifier = Modifier.size(17.dp)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.add_circle),
                contentDescription = "Add",
                tint = Color(0xFF999999)
            )
        }
    }
}


@Preview(heightDp = 915, widthDp = 412)
@Composable
fun BookingScreenPreview(){



    Surface(
        Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Column(
            Modifier.fillMaxHeight(0.8f)
        ) {
            Column(
                Modifier
                    .fillMaxHeight()
                    .padding(horizontal = 16.dp)
            ) {
                var adultsCount by remember { mutableStateOf(1) }
                var childrenCount by remember { mutableStateOf(0) }
                var infantsCount by remember { mutableStateOf(0) }
                var petsCount by remember { mutableStateOf(0) }


                // Total occupancy limit
                val totalOccupancyLimit = 5 // Set your desired total limit

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
//                    GuestsCounter(
//                        label = "Adults",
//                        defaultCount = adultsCount,
//                        onCountChange = { count -> adultsCount = count },
//                        totalOccupancyLimit = totalOccupancyLimit,
//                        adultsCount = adultsCount,
//                        childrenCount = childrenCount
//                    )
//
//                    GuestsCounter(
//                        label = "Children",
//                        defaultCount = childrenCount,
//                        onCountChange = { count -> childrenCount = count },
//                        totalOccupancyLimit = totalOccupancyLimit,
//                        adultsCount = adultsCount,
//                        childrenCount = childrenCount
//                    )
//
//                    GuestsCounter(
//                        label = "Infant",
//                        defaultCount = infantsCount,
//                        onCountChange = { count -> infantsCount = count },
//                        totalOccupancyLimit = 5,
//                        adultsCount = adultsCount,
//                        childrenCount = childrenCount
//                    )
//
//                    GuestsCounter(
//                        label = "Pets",
//                        defaultCount = petsCount,
//                        onCountChange = { count -> petsCount = count },
//                        totalOccupancyLimit = 0,
//                        adultsCount = adultsCount,
//                        childrenCount = childrenCount
//                    )

                }

            }
        }

    }
}


