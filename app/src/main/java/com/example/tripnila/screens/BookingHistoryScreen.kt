package com.example.tripnila.screens

import android.view.MotionEvent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.tripnila.R
import com.example.tripnila.common.AppConfirmAndPayDivider
import com.example.tripnila.common.AppDropDownFilter
import com.example.tripnila.common.AppYourTripRow
import com.example.tripnila.common.Orange
import com.example.tripnila.common.Tag
import com.example.tripnila.common.TouristBottomNavigationBar
import com.example.tripnila.data.BookingHistory
import com.example.tripnila.data.StaycationBooking
import com.example.tripnila.model.BookingHistoryViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.TextStyle
import java.time.temporal.ChronoUnit
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@Composable
fun BookingHistoryScreen(
    touristId: String = "",
    bookingHistoryViewModel: BookingHistoryViewModel? = null,
    navController: NavHostController? = null,
    onBack: () -> Unit,
){

    var selectedItemIndex by rememberSaveable { mutableStateOf(3) }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    // val staycationBookingList = bookingHistoryViewModel?.staycationBookingList?.collectAsState()?.value
    val addReviewResult = bookingHistoryViewModel?.addReviewResult?.collectAsState()?.value
    val isCancelBookingSuccessful = bookingHistoryViewModel?.isSuccessCancelBooking?.collectAsState()?.value
    val staycationBookingFlow = remember { bookingHistoryViewModel!!.getStaycationBookingsForTourist(touristId) }
    val staycationBookingItems = staycationBookingFlow.collectAsLazyPagingItems()

    LaunchedEffect(touristId) {
        bookingHistoryViewModel?.updateStaycationBookingsStatus()
    }

    LaunchedEffect(addReviewResult) {
        if (addReviewResult != null) {
            snackbarHostState.showSnackbar(addReviewResult)
            bookingHistoryViewModel.clearAddReviewResult()
        }
    }

    LaunchedEffect(isCancelBookingSuccessful) {
        if (isCancelBookingSuccessful != null) {
            if (isCancelBookingSuccessful == true) {
                staycationBookingItems.refresh()
                delay(300)
                snackbarHostState.showSnackbar("Booking Cancelled")

            } else {
                snackbarHostState.showSnackbar("Cancellation Failed")
            }

            bookingHistoryViewModel.clearSuccessCancelBooking()
        }
    }





    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Scaffold(
            topBar = {
                Surface(
                    shadowElevation = 10.dp
                ) {
                    BookingHistoryTopBar(
                        onBack = {
                            onBack()
                        }
                    )
                }

            },
            bottomBar = {
                navController?.let {
                    TouristBottomNavigationBar(
                        touristId = touristId,
                        navController = it,
                        selectedItemIndex = selectedItemIndex,
                        onItemSelected = { newIndex ->
                            selectedItemIndex = newIndex
                        }
                    )
                }
            },
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                if (staycationBookingItems.loadState.refresh == LoadState.Loading) {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            CircularProgressIndicator(
                            )
                        }
                    }
                }
                items(staycationBookingItems) { staycationBooking ->
                    staycationBooking?.let {
                        val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
                        dateFormat.timeZone = TimeZone.getTimeZone("Asia/Manila") // Set the timezone to Manila

                        val manilaZoneId = ZoneId.of("Asia/Manila")

                        val formattedBookedDates = formatDateRange(
                            staycationBooking.checkInDate!!.toInstant()
                                .atZone(manilaZoneId)
                                .toLocalDate(),
                            staycationBooking.checkOutDate!!.toInstant()
                                .atZone(manilaZoneId)
                                .toLocalDate()
                        )


                        val bookingHistory = BookingHistory(
                            bookingId = staycationBooking.staycationBookingId,
                         //   ownerImage = staycationBooking.staycation?.hostImage
                            ownerImage = staycationBooking.staycation?.host?.profilePicture
                                ?: "https://upload.wikimedia.org/wikipedia/commons/8/89/Portrait_Placeholder.png",
                            bookedRental = staycationBooking.staycation?.staycationTitle ?: "",
                            date = dateFormat.format(staycationBooking.bookingDate ?: Date()),
                            rentalImage = "https://www.unfe.org/wp-content/uploads/2019/04/SM-placeholder.png",
                            rentalLocation = staycationBooking.staycation?.staycationLocation ?: "",
                            bookedDates = formattedBookedDates,
                            guestsNo = staycationBooking.noOfGuests,
                            totalAmount = staycationBooking.totalAmount,
                            rentalStatus = staycationBooking.bookingStatus,
                            isReviewed = staycationBooking.bookingReview != null,
                            bookingDuration = staycationBooking.getDaysDifference().toInt(),
                            staycationPrice = staycationBooking.staycation?.staycationPrice ?: 0.0,
                            checkInDate = staycationBooking.checkInDate,
                            checkOutDate = staycationBooking.checkOutDate,
                            noOfPets = staycationBooking.noOfPets,
                            noOfInfants = staycationBooking.noOfInfants,
                            noOfGuests = staycationBooking.noOfGuests
                        )

                        bookingHistoryViewModel?.let { bookingHistoryViewModel ->
                            BookingHistoryCard(
                                bookingHistoryViewModel = bookingHistoryViewModel,
                                bookingHistory = bookingHistory,
                                coroutineScope = coroutineScope,
                                modifier = Modifier.padding(top = 15.dp),
                                staycationBookingItems = staycationBookingItems,
                            )
                        }
                    }

                }
                if (staycationBookingItems.loadState.append == LoadState.Loading) {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            CircularProgressIndicator(
                            )
                        }
                    }
                }
            }
        }
    }
}





