package com.example.tripnila.screens

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
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tripnila.common.Orange
import com.example.tripnila.model.AddListingViewModel
import java.time.Instant
import java.time.ZoneId
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AddListingScreen13(
    listingType: String = "Staycation",
    addListingViewModel: AddListingViewModel,
    onNavToNext: (String) -> Unit,
    onNavToBack: () -> Unit,
){

    val availableDates = addListingViewModel.staycation.collectAsState().value.availableDates
        .map { it.availableDate }
        .map {
            it?.let { it1 ->
                Instant.ofEpochSecond(it1.seconds, it.nanoseconds.toLong())
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()
            }
        }.sortedBy { it }

    val bookedDates = addListingViewModel.staycation.collectAsState().value.staycationBookings
        .asSequence()
        .filter { it.bookingStatus != "Cancelled" }
        .map { it.getDatesBetween() }
        .flatten()
        .toSet()
        .sortedBy { it }
        .toList()

    val titleText: MutableState<String?> = remember { mutableStateOf(null) }
    val titleDeleteText: MutableState<String?> = remember { mutableStateOf(null) }

    val availabilityRangePickerState = rememberDateRangePickerState()
    val openAvailabilityBottomSheet = remember { mutableStateOf(false) }
    val availabilityBottomSheetState = rememberModalBottomSheetState( skipPartiallyExpanded = true)

    val hasNavigationBar = WindowInsets.areNavigationBarsVisible
    val enableBottomSaveButton: MutableState<Boolean> = remember { mutableStateOf(false) }
    val isSaveButtonClicked = remember { mutableStateOf(false) }

    val deleteRangePickerState = rememberDateRangePickerState()
    val openDeleteBottomSheet = remember { mutableStateOf(false) }
    val deleteBottomSheetState = rememberModalBottomSheetState( skipPartiallyExpanded = true)

    val enableBottomDeleteButton: MutableState<Boolean> = remember { mutableStateOf(false) }
    val isDeleteButtonClicked = remember { mutableStateOf(false) }

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

    Surface(
        modifier = Modifier
            .fillMaxSize()
    ){
        Scaffold(
            bottomBar = {
                AddListingBottomBookingBar(
                    leftButtonText = "Back",
                    onNext = {
                        onNavToNext(listingType)
                    },
                    onCancel = {
                        onNavToBack()
                    },
                    enableRightButton = addListingViewModel?.staycation?.collectAsState()?.value?.availableDates?.isNotEmpty() == true
                )
            },
            topBar = {
                TopAppBar(
                    title = {
                        SaveAndExitButton(
                            onClick = { /*TODO*/ }
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.White
                    )
                )
            }
        ){
            Column(
                modifier = Modifier
                    .background(Color.White)
                    .fillMaxWidth()
                    .padding(horizontal = 25.dp, vertical = 20.dp)
                    .padding(it)
            ) {

                LazyColumn(
                    //contentPadding = PaddingValues(vertical = 25.dp),
                    verticalArrangement = Arrangement.spacedBy(15.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    item {
                        Text(
                            text = "Set your calendar",
                            color = Color(0xff333333),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 10.dp)
                        )
                    }
                    item {
                        Text(
                            text = "Choose dates you want to be booked",
                            color = Color(0xff333333),
                            fontSize = 12.sp
                        )
                    }
                    item {
                        AvailabilityText(
                            modifier = Modifier.padding(top = 20.dp, bottom = 15.dp)
                        )
                    }
                    item {
                        availableDates?.let { availableDates -> CalendarComposable(availableDates = availableDates, bookedDates = bookedDates) }
                    }
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            BookingOutlinedButton(
                                buttonText = "Add date",
                                onClick = { openAvailabilityBottomSheet.value = true },
                                modifier = Modifier
                                    .width(120.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            BookingFilledButton(
                                buttonText = "Remove date",
                                onClick = { openDeleteBottomSheet.value = true },
                                modifier = Modifier
                                    .width(120.dp)
                            )
                        }
                    }

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
                                    bookedDates.map { (it.toEpochDay()) * 24 * 60 * 60 * 1000 }.toList()


                                    selectedLocalDate > currentDate && selectedLocalDate !in availableDates && selectedLocalDate !in bookedDates

                                   // selectedLocalDate > currentDate && selectedLocalDate !in dateList
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
                                        addListingViewModel.addAvailableDate(currentDate)
                                        currentDate = currentDate.plusDays(1)
                                    }


                                    availabilityRangePickerState.setSelection(null, null)
                                    openAvailabilityBottomSheet.value = false
                                },
                                isLoading = false,
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
                                    bookedDates.map { (it.toEpochDay()) * 24 * 60 * 60 * 1000 }.toList()

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
                                        addListingViewModel.removeAvailableDate(currentDate!!)
                                        currentDate = currentDate!!.plusDays(1)
                                    }


                                    deleteRangePickerState.setSelection(null, null)
                                    openDeleteBottomSheet.value = false
                                },
                                isLoading = false,
                                modifier = Modifier
                                    .noPaddingIf(hasNavigationBar)
                            )
                            Spacer(modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars))
                        }

                    }
                }
                Spacer(modifier = Modifier.weight(1f))
                AddListingStepIndicator(modifier = Modifier, currentPage = 2, pageCount = 4)
            }
        }
    }
}

@Composable
fun AvailabilityText(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .size(size = 18.dp)
                .clip(shape = RoundedCornerShape(30.dp))
                .background(color = Color(0xff9fffb4)),
        )
        Text(
            text = "Select dates to set to available",
            color = Color(0xff333333),
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(
                    x = 24.dp
                ))
    }
}

@Preview
@Composable
private fun AvailabilityPreview() {
    val addListingViewModel = viewModel(modelClass = AddListingViewModel::class.java)

    AddListingScreen13(
        addListingViewModel = addListingViewModel,
        onNavToBack = {},
        onNavToNext = {}
    )
}