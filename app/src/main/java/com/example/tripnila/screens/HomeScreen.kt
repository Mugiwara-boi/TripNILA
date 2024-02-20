package com.example.tripnila.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.listSaver
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.example.tripnila.R
import com.example.tripnila.common.Orange
import com.example.tripnila.common.TouristBottomNavigationBar
import com.example.tripnila.data.HomePagingItem
import com.example.tripnila.model.HomeViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@SuppressLint("StateFlowValueCalledInComposition", "RememberReturnType")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    touristId: String,
    homeViewModel: HomeViewModel,
    onNavToDetailScreen: (String, String) -> Unit,
    navController: NavHostController
){

    LaunchedEffect(touristId) {
        homeViewModel.getUserPreference(touristId)
        homeViewModel.getUniqueServiceIds()
        Log.d("Selected Tab", homeViewModel.selectedTab.value)
    }

    var selectedItemIndex by rememberSaveable {
        mutableIntStateOf(0)
    }

    val preferences = listOf("For You", "Sports", "Food Trip", "Shop", "Nature", "Gaming", "Karaoke", "History", "Clubs", "Sightseeing", "Swimming")

    // Observe the selected tab from the HomeViewModel
    val selectedTab by homeViewModel.selectedTab.collectAsState()
    val serviceIdSet by homeViewModel.serviceIdSet.collectAsState()

    val pagerState = rememberPagerState(
        initialPage = preferences.indexOf(homeViewModel.selectedTab.value),
        initialPageOffsetFraction = 0f
    ) {
        preferences.size
    }
    val scope = rememberCoroutineScope()



    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Scaffold(
            bottomBar = {
                TouristBottomNavigationBar(
                    touristId = touristId,
                    navController = navController,
                    selectedItemIndex = selectedItemIndex,
                    onItemSelected = { newIndex ->
                        selectedItemIndex = newIndex
                    }
                )
            }
        ) {
            Column(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize()
            ) {
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
                    preferences.forEach { tabName ->
                        val isSelected = selectedTab == tabName
                        Tab(
                            selected = isSelected,
                            onClick = {
                                homeViewModel.selectTab(tabName)
                              //  homeViewModel.getServicesByTab(selectedTab)

                                scope.launch {
                                    pagerState.animateScrollToPage(preferences.indexOf(tabName))
                                }
                            },
                            modifier = Modifier.padding(horizontal = 5.dp)
                        ) {
                            Text(
                                text = tabName,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium,
                                color = if (isSelected) Orange else Color.Gray,
                                modifier = Modifier.padding(
                                    top = 18.dp,
                                    bottom = 5.dp,
                                    start = 7.dp,
                                    end = 7.dp
                                ),
                            )
                        }
                    }
                }

                HorizontalPager(
                    state = pagerState,
                    userScrollEnabled = false,
                    modifier = Modifier.fillMaxSize()
                ) { page ->


                    if (serviceIdSet.isNotEmpty()) {



                        val lazyPagingItems = when (selectedTab) {
                            "For You" -> homeViewModel.pagingData.collectAsLazyPagingItems()
                            else -> null
                        }

                        if (lazyPagingItems != null) {
                            Column(
                                modifier = Modifier.fillMaxSize()
                            ) {
                                LazyVerticalGrid(
                                    state = rememberLazyGridState(),
                                    columns = GridCells.Fixed(2), // Number of columns
                                    contentPadding = PaddingValues(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(16.dp),
                                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                                ) {

                                    items(lazyPagingItems.itemCount) { index ->

                                        val service = lazyPagingItems[index]

                                        val cardHeight = (150..180).random()
                                        val imageHeight = cardHeight - 55

                                        // Step 2: Create a Composable for a single grid item
                                        if (service != null) {
                                            ServiceListingCard(
                                                service = service,
                                                cardHeight = cardHeight.dp,
                                                imageHeight = imageHeight.dp,
                                                onItemClick = {
                                                    onNavToDetailScreen.invoke(
                                                        touristId,
                                                        service.serviceId
                                                    )
                                                }
                                            )
                                        }
                                    }

                                }

                                if (lazyPagingItems.loadState.refresh is LoadState.Loading) {
                                    CircularProgressIndicator(
                                        modifier = Modifier
                                            .size(50.dp)
                                            .padding(16.dp)
                                            .align(CenterHorizontally)
                                    )
                                }
                            }
                        }
                    }


                }
                
            }
        }
    }
}


