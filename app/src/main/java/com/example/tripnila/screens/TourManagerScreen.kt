package com.example.tripnila.screens

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.tripnila.R
import com.example.tripnila.common.AppLocationCard
import com.example.tripnila.common.AppReviewsCard
import com.example.tripnila.common.LoadingScreen
import com.example.tripnila.common.Orange
import com.example.tripnila.data.ReviewUiState
import com.example.tripnila.data.TourAvailableDates
import com.example.tripnila.data.TourSchedule
import com.example.tripnila.data.Transaction
import com.example.tripnila.model.TourManagerViewModel
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TourManagerScreen(
    tourId: String = "",
    hostId: String = "",
    onNavToEditTour: (String, String, String) -> Unit,
    onNavToDashboard: (String) -> Unit,
    tourManagerViewModel: TourManagerViewModel
){

    Log.d("Tour", "$tourId $hostId")

    LaunchedEffect(tourId) {
        tourManagerViewModel.getSelectedTour(tourId)
    }

    val tour = tourManagerViewModel.tour.collectAsState().value
    val touristId = hostId.substring(5)


    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f
    ) {
        tour.tourImages.size
    }


//    var tourAvailableDates = tour?.schedule
//    val availableDates = tour?.schedule?.map { tourSchedule ->
//        TourAvailableDates(
//            day = "",  // You might need to calculate the day based on the date
//            date = tourSchedule.date.toString(),  // Convert LocalDate to String
//            startingTime = tourSchedule.startTime,
//            endingTime = tourSchedule.endTime,
//            description = "",  // Add a meaningful description if needed
//            price = 0.0  // Set the actual price
//        )
//    }


    val reviews = listOf(
        ReviewUiState(
            rating = 4.5,
            comment = "A wonderful staycation experience!",
          //  touristImage = R.drawable.joshua,
            touristName = "John Doe",
            reviewDate = "2023-05-15"
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
        ReviewUiState(
            rating = 5.0,
            comment = "Amazing place and great service!",
       //     touristImage = R.drawable.joshua,
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

    val transactions = listOf(
        Transaction(
        //    customerImage = R.drawable.joshua,
            customerName = "Juan Cruz",
            customerUsername = "@jcruz",
            guestsCount = 4,
            price = 2500.00,
            bookedDate = "Sep 3, 2023",
            transactionDate = "Aug 24, 2023",
            transactionStatus = "Completed"
        ),
        Transaction(
     //       customerImage = R.drawable.joshua,
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
        // tourManagerViewModel.isStateRetrieved.collectAsState().value == false
        if (tour.tourId != tourId) {
            LoadingScreen(isLoadingCompleted = false, isLightModeActive = true)
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                item {
//                    Box(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .height(240.dp)
//                    ) {
//                        AsyncImage(
//                            model = if ( tour?.tourImages?.find { it.photoType == "Cover" }?.photoUrl == "") "https://upload.wikimedia.org/wikipedia/commons/thumb/3/3f/Placeholder_view_vector.svg/1022px-Placeholder_view_vector.svg.png" else tour?.tourImages?.find { it.photoType == "Cover" }?.photoUrl,//imageLoader,
//                            contentDescription = "",
//                            contentScale = ContentScale.Crop,
//
//                            )
//                        ManagerTopBarIcons(
//                            onEdit = {
//                                onNavToEditTour(tourId, hostId, "Tour")
//                            },
//                            onBack = {
//                                onNavToDashboard(touristId)
//                            }
//
//                        )
//                    }

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
                            ManagerTopBarIcons(
                                onEdit = {
                                    onNavToEditTour(tourId, hostId, "Tour")
                                },
                                onBack = {
                                    onNavToDashboard(touristId)
                                }

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
                        withEditButton = false,
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
                    TourDescriptionCard3(
                        image1 = tour.tourImages.find { it.photoType == "Cover" }?.photoUrl ?: "https://upload.wikimedia.org/wikipedia/commons/thumb/3/3f/Placeholder_view_vector.svg/1022px-Placeholder_view_vector.svg.png",
                        image2 = tour.tourImages.find { it.photoType == "Cover" }?.photoUrl ?: "https://upload.wikimedia.org/wikipedia/commons/thumb/3/3f/Placeholder_view_vector.svg/1022px-Placeholder_view_vector.svg.png",
                        image3 = tour.tourImages.find { it.photoType == "Cover" }?.photoUrl ?: "https://upload.wikimedia.org/wikipedia/commons/thumb/3/3f/Placeholder_view_vector.svg/1022px-Placeholder_view_vector.svg.png",
                        description = tour.tourDescription,
                        withEditButton = false,
                        modifier = Modifier
                            .offset(y = (-5).dp)
                            .padding(bottom = 12.dp)
                    )
                }
//                item {
////                     AVAILABILITY DATES
//                    TourAvailabilityCard(
//                        availableDates = availableDates,
//                        modifier = Modifier
//                            .offset(y = (-5).dp)
//                            .padding(bottom = 12.dp)
//                    )
//                }
                item {
                    TourAvailableDatesCard(
                        tourManagerViewModel = tourManagerViewModel,
                        modifier = Modifier
                            .offset(y = (-5).dp)
                            .padding(bottom = 12.dp)
                    )
                }

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
                        locationDescription = tour.tourLocation,
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
                            .padding(bottom = 12.dp)
                    )
                }
                item {
                    TourAdditionalInformationCard(
                        withEditButton = false,
                        modifier = Modifier
                            .offset(y = (-5).dp)
                            .padding(bottom = 7.dp)
                    )

                }
//                item {
//                    TourBottomBookingBar()
//                }
            }
        }

    }
}

@Composable
fun ManagerTopBarIcons(
    onBack: () -> Unit,
    onEdit: () -> Unit,
    modifier: Modifier = Modifier
) {

    val iconColor = Color.White  //Color(0xFFC0C0C0)
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
            IconButton(onClick = { onBack() }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = iconColor
                    //modifier = Modifier.padding(start = 8.dp, top = 15.dp)
                )
            }


            Spacer(modifier = Modifier.weight(1f))
            AppOutlinedButtonWithBadge(
                buttonLabel = "Edit",
                onClick = {
                    onEdit()
                },
                modifier = Modifier
                    .width(40.dp)
            )
        }
    }
}

