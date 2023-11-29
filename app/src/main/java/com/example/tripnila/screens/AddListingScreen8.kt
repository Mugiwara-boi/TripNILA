package com.example.tripnila.screens

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.sharp.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.tripnila.R
import com.example.tripnila.common.Orange
import com.example.tripnila.model.AddBusinessViewModel
import com.example.tripnila.model.AddListingViewModel
import com.example.tripnila.model.HostTourViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddListingScreen8(
    listingType: String = "",
    addListingViewModel: AddListingViewModel? = null,
    hostTourViewModel: HostTourViewModel? = null,
    addBusinessViewModel: AddBusinessViewModel? = null,
    onNavToNext: (String) -> Unit,
    onNavToBack: () -> Unit,
){

    Log.d("Listing8", listingType)

    val selectedImageUris = when (listingType) {
        "Staycation" -> remember { mutableStateOf(addListingViewModel?.staycation?.value?.staycationImages?.filter { it.photoType == "Others" }?.map { it.photoUri }) }
        "Tour" -> remember { mutableStateOf(hostTourViewModel?.tour?.value?.tourImages?.filter { it.photoType == "Others" }?.map { it.photoUri }) }
        "Business" -> remember { mutableStateOf(addBusinessViewModel?.business?.value?.businessImages?.filter { it.photoType == "Others" }?.map { it.photoUri }) }
        else -> throw IllegalStateException("Unknown")
    }


    val selectedImageUri = when (listingType) {
        "Staycation" -> remember {
            mutableStateOf(addListingViewModel?.staycation?.value?.staycationImages
                ?.firstOrNull { it.photoType == "Cover" }
                ?.photoUri)
        }
        "Tour" -> remember {
            mutableStateOf(hostTourViewModel?.tour?.value?.tourImages
                ?.firstOrNull { it.photoType == "Cover" }
                ?.photoUri)
        }
        "Business" -> remember {
            mutableStateOf(addBusinessViewModel?.business?.value?.businessImages
                ?.firstOrNull { it.photoType == "Cover" }
                ?.photoUri)
        }
        else -> throw IllegalStateException("Unknown")
    }


    val singlePhotoPickerLauncher = when (listingType) {
        "Staycation" -> rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickVisualMedia()
        ) { uri ->
            if (uri != null) {
                selectedImageUri.value = uri
                addListingViewModel?.setStaycationCoverPhoto(uri)
            }

        }
        "Tour" -> rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickVisualMedia()
        ) { uri ->
            if (uri != null) {
                selectedImageUri.value = uri
                hostTourViewModel?.setCoverPhoto(uri)
            }
        }
        "Business" -> rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickVisualMedia()
        ) { uri ->
            if (uri != null) {
                selectedImageUri.value = uri
                addBusinessViewModel?.setCoverPhoto(uri)
            }
        }
        else -> throw IllegalStateException("Unknown")
    }


    val multiplePhotoPickerLauncher = when (listingType) {
        "Staycation" -> rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickMultipleVisualMedia()
        ) { uris ->
            selectedImageUris.value = selectedImageUris.value?.plus(uris)
            addListingViewModel?.setSelectedImageUris(uris)
        }
        "Tour" -> rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickMultipleVisualMedia()
        ) { uris ->
            selectedImageUris.value = selectedImageUris.value?.plus(uris)
            hostTourViewModel?.setSelectedImageUris(uris)
        }
        "Business" -> rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickMultipleVisualMedia()
        ) { uris ->
            selectedImageUris.value = selectedImageUris.value?.plus(uris)
            addBusinessViewModel?.setSelectedImageUris(uris)
        }
        else -> throw IllegalStateException("Unknown")
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
                    enableRightButton = when (listingType) {
                        "Staycation" -> addListingViewModel?.staycation?.collectAsState()?.value?.staycationImages?.any { it.photoType == "Cover" } == true && addListingViewModel.staycation.collectAsState().value.staycationImages.any { it.photoType == "Others" }
                        "Tour" -> hostTourViewModel?.tour?.collectAsState()?.value?.tourImages?.any { it.photoType == "Cover" } == true && hostTourViewModel.tour.collectAsState().value.tourImages.any { it.photoType == "Others" }
                        "Business" -> addBusinessViewModel?.business?.collectAsState()?.value?.businessImages?.any { it.photoType == "Cover" } == true && addBusinessViewModel.business.collectAsState().value.businessImages.any { it.photoType == "Others" }
                        else -> throw IllegalStateException("Unknown")
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
                    modifier = Modifier.fillMaxWidth().weight(1f)
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
                        val stroke = Stroke(
                            width = 2f,
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(170.dp)
                                .drawBehind {
                                    drawRoundRect(
                                        color = if (selectedImageUri.value != null) Color.Transparent else Orange,
                                        style = stroke
                                    )
                                }
                        ){

                            AsyncImage(
                                model = selectedImageUri.value,
                                contentDescription = "Cover photo",
                                contentScale = ContentScale.FillWidth,
                                //modifier = Modifier.height(170.dp).fillMaxWidth()
                            )
                            Button(
                                onClick = {
                                    singlePhotoPickerLauncher.launch(
                                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                    )
                                },
                                enabled = true,
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

                    val numRows = (selectedImageUris.value?.size?.plus(1))?.div(2)
                    if (numRows != null) {
                        items(numRows) { row ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                val startIndex = row * 2
                                val endIndex = selectedImageUris.value?.size?.let { size ->
                                    minOf(startIndex + 2,
                                        size
                                    )
                                }

                                for (i in startIndex until endIndex!!) {
                                    val uri = selectedImageUris.value?.get(i)
                                    Box {
                                        AsyncImage(
                                            model = uri,
                                            contentDescription = null,
                                            modifier = Modifier
                                                .width(165.dp)
                                                .height(110.dp)
                                                .padding(vertical = 5.dp),
                                            contentScale = ContentScale.FillWidth
                                        )
                                        IconButton(
                                            onClick = {
                                                selectedImageUris.value = selectedImageUris.value?.filter { it != uri }

                                                when (listingType) {
                                                    "Staycation" -> uri?.let { uri -> addListingViewModel?.removeSelectedImage(uri = uri) }
                                                    "Business" -> uri?.let { uri -> addBusinessViewModel?.removeSelectedImage(uri = uri) }
                                                    "Tour" -> uri?.let { uri -> hostTourViewModel?.removeSelectedImage(uri = uri) }
                                                }

                                            },
                                            modifier = Modifier
                                                .align(Alignment.TopEnd)
                                                .offset(x = 8.dp, y = (-5).dp)
                                        ) {
                                            Icon(Icons.Sharp.Clear, contentDescription = "Delete", modifier = Modifier.size(16.dp), tint = Color(0xFF999999))
                                        }
                                    }
                                }
                                if (endIndex - startIndex == 1) {
                                    AddMorePhoto(
                                        onClick = {
                                            multiplePhotoPickerLauncher.launch(
                                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                            )
                                        }
                                    )
                                }
                            }
                        }
                    }
                    if ((selectedImageUris.value?.size?.rem(2) ?: 0) == 0) {
                        item {
                            AddMorePhoto(
                                onClick = {
                                    multiplePhotoPickerLauncher.launch(
                                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                    )

                                }
                            )
                        }
                    }

                }
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
fun AddMorePhoto(
    onClick:() -> Unit,
    modifier: Modifier = Modifier
){

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
            .clickable { onClick() },

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
    //AddMorePhoto()
}

@Preview
@Composable
private fun AddListingScreen8Preview(){
    val addListingViewModel = viewModel(modelClass = AddListingViewModel::class.java)

    AddListingScreen8(
        listingType = "Staycation",
        addListingViewModel = addListingViewModel,
        onNavToBack = {},
        onNavToNext = {}
    )
}