package com.example.tripnila.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tripnila.R
import com.example.tripnila.components.Orange
import com.example.tripnila.data.PropertyDescription

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddListingScreen2(listingType: String = "Business"){

    var selectedPropertyIndex by remember { mutableStateOf(-1) }
    val header = if (listingType == "Staycation") {
        "Which of these best describe your space?"
    } else if (listingType == "Business"){
        "Which of these best describe your business?"
    }
    else {
        "Which of these best describe your tour?"
    }
    val types = if (listingType == "Staycation") {
                    listOf(
                        PropertyDescription(
                            icon = R.drawable.house,
                            label = "House"
                        ),
                        PropertyDescription(
                            icon = R.drawable.apartment,
                            label = "Apartment"
                        ),
                        PropertyDescription(
                            icon = R.drawable.condominium,
                            label = "Condominium"
                        ),
                        PropertyDescription(
                            icon = R.drawable.camp,
                            label = "Camp"
                        ),
                        PropertyDescription(
                            icon = R.drawable.guesthouse,
                            label = "Guest House"
                        ),
                        PropertyDescription(
                            icon = R.drawable.hotel,
                            label = "Hotel"
                        ),
                    )
                }
                else if (listingType == "Business") {
                    listOf(
                        PropertyDescription(
                            icon = R.drawable.house,
                            label = "Restaurant"
                        ),
                        PropertyDescription(
                            icon = R.drawable.apartment,
                            label = "Activity Center"
                        ),
                        PropertyDescription(
                            icon = R.drawable.condominium,
                            label = "Bar or Club"
                        ),
                        PropertyDescription(
                            icon = R.drawable.camp,
                            label = "Retail Store"
                        ),
                        PropertyDescription(
                            icon = R.drawable.guesthouse,
                            label = "Salon or Spa"
                        ),
                        PropertyDescription(
                            icon = R.drawable.hotel,
                            label = "Park or Resort"
                        ),
                    )
                }
                else {
                    listOf(
                        PropertyDescription(
                            icon = R.drawable.house,
                            label = "Photo Tour"
                        ),
                        PropertyDescription(
                            icon = R.drawable.apartment,
                            label = "Food Trip"
                        ),
                        PropertyDescription(
                            icon = R.drawable.apartment,
                            label = "Bar Hopping"
                        )
                    )
                }
    val numRows = (types.size + 1) / 2

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
                    verticalArrangement = Arrangement.spacedBy(15.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    item {
                        Text(
                            text = header,
                            color = Color(0xff333333),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier
                                .width(267.dp)
                                .padding(bottom = 10.dp)
                        )
                    }
                    items(numRows) { row ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            val startIndex = row * 2
                            val endIndex = minOf(startIndex + 2, types.size)

                            for (i in startIndex until endIndex) {
                                val type = types[i]
                                PropertyTypeCard(
                                    icon = type.icon,
                                    label = type.label,
                                    selected = selectedPropertyIndex == types.indexOf(type),
                                    onSelectedChange = { isSelected ->
                                        selectedPropertyIndex = if (isSelected) types.indexOf(type) else -1
                                    },
                                )
                            }
                        }
                    }

                }

//                LazyVerticalGrid(
//                    columns = GridCells.Fixed(2),
//                    contentPadding = PaddingValues(vertical = 25.dp),
//                    verticalArrangement = Arrangement.spacedBy(15.dp),
//                    horizontalArrangement = Arrangement.spacedBy(15.dp),
//                    modifier = Modifier.fillMaxWidth()
//                ) {
//                    items(types) { type ->
//                        PropertyTypeCard(
//                            icon = type.icon,
//                            label = type.label,
//                            selected = selectedPropertyIndex == types.indexOf(type),
//                            onSelectedChange = { isSelected ->
//                                selectedPropertyIndex = if (isSelected) types.indexOf(type) else -1
//                            },
//                        )
//                    }
//                }

                Spacer(modifier = Modifier.weight(1f))
                AddListingStepIndicator(modifier = Modifier, currentPage = 0, pageCount = 4)

            }
        }
    }
}

@Composable
fun PropertyTypeCard(
    icon: Int,
    label: String,
    selected: Boolean,
    onSelectedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val borderColor = if (selected) Orange else Color(0xff999999)
    val containerColor = if (selected) Orange.copy(alpha = 0.2f) else Color.White

    Box(
        modifier = modifier
            .width(width = 165.dp)
            .height(height = 90.dp)
            .clip(shape = RoundedCornerShape(10.dp))
            .border(
                border = BorderStroke(1.dp, borderColor),
                shape = RoundedCornerShape(10.dp)
            )
            .background(containerColor)
            .clickable { onSelectedChange(!selected) } // Toggle the selection state
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, end = 10.dp, top = 15.dp, bottom = 12.dp)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(icon),
                contentDescription = label,
                tint = Color(0xff333333)
            )
            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = label,
                color = Color(0xff333333),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
            )
        }
    }
}


@Preview
@Composable
private fun AddListing2Preview(){
    PropertyTypeCard(
        icon = R.drawable.house,
        label = "House",
        selected = true,
        onSelectedChange = {}
    )
}

@Preview
@Composable
private fun AddListingScreen2Preview(){
    AddListingScreen2()
}