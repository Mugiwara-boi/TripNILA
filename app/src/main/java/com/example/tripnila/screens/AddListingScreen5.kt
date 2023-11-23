package com.example.tripnila.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tripnila.R
import com.example.tripnila.model.AddListingViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddListingScreen5(
    listingType: String = "Staycation",
    addListingViewModel: AddListingViewModel? = null,
    onNavToNext: (String) -> Unit,
    onNavToBack: () -> Unit,
){

    var guestCount by remember { mutableStateOf(addListingViewModel?.staycation?.value?.noOfGuests) }
    var bedroomCount by remember { mutableStateOf(addListingViewModel?.staycation?.value?.noOfBedrooms) }
    var bedCount by remember { mutableStateOf(addListingViewModel?.staycation?.value?.noOfBeds) }
    var bathroomCount by remember { mutableStateOf(addListingViewModel?.staycation?.value?.noOfBathrooms) }

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
                    enableRightButton = addListingViewModel?.staycation?.collectAsState()?.value?.noOfGuests != 0
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
                    DetailsCounter(
                        label = "Guests",
                        textCounter = guestCount ?: 0,
                        onAdd = {
                            guestCount = guestCount!! + 1
                            addListingViewModel?.setNoOfGuests(guestCount!!)
                        },
                        onSubtract = {
                            if (guestCount!! > 0) {
                                guestCount = guestCount!! - 1
                                addListingViewModel?.setNoOfGuests(guestCount!!)
                            }
                        }
                    )
                    DetailsCounter(
                        label = "Bedrooms",
                        textCounter = bedroomCount ?: 0,
                        onAdd = {
                            bedroomCount = bedroomCount!! + 1
                            addListingViewModel?.setNoOfBedrooms(bedroomCount!!)
                        },
                        onSubtract = {
                            if (bedroomCount!! > 0) {
                                bedroomCount = bedroomCount!! - 1
                                addListingViewModel?.setNoOfBedrooms(bedroomCount!!)
                            }
                        }
                    )
                    DetailsCounter(
                        label = "Beds",
                        textCounter = bedCount ?: 0,
                        onAdd = {
                            bedCount = bedCount!! + 1
                            addListingViewModel?.setNoOfBeds(bedCount!!)
                        },
                        onSubtract = {
                            if (bedCount!! > 0) {
                                bedCount = bedCount!! - 1
                                addListingViewModel?.setNoOfBeds(bedCount!!)
                            }
                        }
                    )
                    DetailsCounter(
                        label = "Bathrooms",
                        textCounter = bathroomCount ?: 0,
                        onAdd = {
                            bathroomCount = bathroomCount!! + 1
                            addListingViewModel?.setNoOfBathrooms(bathroomCount!!)
                        },
                        onSubtract = {
                            if (bathroomCount!! > 0) {
                                bathroomCount = bathroomCount!! - 1
                                addListingViewModel?.setNoOfBathrooms(bathroomCount!!)
                            }

                        }
                    )

//
//                    DetailsCounter(label = "Bedrooms")
//                    DetailsCounter(label = "Beds")
//                    DetailsCounter(label = "Bathrooms")
                }

                Spacer(modifier = Modifier.weight(1f))
                AddListingStepIndicator(modifier = Modifier, currentPage = 0, pageCount = 4)

            }
        }
    }
}

@Composable
fun DetailsCounter(
    label: String,
    modifier: Modifier = Modifier,
    textCounter: Int,
    onAdd:() -> Unit,
    onSubtract:() -> Unit,
) {
   // var count by remember { mutableStateOf(0) }

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
                onSubtract()
            },
            enabled = textCounter > 0,
            modifier = Modifier.size(17.dp)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.subtract_circle),
                contentDescription = "Subtract",
                tint = if (textCounter > 0) Color(0xFF999999) else Color(0xFFDEDEDE)
            )
        }
        Text(
            text = textCounter.toString(),
            color = Color(0xff333333),
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 15.dp)
        )
        IconButton(
            onClick = {
            // if (count < maxCount) {
                onAdd()
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


 //   DetailsCounter("Guests")
}

@Preview
@Composable
private fun AddListingScreen5Preview(){
    val addListingViewModel = viewModel(modelClass = AddListingViewModel::class.java)

    AddListingScreen5(
        addListingViewModel = addListingViewModel,
        onNavToBack = {},
        onNavToNext = {}
    )
}
