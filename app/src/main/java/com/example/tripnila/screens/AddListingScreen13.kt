package com.example.tripnila.screens

import android.text.format.DateUtils
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DateRangePickerDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tripnila.common.Orange
import com.example.tripnila.data.Staycation
import com.example.tripnila.data.StaycationAvailability
import com.example.tripnila.model.AddListingViewModel
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.firestore.firestore
import java.time.Duration
import java.time.*
import java.time.Instant
import java.time.ZoneId
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddListingScreen13(
    listingType: String = "Staycation",
    addListingViewModel: AddListingViewModel? = null,
    onNavToNext: (String) -> Unit,
    onNavToBack: () -> Unit,
){

    val context = LocalContext.current
    val dateRangePickerState = rememberDateRangePickerState()

    LaunchedEffect(
        dateRangePickerState.selectedStartDateMillis,
        dateRangePickerState.selectedEndDateMillis
    ) {
        if (dateRangePickerState.selectedStartDateMillis != null && dateRangePickerState.selectedEndDateMillis != null) {
            val startDate = Instant.ofEpochMilli(dateRangePickerState.selectedStartDateMillis!!)
                .plus(Duration.ofHours(4))
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime()
            val endDate = Instant.ofEpochMilli(dateRangePickerState.selectedEndDateMillis!!)
                .plus(Duration.ofHours(4))
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime()

            val datesInRange = mutableListOf<Date>()
            var date = startDate
            while (!date.isAfter(endDate)) {
                datesInRange.add(Date.from(date.atZone(ZoneId.systemDefault()).toInstant()))
                date = date.plusDays(1)
            }

            // Log the dates between the selected date range as Date objects
            for (date in datesInRange) {
                Log.d("Date", date.toString())
            }

            val staycationAvailabilityList = datesInRange.map { StaycationAvailability(availableDate = Timestamp(it)) }

            addListingViewModel?.setAvailableDates(staycationAvailabilityList)

            // Create a Staycation object with the availableDates property
//            val staycation = Staycation(availableDates = staycationAvailabilityList)
//
//
//            Log.d("Date", "$staycation")
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
                        addListingViewModel?.setTag(context,"Swimming pool, Karaoke, Grill, Gaming Console")
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
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .height(400.dp)
                                .fillMaxWidth()
                                .background(Orange.copy(0.3f))
                        ) {
                            DateRangePicker(
                                headline = null,
                                title = {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(80.dp)
                                            .background(Orange),
                                        horizontalArrangement = Arrangement.Center,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = if (dateRangePickerState.selectedStartDateMillis != null && dateRangePickerState.selectedEndDateMillis != null) {
                                                val startDate = Instant.ofEpochMilli(
                                                    dateRangePickerState.selectedStartDateMillis!!
                                                ).atZone(ZoneId.systemDefault()).toLocalDate()
                                                val endDate = Instant.ofEpochMilli(
                                                    dateRangePickerState.selectedEndDateMillis!!
                                                ).atZone(ZoneId.systemDefault()).toLocalDate()
                                                "${startDate.month.name} ${startDate.dayOfMonth} - ${endDate.month.name} ${endDate.dayOfMonth}"
                                            } else {
                                                "Drag to select dates"
                                            },
                                            color = Color.White,
                                            textAlign = TextAlign.Center,
                                            fontSize = 18.sp,
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                    }

                                },
                                showModeToggle = false,
                                state = dateRangePickerState,
                                colors = DatePickerDefaults.colors(
                                    dayContentColor = Color.Black,
                                    selectedDayContainerColor = Color(0xFF9FFFB4),
                                    dayInSelectionRangeContainerColor = Color(0xFF9FFFB4).copy(.3f),
                                    disabledDayContentColor = Color.Black.copy(0.3f),
                                    //disabledSelectedDayContainerColor = Color.Red,
                                    todayDateBorderColor = Orange,
                                    todayContentColor = Color.Black,
                                    selectedDayContentColor = Color.Black
                                ),
                                dateValidator = { dateLong ->
                                    val date = Instant.ofEpochMilli(dateLong).atZone(ZoneId.systemDefault()).toLocalDate()
                                    date.isAfter(LocalDate.now()) || date.isEqual(LocalDate.now())
                                }
                            )
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