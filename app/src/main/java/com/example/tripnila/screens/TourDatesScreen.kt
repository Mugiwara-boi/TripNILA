package com.example.tripnila.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.tripnila.R
import com.example.tripnila.common.Orange
import com.example.tripnila.data.TourAvailableDates
import com.example.tripnila.model.TourDetailsViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun TourDatesScreen(
    touristId: String = "",
    tourId: String = "",
    tourDetailsViewModel: TourDetailsViewModel,
    onNavToTourBookingScreen: (String) -> Unit,
    onBack: () -> Unit
){

    val tour by tourDetailsViewModel.tour.collectAsState()
    val personCount by tourDetailsViewModel.personCount.collectAsState()

    val tourAvailableDates = tour.schedule

    val currentDate = LocalDate.now()
    val availableDates = tourAvailableDates
        .filter { tourSchedule ->
            val remainingSlot = tourSchedule.slot - tourSchedule.bookedSlot
            tourSchedule.date > currentDate && remainingSlot >= personCount // Keep only schedules with remaining slots
        }
        .map { tourSchedule ->
            val remainingSlot = tourSchedule.slot - tourSchedule.bookedSlot
            TourAvailableDates(
                availabilityId = tourSchedule.tourScheduleId,
                day = tourSchedule.date.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.ENGLISH),
                date = tourSchedule.date.format(DateTimeFormatter.ofPattern("MMM d")),
                startingTime = tourSchedule.startTime,
                endingTime = tourSchedule.endTime,
                description = if (remainingSlot == 1) "$remainingSlot slot left" else "$remainingSlot slots left",
                price = tour.tourPrice,
                localDate = tourSchedule.date,
                remainingSlot = remainingSlot
            )
        }.sortedBy { it.localDate }

    //availableDates = availableDates.plus(availableDates)


//    val availableDates = tourAvailableDates.map {  tourSchedule ->
//        val remainingSlot = tourSchedule.slot - tourSchedule.bookedSlot
//        TourAvailableDates(
//            availabilityId = tourSchedule.tourScheduleId,
//            day = tourSchedule.date.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.ENGLISH),
//            date = tourSchedule.date.format(DateTimeFormatter.ofPattern("MMM d")),
//            startingTime = tourSchedule.startTime,
//            endingTime = tourSchedule.endTime,
//            description = if (remainingSlot == 0 || remainingSlot == 1) "$remainingSlot slot left" else "$remainingSlot slots left",
//            price = tour.tourPrice,
//            localDate = tourSchedule.date
//        )
//    }

//
//    val availableDates = tourAvailableDates
//        .filter { tourSchedule -> tourSchedule.date > currentDate }
//        .map { tourSchedule ->
//            TourAvailableDates(
//                availabilityId = tourSchedule.tourScheduleId,
//                day = tourSchedule.date.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.ENGLISH),
//                date = tourSchedule.date.format(DateTimeFormatter.ofPattern("MMM d")),
//                startingTime = tourSchedule.startTime,
//                endingTime = tourSchedule.endTime,
//                description = "Book for private group",
//                price = tour.tourPrice,
//                localDate = tourSchedule.date
//            )
//        }

    val selectedDate by tourDetailsViewModel.selectedDate.collectAsState()


    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Scaffold(
            bottomBar = {
                AddListingBottomBookingBar(
                    enableRightButton = selectedDate != null,
                    onCancel = {
                        onBack()
                    },
                    onNext = {
                        onNavToTourBookingScreen(touristId)
                    }
                )
            }
    ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                AsyncImage(
                    model = tour.tourImages.find { it.photoType == "Cover" }?.photoUrl,
                    contentDescription = "Tour Image",
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
                    .padding(it)
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .clip(shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                    .background(color = Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            start = 25.dp,
                            end = 25.dp,
                            top = 20.dp // 12
                        )
                        .background(Color.White)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Choose schedule",
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

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(20.dp), //Arrangement.spacedBy(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        items(availableDates) { availableDate ->
                            TourDateCardWithoutButton(
                                availableDate = availableDate,
                                isClicked = availableDate == selectedDate,
                                onClick = {
                                    if (availableDate == selectedDate) {
                                        tourDetailsViewModel.removeSelectedDate()
                                    } else {
                                        tourDetailsViewModel.setSelectedDate(availableDate)
                                    }
                                }
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
fun TourDateCardWithoutButton(
    modifier: Modifier = Modifier,
    availableDate: TourAvailableDates,
    isClicked: Boolean,
    onClick: () -> Unit
){

  //  var isClicked by remember { mutableStateOf(false) }

    OutlinedCard(
        modifier = modifier
            .width(width = 135.dp)
            .height(height = 90.dp),
        shape = RoundedCornerShape(10.dp),
        border = if (isClicked) BorderStroke(2.dp, Orange) else BorderStroke(0.5.dp, Color(0xff999999)),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        onClick = {
           // isClicked = !isClicked
            onClick()
        }

    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 10.dp, vertical = 8.dp)
        ) {
            Text(
                text = "${availableDate.day}, ${availableDate.date}",
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "${availableDate.startingTime} - ${availableDate.endingTime}",
                fontSize = 8.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF999999)
            )
            Text(
                text = availableDate.description,
                fontSize = 8.sp,
                fontWeight = FontWeight.Medium,
                textDecoration = TextDecoration.Underline
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("₱ ${"%.2f".format(availableDate.price)}")
                    }
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Medium)) {
                        append(" / person")
                    }
                },
                fontSize = 8.sp,
                modifier = Modifier.padding(vertical = 15.dp)
            )
        }
    }
}


@Preview
@Composable
private fun TourDatesScreenPreview(){
 //   TourDatesScreen()
}