private fun formatDateRange(startDate: LocalDate, endDate: LocalDate): String {
    val startDay = startDate.dayOfMonth
    val endDay = endDate.dayOfMonth
    val startMonth = startDate.month.getDisplayName(TextStyle.SHORT, Locale.getDefault())
    val endMonth = endDate.month.getDisplayName(TextStyle.SHORT, Locale.getDefault())

    return if (startDate.month == endDate.month) {
        "$startDay-$endDay $startMonth"
    } else {
        "$startDay $startMonth-$endDay $endMonth"
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingHistoryCard(
    bookingHistoryViewModel: BookingHistoryViewModel,
    bookingHistory: BookingHistory,
    coroutineScope: CoroutineScope,
    staycationBookingItems: LazyPagingItems<StaycationBooking>,
    modifier: Modifier = Modifier
){
    val isSuccessAddingReview = bookingHistoryViewModel.isSuccessAddReview.collectAsState().value
    val isCancelBookingSuccessful = bookingHistoryViewModel.isSuccessCancelBooking.collectAsState().value

    var openReviewCard by remember { mutableStateOf(false) }
    var openAlertDialog = remember{ mutableStateOf(false)}
    var isModalBottomSheetVisible by remember { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = {
            true
        }
    )

    LaunchedEffect(isSuccessAddingReview) {
        if (isSuccessAddingReview == true) {

            delay(600)

            staycationBookingItems.refresh()
            openReviewCard = false
            bookingHistoryViewModel.clearIsSuccessAddReview()
        }
    }

    LaunchedEffect(isCancelBookingSuccessful) {
        if (isCancelBookingSuccessful != null) {
            if (isCancelBookingSuccessful == true) {
                isModalBottomSheetVisible = false
            }
        }
    }


    Card(
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
            .padding(
                start = 20.dp,
                end = 20.dp,
                top = 10.dp,
                bottom = 10.dp
            ),
        ){
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(100))
                        .size(25.dp)
                ){
                    val imageLoader = rememberAsyncImagePainter(
                        ImageRequest.Builder(LocalContext.current)
                            .data(data = bookingHistory?.ownerImage).apply(block = fun ImageRequest.Builder.() {
                                crossfade(true)
                            }).build()
                    )
                    Image(
                        painter = imageLoader,
                        contentDescription = "",
                        contentScale = ContentScale.Crop
                    )
                }
                Column(
                    modifier = Modifier.padding(horizontal = 5.dp)
                ) {
                    Text(
                        text = bookingHistory.bookedRental,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,

                        )
                    Text(
                        text = bookingHistory.date,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF999999)
                    )
                }
                Tag(tag = bookingHistory.rentalStatus)
              //  bookingHistoryViewModel.rentalStatus.collectAsState().value?.let { Tag(tag = it) }
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.more_vertical),
                    contentDescription = "More",
                    modifier = Modifier
                        .size(24.dp)
                        .offset(x = 10.dp)
                )

            }
            Divider(
                color = Color(0xFFDEDEDE),
                modifier = Modifier.padding(top = 10.dp, bottom = 8.dp)
            )


            if (openReviewCard){
                WriteReviewComposable(
                    bookingHistoryViewModel = bookingHistoryViewModel,
                    bookingId = bookingHistory.bookingId,
                    coroutineScope = coroutineScope,
                    onCancelClick = {
                        openReviewCard = false
                    },
                )

            }
            else{
                Row {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(0.45f)
                            .padding(start = 10.dp)
                    ) {
                        Text(
                            text = "Location",
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = bookingHistory.rentalLocation,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF999999)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Dates",
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = bookingHistory.bookedDates,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF999999)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Guests",
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = bookingHistory.guestsNo.toString(),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF999999)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Total",
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "₱ ${"%.2f".format(bookingHistory.totalAmount)}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = Orange
                        )
                    }
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        ElevatedCard(
                            shape = RoundedCornerShape(10.dp),
                            elevation = CardDefaults.elevatedCardElevation(
                                defaultElevation = 10.dp
                            ),
                            modifier = Modifier
                                .height(120.dp)
                                .width(140.dp)
                                .align(Alignment.End),
                        ) {

                            val imageLoader = rememberAsyncImagePainter(
                                ImageRequest.Builder(LocalContext.current)
                                    .data(data = bookingHistory?.rentalImage ?: "https://upload.wikimedia.org/wikipedia/commons/thumb/3/3f/Placeholder_view_vector.svg/1022px-Placeholder_view_vector.svg.png")
                                    .apply(block = fun ImageRequest.Builder.() {
                                        crossfade(true)
                                    }).build()
                            )
                            Image(
                                painter = imageLoader,
                                contentDescription = "Image",
                                contentScale = ContentScale.FillBounds    //FillBounds
                            )
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp)
                        ) {
                            if (bookingHistory.rentalStatus == "Pending"){
                                BookingOutlinedButton(
                                    buttonText = "Chat host",
                                    onClick = {
                                              /*TODO*/
                                    },
                                    modifier = Modifier.width(95.dp)
                                )
                                Spacer(modifier = Modifier.weight(1f))
                                BookingFilledButton(
                                    buttonText = "Cancel",
                                    onClick = {
                                      //  isOpen.value = true
                                        isModalBottomSheetVisible = true
                                    //    bookingHistoryViewModel.setSelectedBookingHistory(bookingHistory)
                                    },
                                    modifier = Modifier.width(95.dp)
                                )
                            }
                            else{
                                Spacer(modifier = Modifier.weight(1f))
                                BookingOutlinedButton(
                                    enableButton = !bookingHistory.isReviewed && bookingHistory.rentalStatus == "Completed", //|| bookingHistory.rentalStatus != "Cancelled"

                                    buttonText = "Review",
                                    onClick = {
                                        openReviewCard = true
                                    },
                                    modifier = Modifier.width(95.dp),
                                )
                            }
                        }
                    }
                }

            }


            if (isModalBottomSheetVisible) {
                // selectedBookingHistory?.let { selectedHistory ->
                ModalBottomSheet(
                    modifier = Modifier.fillMaxHeight(0.75f),
                    sheetState = bottomSheetState,
                    onDismissRequest = {
                        //bookingHistoryViewModel.clearBookingHistory()
                        isModalBottomSheetVisible = false
                    },
                    dragHandle = {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            //horizontalAlignment = Alignment.End
                        ) {
                            IconButton(
                                onClick = {
                                    isModalBottomSheetVisible = false
                                },
                                modifier = Modifier
                                    .padding(end = 12.dp, top = 8.dp)
                                    .size(30.dp)
                                    .align(Alignment.End)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Close,
                                    contentDescription = "Close",
                                    tint = Color(0xFFCECECE),
                                    modifier = Modifier.size(30.dp)
                                )
                            }
                        }
                    },
                    containerColor = Color.White
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(
                                horizontal = 25.dp,
//                vertical = 20.dp // 12
                            )
                            .background(Color.White)
                    ) {
                        Text(
                            text = "Cancel booking",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                        AppConfirmAndPayDivider(
                            image = R.drawable.staycation1,
                            itinerary = bookingHistory?.bookedRental ?: "",
                            price = bookingHistory?.staycationPrice ?: 0.0,
                            unit = "night"
                        )


                        if (bookingHistory != null) {
                            YourTripCancellationDivider(bookingHistory)
                        }

                        var checkInLocalDate = bookingHistory?.checkInDate?.let { date ->
                            Instant.ofEpochMilli(date.time).atZone(ZoneId.systemDefault()).toLocalDate()
                        }
                        val daysBeforeCheckIn: Int = checkInLocalDate?.let {
                            ChronoUnit.DAYS.between(LocalDate.now(), it).toInt()
                        } ?: 0

                        AppPaymentDivider(
                            forCancelBooking = true,
                            bookingHistoryViewModel = bookingHistoryViewModel,
                            bookingFee = bookingHistory?.staycationPrice ?: 0.0,
                            bookingDuration = bookingHistory!!.bookingDuration?.toInt() ?: 0,
                            maintenanceFee = bookingHistory?.staycationPrice?.times(0.02) ?: 0.0,
                            tripnilaFee = bookingHistory?.staycationPrice?.times(0.05) ?: 0.0,
                            daysBeforeCheckIn = daysBeforeCheckIn,
                        )
                        CancellationAgreementText()
                        Spacer(modifier = Modifier.height(15.dp))
                        BookingFilledButton(
                            isLoading = bookingHistoryViewModel.isLoadingCancelBooking.collectAsState().value,
                            buttonText = "Cancel booking",
                            onClick = {
                                bookingHistoryViewModel.setAlertDialogMessage()
                                openAlertDialog.value = true
                            },
                            modifier = Modifier
                                .padding(horizontal = 10.dp)
                                .fillMaxWidth()
                        )
                    }
                }
            }

        }
    }


