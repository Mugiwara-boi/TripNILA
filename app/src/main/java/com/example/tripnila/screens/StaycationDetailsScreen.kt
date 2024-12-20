package com.example.tripnila.screens

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
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
import com.example.tripnila.data.AttractionUiState
import com.example.tripnila.data.ReviewUiState
import com.example.tripnila.data.Staycation
import com.example.tripnila.data.setImageForAmenity
import com.example.tripnila.model.DetailViewModel
import kotlinx.coroutines.launch
import com.itenirary.IteniraryActivity
import java.text.NumberFormat
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit


@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class,
    ExperimentalFoundationApi::class
)
@Composable
fun StaycationDetailsScreen(
    staycationId: String,
    touristId: String,
    detailViewModel: DetailViewModel,
    onNavToBooking: (String, String) -> Unit,
    onBack: () -> Unit,
    onNavToChat: (String, String) -> Unit,
    onNavToReviewScreen: (String, String, String) -> Unit
) {

    val staycation = detailViewModel.staycation.collectAsState()
    val isUserVerified = detailViewModel.isUserVerified.collectAsState()
    
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val dateRangePickerState = rememberDateRangePickerState()
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val openBottomSheet = remember { mutableStateOf(false) }

    val isSaveButtonClicked = remember { mutableStateOf(false) }
    val titleText: MutableState<String?> = remember { mutableStateOf(null) }
    val bottomBookingText: MutableState<String?> = remember { mutableStateOf(null) }
    val enableBottomBookingButton: MutableState<Boolean> = remember { mutableStateOf(false) }
    val enableBottomSaveButton: MutableState<Boolean> = remember { mutableStateOf(false) }
    val nights = remember { mutableStateOf(0) }
    val hasNavigationBar = WindowInsets.areNavigationBarsVisible
    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f
    ) {
        staycation.value?.staycationImages?.size ?: 0
    }


    val staycationReviews = staycation.value?.staycationBookings?.mapNotNull { it.bookingReview }
    val reviews: List<ReviewUiState> = staycationReviews?.filter { it.bookingId != "" }
        ?.map { review ->
            review.let {
                ReviewUiState(
                    rating = it.rating.toDouble(),
                    comment = it.comment,
                    touristImage = it.reviewer.profilePicture,
                    touristName = "${it.reviewer.firstName} ${it.reviewer.lastName}",
                    reviewDate = it.reviewDate.toString()
                )
            }
        } ?: emptyList()

    val attractionUiStates = listOf(
        AttractionUiState(
            image = R.drawable.map_image,
            name = "Rainforest Park",
            tag = listOf("Nature"),
            distance = 500,
            price = 1000.00,
            openingTime = "7:30"
        ),
        AttractionUiState(
            image = R.drawable.map_image,
            name = "Pasig Museum",
            tag = listOf("History"),
            distance = 2700,
            price = 1000.00,
            openingTime = "9:00"
        ),
    )

    var isFavorite by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(touristId) {
        isFavorite = detailViewModel.isFavorite(staycationId, touristId)
        detailViewModel.incrementViewCount(staycationId)
        detailViewModel.verifyUser(touristId)
    }



    LaunchedEffect(staycationId) {
        detailViewModel.getStaycationById(staycationId)
    }


    LaunchedEffect(
        dateRangePickerState.selectedStartDateMillis,
        dateRangePickerState.selectedEndDateMillis,
        openBottomSheet.value,
        isSaveButtonClicked.value
    ) {

        val selectedStartDateMillis = dateRangePickerState.selectedStartDateMillis

        val selectedEndDateMillis = dateRangePickerState.selectedEndDateMillis

        Log.d("Before (Details)", "$selectedStartDateMillis")
        Log.d("Before (Details)", "$selectedEndDateMillis")


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
            bottomBookingText.value = countNights.let { "for $it nights" }
            enableBottomBookingButton.value = true && countNights > 0
        }

       // isSaveButtonClicked.value = false
