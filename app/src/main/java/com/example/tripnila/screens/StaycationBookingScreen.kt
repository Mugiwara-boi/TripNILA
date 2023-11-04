package com.example.tripnila.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tripnila.R
import com.example.tripnila.common.AppConfirmAndPayDivider
import com.example.tripnila.common.AppYourTripRow
import com.example.tripnila.common.Orange

@Composable
fun StaycationBookingScreen(){
    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = R.drawable.staycation1),
                contentDescription = "Staycation Image",
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
        ){
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        horizontal = 25.dp,
                        vertical = 20.dp // 12
                    )
                    .background(Color.White)
            ) {
                Text(
                    text = "Confirm and pay",
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
                PaymentAgreementText()
                Spacer(modifier = Modifier.padding(vertical = 15.dp))
                BookingFilledButton(
                    buttonText = "Confirm and pay",
                    onClick = {},
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .fillMaxWidth()
                )
            }
        }


    }
}


@Composable
fun YourTripDivider(modifier: Modifier = Modifier) {
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
        AppYourTripRow(rowLabel = "Dates", rowText = "6-10 Sep")
        AppYourTripRow(rowLabel = "Guests", rowText = "4 guest")

    }
    Divider(
        color = Color(0xFF999999),
        modifier = Modifier.padding(top = 5.dp) // 5.dp
    )
}

@Composable
fun AppPaymentDivider(
    bookingFee: Double,
    bookingDuration: Int,
    maintenanceFee: Double? = null,
    tripnilaFee: Double,
    forCancelBooking: Boolean = false,
    modifier: Modifier = Modifier
) {

    val productBookingFee = bookingFee * bookingDuration
    val totalFee = productBookingFee + (maintenanceFee ?: 0.0) + tripnilaFee

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(all = 10.dp)
    ){
        Text(
            text = "Payment",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .padding(bottom = 5.dp)
        )
        PaymentRow(
            feeLabel = "₱ ${"%.2f".format(bookingFee)} x $bookingDuration nights",
            feePrice = productBookingFee
        )

        if (maintenanceFee != null) {
            PaymentRow(
                feeLabel = "Maintenance fee",
                feePrice = maintenanceFee
            )
        }

        PaymentRow(
            feeLabel = "Tripnila service fee",
            feePrice = tripnilaFee
        )
        Divider(
            color = Color(0xFFDEDEDE),
            modifier = Modifier.padding(vertical = 3.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 3.dp)
        ) {
            Text(
                text = if (forCancelBooking) "Total paid" else "Total",
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .weight(1f)
            )
            Text(
                text = "₱ ${"%.2f".format(totalFee)}",
                fontWeight = FontWeight.SemiBold
            )
        }
        if (forCancelBooking) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()

            ) {
                Text(
                    text = "Amount refunded",
                    fontWeight = FontWeight.SemiBold,
                    color = Orange,
                    modifier = Modifier
                        .weight(1f)
                )
                Text(
                    text = "₱ ${"%.2f".format(totalFee * 0.80)}",
                    fontWeight = FontWeight.SemiBold,
                    color = Orange
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = "4 days before check in",
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.sp,
                    color = Color(0xFF999999),
                    modifier = Modifier
                        .weight(1f)
                )
                Text(
                    text = "80%",
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.sp,
                    color = Color(0xFF999999),
                )
            }
        }
        else {
            Text(
                text = "Payment method",
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .padding(vertical = 4.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                PaymentMethodCard(logoId = R.drawable.paypal)
                PaymentMethodCard(logoId = R.drawable.gcash)
                PaymentMethodCard(
                    logoId = R.drawable.paymaya,
                    modifier = Modifier
                        //                    .fillMaxHeight()
                        .height(40.dp)
                        .width(80.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
                PaymentOutlinedButton(
                    buttonText = "Add",
                    cornerSize = 20.dp,
                    modifier = Modifier
                        .padding(top = 5.dp)
                        .height(30.dp)
                )
            }
        }
    }
    Divider(
        color = Color(0xFF999999),
        modifier = Modifier.padding(top = 5.dp) // 10.dp
    )
}


@Composable
fun PaymentRow(feeLabel: String, feePrice: Double, modifier: Modifier = Modifier){
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 3.dp)
    ) {
        Text(
            text = feeLabel,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF999999),
            modifier = Modifier
                .weight(1f)
        )
        Text(
            text = "₱ ${String.format("%.2f", feePrice)}",
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF999999)
        )
    }
}

@Composable
fun PaymentMethodCard(logoId: Int, modifier: Modifier = Modifier) {

    var isClicked by remember { mutableStateOf(false) }
    val borderColor = if (isClicked) Orange else Color(0xFFF3F3F3)

    val interactionSource = remember { MutableInteractionSource() }
    val clickableModifier = Modifier.clickable(
        interactionSource = interactionSource,
        indication = null
    ) {
        isClicked = !isClicked
    }

    ElevatedCard(
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 10.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF3F3F3)
        ),
        modifier = modifier
            .height(40.dp)
            .width(80.dp)
            .padding(vertical = 5.dp, horizontal = 5.dp)
            .border(1.dp, borderColor, RoundedCornerShape(10.dp))
            .then(clickableModifier)
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = logoId),
                contentDescription = "",
                contentScale = ContentScale.FillWidth,
                modifier = modifier
            )
        }
    }
}

@Composable
fun PaymentOutlinedButton(buttonText: String, modifier: Modifier = Modifier, cornerSize: Dp = 10.dp){
    OutlinedButton(
        shape = RoundedCornerShape(cornerSize),
        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Orange),
        onClick = { /*TODO*/ },
        modifier = modifier
    ) {
        Text(
            text = buttonText,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = Orange
        )
    }
}

@Composable
fun PaymentAgreementText(modifier: Modifier = Modifier) {
    Text(
        text = buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    color = Color.Black,
                    fontSize = 10.sp
                )
            ) {
                append("By selecting the button below, I agree to the ")
            }
            withStyle(
                style = SpanStyle(
                    color = Color.Black,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium,
                    textDecoration = TextDecoration.Underline
                )
            ) {
                append("Host’s House Rules")
            }
            withStyle(
                style = SpanStyle(
                    color = Color.Black,
                    fontSize = 10.sp
                )
            ) {append(", ")
            }
            withStyle(
                style = SpanStyle(
                    fontSize = 10.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Medium,
                    textDecoration = TextDecoration.Underline
                )
            ) {
                append("Ground rules for guests")
            }
            withStyle(
                style = SpanStyle(
                    color = Color.Black,
                    fontSize = 10.sp
                )
            ) {
                append(", ")
            }
            withStyle(
                style = SpanStyle(
                    color = Color.Black,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium,
                    textDecoration = TextDecoration.Underline
                )
            ) {
                append("Tripnila’s Rebooking and Refund Policy")
            }
            withStyle(
                style = SpanStyle(
                    color = Color.Black,
                    fontSize = 10.sp
                )
            ) {append(" and Tripnila can charge my payment method if I’m responsible for any damage on the property")
            }
        },
        modifier = modifier
            .padding(
                horizontal = 10.dp,
                vertical = 10.dp // 12
            )
            .fillMaxWidth()

    )
}

@Preview
@Composable
fun BookingScreenPreview(){
    StaycationBookingScreen()
}