//    LaunchedEffect(bookingHistory) {
//        if (bookingHistory != null) {
//            bookingHistoryViewModel.selectBookingHistory(bookingHistory)
//        } else {
//            bookingHistoryViewModel.clearBookingHistory()
//        }
//    }


    //val selectedBookingHistory by bookingHistoryViewModel.selectedBookingHistory.collectAsState()



    if (openAlertDialog.value) {
        Dialog(onDismissRequest = { openAlertDialog.value = false }) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 20.dp
                ),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    //.height(200.dp)
                    .padding(20.dp),
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                ) {
                    Text(
                        text = bookingHistoryViewModel?.alertDialogMessage?.collectAsState()?.value ?: "",
                        color = Color.Black,
                        fontSize = 18.sp,
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                            .padding(top = 25.dp),
                    )
                    Spacer(modifier = Modifier.height(30.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        //   horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        if (bookingHistoryViewModel?.alertDialogMessage?.collectAsState()?.value != "Are you sure you want to proceed?") {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                        TextButton(
                            onClick = { openAlertDialog.value = false },
                            modifier = Modifier.padding(horizontal = 10.dp),
                        ) {
                            Text("Dismiss", color = Color.Black)
                        }
                        if (bookingHistoryViewModel?.alertDialogMessage?.collectAsState()?.value == "Are you sure you want to proceed?") {
                            Spacer(modifier = Modifier.weight(1f))
                            TextButton(
                                onClick = {

                                    coroutineScope.launch {
                                        openAlertDialog.value = false


                                        bookingHistoryViewModel?.cancelStaycationBooking(bookingId = bookingHistory.bookingId)

//                                        delay(5000)
//                                        isOpen = false
                                    }


                                },
                                modifier = Modifier.padding(horizontal = 10.dp),
                            ) {
                                Text("Confirm", color = Color.Black)
                            }
                        }
                    }
                }
            }
        }
    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingHistoryTopBar(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
){

    val filter = listOf("Relevance", "Recent", "Top rated")

    TopAppBar(
        navigationIcon = {
            IconButton(onClick = {
                onBack()
            }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color(0xFF999999)
                )
            }

        },
        title = {
            Row(
                modifier = Modifier.padding(horizontal = 10.dp),
                verticalAlignment = Alignment.CenterVertically

            ) {
                Text(
                    text = "History",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.weight(1f))
                AppDropDownFilter(options = filter)
            }
        },
        modifier = modifier
    )
}