@Composable
fun StaggeredGridListing(
    serviceList: LazyPagingItems<HomePagingItem>,
    onItemClick: (String) -> Unit
) {

    val loadState = serviceList.loadState

  //  val lazyListState = rememberLazyStaggeredGridState()


    Column(
        verticalArrangement = Arrangement.Top,
        modifier = Modifier.fillMaxSize()
    ) {
        LazyVerticalGrid(
            state = rememberLazyGridState(),
            columns = GridCells.Fixed(2), // Number of columns
            contentPadding = PaddingValues(16.dp),
            //verticalItemSpacing = 16.dp,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            items(serviceList.itemCount) { index ->

                val service = serviceList[index]

                val cardHeight = (150..180).random()
                val imageHeight = cardHeight - 55

                // Step 2: Create a Composable for a single grid item
                if (service != null) {
                    ServiceListingCard(
                        service = service,
                        cardHeight = cardHeight.dp,
                        imageHeight = imageHeight.dp,
                        onItemClick = {
                            onItemClick.invoke(service.serviceId)
                        }
                    )
                }
            }

        }

        if (loadState.refresh is LoadState.Loading) {

            CircularProgressIndicator(
                modifier = Modifier
                    .size(50.dp)
                    .padding(16.dp)
                    .align(CenterHorizontally)
            )

        }
    }


}

//@Composable
//fun StaggeredGridListing(
//    staycationList: LazyPagingItems<Staycation>,
//    onItemClick: (String) -> Unit
//) {
//
//    val loadState = staycationList.loadState
//
//    Column(
//        Modifier.fillMaxWidth()
//    ) {
//        LazyVerticalStaggeredGrid(
//            columns = StaggeredGridCells.Fixed(2), // Number of columns
//            contentPadding = PaddingValues(16.dp),
//            verticalItemSpacing = 16.dp,
//            horizontalArrangement = Arrangement.spacedBy(16.dp)
//        ) {
//
//            items(staycationList.itemCount) { index ->
//
//                val staycation = staycationList[index]
//
//                if (staycation != null) {
//                    val cardHeight = (160..190).random()
//                    val imageHeight = cardHeight - 70
//
//                    // Step 2: Create a Composable for a single grid item
//                    StaycationListingCard(
//                        staycation = staycation,
//                        cardHeight = cardHeight.dp,
//                        imageHeight = imageHeight.dp,
//                        onItemClick = {
//                            onItemClick.invoke(staycation.staycationId)
//                        }
//                    )
//                }
//            }
//
//        }
//
//        if (loadState.refresh is LoadState.Loading) {
//
//            CircularProgressIndicator(
//                modifier = Modifier
//                    .size(50.dp)
//                    .padding(16.dp)
//                    .align(CenterHorizontally)
//            )
//
//        }
//    }
//
//
//}


@Composable
fun ServiceListingCard(
    service: HomePagingItem,
    cardHeight: Dp,
    imageHeight: Dp,
    onItemClick: () -> Unit
){

    val serviceImage = service.serviceCoverPhoto //?: "https://upload.wikimedia.org/wikipedia/commons/thumb/3/3f/Placeholder_view_vector.svg/1022px-Placeholder_view_vector.svg.png"

    Card(
        modifier = Modifier
            //  .height(cardHeight)
            .fillMaxWidth()
            // .width(171.dp)
            .clickable { onItemClick.invoke() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )

    ) {
        Column {
            Box {
                AsyncImage(
                    model = serviceImage,
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(imageHeight)

                )
//                Image(
//                    modifier = Modifier
//                        //.height(103.dp)
//                        .height(imageHeight)
//                        .fillMaxWidth(),
//                    painter = painterResource(id = R.drawable.image_placeholder),
//                    contentDescription = "Staycation Unit",
//                    contentScale = ContentScale.Crop
//                )
//                FavoriteButton(
//                    Modifier
//                        .offset(x = (-7).dp, y = (-9).dp)
//                        .width(14.dp)
//                )
            }
            Column(
                modifier = Modifier
                    .padding(top = 7.dp, bottom = 10.dp, start = 8.dp, end = 8.dp)
            ) {
                Row {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth(0.55f),
                        text = service.serviceId,
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
                            text = service.averageReviewRating.toString(), // PLACEHOLDER
                            fontWeight = FontWeight.Medium,
                            fontSize = 9.sp,
                            color = Color(0xFF333333)
                        )
                    }
                }
                Text(
                    text = service.location,
                    fontWeight = FontWeight.Medium,
                    fontSize = 9.sp,
                    color = Color(0xFF727272)
                )
                Text(
                    text = "₱ ${"%.2f".format(service.price)}/night",
                    fontWeight = FontWeight.Medium,
                    fontSize = 9.sp,
                    color = Color(0xFF727272)
                )
            }


        }
    }
}



