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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
import com.example.tripnila.common.AdditionalInformationRow
import com.example.tripnila.common.AppExpandingText
import com.example.tripnila.common.AppLocationCard
import com.example.tripnila.common.AppOutlinedButton
import com.example.tripnila.common.AppReviewsCard
import com.example.tripnila.common.Orange
import com.example.tripnila.common.Tag
import com.example.tripnila.common.UnderlinedText
import com.example.tripnila.data.ReviewUiState
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
    val tags = listOf("Nature", "Food", "History")
    val reviews = listOf(
        ReviewUiState(
            rating = 4.5,
            comment = "A wonderful staycation experience!",
         //   touristImage = R.drawable.joshua,
            touristName = "John Doe",
            reviewDate = "2023-05-15"
        ),
        ReviewUiState(
            rating = 5.0,
            comment = "Amazing place and great service!",
        //    touristImage = R.drawable.joshua,
            touristName = "Jane Smith",
            reviewDate = "2023-04-20"
        ),
        ReviewUiState(
            rating = 5.0,
            comment = "Amazing place and great service!",
        //    touristImage = R.drawable.joshua,
            touristName = "Jane Smith",
            reviewDate = "2023-04-20"
        ),
        ReviewUiState(
            rating = 5.0,
            comment = "Amazing place and great service!",
        //    touristImage = R.drawable.joshua,
            touristName = "Jane Smith",
            reviewDate = "2023-04-20"
        ),
        ReviewUiState(
            rating = 5.0,
            comment = "Amazing place and great service!",
         //   touristImage = R.drawable.joshua,
            touristName = "Jane Smith",
            reviewDate = "2023-04-20"
        ),
        ReviewUiState(
            rating = 5.0,
            comment = "Amazing place and great service!",
        //    touristImage = R.drawable.joshua,
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
                    hostImage = "",
                    hostName = "Joshua",
                    duration = "3",
                    language = "English",
                    modifier = Modifier
                        .offset(y = (-5).dp)
                        .padding(bottom = 12.dp)
                )
            }
            item {
//                TourDescriptionCard3(
//                    image1 = R.drawable.karaoke,
//                    image2 = R.drawable.karaoke,
//                    image3 = R.drawable.karaoke,
//                    description = "Do you have a layover in Manila and don’t know what to do while you’re here?\n" +
//                            "I’m Joshua and I’m a food lover, and I would like to share you a " +
//                            "delicate and lorremadasdawqeqweqweqweqweqesadada.",
//                    modifier = Modifier
//                        .offset(y = (-5).dp)
//                        .padding(bottom = 12.dp)
//                )
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
    withEditButton: Boolean = false,
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
            Row {
                Text(
                    text = tourName,
                    color = Color.Black,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .padding(horizontal = 8.dp, vertical = 6.dp)
                )
                if (withEditButton) {
                    Spacer(modifier = Modifier.width(8.dp))
                    AppOutlinedButtonWithBadge(
                        buttonLabel = "Edit",
                        modifier = Modifier
                            .offset(y = 9.dp)
                            .width(40.dp)
                    )
                }
            }
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
                    fontWeight = FontWeight.Medium,
                    onClick = {

                    }
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
    hostImage: String,
    hostName: String,
    duration: String,
    language: String,
    withEditButton: Boolean = false,
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
                    AsyncImage(
                        model = if (hostImage == "") "https://upload.wikimedia.org/wikipedia/commons/8/89/Portrait_Placeholder.png" else hostImage,//imageLoader,
                        contentDescription = "",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(50.dp)
                            .clip(shape = RoundedCornerShape(50.dp))

                    )
//                    Image(
//                        painter = painterResource(hostImage),
//                        contentDescription = "Host",
//                        contentScale = ContentScale.Crop,
//                        modifier = Modifier
//                            .size(50.dp)
//                            .clip(shape = RoundedCornerShape(50.dp))
//                    )
                }
                Column(
                    modifier = Modifier.padding(horizontal = 8.dp)
                ) {
                    Row {
                        Text(
                            text = "Hosted by $hostName",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier
                                .padding(horizontal = 8.dp, vertical = 6.dp)
                        )
                        if (withEditButton) {
                            Spacer(modifier = Modifier.width(8.dp))
                            AppOutlinedButtonWithBadge(
                                buttonLabel = "Edit",
                                modifier = Modifier
                                    .offset(y = 9.dp)
                                    .width(40.dp)
                            )
                        }
                    }
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
    image1: String,
    image2: String,
    image3: String,
    withEditButton: Boolean = false,
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ){
                Text(
                    text = "What you'll do",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .padding(bottom = 4.dp)
                )
                if (withEditButton) {
                    Spacer(modifier = Modifier.width(8.dp))
                    AppOutlinedButtonWithBadge(
                        buttonLabel = "Edit",
                        modifier = Modifier
                            .width(40.dp)
                    )
                }
            }
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
                        .background(Color.LightGray)

                ) {

                    AsyncImage(
                        model = if (image1 == "") "https://upload.wikimedia.org/wikipedia/commons/thumb/3/3f/Placeholder_view_vector.svg/1022px-Placeholder_view_vector.svg.png"
                                    else image1,
                        contentDescription = "",
                        contentScale = ContentScale.Crop

                    )

//                    Image(
//                        painter = painterResource(id = image1),
//                        contentDescription = "",
//                        contentScale = ContentScale.FillHeight
//                    )
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
                            .background(Color.LightGray)
                    ) {
                        AsyncImage(
                            model = if (image2 == "") "https://upload.wikimedia.org/wikipedia/commons/thumb/3/3f/Placeholder_view_vector.svg/1022px-Placeholder_view_vector.svg.png" else image2,
                            contentDescription = "",
                            contentScale = ContentScale.FillWidth

                        )
//                        Image(
//                            painter = painterResource(id = image2),
//                            contentDescription = "",
//                            contentScale = ContentScale.FillWidth
//                        )
                    }               
                    Spacer(modifier = Modifier.weight(1f))
                    Box(
                        modifier = Modifier
                            .height(76.dp)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(bottomEnd = 10.dp))
                            .background(Color.LightGray)
                    ){
                        AsyncImage(
                            model = if (image3 == "") "https://upload.wikimedia.org/wikipedia/commons/thumb/3/3f/Placeholder_view_vector.svg/1022px-Placeholder_view_vector.svg.png" else image3,
                            contentDescription = "",
                            contentScale = ContentScale.FillWidth

                        )
//                        Image(
//                            painter = painterResource(id = image3),
//                            contentDescription = "",
//                            contentScale = ContentScale.FillWidth
//                        )
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
fun TourAdditionalInformationCard(withEditButton: Boolean = false, modifier: Modifier = Modifier){

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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = "Additional information",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .padding(bottom = 4.dp)
                )
                if (withEditButton) {
                    Spacer(modifier = Modifier.width(8.dp))
                    AppOutlinedButtonWithBadge(
                        buttonLabel = "Edit",
                        modifier = Modifier
                            .width(40.dp)
                    )
                }
            }
            AdditionalInformationRow(textInfo = "What to bring", onClick = {/*TODO*/})
            AdditionalInformationRow(textInfo = "Health & safety", onClick = {/*TODO*/})
            AdditionalInformationRow(textInfo = "Cancellation & reschedule policy", onClick = {/*TODO*/})
            AdditionalInformationRow(textInfo = "Business Information", onClick = {/*TODO*/})

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
