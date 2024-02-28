package com.example.tripnila.screens

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.tripnila.R
import com.example.tripnila.common.AppLocationCard
import com.example.tripnila.common.AppOutlinedButton
import com.example.tripnila.common.AppReviewsCard
import com.example.tripnila.common.LoadingScreen
import com.example.tripnila.common.Tag
import com.example.tripnila.common.UnderlinedText
import com.example.tripnila.data.AmenityBrief
import com.example.tripnila.data.DailySchedule
import com.example.tripnila.data.ReviewUiState
import com.example.tripnila.data.setImageForAmenity
import com.example.tripnila.model.BusinessDetailViewModel


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BusinessDetailsScreen(
    touristId: String = "",
    businessId: String = "",
    businessDetailViewModel: BusinessDetailViewModel
){

    LaunchedEffect(businessId) {
        businessDetailViewModel.getSelectedBusiness(businessId)
        Log.d("Tourist Id", touristId)
    }

    val business by businessDetailViewModel.business.collectAsState()

    val amenities = business.amenities.map { amenity ->
        AmenityBrief(
            image = setImageForAmenity(amenity.amenityName),
            name = amenity.amenityName
        )
    }

    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f
    ) {
        business.businessImages.size
    }

    val dailySchedule = listOf(
        DailySchedule(
            day = "Monday",
            openingTime = business.schedule.find { it.day == "Monday" }?.openingTime ?: "00:00 AM",
            closingTime = business.schedule.find { it.day == "Monday" }?.closingTime ?: "00:00 PM",
            isOpen = business.schedule.any { it.day == "Monday" }
        ),
        DailySchedule(
            day = "Tuesday",
            openingTime = business.schedule.find { it.day == "Tuesday" }?.openingTime ?: "00:00 AM",
            closingTime = business.schedule.find { it.day == "Tuesday" }?.closingTime ?: "00:00 PM",
            isOpen = business.schedule.any { it.day == "Tuesday" }
        ),
        DailySchedule(
            day = "Wednesday",
            openingTime = business.schedule.find { it.day == "Wednesday" }?.openingTime ?: "00:00 AM",
            closingTime = business.schedule.find { it.day == "Wednesday" }?.closingTime ?: "00:00 PM",
            isOpen = business.schedule.any { it.day == "Wednesday" }
        ),
        DailySchedule(
            day = "Thursday",
            openingTime = business.schedule.find { it.day == "Thursday" }?.openingTime ?: "00:00 AM",
            closingTime = business.schedule.find { it.day == "Thursday" }?.closingTime ?: "00:00 PM",
            isOpen = business.schedule.any { it.day == "Thursday" }
        ),
        DailySchedule(
            day = "Friday",
            openingTime = business.schedule.find { it.day == "Friday" }?.openingTime ?: "00:00 AM",
            closingTime = business.schedule.find { it.day == "Friday" }?.closingTime ?: "00:00 PM",
            isOpen = business.schedule.any { it.day == "Friday" }
        ),
        DailySchedule(
            day = "Saturday",
            openingTime = business.schedule.find { it.day == "Saturday" }?.openingTime ?: "00:00 AM",
            closingTime = business.schedule.find { it.day == "Saturday" }?.closingTime ?: "00:00 PM",
            isOpen = business.schedule.any { it.day == "Saturday" }
        ),
        DailySchedule(
            day = "Sunday",
            openingTime = business.schedule.find { it.day == "Sunday" }?.openingTime ?: "00:00 AM",
            closingTime = business.schedule.find { it.day == "Sunday" }?.closingTime ?: "00:00 PM",
            isOpen = business.schedule.any { it.day == "Sunday" }
        )
    )

