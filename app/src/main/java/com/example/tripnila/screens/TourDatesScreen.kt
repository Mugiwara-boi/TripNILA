package com.example.tripnila.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.tripnila.R
import com.example.tripnila.components.AppConfirmAndPayDivider
import com.example.tripnila.components.AppFilledButton
import com.example.tripnila.components.Orange
import com.example.tripnila.data.TourAvailableDates

@Composable
fun TourDatesScreen(){

    val availableDates = listOf(
        TourAvailableDates(
            day = "Mon",
            date = "Sep 11",
            startingTime = "1:00",
            endingTime = "23:00",
            description = "Book for private group",
            price = 3100.00
        ),
        TourAvailableDates(
            day = "Mon",
            date = "Sep 11",
            startingTime = "1:00",
            endingTime = "23:00",
            description = "Book for private group",
            price = 3100.00
        ),
        TourAvailableDates(
            day = "Mon",
            date = "Sep 11",
            startingTime = "1:00",
            endingTime = "23:00",
            description = "Book for private group",
            price = 3100.00
        ),
        TourAvailableDates(
            day = "Mon",
            date = "Sep 11",
            startingTime = "1:00",
            endingTime = "23:00",
            description = "Book for private group",
            price = 3100.00
        ),
        TourAvailableDates(
            day = "Mon",
            date = "Sep 11",
            startingTime = "1:00",
            endingTime = "23:00",
            description = "Book for private group",
            price = 3100.00
        ),
        TourAvailableDates(
            day = "Mon",
            date = "Sep 11",
            startingTime = "1:00",
            endingTime = "23:00",
            description = "Book for private group",
            price = 3100.00
        ),
        TourAvailableDates(
            day = "Mon",
            date = "Sep 11",
            startingTime = "1:00",
            endingTime = "23:00",
            description = "Book for private group",
            price = 3100.00
        ),
        TourAvailableDates(
            day = "Mon",
            date = "Sep 11",
            startingTime = "1:00",
            endingTime = "23:00",
            description = "Book for private group",
            price = 3100.00
        ),
        TourAvailableDates(
            day = "Mon",
            date = "Sep 11",
            startingTime = "1:00",
            endingTime = "23:00",
            description = "Book for private group",
            price = 3100.00
        ),

        )

    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = R.drawable.tour1),
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
                    .background(Color.White)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Choose dates",
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
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, start = 28.dp, end = 28.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),

                    ) {
                    val chunkedDates = availableDates.chunked(2) // Split availableDates into pairs

                    for (pair in chunkedDates) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            pair.forEach { availableDate ->
                                TourDateCardWithoutButton(availableDate = availableDate)
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                ) {
                    BookingOutlinedButton(
                        buttonText = "Cancel",
                        onClick = {},
                        modifier = Modifier.width(120.dp)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    BookingFilledButton(
                        buttonText = "Next",
                        onClick = {},
                        modifier = Modifier.width(120.dp)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TourDateCardWithoutButton(
    availableDate: TourAvailableDates,
    modifier: Modifier = Modifier
){

    var isClicked by remember { mutableStateOf(false) }

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
            isClicked = !isClicked
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
private fun TourDatesItemsPreview(){



}

@Preview
@Composable
private fun TourDatesScreenPreview(){
    TourDatesScreen()
}