package com.example.tripnila.screens

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.areNavigationBarsVisible
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import com.example.tripnila.R
import com.example.tripnila.common.AdditionalInformationRow
import com.example.tripnila.common.AppOutlinedButton
import com.example.tripnila.common.AppReviewsCard
import com.example.tripnila.common.LoadingScreen
import com.example.tripnila.common.Orange
import com.example.tripnila.common.Tag
import com.example.tripnila.common.UnderlinedText
import com.example.tripnila.data.Amenity
import com.example.tripnila.data.AmenityBrief
import com.example.tripnila.data.Attraction
import com.example.tripnila.data.ReviewUiState
import com.example.tripnila.data.Staycation
import com.example.tripnila.model.DetailViewModel
import java.text.NumberFormat
import java.time.LocalDate
import java.time.temporal.ChronoUnit


@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun StaycationDetailsScreen(
    staycationId: String,
    touristId: String,
    detailViewModel: DetailViewModel? = null,
    onNavToBooking: (String, String) -> Unit
) {

    var staycation = detailViewModel?.staycation?.collectAsState()

    val dateRangePickerState = rememberDateRangePickerState()
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    var openBottomSheet = remember { mutableStateOf(false) }
    var isSaveButtonClicked = remember { mutableStateOf(false) }
    var titleText: MutableState<String?> = remember { mutableStateOf(null) }
    var bottomBookingText: MutableState<String?> = remember { mutableStateOf(null) }
    var enableBottomBookingButton: MutableState<Boolean> = remember { mutableStateOf(false) }
    var enableBottomSaveButton: MutableState<Boolean> = remember { mutableStateOf(false) }
    var nights = remember { mutableStateOf(0) }
    var hasNavigationBar = WindowInsets.areNavigationBarsVisible

    val staycationReviews = staycation?.value?.staycationBookings?.map { it.bookingReview }
    val reviews: List<ReviewUiState> = staycationReviews?.map { review ->
        ReviewUiState(
            rating = review?.rating?.toDouble() ?: 0.0,
            comment = review?.comment ?: "",
            touristImage = review?.reviewerImage ?: "https://upload.wikimedia.org/wikipedia/commons/8/89/Portrait_Placeholder.png",
            touristName = "${review?.reviewerFirstName} ${review?.reviewerLastName}",
            reviewDate = review?.reviewDate?.toString() ?: ""
        )
    } ?: emptyList()
    val attractions = listOf(
        Attraction(
            image = R.drawable.map_image,
            name = "Rainforest Park",
            tag = "Nature",
            distance = 500,
            price = 1000.00,
            openingTime = "7:30"
        ),
        Attraction(
            image = R.drawable.map_image,
            name = "Pasig Museum",
            tag = "History",
            distance = 2700,
            price = 1000.00,
            openingTime = "9:00"
        ),
    )


    LaunchedEffect(staycationId) {
        detailViewModel?.getStaycationById(staycationId)
    }


    LaunchedEffect(
        dateRangePickerState.selectedStartDateMillis,
        dateRangePickerState.selectedEndDateMillis,
        openBottomSheet.value,
        isSaveButtonClicked.value
    ) {

        var selectedStartDateMillis = dateRangePickerState.selectedStartDateMillis
        var selectedEndDateMillis = dateRangePickerState.selectedEndDateMillis

        val countNights = if (selectedStartDateMillis != null && selectedEndDateMillis != null) {
            calculateNights(selectedStartDateMillis, selectedEndDateMillis)
        } else {
            null
        }
        
        titleText.value = countNights?.let { "$it nights" } ?: "Select date"
        nights.value = countNights?.toInt() ?: 0
        enableBottomSaveButton.value = countNights != null && countNights > 0

        if (!openBottomSheet.value && !isSaveButtonClicked.value) {
            dateRangePickerState.setSelection(startDateMillis = null, endDateMillis = null)
            bottomBookingText.value = "Check availability"
        }

        val todayMillis = System.currentTimeMillis()



        // Check if selectedStartDate is not earlier than today
        if (selectedStartDateMillis != null && selectedStartDateMillis < todayMillis) {
            dateRangePickerState.setSelection(startDateMillis = null, endDateMillis = null)
        }

        // Check if countNights is greater than 0
        if (countNights != null && countNights <= 0) {
            dateRangePickerState.setSelection(startDateMillis = null, endDateMillis = null)
        }

        if (isSaveButtonClicked.value && countNights != null) {
            bottomBookingText.value = countNights?.let { "for $it nights" } ?: "Check availability"
            enableBottomBookingButton.value = countNights != null && countNights > 0
        }

       // isSaveButtonClicked.value = false

        detailViewModel?.setNightsDifference(countNights)
        detailViewModel?.setStartDate(dateRangePickerState.selectedStartDateMillis)
        detailViewModel?.setEndDate(dateRangePickerState.selectedEndDateMillis)

    }

    if (staycation?.value == null) {
        LoadingScreen(isLoadingCompleted = false, isLightModeActive = true)
    } else {

        Surface(
            modifier = Modifier
                .fillMaxSize(),
            color = Color(0xFFEFEFEF)
        ) {
            Scaffold(
                bottomBar = {
                    bottomBookingText.value?.let { nights ->
                        StaycationBottomBookingBar(
                            staycation = staycation?.value,
                            nights = nights,
                            enableButton = enableBottomBookingButton.value,
                            onClickUnderlinedText = {
                                openBottomSheet.value = true
                            },
                            onClickChatHost = {
                                /*TODO*/
                            },
                            onClickBook = {
                                onNavToBooking(touristId, staycationId)
                            }
                        )

                    }
                }
            ) {

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it)
                ) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.staycation1),
                                contentDescription = "h5 1",
                                contentScale = ContentScale.FillWidth
                            )
                            //TopBarIcons()
                            DetailsTopAppBar()
                        }
                    }
                    item {
                        staycation?.value?.let { staycation ->
                            StaycationDescriptionCard1(
                                staycation = staycation,
                                modifier = Modifier
                                    .offset(y = (-17).dp)
                            )
                        }
                    }
                    item {
                        StaycationDescriptionCard2(
                            modifier = Modifier
                                .offset(y = (-5).dp)
                                .padding(bottom = 12.dp)
                        )
                    }
                    item {
                        staycation?.value?.let { staycation ->
                            StaycationDescriptionCard3(
                                staycation = staycation,
                                modifier = Modifier
                                    .offset(y = (-5).dp)
                                    .padding(bottom = 12.dp)
                            )
                        }
                    }
                    item {
                        AttractionsNearbyCard(
                            attractions = attractions,
                            modifier = Modifier
                                .offset(y = (-5).dp)
                                .padding(bottom = 12.dp)
                        )
                    }
                    item {
                        staycation?.value?.totalReviews?.let { totalReviews ->
                            staycation?.value?.averageReviewRating?.let { averageRating ->
                                AppReviewsCard(
                                    totalReviews = totalReviews,
                                    averageRating = averageRating,
                                    reviews = reviews,
                                    modifier = Modifier
                                        .offset(y = (-5).dp)
                                        .padding(bottom = 12.dp)
                                )
                            }
                        }
                    }
                    item {
                        staycation?.value?.let { staycation ->
                            StaycationAmenitiesCard(
                                staycation = staycation,
                                // amenities = amenities,
                                modifier = Modifier
                                    .offset(y = (-5).dp)
                                    .padding(bottom = 12.dp)
                            )
                        }
                    }
                    item {
                        StaycationAdditionalInformationCard(
                            modifier = Modifier
                                .offset(y = (-5).dp)
                                .padding(bottom = 7.dp)
                        )
                    }
                }
            }

            if (openBottomSheet.value) {
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
                                onClick = { openBottomSheet.value = false },
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
                                    dateRangePickerState.setSelection(null, null)
                                    isSaveButtonClicked.value = false
                                }

                            )


                        }
                    },
                    onDismissRequest = { openBottomSheet.value = false },
                    sheetState = bottomSheetState,
                    modifier = Modifier
                        .fillMaxHeight(0.8f) //0.693
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Spacer(modifier = Modifier
                            .weight(1f)
                          //  .height(15.dp)
                        )
                        DateRangePicker(
                            modifier = Modifier
                                .fillMaxHeight(if (hasNavigationBar) 0.8f else 1f), //0.8f

                            title = {
                                titleText.value?.let { text ->
                                    Text(
                                        text = text,
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        modifier = Modifier.padding(horizontal = 30.dp)
                                    )
                                }
                            },
                            headline = null,
                            showModeToggle = false,
                            state = dateRangePickerState,
                            colors = DatePickerDefaults.colors(
                                dayContentColor = Color.Black,
                                selectedDayContainerColor = Orange,
                                dayInSelectionRangeContainerColor = Orange.copy(.3f),
                                disabledDayContentColor = Color.Black.copy(0.3f),
                                //disabledSelectedDayContainerColor = Color.Red,
                                todayDateBorderColor = Orange,
                                todayContentColor = Color.Black
                            ),
                            dateValidator = { date ->
                                val adjustedDates = staycation?.value?.availableDates?.map {
                                    it.availableDate?.toDate()?.time?.plus(28800000) ?: 0
                                } ?: emptyList()

                                val threshold = 60000
                                adjustedDates.any { Math.abs(date - it) <= threshold }
                            },
                        )

                        Spacer(modifier = Modifier.weight(1f))
                        ConfirmCalendar(
                            nights = nights.value,
                            staycation = staycation.value,
                            enableButton = enableBottomSaveButton.value,
                            onClickSave = {
                                isSaveButtonClicked.value = true
                                openBottomSheet.value = false
                            },
                            modifier = Modifier
                                //.windowInsetsPadding(WindowInsets.navigationBars)
                                //.navigationBarsPadding()
                                .noPaddingIf(hasNavigationBar)
                        )
                        Spacer(modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars))
                    }

                }
            }
        }
    }
}

