package com.example.tripnila.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tripnila.R
import com.example.tripnila.common.AppConfirmAndPayDivider
import com.example.tripnila.common.AppYourTripRow

@Composable
fun TourBookingScreen(){
    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = R.drawable.tour1),
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
                    image = R.drawable.tour1,
                    itinerary = "Cubao night tour",
                    price = 3100.00,
                    unit = "person"
                )
                YourTourDivider()
                AppPaymentDivider(
                    bookingFee = 2755.00,
                    bookingDuration = 4,
                    tripnilaFee = 625.00,
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
fun YourTourDivider(modifier: Modifier = Modifier) {
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
        AppYourTripRow(rowLabel = "Date & time", rowText = "11 Sep, Monday 19:00 - 22:00")
        AppYourTripRow(rowLabel = "Guests", rowText = "4 guest")

    }
    Divider(
        color = Color(0xFF999999),
        modifier = Modifier.padding(top = 10.dp) // 5.dp
    )
}


@Preview
@Composable
private fun TourBookingScreenPreview(){
    TourBookingScreen()
}