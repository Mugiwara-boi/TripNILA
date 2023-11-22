package com.example.tripnila.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tripnila.R
import com.example.tripnila.data.PropertyDescription
import com.example.tripnila.model.AddListingViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddListingScreen4(
    listingType: String = "Staycation",
    addListingViewModel: AddListingViewModel? = null,
    onNavToNext: (String) -> Unit,
    onNavToBack: () -> Unit,
){

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
                    verticalArrangement = Arrangement.spacedBy(15.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    item {
                        Text(
                            text = "Where is it located?",
                            color = Color(0xff333333),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier
                                .width(267.dp)
                                .padding(bottom = 10.dp)
                        )
                    }


                }
                Spacer(modifier = Modifier.weight(1f))
                AddListingStepIndicator(modifier = Modifier, currentPage = 0, pageCount = 4)

            }
        }
    }
}