@Composable
fun Modifier.noPaddingIf(hasNavigationBar: Boolean) = composed {
    if (hasNavigationBar) {
        this
    } else {
        this.navigationBarsPadding()
    }
}

fun calculateNights(startDateMillis: Long, endDateMillis: Long): Long {
    val startDate = LocalDate.ofEpochDay(startDateMillis / (24 * 60 * 60 * 1000))
    val endDate = LocalDate.ofEpochDay(endDateMillis / (24 * 60 * 60 * 1000))

    return ChronoUnit.DAYS.between(startDate, endDate)
}
@Composable
fun TopBarIcons(
    forStaycationManager: Boolean = false,
    modifier: Modifier = Modifier
) {

    val iconColor = Color.White  //Color(0xFFC0C0C0)
    var isFavorite by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height = 50.dp)
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 15.dp, top = 15.dp, end = 15.dp)
                .background(Color.Transparent)
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = "Back",
                tint = iconColor
                //modifier = Modifier.padding(start = 8.dp, top = 15.dp)
            )
            if (!forStaycationManager){
                Spacer(
                    modifier = Modifier
                        .width(265.dp)
                )
                Icon(
                    imageVector = Icons.Outlined.Share,
                    contentDescription = "Share",
                    tint = iconColor
                    //modifier = Modifier.padding(start = 8.dp, top = 15.dp)
                )
                Spacer(
                    modifier = Modifier
                        .width(15.dp)
                )

                IconToggleButton(
                    modifier = Modifier.offset(y = (-7).dp),
                    checked = isFavorite,
                    onCheckedChange = {
                        isFavorite = !isFavorite
                    }
                ) {
                    Icon(
                        imageVector = if(isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "",
                        tint = if(isFavorite) Color.Red else iconColor

                    )
                }
            }
        }
    }
}

