package com.example.tripnila.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tripnila.common.Orange
import com.example.tripnila.data.Discount
import com.example.tripnila.data.Promotion
import com.example.tripnila.model.AddListingViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddListingScreen12(
    listingType: String = "Staycation",
    addListingViewModel: AddListingViewModel? = null,
    onNavToNext: (String) -> Unit,
    onNavToBack: () -> Unit,
){

    val promotions = listOf(
        Promotion(
            promoId = "kIx5o7g5J5yMYOU8XO1V",
            discount = 0.2,
            promoName = "New listing promotion",
            description = "Offer 20% off for first 3 bookings",
        ),
        Promotion(
            promoId = "bG4mUuwlVooIcUM6dxIS",
            discount = 0.10,
            promoName = "Weekly discount",
            description = "for stays of 7 nights or more",
        ),
        Promotion(
            promoId = "VbMFPAsRK1Wl9RYstMrn",
            discount = 0.15,
            promoName = "Monthly discount",
            description = "for stays of 28 nights or more",
        )
    )

    val selectedPromotions = addListingViewModel?.staycation?.collectAsState()?.value?.promotions

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
                    }
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
                            text = "Add discounts",
                            color = Color(0xff333333),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 10.dp)
                        )
                    }

                    items(promotions) {promotion ->
                        selectedPromotions?.contains(promotion)?.let { isSelected ->
                            DiscountCard(
                                discount = promotion,
                                selected = isSelected,
                                onSelectedChange = {isSelected ->
                                    if (isSelected) {
                                        addListingViewModel.addStaycationPromotion(promotion)
                                    } else {
                                        addListingViewModel.removeStaycationPromotion(promotion)
                                    }

                                }
                            )
                        }
                    }


//                    items(promotions) { promotion ->
//                        val isSelected = addListingViewModel?.isPromotionSelected(promotion)
//                        if (isSelected != null) {
//                            DiscountCard(
//                                discount = promotion,
//                                selected = isSelected,
//                                onSelectedChange = { isSelected ->
//                                    if (isSelected) {
//                                        addListingViewModel.addStaycationPromotion(promotion)
//                                    } else {
//                                        addListingViewModel.removeStaycationPromotion(promotion)
//                                    }
//                                },
//                            )
//                        }
//                    }

                }
                Spacer(modifier = Modifier.weight(1f))
                AddListingStepIndicator(modifier = Modifier, currentPage = 2, pageCount = 4)
            }
        }
    }
}

@Composable
fun DiscountCard(
    discount: Promotion,
    selected: Boolean,
    onSelectedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val borderColor = if (selected) Orange else Color(0xff999999)
    val containerColor = if (selected) Orange.copy(alpha = 0.2f) else Color.White

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(10.dp))
            .border(
                border = BorderStroke(1.dp, borderColor),
                shape = RoundedCornerShape(10.dp)
            )
            .background(containerColor)
            .clickable { onSelectedChange(!selected) }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 25.dp, end = 10.dp, top = 12.dp, bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${discount.discount.times(100).toInt()} %",
                color = Color(0xff333333),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.weight(0.5f))
            Column {
                Text(
                    text = discount.promoName,
                    color = Color(0xff333333),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = discount.description,
                    color = Color(0xff999999),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Checkbox(
                checked = selected,
                onCheckedChange = { onSelectedChange(it) },
                colors = CheckboxDefaults.colors(
                    checkedColor = Orange //Color(0xFF333333)
                )
            )
        }
    }
}

@Preview
@Composable
private fun AddListingPreview(){



}

@Preview
@Composable
private fun AddListingScreen12Preview(){
    val addListingViewModel = viewModel(modelClass = AddListingViewModel::class.java)

    AddListingScreen12(
        addListingViewModel = addListingViewModel,
        onNavToBack = {},
        onNavToNext = {}
    )
}