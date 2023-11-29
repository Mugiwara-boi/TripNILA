package com.example.tripnila.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.tripnila.R
import com.example.tripnila.common.Orange
import com.example.tripnila.common.Tag
import com.example.tripnila.common.TouristBottomNavigationBar
import com.example.tripnila.data.AttractionUiState
import com.example.tripnila.data.ItineraryUiState
import com.example.tripnila.data.Staycation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItineraryPopulatedScreen(
    touristId: String = "",
    navController: NavHostController? = null
){

    var selectedItemIndex by rememberSaveable {
        mutableStateOf(1)
    }

    var selectedTabIndex by remember { mutableStateOf(0) }

    var selectedItems by remember { mutableStateOf(emptyList<ItineraryUiState>()) }

    val tabs = listOf("Sports", "Food Trip", "Shop", "Nature", "Gaming", "Karaoke", "History", "Clubs", "Sightseeing", "Swimming")

    var isDialogOpen by remember { mutableStateOf(false) }
    var openBottomSheet by remember { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    val horizontalPaddingValue = 16.dp
    val verticalPaddingValue = 10.dp

    val backgroundColor = Color.White
    
    val itinerary = listOf(
        ItineraryUiState(
            image = "https://upload.wikimedia.org/wikipedia/commons/thumb/3/3f/Placeholder_view_vector.svg/1022px-Placeholder_view_vector.svg.png",
            rating = 4.0,
            price = 2000,
            itineraryType = "Restaurant",
            title = "Le's Bar & Grill",
            location = "2.1 km"

        ),
        ItineraryUiState(
            image = "https://upload.wikimedia.org/wikipedia/commons/thumb/3/3f/Placeholder_view_vector.svg/1022px-Placeholder_view_vector.svg.png",
            rating = 4.0,
            price = 2000,
            itineraryType = "Restaurant",
            title = "Lo's Bar & Grill",
            location = "2.1 km"

        ),
        ItineraryUiState(
            image = "https://upload.wikimedia.org/wikipedia/commons/thumb/3/3f/Placeholder_view_vector.svg/1022px-Placeholder_view_vector.svg.png",
            rating = 4.0,
            price = 2000,
            itineraryType = "Restaurant",
            title = "Leo' Bar & Grill",
            location = "2.1 km"

        ),
        ItineraryUiState(
            image = "https://upload.wikimedia.org/wikipedia/commons/thumb/3/3f/Placeholder_view_vector.svg/1022px-Placeholder_view_vector.svg.png",
            rating = 4.0,
            price = 2000,
            itineraryType = "Restaurant",
            title = "eo's Bar & Grill",
            location = "2.1 km"

        ),
        ItineraryUiState(
            image = "https://upload.wikimedia.org/wikipedia/commons/thumb/3/3f/Placeholder_view_vector.svg/1022px-Placeholder_view_vector.svg.png",
            rating = 4.0,
            price = 2000,
            itineraryType = "Restaurant",
            title = "Leo's Bar & rill",
            location = "2.1 km"

        )
    )

    val attractionUiStates = listOf(
        AttractionUiState(
            image = R.drawable.map_image,
            name = "Rainforest Park",
            tag = listOf("Nature"),
            distance = 500,
            price = 1000.00,
            openingTime = "7:30 am"
        ),
        AttractionUiState(
            image = R.drawable.map_image,
            name = "Pasig Museum",
            tag = listOf("History"),
            distance = 2700,
            price = 1000.00,
            openingTime = "9:00 pm"
        ),
    )

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ){
        Scaffold(
            bottomBar = {
                navController?.let {
                    TouristBottomNavigationBar(
                        touristId = touristId,
                        navController = it,
                        selectedItemIndex = selectedItemIndex,
                        onItemSelected = { newIndex ->
                            selectedItemIndex = newIndex
                        }
                    )
                }
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        openBottomSheet = true
                    },
                    shape = CircleShape,
                    containerColor = Orange,
                    contentColor = Color.White,
                    elevation = FloatingActionButtonDefaults.elevation(
                        defaultElevation = 10.dp
                    ),
                    modifier = Modifier
                        // .padding(start = 10.dp)
                        .size(50.dp)


                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Send",
                        modifier = Modifier.size(30.dp),
                    )
                }
            }
        ){
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                item {
                    AppTopBar(
                        headerText = "Itinerary Planner",
                        color = Color.White
                    )
                }
//                item {
//                    SelectedStaycationCard(
//                        staycation = null
//                    )
//                }
                item {
                    BudgetCard(
                        modifier = Modifier
                            .padding(vertical = verticalPaddingValue)
                    )
                }
//                item {
//                    ActivitiesCard(
//                        modifier = Modifier
//                            .padding(vertical = verticalPaddingValue)
//                    )
//                }
                item {
                    YourTripCard(
                        attractions = attractionUiStates,
                        onClick = {
                            openBottomSheet = true
                        },
                        modifier = Modifier
                            .padding(
                                horizontal = horizontalPaddingValue,
                                vertical = verticalPaddingValue
                            )
                    )
                }
            }
        }

        if (openBottomSheet) {
            ModalBottomSheet(
                modifier = Modifier.fillMaxHeight(.88f),
                onDismissRequest = {
                    openBottomSheet = false
                },
                sheetState = bottomSheetState,
                containerColor = Color.White,
                dragHandle = {

                }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = horizontalPaddingValue)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = verticalPaddingValue),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Text(
                            text = "Add on trip",
                            style = TextStyle(
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium
                            ),
                        )
                    }
                    Card(
                        border = BorderStroke(2.dp, Color(0xff666666).copy(alpha = 0.4f)),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    ) {

                    }

                    Text(
                        text = "Budget",
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium),
                        modifier = Modifier.padding(vertical = verticalPaddingValue),

                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        BudgetTextField()
                        Divider(
                            Modifier
                                .width(40.dp)
                                .offset(y = 15.dp))
                        BudgetTextField()
                    }
                    Box(modifier = Modifier.offset()) {
                        Text(
                            text = "Min",
                            color = Color(0xffc2c2c2),
                            style = TextStyle(
                                fontSize = 7.sp,
                                fontWeight = FontWeight.Medium),
                            modifier = Modifier.offset(x = 28.dp, y = (-32).dp)
                        )
                        Text(
                            text = "Max",
                            color = Color(0xffc2c2c2),
                            style = TextStyle(
                                fontSize = 7.sp,
                                fontWeight = FontWeight.Medium),
                            modifier = Modifier.offset(x = 228.dp, y = (-32).dp)
                        )
                    }
                    Text(
                        text = "Activities",
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium),
                        modifier = Modifier.padding(vertical = verticalPaddingValue),

                    )
                    ScrollableTabRow(
                        selectedTabIndex = selectedTabIndex,
                        containerColor = Color.White,
                        edgePadding = 5.dp,
                        divider = {},
                        indicator = {},
                        modifier = Modifier
                            .padding(vertical = 5.dp)
                            .padding(start = 10.dp)
                            .fillMaxWidth(),
                    ) {
                        tabs.forEachIndexed { index, tabLabel ->
                            DayTab(
                                tabLabel = tabLabel,
                                isSelected = index == selectedTabIndex,
                                onTabSelected = { selectedTabIndex = index },
                            )
                        }
                    }
                    LazyRow {
                        items(itinerary) { itineraryItem ->
                            ItineraryCard(
                                modifier = Modifier.padding(horizontal = 8.dp),
                                image = itineraryItem.image,
                                rating = itineraryItem.rating,
                                price = itineraryItem.price,
                                itineraryType = itineraryItem.itineraryType,
                                title = itineraryItem.title,
                                location = itineraryItem.location,
                                onSelect = {
                                    if (it) {
                                        selectedItems = selectedItems + itineraryItem
                                    } else {
                                        selectedItems = selectedItems - itineraryItem
                                    }
                                },
                                isSelected = itineraryItem in selectedItems
                            )
                        }
                    }
                    Text(
                        text = "Tours",
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium),
                        modifier = Modifier.padding(vertical = verticalPaddingValue),
                    )
                    LazyRow {
                        items(itinerary) { itineraryItem ->
                            ItineraryCard(
                                modifier = Modifier.padding(horizontal = 8.dp),
                                image = itineraryItem.image,
                                rating = itineraryItem.rating,
                                price = itineraryItem.price,
                                itineraryType = itineraryItem.itineraryType,
                                title = itineraryItem.title,
                                location = itineraryItem.location,
                                onSelect = {
                                    if (it) {
                                        selectedItems = selectedItems + itineraryItem
                                    } else {
                                        selectedItems = selectedItems - itineraryItem
                                    }
                                },
                                isSelected = itineraryItem in selectedItems
                            )
                        }
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = horizontalPaddingValue,
                                vertical = verticalPaddingValue
                            )
                    ) {
                        BookingOutlinedButton(
                            buttonText = "Cancel",
                            onClick = { /*TODO*/ },
                            modifier = Modifier.width(120.dp)
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        BookingFilledButton(
                            buttonText = "Add",
                            onClick = { /*TODO*/ },
                            modifier = Modifier.width(120.dp)
                        )
                    }


                }

                
            }
        }
    }

