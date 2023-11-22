package com.example.tripnila.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tripnila.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBusinessScreen8(){

    val photos = listOf(
        R.drawable.business1,
    )

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
                        AddListingCoverPhoto(R.drawable.business1)
                    }

                    items(numRows) { row ->
                        lastIndex = 0
                        Row(
                            modifier = Modifier.fillMaxWidth(),
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
                                        contentDescription = "Menu photo",
                                        contentScale = ContentScale.FillWidth
                                    )
                                }
                                lastIndex = i
                            }
                            if (lastIndex == photos.size - 1 && !(photos.size % 2 == 0)) {
                             //   AddMorePhoto()
                            }
                        }
                    }

                    if (photos.size % 2 == 0) {
                        item {
                   //         AddMorePhoto()
                        }
                    }

                    item {
                        Text(
                            text = "Any additional information?",
                            color = Color(0xff333333),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(top = 10.dp, bottom = 5.dp)
                        )
                        /*TODO*/
//                    LongBasicTextFieldWithCharacterLimit(
//                        inputText = ,
//                        maxCharacterLimit = 500
//                    )
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
                AddListingStepIndicator(modifier = Modifier, currentPage = 1, pageCount = 4)
            }
        }
    }
}

@Preview
@Composable
private fun AddBusiness8Preview(){
}

@Preview
@Composable
private fun AddBusinessScreen8Preview(){
    AddBusinessScreen8()
}