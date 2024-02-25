package com.example.tripnila.screens

import com.example.tripnila.data.AmenityBrief


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.tripnila.R
import com.example.tripnila.common.AppLocationCard
import com.example.tripnila.common.AppOutlinedButton
import com.example.tripnila.common.AppReviewsCard
import com.example.tripnila.common.Tag
import com.example.tripnila.common.UnderlinedText
import com.example.tripnila.data.DailySchedule
import com.example.tripnila.data.ReviewUiState


@Composable
fun BusinessDetailsScreen(){


    val amenities = listOf(
        AmenityBrief(
            image = R.drawable.person,
            count = 4,
            name = "person"
        ),
        AmenityBrief(
            image = R.drawable.pool,
            count = 1,
            name = "swimming pool"
        ),
        AmenityBrief(
            image = R.drawable.bedroom,
            count = 2,
            name = "bedroom"
        ),
        AmenityBrief(
            image = R.drawable.bathroom,
            count = 2,
            name = "bathroom"
        ),
        AmenityBrief(
            image = R.drawable.kitchen,
            count = 1,
            name = "kitchen"
        )
    )
    val promos = listOf("Senior citizen & PWD : 20%")
    val tags = listOf("Food", "Bar")
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
        ReviewUiState(
            rating = 4.5,
            comment = "A wonderful staycation experience!",
           // touristImage = R.drawable.joshua,
            touristName = "John Doe",
            reviewDate = "2023-05-15"
        ),
        ReviewUiState(
            rating = 5.0,
            comment = "Amazing place and great service!",
            //touristImage = R.drawable.joshua,
            touristName = "Jane Smith",
            reviewDate = "2023-04-20"
        ),
        ReviewUiState(
            rating = 5.0,
            comment = "Amazing place and great service!",
            //touristImage = R.drawable.joshua,
            touristName = "Jane Smith",
            reviewDate = "2023-04-20"
        ),
        ReviewUiState(
            rating = 5.0,
            comment = "Amazing place and great service!",
           //touristImage = R.drawable.joshua,
            touristName = "Jane Smith",
            reviewDate = "2023-04-20"
        ),
        ReviewUiState(
            rating = 5.0,
            comment = "Amazing place and great service!",
           // touristImage = R.drawable.joshua,
            touristName = "Jane Smith",
            reviewDate = "2023-04-20"
        ),
        ReviewUiState(
            rating = 5.0,
            comment = "Amazing place and great service!",
           // touristImage = R.drawable.joshua,
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
                        painter = painterResource(id = R.drawable.business1),
                        contentDescription = "Business",
                        contentScale = ContentScale.FillWidth
                    )
                    TopBarIcons()
                }
            }
            item {
                BusinessDescriptionCard1(
                    businessName = "Leo’s Bar & Grill",
                    tags = tags,
                    location = "Rainforest, Pasig City",
                    averageRating = 4.7,
                    totalReviews = 254,
                    modifier = Modifier
                        .offset(y = (-17).dp)
                )
            }
            item {
                BusinessDescriptionCard2(
                    hostImage = "R.drawable.joshua",
                    hostName = "Juswa",
                    businessDescription = "Enjoy our wide selection of meat and vegetables in our menu, along with our selection of cocktails and drinks. Chill and have fun with your families and friends.",
                    modifier = Modifier
                        .offset(y = (-5).dp)
                        .padding(bottom = 12.dp)
                )
            }
            item {
                BusinessAmenitiesCard(
                    amenities = amenities,
                    modifier = Modifier
                        .offset(y = (-5).dp)
                        .padding(bottom = 12.dp)
                )
            }
            item {
                BusinessMenuCard(
                    menuImage = "R.drawable.business1",
                    promos = promos,
                    modifier = Modifier
                        .offset(y = (-5).dp)
                        .padding(bottom = 12.dp)
                )
            }
            item {
                BusinessScheduleCard(
                    dailySchedule = dailySchedule,
                    modifier = Modifier
                        .offset(y = (-5).dp)
                        .padding(bottom = 12.dp)
                )
            }
            item {
                AppLocationCard(
                    location = "North Greenhills",
                    locationImage = R.drawable.map_image2,
                    locationDescription = "Located on the rooftop of Building A, beside savemore.",
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
                        .padding(bottom = 7.dp)
                )
            }
            item {
                BusinessBottomBookingBar()
            }
        }
    }
}