//    val promos = listOf("Senior citizen & PWD : 20%")
//    val tags = listOf("Food", "Bar")

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
        if (businessDetailViewModel.isStateRetrieved.collectAsState().value == false) {
            LoadingScreen(isLoadingCompleted = false, isLightModeActive = true)
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                item {
                    HorizontalPager(
                        state = pagerState, // Specify the count of items in the pager
                        modifier = Modifier.fillMaxSize()
                    ) { page ->
                        val image = business.businessImages.sortedBy { it.photoType }.getOrNull(page)
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(240.dp)
                        ) {
                            if (image != null) {
                                AsyncImage(
                                    model = image.photoUrl,
                                    contentDescription = "",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = Color.Black.copy(alpha = 0.69f)
                                ),
                                shape = RoundedCornerShape(20.dp),
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .padding(horizontal = 15.dp, vertical = 25.dp)
                                    .width(30.dp)
                                    .height(20.dp)
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = "${page + 1}/${business.businessImages.size}",
                                        color = Color.White,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium,
                                    )
                                }

                            }


                        }

                    }



//                Box(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(240.dp)
//                )  {
//                    AsyncImage(
//                        model = if ( business.businessImages.find { it.photoType == "Cover" }?.photoUrl == "") "https://upload.wikimedia.org/wikipedia/commons/thumb/3/3f/Placeholder_view_vector.svg/1022px-Placeholder_view_vector.svg.png" else business.businessImages.find { it.photoType == "Cover" }?.photoUrl,//imageLoader,
//                        contentDescription = "",
//                        contentScale = ContentScale.Crop,
//                        modifier = Modifier.fillMaxSize()
//                    )
//                   // TopBarIcons()
//                }
                }
                item {
                    BusinessDescriptionCard1(
                        businessName = business.businessTitle,
                        tags = business.businessTags.map { it.tagName },
                        location = business.businessLocation,
                        averageRating = 4.7,
                        totalReviews = 254,
                        modifier = Modifier
                            .offset(y = (-17).dp)
                    )
                }
                item {
                    BusinessDescriptionCard2(
                        hostImage = business.host.profilePicture,
                        hostName = business.host.firstName,
                        businessDescription = business.businessDescription,
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
                        menuImage = business.businessMenu.find { it.photoType == "Cover" }?.photoUrl  ?: "",
                        entranceFee = business.entranceFee,
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
                        location = business.businessLocation,
                        locationImage = R.drawable.map_image2,
                        locationDescription =  business.additionalInfo, // CHANGE ADDITIONAL INFO INTO LOCATION INFO
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
}

@Composable
fun BusinessDescriptionCard1(
    modifier: Modifier = Modifier,
    businessName: String,
    tags: List<String>,
    location: String,
    averageRating: Double,
    totalReviews: Int,
    withEditButton: Boolean = false,
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
    modifier: Modifier = Modifier,
    hostImage: String,
    hostName: String,
    businessDescription: String,
    withEditButton: Boolean = false
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
    modifier: Modifier = Modifier,
    amenities: List<AmenityBrief>,
    withEditButton: Boolean = false,

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
    modifier: Modifier = Modifier,
    menuImage: String,
    entranceFee: Double,
    withEditButton: Boolean = false,

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
                text = "Entrance Fee",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .padding(top = 10.dp)
                    .align(Alignment.Start)
            )
            if(entranceFee != 0.0){
                Text(
                    text = "₱ $entranceFee",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .align(Alignment.Start)
                )
            } else{
                Text(
                    text = "No Entrance Fee",
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
    modifier: Modifier = Modifier,
    dailySchedule: List<DailySchedule>,
    withEditButton: Boolean = false,

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

//@Preview
//@Composable
//private fun BusinessDetailsItemPreviews(){
//
//    BusinessBottomBookingBar()
//}


@Preview
@Composable
private fun BusinessDetailsPreview() {

    val businessDetailViewModel = viewModel(modelClass = BusinessDetailViewModel::class.java)

    BusinessDetailsScreen(
        touristId = "ITZbCFfF7Fzqf1qPBiwx",
        businessId = "10005",
        businessDetailViewModel = businessDetailViewModel
    )

}
