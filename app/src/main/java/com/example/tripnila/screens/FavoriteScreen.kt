package com.example.tripnila.screens

import android.util.Log
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.example.tripnila.R
import com.example.tripnila.common.AppDropDownFilter
import com.example.tripnila.common.Orange
import com.example.tripnila.common.Tag
import com.example.tripnila.common.TouristBottomNavigationBar
import com.example.tripnila.data.HomePagingItem
import com.example.tripnila.model.FavoriteViewModel
import com.example.tripnila.model.HomeViewModel
import com.example.tripnila.model.HostTourViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteScreen(
    touristId: String,
    favoriteViewModel: FavoriteViewModel,
    onBack: () -> Unit,
    onNavToDetailScreen: (String, String) -> Unit,
    onNavToTourDetails: (String, String) -> Unit
) {

    val currentUserId by favoriteViewModel.touristId.collectAsState()

    LaunchedEffect(touristId) {
        if (touristId != currentUserId) {
            favoriteViewModel.setTouristId(touristId)
            favoriteViewModel.refreshFavoritePagingData()
        }


    }

    val lazyPagingItems = favoriteViewModel.favoritePagingData.collectAsLazyPagingItems()
    val scope = rememberCoroutineScope()

    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Scaffold(
            topBar = {
                Surface(
                    shadowElevation = 10.dp
                ) {
                    TopAppBar(
                        navigationIcon = {
                            IconButton(onClick = {
                                onBack()
                            }) {
                                Icon(
                                    imageVector = Icons.Filled.ArrowBack,
                                    contentDescription = "Back",
                                    tint = Color(0xFF999999)
                                )
                            }

                        },
                        title = {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp),
                                verticalAlignment = Alignment.CenterVertically

                            ) {
                                Text(
                                    text = "Favorite",
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Medium
                                )
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        },
                    )
                }

            },
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                LazyVerticalGrid(
                    state = rememberLazyGridState(),
                    columns = GridCells.Fixed(2), // Number of columns
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                ) {



                    items(lazyPagingItems.itemCount) { index ->
                        val service = lazyPagingItems[index]
                        val cardHeight = (150..180).random()
                        val imageHeight = cardHeight - 55
                        // Step 2: Create a Composable for a single grid item
                        if (service != null) {
                            ServiceListingCard(
                                service = service,
                                favoriteViewModel = favoriteViewModel,
                                imageHeight = imageHeight.dp,
                                scope = scope,
                                onItemClick = { serviceType ->
                                    if (serviceType == "Tour") {
                                        onNavToTourDetails(touristId, service.serviceId)
                                    } else {
                                        onNavToDetailScreen(touristId, service.serviceId)
                                    }
                                }
                            )
                        }
                    }
                }

                if (lazyPagingItems.loadState.refresh is LoadState.Loading) {
                    CircularProgressIndicator(
                        color = Orange,
                        modifier = Modifier
                            .size(50.dp)
                            .padding(16.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                }
            }

        }
    }
}

@Composable
fun ServiceListingCard(
    service: HomePagingItem,
    favoriteViewModel: FavoriteViewModel,
    imageHeight: Dp,
    scope: CoroutineScope,
    onItemClick: (String) -> Unit
){

    val serviceImage = service.serviceCoverPhoto //?: "https://upload.wikimedia.org/wikipedia/commons/thumb/3/3f/Placeholder_view_vector.svg/1022px-Placeholder_view_vector.svg.png"
    val touristId by favoriteViewModel.touristId.collectAsState()

    var isFavorite by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(touristId) {
        isFavorite = favoriteViewModel.isFavorite(service.serviceId, touristId)
        Log.d("First", "LE")
    }


    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onItemClick(service.serviceType)
            },
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
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 7.dp, end = 7.dp)
                ) {
                    FavoriteButton(
                        isFavorite = isFavorite,
                        onCheckedChange = { isChecked ->

                            scope.launch {
                                favoriteViewModel.toggleFavorite(
                                    service.serviceId,
                                    touristId,
                                    service.serviceType
                                )
                                isFavorite = isChecked
                            }

                        },
                        modifier = Modifier
                            .offset(y = (-9).dp)
                            .width(16.dp)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Tag(
                        tag = service.serviceType,
                        modifier = Modifier.padding(top = 3.dp)
                    )

                }
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






@Preview
@Composable
private fun FavoriteScreenPreview() {

    val favoriteViewModel = viewModel(modelClass = FavoriteViewModel::class.java)
    val touristId = "mgPPHdYnYlJXMFxCaJOj"


//    FavoriteScreen(
//        favoriteViewModel = favoriteViewModel,
//        touristId = touristId,
//        onBack = {
//
//        }
//    )

}