@Composable
fun BusinessDescriptionCard1(
    businessName: String,
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = businessName,
                    color = Color.Black,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                )
                if (withEditButton) {
                    Spacer(modifier = Modifier.width(8.dp))
                    AppOutlinedButtonWithBadge(
                        buttonLabel = "Edit",
                        modifier = Modifier
                            .offset(y = 3.dp)
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
fun BusinessDescriptionCard2(
    hostImage: String,
    hostName: String,
    businessDescription: String,
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
                        model = if (hostImage == "") "https://upload.wikimedia.org/wikipedia/commons/8/89/Portrait_Placeholder.png" else hostImage,
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
                text = businessDescription,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 10.dp)
            )
        }

    }
}

@Composable
fun BusinessAmenitiesCard(
    amenities: List<AmenityBrief>,
    withEditButton: Boolean = false,
    modifier: Modifier = Modifier
) {

    Box(
        modifier = modifier
            .fillMaxWidth()
            //.height(height = 110.dp)
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
                    text = "Services and offers",
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
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {

                val numRows = (amenities.size + 1) / 2

                for (row in 0 until numRows) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start,
                    ) {
                        val startIndex = row * 2
                        val endIndex = minOf(startIndex + 2, amenities.size)

                        for (i in startIndex until endIndex) {
                            BusinessAmenityDetail(amenity = amenities[i])
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
            AppOutlinedButton(
                buttonText = "See all offers",
                onClick = {

                },
                modifier = Modifier
                    .padding(top = 12.dp)
            )

        }
    }
}

@Composable
fun BusinessMenuCard(
    menuImage: String,
    promos: List<String>,
    withEditButton: Boolean = false,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            //.height(height = 110.dp)
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
                    text = "Prices and menu",
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

            ElevatedCard(
                shape = RoundedCornerShape(10.dp),
                elevation = CardDefaults.elevatedCardElevation(
                    defaultElevation = 10.dp
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 5.dp)
                    .height(165.dp)

            ){
                AsyncImage(
                    model = if ( menuImage == "") "https://upload.wikimedia.org/wikipedia/commons/thumb/3/3f/Placeholder_view_vector.svg/1022px-Placeholder_view_vector.svg.png" else menuImage,//imageLoader,
                    contentDescription = "",
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier

                )

//                Image(
//                    painter = painterResource(id = menuImage),
//                    contentDescription = "Menu",
//                    contentScale = ContentScale.FillWidth
//                )
            }
            Text(
                text = "Discount and promos",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .padding(top = 10.dp)
                    .align(Alignment.Start)
            )
            promos.forEach { promoText ->
                Text(
                    text = promoText,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .align(Alignment.Start)
                )
            }


        }
    }
}

@Composable
fun BusinessScheduleCard(
    dailySchedule: List<DailySchedule>,
    withEditButton: Boolean = false,
    modifier: Modifier = Modifier
) {

    Box(
        modifier = modifier
            .fillMaxWidth()
            //.height(height = 110.dp)
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
                    text = "Schedule",
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
            dailySchedule.forEach {schedule ->
                    ScheduleRow(
                        day = schedule.day,
                        openingTime = schedule.openingTime,
                        closingTime = schedule.closingTime,
                        isOpen = schedule.isOpen
                    )
            }
        }
    }
}

@Composable
fun BusinessBottomBookingBar(modifier: Modifier = Modifier){
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
                buttonText = "Add",
                onClick = {},
                modifier = Modifier.width(120.dp)
            )
        }
    }
}


@Composable
fun ScheduleRow(day: String, openingTime: String?, closingTime: String?, isOpen: Boolean){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(vertical = 5.dp, horizontal = 12.dp)
    ) {
        Text(
            text = day,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = if(isOpen) "$openingTime - $closingTime" else "Closed",
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF999999),
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp)
        )
        Divider(color = Color(0xFFDEDEDE))
    }
}


@Composable
fun BusinessAmenityDetail(amenity: AmenityBrief, modifier: Modifier = Modifier){
    Box(
        modifier = modifier
            .width(148.dp)
            .padding(
                //horizontal = 5.dp,
                vertical = 3.dp
            )
    ) {
        Row {
            Image(
                imageVector = ImageVector.vectorResource(id = amenity.image),
                contentDescription = amenity.name
            )
            Text(
                text = amenity.name,
                //fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .padding(
                        horizontal = 3.dp,
                        //vertical = 5.dp
                    )
            )
        }
    }
}

@Preview
@Composable
private fun BusinessDetailsItemPreviews(){

    BusinessBottomBookingBar()
}


@Preview
@Composable
private fun BusinessDetailsPreview() {

    BusinessDetailsScreen()

}