//
//        Log.d("AFTER", "$selectedStartDateMillis")
//        Log.d("BEFORE", "$selectedEndDateMillis")
//
//        val calendar = Calendar.getInstance()
//        calendar.time = selectedStartDateMillis?.let { Date(it) } ?: Date(0)
//        calendar.add(Calendar.HOUR_OF_DAY, -8)
//        val checkInDatePlus2Hours = calendar.time
//
//        calendar.time = selectedEndDateMillis?.let { Date(it) } ?: Date(0)
//        calendar.add(Calendar.HOUR_OF_DAY, -8)
//        val checkOutDatePlus4Hours = calendar.time
//
//        Log.d("AFTER(DATE)", checkInDatePlus2Hours.toString())
//        Log.d("BEFORE(DATE)", checkOutDatePlus4Hours.toString())

        detailViewModel.setNightsDifference(countNights)
        detailViewModel.setStartDate(dateRangePickerState.selectedStartDateMillis)
        detailViewModel.setEndDate(dateRangePickerState.selectedEndDateMillis)

    }

    // staycation?.value == null ||
    if (staycation.value?.staycationId != staycationId) {
        LoadingScreen(isLoadingCompleted = false, isLightModeActive = true)
    } else {

        Surface(
            modifier = Modifier
                .fillMaxSize(),
            color = Color(0xFFEFEFEF)
        ) {
            Scaffold(
                bottomBar = {
                    if (isUserVerified.value == true) {
                        bottomBookingText.value?.let { nights ->
                            StaycationBottomBookingBar(
                                staycation = staycation.value,
                                nights = nights,
                                enableButton = enableBottomBookingButton.value,
                                onClickUnderlinedText = {
                                    openBottomSheet.value = true
                                },
                                onClickChatHost = {
                                    Log.d("Other User", staycation.value!!.host.touristId)
                                    onNavToChat(touristId, staycation.value!!.host.touristId)
                                },
                                onClickBook = {
                                    onNavToBooking(touristId, staycationId)
                                }
                            )

                        }
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
                            val image = staycation.value!!.staycationImages.sortedBy { it.photoType }.getOrNull(page)
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
                                } else {
                                    AsyncImage(
                                        model = "https://upload.wikimedia.org/wikipedia/commons/thumb/3/3f/Placeholder_view_vector.svg/1022px-Placeholder_view_vector.svg.png",
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
                                            detailViewModel.toggleFavorite(
                                                staycationId,
                                                touristId,
                                                "Staycation"
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
                                            text = "${page + 1}/${staycation.value!!.staycationImages.size}",
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
                        staycation.value?.let { staycation ->
                            StaycationDescriptionCard1(
                                staycation = staycation,
                                modifier = Modifier
                                    .offset(y = (-17).dp)
                            )
                        }
                    }

                    if (staycation.value?.isEcoFriendly == true) {
                        item {
                            StaycationDescriptionCard2(
                                modifier = Modifier
                                    .offset(y = (-5).dp)
                                    .padding(bottom = 12.dp)
                            )
                        }
                    }


                    item {
                        staycation.value?.let { staycation ->
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
                            attractionUiStates = attractionUiStates,
                            onSeeItineraryButtonClick = {

                                // Staycation Detail -> Itinerary
                                // Sa staycationId na variable nakastore yung Id ng staycation
                                /*Toast.makeText(
                                    context,
                                    "StaycationId: $staycationId",
                                    Toast.LENGTH_SHORT
                                ).show()*/

                                val intent = Intent(context, IteniraryActivity::class.java)
                                intent.putExtra("cameFromProfile", false)
                                intent.putExtra("touristId", touristId)
                                intent.putExtra("staycationId", staycationId)
                                context.startActivity(intent)
                            },
                            modifier = Modifier
                                .offset(y = (-5).dp)
                                .padding(bottom = 12.dp)
                        )
                    }
                    item {
                        staycation.value?.totalReviews?.let { totalReviews ->
                            staycation.value?.averageReviewRating?.let { averageRating ->
                                AppReviewsCard(
                                    totalReviews = totalReviews,
                                    averageRating = averageRating,
                                    reviews = reviews,
                                    onSeeAllReviews = {
                                        onNavToReviewScreen(touristId, staycationId, "Staycation")
                                    },
                                    modifier = Modifier
                                        .offset(y = (-5).dp)
                                        .padding(bottom = 12.dp)
                                )
                            }
                        }
                    }
                    item {
                        staycation.value?.let { staycation ->
                            StaycationAmenitiesCard(
                                staycation = staycation,
                                modifier = Modifier
                                    .offset(y = (-5).dp)
                                    .padding(bottom = 12.dp)
                            )
                        }
                    }
                    item {
                        StaycationAdditionalInformationCard(
                            detailViewModel = detailViewModel,
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

                                val selectedDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(date), ZoneId.systemDefault()).toLocalDate()
                                val today = LocalDateTime.now().toLocalDate()

                                val adjustedDates = staycation.value?.availableDates?.map {
                                    it.availableDate?.toDate()?.time?.plus(28800000) ?: 0
                                } ?: emptyList()

                                adjustedDates.contains(date) && selectedDate.isAfter(today)

//                                val adjustedDates = staycation.value?.availableDates?.map {
//                                    it.availableDate?.toDate()?.time?.plus(28800000) ?: 0
//                                } ?: emptyList()
//
//                                val threshold = 86400000
//                                adjustedDates.any { Math.abs(date - it) <= threshold }
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
    modifier: Modifier = Modifier,
    forStaycationManager: Boolean = false,
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
    modifier: Modifier = Modifier,
    staycation: Staycation? = null,
    withEditButton: Boolean = false,
    onEdit: (() -> Unit)? = null,
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
                        onClick = {
                            if (onEdit != null) {
                                onEdit()
                            }
                        },
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

                if ((staycation?.staycationTags?.size ?: 0) > maxTagsToShow) {
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
    modifier: Modifier = Modifier,
    staycation: Staycation? = null,
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
    modifier: Modifier = Modifier,
    staycation: Staycation? = null,
    withEditButton: Boolean = false,
) {


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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 25.dp,
                    vertical = 20.dp
                ),
        ) {
            Box {
//                val hostImage = null
//                val placeholderImage = "https://upload.wikimedia.org/wikipedia/commons/8/89/Portrait_Placeholder.png"
//
//                val imageLoader = rememberImagePainter(
//                    data = hostImage ?: placeholderImage,
//                    builder = {
//                        crossfade(true)
//                    }
//                )
//                Image(
//                    painter = imageLoader,
//                    contentDescription = "Host",
//                    contentScale = ContentScale.Crop,
//                    modifier = Modifier
//                        .size(50.dp)
//                        .clip(shape = RoundedCornerShape(50.dp))
//                )
                AsyncImage(
                    model = staycation?.host?.profilePicture,
                    contentDescription = "",
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
//                        text = "Hosted by ${staycation?.hostFirstName}",
                        text = "Hosted by ${staycation?.host?.firstName}",
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

                    val numRows = (amenities.size.plus(1)).div(2)

                    for (row in 0 until numRows) {
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
fun AttractionsNearbyCard(modifier: Modifier = Modifier, attractionUiStates: List<AttractionUiState>, onSeeItineraryButtonClick: () -> Unit){
    /*TODO*/
    // NOT FUNCTIONING

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
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Itinerary Planner",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .align(Alignment.Start)
            )
            Spacer(modifier = Modifier.height(10.dp))
            AppOutlinedButton(
                buttonText = "See itinerary",
                onClick = {
                    onSeeItineraryButtonClick()
                },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StaycationAmenitiesCard(
    modifier: Modifier = Modifier,
    staycation: Staycation? = null,
    withEditButton: Boolean = false,
) {

    val amenities = staycation?.amenities?.take(6) ?: emptyList()

    val allAmenities = staycation?.amenities

    val seeAllAmenities = remember { mutableStateOf(false) }
    val amenitiesSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)


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
                    vertical = 20.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Amenities and offers",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .padding(bottom = 4.dp)
                    .align(Alignment.Start)
            )
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                userScrollEnabled = false,
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalArrangement = Arrangement.spacedBy(5.dp),
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .fillMaxWidth()
                    .height(100.dp)
                   // .weight(1f)
            ) {
                items(amenities) {  amenity ->
                    StaycationAmenityDetailWithoutCount(amenity = amenity)
                }
            }
            AppOutlinedButton(
                buttonText = "See all amenities",
                onClick = {
                    seeAllAmenities.value = true
                },
//                modifier = Modifier
//                    .padding(top = 12.dp)
            )
        }
    }


    //            Column(
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                val numRows = (amenities.size + 1) / 2
//
//                for (row in 0 until numRows) {
//                    Row(
//                        modifier = Modifier.fillMaxWidth(),
//                        horizontalArrangement = Arrangement.Start,
//                    ) {
//                        val startIndex = row * 2
//                        val endIndex = minOf(startIndex + 2, amenities.size)
//
//                        for (i in startIndex until endIndex) {
//                            StaycationAmenityDetailWithoutCount(amenity = amenities[i])
//                            Spacer(modifier = Modifier.weight(1f))
//                        }
//                    }
//                }
//            }


    if (seeAllAmenities.value) {
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
                        onClick = { seeAllAmenities.value = false },
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Close"
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))

                }
            },
            onDismissRequest = { seeAllAmenities.value = false },
            sheetState = amenitiesSheetState,
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
                    text = "Amenities and offers",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 16.dp)
                        .fillMaxWidth()
                )

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalArrangement = Arrangement.spacedBy(5.dp),
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    items(allAmenities!!) {  amenity ->
                        StaycationAmenityDetailWithoutCount(
                            amenity = amenity,
                            modifier = Modifier
                                .padding(vertical = 3.dp)
                        )
                    }
                }
            }

        }
    }


}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StaycationAdditionalInformationCard(withEditButton: Boolean = false, modifier: Modifier = Modifier, detailViewModel: DetailViewModel){

    val phoneNo by remember{ mutableStateOf(detailViewModel.staycation.value?.phoneNo) }
    val email by remember{ mutableStateOf(detailViewModel.staycation.value?.email) }
    val noReschedule by remember{ mutableStateOf(detailViewModel.staycation.value?.noReschedule) }
    val noCancel by remember{ mutableStateOf(detailViewModel.staycation.value?.noCancel) }
    val hasSecurityCamera by remember{ mutableStateOf(detailViewModel.staycation.value?.hasSecurityCamera) }
    val hasFirstAid by remember{ mutableStateOf(detailViewModel.staycation.value?.hasFirstAid) }
    val hasFireExit by remember{ mutableStateOf(detailViewModel.staycation.value?.hasFireExit) }
    val hasFireExtinguisher by remember{ mutableStateOf(detailViewModel.staycation.value?.hasFireExtinguisher) }
    val maxGuest by remember{ mutableStateOf(detailViewModel.staycation.value?.maxNoOfGuests) }
    val noisePolicy by remember{ mutableStateOf(detailViewModel.staycation.value?.noisePolicy) }
    val guestCount by remember{ mutableStateOf(detailViewModel.staycation.value?.noOfGuests) }
    val allowPets by remember{ mutableStateOf(detailViewModel.staycation.value?.allowPets) }
    val allowSmoking by remember{ mutableStateOf(detailViewModel.staycation.value?.allowSmoking) }
    val additionalInfo by remember{ mutableStateOf(detailViewModel.staycation.value?.additionalInfo) }
    val staycationDetails by remember{ mutableStateOf(detailViewModel.staycation.value?.staycationDescription) }

    val openHouseRulesModal = remember { mutableStateOf(false)}
    val openHealthAndSafety = remember { mutableStateOf(false)}
    val openCancelAndReschedule = remember { mutableStateOf(false)}
    val openBusinessInformation = remember { mutableStateOf(false)}
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

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
            AdditionalInformationRow(textInfo = "House Rules", onClick = {openHouseRulesModal.value = true})
            AdditionalInformationRow(textInfo = "Health & safety", onClick = {openHealthAndSafety.value = true})
            AdditionalInformationRow(textInfo = "Cancellation & reschedule policy", onClick = {openCancelAndReschedule.value = true})
            AdditionalInformationRow(textInfo = "Business Information", onClick = {openBusinessInformation.value = true})

        }
    }

    if(openHouseRulesModal.value){
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
                        onClick = { openHouseRulesModal.value = false },
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Close"
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))

                }
            },
            onDismissRequest = { openHouseRulesModal.value = false },
            sheetState = bottomSheetState,
            modifier = Modifier
                .fillMaxHeight(0.8f) //0.693
        ) {

                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    item {
                        Text(
                            text = "House Rules",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 16.dp)
                                .fillMaxWidth()
                        )
                    }
                    item {

                        Text(
                            text = "Check-in/Check-out",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .padding(start = 16.dp, end = 16.dp)
                                .fillMaxWidth()
                        )

                        Text(
                            text = "Check-in time is at 2pm, and check-out time is at 12 PM.",
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier
                                .padding(start = 16.dp, end = 16.dp)
                                .fillMaxWidth()
                        )
                        Divider(
                            color = Color(0xFFDEDEDE),
                            modifier = Modifier.padding(vertical = 10.dp, horizontal = 10.dp)
                        )
                    }
                    item {

                        if (noisePolicy!!) {
                            Text(
                                text = "Noise Policy",
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .padding(start = 16.dp, end = 16.dp)
                                    .fillMaxWidth()
                            )

                            Text(
                                text = "Respect quiet hours from 10 PM to 7 AM. Keep noise levels to a minimum during this period to ensure a peaceful environment for all guests.",
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier
                                    .padding(start = 16.dp, end = 16.dp)
                                    .fillMaxWidth()
                            )
                            Divider(
                                color = Color(0xFFDEDEDE),
                                modifier = Modifier.padding(vertical = 10.dp, horizontal = 10.dp)
                            )

                        }
                    }
                    item {

                        if (allowSmoking!!) {
                            Text(
                                text = "Smoking Policy",
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .padding(start = 16.dp, end = 16.dp)
                                    .fillMaxWidth()
                            )

                            Text(
                                text = "Smoking is allowed inside the accommodations. Please make sure to dispose cigarette butts properly.",
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier
                                    .padding(start = 16.dp, end = 16.dp)
                                    .fillMaxWidth()
                            )
                            Divider(
                                color = Color(0xFFDEDEDE),
                                modifier = Modifier.padding(vertical = 10.dp, horizontal = 10.dp)
                            )

                        } else {
                            Text(
                                text = "No Smoking",
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .padding(start = 16.dp, end = 16.dp)
                                    .fillMaxWidth()
                            )

                            Text(
                                text = "Smoking is strictly prohibited inside the accommodations. Please use designated outdoor smoking areas and dispose of cigarette butts responsibly.",
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier
                                    .padding(start = 16.dp, end = 16.dp)
                                    .fillMaxWidth()
                            )
                            Divider(
                                color = Color(0xFFDEDEDE),
                                modifier = Modifier.padding(vertical = 10.dp, horizontal = 10.dp)
                            )
                        }
                    }
                    item {

                        if (allowPets!!) {
                            Text(
                                text = "Pet Policy",
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .padding(start = 16.dp, end = 16.dp)
                                    .fillMaxWidth()
                            )

                            Text(
                                text = "Pets are allowed. Please make sure to clean after your pets. Any damage caused by your pet on the property will be subject to repair fees",
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier
                                    .padding(start = 16.dp, end = 16.dp)
                                    .fillMaxWidth()
                            )
                            Divider(
                                color = Color(0xFFDEDEDE),
                                modifier = Modifier.padding(vertical = 10.dp, horizontal = 10.dp)
                            )

                        } else {
                            Text(
                                text = "Pet Policy",
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .padding(start = 16.dp, end = 16.dp)
                                    .fillMaxWidth()
                            )

                            Text(
                                text = "Pets are not allowed.",
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier
                                    .padding(start = 16.dp, end = 16.dp)
                                    .fillMaxWidth()
                            )
                            Divider(
                                color = Color(0xFFDEDEDE),
                                modifier = Modifier.padding(vertical = 10.dp, horizontal = 10.dp)
                            )
                        }
                    }
                    item{
                        if(additionalInfo!=""){
                            Text(
                                text = "Eco-friendly Policies",
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .padding(start = 16.dp, end = 16.dp)
                                    .fillMaxWidth()
                            )

                            Text(
                                text = additionalInfo!!,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier
                                    .padding(start = 16.dp, end = 16.dp)
                                    .fillMaxWidth()
                            )
                            Divider(
                                color = Color(0xFFDEDEDE),
                                modifier = Modifier.padding(vertical = 10.dp, horizontal = 10.dp)
                            )
                        }
                    }
                    item {

                        if (maxGuest!! > guestCount!!) {
                            Text(
                                text = "Guest Limit",
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .padding(start = 16.dp, end = 16.dp)
                                    .fillMaxWidth()
                            )

                            Text(
                                text = "The maximum number of guests allowed per accommodation is $guestCount up to $maxGuest, subject for additional fees. Please adhere to this limit for safety and comfort reasons.",
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier
                                    .padding(start = 16.dp, end = 16.dp)
                                    .fillMaxWidth()
                            )
                            Divider(
                                color = Color(0xFFDEDEDE),
                                modifier = Modifier.padding(vertical = 10.dp, horizontal = 10.dp)
                            )

                        } else {
                            Text(
                                text = "Guest Limit",
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .padding(start = 16.dp, end = 16.dp)
                                    .fillMaxWidth()
                            )

                            Text(
                                text = "The maximum number of guests allowed per accommodation is $guestCount. Please adhere to this limit for safety and comfort reasons.",
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier
                                    .padding(start = 16.dp, end = 16.dp)
                                    .fillMaxWidth()
                            )
                            Divider(
                                color = Color(0xFFDEDEDE),
                                modifier = Modifier.padding(vertical = 10.dp, horizontal = 10.dp)
                            )
                        }
                    }
                    item {

                        Text(
                            text = "Respect for Property",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .padding(start = 16.dp, end = 16.dp)
                                .fillMaxWidth()
                        )

                        Text(
                            text = "Treat the accommodations and amenities with care and respect. Any damages caused by guests will be subject to repair charges.",
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier
                                .padding(start = 16.dp, end = 16.dp)
                                .fillMaxWidth()
                        )
                        Divider(
                            color = Color(0xFFDEDEDE),
                            modifier = Modifier.padding(vertical = 10.dp, horizontal = 10.dp)
                        )
                    }
                    item {

                        Text(
                            text = "Security Measures",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .padding(start = 16.dp, end = 16.dp)
                                .fillMaxWidth()
                        )

                        Text(
                            text = "Ensure that doors and windows are securely locked when leaving the accommodation unattended. Report any suspicious activity to management immediately.",
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier
                                .padding(start = 16.dp, end = 16.dp)
                                .fillMaxWidth()
                        )
                        Divider(
                            color = Color(0xFFDEDEDE),
                            modifier = Modifier.padding(vertical = 10.dp, horizontal = 10.dp)
                        )
                    }
                    item {

                        Text(
                            text = "Community Guidelines",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .padding(start = 16.dp, end = 16.dp)
                                .fillMaxWidth()
                        )

                        Text(
                            text = "Be courteous and considerate towards other guests and residents. Refrain from disruptive behavior that may disturb the peace and enjoyment of others.",
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier
                                .padding(start = 16.dp, end = 16.dp)
                                .fillMaxWidth()
                        )
                        Divider(
                            color = Color(0xFFDEDEDE),
                            modifier = Modifier.padding(vertical = 10.dp, horizontal = 10.dp)
                        )
                    }
                    item {

                        Text(
                            text = "Compliance with Local Laws",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .padding(start = 16.dp, end = 16.dp)
                                .fillMaxWidth()
                        )

                        Text(
                            text = "Guests are expected to comply with all local laws and regulations during their stay.",
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier
                                .padding(start = 16.dp, end = 16.dp)
                                .fillMaxWidth()
                        )
                        Divider(
                            color = Color(0xFFDEDEDE),
                            modifier = Modifier.padding(vertical = 10.dp, horizontal = 10.dp)
                        )

                    }

                    item {
                        if(additionalInfo != "") {

                            Text(
                                text = "Additional Information",
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .padding(start = 16.dp, end = 16.dp)
                                    .fillMaxWidth()
                            )

                            Text(
                                text = additionalInfo!!,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier
                                    .padding(start = 16.dp, end = 16.dp)
                                    .fillMaxWidth()
                            )
                            Divider(
                                color = Color(0xFFDEDEDE),
                                modifier = Modifier.padding(vertical = 10.dp, horizontal = 10.dp)
                            )

                        }
                        Spacer(modifier = Modifier.weight(1f))

                        Spacer(modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars))
                    }


                }
            }


        }


    if(openHealthAndSafety.value){
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
                        onClick = { openHealthAndSafety.value = false },
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Close"
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                }
            },
            onDismissRequest = { openHealthAndSafety.value = false },
            sheetState = bottomSheetState,
            modifier = Modifier
                .fillMaxHeight(0.8f) //0.693
        ) {

            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                item {
                    Text(
                        text = "Health & Safety",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 16.dp)
                            .fillMaxWidth()
                    )
                }
                item {

                    Text(
                        text = "Sanitation Protocol",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(start = 16.dp, end = 16.dp)
                            .fillMaxWidth()
                    )

                    Text(
                        text = "Accommodations are thoroughly cleaned and sanitized between guest stays.",
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                            .padding(start = 16.dp, end = 16.dp)
                            .fillMaxWidth()
                    )
                    Divider(
                        color = Color(0xFFDEDEDE),
                        modifier = Modifier.padding(vertical = 10.dp, horizontal = 10.dp)
                    )
                }
                item {

                    if (hasFirstAid!!) {
                        Text(
                            text = "First Aid Kit",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .padding(start = 16.dp, end = 16.dp)
                                .fillMaxWidth()
                        )

                        Text(
                            text = "First aid kits are available in designated areas.",
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier
                                .padding(start = 16.dp, end = 16.dp)
                                .fillMaxWidth()
                        )
                        Divider(
                            color = Color(0xFFDEDEDE),
                            modifier = Modifier.padding(vertical = 10.dp, horizontal = 10.dp)
                        )

                    }
                }
                item {

                    if (hasFireExit!!) {
                        Text(
                            text = "Fire Safety Instructions",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .padding(start = 16.dp, end = 16.dp)
                                .fillMaxWidth()
                        )

                        Text(
                            text = "Evacuation plan are posted on designated areas. Follow marked exits in case of fire alarm.",
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier
                                .padding(start = 16.dp, end = 16.dp)
                                .fillMaxWidth()
                        )
                        Divider(
                            color = Color(0xFFDEDEDE),
                            modifier = Modifier.padding(vertical = 10.dp, horizontal = 10.dp)
                        )

                    }
                }
                item {

                    if (hasFireExtinguisher!!) {
                        Text(
                            text = "Fire Extinguisher",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .padding(start = 16.dp, end = 16.dp)
                                .fillMaxWidth()
                        )

                        Text(
                            text = "Fire extinguishers are available at designated areas. Please use only in case of a fire.",
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier
                                .padding(start = 16.dp, end = 16.dp)
                                .fillMaxWidth()
                        )
                        Divider(
                            color = Color(0xFFDEDEDE),
                            modifier = Modifier.padding(vertical = 10.dp, horizontal = 10.dp)
                        )

                    }
                }

                item {
                    if(hasSecurityCamera!!) {
                        Text(
                            text = "Security Measures",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .padding(start = 16.dp, end = 16.dp)
                                .fillMaxWidth()
                        )

                        Text(
                            text = "Security cameras are in place. Please use provided locks and security devices for safety.",
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier
                                .padding(start = 16.dp, end = 16.dp)
                                .fillMaxWidth()
                        )
                        Divider(
                            color = Color(0xFFDEDEDE),
                            modifier = Modifier.padding(vertical = 10.dp, horizontal = 10.dp)
                        )
                        Spacer(modifier = Modifier.weight(1f))

                        Spacer(modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars))
                    }
                    else{
                        Text(
                            text = "Security Measures",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .padding(start = 16.dp, end = 16.dp)
                                .fillMaxWidth()
                        )

                        Text(
                            text = "Please use provided locks and security devices for safety.",
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier
                                .padding(start = 16.dp, end = 16.dp)
                                .fillMaxWidth()
                        )
                        Divider(
                            color = Color(0xFFDEDEDE),
                            modifier = Modifier.padding(vertical = 10.dp, horizontal = 10.dp)
                        )
                        Spacer(modifier = Modifier.weight(1f))

                        Spacer(modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars))
                    }
                }

            }
        }


    }

    if(openCancelAndReschedule.value){
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
                        onClick = { openCancelAndReschedule.value = false },
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Close"
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                }
            },
            onDismissRequest = { openCancelAndReschedule.value = false },
            sheetState = bottomSheetState,
            modifier = Modifier
                .fillMaxHeight(0.8f) //0.693
        ) {

            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                item {
                    Text(
                        text = "Cancellation and Rescheduling Policy",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 16.dp)
                            .fillMaxWidth()
                    )
                }

                item {

                    if (noCancel!!) {
                        Text(
                            text = "Cancellation Policy",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .padding(start = 16.dp, end = 16.dp)
                                .fillMaxWidth()
                        )

                        Text(
                            text = "Guests can only cancel 14 days before the booked appointment for a full refund. Cancellations are not allowed for anytime shorter than 14 days",
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier
                                .padding(start = 16.dp, end = 16.dp)
                                .fillMaxWidth()
                        )
                        Divider(
                            color = Color(0xFFDEDEDE),
                            modifier = Modifier.padding(vertical = 10.dp, horizontal = 10.dp)
                        )

                    } else{
                        Text(
                            text = "Cancellation Policy",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .padding(start = 16.dp, end = 16.dp)
                                .fillMaxWidth()
                        )

                        Text(
                            text = "Guests can only cancel 14 days before the booked appointment for a full refund. For cancellations 7 days before appointed date, an 80% refund will be given. Cancellations are not allowed for anything shorter than 7 days",
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier
                                .padding(start = 16.dp, end = 16.dp)
                                .fillMaxWidth()
                        )
                        Divider(
                            color = Color(0xFFDEDEDE),
                            modifier = Modifier.padding(vertical = 10.dp, horizontal = 10.dp)
                        )
                    }
                }
                item {

                    if (noReschedule!!) {
                        Text(
                            text = "Rescheduling Policy",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .padding(start = 16.dp, end = 16.dp)
                                .fillMaxWidth()
                        )

                        Text(
                            text = "Guests can only reschedule 14 days before the booked appointment. Rescheduling are not allowed for anytime shorter than 14 days",
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier
                                .padding(start = 16.dp, end = 16.dp)
                                .fillMaxWidth()
                        )
                        Divider(
                            color = Color(0xFFDEDEDE),
                            modifier = Modifier.padding(vertical = 10.dp, horizontal = 10.dp)
                        )

                    } else{
                        Text(
                            text = "Rescheduling Policy",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .padding(start = 16.dp, end = 16.dp)
                                .fillMaxWidth()
                        )

                        Text(
                            text = "Guests can reschedule 7 days before the booked appointment. Rescheduling are not allowed for anytime shorter than 7 days",
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier
                                .padding(start = 16.dp, end = 16.dp)
                                .fillMaxWidth()
                        )
                        Divider(
                            color = Color(0xFFDEDEDE),
                            modifier = Modifier.padding(vertical = 10.dp, horizontal = 10.dp)
                        )
                        Spacer(modifier = Modifier.weight(1f))

                        Spacer(modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars))
                    }
                }


            }
        }


    }

    if(openBusinessInformation.value){
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
                        onClick = { openBusinessInformation.value = false },
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Close"
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                }
            },
            onDismissRequest = { openBusinessInformation.value = false },
            sheetState = bottomSheetState,
            modifier = Modifier
                .fillMaxHeight(0.8f) //0.693
        ) {

            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                item {
                    Text(
                        text = "Business Information",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 16.dp)
                            .fillMaxWidth()
                    )
                }
                item {

                    Text(
                        text = "About Us",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(start = 16.dp, end = 16.dp)
                            .fillMaxWidth()
                    )

                    Text(
                        text = staycationDetails!!,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                            .padding(start = 16.dp, end = 16.dp)
                            .fillMaxWidth()
                    )
                    Divider(
                        color = Color(0xFFDEDEDE),
                        modifier = Modifier.padding(vertical = 10.dp, horizontal = 10.dp)
                    )
                }
                item {

                        Text(
                            text = "Contact Information",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .padding(start = 16.dp, end = 16.dp)
                                .fillMaxWidth()
                        )

                        Text(
                            text = "Phone: $phoneNo",
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier
                                .padding(start = 16.dp, end = 16.dp)
                                .fillMaxWidth()
                        )
                        Text(
                            text = "Email: $email",
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier
                                .padding(start = 16.dp, end = 16.dp)
                                .fillMaxWidth()
                        )
                        Divider(
                            color = Color(0xFFDEDEDE),
                            modifier = Modifier.padding(vertical = 10.dp, horizontal = 10.dp)
                        )
                    Spacer(modifier = Modifier.weight(1f))

                    Spacer(modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars))

                }

            }
        }

    }
    }



