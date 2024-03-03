package com.example.tripnila.screens

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.areNavigationBarsVisible
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.tripnila.R
import com.example.tripnila.common.AdditionalInformationRow
import com.example.tripnila.common.AppExpandingText
import com.example.tripnila.common.AppLocationCard
import com.example.tripnila.common.AppOutlinedButton
import com.example.tripnila.common.AppReviewsCard
import com.example.tripnila.common.EmptyPlaceholder
import com.example.tripnila.common.LoadingScreen
import com.example.tripnila.common.Tag
import com.example.tripnila.common.UnderlinedText
import com.example.tripnila.data.ReviewUiState
import com.example.tripnila.data.TourAvailableDates
import com.example.tripnila.model.TourDetailsViewModel
import com.example.tripnila.navigateToTourDates
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale


@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class,
    ExperimentalLayoutApi::class
)
@Composable
fun TourDetailsScreen(
    touristId: String = "",
    tourId: String = "",
    tourDetailsViewModel: TourDetailsViewModel,
    onNavToChooseDate: (String) -> Unit,
    onNavToChat: (String, String) -> Unit,
    onBack: () -> Unit
){

    LaunchedEffect(tourId) {
        tourDetailsViewModel.getSelectedTour(tourId)
        tourDetailsViewModel.getAllReviewsByTourId(tourId)
        Log.d("Tourist ID", touristId)
        Log.d("Tour ID", tourId)
    }

    val tour by tourDetailsViewModel.tour.collectAsState()
    val personCount by tourDetailsViewModel.personCount.collectAsState()
    val tourBookings by tourDetailsViewModel.tourBookings.collectAsState()
    val isUserVerified by tourDetailsViewModel.isUserVerified.collectAsState()

    val filteredReviews = tourBookings
        .mapNotNull { it.bookingReview }
        .filter { it.bookingId != "" }
        .sortedByDescending { it.reviewDate }

    val averageRating = filteredReviews
        .map { it.rating }
        .average()

    val validAverage = if (averageRating.isNaN()) 0.0 else averageRating

    val totalReviews = filteredReviews
        .size

    val reviews = filteredReviews.map { review ->
        ReviewUiState(
            rating = review.rating.toDouble(),
            comment = review.comment,
            touristImage = review.reviewer.profilePicture,
            touristName = "${review.reviewer.firstName} ${review.reviewer.lastName}",
            reviewDate = review.reviewDate.toString()
        )

    }

   // val limit = tour.schedule.
    val count by tourDetailsViewModel.tempCount.collectAsState()

    val tourHost = tour.host

    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f
    ) {
        tour.tourImages.size
    }

    val bottomSheetState = rememberModalBottomSheetState( skipPartiallyExpanded = true)
    val hasNavigationBar = WindowInsets.areNavigationBarsVisible
    val scope = rememberCoroutineScope()

    val context = LocalContext.current
    var openBottomSheet by remember { mutableStateOf(false) }

    val tourAvailableDates = tour.schedule

    val currentDate = LocalDate.now()

    val availableDatesBeforeGuestCount = tourAvailableDates
        .filter { tourSchedule ->
            val remainingSlot = tourSchedule.slot - tourSchedule.bookedSlot
            tourSchedule.date > currentDate && remainingSlot >= 1 // Keep only schedules with remaining slots
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

    var isFavorite by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(touristId) {
        isFavorite = tourDetailsViewModel.isFavorite(tourId, touristId)
        tourDetailsViewModel.incrementViewCount(tourId)
        tourDetailsViewModel.verifyUser(touristId)
    }

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
//    }.sortedBy { it.localDate }


    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = Color(0xFFEFEFEF)
    ) {
        Scaffold(
            bottomBar = {

                if (isUserVerified == true) {
                    TourBottomBookingBar(
                        tourPrice = tour.tourPrice,
                        underlinedText = if (personCount == 0) {
                            "Select guests"
                        } else if (personCount == 1) {
                            "for $personCount person"
                        }
                        else {
                            "for $personCount persons"
                        },
                        enableButton = personCount > 0,
                        onClickChatHost = {
                            onNavToChat(touristId, tourHost.hostId.substring(5))
                        },
                        onClickChooseDate = {
                            onNavToChooseDate(touristId)
                        },
                        onClickUnderlinedText = {
                            openBottomSheet = true
                        }

                    )
                } else {
                    Surface(
                        color = Color.White,
                        tonalElevation = 10.dp,
                        shadowElevation = 10.dp,
                        modifier = Modifier
                            .height(78.dp)
                            .fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(
                                    horizontal = 25.dp,
                                    //vertical = 25.dp
                                ),
                            verticalArrangement = Arrangement.Center
                            //horizontalAlignment = Alignment.CenterHorizontally
                        ){
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {

                                Text(
                                    text = "Account needs verification",
                                    //  fontColor = if (isUserVerified?.value == true) Color.Black else Color(0xffCC0033),
                                    color =  Color(0xFFCC0033),
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier
                                        //   .padding(vertical = 10.dp, horizontal = horizontalPaddingValue)
                                        .fillMaxWidth()
                                    //  .wrapContentWidth(align = Alignment.Start)
                                )
                            }

                        }

                    }
                }



            }
        ) {
            if (tour.tourId != tourId) {
                LoadingScreen(isLoadingCompleted = false, isLightModeActive = true)
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it)
                ) {
                    item {
                        HorizontalPager(
                            state = pagerState, // Specify the count of items in the pager
                            modifier = Modifier.fillMaxSize()
                        ) { page ->
                            val image = tour.tourImages.sortedBy { it.photoType }.getOrNull(page)
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
                                DetailsTopAppBar(
                                    onBack = {
                                        onBack()
                                    },
                                    isFavorite = isFavorite,
                                    onCheckedChange = { isChecked ->

                                        scope.launch {
                                            tourDetailsViewModel.toggleFavorite(
                                                tourId,
                                                touristId,
                                                "Tour"
                                            )
                                            isFavorite = isChecked
                                        }

                                    },
                                )
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
                                            text = "${page + 1}/${tour.tourImages.size}",
                                            color = Color.White,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Medium,
                                        )
                                    }

                                }


                            }

                        }


                    }
                    item {
                        TourDescriptionCard1(
                            tourName = tour.tourTitle,
                            tags = tour.tourTags.map { it.tagName },
                            location = tour.tourLocation,
                            averageRating = 4.7,
                            totalReviews = 254,
                            modifier = Modifier
                                .offset(y = (-17).dp)
                        )
                    }
                    item {
                        TourDescriptionCard2(
                            hostImage = tour.host.profilePicture,
                            hostName = tour.host.firstName,
                            duration = tour.tourDuration,
                            language = tour.tourLanguage,
                            withEditButton = false,
                            modifier = Modifier
                                .offset(y = (-5).dp)
                                .padding(bottom = 12.dp)
                        )
                    }
                    item {
                        tour.tourImages.map { it.photoUrl }.let { it1 ->
                            TourDescriptionCard3(
                                images = it1,
                                description = tour.tourDescription,
                                withEditButton = false,
                                modifier = Modifier
                                    .offset(y = (-5).dp)
                                    .padding(bottom = 12.dp)
                            )
                        }
                    }
                    item {
                        TourAvailabilityCard(
                            availableDates = availableDatesBeforeGuestCount,
                            modifier = Modifier
                                .offset(y = (-5).dp)
                                .padding(bottom = 12.dp)
                        )
                    }
                    item {
                        AppLocationCard(
                            location = "Where we'll meet",
                            lat = tour.tourLat,
                            lng = tour.tourLng,
                            context = context,
                            locationDescription = tour.tourLocation,
                            modifier = Modifier
                                .offset(y = (-5).dp)
                                .padding(bottom = 12.dp)
                        )
                    }
                    item {
                        AppReviewsCard(
                            reviews = reviews,
                            totalReviews = totalReviews,
                            averageRating = validAverage,
                            onSeeAllReviews = {

                            },
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

                }
            }
        }


        if (openBottomSheet) {
            ModalBottomSheet(
                shape = RoundedCornerShape(20.dp),
                containerColor = Color.White,
                dragHandle = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start,
                        modifier = Modifier
                            .padding(start = 3.dp, end = 16.dp) //, top = 3.dp
                            .fillMaxWidth()
                    ) {
                        IconButton(
                            onClick = {
                                openBottomSheet = false
                            },
                        ) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = "Close"
                            )
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        ClickableText(
                            text = buildAnnotatedString {
                                withStyle(
                                    style = SpanStyle(
                                        fontSize = 16.sp,
                                        textDecoration = TextDecoration.Underline
                                    )
                                ) {
                                    append("Clear")
                                }
                            },
                            onClick = {
                                tourDetailsViewModel.setTempCount(0)
                            }

                        )


                    }
                },
                onDismissRequest = { openBottomSheet = false },
                sheetState = bottomSheetState,
                modifier = Modifier
                    .fillMaxHeight(0.25f) //0.693
            ) {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Row(
                        modifier = Modifier
                            .background(Color.White)
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 10.dp)

                    ) {
                        Text(
                            text = "Guests",
                            color = Color(0xff333333),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        IconButton(
                            onClick = {
                                if (count > 0) {
                                   // tourDetailsViewModel.decrementPersonCount()
                                    //count--
                                    tourDetailsViewModel.decrementCount()
                                }
                            },
                            enabled = count > 0,
                            modifier = Modifier.size(17.dp)
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.subtract_circle),
                                contentDescription = "Subtract",
                                tint = if (count > 0) Color(0xff333333) else Color(0xFFDEDEDE)
                            )
                        }
                        Text(
                            text = count.toString(),
                            color = Color(0xff333333),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(horizontal = 15.dp)
                        )
                        IconButton(
                            onClick = {
                             //   if (count < limit) {
                                  //  tourDetailsViewModel.incrementPersonCount()
                                    tourDetailsViewModel.incrementCount()
                             //   }
                            },
                          //  enabled = count < limit, //
                            modifier = Modifier.size(17.dp)
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.add_circle),
                                contentDescription = "Add",
                               // tint = if (count < limit) Color(0xff333333) else Color(0xFFDEDEDE),
                                tint = Color(0xff333333)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.weight(1f))
                    ConfirmCount(
                        tourPrice = tour.tourPrice,
                        personCount = count,
                        enableButton = count != personCount,
                        onClickSave = {
                        //    isSaveButtonClicked = true
                            tourDetailsViewModel.setTempCount(count)
                            tourDetailsViewModel.setPersonCount(count)
                            openBottomSheet = false

                        },
                        modifier = Modifier
                            .noPaddingIf(hasNavigationBar)
                    )
                    Spacer(modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars))
                }

            }


        }
    }
}

