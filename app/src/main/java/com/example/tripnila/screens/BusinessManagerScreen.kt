package com.example.tripnila.screens

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.tripnila.R
import com.example.tripnila.common.AppLocationCard
import com.example.tripnila.common.AppReviewsCard
import com.example.tripnila.common.LoadingScreen
import com.example.tripnila.data.AmenityBrief
import com.example.tripnila.data.DailySchedule
import com.example.tripnila.data.ReviewUiState
import com.example.tripnila.model.BusinessManagerViewModel

@Composable
fun BusinessManagerScreen(
    businessManagerViewModel: BusinessManagerViewModel? = null,
    businessId: String = "",
    hostId: String = "",
    onNavToEditBusiness: (String, String, String) -> Unit,
    onNavToDashboard: (String) -> Unit,
){

    Log.d("BusinessManagerScreen", "$businessId $hostId")

    LaunchedEffect(businessId) {
        businessManagerViewModel?.getSelectedBusiness(businessId)
    }

    val business = businessManagerViewModel?.business?.collectAsState()?.value
    val touristId = hostId.substring(5)
    val minSpend = business?.minSpend ?: 0.0
    val entranceFee = business?.entranceFee ?: 0.0

    val amenities = business?.amenities?.map { amenity ->
        AmenityBrief(
            image = R.drawable.person,
            name = amenity.amenityName
        )
    } ?: emptyList()

    val dailySchedule = listOf(
        DailySchedule(
            day = "Monday",
            openingTime = business?.schedule?.find { it.day == "Monday" }?.openingTime ?: "00:00 AM",
            closingTime = business?.schedule?.find { it.day == "Monday" }?.closingTime ?: "00:00 PM",
            isOpen = business?.schedule?.any { it.day == "Monday" } == true
        ),
        DailySchedule(
            day = "Tuesday",
            openingTime = business?.schedule?.find { it.day == "Tuesday" }?.openingTime ?: "00:00 AM",
            closingTime = business?.schedule?.find { it.day == "Tuesday" }?.closingTime ?: "00:00 PM",
            isOpen = business?.schedule?.any { it.day == "Tuesday" } == true
        ),
        DailySchedule(
            day = "Wednesday",
            openingTime = business?.schedule?.find { it.day == "Wednesday" }?.openingTime ?: "00:00 AM",
            closingTime = business?.schedule?.find { it.day == "Wednesday" }?.closingTime ?: "00:00 PM",
            isOpen = business?.schedule?.any { it.day == "Wednesday" } == true
        ),
        DailySchedule(
            day = "Thursday",
            openingTime = business?.schedule?.find { it.day == "Thursday" }?.openingTime ?: "00:00 AM",
            closingTime = business?.schedule?.find { it.day == "Thursday" }?.closingTime ?: "00:00 PM",
            isOpen = business?.schedule?.any { it.day == "Thursday" } == true
        ),
        DailySchedule(
            day = "Friday",
            openingTime = business?.schedule?.find { it.day == "Friday" }?.openingTime ?: "00:00 AM",
            closingTime = business?.schedule?.find { it.day == "Friday" }?.closingTime ?: "00:00 PM",
            isOpen = business?.schedule?.any { it.day == "Friday" } == true
        ),
        DailySchedule(
            day = "Saturday",
            openingTime = business?.schedule?.find { it.day == "Saturday" }?.openingTime ?: "00:00 AM",
            closingTime = business?.schedule?.find { it.day == "Saturday" }?.closingTime ?: "00:00 PM",
            isOpen = business?.schedule?.any { it.day == "Saturday" } == true
        ),
        DailySchedule(
            day = "Sunday",
            openingTime = business?.schedule?.find { it.day == "Sunday" }?.openingTime ?: "00:00 AM",
            closingTime = business?.schedule?.find { it.day == "Sunday" }?.closingTime ?: "00:00 PM",
            isOpen = business?.schedule?.any { it.day == "Sunday" } == true
        )
    )

    val promos = listOf("Senior citizen & PWD : 20%")

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
           // touristImage = R.drawable.joshua,
            touristName = "Jane Smith",
            reviewDate = "2023-04-20"
        ),
        ReviewUiState(
            rating = 5.0,
            comment = "Amazing place and great service!",
          //  touristImage = R.drawable.joshua,
            touristName = "Jane Smith",
            reviewDate = "2023-04-20"
        ),
        ReviewUiState(
            rating = 5.0,
            comment = "Amazing place and great service!",
          //  touristImage = R.drawable.joshua,
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
          //  touristImage = R.drawable.joshua,
            touristName = "Jane Smith",
            reviewDate = "2023-04-20"
        ),
    )



    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = Color(0xFFEFEFEF)
    ) {
        if (businessManagerViewModel?.isStateRetrieved?.collectAsState()?.value == false) {
            LoadingScreen(isLoadingCompleted = false, isLightModeActive = true)
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(240.dp)
                    ) {
                        AsyncImage(
                            model = if ( business?.businessImages?.find { it.photoType == "Cover" }?.photoUrl == "") "https://upload.wikimedia.org/wikipedia/commons/thumb/3/3f/Placeholder_view_vector.svg/1022px-Placeholder_view_vector.svg.png" else business?.businessImages?.find { it.photoType == "Cover" }?.photoUrl,//imageLoader,
                            contentDescription = "",
                            contentScale = ContentScale.Crop,

                        )
                        ManagerTopBarIcons(
                            onEdit = {
                                onNavToEditBusiness(businessId, hostId, "Business")
                            },
                            onBack = {
                                onNavToDashboard(touristId)
                            }
                        )
                    }
                }
                item {
                    BusinessDescriptionCard1(
                        businessName = business?.businessTitle ?: "",
                        tags = business?.businessTags?.map { it.tagName } ?: emptyList(),
                        location = business?.businessLocation ?: "",
                        averageRating = 4.7,
                        totalReviews = 254,
                        withEditButton = false,
                        modifier = Modifier
                            .offset(y = (-17).dp)
                    )
                }
                item {
                    BusinessDescriptionCard2(
                        hostImage = business?.host?.profilePicture ?: "",
                        hostName = business?.host?.firstName ?: "",
                        businessDescription = business?.businessDescription ?: "",
                        withEditButton = false,
                        modifier = Modifier
                            .offset(y = (-5).dp)
                            .padding(bottom = 12.dp)
                    )
                }
                item {
                    BusinessAmenitiesCard(
                        amenities = amenities,
                        withEditButton = false,
                        modifier = Modifier
                            .offset(y = (-5).dp)
                            .padding(bottom = 12.dp)
                    )
                }
                item {
                    BusinessMenuCard(
                        menuImage = business?.businessMenu?.find { it.photoType == "Cover" }?.photoUrl  ?: "",
                        entranceFee = entranceFee,
                        withEditButton = false,
                        modifier = Modifier
                            .offset(y = (-5).dp)
                            .padding(bottom = 12.dp)
                    )
                }
                item {
                    BusinessScheduleCard(
                        dailySchedule = dailySchedule,
                        withEditButton = false,
                        modifier = Modifier
                            .offset(y = (-5).dp)
                            .padding(bottom = 12.dp)
                    )
                }
                item {
                    AppLocationCard(
                        location = business?.businessLocation ?: "",
                        locationImage = R.drawable.map_image2,
                        locationDescription =  business?.additionalInfo ?: "", // CHANGE ADDITIONAL INFO INTO LOCATION INFO
                        withEditButton = false,
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
//                item {
//                    BusinessBottomBookingBar()
//                }
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

    val businessManagerViewModel = viewModel(modelClass = BusinessManagerViewModel::class.java)

    BusinessManagerScreen(
        businessManagerViewModel = businessManagerViewModel,
        businessId = "10001",
        hostId = "HOST-ITZbCFfF7Fzqf1qPBiwx",
        onNavToEditBusiness = { a,b,c ->

        },
        onNavToDashboard = {

        }
    )

}