package com.example.tripnila.screens

import androidx.compose.foundation.BorderStroke
import com.example.tripnila.data.Amenity
import com.example.tripnila.data.Review


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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tripnila.R
import com.example.tripnila.components.AdditionalInformationRow
import com.example.tripnila.components.AppExpandingText
import com.example.tripnila.components.AppFilledButton
import com.example.tripnila.components.AppLocationCard
import com.example.tripnila.components.AppOutlinedButton
import com.example.tripnila.components.AppReviewsCard
import com.example.tripnila.components.Orange
import com.example.tripnila.components.ReviewCard
import com.example.tripnila.components.Tag
import com.example.tripnila.components.UnderlinedText
import com.example.tripnila.data.DailySchedule
import com.example.tripnila.data.TourAvailableDates


@Composable
fun TourDetailsScreen(){

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
    )
    val amenities = listOf(
        Amenity(
            image = R.drawable.person,
            count = 4,
            name = "person"
        ),
        Amenity(
            image = R.drawable.pool,
            count = 1,
            name = "swimming pool"
        ),
        Amenity(
            image = R.drawable.bedroom,
            count = 2,
            name = "bedroom"
        ),
        Amenity(
            image = R.drawable.bathroom,
            count = 2,
            name = "bathroom"
        ),
        Amenity(
            image = R.drawable.kitchen,
            count = 1,
            name = "kitchen"
        )
    )
    val promos = listOf("Senior citizen & PWD : 20%")
    val tags = listOf("Nature", "Food", "History")
    val dailySchedule = listOf(
        DailySchedule(
            day = "Monday",
            openingTime = "9:00 am",
            closingTime = "7:00 pm",
            isOpen = true
        ),
        DailySchedule(
            day = "Tuesday",
            openingTime = "9:00 am",
            closingTime = "7:00 pm",
            isOpen = true
        ),
        DailySchedule(
            day = "Wednesday",
            openingTime = "9:00 am",
            closingTime = "7:00 pm",
            isOpen = true
        ),
        DailySchedule(
            day = "Thursday",
            openingTime = "9:00 am",
            closingTime = "7:00 pm",
            isOpen = true
        ),
        DailySchedule(
            day = "Friday",
            openingTime = "9:00 am",
            closingTime = "7:00 pm",
            isOpen = true
        ),
        DailySchedule(
            day = "Saturday",
            openingTime = "9:00 am",
            closingTime = "7:00 pm",
            isOpen = true
        ),
        DailySchedule(
            day = "Sunday",
            openingTime = "9:00 am",
            closingTime = "7:00 pm",
            isOpen = false
        )
    )
    val reviews = listOf(
        Review(
            rating = 4.5,
            comment = "A wonderful staycation experience!",
            touristImage = R.drawable.joshua,
            touristName = "John Doe",
            reviewDate = "2023-05-15"
        ),
        Review(
            rating = 5.0,
            comment = "Amazing place and great service!",
            touristImage = R.drawable.joshua,
            touristName = "Jane Smith",
            reviewDate = "2023-04-20"
        ),
        Review(
            rating = 5.0,
            comment = "Amazing place and great service!",
            touristImage = R.drawable.joshua,
            touristName = "Jane Smith",
            reviewDate = "2023-04-20"
        ),
        Review(
            rating = 5.0,
            comment = "Amazing place and great service!",
            touristImage = R.drawable.joshua,
            touristName = "Jane Smith",
            reviewDate = "2023-04-20"
        ),
        Review(
            rating = 5.0,
            comment = "Amazing place and great service!",
            touristImage = R.drawable.joshua,
            touristName = "Jane Smith",
            reviewDate = "2023-04-20"
        ),
        Review(
            rating = 5.0,
            comment = "Amazing place and great service!",
            touristImage = R.drawable.joshua,
            touristName = "Jane Smith",
            reviewDate = "2023-04-20"
        ),
    )



    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = Color(0xFFEFEFEF)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.tour1),
                        contentDescription = "Tour",
                        contentScale = ContentScale.FillWidth
                    )
                    TopBarIcons()
                }
            }
            item {
                TourDescriptionCard1(
                    tourName = "Cubao Night Tour",
                    tags = tags,
                    location = "Cubao",
                    averageRating = 4.7,
                    totalReviews = 254,
                    modifier = Modifier
                        .offset(y = (-17).dp)
                )
            }
            item {
                TourDescriptionCard2(
                    hostImage = R.drawable.joshua,
                    hostName = "Joshua",
                    duration = "3",
                    language = "English",
                    modifier = Modifier
                        .offset(y = (-5).dp)
                        .padding(bottom = 12.dp)
                )
            }
            item {
                TourDescriptionCard3(
                    image1 = R.drawable.karaoke,
                    image2 = R.drawable.karaoke,
                    image3 = R.drawable.karaoke,
                    description = "Do you have a layover in Manila and don’t know what to do while you’re here?\n" +
                            "I’m Joshua and I’m a food lover, and I would like to share you a " +
                            "delicate and lorremadasdawqeqweqweqweqweqesadada.",
                    modifier = Modifier
                        .offset(y = (-5).dp)
                        .padding(bottom = 12.dp)
                )
            }
            item {
                TourAvailabilityCard(
                    availableDates = availableDates,
                    modifier = Modifier
                        .offset(y = (-5).dp)
                        .padding(bottom = 12.dp)
                )
            }
            item {
                AppLocationCard(
                    location = "Where we'll meet",
                    locationImage = R.drawable.map_image2,
                    locationDescription = "MC Kitchen, Greenhills",
                    modifier = Modifier
                        .offset(y = (-5).dp)
                        .padding(bottom = 12.dp)
                )
            }
            item {
                AppReviewsCard(
                    reviews = reviews,
                    modifier = Modifier
                        .offset(y = (-5).dp)
                        .padding(bottom = 12.dp)
                )
            }
            item {
                TourAdditionalInformationCard(
                    modifier = Modifier
                        .offset(y = (-5).dp)
                        .padding(bottom = 7.dp)
                )

            }
            item {
                TourBottomBookingBar()
            }
        }
    }
}

