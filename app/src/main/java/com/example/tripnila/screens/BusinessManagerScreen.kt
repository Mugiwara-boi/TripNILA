package com.example.tripnila.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tripnila.R
import com.example.tripnila.components.AppLocationCard
import com.example.tripnila.components.AppReviewsCard
import com.example.tripnila.data.Amenity
import com.example.tripnila.data.DailySchedule
import com.example.tripnila.data.Review

@Composable
fun BusinessManagerScreen(){

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
                    withEditButton = true,
                    modifier = Modifier
                        .offset(y = (-17).dp)
                )
            }
            item {
                BusinessDescriptionCard2(
                    hostImage = R.drawable.joshua,
                    hostName = "Juswa",
                    businessDescription = "Enjoy our wide selection of meat and vegetables in our menu, along with our selection of cocktails and drinks. Chill and have fun with your families and friends.",
                    withEditButton = true,
                    modifier = Modifier
                        .offset(y = (-5).dp)
                        .padding(bottom = 12.dp)
                )
            }
            item {
                BusinessAmenitiesCard(
                    amenities = amenities,
                    withEditButton = true,
                    modifier = Modifier
                        .offset(y = (-5).dp)
                        .padding(bottom = 12.dp)
                )
            }
            item {
                BusinessMenuCard(
                    menuImage = R.drawable.business1,
                    promos = promos,
                    withEditButton = true,
                    modifier = Modifier
                        .offset(y = (-5).dp)
                        .padding(bottom = 12.dp)
                )
            }
            item {
                BusinessScheduleCard(
                    dailySchedule = dailySchedule,
                    withEditButton = true,
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
                    withEditButton = true,
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

@Preview
@Composable
private fun BusinessManagerItemPreviews(){


}


@Preview
@Composable
private fun BusinessManagerPreview() {

    BusinessManagerScreen()

}