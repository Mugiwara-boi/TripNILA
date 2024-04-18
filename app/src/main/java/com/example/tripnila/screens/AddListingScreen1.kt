package com.example.tripnila.screens

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tripnila.common.LoadingScreen
import com.example.tripnila.common.Orange
import com.example.tripnila.model.AddBusinessViewModel
import com.example.tripnila.model.AddListingViewModel
import com.example.tripnila.model.HostTourViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddListingScreen1(
    listingType: String = "",
    hostId: String = "",
    serviceId: String = "empty",
    onNavToNext: (String) -> Unit,
    onNavToCancel: (String) -> Unit,
    addListingViewModel: AddListingViewModel? = null,
    hostTourViewModel: HostTourViewModel? = null,
    addBusinessViewModel: AddBusinessViewModel? = null,
){

    val staycationId = addListingViewModel?.staycation?.collectAsState()?.value?.staycationId
    val tourId = hostTourViewModel?.tour?.collectAsState()?.value?.tourId
    val businessId = addBusinessViewModel?.business?.collectAsState()?.value?.businessId

    LaunchedEffect(hostId) {
        if (hostId != "") {
            when (listingType) {
                "Staycation" -> addListingViewModel?.setHostId(hostId)
                "Tour" -> hostTourViewModel?.setHostId(hostId)
                "Business" -> addBusinessViewModel?.setHostId(hostId)
                else -> throw IllegalStateException("Unknown")
            }
        }
    }

    LaunchedEffect(serviceId) {
        if (serviceId != "empty"){
            when (listingType) {
                "Staycation" -> addListingViewModel?.getSelectedStaycation(serviceId)
                "Tour" -> hostTourViewModel?.getSelectedTour(serviceId)
                "Business" -> addBusinessViewModel?.getSelectedBusiness(serviceId)
            }
        }
    }

    val currentServiceId = when(listingType) {
        "Staycation" -> if (staycationId != "") staycationId else "empty"
        "Tour" -> if (tourId != "") tourId else "empty"
        "Business" -> if (businessId != "") businessId else "empty"
        else -> "empty"
    }



    Log.d("HostId", "Host ID: $hostId, ListingType: $listingType, Service ID: $serviceId, Current Service ID: $currentServiceId" )

    val header = when (listingType) {
        "Staycation" -> {
            "Tell us about your space"
        }
        "Business" -> {
            "Tell us about your business"
        }
        else -> {
            "Tell us about your tour"
        }
    }

    val description = when (listingType) {
        "Staycation" -> {
            "In this step, we’ll ask you what type of property you want to list and " +
                    "if guests will book the entire space or just a room. Then tell us how many" +
                    " guests will be allowed to book, and how many rooms does your space have. " +
                    "We highly encourage promoting eco-friendly amenities and activities. " +
                    "If you have any policies that are eco-friendly, let us know!"
        }
        "Business" -> {
            "In this step, we’ll ask you what type of business you want to upload, " +
                    "and basic information about it. "+
                    "We highly encourage promoting eco-friendly amenities and activities. " +
                    "If you have any policies that are eco-friendly, let us know!"

        }
        else -> {
            "In this step, we’ll ask you what type of tour you want to list, and basic information about it. "+
                    "We highly encourage promoting eco-friendly amenities and activities. " +
                    "If you have any policies that are eco-friendly, let us know!"
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
    ){
        if ((serviceId != currentServiceId)) {
            LoadingScreen(isLoadingCompleted = false, isLightModeActive = true)
        } else {
            Scaffold(
                bottomBar = {
                    AddListingBottomBookingBar(
                        onNext = {
                            onNavToNext(listingType)
                        },
                        onCancel = {
                            onNavToCancel(hostId.substring(5))
                        }
                    )
                },
                topBar = {
                    TopAppBar(
                        title = {
                            /*SaveAndExitButton(
                                onClick = { *//*TODO*//* }
                        )*/
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
                        text = "Step 1",
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
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    AddListingStepIndicator(modifier = Modifier, currentPage = 0, pageCount = 4)


                }
            }
        }

    }
}

@Composable
fun AddListingBottomBookingBar(
    modifier: Modifier = Modifier,
    onCancel: (() -> Unit)? = null,
    onNext: (() -> Unit)? = null,
    leftButtonText: String = "Cancel",
    rightButtonText: String = "Next",
    enableLeftButton: Boolean = true,
    enableRightButton: Boolean = true,
    isRightButtonLoading: Boolean = false,
){
    Surface(
        color = Color.White,
        tonalElevation = 10.dp,
        shadowElevation = 10.dp,
        modifier = modifier
            .height(78.dp)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 25.dp, vertical = 12.dp),
        ) {
            BookingOutlinedButton(
                enableButton = enableLeftButton,
                buttonText = leftButtonText,
                onClick = {
                    onCancel?.invoke()
                },
                modifier = Modifier.width(120.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            BookingFilledButton(
                enabled = enableRightButton,
                buttonText = rightButtonText,
                onClick = {
                    onNext?.invoke()
                },
                isLoading = isRightButtonLoading,
                modifier = Modifier.width(120.dp)
            )
        }
    }
}

@Composable
fun SaveAndExitButton( onClick: () -> Unit, modifier: Modifier = Modifier) {

    OutlinedButton(
        onClick = onClick,
        shape = RoundedCornerShape(15.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Orange),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
        modifier = modifier
            .height(height = 22.dp)
    ) {
        Text(
            text = "Save & exit",
            color = Orange,
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium,
        )
    }
}

@Composable
fun AddListingStepIndicator(
    modifier: Modifier = Modifier,
    currentPage: Int,
    pageCount: Int
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        repeat(pageCount) { page ->
            val iconColor = if (page == currentPage) Orange else Color(0xFFBABABA)
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .size(9.dp)
                    .background(iconColor)
            )
            if (page < pageCount - 1) {
                Spacer(modifier = Modifier.width(6.dp))
            }
        }
    }
}

@Preview
@Composable
private fun AddListingPreview(){
    AddListingStepIndicator(modifier = Modifier, currentPage = 0, pageCount = 4)
}

@Preview
@Composable
private fun AddListingScreen1Preview(){
   // AddListingScreen1({})
}