@Composable
fun TourBottomBookingBar(
    modifier: Modifier = Modifier,
    tourPrice: Double,
    bookButtonText: String = "Pick date",
    underlinedText: String = "",
    enableButton: Boolean = true,
    onClickChatHost: () -> Unit,
    onClickChooseDate: () -> Unit,
    onClickUnderlinedText: () -> Unit,
){
    val formattedNumber = NumberFormat.getNumberInstance().format(tourPrice)

    Surface(
        color = Color.White,
        tonalElevation = 10.dp,
        shadowElevation = 10.dp,
        modifier = modifier
            .height(78.dp)
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(
                    horizontal = 25.dp,
                    //vertical = 25.dp
                ),
            verticalArrangement = Arrangement.Center
            //horizontalAlignment = Alignment.CenterHorizontally
        ){
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    Text(
                        text = buildAnnotatedString {
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                //append("₱ ${"%.2f".format(staycation?.staycationPrice)}")
                                append("₱ $formattedNumber")

                            }
                            append("/person")
                        }
                    )
                    ClickableText(
                        text =  buildAnnotatedString {
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Medium,
                                color = Color(0xff999999), //
                                textDecoration = TextDecoration.Underline)
                            ) {
                                append(underlinedText)
                            }
                        },
                        onClick = {
                            onClickUnderlinedText()
                        }

                    )



                }
                BookingOutlinedButton(
                    buttonText = "Chat host",
                    onClick = {
                        onClickChatHost()
                    },
                    modifier = Modifier.padding(horizontal = 15.dp)
                )
                BookingFilledButton(
                    buttonText = bookButtonText,
                    onClick = {
                        onClickChooseDate()
                    },
                    enabled = enableButton,
                    modifier = Modifier.width(90.dp)
                )
            }

        }

    }
}


