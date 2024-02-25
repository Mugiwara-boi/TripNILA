package com.example.tripnila.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.twotone.Close
import androidx.compose.material.icons.twotone.MoreVert
import androidx.compose.material.icons.twotone.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SelectableChipBorder
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
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
import java.text.NumberFormat


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

    val searchText = remember{ mutableStateOf("") }
    val isActive = remember { mutableStateOf(false) }
//    val paddingValues = if (isActive.value) {
//        PaddingValues(0.dp)
//    } else {
//        PaddingValues(top = 8.dp, start = 10.dp, end = 10.dp)
//    }

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
                        Icon(
                            imageVector = Icons.TwoTone.Search,
                            contentDescription = "Search",
                            tint = Color(0xFF333333),
                        )
                    },
                    trailingIcon = {
                        if(isActive.value){
                            Icon(
                                imageVector = Icons.TwoTone.Close,
                                contentDescription = "Close",
                                tint = Color(0xFF333333),
                                modifier = Modifier.clickable {
                                    isActive.value = false
                                    searchText.value = ""
                                }
                            )
                        }
                        else{
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.filter),
                                contentDescription = "Close",
                                tint = Color(0xFF333333),
                                modifier = Modifier.size(26.dp)
                            )
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
                        .padding(horizontal = 5.dp)
                        .fillMaxWidth()
                      //  .padding(paddingValues)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.White)
                    ) {

                    }
                }
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
                            "For You" -> homeViewModel.forYouPagingData.collectAsLazyPagingItems()
                            "Sports" -> homeViewModel.sportsPagingData.collectAsLazyPagingItems()
                            "Food Trip" -> homeViewModel.foodTripPagingData.collectAsLazyPagingItems()
                            "Shop" -> homeViewModel.shopPagingData.collectAsLazyPagingItems()
                            "Nature" -> homeViewModel.naturePagingData.collectAsLazyPagingItems()
                            "Gaming" -> homeViewModel.gamingPagingData.collectAsLazyPagingItems()
                            "Karaoke" -> homeViewModel.karaokePagingData.collectAsLazyPagingItems()
                            "History" -> homeViewModel.historyPagingData.collectAsLazyPagingItems()
                            "Clubs" -> homeViewModel.clubsPagingData.collectAsLazyPagingItems()
                            "Sightseeing" -> homeViewModel.sightseeingPagingData.collectAsLazyPagingItems()
                            "Swimming" -> homeViewModel.swimmingPagingData.collectAsLazyPagingItems()
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
                        text = service.serviceTitle,
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
                if (service.tourDuration != null ) {
                    Text(
                        text = "₱ ${"%.2f".format(service.price)}/head   •   ${service.tourDuration} hours",
                        fontWeight = FontWeight.Medium,
                        fontSize = 9.sp,
                        color = Color(0xFF727272)
                    )
                } else {
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountFilterChip(
    modifier: Modifier = Modifier,
    text: String,
    selected: Boolean,
    onSelectedChange: (Boolean) -> Unit
) {

    FilterChip(
        onClick = {
            onSelectedChange(!selected)
        },
        label = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(x = (-3).dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(text = text)
            }
        },
        colors = FilterChipDefaults.filterChipColors(
            containerColor = Color.White,
            selectedLabelColor = Orange,
            selectedLeadingIconColor = Color.White,
            selectedContainerColor = Color.White,

        ),
        selected = selected,
        shape = RoundedCornerShape(30.dp),
        border = FilterChipDefaults.filterChipBorder(
            borderColor = Color(0xffC2C2C2),
            selectedBorderColor = Orange,
            borderWidth = 1.dp,
            selectedBorderWidth = 1.5.dp
        ),
        modifier = modifier.width(60.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TypeFilterChip(
    text: String,
    selected: Boolean,
    onSelectedChange: (Boolean) -> Unit
) {
//    var selected by remember { mutableStateOf(false) }

    FilterChip(
        onClick = {
           // selected = !selected
            onSelectedChange(!selected) // Call the lambda to return the selected state
        },
        label = {
            Text(text) // Use the provided text label
        },
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = Orange,
            selectedLabelColor = Color.White,
            selectedLeadingIconColor = Color.White
        ),
        selected = selected,
        leadingIcon = if (selected) {
            {
                Icon(
                    imageVector = Icons.Filled.Done,
                    contentDescription = "Done icon",
                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                )
            }
        } else {
            null
        },
    )
}

@Composable
fun PriceTextField(
    modifier: Modifier = Modifier,
    initialValue: Int,
    onValueChange: (Int) -> Unit
) {

    var text by remember { mutableStateOf(initialValue.toString()) }
    var isFocused by remember { mutableStateOf(false) }
    val localFocusManager = LocalFocusManager.current

//    val inputAmount = text
//
//    val formattedAmount = NumberFormat.getNumberInstance().format(inputAmount)

    BasicTextField(
        value = text,
        onValueChange = { newValue ->
            val parsedValue = newValue.filter { it.isDigit() }
            text = parsedValue
            val intValue = parsedValue.toIntOrNull() ?: 0
            onValueChange(intValue)
        },
        textStyle = TextStyle(
            fontWeight = FontWeight.Medium,
            color = Color.Black
        ),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Number
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                localFocusManager.clearFocus()
            }
        ),
        singleLine = true,
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier
                    .background(color = Color.White, shape = RoundedCornerShape(size = 10.dp))
                    .border(
                        width = 2.dp,
                        color = if (isFocused) Orange else Color(0xFFC2C2C2),
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
        },
        modifier = modifier
            .width(128.dp)
            .onFocusChanged { focusState ->
                isFocused = focusState.isFocused
            }
    )
}

