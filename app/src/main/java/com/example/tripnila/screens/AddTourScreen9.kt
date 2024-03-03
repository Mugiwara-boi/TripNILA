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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tripnila.common.Orange
import com.example.tripnila.data.TourSchedule
import com.example.tripnila.model.HostTourViewModel
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTourScreen9(
    listingType: String = "Tourist",
    hostTourViewModel: HostTourViewModel? = null,
    onNavToNext: (String) -> Unit,
    onNavToBack: () -> Unit,
){


    val tourSchedules = hostTourViewModel?.tour?.collectAsState()?.value?.schedule

    var openingHour by remember { mutableIntStateOf(0) }
    var openingMinute by remember { mutableIntStateOf(0) }

    var closingHour by remember { mutableIntStateOf(0) }
    var closingMinute by remember { mutableIntStateOf(0) }

    var isFocused by remember { mutableStateOf(false) }
    var slot by remember { mutableIntStateOf(0) }

    var currentDate by remember { mutableStateOf(LocalDate.now()) }
    var openDialogBox by remember { mutableStateOf(false) }

    var showOpeningDialog by remember { mutableStateOf(false) }
    var showClosingDialog by remember { mutableStateOf(false) }


    val openingTimePickerState = rememberTimePickerState(
        initialHour = openingHour,
        initialMinute = openingMinute,
        is24Hour = false
    )
    val closingTimePickerState = rememberTimePickerState(
        initialHour = closingHour,
        initialMinute = closingMinute,
        is24Hour = false
    )

    val openingCalendar = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, openingHour)
        set(Calendar.MINUTE, openingMinute)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }

    val closingCalendar = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, closingHour)
        set(Calendar.MINUTE, closingMinute)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }


    val formattedOpeningTime = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(openingCalendar.time)
    val formattedClosingTime = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(closingCalendar.time)


    LaunchedEffect(openDialogBox) {
        if (!openDialogBox) {
            openingHour = 0
            openingMinute = 0
            closingHour = 0
            closingMinute = 0
            slot = 0
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
                    enableRightButton = hostTourViewModel?.tour?.collectAsState()?.value?.schedule?.isNotEmpty() == true

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
                    .fillMaxHeight()
                    .padding(horizontal = 25.dp)
                    .padding(top = 20.dp)
                    .padding(it)
            ) {

                LazyColumn(
                    //contentPadding = PaddingValues(vertical = 25.dp),
                    verticalArrangement = Arrangement.spacedBy(15.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        //.fillMaxHeight()
                        .weight(1f)
                ) {

                    item {
                        Text(
                            text = "Set your calendar!",
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
                        Card(
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 10.dp
                            ),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier

                                .fillMaxWidth()
                                //.height(450.dp)
                        ){
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

                               // horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                IconButton(onClick = {
                                    currentDate = currentDate.minusMonths(1)
                                }) {
                                    Icon(imageVector = Icons.Filled.KeyboardArrowLeft, tint = Color.White, modifier = Modifier.size(18.dp), contentDescription = null)
                                }
                                Spacer(modifier = Modifier.weight(1f))
                                Text(
                                    text = currentDate.format(DateTimeFormatter.ofPattern("MMMM yyyy")) ,
                                    color = Color.White,
                                    style = TextStyle(
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                )
                                Spacer(modifier = Modifier.weight(1f))
                                IconButton(onClick = { currentDate = currentDate.plusMonths(1) }) {
                                    Icon(imageVector = Icons.Filled.KeyboardArrowRight, tint = Color.White, modifier = Modifier.size(18.dp),contentDescription = null)
                                }
                            }
                            Row(
                                modifier = Modifier
                                    .padding(horizontal = 10.dp)
                                    .fillMaxWidth()
                                    .height(45.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                IconButton(onClick = { currentDate = currentDate.minusDays(1) }) {
                                    Icon(imageVector = Icons.Filled.KeyboardArrowLeft, tint = Orange, modifier = Modifier.size(18.dp), contentDescription = null)
                                }
                                Spacer(modifier = Modifier.weight(1f))
                                Column(
                                    modifier = Modifier.width(80.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = currentDate.format(DateTimeFormatter.ofPattern("d")),
                                        color = Color(0xfff9a664),
                                        style = TextStyle(
                                            fontSize = 8.sp,
                                            fontWeight = FontWeight.Medium)
                                    )
                                    Text(
                                        text = currentDate.format(DateTimeFormatter.ofPattern("EEEE")),
                                        color = Color(0xfff9a664),
                                        style = TextStyle(
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Medium)
                                    )
                                }
                                Spacer(modifier = Modifier.weight(1f))
                                IconButton(onClick = { currentDate = currentDate.plusDays(1) }) {
                                    Icon(imageVector = Icons.Filled.KeyboardArrowRight, tint = Orange, modifier = Modifier.size(18.dp),contentDescription = null)
                                }
                            }
                            Divider()
                            val stroke = Stroke(
                                width = 8f,
                                pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                            )
                            Column(
                                modifier = Modifier
                                    .padding(horizontal = 10.dp)
                                    .fillMaxWidth()
                            ){

                                tourSchedules?.forEach { tourSchedule ->
                                    TourScheduleCard(
                                        tourSchedule = tourSchedule,
                                        onDelete = { sched ->
                                            hostTourViewModel.removeSchedule(sched)
                                        },
                                        modifier = Modifier
                                            .padding(horizontal = 20.dp)
                                            .padding(top = 10.dp)
                                    )

                                }




                                Box(
                                    modifier = Modifier
                                        .padding(horizontal = 20.dp, vertical = 10.dp)
                                        .fillMaxWidth()
                                        .height(45.dp)
                                        .clip(shape = RoundedCornerShape(10.dp))
                                        .drawBehind {
                                            drawRoundRect(
                                                color = Orange,
                                                style = stroke,
                                                cornerRadius = CornerRadius(10.dp.toPx())
                                            )
                                        }
                                        .clickable {
                                            openDialogBox = true
                                        }
                                ){
                                    Row(
                                        modifier = Modifier.fillMaxSize(),
                                        horizontalArrangement = Arrangement.Center,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.Add,
                                            contentDescription = "Add",
                                            tint = Orange,
                                            modifier = Modifier
                                                .size(18.dp)
                                                .padding(end = 3.dp)
                                        )
                                        Text(
                                            text = "Add more",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = Orange
                                        )

                                    }

                                    if (openDialogBox) {
                                        Dialog(
                                            onDismissRequest = {
                                                openDialogBox = false
                                            }
                                        ) {
                                            // Draw a rectangle shape with rounded corners inside the dialog
                                            Card(
                                                colors = CardDefaults.outlinedCardColors(
                                                    containerColor = Color.White
                                                ),
                                                shape = RoundedCornerShape(10.dp),
                                                border = BorderStroke(1.dp, Color(0xff999999)),
                                                modifier = Modifier
                                                    .fillMaxWidth()

                                            ) {
                                                Column(
                                                    modifier = Modifier
                                                        .padding(horizontal = 10.dp)
                                                        .fillMaxWidth(),
                                                    horizontalAlignment = Alignment.CenterHorizontally,
                                                ) {

                                                    Text(
                                                        text = "Set time",
                                                        color = Color(0xff333333),
                                                        style = TextStyle(
                                                            fontSize = 14.sp,
                                                            fontWeight = FontWeight.Medium
                                                        ),
                                                        modifier = Modifier
                                                            .align(Alignment.Start)
                                                            .padding(10.dp)
                                                    )

                                                    Row(
                                                        modifier = Modifier
                                                            .padding(
                                                                horizontal = 20.dp,
                                                                vertical = 3.dp
                                                            )
                                                            .fillMaxWidth(),
                                                        horizontalArrangement = Arrangement.Center,
                                                    ) {
                                                        Text(
                                                            text = "Start",
                                                            color = Color(0xff333333),
                                                            fontSize = 12.sp,
                                                            fontWeight = FontWeight.Medium,
                                                            modifier = Modifier.padding(end = 10.dp, top = 5.dp)
                                                        )
                                                        Card(
                                                            colors = CardDefaults.outlinedCardColors(
                                                                containerColor = Color.White
                                                            ),
                                                            shape = RoundedCornerShape(10.dp),
                                                            border = BorderStroke(1.dp, Color(0xffc2c2c2)),
                                                            modifier = Modifier
                                                                .padding(end = 10.dp)
                                                                .width(75.dp)
                                                                .height(30.dp)
                                                                .clickable {
                                                                    showOpeningDialog = true
                                                                }

                                                        ) {

                                                            Column(
                                                                modifier = Modifier.fillMaxSize(),
                                                                horizontalAlignment = Alignment.CenterHorizontally
                                                            ) {
                                                                Text(
                                                                    text = "Time",
                                                                    color = Color(0xffc2c2c2),
                                                                    fontSize = 7.sp,
                                                                    fontWeight = FontWeight.Medium,
                                                                    modifier = Modifier
                                                                        .padding(start = 5.dp)
                                                                        .align(Alignment.Start)

                                                                )

                                                                Text(
                                                                    text = if (formattedOpeningTime == "12:00 AM") "00:00 AM" else formattedOpeningTime,
                                                                    color = Color(0xff333333),
                                                                    fontSize = 12.sp,
                                                                    fontWeight = FontWeight.Medium
                                                                )
                                                            }
                                                        }
                                                        Spacer(modifier = Modifier.weight(1f))
                                                        Text(
                                                            text = "End",
                                                            color = Color(0xff333333),
                                                            fontSize = 12.sp,
                                                            fontWeight = FontWeight.Medium,
                                                            modifier = Modifier
                                                                .padding(end = 10.dp, top = 5.dp)
                                                                .offset(x = 10.dp)
                                                        )
                                                        Card(
                                                            colors = CardDefaults.outlinedCardColors(
                                                                containerColor = Color.White
                                                            ),
                                                            shape = RoundedCornerShape(10.dp),
                                                            border = BorderStroke(1.dp, Color(0xffc2c2c2)),
                                                            modifier = Modifier
                                                                .padding(end = 10.dp)
                                                                .width(75.dp)
                                                                .height(30.dp)
                                                                .offset(x = 10.dp)
                                                                .clickable {
                                                                    showClosingDialog = true
                                                                }

                                                        ) {
                                                            Column(
                                                                modifier = Modifier.fillMaxSize(),
                                                                horizontalAlignment = Alignment.CenterHorizontally
                                                            ) {
                                                                Text(
                                                                    text = "Time",
                                                                    color = Color(0xffc2c2c2),
                                                                    fontSize = 7.sp,
                                                                    fontWeight = FontWeight.Medium,
                                                                    modifier = Modifier
                                                                        .padding(start = 5.dp)
                                                                        .align(Alignment.Start)

                                                                )


                                                                Text(
                                                                    text = if (formattedClosingTime == "12:00 AM") "00:00 AM" else formattedClosingTime,
                                                                    color = Color(0xff333333),
                                                                    fontSize = 12.sp,
                                                                    fontWeight = FontWeight.Medium
                                                                )
                                                            }
                                                        }
                                                    }
                                                    // SLOT
                                                    Row(
                                                        modifier = Modifier
                                                            .padding(
                                                                horizontal = 20.dp,
                                                                vertical = 3.dp
                                                            )
                                                            .fillMaxWidth(),
                                                        horizontalArrangement = Arrangement.Start,
                                                    ) {
                                                        Text(
                                                            text = "Slot",
                                                            color = Color(0xff333333),
                                                            fontSize = 12.sp,
                                                            fontWeight = FontWeight.Medium,
                                                            modifier = Modifier.padding(
                                                                end = 10.dp,
                                                                top = 5.dp
                                                            )
                                                        )
                                                        BasicTextField(
                                                            value = slot.toString(),
                                                            onValueChange = { newValue ->
                                                                val newText = newValue.filter { it.isDigit() }
                                                                slot = if (newText.isNotEmpty()) newText.toInt() else 0
                                                            },
                                                            textStyle = TextStyle(
                                                                fontWeight = FontWeight.Medium,
                                                                color = Color.Black
                                                            ),
                                                            keyboardOptions = KeyboardOptions(
                                                                imeAction = ImeAction.Done,
                                                                keyboardType = KeyboardType.Number
                                                            ),
                                                            singleLine = true,
                                                            decorationBox = { innerTextField ->
                                                                Row(
                                                                    modifier = Modifier
                                                                        .background(color = Color.White, shape = RoundedCornerShape(size = 10.dp))
                                                                        .border(
                                                                            width = 1.dp,
                                                                            color = if (isFocused) {
                                                                                Orange
                                                                            } else {
                                                                                Color(0xffc2c2c2)
                                                                            },
                                                                            shape = RoundedCornerShape(size = 10.dp)
                                                                        )
                                                                        .padding(horizontal = 8.dp), // inner padding
                                                                    verticalAlignment = Alignment.CenterVertically
                                                                ) {
                                                                    innerTextField()
                                                                }
                                                            },
                                                            modifier = Modifier
                                                               // .width(80.dp)
                                                                .width(75.dp)
                                                                .height(30.dp)
                                                                .offset(x = 5.dp)
                                                                .onFocusChanged { focusState ->
                                                                    isFocused = focusState.isFocused
                                                                }
                                                        )
                                                    }



                                                    Row(
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .padding(horizontal = 15.dp)
                                                            .padding(vertical = 10.dp)
                                                    ) {
                                                        BookingOutlinedButton(
                                                            buttonText = "Cancel",
                                                            contentPadding = PaddingValues(vertical = 0.dp),
                                                            onClick = { openDialogBox = false },
                                                            modifier = Modifier
                                                                .width(105.dp)
                                                                .height(30.dp)
                                                        )
                                                        Spacer(modifier = Modifier.weight(1f))
                                                        BookingFilledButton(
                                                            buttonText = "Add",
                                                            contentPadding = PaddingValues(vertical = 0.dp),
                                                            onClick = {
                                                                hostTourViewModel?.setSchedule(
                                                                    TourSchedule(
                                                                        date = currentDate,
                                                                        startTime = formattedOpeningTime,
                                                                        endTime = formattedClosingTime,
                                                                        slot = slot,
                                                                        bookedSlot = 0
                                                                    )
                                                                )
                                                                openDialogBox = false
                                                            },
                                                            modifier = Modifier
                                                                .width(105.dp)
                                                                .height(30.dp)

                                                        )
                                                    }



                                                }

                                                if (showOpeningDialog ) { //showOpeningDialog &&
                                                    AlertDialog(
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .background(
                                                                color = MaterialTheme.colorScheme.surface,
                                                                shape = RoundedCornerShape(size = 12.dp)
                                                            ),
                                                        onDismissRequest = {
                                                            openingHour = 0
                                                            openingMinute = 0
                                                            showOpeningDialog = false
                                                        }

                                                    ) {
                                                        Column(
                                                            modifier = Modifier
                                                                .background(
                                                                    color = Color.LightGray.copy(
                                                                        alpha = 0.3f
                                                                    )
                                                                )
                                                                .padding(
                                                                    top = 28.dp,
                                                                    start = 20.dp,
                                                                    end = 20.dp,
                                                                    bottom = 12.dp
                                                                ),
                                                            verticalArrangement = Arrangement.Center,
                                                            horizontalAlignment = Alignment.CenterHorizontally
                                                        ) {
                                                            // time picker
                                                            TimeInput(
                                                                state = openingTimePickerState,
                                                                colors = TimePickerDefaults.colors(
                                                                    clockDialColor = Orange,
                                                                    selectorColor = Orange,
                                                                    containerColor = Orange.copy(.3f),
                                                                    timeSelectorSelectedContainerColor = Orange.copy(.5f),
                                                                    timeSelectorUnselectedContainerColor = Orange.copy(.3f),
                                                                    periodSelectorSelectedContainerColor = Orange.copy(.5f),
                                                                    timeSelectorSelectedContentColor = Color.Black,
                                                                    timeSelectorUnselectedContentColor = Color.Black.copy(.5f),

                                                                )
                                                            )

                                                            // buttons
                                                            Row(
                                                                modifier = Modifier
                                                                    .padding(top = 12.dp)
                                                                    .fillMaxWidth(),
                                                                horizontalArrangement = Arrangement.End
                                                            ) {
                                                                // dismiss button
                                                                TextButton(onClick = {
                                                                    openingHour = 0
                                                                    openingMinute = 0
                                                                    showOpeningDialog = false
                                                                }) {
                                                                    Text(text = "Dismiss", color = Orange)
                                                                }

                                                                // confirm button
                                                                TextButton(
                                                                    onClick = {
                                                                        openingHour = openingTimePickerState.hour
                                                                        openingMinute = openingTimePickerState.minute
                                                                        showOpeningDialog = false
                                                                    }
                                                                ) {
                                                                    Text(text = "Confirm", color = Orange)
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                                if (showClosingDialog ) { //showOpeningDialog &&
                                                    AlertDialog(
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .background(
                                                                color = MaterialTheme.colorScheme.surface,
                                                                shape = RoundedCornerShape(size = 12.dp)
                                                            ),
                                                        onDismissRequest = {
                                                            closingHour = 0
                                                            closingMinute = 0
                                                            showClosingDialog = false
                                                        }
                                                    ) {
                                                        Column(
                                                            modifier = Modifier
                                                                .background(
                                                                    color = Color.LightGray.copy(
                                                                        alpha = 0.3f
                                                                    )
                                                                )
                                                                .padding(
                                                                    top = 28.dp,
                                                                    start = 20.dp,
                                                                    end = 20.dp,
                                                                    bottom = 12.dp
                                                                ),
                                                            verticalArrangement = Arrangement.Center,
                                                            horizontalAlignment = Alignment.CenterHorizontally
                                                        ) {
                                                            // time picker
                                                            TimeInput(
                                                                state = closingTimePickerState,
                                                                colors = TimePickerDefaults.colors(
                                                                    clockDialColor = Orange,
                                                                    selectorColor = Orange,
                                                                    containerColor = Orange.copy(.3f),
                                                                    timeSelectorSelectedContainerColor = Orange.copy(
                                                                        .5f
                                                                    ),
                                                                    timeSelectorUnselectedContainerColor = Orange.copy(
                                                                        .3f
                                                                    ),
                                                                    periodSelectorSelectedContainerColor = Orange.copy(
                                                                        .5f
                                                                    ),
                                                                    timeSelectorSelectedContentColor = Color.Black,
                                                                    timeSelectorUnselectedContentColor = Color.Black.copy(
                                                                        .5f
                                                                    ),

                                                                )


                                                            )

                                                            // buttons
                                                            Row(
                                                                modifier = Modifier
                                                                    .padding(top = 12.dp)
                                                                    .fillMaxWidth(),
                                                                horizontalArrangement = Arrangement.End
                                                            ) {
                                                                // dismiss button
                                                                TextButton(onClick = {
                                                                    closingHour = 0
                                                                    closingMinute = 0
                                                                    showClosingDialog = false
                                                                }) {
                                                                    Text(
                                                                        text = "Dismiss",
                                                                        color = Orange
                                                                    )
                                                                }

                                                                // confirm button
                                                                TextButton(
                                                                    onClick = {
                                                                        closingHour = closingTimePickerState.hour
                                                                        closingMinute = closingTimePickerState.minute
                                                                        showClosingDialog = false

                                                                    }
                                                                ) {
                                                                    Text(
                                                                        text = "Confirm",
                                                                        color = Orange
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

                    }
                }
                //Spacer(modifier = Modifier.weight(1f))

                AddListingStepIndicator(modifier = Modifier, currentPage = 1, pageCount = 4)


            }
        }
    }
}

@Composable
fun TourScheduleCard(
    modifier: Modifier = Modifier,
    tourSchedule: TourSchedule = TourSchedule(
        tourScheduleId = "",
        date = LocalDate.now(),
        startTime = "00:00 AM",
        endTime = "00:00 PM",
        slot = 0,
        bookedSlot = 0
    ),
    onDelete: (TourSchedule) -> Unit,
) {

    val remainingSlot = tourSchedule.slot - tourSchedule.bookedSlot
    val slotText = if (remainingSlot == 0 || remainingSlot == 1) "$remainingSlot remaining slot" else "$remainingSlot remaining slots"

    Card(
        border = BorderStroke(0.5.dp, Color(0xff999999)),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(10.dp),
        modifier = modifier
            .fillMaxWidth()
           // .height(height = 42.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Column {
                Text(
                    text = tourSchedule.date.format(DateTimeFormatter.ofPattern("EEE, MMM d")) + "  •  $slotText",//"Mon, Sep 11",
                    color = Color(0xff333333),
                    style = TextStyle(
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium
                    ),
                    modifier = Modifier
                )
                Text(
                    text = "${tourSchedule.startTime} - ${tourSchedule.endTime}",
                    color = Color(0xff999999),
                    style = TextStyle(
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Medium
                    ),
                    modifier = Modifier

                )
            }
            Spacer(modifier = Modifier.weight(1f))
            BookingOutlinedButton(
                buttonText = "Delete",
                onClick = {
                    onDelete(tourSchedule)
                },
                buttonShape = RoundedCornerShape(20.dp),
                contentFontSize = 8.sp,
                contentPadding = PaddingValues(0.dp),
                modifier = Modifier
                    .height(20.dp)
                    .width(50.dp),
            )
        }


    }

}



@Preview
@Composable
private fun AddTourScreen9Preview() {
  //
    val hostTourViewModel = viewModel(modelClass = HostTourViewModel::class.java)

    AddTourScreen9(
        hostTourViewModel = hostTourViewModel,
        onNavToNext = {

        },
        onNavToBack = {

        }
    )

  //  TourScheduleCard()

}