@Composable
fun TourDescriptionCard1(
    tourName: String,
    tags: List<String>,
    location: String,
    averageRating: Double,
    totalReviews: Int,
    modifier: Modifier = Modifier
) {

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(20.dp))
            .background(color = Color.White)
    ) {
        Column(
            modifier = Modifier
                .padding(
                    horizontal = 25.dp,
                    vertical = 20.dp // 12
                )
                .fillMaxWidth()
        ) {
            Text(
                text = tourName,
                color = Color.Black,
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.align(Alignment.Start)


            )
            Row(
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.padding(top = 10.dp),
            ) {
                Icon(
                    modifier = Modifier.height(18.dp),
                    painter = painterResource(id = R.drawable.star),
                    contentDescription = "Star"
                )
                Text(
                    text = averageRating.toString(),
                    //fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.width(15.dp))
                UnderlinedText(
                    textLabel = "$totalReviews reviews",
                    color = Color.Black,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.width(5.dp))
                tags.forEach { tag ->
                    Tag(tag = tag)
                }

            }
            Text(
                text = location,
                //fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .padding(top = 10.dp)
                    .align(Alignment.Start)
            )
        }

    }
}

@Composable
fun TourDescriptionCard2(
    hostImage: Int,
    hostName: String,
    duration: String,
    language: String,
    modifier: Modifier = Modifier
){

    Box(
        modifier = modifier
            .fillMaxWidth()
            //.height(height = 139.dp)
            .clip(shape = RoundedCornerShape(20.dp))
            .background(color = Color.White)
    ){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                //                .padding(horizontal = 25.dp, vertical = 12.dp),
                .padding(
                    horizontal = 25.dp,
                    vertical = 20.dp // 12
                ),
        ) {
            Row {
                Box {
                    Image(
                        painter = painterResource(hostImage),
                        contentDescription = "Host",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(50.dp)
                            .clip(shape = RoundedCornerShape(50.dp))
                    )
                }
                Column(
                    modifier = Modifier.padding(horizontal = 8.dp)
                ) {
                    Text(
                        text = "Hosted by $hostName",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                            .padding(top = 6.dp)
                    )
                    Text(
                        text = "$duration hours • Hosted in $language",
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF666666)

                    )
                }
            }

        }

    }
}