@Composable
fun TourDescriptionCard1(
    modifier: Modifier = Modifier,
    tourName: String,
    tags: List<String>,
    location: String,
    averageRating: Double,
    totalReviews: Int,
    withEditButton: Boolean = false
) {

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
    modifier: Modifier = Modifier,
    hostImage: String,
    hostName: String,
    duration: String,
    language: String,
    withEditButton: Boolean = false,
){

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TourDescriptionCard3(
    modifier: Modifier = Modifier,
    description: String,
    images: List<String?>,
    withEditButton: Boolean = false,
){


    val seeAllImages = remember { mutableStateOf(false) }
    val imagesBottomSheet = rememberModalBottomSheetState(skipPartiallyExpanded = true)

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

            LazyVerticalGrid(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .height(120.dp),
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                items(images.take(2)) { imageUrl ->
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color.LightGray)
                    ) {
                        if (imageUrl != null) {
                            AsyncImage(
                                model = imageUrl.ifBlank {
                                    "https://upload.wikimedia.org/wikipedia/commons/thumb/3/3f/Placeholder_view_vector.svg/1022px-Placeholder_view_vector.svg.png"
                                },
                                contentDescription = "",
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }
            }

            AppOutlinedButton(
                buttonText = "See all photos",
                onClick = {
                    seeAllImages.value = true
                }
            )
        }
    }

    if (seeAllImages.value) {
        ModalBottomSheet(
            shape = RoundedCornerShape(20.dp),
            containerColor = Color.White,
            dragHandle = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier
                        .padding(start = 3.dp, end = 16.dp) //, top = 3.dp
                        .fillMaxWidth()
                ) {
                    IconButton(
                        onClick = { seeAllImages.value = false },
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Close"
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))

                }
            },
            onDismissRequest = { seeAllImages.value = false },
            sheetState = imagesBottomSheet,
            modifier = Modifier
                .fillMaxHeight(0.8f) //0.693
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    // .padding(horizontal = 25.dp,)
                    .background(Color.White)
            ) {
                Text(
                    text = "Tour images",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 16.dp)
                        .fillMaxWidth()
                )

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(15.dp),
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    items(images) {  imageUrl ->
                        if (imageUrl != null) {
                            AsyncImage(
                                model = imageUrl.ifBlank {
                                    "https://upload.wikimedia.org/wikipedia/commons/thumb/3/3f/Placeholder_view_vector.svg/1022px-Placeholder_view_vector.svg.png"
                                },
                                contentDescription = "",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxWidth()
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
fun TourAvailabilityCard(
    modifier: Modifier = Modifier,
    availableDates: List<TourAvailableDates>,
){


    val seeAll = remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

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
                text = "Available schedules",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .align(Alignment.Start)
            )
            Text(
                text = if (availableDates.isEmpty() || availableDates.count() == 1) "${availableDates.count()} available schedule" else "${availableDates.count()} available schedules",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF666666),
                modifier = Modifier
                    .align(Alignment.Start)
            )

            if (availableDates.isEmpty()) {
                EmptyPlaceholder(
                    modifier = Modifier.padding(bottom = 18.dp, top = 7.dp)
                )
            } else {
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
            }

            AppOutlinedButton(
                buttonText = "See all schedules",
                onClick = {
                    seeAll.value = true
                }
            )
        }
    }

    if (seeAll.value) {
        ModalBottomSheet(
            shape = RoundedCornerShape(20.dp),
            containerColor = Color.White,
            dragHandle = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier
                        .padding(start = 3.dp, end = 16.dp) //, top = 3.dp
                        .fillMaxWidth()
                ) {
                    IconButton(
                        onClick = { seeAll.value = false },
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Close"
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))

                }
            },
            onDismissRequest = { seeAll.value = false },
            sheetState = sheetState,
            modifier = Modifier
                .fillMaxHeight(0.8f) //0.693
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    // .padding(horizontal = 25.dp,)
                    .background(Color.White)
            ) {
                Text(
                    text = "All available dates",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 16.dp)
                        .fillMaxWidth()
                )

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(20.dp), //Arrangement.spacedBy(16.dp),
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    items(availableDates) { availableDate ->
                        TourDateCardWithoutButton(
                            availableDate = availableDate,
                            isClicked = false,
                            onClick = {
                            }
                        )
                    }
                }
            }

        }
    }

}

