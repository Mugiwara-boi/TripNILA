package com.example.tripnila.screens

import android.util.Log
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.tripnila.R
import com.example.tripnila.common.AppDropDownFilterWithCallback
import com.example.tripnila.common.AppFilledButton
import com.example.tripnila.common.AppLocationCard
import com.example.tripnila.common.LoadingScreen
import com.example.tripnila.data.AmenityBrief
import com.example.tripnila.data.Business
import com.example.tripnila.data.DailySchedule
import com.example.tripnila.data.ReviewUiState
import com.example.tripnila.model.BusinessManagerViewModel
import com.example.tripnila.model.BusinessViewsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.time.LocalDate

@Composable
fun BusinessManagerScreen(
    businessManagerViewModel: BusinessManagerViewModel? = null,
    businessId: String = "",
    hostId: String = "",
    onNavToEditBusiness: (String, String, String) -> Unit,
    onNavToDashboard: (String) -> Unit,
    businessViewsViewModel: BusinessViewsViewModel,
    onNavToGeneratedViewsReport: (String) -> Unit,
){

    Log.d("BusinessManagerScreen", "$businessId $hostId")

    LaunchedEffect(businessId) {
        businessManagerViewModel?.getSelectedBusiness(businessId)
        businessViewsViewModel.fetchBusinessViews(businessId)
    }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val business = businessManagerViewModel?.business?.collectAsState()?.value ?: Business()
    businessViewsViewModel.setBusiness(business)
    val touristId = hostId.substring(5)
    val minSpend = business?.minSpend ?: 0.0
    val entranceFee = business?.entranceFee ?: 0.0

    val amenities = business?.amenities?.map { amenity ->
        AmenityBrief(
            image = R.drawable.person,
            name = amenity.amenityName
        )
    } ?: emptyList()

//    val isFetchingBusinessViews by businessViewsViewModel.isFetchingBusinessViews.collectAsState()
//    val isBusinessViewsFetched by businessViewsViewModel.isBusinessViewsFetched.collectAsState()

//    LaunchedEffect(
//        isBusinessViewsFetched
//    ) {
//        if (isBusinessViewsFetched) {
//            onNavToGeneratedViewsReport("viewsReport")
//            businessViewsViewModel.resetFetchViewsStatus()
//        }
//    }
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
                    BusinessInsightsCard(
                        businessViewsViewModel = businessViewsViewModel,
                        onClick = {
                            scope.launch {
                                onNavToGeneratedViewsReport("viewsReport")


                            }
                        },
                        modifier = Modifier
                            .offset(y = (-5).dp)
                            .padding(bottom = 12.dp)
                    )
                }
                item {
                    BusinessMenuCard(
                        menuImage = business?.businessMenu?.find { it.photoType == "Cover" }?.photoUrl  ?: "https://upload.wikimedia.org/wikipedia/commons/thumb/3/3f/Placeholder_view_vector.svg/1022px-Placeholder_view_vector.svg.png",
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
                        lat = business?.businessLat!!,
                        lng = business?.businessLng!!,
                        context = context,
                        locationDescription =  business?.additionalInfo ?: "", // CHANGE ADDITIONAL INFO INTO LOCATION INFO
                        modifier = Modifier
                            .offset(y = (-5).dp)
                            .padding(bottom = 12.dp)
                    )

                }
//                item {
//                    AppReviewsCard(
//                        reviews = reviews,
//                        onSeeAllReviews = {
//
//                        },
//                        modifier = Modifier
//                            .offset(y = (-5).dp)
//                            .padding(bottom = 7.dp)
//                    )
//                }
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

@Composable
fun BusinessInsightsCard(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    businessViewsViewModel: BusinessViewsViewModel
) {
//    val scope = rememberCoroutineScope()
//    var insightsSelectedCategory by remember { mutableStateOf("Monthly") }

    val viewCount by businessViewsViewModel.viewCount.collectAsState()
    val insightsSelectedYear by businessViewsViewModel.insightsSelectedYear.collectAsState()
    val isInitialLaunch by businessViewsViewModel.isInitialLaunch.collectAsState()

    val yearOptions = ((2023..LocalDate.now().year).toList().map { it.toString() } + "All").reversed()

    LaunchedEffect(Unit) {
        if (isInitialLaunch) {
            businessViewsViewModel.setInsightsSelectedYear("All")
        }
    }




    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp
        ),
        modifier = modifier
            .fillMaxWidth()
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
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically // Optional alignment
            ) {
                Text(
                    text = "Insight",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f) // Allow text to expand to fill available space
                )
                AppDropDownFilterWithCallback(
                    options = yearOptions,
                    fontSize = 10.sp,
                    selectedCategory = insightsSelectedYear,
                    onCategorySelected = { newCategory ->
                        businessViewsViewModel.setInsightsSelectedYear(newCategory)
                    },
                    modifier = Modifier.align(Alignment.CenterVertically) // Align at the center vertically
                )
            }
            Row(
                modifier = Modifier.padding(top = 5.dp)
            ){
                InsightInfoCard(
                    modifier = Modifier.weight(.4f),
                    cardLabel = "Views",
                    cardInfoCount = viewCount
                )
                AppFilledButton(
                    buttonText = "Generate Views Report",
                    isLoading = false, //isFetchingBusinessViews,
                    onClick = {
                        onClick()

                    },
                )
            }



        }
    }

}

//@Preview
//@Composable
//private fun BusinessManagerPreview() {
//
//    val businessManagerViewModel = viewModel(modelClass = BusinessManagerViewModel::class.java)
//    val businessViewsViewModel = viewModel(modelClass = BusinessViewsViewModel::class.java)
//    BusinessManagerScreen(
//        businessManagerViewModel = businessManagerViewModel,
//        businessViewsViewModel = businessViewsViewModel,
//        businessId = "10001",
//        hostId = "HOST-ITZbCFfF7Fzqf1qPBiwx",
//        onNavToEditBusiness = { a,b,c ->
//
//        },
//        onNavToDashboard = {
//
//        }
//    )
//
//}