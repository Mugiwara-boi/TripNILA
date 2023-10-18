package com.example.tripnila.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tripnila.R
import com.example.tripnila.data.PropertyDescription

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddListingScreen5(){

    var selectedPropertyIndex by remember { mutableStateOf(-1) }
    val types = listOf(
        PropertyDescription(
            icon = R.drawable.house,
            label = "An entire place"
        ),
        PropertyDescription(
            icon = R.drawable.room,
            label = "A room"
        )
    )

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
                Text(
                    text = "Give some basics about your space",
                    color = Color(0xff333333),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .width(250.dp)
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(15.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 25.dp)
                ) {
                    DetailsCounter(label = "Guests")
                    DetailsCounter(label = "Bedrooms")
                    DetailsCounter(label = "Beds")
                    DetailsCounter(label = "Bathrooms")
                }

                Spacer(modifier = Modifier.weight(1f))
                AddListingStepIndicator(modifier = Modifier, currentPage = 0, pageCount = 4)

            }
        }
    }
}

@Composable
fun DetailsCounter(label: String, modifier: Modifier = Modifier) {
    var count by remember { mutableStateOf(0) }

    Row(
        modifier = modifier
            .background(Color.White)
            .fillMaxWidth()
            .padding(top = 5.dp, bottom = 20.dp)

    ) {
        Text(
            text = label,
            color = Color(0xff333333),
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.weight(1f))
        IconButton(
            onClick = {
                if (count > 0) {
                    count--
                }
            },
            enabled = count > 0,
            modifier = Modifier.size(17.dp)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.subtract_circle),
                contentDescription = "Subtract",
                tint = if (count > 0) Color(0xFF999999) else Color(0xFFDEDEDE)
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
            // if (count < maxCount) {
                count++
            // }
            },
            modifier = Modifier.size(17.dp)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.add_circle),
                contentDescription = "Add",
                tint = Color(0xFF999999)
            )
        }
    }
    Divider(color = Color(0xFF999999))
}

@Preview
@Composable
private fun AddListing5Preview(){


    DetailsCounter("Guests")
}

@Preview
@Composable
private fun AddListingScreen5Preview(){
    AddListingScreen5()
}