package com.example.tripnila.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Close
import androidx.compose.material.icons.twotone.MoreVert
import androidx.compose.material.icons.twotone.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
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
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.tripnila.R
import com.example.tripnila.common.Orange
import com.example.tripnila.common.TouristBottomNavigationBar
import com.example.tripnila.data.Staycation
import com.example.tripnila.model.HomeViewModel


@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    touristId: String,
    homeViewModel: HomeViewModel? = null,
    onNavToDetailScreen: (String, String) -> Unit,
    navController: NavHostController
){

    LaunchedEffect(touristId) {
        homeViewModel?.getUserPreference(touristId)
    }

    var searchText = remember{ mutableStateOf("") }
    var isActive = remember { mutableStateOf(false) }
    var selectedItemIndex by rememberSaveable {
        mutableStateOf(0)
    }

    val selectedTab = homeViewModel?.selectedTab?.collectAsState()


    val paddingValues = if (isActive.value) {
        PaddingValues(0.dp)
    } else {
        PaddingValues(top = 8.dp, start = 10.dp, end = 10.dp)
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
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
            }
        ) {
            Column(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize()
            ) {
                //TripNilaIcon(modifier = Modifier.offset(x = (-15).dp, y = (-7).dp))

                SearchBar(
                    query = searchText.value,
                    onQueryChange = { searchText.value = it },
                    onSearch = { isActive.value = false },
                    active = isActive.value,
                    onActiveChange = { isActive.value = it },
                    //shape = RoundedCornerShape(20.dp),
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
                            fontSize = 16.sp,
                            modifier = Modifier

                        )
                    },
                    colors = SearchBarDefaults.colors(
                        containerColor = Color(0xFFDFDFDF).copy(0.5f),

                    ),
                    tonalElevation = 10.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(paddingValues)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.White)
                    ) {

                    }
                }
                homeViewModel?.let { viewModel -> PreferenceTabRow(viewModel) }

                val staycations = homeViewModel?.getStaycationsByTab(selectedTab?.value ?: "For You")?.collectAsLazyPagingItems()

                staycations?.let { staycations ->
                    StaggeredGridListing(
                        staycationList = staycations,
                        onItemClick = { staycationId ->
                            onNavToDetailScreen(touristId, staycationId)
                        }
                    )
                }




            }
        }
    }
}




@Composable
fun PreferenceTabRow(
    homeViewModel: HomeViewModel,
   // selectedTab: String
) {
    val preferences = listOf("For You", "Sports", "Food Trip", "Shop", "Nature", "Gaming", "Karaoke", "History", "Clubs", "Sightseeing", "Swimming")

    // Observe the selected tab from the HomeViewModel
    val selectedTab by homeViewModel.selectedTab.collectAsState()

    ScrollableTabRow(
        selectedTabIndex = preferences.indexOf(selectedTab),
        edgePadding = 3.dp,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                color = Orange,
                modifier = Modifier.tabIndicatorOffset(tabPositions[preferences.indexOf(selectedTab)])
            )
        },
        divider = { Divider(color = Color.Transparent) }
    ) {
        preferences.forEach { text ->
            val isSelected = selectedTab == text
            Tab(
                selected = isSelected,
                onClick = {
                    homeViewModel.selectTab(text)

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
fun StaggeredGridListing(
    staycationList: LazyPagingItems<Staycation>,
    onItemClick: (String) -> Unit
) {

    val loadState = staycationList?.loadState

    Column(
        Modifier.fillMaxWidth()
    ) {
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(2), // Number of columns
            contentPadding = PaddingValues(16.dp),
            verticalItemSpacing = 16.dp,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            items(staycationList.itemCount) { index ->

                val staycation = staycationList[index]

                if (staycation != null) {
                    var cardHeight = (165..200).random()
                    var imageHeight = cardHeight - 62

                    // Step 2: Create a Composable for a single grid item
                    StaycationListingCard(
                        staycation = staycation,
                        cardHeight = cardHeight.dp,
                        imageHeight = imageHeight.dp,
                        onItemClick = {
                            onItemClick.invoke(staycation.staycationId)
                        }
                    )
                }
            }

        }

        if (loadState?.refresh is LoadState.Loading) {

            CircularProgressIndicator(
                modifier = Modifier
                    .size(50.dp)
                    .padding(16.dp)
                    .align(CenterHorizontally)
            )

        }
    }


}

@Composable
fun StaycationListingCard(
    staycation: Staycation,
    cardHeight: Dp,
    imageHeight: Dp,
    onItemClick: () -> Unit
){
    Card(
        modifier = Modifier
            //.height(165.dp)
            .height(cardHeight)
            .width(171.dp)
            .clickable { onItemClick.invoke() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            Box {
                Image(
                    modifier = Modifier
                        //.height(103.dp)
                        .height(imageHeight)
                        .fillMaxWidth(),
                    painter = painterResource(id = R.drawable.image_placeholder),
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
                       // text = "Staycation hosted by " + staycation.hostFirstName,
                        text = "Staycation hosted by " + staycation.host.firstName,
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
                            text = staycation.averageReviewRating.toString(), // PLACEHOLDER
                            fontWeight = FontWeight.Medium,
                            fontSize = 9.sp,
                            color = Color(0xFF333333)
                        )
                    }
                }
                Text(
                    text = staycation.staycationLocation,
                    fontWeight = FontWeight.Medium,
                    fontSize = 9.sp,
                    color = Color(0xFF727272)
                )
                Text(
                    text = "₱ ${"%.2f".format(staycation.staycationPrice)}/night",
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



@Preview
@Composable
private fun StaycationdetailsPreview() {
//    val homeViewModel = viewModel(modelClass = HomeViewModel::class.java)
//
//    HomeScreen("", homeViewModel)

}