@Composable
fun TourAdditionalInformationCard(modifier: Modifier = Modifier, withEditButton: Boolean = false){

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

//@Composable
//fun TourBottomBookingBar(modifier: Modifier = Modifier){
//    Box(
//        modifier = modifier
//            .fillMaxWidth()
//            .background(color = Color.White)
//            .border(0.1.dp, Color.Black)
//    ) {
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(horizontal = 25.dp, vertical = 12.dp),
//        ) {
//            BookingOutlinedButton(
//                buttonText = "Cancel",
//                onClick = {},
//                modifier = Modifier.width(120.dp)
//            )
//            Spacer(modifier = Modifier.weight(1f))
//            BookingFilledButton(
//                buttonText = "Choose date",
//                onClick = {},
//                modifier = Modifier.width(140.dp)
//            )
//        }
//    }
//}

@Composable
fun TourDateCard(
    availableDate: TourAvailableDates,
    modifier: Modifier = Modifier
){
    OutlinedCard(
        modifier = modifier
            .width(width = 135.dp)
            .height(height = 90.dp),
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
            Spacer(modifier = Modifier.weight(1f))
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
         //   Spacer(modifier = Modifier.weight(1f))
//            Button(
//                onClick = {},
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = Orange
//                ),
//                contentPadding = PaddingValues(2.dp),
//                modifier = Modifier
//                    .padding(bottom = 2.dp)
//                    .width(50.dp)
//                    .height(20.dp)
//
//            ){
//                Text(
//                    text = "Choose",
//                    color = Color.White,
//                    fontWeight = FontWeight.Medium,
//                    fontSize = 8.sp
//                )
//            }
        }
    }

}