@Composable
fun YourTripCancellationDivider(
    bookingHistory: BookingHistory,
    modifier: Modifier = Modifier,
) {

    var checkInLocalDate= bookingHistory.checkInDate?.let { date ->
        Instant.ofEpochMilli(date.time).atZone(ZoneId.systemDefault()).toLocalDate()
    }
    var checkOutLocalDate= bookingHistory.checkOutDate?.let { date ->
        Instant.ofEpochMilli(date.time).atZone(ZoneId.systemDefault()).toLocalDate()
    }

    val startDay = checkInLocalDate?.dayOfMonth
    val endDay = checkOutLocalDate?.dayOfMonth
    val startMonth = checkInLocalDate?.month?.getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
    val endMonth = checkOutLocalDate?.month?.getDisplayName(TextStyle.SHORT, Locale.ENGLISH)

    val formattedDateRange = if (checkInLocalDate?.month == checkOutLocalDate?.month) "$startDay-$endDay $startMonth" else "$startDay $startMonth-$endDay $endMonth"

    var guestCount = bookingHistory.noOfGuests
    var infantCount = bookingHistory.noOfInfants
    var petCount = bookingHistory.noOfPets

    val guestInfoText = buildAnnotatedString {
        append("$guestCount ${
            guestCount?.let {
                if (it == 1) "guest" else "guests"
            }
        }")

        if (infantCount!! > 0) {
            append(", $infantCount ${
                infantCount?.let {
                    if (it == 1) "infant" else "infants"
                }
            }")
        }

        if (petCount!! > 0) {
            append(", $petCount ${
                petCount?.let {
                    if (it == 1) "pet" else "pets"
                }
            }")
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(all = 10.dp)

    ){
        Text(
            text = "Your trip",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
        val dateRowText = formattedDateRange.takeIf { it?.isNotBlank() == true } ?: "Select dates*"
        val dateRowTextColor = if (formattedDateRange?.isNotBlank() == true) Color(0xFF999999) else Color.Red


        AppYourTripRow(
            rowLabel = "Dates",
            rowText = dateRowText,
            fontColor = dateRowTextColor,
            forCancelBooking = true

        )

        // Display a prompt if guestCount is null or zero
        val guestCountText = when {
            guestCount == null || guestCount <= 0 -> "Select guests*"
            else -> "$guestInfoText"
        }

        val guestColor = when {
            guestCount == null || guestCount <= 0 -> Color.Red
            else -> Color(0xFF999999)
        }
        AppYourTripRow(
            rowLabel = "Guests",
            fontColor = guestColor,
            rowText = guestCountText,
            forCancelBooking = true
        )
    }
    Divider(
        color = Color(0xFF999999),
        modifier = Modifier.padding(top = 5.dp) // 5.dp
    )
}



@Composable
fun WriteReviewComposable(
    bookingHistoryViewModel: BookingHistoryViewModel,
    coroutineScope: CoroutineScope,
    bookingId: String,
    onCancelClick: () -> Unit,
){

    val reviewInput = bookingHistoryViewModel.userInputReview.collectAsState().value

    val localFocusManager = LocalFocusManager.current
    //var comment by remember { mutableStateOf("") }
    var comment = reviewInput.comment
    val maxCharacterLimit = 100
    val remainingCharacters = maxCharacterLimit - comment?.length!!



//    var selectedImageUris by remember {
//        mutableStateOf<List<Uri>>(emptyList())
//    }

    var selectedImageUris = bookingHistoryViewModel.selectedImageUris.collectAsState().value

    val multiplePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia()
    )
    //onResult = { uris -> selectedImageUris = uris }
    { uris ->
        selectedImageUris = uris
        bookingHistoryViewModel.setSelectedImageUris(selectedImageUris)
    }


    Text(
        text = "Write a review",
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp
    )
    AppRatingBar(bookingHistoryViewModel = bookingHistoryViewModel, initialRating = bookingHistoryViewModel.userInputReview.collectAsState().value.rating)
    Spacer(modifier = Modifier.height(8.dp))
    comment?.let {
        BasicTextField(
        value = it,
        onValueChange = {
            if (it.length <= maxCharacterLimit) {
                comment = it
                bookingHistoryViewModel.setComment(comment)
            }

        },
        textStyle = androidx.compose.ui.text.TextStyle(fontSize = 10.sp, color = Color(0xFF6B6B6B)),
        modifier = Modifier
            .fillMaxWidth()
            .height(65.dp)
            .border(1.dp, Color(0xFF999999), shape = RoundedCornerShape(10.dp))
        ,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions {
            localFocusManager.clearFocus()
        },
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier
                    .padding(vertical = 4.dp, horizontal = 6.dp)
                    .fillMaxWidth()
                // .fillMaxHeight()
            ){
                Row(
                    // modifier =Modifier.fillMaxHeight(),
                    verticalAlignment = Alignment.Top
                ) {
                    if (comment?.isEmpty() == true) {
                        Text(
                            text = "Write something here...",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color(0xFF6B6B6B),
                            modifier = Modifier.weight(1f)
                        )
                    }

                }
                innerTextField()
            }
        }
    )
    }
    Text(
        text = buildAnnotatedString {
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                append("$remainingCharacters")
            }
            withStyle(style = SpanStyle(fontWeight = FontWeight.Medium)) {
                append(" characters available")
            }
        },
        fontSize = 8.sp,
        color = Color.Gray
    )
    Spacer(modifier = Modifier.height(8.dp))
    Text(
        text = "Add photos",
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
    ) {
        val itemsPerRow = 2
        val rows = (selectedImageUris.size + itemsPerRow - 1) / itemsPerRow
        var lastIndex = 0

        for (row in 0 until rows) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                val startIndex = row * itemsPerRow
                val endIndex = minOf(startIndex + itemsPerRow, selectedImageUris.size)

                for (i in startIndex until endIndex) {
                    val uri = selectedImageUris[i]
                    AsyncImage(
                        model = uri,
                        contentDescription = null,
                        modifier = Modifier
                            .width(125.dp)
                            .height(80.dp)
                            .padding(vertical = 5.dp)
                             ,
                        contentScale = ContentScale.Crop
                    )

                    lastIndex = i

                    if (i == startIndex) {
                        Spacer(modifier = Modifier.weight(1f)) // Adjust the width as needed
                    }
                }


                if (lastIndex == selectedImageUris.size - 1 && selectedImageUris.size % itemsPerRow != 0) {
                    AppChoosePhoto(
                        modifier = Modifier
                            .padding(vertical = 5.dp)
                            .width(125.dp)
                            .height(70.dp),
                        onClick = {
                            multiplePhotoPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        }
                    )
                }
            }
        }

        if (selectedImageUris.size % itemsPerRow == 0) {
            // AddMorePhoto if the last row is fully filled
            Row(
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                AppChoosePhoto(
                    modifier = Modifier
                        //.padding(top = 8.dp, bottom = 12.dp)
                        .padding(vertical = 5.dp)
                        .width(125.dp)
                        .height(70.dp),
                    onClick = {
                        multiplePhotoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }
                )
            }
        }
    }



    val isLoading = bookingHistoryViewModel.isLoadingAddReview.collectAsState().value


    Row {
        BookingOutlinedButton(
            buttonText = "Cancel",
            onClick = {
                bookingHistoryViewModel.setSelectedImageUris(emptyList())
                bookingHistoryViewModel.setComment("")
                bookingHistoryViewModel.setRating(0)
                onCancelClick()
            },
            modifier = Modifier.width(90.dp)

        )
        Spacer(modifier = Modifier.weight(1f))
        BookingFilledButton(
            isLoading = isLoading,
            buttonText = "Confirm",
            onClick = {
                coroutineScope.launch {
                    bookingHistoryViewModel.setStaycationReview(bookingId)
                }
            },
            modifier = Modifier.width(90.dp)
        )
    }
}