@Composable
fun StaycationDescriptionCard1(
    staycation: Staycation? = null,
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
                    text = staycation?.staycationTitle ?: "",
                    color = Color.Black,
                    //  fontSize = 20.dp,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
//                    modifier = Modifier.align(Alignment.Start)
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
                    text = staycation?.averageReviewRating.toString(),
                    //fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.width(15.dp))
                UnderlinedText(
                    textLabel = staycation?.totalReviews.toString() + " reviews",
                    color = Color.Black,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    onClick = {
                        /*TODO*/
                        // Navigate to reviews screen
                    }
                )
                Spacer(modifier = Modifier.width(5.dp))
//                Tag(tag = "Nature")
//                Tag(tag = "Movies")
//                Tag(tag = "History")
//                Tag(tag = "+5")
                val maxTagsToShow = 3
                staycation?.staycationTags?.take(maxTagsToShow)?.forEachIndexed { index, tag ->
                    Tag(tag = tag.tagName)
//                    if (index < 2) {
//                        Spacer(modifier = Modifier.width(5.dp))
//                    }
                }

                if (staycation?.staycationTags?.size ?: 0 > maxTagsToShow) {
                    Tag(tag = "+${staycation?.staycationTags?.size?.minus(maxTagsToShow)}")
                }
            }
            Text(
                text = staycation?.staycationLocation ?: "",
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
fun StaycationDescriptionCard2(
    staycation: Staycation? = null,
    modifier: Modifier = Modifier
){
    Box(
        modifier = modifier
            .fillMaxWidth()
            //.height(height = 81.dp)
            .clip(shape = RoundedCornerShape(20.dp))
            .background(color = Color.White)
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                //                .padding(horizontal = 25.dp, vertical = 12.dp),
                .padding(
                    horizontal = 25.dp,
                    vertical = 20.dp // 12
                ),
            verticalAlignment = Alignment.CenterVertically,
            //horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "This staycation is eco-friendly. Amenities used and attractions are eco-friendly. ", /*TODO*/
                //fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    //.width(280.dp)
                    .weight(1f)
            )
            //Spacer(modifier = Modifier.width(14.dp))
            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.leaf),
                contentDescription = ""
            )
        }
    }
}