@Composable
fun ConfirmCount(
    modifier: Modifier = Modifier,
    tourPrice: Double,
    personCount: Int,
    onClickSave: (Int) -> Unit,
    enableButton: Boolean = false,
) {

    val formattedTotalAmount = NumberFormat.getNumberInstance().format(tourPrice.times(personCount))
    val formattedNumber = NumberFormat.getNumberInstance().format(tourPrice)

    Surface(
        color = Color.White,
        tonalElevation = 10.dp,
        shadowElevation = 10.dp,
        modifier = modifier
            .height(78.dp)
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(
                    horizontal = 25.dp,
                    //vertical = 25.dp
                ),
            verticalArrangement = Arrangement.Center
            //horizontalAlignment = Alignment.CenterHorizontally
        ){
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    val annotatedText = if (personCount == 0) {
                        buildAnnotatedString {
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                //append("₱ ${"%.2f".format(staycation?.staycationPrice)}")
                                append("₱ $formattedNumber")

                            }
                            append("/person")
                        }
                    } else {
                        buildAnnotatedString {
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                //append("₱ ${"%.2f".format(staycation?.staycationPrice)}")
                                append("₱ $formattedTotalAmount")

                            }
                            append(" for ")
                            withStyle(style = SpanStyle(fontWeight = FontWeight.SemiBold)) {
                                //append("₱ ${"%.2f".format(staycation?.staycationPrice)}")
                                append("$personCount")

                            }
                            append(" person")
                        }
                    }


                    Text(
                        text = annotatedText
                    )

                }
                BookingFilledButton(
                    buttonText = "Save",
                    onClick = {
                        onClickSave(personCount)
                    },
                    enabled = enableButton,
                    modifier = Modifier.width(90.dp)
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


@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Preview
@Composable
private fun TourDetailsPreview() {

    val tourDetailsViewModel = viewModel(modelClass = TourDetailsViewModel::class.java)
    val navController = rememberNavController()

    TourDetailsScreen(
        touristId = "3BKN3xDmKlI4P60FW3Q9",
        tourId = "20018",
        tourDetailsViewModel = tourDetailsViewModel,
        onNavToChooseDate = { touristId ->
            navigateToTourDates(navController, touristId)
        },
        onNavToChat = { b,a ->

        },
        onBack = {}
    )



}