@Composable
fun TourAvailableDatesCard(
    tourManagerViewModel: TourManagerViewModel?,
    modifier: Modifier = Modifier
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
                    text = "Available schedules",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .padding(bottom = 4.dp)
                )
            }

            TourDateSelector(
                tourManagerViewModel = tourManagerViewModel
            )

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TourDateSelector(
    tourManagerViewModel: TourManagerViewModel?,
    modifier: Modifier = Modifier
){

    DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH)

    val tourSchedules = tourManagerViewModel?.tour?.collectAsState()?.value?.schedule?.sortedWith(compareBy({ it.date }, { LocalTime.parse(it.startTime, DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH)) }))

    var isFocused by remember { mutableStateOf(false) }
    var slot by remember { mutableIntStateOf(0) }

    var openingHour by remember { mutableIntStateOf(0) }
    var openingMinute by remember { mutableIntStateOf(0) }

    var closingHour by remember { mutableIntStateOf(0) }
    var closingMinute by remember { mutableIntStateOf(0) }

    var currentDate by remember { mutableStateOf(LocalDate.now()) }
    var openDialogBox by remember { mutableStateOf(false) }

    var showOpeningDialog by remember { mutableStateOf(false) }
    var showClosingDialog by remember { mutableStateOf(false) }


    val openingTimePickerState = rememberTimePickerState(
        initialHour = openingHour,
        initialMinute = openingMinute,
        is24Hour = false
    )
    val closingTimePickerState = rememberTimePickerState(
        initialHour = closingHour,
        initialMinute = closingMinute,
        is24Hour = false
    )

    val openingCalendar = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, openingHour)
        set(Calendar.MINUTE, openingMinute)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }

    val closingCalendar = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, closingHour)
        set(Calendar.MINUTE, closingMinute)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }


    val formattedOpeningTime = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(openingCalendar.time)
    val formattedClosingTime = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(closingCalendar.time)

    LaunchedEffect(openDialogBox) {
        if (!openDialogBox) {
            openingHour = 0
            openingMinute = 0
            closingHour = 0
            closingMinute = 0
            slot = 0
        }
    }


    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(10.dp),
        modifier = modifier
            .fillMaxWidth()
    ){
        Row(
            modifier = Modifier
                .background(color = Orange)
                .padding(horizontal = 10.dp)
                .fillMaxWidth()
                .height(45.dp)
                .clip(
                    shape = RoundedCornerShape(
                        topStart = 10.dp,
                        topEnd = 10.dp
                    )
                ),
            verticalAlignment = Alignment.CenterVertically,

            // horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            IconButton(onClick = {
                currentDate = currentDate.minusMonths(1)
            }) {
                Icon(imageVector = Icons.Filled.KeyboardArrowLeft, tint = Color.White, modifier = Modifier.size(18.dp), contentDescription = null)
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = currentDate.format(DateTimeFormatter.ofPattern("MMMM yyyy")) ,
                color = Color.White,
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            )
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = { currentDate = currentDate.plusMonths(1) }) {
                Icon(imageVector = Icons.Filled.KeyboardArrowRight, tint = Color.White, modifier = Modifier.size(18.dp),contentDescription = null)
            }
        }
        Row(
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .fillMaxWidth()
                .height(45.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = { currentDate = currentDate.minusDays(1) }) {
                Icon(imageVector = Icons.Filled.KeyboardArrowLeft, tint = Orange, modifier = Modifier.size(18.dp), contentDescription = null)
            }
            Spacer(modifier = Modifier.weight(1f))
            Column(
                modifier = Modifier.width(80.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = currentDate.format(DateTimeFormatter.ofPattern("d")),
                    color = Color(0xfff9a664),
                    style = TextStyle(
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Medium)
                )
                Text(
                    text = currentDate.format(DateTimeFormatter.ofPattern("EEEE")),
                    color = Color(0xfff9a664),
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium)
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = { currentDate = currentDate.plusDays(1) }) {
                Icon(imageVector = Icons.Filled.KeyboardArrowRight, tint = Orange, modifier = Modifier.size(18.dp),contentDescription = null)
            }
        }
        Divider()
        val stroke = Stroke(
            width = 8f,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
        )
        Column(
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .fillMaxWidth()
        ){

            tourSchedules?.forEach { tourSchedule ->
                TourScheduleCard(
                    tourSchedule = tourSchedule,
                    onDelete = { sched ->
                        tourManagerViewModel.removeSchedule(sched)
                    },
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .padding(top = 10.dp)
                )

            }

            Box(
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 10.dp)
                    .fillMaxWidth()
                    .height(45.dp)
                    .clip(shape = RoundedCornerShape(10.dp))
                    .drawBehind {
                        drawRoundRect(
                            color = Orange,
                            style = stroke,
                            cornerRadius = CornerRadius(10.dp.toPx())
                        )
                    }
                    .clickable {
                        openDialogBox = true
                    }
            ){
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Add",
                        tint = Orange,
                        modifier = Modifier
                            .size(18.dp)
                            .padding(end = 3.dp)
                    )
                    Text(
                        text = "Add more",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = Orange
                    )

                }

                if (openDialogBox) {
                    Dialog(onDismissRequest = { openDialogBox = false }) {
                        // Draw a rectangle shape with rounded corners inside the dialog
                        Card(
                            colors = CardDefaults.outlinedCardColors(
                                containerColor = Color.White
                            ),
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(1.dp, Color(0xff999999)),
                            modifier = Modifier
                                .fillMaxWidth()

                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(horizontal = 10.dp)
                                    .fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {

                                Text(
                                    text = "Set time",
                                    color = Color(0xff333333),
                                    style = TextStyle(
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Medium
                                    ),
                                    modifier = Modifier
                                        .align(Alignment.Start)
                                        .padding(10.dp)
                                )

                                Row(
                                    modifier = Modifier
                                        .padding(
                                            horizontal = 20.dp,
                                            vertical = 3.dp
                                        )
                                        .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center,
                                ) {
                                    Text(
                                        text = "Start",
                                        color = Color(0xff333333),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium,
                                        modifier = Modifier.padding(end = 10.dp, top = 5.dp)
                                    )
                                    Card(
                                        colors = CardDefaults.outlinedCardColors(
                                            containerColor = Color.White
                                        ),
                                        shape = RoundedCornerShape(10.dp),
                                        border = BorderStroke(1.dp, Color(0xffc2c2c2)),
                                        modifier = Modifier
                                            .padding(end = 10.dp)
                                            .width(75.dp)
                                            .height(30.dp)
                                            .clickable {
                                                showOpeningDialog = true
                                            }

                                    ) {

                                        Column(
                                            modifier = Modifier.fillMaxSize(),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Text(
                                                text = "Time",
                                                color = Color(0xffc2c2c2),
                                                fontSize = 7.sp,
                                                fontWeight = FontWeight.Medium,
                                                modifier = Modifier
                                                    .padding(start = 5.dp)
                                                    .align(Alignment.Start)

                                            )

                                            Text(
                                                text = if (formattedOpeningTime == "12:00 AM") "00:00 AM" else formattedOpeningTime,
                                                color = Color(0xff333333),
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Medium
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.weight(1f))
                                    Text(
                                        text = "End",
                                        color = Color(0xff333333),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium,
                                        modifier = Modifier
                                            .padding(end = 10.dp, top = 5.dp)
                                            .offset(x = 10.dp)
                                    )
                                    Card(
                                        colors = CardDefaults.outlinedCardColors(
                                            containerColor = Color.White
                                        ),
                                        shape = RoundedCornerShape(10.dp),
                                        border = BorderStroke(1.dp, Color(0xffc2c2c2)),
                                        modifier = Modifier
                                            .padding(end = 10.dp)
                                            .width(75.dp)
                                            .height(30.dp)
                                            .offset(x = 10.dp)
                                            .clickable {
                                                showClosingDialog = true
                                            }

                                    ) {
                                        Column(
                                            modifier = Modifier.fillMaxSize(),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Text(
                                                text = "Time",
                                                color = Color(0xffc2c2c2),
                                                fontSize = 7.sp,
                                                fontWeight = FontWeight.Medium,
                                                modifier = Modifier
                                                    .padding(start = 5.dp)
                                                    .align(Alignment.Start)

                                            )


                                            Text(
                                                text = if (formattedClosingTime == "12:00 AM") "00:00 AM" else formattedClosingTime,
                                                color = Color(0xff333333),
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Medium
                                            )
                                        }
                                    }
                                }

                                // SLOT
                                Row(
                                    modifier = Modifier
                                        .padding(
                                            horizontal = 20.dp,
                                            vertical = 3.dp
                                        )
                                        .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Start,
                                ) {
                                    Text(
                                        text = "Slot",
                                        color = Color(0xff333333),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium,
                                        modifier = Modifier.padding(
                                            end = 10.dp,
                                            top = 5.dp
                                        )
                                    )
                                    BasicTextField(
                                        value = slot.toString(),
                                        onValueChange = { newValue ->
                                            val newText = newValue.filter { it.isDigit() }
                                            slot = if (newText.isNotEmpty()) newText.toInt() else 0
                                        },
                                        textStyle = TextStyle(
                                            fontWeight = FontWeight.Medium,
                                            color = Color.Black
                                        ),
                                        keyboardOptions = KeyboardOptions(
                                            imeAction = ImeAction.Done,
                                            keyboardType = KeyboardType.Number
                                        ),
                                        singleLine = true,
                                        decorationBox = { innerTextField ->
                                            Row(
                                                modifier = Modifier
                                                    .background(color = Color.White, shape = RoundedCornerShape(size = 10.dp))
                                                    .border(
                                                        width = 1.dp,
                                                        color = if (isFocused) {
                                                            Orange
                                                        } else {
                                                            Color(0xffc2c2c2)
                                                        },
                                                        shape = RoundedCornerShape(size = 10.dp)
                                                    )
                                                    .padding(horizontal = 8.dp), // inner padding
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                innerTextField()
                                            }
                                        },
                                        modifier = Modifier
                                            // .width(80.dp)
                                            .width(75.dp)
                                            .height(30.dp)
                                            .offset(x = 5.dp)
                                            .onFocusChanged { focusState ->
                                                isFocused = focusState.isFocused
                                            }
                                    )
                                }


                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 15.dp)
                                        .padding(vertical = 10.dp)
                                ) {
                                    BookingOutlinedButton(
                                        buttonText = "Cancel",
                                        contentPadding = PaddingValues(vertical = 0.dp),
                                        onClick = { openDialogBox = false },
                                        modifier = Modifier
                                            .width(105.dp)
                                            .height(30.dp)
                                    )
                                    Spacer(modifier = Modifier.weight(1f))
                                    BookingFilledButton(
                                        buttonText = "Add",
                                        contentPadding = PaddingValues(vertical = 0.dp),
                                        onClick = {
                                            tourManagerViewModel?.setSchedule(
                                                TourSchedule(date = currentDate, startTime = formattedOpeningTime, endTime = formattedClosingTime, slot = slot, bookedSlot = 0))
                                            openDialogBox = false
                                        },
                                        modifier = Modifier
                                            .width(105.dp)
                                            .height(30.dp)
                                    )
                                }



                            }

                            if (showOpeningDialog ) { //showOpeningDialog &&
                                AlertDialog(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(
                                            color = MaterialTheme.colorScheme.surface,
                                            shape = RoundedCornerShape(size = 12.dp)
                                        ),
                                    onDismissRequest = {
                                        openingHour = 0
                                        openingMinute = 0
                                        showOpeningDialog = false
                                    }

                                ) {
                                    Column(
                                        modifier = Modifier
                                            .background(
                                                color = Color.LightGray.copy(
                                                    alpha = 0.3f
                                                )
                                            )
                                            .padding(
                                                top = 28.dp,
                                                start = 20.dp,
                                                end = 20.dp,
                                                bottom = 12.dp
                                            ),
                                        verticalArrangement = Arrangement.Center,
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        // time picker
                                        TimeInput(
                                            state = openingTimePickerState,
                                            colors = TimePickerDefaults.colors(
                                                clockDialColor = Orange,
                                                selectorColor = Orange,
                                                containerColor = Orange.copy(.3f),
                                                timeSelectorSelectedContainerColor = Orange.copy(.5f),
                                                timeSelectorUnselectedContainerColor = Orange.copy(.3f),
                                                periodSelectorSelectedContainerColor = Orange.copy(.5f),
                                                timeSelectorSelectedContentColor = Color.Black,
                                                timeSelectorUnselectedContentColor = Color.Black.copy(.5f),

                                                )
                                        )

                                        // buttons
                                        Row(
                                            modifier = Modifier
                                                .padding(top = 12.dp)
                                                .fillMaxWidth(),
                                            horizontalArrangement = Arrangement.End
                                        ) {
                                            // dismiss button
                                            TextButton(onClick = {
                                                openingHour = 0
                                                openingMinute = 0
                                                showOpeningDialog = false
                                            }) {
                                                Text(text = "Dismiss", color = Orange)
                                            }

                                            // confirm button
                                            TextButton(
                                                onClick = {
                                                    openingHour = openingTimePickerState.hour
                                                    openingMinute = openingTimePickerState.minute
                                                    showOpeningDialog = false
                                                }
                                            ) {
                                                Text(text = "Confirm", color = Orange)
                                            }
                                        }
                                    }
                                }
                            }
                            if (showClosingDialog ) { //showOpeningDialog &&
                                AlertDialog(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(
                                            color = MaterialTheme.colorScheme.surface,
                                            shape = RoundedCornerShape(size = 12.dp)
                                        ),
                                    onDismissRequest = {
                                        closingHour = 0
                                        closingMinute = 0
                                        showClosingDialog = false
                                    }
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .background(
                                                color = Color.LightGray.copy(
                                                    alpha = 0.3f
                                                )
                                            )
                                            .padding(
                                                top = 28.dp,
                                                start = 20.dp,
                                                end = 20.dp,
                                                bottom = 12.dp
                                            ),
                                        verticalArrangement = Arrangement.Center,
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        // time picker
                                        TimeInput(
                                            state = closingTimePickerState,
                                            colors = TimePickerDefaults.colors(
                                                clockDialColor = Orange,
                                                selectorColor = Orange,
                                                containerColor = Orange.copy(.3f),
                                                timeSelectorSelectedContainerColor = Orange.copy(
                                                    .5f
                                                ),
                                                timeSelectorUnselectedContainerColor = Orange.copy(
                                                    .3f
                                                ),
                                                periodSelectorSelectedContainerColor = Orange.copy(
                                                    .5f
                                                ),
                                                timeSelectorSelectedContentColor = Color.Black,
                                                timeSelectorUnselectedContentColor = Color.Black.copy(
                                                    .5f
                                                ),

                                                )


                                        )

                                        // buttons
                                        Row(
                                            modifier = Modifier
                                                .padding(top = 12.dp)
                                                .fillMaxWidth(),
                                            horizontalArrangement = Arrangement.End
                                        ) {
                                            // dismiss button
                                            TextButton(onClick = {
                                                closingHour = 0
                                                closingMinute = 0
                                                showClosingDialog = false
                                            }) {
                                                Text(
                                                    text = "Dismiss",
                                                    color = Orange
                                                )
                                            }

                                            // confirm button
                                            TextButton(
                                                onClick = {
                                                    closingHour = closingTimePickerState.hour
                                                    closingMinute = closingTimePickerState.minute
                                                    showClosingDialog = false

                                                }
                                            ) {
                                                Text(
                                                    text = "Confirm",
                                                    color = Orange
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

            }
        }
    }


}


@Preview
@Composable
private fun TourManagerPreviews(){

  //  TourAvailableDatesCard()


}


@Preview
@Composable
private fun TourManagerScreenPreview() {

    val tourManagerViewModel = viewModel(modelClass = TourManagerViewModel::class.java)
    val tourId = "20019"

//    LaunchedEffect(tourId) {
//        tourManagerViewModel?.getSelectedTour(tourId)
//    }
//
    TourManagerScreen(
        tourId = tourId,
        hostId = "HOST-ITZbCFfF7Fzqf1qPBiwx",
        onNavToEditTour = { para1,para2,para3 ->

        },
        onNavToDashboard = {

        },
        tourManagerViewModel = tourManagerViewModel
    )

}