@Composable
fun CancellationAgreementText(modifier: Modifier = Modifier) {
    Text(
        text = "This process cannot be undone. Please check all the information. Any amount to be refunded will be sent to your chosen payment method.",
        color = Color.Black,
        fontSize = 10.sp,
        modifier = modifier
            .padding(horizontal = 10.dp, vertical = 10.dp)
            .fillMaxWidth()
    )
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AppRatingBar(
    bookingHistoryViewModel: BookingHistoryViewModel,
    modifier: Modifier = Modifier,
    initialRating: Int
) {
    var ratingState by remember {
        mutableStateOf(initialRating)
    }

  // var ratingState = bookingHistoryViewModel.userInputReview.collectAsState().value.rating

    var selected by remember {
        mutableStateOf(false)
    }
    val size by animateDpAsState(
        targetValue = if (selected) 12.dp else 12.dp,
        spring(Spring.DampingRatioMediumBouncy)
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 1..5) {
            Icon(
                painter = painterResource(id = R.drawable.star_rating),
                contentDescription = "star",
                modifier = modifier
                    .padding(2.dp)
                    .width(size)
                    .height(size)

                    .pointerInteropFilter {
                        when (it.action) {
                            MotionEvent.ACTION_DOWN -> {
                                selected = true
                                ratingState = i
                            }

                            MotionEvent.ACTION_UP -> {
                                selected = false
                                bookingHistoryViewModel.setRating(ratingState)
                            }


                        }
                        true
                    },
                tint = if (i <= ratingState) Orange else Color(0xFFA2ADB1)
            )
        }
    }
}


@Preview
@Composable
private fun BookingHistoryScreenPreview(){
    // BookingHistoryScreen()

}





