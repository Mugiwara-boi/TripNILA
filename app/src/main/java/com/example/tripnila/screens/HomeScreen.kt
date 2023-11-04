package com.example.tripnila.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Close
import androidx.compose.material.icons.twotone.MoreVert
import androidx.compose.material.icons.twotone.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import com.example.tripnila.R
import com.example.tripnila.common.AppBottomNavigationBar
import com.example.tripnila.common.Orange
import com.example.tripnila.data.Staycation


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(){

    var searchText = remember{ mutableStateOf("") }
    var isActive = remember { mutableStateOf(false) }
    var selectedItemIndex by rememberSaveable {
        mutableStateOf(0)
    }

    val staycationList = listOf(
        Staycation(
            image = R.drawable.staycation1,
            host = "Host 1",
            location = "Location 1",
            price = 100,
            rating = 4.5
        ),
        Staycation(
            image = R.drawable.staycation1,
            host = "Host 1",
            location = "Location 1",
            price = 100,
            rating = 4.5
        ),
        Staycation(
            image = R.drawable.staycation1,
            host = "Host 1",
            location = "Location 1",
            price = 100,
            rating = 4.5
        ),
        Staycation(
            image = R.drawable.staycation1,
            host = "Host 1",
            location = "Location 1",
            price = 100,
            rating = 4.5
        ),
        Staycation(
            image = R.drawable.staycation1,
            host = "Host 1",
            location = "Location 1",
            price = 100,
            rating = 4.5
        ),
        Staycation(
            image = R.drawable.staycation1,
            host = "Host 1",
            location = "Location 1",
            price = 100,
            rating = 4.5
        ),
        Staycation(
            image = R.drawable.staycation1,
            host = "Host 1",
            location = "Location 1",
            price = 100,
            rating = 4.5
        ),
        Staycation(
            image = R.drawable.staycation1,
            host = "Host 1",
            location = "Location 1",
            price = 100,
            rating = 4.5
        ),
        Staycation(
            image = R.drawable.staycation1,
            host = "Host 1",
            location = "Location 1",
            price = 100,
            rating = 4.5
        ),
        Staycation(
            image = R.drawable.staycation1,
            host = "Host 1",
            location = "Location 1",
            price = 100,
            rating = 4.5
        ),
        Staycation(
            image = R.drawable.staycation1,
            host = "Host 1",
            location = "Location 1",
            price = 100,
            rating = 4.5
        ),
        Staycation(
            image = R.drawable.staycation1,
            host = "Host 1",
            location = "Location 1",
            price = 100,
            rating = 4.5
        ),

    )

    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Scaffold(
            bottomBar = {
                AppBottomNavigationBar(
                    selectedItemIndex = selectedItemIndex,
                    onItemSelected = { newIndex ->
                        selectedItemIndex = newIndex
                    }
                )
            }
        ) {
            Column(
                modifier = Modifier.padding(it)
            ) {
                //TripNilaIcon(modifier = Modifier.offset(x = (-15).dp, y = (-7).dp))

                SearchBar(
                    query = searchText.value,
                    onQueryChange = { searchText.value = it },
                    onSearch = { isActive.value = false },
                    active = isActive.value,
                    onActiveChange = { isActive.value = it },
                    //shape = ShapeDefaults.ExtraLarge,
                    leadingIcon = {
                        Icon(imageVector = Icons.TwoTone.Search, contentDescription = "Search")
                    },
                    trailingIcon = {
                        if(isActive.value){
                            Icon(
                                imageVector = Icons.TwoTone.Close,
                                contentDescription = "Close",
                                modifier = Modifier.clickable {
                                    isActive.value = false
                                    searchText.value = ""
                                }
                            )
                        }
                        else{
                            Icon(imageVector = Icons.TwoTone.MoreVert, contentDescription = "Close")
                        }
                    },
                    placeholder = {
                        Text(
                            text = "Search",
                            fontSize = 14.sp,
                            modifier = Modifier

                        )
                    },
                    modifier = Modifier
                        //.padding(top = 15.dp, start = 55.dp, end = 10.dp)
                        .fillMaxWidth()
                        .padding(top = 8.dp, start = 10.dp, end = 10.dp)
                        //.height(55.dp)
                ) {
                }
                PreferenceTabRow()
                StaggeredGridListing(staycationList = staycationList)
            }
        }
    }
}