@Composable
fun LocationTextField(
    modifier: Modifier = Modifier,
    initialValue: String,
    onValueChange: (String) -> Unit
) {

    var text by remember { mutableStateOf(initialValue) }
    var isFocused by remember { mutableStateOf(false) }
    val localFocusManager = LocalFocusManager.current

    BasicTextField(
        value = text,
        onValueChange = { newValue ->
            text = newValue
            onValueChange(newValue)
        },
        textStyle = TextStyle(
            fontWeight = FontWeight.Medium,
            color = Color.Black
        ),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Text
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                localFocusManager.clearFocus()
            }
        ),
        singleLine = true,
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier
                    .background(color = Color.White, shape = RoundedCornerShape(size = 10.dp))
                    .border(
                        width = 2.dp,
                        color = if (isFocused) Orange else Color(0xFFC2C2C2),
                        shape = RoundedCornerShape(size = 10.dp)
                    )
                    .padding(all = 8.dp), // inner padding
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (text.isEmpty()) {
                    Text(
                        text = "City",
                        color = Color(0xFFC2C2C2),
                        fontWeight = FontWeight.Medium,
                    )
                }
                innerTextField()
            }
        },
        modifier = modifier
            .width(128.dp)
            .onFocusChanged { focusState ->
                isFocused = focusState.isFocused
            }
    )
}

@Composable
fun NumberTextField(
    modifier: Modifier = Modifier,
    initialValue: String,
    onValueChange: (String) -> Unit
) {

    var text by remember { mutableStateOf(initialValue) }
    var isFocused by remember { mutableStateOf(false) }
    val localFocusManager = LocalFocusManager.current

    BasicTextField(
        value = text,
        onValueChange = { newValue ->
            text = newValue
            onValueChange(newValue)
        },
        textStyle = TextStyle(
            fontWeight = FontWeight.Medium,
            color = Color.Black
        ),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Number
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                localFocusManager.clearFocus()
            }
        ),
        singleLine = true,
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier
                    .background(color = Color.White, shape = RoundedCornerShape(size = 10.dp))
                    .border(
                        width = 2.dp,
                        color = if (isFocused) Orange else Color(0xFFC2C2C2),
                        shape = RoundedCornerShape(size = 10.dp)
                    )
                    .padding(all = 8.dp), // inner padding
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (text.isNullOrEmpty()) {
                    Text(
                        text = "Person",
                        color = Color(0xFFC2C2C2),
                        fontWeight = FontWeight.Medium,
                    )
                }
                innerTextField()
            }
        },
        modifier = modifier
            .width(128.dp)
            .onFocusChanged { focusState ->
                isFocused = focusState.isFocused
            }
    )
}

