package com.example.tripnila.screens

import android.view.MotionEvent
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tripnila.R
import com.example.tripnila.components.AppBottomNavigationBar
import com.example.tripnila.components.AppConfirmAndPayDivider
import com.example.tripnila.components.AppDropDownFilter
import com.example.tripnila.components.AppFilledButton
import com.example.tripnila.components.Orange
import com.example.tripnila.components.Tag
import com.example.tripnila.data.BookingHistory
import kotlinx.coroutines.launch

@Composable
fun BookingHistoryScreen(){

    var selectedItemIndex by rememberSaveable { mutableStateOf(0) }
    val bookingHistories = listOf(
        BookingHistory(
            ownerImage = R.drawable.joshua,
            bookedRental = "Modern House with 2 bedrooms",
            date = "24 August 2023",
            rentalImage = R.drawable.staycation1,
            rentalLocation = "Rainforest, Pasig City",
            bookedDates = "6-10 Sep",
            guestsNo = 4,
            totalAmount = 12600.00,
            rentalStatus = "Pending"
        ),
        BookingHistory(
            ownerImage = R.drawable.joshua,
            bookedRental = "Modern House with 2 bedrooms",
            date = "24 August 2023",
            rentalImage = R.drawable.staycation1,
            rentalLocation = "Rainforest, Pasig City",
            bookedDates = "6-10 Sep",
            guestsNo = 4,
            totalAmount = 12600.00,
            rentalStatus = "Completed"
        ),

    )

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

                    )
                }

            },
            bottomBar = {
                AppBottomNavigationBar(
                    selectedItemIndex = selectedItemIndex,
                    onItemSelected = { newIndex ->
                        selectedItemIndex = newIndex
                    }
                )
            }
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                items(bookingHistories) { bookingHistory ->
                    
                    BookingHistoryCard(
                        bookingHistory = bookingHistory,
                        modifier = Modifier.padding(top = 15.dp)
                    )
                }
            }

        }
    }
}

@Composable
fun BookingHistoryCard(
    bookingHistory: BookingHistory,
    modifier: Modifier = Modifier
){

    var isOpen by remember { mutableStateOf(false) }
    var openReviewCard by remember {
        mutableStateOf(false)
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
                    Image(
                        painter = painterResource(id = bookingHistory.ownerImage),
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
                    onCancelClick = {
                        openReviewCard = false
                    },
                    onConfirmClick = {
                        openReviewCard = false
                    }
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
                            Image(
                                painter = painterResource(id = bookingHistory.rentalImage),
                                contentDescription = "Image",
                                contentScale = ContentScale.FillBounds
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
                                    onClick = { /*TODO*/ },
                                    modifier = Modifier.width(95.dp)
                                )
                                Spacer(modifier = Modifier.weight(1f))
                                BookingFilledButton(
                                    buttonText = "Cancel",
                                    onClick = {
                                        isOpen = true
                                    },
                                    modifier = Modifier.width(95.dp)
                                )
                            }
                            else{
                                Spacer(modifier = Modifier.weight(1f))
                                BookingOutlinedButton(
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

        }
    }

    AppBottomSheet(
        openBottomSheet = isOpen,
        onOpenBottomSheetChange = {
            isOpen = it
        }

    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingHistoryTopBar(modifier: Modifier = Modifier){

    val filter = listOf("Relevance", "Recent", "Top rated")

    TopAppBar(
        navigationIcon = {
            IconButton(onClick = { /*TODO*/ }) {
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBottomSheet(openBottomSheet: Boolean, onOpenBottomSheetChange: (Boolean) -> Unit) {

    val scope = rememberCoroutineScope()
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    if (openBottomSheet) {
        ModalBottomSheet(
            modifier = Modifier.fillMaxHeight(0.75f),
            sheetState = bottomSheetState,
            onDismissRequest = {
                onOpenBottomSheetChange(false)
            },
            dragHandle = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    //horizontalAlignment = Alignment.End
                ) {
                   IconButton(
                       onClick = {
                           scope.launch { bottomSheetState.hide() }.invokeOnCompletion {
                               if (!bottomSheetState.isVisible) {
                                   onOpenBottomSheetChange(false)
                               }
                           }
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
            BottomSheetContent(
                onHideButtonClick = {
                    scope.launch { bottomSheetState.hide() }.invokeOnCompletion {
                        if (!bottomSheetState.isVisible) {
                            onOpenBottomSheetChange(false)
                        }
                    }
                }
            )
        }
    }
}
@Composable
fun BottomSheetContent(
    onHideButtonClick: () -> Unit
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
            itinerary = "Modern house with 2 bedrooms",
            price = 2500.00,
            unit = "night"
        )
        YourTripDivider()
        AppPaymentDivider(
            bookingFee = 2500.00,
            bookingDuration = 5,
            maintenanceFee = 250.00,
            tripnilaFee = 625.00

        )
        CancellationAgreementText()
        Spacer(modifier = Modifier.height(15.dp))
        BookingFilledButton(
            buttonText = "Cancel booking",
            onClick = onHideButtonClick,
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .fillMaxWidth()
        )
    }
}

@Composable
fun WriteReviewComposable(
    onCancelClick: () -> Unit,
    onConfirmClick: () -> Unit
){

    val localFocusManager = LocalFocusManager.current
    var comment by remember { mutableStateOf("") }
    val maxCharacterLimit = 100
    val remainingCharacters = maxCharacterLimit - comment.length

    Text(
        text = "Write a review",
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp
    )
    AppRatingBar(rating = 0)
    Spacer(modifier = Modifier.height(8.dp))
    BasicTextField(
        value = comment,
        onValueChange = {
            if (it.length <= maxCharacterLimit) {
                comment = it
            }
        },
        textStyle = TextStyle(fontSize = 10.sp, color = Color(0xFF6B6B6B)),
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
                    if (comment.isEmpty()) {
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
    AppChoosePhoto(
        modifier = Modifier
            .padding(top = 8.dp, bottom = 12.dp)
            .width(125.dp)
            .height(80.dp)
    )
    Row {
        BookingOutlinedButton(
            buttonText = "Cancel",
            onClick = {
                onCancelClick()
            },
            modifier = Modifier.width(90.dp)

        )
        Spacer(modifier = Modifier.weight(1f))
        BookingFilledButton(
            buttonText = "Confirm",
            onClick = {
                onConfirmClick()
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
    modifier: Modifier = Modifier,
    rating: Int
) {
    var ratingState by remember {
        mutableStateOf(rating)
    }

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
    BookingHistoryScreen()

}