@Composable
fun StaycationDescriptionCard3(
    staycation: Staycation? = null,
    withEditButton: Boolean = false,
    modifier: Modifier = Modifier
) {

//    val imageLoader = rememberImageP(
//        data = if (host.hasImage) host.imageUrl else "placeholder_url",
//        builder = {
//            crossfade(true)
//            placeholder(R.drawable.placeholder)
//        }
//    )

    val amenities = listOf(
        AmenityBrief(
            image = R.drawable.person,
            count = staycation?.noOfGuests,
            name = "person"
        ),
        AmenityBrief(
            image = R.drawable.bedroom,
            count = staycation?.noOfBedrooms,
            name = "bedroom"
        ),
        AmenityBrief(
            image = R.drawable.bedroom,
            count = staycation?.noOfBeds,
            name = "bed"
        ),
        AmenityBrief(
            image = R.drawable.bathroom,
            count = staycation?.noOfBathrooms,
            name = "bathroom"
        )
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(20.dp))
            .background(color = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 25.dp,
                    vertical = 20.dp
                ),
        ) {
            Box {
                val hostImage = null
                val placeholderImage = "https://upload.wikimedia.org/wikipedia/commons/8/89/Portrait_Placeholder.png"

                val imageLoader = rememberImagePainter(
                    data = hostImage ?: placeholderImage,
                    builder = {
                        crossfade(true)
                    }
                )
                Image(
                    painter = imageLoader,
                    contentDescription = "Host",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(50.dp)
                        .clip(shape = RoundedCornerShape(50.dp))
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        text = "Hosted by ${staycation?.hostFirstName}",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                            .padding(start = 8.dp, top = 6.dp, bottom = 6.dp)
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
                Column(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    val amenities = amenities
                    val numRows = (amenities?.size?.plus(1))?.div(2)

                    for (row in 0 until numRows!!) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            val startIndex = row * 2
                            val endIndex = minOf(startIndex + 2, amenities.size)

                            for (i in startIndex until endIndex) {
                                StaycationAmenityDetail(amenity = amenities[i])
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun AttractionsNearbyCard(attractions: List<Attraction>, modifier: Modifier = Modifier){
    /*TODO*/
    // NOT FUNCTIONING

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
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Attractions nearby",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .align(Alignment.Start)
            )
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                attractions.forEach { attraction ->
                    AttractionRow(attraction)
                }
            }
            AppOutlinedButton(
                buttonText = "See itinerary",
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
fun StaycationAmenitiesCard(
    staycation: Staycation? = null,
    withEditButton: Boolean = false,
    modifier: Modifier = Modifier
) {
    val amenities = staycation?.amenities?.take(6) ?: emptyList()

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
                    vertical = 20.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = "Amenities and offers",
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
                            StaycationAmenityDetailWithoutCount(amenity = amenities[i])
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
            AppOutlinedButton(
                buttonText = "See all amenities",
                modifier = Modifier
                    .padding(top = 12.dp)
            )
        }
    }
}



@Composable
fun StaycationAdditionalInformationCard(withEditButton: Boolean = false, modifier: Modifier = Modifier){

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
            AdditionalInformationRow(textInfo = "House Rules", onClick = {/*TODO*/})
            AdditionalInformationRow(textInfo = "Health & safety", onClick = {/*TODO*/})
            AdditionalInformationRow(textInfo = "Cancellation & reschedule policy", onClick = {/*TODO*/})
            AdditionalInformationRow(textInfo = "Business Information", onClick = {/*TODO*/})

        }
    }
}

@Composable
fun StaycationBottomBookingBar(
    staycation: Staycation? = null,
    bookButtonText: String = "Book",
    nights: String = "",
    enableButton: Boolean = true,
    onClickChatHost: () -> Unit ,
    onClickBook: () -> Unit ,
    onClickUnderlinedText: () -> Unit,
    modifier: Modifier = Modifier
){
    val formattedNumber = NumberFormat.getNumberInstance().format(staycation?.staycationPrice)

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
                            append(" / night")
                        }
                    )
                    ClickableText(
                        text =  buildAnnotatedString {
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Medium,
                                color = Color(0xff999999), //
                                textDecoration = TextDecoration.Underline)) {
                                append(nights)
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
                        /*TODO*/
                        onClickChatHost()
                    },
                    modifier = Modifier.padding(horizontal = 15.dp)
                )
                BookingFilledButton(
                    buttonText = bookButtonText,
                    onClick = {
                        onClickBook()
                    },
                    enabled = enableButton,
                    modifier = Modifier.width(90.dp)
                )
            }

        }

    }
}

