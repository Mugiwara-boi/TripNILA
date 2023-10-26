package com.example.tripnila.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tripnila.R
import com.example.tripnila.components.Orange
import com.example.tripnila.data.PropertyDescription
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBusinessScreen10(){

    val days = listOf(
        "Monday",
        "Tuesday",
        "Wednesday",
        "Thursday",
        "Friday",
        "Saturday",
        "Sunday"
    )
    val selectedDayIndex = remember { mutableStateListOf<Int>() }

    Surface(
        modifier = Modifier
            .fillMaxSize()
    ){
        Scaffold(
            bottomBar = {
                AddListingBottomBookingBar()
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
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    item {
                        Text(
                            text = "Set your schedule",
                            color = Color(0xff333333),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier
                                .width(267.dp)
                                .padding(bottom = 15.dp)
                        )
                    }

                    items(days){day ->
                        ScheduleCard(
                            day = day,
                            selected = days.indexOf(day) in selectedDayIndex,
                            onSelectedChange = { isSelected ->
                                if (isSelected) {
                                    selectedDayIndex.add(days.indexOf(day))
                                } else {
                                    selectedDayIndex.remove(days.indexOf(day))
                                }
                            }
                        )
                    }

                }
                Spacer(modifier = Modifier.weight(1f))
                AddListingStepIndicator(modifier = Modifier, currentPage = 2, pageCount = 4)

            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleCard(
    day: String,
    selected: Boolean,
    onSelectedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
){
    var selectedOpeningHour by remember { mutableStateOf(0) }
    var selectedOpeningMinute by remember { mutableStateOf(0) }
    var showOpeningDialog by remember { mutableStateOf(false) }

    var selectedClosingHour by remember { mutableStateOf(0) }
    var selectedClosingMinute by remember { mutableStateOf(0) }
    var showClosingDialog by remember { mutableStateOf(false) }

    val openingTimePickerState = rememberTimePickerState(
        initialHour = selectedOpeningHour,
        initialMinute = selectedOpeningMinute,
        is24Hour = false
    )

    val closingTimePickerState = rememberTimePickerState(
        initialHour = selectedClosingHour,
        initialMinute = selectedClosingMinute,
        is24Hour = false
    )

    val borderColor = if (selected) Orange else Color(0xff999999)
    val containerColor = if (selected) Orange.copy(alpha = 0.2f) else Color.White

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height = 75.dp)
            .clip(shape = RoundedCornerShape(10.dp))
            .border(
                border = BorderStroke(1.dp, borderColor),
                shape = RoundedCornerShape(10.dp)
            )
            .background(containerColor)
            .clickable { onSelectedChange(!selected) } // Toggle the selection state
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, end = 10.dp, top = 10.dp, bottom = 10.dp) // , top = 15.dp, bottom = 15.dp
        ) {
            Text(
                text = day,
                color = Color(0xff333333),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
            Row(
                modifier = Modifier
                    .padding(top = 6.dp)
                    .fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "Open",
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
                        .clickable { showOpeningDialog = true }

                ){
                    val formattedOpeningTime = SimpleDateFormat("hh:mm a", Locale.getDefault())
                        .format(Date(selectedOpeningHour.toLong() * 60 * 60 * 1000 + selectedOpeningMinute.toLong() * 60 * 1000))
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
                            text = formattedOpeningTime,
                            color = Color(0xff333333),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "Close",
                    color = Color(0xff333333),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(end = 10.dp, top = 5.dp).offset(x = 10.dp)
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
                        .clickable { showClosingDialog = true }

                ){
                    val formattedClosingTime = SimpleDateFormat("hh:mm a", Locale.getDefault())
                        .format(Date(selectedClosingHour.toLong() * 60 * 60 * 1000 + selectedClosingMinute.toLong() * 60 * 1000))
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
                            text = formattedClosingTime,
                            color = Color(0xff333333),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
                Checkbox(
                    checked = selected,
                    onCheckedChange = { onSelectedChange(it) },
                    colors = CheckboxDefaults.colors(
                        checkedColor = Color(0xFF333333)
                    ),
                    modifier = Modifier.offset(y = (-8).dp)
                )

            }

        }
    }

    if (showOpeningDialog && selected) {
        AlertDialog(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(size = 12.dp)
                ),
            onDismissRequest = { showOpeningDialog = false }
        ) {
            Column(
                modifier = Modifier
                    .background(
                        color = Color.LightGray.copy(alpha = 0.3f)
                    )
                    .padding(top = 28.dp, start = 20.dp, end = 20.dp, bottom = 12.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // time picker
                TimeInput(state = openingTimePickerState)

                // buttons
                Row(
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    // dismiss button
                    TextButton(onClick = { showOpeningDialog = false }) {
                        Text(text = "Dismiss")
                    }

                    // confirm button
                    TextButton(
                        onClick = {
                            showOpeningDialog = false
                            selectedOpeningHour = openingTimePickerState.hour
                            selectedOpeningMinute = openingTimePickerState.minute
                        }
                    ) {
                        Text(text = "Confirm")
                    }
                }
            }
        }
    }
    if (showClosingDialog && selected) {
        AlertDialog(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(size = 12.dp)
                ),
            onDismissRequest = { showClosingDialog = false }
        ) {
            Column(
                modifier = Modifier
                    .background(
                        color = Color.LightGray.copy(alpha = 0.3f)
                    )
                    .padding(top = 28.dp, start = 20.dp, end = 20.dp, bottom = 12.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // time picker
                TimeInput(state = closingTimePickerState)

                // buttons
                Row(
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    // dismiss button
                    TextButton(onClick = { showClosingDialog = false }) {
                        Text(text = "Dismiss")
                    }

                    // confirm button
                    TextButton(
                        onClick = {
                            showClosingDialog = false
                            selectedClosingHour = closingTimePickerState.hour
                            selectedClosingMinute = closingTimePickerState.minute
                        }
                    ) {
                        Text(text = "Confirm")
                    }
                }
            }
        }
    }


}

@Preview
@Composable
private fun AddBusinessPreview(){
    ScheduleCard(
        "Monday", false, {},
    )
}

@Preview
@Composable
private fun AddBusinessScreen10Preview(){
    AddBusinessScreen10()
}