@Composable
fun StaycationBottomBookingBar(
    modifier: Modifier = Modifier,
    staycation: Staycation? = null,
    bookButtonText: String = "Book",
    nights: String = "",
    enableButton: Boolean = true,
    onClickChatHost: () -> Unit ,
    onClickBook: () -> Unit ,
    onClickUnderlinedText: () -> Unit,
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
                            append("/night")
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
    modifier: Modifier = Modifier,
    buttonText: String,
    containerColor: Color = Color.White,
    buttonShape: RoundedCornerShape = RoundedCornerShape(10.dp),
    borderStroke: BorderStroke = BorderStroke(1.dp, Orange),
    contentPadding: PaddingValues = PaddingValues(10.dp),
    contentFontSize: TextUnit = 16.sp,
    contentFontWeight: FontWeight = FontWeight.Medium,
    contentColor: Color = Orange,
    onClick: () -> Unit,
    enableButton: Boolean = true,
){

    OutlinedButton(
        onClick = onClick,
        border = if (enableButton) borderStroke else BorderStroke(1.dp, contentColor.copy(alpha = 0.3f)),
        shape = buttonShape,
        colors = ButtonDefaults.outlinedButtonColors(containerColor),
        contentPadding = contentPadding,
        enabled = enableButton,
        modifier = modifier
    ) {
        Text(
            text = buttonText,
            fontSize = contentFontSize,
            fontWeight = contentFontWeight,
            color = if (enableButton) contentColor else contentColor.copy(alpha = 0.3f)
        )
    }
}