@Composable
fun PreferenceTabRow(){

    val preferences = listOf("For you", "Sports", "Food Trip", "Shop", "Nature", "Gaming", "Karaoke", "History", "Clubs", "Sightseeing", "Swimming")
    val selectedTab = remember { mutableIntStateOf(0) }

    ScrollableTabRow(
        selectedTabIndex = selectedTab.intValue,
        //contentColor = Color.Gray,
        edgePadding = 3.dp,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                color = Orange,
                modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab.intValue])
            )
        },
        divider = { Divider(color = Color.Transparent) }


    ) {
        preferences.forEachIndexed { index, text ->
            val isSelected = selectedTab.intValue == index
            Tab(
                selected = isSelected,
                onClick = {
                    selectedTab.intValue = index
                },
                modifier = Modifier.padding(horizontal = 5.dp)
            ) {
                Text(
                    text = text,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (isSelected) Orange else Color.Gray,
                    modifier = Modifier.padding(top = 18.dp, bottom = 5.dp, start = 7.dp, end = 7.dp),
                )

            }
        }
    }
}


@Composable
fun StaycationListingCard(staycation: Staycation, cardHeight: Dp, imageHeight: Dp){
    Card(
        modifier = Modifier
            //.height(165.dp)
            .height(cardHeight)
            .width(171.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            Box {
                Image(
                    modifier = Modifier
                        //.height(103.dp)
                        .height(imageHeight)
                        .fillMaxWidth(),
                    painter = painterResource(id = staycation.image),
                    contentDescription = "Staycation Unit",
                    contentScale = ContentScale.Crop
                )
                FavoriteButton(
                    Modifier
                        .offset(x = (-7).dp, y = (-9).dp)
                        .width(14.dp)
                )
            }
            Column(
                modifier = Modifier
                    .padding(vertical = 3.dp, horizontal = 8.dp)
            ) {
                Row {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth(0.6f),
                        text = "Staycation hosted by " + staycation.host,
                        fontWeight = FontWeight.Medium,
                        fontSize = 11.sp,
                        lineHeight = 11.sp,
                        color = Color(0xFF333333)
                    )
                    Row(
                        modifier = Modifier.padding(top = 1.dp, start = 36.dp)
                    ) {
                        Icon(
                            modifier = Modifier.height(13.dp),
                            painter = painterResource(id = R.drawable.star),
                            contentDescription = "Star"
                        )
                        Text(
                            text = staycation.rating.toString(), // PLACEHOLDER
                            fontWeight = FontWeight.Medium,
                            fontSize = 9.sp,
                            color = Color(0xFF333333)
                        )
                    }
                }
                Text(
                    text = staycation.location,
                    fontWeight = FontWeight.Medium,
                    fontSize = 9.sp,
                    color = Color(0xFF727272)
                )
                Text(
                    text = "P" + staycation.price.toString() + "/night",
                    fontWeight = FontWeight.Medium,
                    fontSize = 9.sp,
                    color = Color(0xFF727272)
                )
            }


        }
    }
}



@Composable
fun FavoriteButton(modifier: Modifier = Modifier) {

    var isFavorite by remember { mutableStateOf(false) }

    IconToggleButton(
        modifier = modifier,
        checked = isFavorite,
        onCheckedChange = {
            isFavorite = !isFavorite
        }
    ) {
        Image(
            imageVector = ImageVector.vectorResource(id = R.drawable.favorite_off),
            contentDescription = "",
            colorFilter = if(isFavorite) ColorFilter.tint(Color.Red) else null

        )
    }
}

@Composable
fun StaggeredGridListing(staycationList: List<Staycation>) {

    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2), // Number of columns
        contentPadding = PaddingValues(16.dp),
        verticalItemSpacing = 16.dp,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(staycationList) { staycation ->

            var cardHeight = (165..200).random()
            var imageHeight = cardHeight - 62

            // Step 2: Create a Composable for a single grid item
            StaycationListingCard(
                staycation = staycation,
                cardHeight = cardHeight.dp,
                imageHeight = imageHeight.dp
            )
        }
    }
}


@Preview
@Composable
private fun StaycationdetailsPreview() {

    HomeScreen()

}

