package com.example.tripnila.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tripnila.model.AddBusinessViewModel
import com.example.tripnila.model.AddListingViewModel
import com.example.tripnila.model.HostTourViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddListingScreen15(
    listingType: String = "Staycation",
    addListingViewModel: AddListingViewModel? = null,
    hostTourViewModel: HostTourViewModel? = null,
    addBusinessViewModel: AddBusinessViewModel? = null,
    onNavToDashboard: (String) -> Unit,
    onNavToBack: () -> Unit,
){

    val staycation = when (listingType) {
        "Staycation" -> addListingViewModel?.staycation?.collectAsState()?.value
        "Tour" -> hostTourViewModel?.tour?.collectAsState()?.value
        "Business" -> addBusinessViewModel?.business?.collectAsState()?.value
        else -> throw IllegalStateException("Unknown")
    }


    val header = if (listingType == "Staycation") {
        "Your listing has been published"
    } else if (listingType == "Business"){
        "Your business has been published"
    }
    else {
        "Your tour has been published"
    }

    val description = if (listingType == "Staycation") {
        "You can still edit information about your staycation on the staycation manager. Good luck!"
    } else if (listingType == "Business"){
        "You can still edit information about your business on the business manager. Good luck!"
    }
    else {
        "You can still edit information about your tour on the tour manager. Good luck!"
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
    ){
        Scaffold(
            bottomBar = {
                AddListingBottomBookingBar(
                    leftButtonText = "Back",
                    rightButtonText = "Confirm",
                    onNext = {
                       Log.d("$listingType", "$staycation")

                        when (listingType) {
                            "Staycation" -> addListingViewModel?.staycation?.value?.host?.hostId?.substring(5)?.let { touristId -> onNavToDashboard(touristId) }
                            "Tour" -> hostTourViewModel?.tour?.value?.host?.hostId?.substring(5)?.let { touristId -> onNavToDashboard(touristId) }
                            "Business" -> addBusinessViewModel?.business?.value?.host?.hostId?.substring(5)?.let { touristId -> onNavToDashboard(touristId) }
                        }

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
                Text(
                    text = "",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = header,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = description,
                    fontSize = 12.sp,
//                    modifier = Modifier
//                        .width(309.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
                AddListingStepIndicator(modifier = Modifier, currentPage = 3, pageCount = 4)
            }
        }

    }
}

@Preview
@Composable
private fun AddListingScreen15Preview() {
    val addListingViewModel = viewModel(modelClass = AddListingViewModel::class.java)
//
//    AddListingScreen15(
//        addListingViewModel = addListingViewModel,
//        onNavToBack = {},
//        onNavToNext = {}
//    )
}