@Composable
fun AmenityCheckboxItem(
    modifier: Modifier = Modifier,
    amenityName: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .height(20.dp)
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(
                checkedColor = Orange
            ),
        )
        Text(
            text = amenityName,
            color = Color(0xff666666),
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp,
        )
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun HomeScreenPreview() {
//    val homeViewModel = viewModel(modelClass = HomeViewModel::class.java)
//
//    HomeScreen("ITZbCFfF7Fzqf1qPBiwx", homeViewModel, { a,b -> }, rememberNavController())

    val searchText = remember{ mutableStateOf("") }
    val isActive = remember { mutableStateOf(true) }

    var anySelected by remember { mutableStateOf(false) }
    var roomSelected by remember { mutableStateOf(false) }
    var entireHomeSelected by remember { mutableStateOf(false) }

    var isEcoFriendly by remember { mutableStateOf(false) }

    var minPrice by remember { mutableDoubleStateOf(0.0) }
    var maxPrice by remember { mutableDoubleStateOf(100000.0) }

    var city by remember { mutableStateOf("") }
    var capacity by remember { mutableStateOf("")}

    var bedroomCount by remember { mutableStateOf("Any") }
    var bedCount by remember { mutableStateOf("Any") }
    var bathroomCount by remember { mutableStateOf("Any") }


    val allAmenities = listOf("Swimming Pool", "Gym", "Parking", "Wi-Fi", "Restaurant", "Spa", "Bar", "Business Center")
    val checkedAmenities = remember { mutableStateListOf<Boolean>() }

    if (checkedAmenities.isEmpty()) {
        repeat(allAmenities.size) {
            checkedAmenities.add(false)
        }
    }

    val onAmenityCheckedChange: (Int, Boolean) -> Unit = { index, isChecked ->
        checkedAmenities[index] = isChecked
    }


    var showAllAmenities by remember { mutableStateOf(false) }

    val visibleAmenities = if (showAllAmenities) allAmenities else allAmenities.take(6)

    val toggleShowAllAmenities: () -> Unit = {
        showAllAmenities = !showAllAmenities
    }

    val toggleShowLessAmenities: () -> Unit = {
        showAllAmenities = false
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Scaffold(

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
                        Icon(
                            imageVector = Icons.TwoTone.Search,
                            contentDescription = "Search",
                            tint = Color(0xFF333333),
                        )
                    },
                    trailingIcon = {
                        if(isActive.value){
                            Icon(
                                imageVector = Icons.TwoTone.Close,
                                contentDescription = "Close",
                                tint = Color(0xFF333333),
                                modifier = Modifier.clickable {
                                    isActive.value = false
                                    searchText.value = ""
                                }
                            )
                        }
                        else{
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.filter),
                                contentDescription = "Close",
                                tint = Color(0xFF333333),
                                modifier = Modifier.size(26.dp)
                            )
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
                        dividerColor = Color.Transparent,
                    ),
                    tonalElevation = 10.dp,
                    modifier = Modifier
                        .padding(horizontal = 5.dp)
                        .fillMaxWidth()
                    //  .padding(paddingValues)
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.White)
                            .padding(horizontal = 16.dp)

                    ) {
                        item {
                            // ------------------------------------------------------
                            // STAYCATION TYPE
                            Text(
                                text = "Type of Staycation",
                                color = Color.Black,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(top = 20.dp)
                            )
                            Row(
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 5.dp, bottom = 5.dp)
                            ) {
                                TypeFilterChip(
                                    text = "Any",
                                    selected = anySelected,
                                    onSelectedChange = { selected ->
                                        if (selected) {
                                            anySelected = true
                                            roomSelected = false
                                            entireHomeSelected = false
                                        }
                                    }
                                )
                                TypeFilterChip(
                                    text = "Room",
                                    selected = roomSelected,
                                    onSelectedChange = { selected ->
                                        anySelected =
                                            false // Unselect "Any" when Room chip is selected
                                        roomSelected = selected
                                    }
                                )
                                TypeFilterChip(
                                    text = "Entire Home",
                                    selected = entireHomeSelected,
                                    onSelectedChange = { selected ->
                                        anySelected =
                                            false // Unselect "Any" when Entire Home chip is selected
                                        entireHomeSelected = selected
                                    }
                                )
                            }
                        }

                        item {
                            // ------------------------------------------------------
                            // ECO FRIENDLY
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .padding(
                                        top = 1.dp,
                                        bottom = 8.dp
                                    )
                                    .height(20.dp)
                            ) {
                                Checkbox(
                                    checked = isEcoFriendly,
                                    onCheckedChange = { newValue ->
                                        isEcoFriendly = newValue
                                    },
                                    colors = CheckboxDefaults.colors(
                                        checkedColor = Orange
                                    ),
                                )
                                Text(
                                    text = "Eco-friendly badge",
                                    color = Color(0xff666666),
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 14.sp,
                                )
                            }
                        }
                        item {
                            // ------------------------------------------------------
                            // PRICE RANGE
                            Text(
                                text = "Price range",
                                color = Color.Black,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Row(
                                horizontalArrangement = Arrangement.SpaceAround,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column {
                                    Text(
                                        text = "Minimum",
                                        color = Color.Black,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium,
                                        modifier = Modifier.offset(x = (-11).dp)
                                    )
                                    PriceTextField(
                                        initialValue = minPrice.toInt(),
                                        onValueChange = { updatedPrice ->
                                            minPrice = updatedPrice.toDouble()
                                            Log.d("Min Price", updatedPrice.toString())
                                        }
                                    )
                                }
                                Column {
                                    Text(
                                        text = "Maximum",
                                        color = Color.Black,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium,
                                        modifier = Modifier.offset(x = (-11).dp)
                                    )
                                    PriceTextField(
                                        initialValue = maxPrice.toInt(),
                                        onValueChange = { updatedPrice ->
                                            maxPrice = updatedPrice.toDouble()
                                            Log.d("Max Price", updatedPrice.toString())
                                        }
                                    )
                                }
                            }

                        }

                        item {
                            // ------------------------------------------------------
                            // LOCATION + CAPACITY

                            Row(
                                horizontalArrangement = Arrangement.SpaceAround,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column {
                                    Text(
                                        text = "Location",
                                        color = Color.Black,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Medium,
                                        modifier = Modifier.offset(x = (-22).dp)
                                    )
                                    LocationTextField(
                                        initialValue = city,
                                        onValueChange = { newCity ->
                                            city = newCity
                                            Log.d("City", newCity)
                                        }
                                    )
                                }
                                Column {
                                    Text(
                                        text = "Capacity",
                                        color = Color.Black,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Medium,
                                        modifier = Modifier.offset(x = (-22).dp)
                                    )
                                    NumberTextField(
                                        initialValue = capacity,
                                        onValueChange = { newCapacity ->
                                            capacity = newCapacity
                                            Log.d("Capacity", newCapacity)
                                        }
                                    )
                                }
                            }
                            // ------------------------------------------------------
                            // BEDROOM AND BEDS

                            Text(
                                text = "Bedroom and beds",
                                color = Color.Black,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )

                            // BEDROOM
                            Text(
                                text = "Bedroom",
                                color = Color.Black,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier
                                    .padding(horizontal = 11.dp)
                            )
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                val bedroomCounts = listOf("Any", "1", "2", "3", "4")

                                bedroomCounts.forEachIndexed { index, bedroomCountOption ->
                                    CountFilterChip(
                                        text = bedroomCountOption,
                                        selected = bedroomCount == bedroomCountOption,
                                        onSelectedChange = { selected ->
                                            if (selected) {
                                                bedroomCount = bedroomCountOption
                                            }
                                        }
                                    )
                                }
                            }


                            // BEDS
                            Text(
                                text = "Beds",
                                color = Color.Black,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier
                                    .padding(horizontal = 11.dp)
                            )
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                val bedCounts = listOf("Any", "1", "2", "3", "4")

                                bedCounts.forEachIndexed { index, bedCountOption ->
                                    CountFilterChip(
                                        text = bedCountOption,
                                        selected = bedCount == bedCountOption,
                                        onSelectedChange = { selected ->
                                            if (selected) {
                                                bedCount = bedCountOption
                                            }
                                        },
                                    )
                                }
                            }

                            // BATHROOM
                            Text(
                                text = "Bathroom",
                                color = Color.Black,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier
                                    .padding(horizontal = 11.dp)
                            )
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                val bathroomCounts = listOf("Any", "1", "2", "3", "4")

                                bathroomCounts.forEachIndexed { index, bathroomCountOption ->
                                    CountFilterChip(
                                        text = bathroomCountOption,
                                        selected = bathroomCount == bathroomCountOption,
                                        onSelectedChange = { selected ->
                                            if (selected) {
                                                bathroomCount = bathroomCountOption
                                            }
                                        }
                                    )
                                }
                            }

                        }


                        item {
                            // ------------------------------------------------------
                            // AMENITIES

                            Text(
                                text = "Amenities",
                                color = Color.Black,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )

                            LazyVerticalGrid(
                                columns = GridCells.Fixed(2),
                                userScrollEnabled = false,
                                modifier = Modifier.fillMaxWidth().height(120.dp),
                            ) {
                                items(visibleAmenities) { amenity ->
                                    AmenityCheckboxItem(
                                        amenityName = amenity,
                                        checked = checkedAmenities[allAmenities.indexOf(amenity)],
                                        onCheckedChange = { isChecked ->
                                            onAmenityCheckedChange(allAmenities.indexOf(amenity), isChecked)
                                        },
                                        modifier = Modifier.height(25.dp)
                                    )
                                }
                                item {
                                    Text(
                                        text = if (showAllAmenities) "Show Less" else "Show More",
                                        color = Color.Black,
                                        fontSize = 12.sp,
                                        textDecoration = TextDecoration.Underline,
                                        modifier = Modifier
                                            .padding(horizontal = 15.dp)
                                            .padding(top = 5.dp)
                                            .clickable { toggleShowAllAmenities() }
                                    )
                                }
                            }
                        }

                        item {
                            Text(
                                text = "Choose Dates",
                                color = Color.Black,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )

                            CalendarComposable()

                        }

                    }
                }
            }
        }
    }
}




