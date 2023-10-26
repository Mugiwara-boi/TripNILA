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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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

    var selectedPropertyIndex by remember { mutableStateOf(-1) }
    val types = listOf(
        PropertyDescription(
            icon = R.drawable.house,
            label = "An entire place"
        ),
        PropertyDescription(
            icon = R.drawable.room,
            label = "A room"
        )
    )

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
                    verticalArrangement = Arrangement.spacedBy(15.dp),
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
                                .padding(bottom = 10.dp)
                        )
                    }
                    items(types) { type ->
                        PropertySpaceCard(
                            icon = type.icon,
                            label = type.label,
                            selected = selectedPropertyIndex == types.indexOf(type),
                            onSelectedChange = { isSelected ->
                                selectedPropertyIndex = if (isSelected) types.indexOf(type) else -1
                            },
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


    var selectedOpeningHour by remember {
        mutableIntStateOf(0) // or use  mutableStateOf(0)
    }

    var selectedOpeningMinute by remember {
        mutableIntStateOf(0) // or use  mutableStateOf(0)
    }

    var showDialog by remember {
        mutableStateOf(false)
    }

    val timePickerState = rememberTimePickerState(
        initialHour = selectedOpeningHour,
        initialMinute = selectedOpeningMinute,
        is24Hour = false
    )

    val borderColor = if (selected) Orange else Color(0xff999999)
    val containerColor = if (selected) Orange.copy(alpha = 0.2f) else Color.White

    if (showDialog) {
        AlertDialog(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(size = 12.dp)
                ),
            onDismissRequest = { showDialog = false }
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
                TimeInput(state = timePickerState)

                // buttons
                Row(
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    // dismiss button
                    TextButton(onClick = { showDialog = false }) {
                        Text(text = "Dismiss")
                    }

                    // confirm button
                    TextButton(
                        onClick = {
                            showDialog = false
                            selectedOpeningHour = timePickerState.hour
                            selectedOpeningMinute = timePickerState.minute
                        }
                    ) {
                        Text(text = "Confirm")
                    }
                }
            }
        }
    }


    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height = 86.dp)
            .clip(shape = RoundedCornerShape(10.dp))
            .border(
                border = BorderStroke(1.dp, borderColor),
                shape = RoundedCornerShape(10.dp)
            )
            .background(containerColor)
            .clickable { onSelectedChange(!selected) } // Toggle the selection state
    ) {
        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(start = 10.dp, end = 10.dp, top = 15.dp, bottom = 15.dp)
        ) {
            Text(
                text = day,
                color = Color(0xff333333),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
            Row {
                Text(
                    text = "Open",
                    color = Color(0xff333333),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
                Card(
                    colors = CardDefaults.outlinedCardColors(
                        containerColor = Color.White
                    ),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, Color(0xffc2c2c2)),
                    modifier = Modifier
                        .width(75.dp)
                        .height(30.dp)
                        .clickable { showDialog = true }

                ){
                    val formattedTime = SimpleDateFormat("hh:mm a", Locale.getDefault())
                        .format(Date(selectedOpeningHour.toLong() * 60 * 60 * 1000 + selectedOpeningMinute.toLong() * 60 * 1000))
                    Text(text = formattedTime)
                }
                Text(
                    text = "Close",
                    color = Color(0xff333333),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
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
