package com.example.tripnila.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tripnila.components.Orange

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddListingScreen11(){

    var price by remember {
        mutableIntStateOf(1250)
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
    ){
        Scaffold(
            bottomBar = {
                AddListingBottomBookingBar()
            },
            topBar = {
                TopAppBar(
                    title = {
                        SaveAndExitButton(
                            onClick = { /*TODO*/ }
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.White
                    )
                )
            }
        ){
            Column(
                modifier = Modifier
                    .background(Color.White)
                    .fillMaxWidth()
                    .padding(horizontal = 25.dp, vertical = 20.dp)
                    .padding(it)
            ) {

                LazyColumn(
                    //contentPadding = PaddingValues(vertical = 25.dp),
                    verticalArrangement = Arrangement.spacedBy(15.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    item {
                        Text(
                            text = "Set your price",
                            color = Color(0xff333333),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier
                                // .width(300.dp)
                                .fillMaxWidth()
                                .padding(bottom = 10.dp)
                        )
                    }
                    item {
                        Text(
                            text = buildAnnotatedString {
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, fontSize = 36.sp)) {
                                    append("₱ ${String.format("%,d", price)}")
                                }
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Medium, fontSize = 12.sp)) {
                                    append(" per night")
                                }
                            },
                            textAlign = TextAlign.Center,
                            color = Color(0xff333333),
                            modifier = Modifier
                                // .width(300.dp)
                                .fillMaxWidth()
                                .padding(bottom = 10.dp)
                        )
                    }
                    item {
                        PriceDistributionCard(price.toDouble())
                    }
                    item {
                        ProfitCard(price.toDouble())
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
                AddListingStepIndicator(modifier = Modifier, currentPage = 2, pageCount = 4)
            }
        }
    }
}

@Composable
fun PriceDistributionCard(basePrice: Double, modifier: Modifier = Modifier) {

    var maintenanceFee by remember {
        mutableStateOf((basePrice * 0.10))
    }
    var serviceFee by remember {
        mutableStateOf((basePrice * 0.05))
    }
    var guestPrice by remember {
        mutableStateOf(basePrice + serviceFee)
    }

    OutlinedCard(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.dp, Color(0xff999999)),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 3.dp)
            ) {
                Text(
                    text = "Base price",
                    color = Color(0xff999999),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "₱ ${String.format("%,.2f", basePrice)}",
                    color = Color(0xff999999),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 3.dp)
            ) {
                Text(
                    text = "Maintenance fee",
                    color = Color(0xff999999),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "₱ ${String.format("%,.2f", maintenanceFee)}",
                    color = Color(0xff999999),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 3.dp)
            ) {
                Text(
                    text = "TripNILA service fee",
                    color = Color(0xff999999),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "₱ ${String.format("%,.2f", serviceFee)}",
                    color = Color(0xff999999),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                )
            }
            Divider(
                color = Color(0xFFDEDEDE),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 3.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Guest Price",
                    color = Color(0xff333333),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "₱ ${String.format("%,.2f", guestPrice)}",
                    color = Color(0xff333333),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                )
            }
        }
    }
}

@Composable
fun ProfitCard(basePrice: Double, modifier: Modifier = Modifier){

    var serviceFee by remember {
        mutableStateOf((basePrice * 0.05))
    }
    var profit by remember {
        mutableStateOf(basePrice + serviceFee)
    }

    OutlinedCard(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.dp, Color(0xff999999)),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ){
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp)
        ) {
            Text(
                text = "You earn",
                color = Color(0xff333333),
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium,
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "₱ ${String.format("%,.2f", profit)}",
                color = Color(0xff333333),
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium,
            )
        }
    }
}

@Preview
@Composable
private fun AddListingPreview(){

    ProfitCard((1250).toDouble())

}

@Preview
@Composable
private fun AddListingScreenPreview(){
    AddListingScreen11()
}