@Composable
fun BookingOutlinedButton(
    buttonText: String,
    containerColor: Color = Color.White,
    buttonShape: RoundedCornerShape = RoundedCornerShape(10.dp),
    borderStroke: BorderStroke = BorderStroke(1.dp, Orange),
    contentPadding: PaddingValues = PaddingValues(10.dp),
    contentFontSize: TextUnit = 16.sp,
    contentFontWeight: FontWeight = FontWeight.Medium,
    contentColor: Color = Orange,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
){
    OutlinedButton(
        onClick = onClick,
        border = borderStroke,
        shape = buttonShape,
        colors = ButtonDefaults.buttonColors(containerColor),
        contentPadding = contentPadding,
        modifier = modifier
    ) {
        Text(
            text = buttonText,
            fontSize = contentFontSize,
            fontWeight = contentFontWeight,
            color = contentColor
        )
    }
}

@Composable
fun BookingFilledButton(
    buttonText: String,
    buttonShape: RoundedCornerShape = RoundedCornerShape(10.dp),
    onClick: () -> Unit,
    containerColor: Color = Orange,
    contentPadding: PaddingValues = PaddingValues(10.dp),
    contentFontSize: TextUnit = 16.sp,
    contentFontWeight: FontWeight = FontWeight.Medium,
    contentColor: Color = Color.White,
    isLoading: Boolean = false,
    strokeWidth: Dp = 3.dp,
    enabled: Boolean = true,
    circularProgressIndicatorSize: Dp = 20.dp,
    modifier: Modifier = Modifier
){

    Button(
        onClick = onClick,
        shape = buttonShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            disabledContainerColor = containerColor.copy(0.3f)
        ),
        contentPadding = contentPadding,
        enabled = enabled,
        modifier = modifier
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                color = Color.White,
                strokeWidth = strokeWidth,
                modifier = Modifier.size(circularProgressIndicatorSize)
            )
        } else {
            Text(
                text = buttonText,
                fontSize = contentFontSize,
                fontWeight = contentFontWeight,
                color = contentColor
            )
        }

    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsTopAppBar(modifier: Modifier = Modifier){
    var isFavorite by remember { mutableStateOf(false) }
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(state = rememberTopAppBarState())

    TopAppBar(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        scrollBehavior = scrollBehavior,
        title = { /*TODO*/ },
        navigationIcon = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Back",
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
            scrolledContainerColor = Color.Transparent,
            navigationIconContentColor = Color.White,
            actionIconContentColor = Color.White
        ),
        actions = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Outlined.Share,
                    contentDescription = "Share",
                )
            }
            Spacer(
                modifier = Modifier
                    .width(15.dp)
            )

            IconToggleButton(
                checked = isFavorite,
                onCheckedChange = {
                    isFavorite = !isFavorite
                }
            ) {
                Icon(
                    imageVector = if(isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = "",
                    tint = if(isFavorite) Color.Red else Color.White

                )
            }
        }

    )
}