@Composable
fun TourDescriptionCard3(
    description: String,
    image1: Int,
    image2: Int,
    image3: Int,
    modifier: Modifier = Modifier
){

    Box(
        modifier = modifier
            .fillMaxWidth()
            //.height(height = 139.dp)
            .clip(shape = RoundedCornerShape(20.dp))
            .background(color = Color.White)
    ){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                //                .padding(horizontal = 25.dp, vertical = 12.dp),
                .padding(
                    horizontal = 25.dp,
                    vertical = 20.dp // 12
                ),
        ) {
            Text(
                text = "What you'll do",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .align(Alignment.Start)
            )
            AppExpandingText(
                longText = description,
                modifier = Modifier.padding(vertical = 10.dp)
            )
            Row(
                modifier = Modifier
                    .padding(top = 8.dp, bottom = 18.dp)
                    .height(160.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(0.48f)
                        .clip(RoundedCornerShape(topStart = 10.dp, bottomStart = 10.dp))
                        .background(Color.Red)

                ) {
                    Image(
                        painter = painterResource(id = image1),
                        contentDescription = "",
                        contentScale = ContentScale.FillHeight
                    )
                }
                Spacer(modifier = Modifier.fillMaxWidth(0.04f))
                Column(
                    Modifier
                        .fillMaxHeight()
                        .fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .height(76.dp)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(topEnd = 10.dp))
                            .background(Color.Red)
                    ) {
                        Image(
                            painter = painterResource(id = image2),
                            contentDescription = "",
                            contentScale = ContentScale.FillWidth
                        )
                    }               
                    Spacer(modifier = Modifier.weight(1f))
                    Box(
                        modifier = Modifier
                            .height(76.dp)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(bottomEnd = 10.dp))
                            .background(Color.Red)
                    ){
                        Image(
                            painter = painterResource(id = image3),
                            contentDescription = "",
                            contentScale = ContentScale.FillWidth
                        )
                    }
                }
            }
            AppOutlinedButton(
                buttonText = "See all photos",

            )
        }
    }
}


@Composable
fun TourAvailabilityCard(availableDates: List<TourAvailableDates> , modifier: Modifier = Modifier){

    Box(
        modifier = modifier
            .fillMaxWidth()
            //.height(height = 139.dp)
            .clip(shape = RoundedCornerShape(20.dp))
            .background(color = Color.White)
    ){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                //                .padding(horizontal = 25.dp, vertical = 12.dp),
                .padding(
                    horizontal = 25.dp,
                    vertical = 20.dp // 12
                ),
        ) {
            Text(
                text = "Available dates",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .align(Alignment.Start)
            )
            Text(
                text = "6 available for Sep 11 - 14",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF666666),
                modifier = Modifier
                    .align(Alignment.Start)
            )
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 7.dp, bottom = 18.dp),
                //contentPadding = PaddingValues(start = 12.dp, end = 12.dp),
            ) {
                items(availableDates) { availableDate ->
                    TourDateCard(
                        availableDate = availableDate,
                        modifier = Modifier
                            .padding(top = 7.dp , end = 12.dp)
                    )
                }
            }
            AppOutlinedButton(buttonText = "See all dates")
        }
    }
}

@Composable
fun TourAdditionalInformationCard(modifier: Modifier = Modifier){

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
            Text(
                text = "Additional information",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .padding(bottom = 4.dp)
                    .align(Alignment.Start)
            )
            AdditionalInformationRow("What to bring")
            AdditionalInformationRow("Health & safety")
            AdditionalInformationRow("Cancellation & reschedule policy")
            AdditionalInformationRow("Business Information")

        }
    }
}

@Composable
fun TourBottomBookingBar(modifier: Modifier = Modifier){
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(color = Color.White)
            .border(0.1.dp, Color.Black)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 25.dp, vertical = 12.dp),
        ) {
            BookingOutlinedButton(
                buttonText = "Cancel",
                onClick = {},
                modifier = Modifier.width(120.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            BookingFilledButton(
                buttonText = "Choose date",
                onClick = {},
                modifier = Modifier.width(140.dp)
            )
        }
    }
}

@Composable
fun TourDateCard(
    availableDate: TourAvailableDates,
    modifier: Modifier = Modifier
){
    OutlinedCard(
        modifier = modifier
            .width(width = 135.dp)
            .height(height = 116.dp),
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(0.5.dp, Color(0xff999999)),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        )

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
            Text(
                text = buildAnnotatedString {
                    // Apply a bold style to the price
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
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(
                    containerColor = Orange
                ),
                contentPadding = PaddingValues(2.dp),
                modifier = Modifier
                    .padding(bottom = 2.dp)
                    .width(50.dp)
                    .height(20.dp)

            ){
                Text(
                    text = "Choose",
                    color = Color.White,
                    fontWeight = FontWeight.Medium,
                    fontSize = 8.sp
                )
            }
        }
    }

}





@Preview
@Composable
private fun TourDetailsItemPreviews(){

  // TourBottomBookingBar()

}


@Preview
@Composable
private fun TourDetailsPreview() {

    TourDetailsScreen()

}