//@Composable
//fun StaycationListingCard(
//    staycation: Staycation,
//    cardHeight: Dp,
//    imageHeight: Dp,
//    onItemClick: () -> Unit
//){
//
//    val staycationImage = staycation.staycationImages.find { it.photoType == "Cover" }?.photoUrl ?: "https://upload.wikimedia.org/wikipedia/commons/thumb/3/3f/Placeholder_view_vector.svg/1022px-Placeholder_view_vector.svg.png"
//
//    Card(
//        modifier = Modifier
//            //.height(165.dp)
//            .height(cardHeight)
//            .fillMaxWidth()
//            // .width(171.dp)
//            .clickable { onItemClick.invoke() },
//        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
//        colors = CardDefaults.cardColors(
//            containerColor = Color.White
//        )
//
//    ) {
//        Column {
//            Box {
//                AsyncImage(
//                    model = staycationImage,
//                    contentDescription = "",
//                    contentScale = ContentScale.Crop,
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(imageHeight)
//
//                )
////                Image(
////                    modifier = Modifier
////                        //.height(103.dp)
////                        .height(imageHeight)
////                        .fillMaxWidth(),
////                    painter = painterResource(id = R.drawable.image_placeholder),
////                    contentDescription = "Staycation Unit",
////                    contentScale = ContentScale.Crop
////                )
////                FavoriteButton(
////                    Modifier
////                        .offset(x = (-7).dp, y = (-9).dp)
////                        .width(14.dp)
////                )
//            }
//            Column(
//                modifier = Modifier
//                    .padding(vertical = 3.dp, horizontal = 8.dp)
//            ) {
//                Row {
//                    Text(
//                        modifier = Modifier
//                            .fillMaxWidth(0.55f),
//                        text = "Staycation hosted by " + staycation.host.firstName,
//                        fontWeight = FontWeight.Medium,
//                        fontSize = 11.sp,
//                        lineHeight = 11.sp,
//                        color = Color(0xFF333333)
//                    )
//                    Row(
//                        modifier = Modifier.padding(top = 1.dp, start = 36.dp)
//                    ) {
//                        Icon(
//                            modifier = Modifier.height(13.dp),
//                            painter = painterResource(id = R.drawable.star),
//                            contentDescription = "Star"
//                        )
//                        Text(
//                            text = staycation.averageReviewRating.toString(), // PLACEHOLDER
//                            fontWeight = FontWeight.Medium,
//                            fontSize = 9.sp,
//                            color = Color(0xFF333333)
//                        )
//                    }
//                }
//                Text(
//                    text = staycation.staycationLocation,
//                    fontWeight = FontWeight.Medium,
//                    fontSize = 9.sp,
//                    color = Color(0xFF727272)
//                )
//                Text(
//                    text = "₱ ${"%.2f".format(staycation.staycationPrice)}/night",
//                    fontWeight = FontWeight.Medium,
//                    fontSize = 9.sp,
//                    color = Color(0xFF727272)
//                )
//            }
//
//
//        }
//    }
//}



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
    val homeViewModel = viewModel(modelClass = HomeViewModel::class.java)

    HomeScreen("ITZbCFfF7Fzqf1qPBiwx", homeViewModel, { a,b -> }, rememberNavController())

}

