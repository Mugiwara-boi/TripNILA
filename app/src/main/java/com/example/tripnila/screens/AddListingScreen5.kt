package com.example.tripnila.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tripnila.R
import com.example.tripnila.common.Orange
import com.example.tripnila.model.AddListingViewModel
import java.text.NumberFormat
import java.util.Locale

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
    var allowAddGuest by remember { mutableStateOf(false) }
    val formatter = NumberFormat.getNumberInstance(Locale.US)
    val fee = 0
    var isFocused by remember { mutableStateOf(false) }
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
                    Row(modifier = Modifier
                        .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Allow extra guests?",
                            color = Color(0xff333333),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(top = 10.dp, bottom = 5.dp)
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Checkbox(
                            checked = allowAddGuest,
                            onCheckedChange = { isChecked ->
                                allowAddGuest = isChecked
                            },
                            colors = CheckboxDefaults.colors(
                                checkedColor = Orange
                            ),
                            modifier = Modifier.offset(x = 13.dp)

                        )

                    }
                    Divider(color = Color(0xFF999999))

                    if(allowAddGuest){
                        DetailsCounter(
                            label = "Max Guests",
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
                        Row(modifier = Modifier
                            .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Additional fee per guest",
                                color = Color(0xff333333),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(top = 10.dp, bottom = 5.dp)
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            BasicTextField(

                                value = formatter.format(fee),
                                onValueChange = {
                                    /*entranceFee.value = it.replace(",", "").toIntOrNull() ?: 0

                                    addBusinessViewModel?.setEntranceFee(entranceFee?.value!!.toDouble())*/
                                },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                textStyle = TextStyle(fontSize = 18.sp),
                                singleLine = true,
                                decorationBox = { innerTextField ->
                                    Row(
                                        modifier = Modifier
                                            .background(
                                                color = Color.White,
                                                shape = RoundedCornerShape(size = 10.dp)
                                            )
                                            .border(
                                                width = 2.dp,
                                                color = if (isFocused) Orange else Color(
                                                    0xFFC2C2C2
                                                ),
                                                shape = RoundedCornerShape(size = 10.dp)
                                            )
                                            .padding(all = 8.dp), // inner padding
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "₱ ",
                                            fontWeight = FontWeight.Medium,
                                        )

                                        innerTextField()
                                    }
                                }
                            )

                        }
                        Divider(color = Color(0xFF999999))

                    }
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
/*@Composable
fun MaxGuestCounter(
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
        CheckboxRow(rowLabel = , selected = , onSelectedChange = )
    }
    Divider(color = Color(0xFF999999))
}*/
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
