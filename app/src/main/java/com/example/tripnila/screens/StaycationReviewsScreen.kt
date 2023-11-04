package com.example.tripnila.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
import com.example.tripnila.common.AppDropDownFilter
import com.example.tripnila.common.Orange
import com.example.tripnila.data.FullReview
import kotlin.math.roundToInt

@Composable
fun StaycationReviewsScreen(){

    val fullReviews = listOf(
        FullReview(
            image = R.drawable.joshua,
            firstName = "Joshua",
            reviewDate = "04 April 2023",
            rating = 5,
            reviewComment = "Everything was perfect! definitely recommend this staycation in Pasig!!",
            reviewImage1 = R.drawable.staycation1,
            reviewImage2 = R.drawable.staycation1,
            isAlreadyLiked = false,
            initialLikesCount = 5,
            initialCommentsCount = 5,
        ),
        FullReview(
            image = R.drawable.joshua,
            firstName = "Joshua",
            reviewDate = "04 April 2023",
            rating = 5,
            reviewComment = "Everything was perfect! definitely recommend this staycation in Pasig!!",
            reviewImage1 = R.drawable.staycation1,
            reviewImage2 = R.drawable.staycation1,
            isAlreadyLiked = false,
            initialLikesCount = 2,
            initialCommentsCount = 5,
        ),
        FullReview(
            image = R.drawable.joshua,
            firstName = "Joshua",
            reviewDate = "04 April 2023",
            rating = 5,
            reviewComment = "Everything was perfect! definitely recommend this staycation in Pasig!!",
            reviewImage1 = R.drawable.staycation1,
            reviewImage2 = R.drawable.staycation1,
            isAlreadyLiked = true,
            initialLikesCount = 5,
            initialCommentsCount = 8,
        ),
        FullReview(
            image = R.drawable.joshua,
            firstName = "Joshua",
            reviewDate = "04 April 2023",
            rating = 5,
            reviewComment = "Everything was perfect! definitely recommend this staycation in Pasig!!",
            reviewImage1 = R.drawable.staycation1,
            reviewImage2 = R.drawable.staycation1,
            isAlreadyLiked = false,
            initialLikesCount = 12,
            initialCommentsCount = 5,
        )
    )

    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Scaffold(
            topBar = {
                Surface(
                    shadowElevation = 10.dp
                ) {
                    StaycationReviewsTopBar(
                        averageRating = 4.7,
                        totalReviews = 230
                    )
                }

            },
            bottomBar = {
                StaycationBottomBookingBar()
            }
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                item {
                    ReviewsTabRow()
                }
                items(fullReviews) { review ->
                    FullReviewCard(
                        fullReview = review,
                        modifier = Modifier.padding(vertical = 10.dp)
                    )
                }

            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StaycationReviewsTopBar(
    averageRating: Double,
    totalReviews: Int,
    modifier: Modifier = Modifier
){

    TopAppBar(
        navigationIcon = {
            IconButton(onClick = { /*TODO*/ }) {
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
                    text = averageRating.toString(),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Medium
                )
                RatingBar(rating = averageRating.roundToInt())
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "$totalReviews reviews",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF999999),
                    modifier = Modifier.padding(top = 12.dp)
                )
            }
        },
        modifier = modifier
    )

}

@Composable
fun RatingBar(
    modifier: Modifier = Modifier,
    rating: Int
) {
    var ratingState by remember {
        mutableStateOf(rating)
    }

    Row(
       modifier = Modifier.padding(top = 12.dp, start = 8.dp)
    ) {
        for (i in 1..5) {
            Icon(
                painter = painterResource(id = R.drawable.star_rating),
                contentDescription = "star",
                modifier = modifier
                    .width(14.dp)
                    .height(14.dp),
                tint = if (i <= ratingState) Orange else Color(0xFFA2ADB1)
            )
        }
    }
}

@Composable
fun ReviewsTabRow(modifier: Modifier = Modifier){

    val preferences = listOf("All", "With media", "Low ratings")
    val selectedTab = remember { mutableIntStateOf(0) }
    val filter = listOf("Relevance", "Recent", "Top rated")

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 5.dp)
    ) {
        TabRow(
            selectedTabIndex = selectedTab.intValue,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    color = Orange,
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab.intValue])
                )
            },
            divider = { Divider(color = Color.Transparent) },
            modifier = Modifier
                .width(240.dp)
                .padding(vertical = 5.dp)
        ) {
            preferences.forEachIndexed { index, text ->
                val isSelected = selectedTab.intValue == index
                Tab(
                    selected = isSelected,
                    onClick = {
                        selectedTab.intValue = index
                    },
                    modifier = Modifier.padding(horizontal = 8.dp)
                ) {
                    Text(
                        text = text,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (isSelected) Orange else Color(0xFF333333),
                        modifier = Modifier
                            .padding(top = 10.dp, bottom = 5.dp),
                    )

                }
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        AppDropDownFilter(
            options = filter,
            modifier = Modifier
                .padding(
                    top = 13.dp,
                    end = 14.dp
                )
        )
    }

}

