package com.example.tripnila.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
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
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.example.tripnila.R
import com.example.tripnila.common.AppConfirmAndPayDivider
import com.example.tripnila.common.AppYourTripRow
import com.example.tripnila.data.Host
import com.example.tripnila.data.TourAvailableDates
import com.example.tripnila.model.TourDetailsViewModel
import com.example.tripnila.model.TouristWalletViewModel
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import java.text.NumberFormat
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun TourBookingScreen(
    touristId: String = "",
    tourDetailsViewModel: TourDetailsViewModel,
    touristWalletViewModel: TouristWalletViewModel,
    onBack: () -> Unit
){

    val tour by tourDetailsViewModel.tour.collectAsState()
    val selectedDate by tourDetailsViewModel.selectedDate.collectAsState()
    val selectedPersonCount by tourDetailsViewModel.personCount.collectAsState()
    val bookingResult by tourDetailsViewModel.bookingResult.collectAsState()
    val limit = selectedDate?.remainingSlot
    val tripnilaFee by touristWalletViewModel.tripnilaFee.collectAsState()
    val totalFee by touristWalletViewModel.totalFee.collectAsState()
    val host = tour.host?: Host()
    val hostId = host.hostId
    val hostWalletId = hostId.removePrefix("HOST-")

    val formattedNumber = NumberFormat.getNumberInstance()
    val formattedNumberWithDecimalFormat = NumberFormat.getNumberInstance() as DecimalFormat

    formattedNumberWithDecimalFormat.apply {
        maximumFractionDigits = 2
        minimumFractionDigits = 2
    }

    val bookingFee = tour.tourPrice
//    val productBookingFee = bookingFee * selectedPersonCount
//    val tripNilaFee = productBookingFee * 0.05
//    val totalFeeState = productBookingFee + tripNilaFee

    var openBottomSheet by remember { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState( skipPartiallyExpanded = true)
    val count by tourDetailsViewModel.tempCount.collectAsState()
    val hasNavigationBar = WindowInsets.areNavigationBarsVisible

    val snackbarHostState = remember { SnackbarHostState() }
    var openAlertDialog by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(bookingResult) {
        if (bookingResult != null) {
            snackbarHostState.showSnackbar(bookingResult!!)
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                AsyncImage(
                    model = tour.tourImages.find { it.photoType == "Cover" }?.photoUrl,
                    contentDescription = "Tour Image",
                    contentScale = ContentScale.FillWidth
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .background(color = Color(0xff1a1a1a).copy(alpha = 0.42f))
                )
            }
            Box(
                modifier = Modifier
                    .padding(top = 170.dp) // 160
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .clip(shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                    .background(color = Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it)
                        .padding(
                            horizontal = 25.dp,
                            vertical = 20.dp // 12
                        )
                        .background(Color.White)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Confirm and pay",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "Close",
                            tint = Color(0xFFCECECE),
                            modifier = Modifier
                                .size(30.dp)
                                .offset(x = 5.dp, y = (-5).dp)
                                .clickable {
                                    onBack()
                                }
                        )

                    }

                    AppConfirmAndPayDivider(
                        image = tour.tourImages.find { it.photoType == "Cover" }?.photoUrl
                            ?: "https://upload.wikimedia.org/wikipedia/commons/thumb/3/3f/Placeholder_view_vector.svg/1022px-Placeholder_view_vector.svg.png",
                        itinerary = tour.tourTitle,
                        price = tour.tourPrice,
                        unit = "person"
                    )
                    YourTourDivider(
                        selectedDate = selectedDate,
                        personCount = selectedPersonCount,
                        onEditDate = {
                            onBack()
                        },
                        onEditGuests = {
                            openBottomSheet = true
                        }
                    )

                    AppPaymentDivider(
                        forTourBooking = true,
                        touristId = touristId,
                        bookingFee = bookingFee,
                        bookingDuration = selectedPersonCount,
                        //     tripnilaFee = bookingHistory?.staycationPrice?.times(0.05) ?: 0.0,
                        touristWalletViewModel = touristWalletViewModel
                    )

//                    Column(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(all = 10.dp)
//                    ) {
//                        Text(
//                            text = "Payment",
//                            fontSize = 16.sp,
//                            fontWeight = FontWeight.Medium,
//                            modifier = Modifier
//                                .padding(bottom = 5.dp)
//                        )
//                        PaymentRow(
//                            feeLabel = "₱ ${formattedNumber.format(bookingFee)} x $selectedPersonCount persons",
//                            feePrice = productBookingFee
//                        )
//
//                        PaymentRow(
//                            feeLabel = "Tripnila service fee",
//                            feePrice = tripNilaFee
//                        )
//                        Divider(
//                            color = Color(0xFFDEDEDE),
//                            modifier = Modifier.padding(vertical = 3.dp)
//                        )
//                        Row(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(vertical = 3.dp)
//                        ) {
//                            Text(
//                                text = "Total",
//                                fontWeight = FontWeight.SemiBold,
//                                modifier = Modifier
//                                    .weight(1f)
//                            )
//                            Text(
//                                text = "₱ ${formattedNumberWithDecimalFormat.format(totalFeeState)}",
//                                fontWeight = FontWeight.SemiBold
//                            )
//                        }
//                    }
//                    Divider(
//                        color = Color(0xFF999999),
//                        modifier = Modifier.padding(top = 5.dp) // 10.dp
//                    )
                    PaymentAgreementText()
                    Spacer(modifier = Modifier.padding(vertical = 15.dp))
                    BookingFilledButton(
                        buttonText = "Confirm and pay",
                        onClick = {
                            openAlertDialog = true
                        },
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                            .fillMaxWidth()
                    )
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
                               if (count < limit!!) {
                                    tourDetailsViewModel.incrementCount()
                               }
                            },
                            enabled = count < limit!!, //
                            modifier = Modifier.size(17.dp)
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.add_circle),
                                contentDescription = "Add",
                                tint = if (count < limit) Color(0xff333333) else Color(0xFFDEDEDE),
                               // tint = Color(0xff333333)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.weight(1f))
                    ConfirmCount(
                        tourPrice = tour.tourPrice,
                        personCount = count,
                        enableButton = count != selectedPersonCount,
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

        if (openAlertDialog) {
            Dialog(onDismissRequest = { openAlertDialog = false }) {
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

                        val alertMessage = if (selectedPersonCount == 0 && selectedDate == null) {
                            "Please tour fields."
                        } else if (selectedDate == null) {
                            "Please select tour schedule."
                        } else if (selectedPersonCount == 0) {
                            "Please select tour guests."
                        } else {
                            "Are you sure you want to proceed?"
                        }

                        Text(
                            text = alertMessage,
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
                            if (alertMessage != "Are you sure you want to proceed?") {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                            TextButton(
                                onClick = { openAlertDialog = false },
                                modifier = Modifier.padding(horizontal = 10.dp),
                            ) {
                                Text("Dismiss", color = Color.Black)
                            }
                            if (alertMessage == "Are you sure you want to proceed?") {
                                Spacer(modifier = Modifier.weight(1f))
                                TextButton(
                                    onClick = {
                                        coroutineScope.launch {
                                            openAlertDialog = false
                                            tourDetailsViewModel.addBooking(touristId)
                                            touristWalletViewModel.setBookingPayment(totalFee,touristId)
                                            touristWalletViewModel.setPendingAmount(totalFee = totalFee,hostWalletId = hostWalletId,tripnilaFee = tripnilaFee)
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
}


@Composable
fun YourTourDivider(
    modifier: Modifier = Modifier,
    selectedDate: TourAvailableDates?,
    personCount: Int,
    onEditDate: () -> Unit,
    onEditGuests: () -> Unit
) {

    val formatter = DateTimeFormatter.ofPattern("d MMM, EEEE", Locale.ENGLISH)
    val formattedDate = if (selectedDate != null) "${selectedDate.localDate.format(formatter)} ${selectedDate.startingTime} - ${selectedDate.endingTime}" else "Select date*"
    val formattedGuest = if (personCount > 0) "$personCount guest" else "Select guests*"

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(all = 10.dp)

    ){
        Text(
            text = "Your tour",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
        AppYourTripRow(
            rowLabel = "Date & time",
            rowText = formattedDate, //"11 Sep, Monday 19:00 - 22:00"
            fontColor = if (selectedDate != null) Color(0xFF999999) else Color.Red,
            onClickEdit = {
                onEditDate()
            }
        )
        AppYourTripRow(
            rowLabel = "Guests",
            rowText = formattedGuest,
            fontColor = if (personCount > 0) Color(0xFF999999) else Color.Red,
            onClickEdit = {
                onEditGuests()
            }
        )

    }
    Divider(
        color = Color(0xFF999999),
        modifier = Modifier.padding(top = 10.dp) // 5.dp
    )
}


@Preview
@Composable
private fun TourBookingScreenPreview(){

  //  TourBookingScreen(touristId = "touristId", touristWalletViewModel = TouristWalletViewModel())
}