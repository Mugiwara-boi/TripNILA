package com.example.tripnila.screens

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tripnila.R
import com.example.tripnila.components.Orange
import com.example.tripnila.data.PropertyDescription

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddListingScreen7(){

    var selectedPropertyIndices by remember { mutableStateOf(listOf<Int>()) }
    val offers = listOf(
        PropertyDescription(
            icon = R.drawable.wifi,
            label = "Wifi"
        ),
        PropertyDescription(
            icon = R.drawable.tv,
            label = "TV"
        ),
        PropertyDescription(
            icon = R.drawable.kitchen,
            label = "Kitchen"
        ),
        PropertyDescription(
            icon = R.drawable.washing_machine,
            label = "Washing machine"
        ),
        PropertyDescription(
            icon = R.drawable.workspace,
            label = "Dedicated workspace"
        )
    )

    val amenities = listOf(
        PropertyDescription(
            icon = R.drawable.bigger_pool,
            label = "Pool"
        ),
        PropertyDescription(
            icon = R.drawable.gym,
            label = "Gym equipment"
        ),
        PropertyDescription(
            icon = R.drawable.hot_tub,
            label = "Hot tub"
        ),
    )

    val views = listOf(
        PropertyDescription(
            icon = R.drawable.condominium,
            label = "City view"
        ),
    )

    val numOffersRows = (offers.size + 2) / 2
    val numAmenitiesRows = (amenities.size + 2) / 2
    val numViewsRows = (views.size + 2) / 2
    var lastIndex: Int

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
                 //   contentPadding = PaddingValues(vertical = 25.dp),
                    verticalArrangement = Arrangement.spacedBy(15.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {

                    item {
                        Text(
                            text = "Tell us what your staycation has to offer",
                            color = Color(0xff333333),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier
                               // .width(300.dp)
                                .fillMaxWidth()
                                .padding(bottom = 10.dp)

                        )
                    }

                    items(numOffersRows) { row ->
                        lastIndex = 0
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(15.dp)
                        ) {
                            val startIndex = row * 2
                            val endIndex = minOf(startIndex + 2, offers.size)

                            for (i in startIndex until endIndex) {
                                val offer = offers[i]
                                PropertyTypeCard(
                                    icon = offer.icon,
                                    label = offer.label,
                                    selected = selectedPropertyIndices.contains(i),
                                    onSelectedChange = { isSelected ->
                                        if (isSelected) {
                                            selectedPropertyIndices =
                                                (selectedPropertyIndices + i).distinct()
                                        } else {
                                            selectedPropertyIndices = selectedPropertyIndices - i
                                        }
                                    }
                                )
                                lastIndex = i
                            }
                            if (lastIndex == offers.size - 1) {
                                AddMoreAmenity()
                            }
                        }
                    }

                    item {
                        Text(
                            text = "Do you have any standout amenities?",
                            color = Color(0xff333333),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier
                                // .width(300.dp)
                                .fillMaxWidth()
                                .padding(vertical = 10.dp)

                        )
                    }

                    items(numAmenitiesRows) { row ->
                        lastIndex = 0
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(15.dp)
                        ) {
                            val startIndex = row * 2
                            val endIndex = minOf(startIndex + 2, amenities.size)

                            for (i in startIndex until endIndex) {
                                val amenity = amenities[i]
                                PropertyTypeCard(
                                    icon = amenity.icon,
                                    label = amenity.label,
                                    selected = selectedPropertyIndices.contains(i),
                                    onSelectedChange = { isSelected ->
                                        if (isSelected) {
                                            selectedPropertyIndices =
                                                (selectedPropertyIndices + i).distinct()
                                        } else {
                                            selectedPropertyIndices = selectedPropertyIndices - i
                                        }
                                    }
                                )
                                lastIndex = i
                            }
                            if (lastIndex == amenities.size - 1) {
                                AddMoreAmenity()
                            }
                        }
                    }
                    item {
                        Text(
                            text = "Do you have any scenic view?",
                            color = Color(0xff333333),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier
                                // .width(300.dp)
                                .fillMaxWidth()
                                .padding(vertical = 10.dp)

                        )
                    }
                    items(numViewsRows) { row ->
                        lastIndex = 0
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(15.dp)
                        ) {
                            val startIndex = row * 2
                            val endIndex = minOf(startIndex + 2, views.size)

                            for (i in startIndex until endIndex) {
                                val view = views[i]
                                PropertyTypeCard(
                                    icon = view.icon,
                                    label = view.label,
                                    selected = selectedPropertyIndices.contains(i),
                                    onSelectedChange = { isSelected ->
                                        if (isSelected) {
                                            selectedPropertyIndices =
                                                (selectedPropertyIndices + i).distinct()
                                        } else {
                                            selectedPropertyIndices = selectedPropertyIndices - i
                                        }
                                    }
                                )
                                lastIndex = i
                            }
                            if (lastIndex == views.size - 1) {
                                AddMoreAmenity()
                            }
                        }
                    }


                }
                Spacer(modifier = Modifier.weight(1f))
                AddListingStepIndicator(modifier = Modifier, currentPage = 0, pageCount = 4)
            }
        }
    }
}

@Composable
fun AddMoreAmenity(modifier: Modifier = Modifier){

    val stroke = Stroke(
        width = 8f,
        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
    )

    Box(
        modifier = modifier
            .width(165.dp)
            .height(height = 86.dp)
            .clip(shape = RoundedCornerShape(10.dp))
            .drawBehind {
                drawRoundRect(
                    color = Orange,
                    style = stroke,
                    cornerRadius = CornerRadius(10.dp.toPx())
                )
            }
            .clickable { /*TODO*/ }
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
                modifier = Modifier.size(18.dp).padding(end = 3.dp)
            )
            Text(
                text = "Add more",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = Orange
            )

        }
    }
}

@Preview
@Composable
private fun AddListingPreview(){
    AddMoreAmenity()
}

@Preview
@Composable
private fun AddListingScreen7Preview(){
    AddListingScreen7()
}