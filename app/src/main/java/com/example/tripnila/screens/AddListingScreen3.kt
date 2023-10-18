package com.example.tripnila.screens

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
fun AddListingScreen3(){

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
                    text = "How much of the space will the guest have?",
                    color = Color(0xff333333),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .width(270.dp)
                )

                LazyVerticalGrid(
                    columns = GridCells.Fixed(1),
                    contentPadding = PaddingValues(vertical = 25.dp),
                    verticalArrangement = Arrangement.spacedBy(15.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(types) { type ->
                        PropertySpaceCard(
                            icon = type.icon,
                            label = type.label,
                            selected = selectedPropertyIndex == types.indexOf(type),
                            onSelectedChange = { isSelected ->
                                selectedPropertyIndex = if (isSelected) types.indexOf(type) else -1
                            },
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))
                AddListingStepIndicator(modifier = Modifier, currentPage = 0, pageCount = 4)

            }
        }
    }
}

@Composable
fun PropertySpaceCard(
    icon: Int,
    label: String,
    selected: Boolean,
    onSelectedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val borderColor = if (selected) Orange else Color(0xff999999)
    val containerColor = if (selected) Orange.copy(alpha = 0.2f) else Color.White
    val spaceDescription = if (label == "An entire place"){
                                "Guests have the whole place to themselves."
                            }
                            else {
                                "Guests have their own room in a place, plus access to shared spaces."
                            }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height = 86.dp)
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
                .padding(start = 10.dp, end = 10.dp, top = 15.dp, bottom = 15.dp)
        ) {
            Row {
                Text(
                    text = label,
                    color = Color(0xff333333),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    imageVector = ImageVector.vectorResource(icon),
                    contentDescription = label,
                    tint = Color(0xff333333),
                    modifier = Modifier.offset(y = 5.dp)
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = spaceDescription,
                color = Color(0xff999999),
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.width(181.dp)
            )
        }
    }
}

@Preview
@Composable
private fun AddListing3Preview(){
    val property = PropertyDescription(
        icon = R.drawable.house,
        label = "An entire place"
    )

    PropertySpaceCard(
        property.icon, property.label,  false, {}
    )

}

@Preview
@Composable
private fun AddListingScreen3Preview(){
    AddListingScreen3()
}