//    ChooseStaycationDialog(
//        backgroundColor = backgroundColor,
//        isDialogOpen = isDialogOpen,
//        onDismissRequest = {
//            isDialogOpen = false
//        },
//        onConfirm = {
//            isDialogOpen = false
//        }
//    )
}

@Composable
fun ItineraryCard(
    modifier: Modifier = Modifier,
    isSelected: Boolean,
    onSelect: (Boolean) -> Unit,
    image: String,
    rating: Double,
    price: Int,
    itineraryType: String,
    title: String,
    location: String
) {

    Column {
        Box(
            modifier = modifier
                .width(width = 90.dp)
                .height(height = 85.dp)
                .clip(RoundedCornerShape(10.dp))
                .clickable {
                    onSelect(!isSelected)
                }
                .border(
                    width = 2.dp,
                    color = if (isSelected) Orange else Color.Transparent
                )
        ) {
            AsyncImage(model = image, contentDescription = null, contentScale = ContentScale.Crop)
            Text(
                text = rating.toString(),
                style = TextStyle(
                    fontSize = 5.800000190734863.sp,
                    fontWeight = FontWeight.Medium),
                modifier = Modifier
                    .padding(horizontal = 4.dp, vertical = 4.dp)
                    .background(Color.White)
                    .padding(horizontal = 4.dp, vertical = 4.dp)
                    .align(Alignment.TopStart),
            )
            Text(
                text = "₱ $price",
                color = Color.White,
                style = TextStyle(
                    fontSize = 5.800000190734863.sp,
                    fontWeight = FontWeight.Medium),
                modifier = Modifier
                    .padding(horizontal = 4.dp, vertical = 4.dp)
                    .background(Color.Black.copy(.8f))
                    .padding(horizontal = 4.dp, vertical = 4.dp)
                    .align(Alignment.BottomEnd),
            )

        }
        Text(
            text = "$itineraryType • $location",
            color = Color(0xff999999),
            style = TextStyle(
                fontSize = 7.sp,
                fontWeight = FontWeight.Medium),
            modifier = Modifier.padding(start = 10.dp)
        )
        Text(
            text = "$title",
            textDecoration = TextDecoration.Underline,
            style = TextStyle(
                fontSize = 9.sp),
            modifier = Modifier.padding(start = 10.dp)
        )
    }

}


