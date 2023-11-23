package com.example.tripnila.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tripnila.model.AddListingViewModel
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddListingScreen11(
    listingType: String = "Staycation",
    addListingViewModel: AddListingViewModel? = null,
    onNavToNext: (String) -> Unit,
    onNavToBack: () -> Unit,
){


    val formatter = NumberFormat.getNumberInstance(Locale.US)
    var price = remember {
        mutableStateOf(addListingViewModel?.staycation?.value?.staycationPrice?.toInt())
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
    ){
        Scaffold(
            bottomBar = {
                AddListingBottomBookingBar(
                    leftButtonText = "Back",
                    onNext = {
                        onNavToNext(listingType)
                    },
                    onCancel = {
                        onNavToBack()
                    },
                    enableRightButton = addListingViewModel?.staycation?.collectAsState()?.value?.staycationPrice != 0.0
                )
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

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .padding(bottom = 10.dp)
                                .fillMaxWidth()
                        ) {
                            Box(
                                modifier = Modifier.width(290.dp)
                            ) {
                                BasicTextField(
                                    value = formatter.format(price.value),
                                    onValueChange = {
                                        price.value = it.replace(",", "").toIntOrNull() ?: 0
                                        addListingViewModel?.setStaycationPrice(price?.value!!.toDouble())
                                    },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    textStyle = TextStyle(fontWeight = FontWeight.Bold, fontSize = 36.sp),
                                    singleLine = true,
                                    decorationBox = { innerTextField ->
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.Center
                                        ) {
                                            Text("₱ ", style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 36.sp))
                                            innerTextField()
                                        }
                                    }
                                )
                                Text(
                                    text = " per night",
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 12.sp,
                                    modifier = Modifier.align(Alignment.CenterEnd)
                                )
                            }
                        }




//                        Row(
//                            horizontalArrangement = Arrangement.Center,
//                            modifier = Modifier
//                                .padding(bottom = 10.dp)
//                                .fillMaxWidth()
//                        ) {
//                            BasicTextField(
//                                value = price.value.toString(),
//                                onValueChange = {
//                                    price.value = it.toIntOrNull() ?: 0
//                                },
//                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
//                                textStyle = TextStyle(fontWeight = FontWeight.Bold, fontSize = 36.sp),
//                                singleLine = true,
//                                modifier = Modifier.width(150.dp),
//                                decorationBox = { innerTextField ->
//                                    Row(
//                                    ) {
//                                        Text("₱ ", style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 36.sp))
//                                        innerTextField()
//                                    }
//                                }
//                            )
//                            Text(
//                                text = " per night",
//                                fontWeight = FontWeight.Medium,
//                                fontSize = 12.sp,
//                                modifier = Modifier.wrapContentHeight(Alignment.CenterVertically)
//                            )
//                        }


//                        Text(
//                            text = buildAnnotatedString {
//                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, fontSize = 36.sp)) {
//                                    append("₱ ${String.format("%,d", price)}")
//                                }
//                                withStyle(style = SpanStyle(fontWeight = FontWeight.Medium, fontSize = 12.sp)) {
//                                    append(" per night")
//                                }
//                            },
//                            textAlign = TextAlign.Center,
//                            color = Color(0xff333333),
//                            modifier = Modifier
//                                // .width(300.dp)
//                                .fillMaxWidth()
//                                .padding(bottom = 10.dp)
//                        )
                    }
                    item {
                        price.value?.toDouble()
                            ?.let { value -> PriceDistributionCard(basePrice = value) }
                    }
                    item {
                        price.value?.toDouble()?.let { value -> ProfitCard(basePrice = value) }
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
                AddListingStepIndicator(modifier = Modifier, currentPage = 2, pageCount = 4)
            }
        }
    }
}

//    var maintenanceFee by remember {
//        mutableStateOf((basePrice * 0.10))
//    }
//    var serviceFee by remember {
//        mutableStateOf((basePrice * 0.05))
//    }
//    var guestPrice by remember {
//        mutableStateOf(basePrice + serviceFee)
//    }

@Composable
fun PriceDistributionCard(basePrice: Double, modifier: Modifier = Modifier) {

    val maintenanceFee = basePrice * 0.10
    val serviceFee = basePrice * 0.05
    val guestPrice = basePrice + serviceFee + maintenanceFee

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

   // val serviceFee = basePrice * 0.05
    val maintenanceFee = basePrice * 0.10
    var profit = basePrice + maintenanceFee


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
private fun AddListingScreen11Preview(){
    val addListingViewModel = viewModel(modelClass = AddListingViewModel::class.java)

    AddListingScreen11(
        addListingViewModel = addListingViewModel,
        onNavToBack = {},
        onNavToNext = {}
    )
}