@Composable
fun FullReviewCard(
    fullReview: FullReview,
    modifier: Modifier = Modifier
){
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp
        ),
        modifier = modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(
//                    horizontal = 25.dp,
//                    vertical = 20.dp // 12
                    start = 20.dp,
                    end = 20.dp,
                    top = 10.dp,
                    bottom = 10.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(100))
                        .size(25.dp)
                ){
                    Image(
                        painter = painterResource(id = fullReview.image),
                        contentDescription = fullReview.firstName,
                        contentScale = ContentScale.Crop
                    )
                }
                Column(
                    modifier = Modifier.padding(horizontal = 5.dp)
                ) {
                    Text(
                        text = fullReview.firstName,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,

                        )
                    Text(
                        text = fullReview.reviewDate,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF999999)
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                RatingBar(
                    rating = fullReview.rating,
                    modifier = Modifier.offset(y = (-5).dp)
                )
            }
            Text(
                text = fullReview.reviewComment,
                fontSize = 10.sp,
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .align(Alignment.Start)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),


            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(topStart = 10.dp, bottomStart = 10.dp))
                        .width(90.dp)
                        .height(70.dp)
                ){
                    Image(
                        painter = painterResource(id = fullReview.reviewImage1),
                        contentDescription = "",
                        contentScale = ContentScale.FillHeight
                    )
                }
                Spacer(modifier = Modifier.width(3.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(topEnd = 10.dp, bottomEnd = 10.dp))
                        .width(90.dp)
                        .height(70.dp)
                ){
                    Image(
                        painter = painterResource(id = fullReview.reviewImage2),
                        contentDescription = "",
                        contentScale = ContentScale.FillHeight
                    )
                }

            }
            Divider(modifier = Modifier.padding(top = 10.dp), color = Color(0xFFDEDEDE))
            FullReviewCardBottomBar(
                isAlreadyLiked = fullReview.isAlreadyLiked,
                initialLikesCount = fullReview.initialLikesCount,
                initialCommentsCount = fullReview.initialCommentsCount
            )
        }
    }
}

@Composable
fun FullReviewCardBottomBar(
    isAlreadyLiked: Boolean,
    initialLikesCount: Int,
    initialCommentsCount: Int,
    modifier: Modifier = Modifier
){


    var likesCount by remember {
        mutableIntStateOf(initialLikesCount)
    }
    var commentsCount by remember {
        mutableIntStateOf(initialCommentsCount)
    }
    var isLiked by remember { mutableStateOf(isAlreadyLiked) }
    var hasComment by remember {
        mutableStateOf(false)
    }
    val icon = if (isLiked)
        R.drawable.like_filled
    else
        R.drawable.like_outlined

    Row(
        modifier = modifier
            .padding(top = 5.dp)
            .fillMaxWidth()
            .background(Color.White)
    ) {
        IconButton(
            modifier = Modifier.size(20.dp),
            onClick = {
                isLiked = !isLiked
                likesCount += if (isLiked) 1 else -1
            }
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(icon),
                contentDescription = "Like",
                tint = if (isLiked) Orange else Color(0xFF333333),
                modifier = Modifier.size(16.dp)
            )
        }
        Text(
            text = "$likesCount likes",
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF696969),
            modifier = Modifier.padding(5.dp)
        )
        Spacer(modifier = Modifier.width(5.dp))
        IconButton(
            modifier = Modifier.size(20.dp),
            onClick = {
                hasComment = !hasComment
            }
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.comment),
                contentDescription = "Like",
                tint = Color(0xFF333333),
                modifier = Modifier.size(16.dp)
            )
        }
        Text(
            text = "$commentsCount comments",
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF696969),
            modifier = Modifier.padding(5.dp)
        )
        Spacer(modifier = Modifier.weight(1f))
        IconButton(
            modifier = Modifier.size(20.dp),
            onClick = { /*TODO*/ }
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.more_vertical),
                contentDescription = "",
                modifier = Modifier.size(18.dp)
            )
        }
    }
}




@Preview
@Composable
private fun StaycationReviewsItemPreview(){

    val fullReviews = listOf(
        FullReview(
            image = R.drawable.joshua,
            firstName = "Joshua",
            reviewDate = "04 April 2023",
            rating = 5,
            reviewComment = "Everything was perfect! definitely recommend this staycation in Pasig!!",
            reviewImage1 = R.drawable.staycation1,
            reviewImage2 = R.drawable.staycation1,
            isAlreadyLiked = false,
            initialLikesCount = 5,
            initialCommentsCount = 5,
        )
    )

    //FullReviewCard(fullReviews[0])

//    FullReviewCardBottomBar(
//        isAlreadyLiked = false,
//        initialLikesCount = 6,
//        initialCommentsCount = 5
//    )
}


@Preview
@Composable
private fun StaycationReviewsScreenPreview(){
    StaycationReviewsScreen()
}