@Composable
fun BookingFilledButton(
    modifier: Modifier = Modifier,
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
                color = contentColor,

            )
        }

    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsTopAppBar(
    onBack: () -> Unit,
    isFavorite: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
){
  //  var isFavorite by remember { mutableStateOf(false) }
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(state = rememberTopAppBarState())

    TopAppBar(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        scrollBehavior = scrollBehavior,
        title = { /*TODO*/ },
        navigationIcon = {
            IconButton(onClick = { onBack() }) {
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
            IconToggleButton(
                checked = isFavorite,
                onCheckedChange = {
                    onCheckedChange(it)
                }
            ) {
                Image(
                    imageVector = ImageVector.vectorResource(id = R.drawable.favorite_off),
                    contentDescription = "",
                    colorFilter = if(isFavorite) ColorFilter.tint(Color(220, 20, 60)) else null
                )
            }
        }

    )
}

@Composable
fun AttractionRow(attractionUiState: AttractionUiState, modifier: Modifier = Modifier){

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
    ){
        Column {
            Row {
                Text(
                    text = attractionUiState.name,
                    fontWeight = FontWeight.Medium,
                )
                attractionUiState.tag.forEach {
                    Tag(
                        tag = it,
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                    )
                }

            }
            Row {
                Text(
                    text = attractionUiState.distance.toString() +
                            " meters • ₱ " + String.format("%.2f", attractionUiState.price) +
                            " • Opens at " + attractionUiState.openingTime,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF999999)
                )
            }
            Divider(modifier = Modifier.padding(top = 20.dp))
        }
        Image(
            painter = painterResource(id = attractionUiState.image),
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

    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .height(30.dp),
            verticalAlignment = Alignment.CenterVertically

        ) {
            Icon(
                imageVector = ImageVector.vectorResource(setImageForAmenity(amenity.amenityName)),
                contentDescription = amenity.amenityName,
                tint = Color(0xFF333333),
                modifier = Modifier.size(24.dp)
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
                    val annotatedText = if (nights == 0 || nights == null) {
                        buildAnnotatedString {
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                //append("₱ ${"%.2f".format(staycation?.staycationPrice)}")
                                append("₱ $formattedNumber")

                            }
                            append("/night")
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

    StaycationDetailsScreen(
        detailViewModel = detailViewModel,
        staycationId = "LxpNxRFdwkQzBxujF3gx",
        touristId = "n7r1JjE18t5iCP32GXjt",
        onNavToBooking = { staycationId, touristId ->
            // Your implementation here
        },
        onBack = {

        },
        onNavToChat = { _, _ ->

        },
        onNavToReviewScreen = { _, _, _ ->

        }
    )
}