@Composable
fun SelectedStaycationCard(

    modifier: Modifier = Modifier,
    staycation: Staycation? = null,
    onChange: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp
        ),
        shape = RoundedCornerShape(20.dp),
        modifier = modifier
            .fillMaxWidth()
            //.requiredHeight(height = 93.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(
                    horizontal = 16.dp,
                    vertical = 15.dp // 12
                )
                .fillMaxWidth()
        ) {
            Row {
                Column {
                    Row {
                        Text(
                            text = staycation?.staycationTitle ?: "Modern house with 2 bedrooms",
                            color = Color(0xff333333),
                            style = TextStyle(
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            ),
                            modifier = Modifier
                                .width(147.dp)
                        )
                        AppOutlinedButtonWithBadge(
                            buttonLabel = "Change",
                            onClick = {
                                onChange()
                            }
                        )


                    }
                    Text(
                        text = "by ${staycation?.host?.firstName ?: "Joshua"}",
                        color = Color(0xfff9a664),
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    )
                    Row {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.location),
                            contentDescription = "Location",
                            tint = Color(0xff999999),
                            modifier = Modifier
                                .padding(top = 3.dp, end = 5.dp)
                        )
                        Text(
                            text = staycation?.staycationLocation ?: "North Greenhills",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xff999999),
                            modifier = Modifier
                        )
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
                Card(
                    shape = RoundedCornerShape(10.dp),
                    modifier = modifier
                        .width(width = 90.dp)
                        .height(height = 66.dp)
                ) {
                    AsyncImage(
                        model = staycation?.staycationImages?.find { it.photoType == "Cover" }?.photoUrl
                            ?: "https://upload.wikimedia.org/wikipedia/commons/thumb/3/3f/Placeholder_view_vector.svg/1022px-Placeholder_view_vector.svg.png",
                        contentDescription = "Staycation"
                    )
                }

            }
        }

    }

}

