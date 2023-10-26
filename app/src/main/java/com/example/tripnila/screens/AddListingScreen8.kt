package com.example.tripnila.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tripnila.R
import com.example.tripnila.components.Orange

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddListingScreen8(listingType: String = "Staycation"){

    val photos = if (listingType == "Staycation") {
                                listOf(
                                    R.drawable.staycation1,
                                    R.drawable.staycation1,
                                    R.drawable.staycation1,
                                    R.drawable.staycation1,
                                )
                            }
                            else if (listingType == "Business") {
                                listOf(
                                    R.drawable.business1,
                                    R.drawable.business1,
                                    R.drawable.business1,
                                    R.drawable.business1,
                                )
                            }
                            else {
                                listOf(
                                    R.drawable.tour1,
                                    R.drawable.tour1,
                                    R.drawable.tour1,
                                    R.drawable.tour1,
                                )
                            }


    val numRows = (photos.size + 1) / 2
    var lastIndex = 0

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
                            text = "Upload some photos!",
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
                        AddListingCoverPhoto(R.drawable.staycation1)
                    }

                    items(numRows) { row ->
                        lastIndex = 0
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            //horizontalArrangement = Arrangement.spacedBy(15.dp)
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            val startIndex = row * 2
                            val endIndex = minOf(startIndex + 2, photos.size)

                            for (i in startIndex until endIndex) {
                                val photo = photos[i]
                                Box(
                                    modifier = Modifier
                                        .width(165.dp)
                                        .height(110.dp)
                                ) {
                                    Image(
                                        painter = painterResource(id = photo),
                                        contentDescription = "$listingType photo",
                                        contentScale = ContentScale.FillWidth
                                    )
                                }
                                lastIndex = i
                            }
                            if (lastIndex == photos.size - 1 && !(photos.size % 2 == 0)) {
                                AddMorePhoto()
                            }
                        }
                    }

                    if (photos.size % 2 == 0) {
                        item {
                            AddMorePhoto()
                        }
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
                AddListingStepIndicator(modifier = Modifier, currentPage = 1, pageCount = 4)
            }
        }
    }
}

@Composable
fun AddListingCoverPhoto(coverPhoto: Int, modifier: Modifier = Modifier){
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(170.dp)
    ){
        Image(
            painter = painterResource(id = coverPhoto),
            contentDescription = "Cover photo",
            contentScale = ContentScale.FillWidth
        )
        Button(
            onClick = { /*TODO*/ },
            enabled = false,
            shape = RoundedCornerShape(3.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Orange,
                disabledContainerColor = Orange
            ),
            contentPadding = PaddingValues(0.dp),
            modifier = Modifier
                .padding(5.dp)
                .height(16.dp)
                .width(54.dp)
        ) {
            Text(
                text = "Cover photo",
                fontSize = 8.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White,
                modifier = Modifier.wrapContentSize(Alignment.Center)
            )
        }
    }
}

@Composable
fun AddMorePhoto(modifier: Modifier = Modifier){

    val stroke = Stroke(
        width = 2f,
        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
    )

    Box(
        modifier = modifier
            .width(165.dp)
            .height(110.dp)
            .drawBehind {
                drawRoundRect(color = Orange, style = stroke)
            }
            .clickable { /*TODO*/ },

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
                modifier = Modifier
                    .size(18.dp)
                    .padding(end = 3.dp)
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
private fun AddListing8Preview(){
    AddMorePhoto()
}

@Preview
@Composable
private fun AddListingScreen8Preview(){
    AddListingScreen8()
}