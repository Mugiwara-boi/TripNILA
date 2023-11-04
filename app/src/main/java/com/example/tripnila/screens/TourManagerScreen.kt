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
import com.example.tripnila.data.Review
import com.example.tripnila.data.TourAvailableDates
import com.example.tripnila.data.Transaction

@Composable
fun TourManagerScreen(){

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

    val transactions = listOf(
        Transaction(
            customerImage = R.drawable.joshua,
            customerName = "Juan Cruz",
            customerUsername = "@jcruz",
            guestsCount = 4,
            price = 2500.00,
            bookedDate = "Sep 3, 2023",
            transactionDate = "Aug 24, 2023",
            transactionStatus = "Completed"
        ),
        Transaction(
            customerImage = R.drawable.joshua,
            customerName = "Juan Cruz",
            customerUsername = "@jcruz",
            guestsCount = 4,
            price = 2500.00,
            bookedDate = "Sep 6, 2023",
            transactionDate = "Aug 24, 2023",
            transactionStatus = "Completed"
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
                    withEditButton = true,
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
                    withEditButton = true,
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
                    withEditButton = true,
                    modifier = Modifier
                        .offset(y = (-5).dp)
                        .padding(bottom = 12.dp)
                )
            }
//            item {
////                     AVAILABILITY DATES
//                TourAvailabilityCard(
//                    availableDates = availableDates,
//                    modifier = Modifier
//                        .offset(y = (-5).dp)
//                        .padding(bottom = 12.dp)
//                )
//            }

            item{
                AppTransactionsCard(
                    transactions = transactions,
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
                        .padding(bottom = 12.dp)
                )
            }
            item {
                TourAdditionalInformationCard(
                    withEditButton = true,
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

@Preview
@Composable
private fun TourManagerPreviews(){

    // TourBottomBookingBar()

}


@Preview
@Composable
private fun TourManagerScreenPreview() {

    TourManagerScreen()

}