@Composable
fun AttractionCard(
    modifier: Modifier = Modifier,
    attraction: AttractionUiState? = null,
    selected: Boolean,
    onSelectedChange: (Boolean) -> Unit,
) {

    val borderColor = if (selected) Orange else Color(0xff999999)

    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        border = BorderStroke(1.dp, borderColor),
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        modifier = modifier
            .fillMaxWidth()
    ) {
        Row (
            modifier = Modifier
                .wrapContentHeight(Alignment.CenterVertically)
                .padding(// = 10.dp,
                    top = 5.dp,
                    bottom = 5.dp
                ),
            verticalAlignment = Alignment.CenterVertically

        ) {
            Checkbox(
                checked = selected,
                onCheckedChange = { onSelectedChange(it) },
                colors = CheckboxDefaults.colors(
                    checkedColor = Orange //Color(0xFF333333)
                )
            )
            Column(
                modifier = Modifier.wrapContentHeight(Alignment.CenterVertically)
            ) {
                Row {
                    Text(
                        text = attraction?.name ?: "Pinaglabanan Memorial Shrine",
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    )
                    val maxTagsToShow = 3
                    attraction?.tag?.take(maxTagsToShow)?.forEachIndexed { _, tag ->
                        Tag(tag = tag )
                    }

                    if ((attraction?.tag?.size ?: 0) > maxTagsToShow) {
                        Tag(tag = "+${attraction?.tag?.size?.minus(maxTagsToShow)}")
                    }
                }

                Text(
                    text = attraction?.distance.toString() +
                            " meters • ₱ " + String.format("%.2f", attraction?.price ?: 0.0) +
                            " • Opens at " + attraction?.openingTime,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF999999)
                )

            }


            Spacer(modifier = Modifier.weight(1f))

            Image(painter = painterResource(id = R.drawable.direction), contentDescription = null)
            IconButton(
                onClick = { /*TODO*/ },
                modifier = Modifier.size(35.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = Color(0xffD8D8D8),
                    modifier = Modifier.size(35.dp)
                )
            }
        }
    }
}

@Preview
@Composable
fun ItineraryPopulatedScreenPreview(){
    ItineraryPopulatedScreen(

    )

 //   SelectedStaycationCard()

}