@Composable
fun AttractionRow(attraction: Attraction, modifier: Modifier = Modifier){

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
    ){
        Column {
            Row {
                Text(
                    text = attraction.name,
                    fontWeight = FontWeight.Medium,
                )
                Tag(
                    tag = attraction.tag,
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                )
            }
            Row {
                Text(
                    text = attraction.distance.toString() +
                            " meters • ₱ " + String.format("%.2f", attraction.price) +
                            " • Opens at " + attraction.openingTime,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF999999)
                )
            }
            Divider(modifier = Modifier.padding(top = 20.dp))
        }
        Image(
            painter = painterResource(id = attraction.image),
            contentDescription = "Map",
            modifier = Modifier
                .size(55.dp)
                .align(alignment = Alignment.TopEnd)
        )

    }
}

@Composable
fun StaycationAmenityDetail(amenity: AmenityBrief, modifier: Modifier = Modifier){
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
                text = amenity.count.toString() + " " + amenity.name,
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

@Composable
fun StaycationAmenityDetailWithoutCount(amenity: Amenity, modifier: Modifier = Modifier){

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
                imageVector = if (amenity.amenityIcon == 0) ImageVector.vectorResource(id = R.drawable.wifi) else ImageVector.vectorResource(id = amenity.amenityIcon),
                contentDescription = amenity.amenityName
            )
            Text(
                text = " ${amenity.amenityName}",
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

@Composable
fun ConfirmCalendar(
    staycation: Staycation?,
    nights: Int,
    modifier: Modifier = Modifier,
    onClickSave: () -> Unit,
    enableButton: Boolean = false,
) {
    val formattedTotalAmount = NumberFormat.getNumberInstance().format(staycation?.staycationPrice?.times(nights))
    val formattedNumber = NumberFormat.getNumberInstance().format(staycation?.staycationPrice)

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
                    var annotatedText = if (nights == 0 || nights == null) {
                        buildAnnotatedString {
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                //append("₱ ${"%.2f".format(staycation?.staycationPrice)}")
                                append("₱ $formattedNumber")

                            }
                            append(" / night")
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
                                append("$nights")

                            }
                            append(" nights")
                        }
                    }


                    Text(
                        text = annotatedText
                    )
//                    ClickableText(
//                        text =  buildAnnotatedString {
//                            withStyle(style = SpanStyle(fontWeight = FontWeight.Medium,
//                                color = Color(0xff999999), //
//                                textDecoration = TextDecoration.Underline)) {
//                                append(nights)
//                            }
//                        },
//                        onClick = {
//                            onClickUnderlinedText()
//                        }
//
//                    )



                }
                BookingFilledButton(
                    buttonText = "Save",
                    onClick = {
                        onClickSave()
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
private fun StaycationDetailsPreview() {

    val detailViewModel = viewModel(modelClass = DetailViewModel::class.java)
    val onNavToBooking: (String, String) -> Unit = { staycationId, touristId ->
        // Your implementation here
    }

    StaycationDetailsScreen(
        detailViewModel = detailViewModel,
        staycationId = "LxpNxRFdwkQzBxujF3gx",
        touristId = "5JCZ1j5hODQ7BcURS2GI",
        onNavToBooking = onNavToBooking


    )








}

