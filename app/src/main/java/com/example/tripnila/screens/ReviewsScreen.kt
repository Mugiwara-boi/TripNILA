package com.example.tripnila.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.tripnila.R
import com.example.tripnila.common.AppDropDownFilter
import com.example.tripnila.common.LoadingScreen
import com.example.tripnila.common.Orange
import com.example.tripnila.data.Comment
import com.example.tripnila.data.FullReview
import com.example.tripnila.model.ReviewViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import kotlin.math.roundToInt

@Composable
fun ReviewsScreen(
    touristId: String,
    serviceId: String,
    serviceType: String,
    reviewViewModel: ReviewViewModel,
    onBack: () -> Unit,
){

    //val staycationReviews = staycation.value?.staycationBookings?.mapNotNull { it.bookingReview }
    LaunchedEffect(touristId) {
        reviewViewModel.setCurrentUser(touristId)
    }


    LaunchedEffect(serviceId) {
        if (serviceType == "Staycation") {
            reviewViewModel.getAllReviewsByStaycationId(serviceId)
        } else {
            reviewViewModel.getAllReviewsByTourId(serviceId)
        }

    }

    val isLoading by reviewViewModel.isLoading.collectAsState()
    val staycationBookings by reviewViewModel.staycationBookings.collectAsState()
    val tourBookings by reviewViewModel.tourBookings.collectAsState()

    val filteredReviews = if (serviceType == "Staycation") {
        staycationBookings
            .mapNotNull { it.bookingReview }
            .filter { it.bookingId != "" }
            .sortedByDescending { it.reviewDate }
    } else {
        tourBookings
            .mapNotNull { it.bookingReview }
            .filter { it.bookingId != "" }
            .sortedByDescending { it.reviewDate }
    }

//    val staycationReviews = staycationBookings
//        .mapNotNull { it.bookingReview }
//        .filter { it.bookingId != "" }
//        .sortedByDescending { it.reviewDate }

    val averageRating = filteredReviews
        .map { it.rating }
        .average()

    val validAverage = if (averageRating.isNaN()) 0.0 else averageRating

    val totalReviews = filteredReviews
        .size


    val fullReviews = filteredReviews.map { review ->
        FullReview(
            image = review.reviewer.profilePicture,
            firstName = review.reviewer.firstName,
            reviewDate = review.reviewDate.toString(),
            rating = review.rating,
            reviewComment = review.comment,
            reviewImages = review.reviewPhotos.map { it.reviewUrl },
            isAlreadyLiked = review.likes.any { like -> like.touristId == touristId },
            initialLikesCount = review.likes.size,
            initialCommentsCount = review.comments.size,
            comments = review.comments,
            reviewId = review.reviewId
        )
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {

        if (isLoading) {
            LoadingScreen(isLoadingCompleted = false, isLightModeActive = true)
        } else {
            Scaffold(
                topBar = {
                    Surface(
                        shadowElevation = 10.dp
                    ) {
                        StaycationReviewsTopBar(
                            averageRating = validAverage,
                            totalReviews = totalReviews,
                            onBack = {
                                onBack()
                            }
                        )
                    }

                },
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it)
                ) {
                    item {
                        ReviewsTabRow()
                    }
//                    if (fullReviews.isNotEmpty()) {
                    items(fullReviews) { review ->
                        FullReviewCard(
                            fullReview = review,
                            modifier = Modifier.padding(vertical = 10.dp),
                            reviewViewModel = reviewViewModel,
                            serviceType = serviceType
                        )
                    }
//                    } else {
//                        item {
//                            CircularProgressIndicator(
//                                color = Orange,
//                                modifier = Modifier
//                                    .size(50.dp)
//                                    .padding(16.dp)
//                                    .wrapContentWidth(Alignment.CenterHorizontally)
//                            )
//                        }
//                    }

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
    onBack: () -> Unit,
    modifier: Modifier = Modifier
){

    TopAppBar(
        navigationIcon = {
            IconButton(onClick = { onBack() }) {
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
                tint = if (i <= rating) Orange else Color(0xFFA2ADB1)
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
    reviewViewModel: ReviewViewModel,
    serviceType: String,
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
                    AsyncImage(
                        model = fullReview.image,
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

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp)
            ) {
                val itemsPerRow = 2
                val rows = (fullReview.reviewImages.size + itemsPerRow - 1) / itemsPerRow

                for (row in 0 until rows) {
                    Row(
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        val startIndex = row * itemsPerRow
                        val endIndex = minOf(startIndex + itemsPerRow, fullReview.reviewImages.size)

                        for (i in startIndex until endIndex) {
                            val imageUrl = fullReview.reviewImages[i]
                            Box(
                                modifier = Modifier
                                    .width(90.dp)
                                    .height(70.dp)
                                    .padding(vertical = 5.dp)
                                    .clip(RoundedCornerShape(10.dp))
                            ) {
                                AsyncImage(
                                    model = imageUrl,
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop
                                )
                            }

                        }
                    }
                }
            }

            Divider(modifier = Modifier.padding(top = 10.dp), color = Color(0xFFDEDEDE))
            FullReviewCardBottomBar(
                isAlreadyLiked = fullReview.isAlreadyLiked,
                initialLikesCount = fullReview.initialLikesCount,
                initialCommentsCount = fullReview.initialCommentsCount,
                comments = fullReview.comments,
                reviewId = fullReview.reviewId,
                reviewViewModel = reviewViewModel,
                serviceType = serviceType
            )



        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FullReviewCardBottomBar(
    isAlreadyLiked: Boolean,
    initialLikesCount: Int,
    initialCommentsCount: Int,
    comments: List<Comment>,
    reviewId: String,
    reviewViewModel: ReviewViewModel,
    serviceType: String,

    modifier: Modifier = Modifier
){

    val modalBottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var isModalBottomSheetVisible by remember { mutableStateOf(false) }

    val icon = if (isAlreadyLiked)
        R.drawable.like_filled
    else
        R.drawable.like_outlined

    Column {
        Row(
            modifier = modifier
                .padding(top = 5.dp)
                .fillMaxWidth()
                .background(Color.White)
        ) {
            IconButton(
                modifier = Modifier.size(20.dp),
                onClick = {
//                    isLiked = !isLiked
//                    likesCount += if (isLiked) 1 else -1
                    reviewViewModel.toggleLike(reviewId, serviceType)
                }
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(icon),
                    contentDescription = "Like",
                    tint = if (isAlreadyLiked) Orange else Color(0xFF333333),
                    modifier = Modifier.size(16.dp)
                )
            }
            Text(
                text = "$initialLikesCount likes",
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF696969),
                modifier = Modifier.padding(5.dp)
            )
            Spacer(modifier = Modifier.width(5.dp))
            IconButton(
                modifier = Modifier.size(20.dp),
                onClick = {
                    isModalBottomSheetVisible = true
                }
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.comment),
                    contentDescription = "Comment",
                    tint = Color(0xFF333333),
                    modifier = Modifier.size(16.dp)
                )
            }
            Text(
                text = "$initialCommentsCount comments",
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

        if (isModalBottomSheetVisible) {
            ModalBottomSheet(
                modifier = Modifier.fillMaxHeight(0.75f),
                sheetState = modalBottomSheetState,
                onDismissRequest = {
                    isModalBottomSheetVisible = false
                },
                dragHandle = {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        //horizontalAlignment = Alignment.End
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 12.dp, end = 12.dp, top = 8.dp)
                        ) {
                            Text(
                                text = "Comments",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(top = 8.dp)
                            )
                            IconButton(
                                onClick = {
                                    isModalBottomSheetVisible = false
                                },
                                modifier = Modifier
                                    .size(30.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Close,
                                    contentDescription = "Close",
                                    tint = Color(0xFFCECECE),
                                    modifier = Modifier.size(30.dp)
                                )
                            }
                        }
                        Divider(color = Color(0xFFDEDEDE), modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp))
                    }
                },
                containerColor = Color.White
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            horizontal = 25.dp,
                        )
                        .background(Color.White)
                ) {

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        items(comments) {comment ->
                            CommentRow(
                                comment = comment.comment,
                                image = if (comment.commenter.profilePicture == "") "https://upload.wikimedia.org/wikipedia/commons/8/89/Portrait_Placeholder.png" else comment.commenter.profilePicture ,//comment.commenterProfile,
                                date = comment.commentDate,
                                name = comment.commenter.firstName, //comment.commenterFirstName
                                modifier = Modifier.padding(bottom = 5.dp)
                            )

                        }
                    }
                    CommentBar(
                        reviewViewModel = reviewViewModel,
                        reviewId = reviewId,
                        serviceType = serviceType
                      //  reviewId = comments.firstOrNull()!!.reviewId
                    )


                    
                }
            }
        }
    }
}

@Composable
fun CommentRow(
    modifier: Modifier = Modifier,
    comment: String,
    image: String,
    date: String,
    name: String
) {

    val formatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss zzz yyyy")
    val providedDate = LocalDateTime.parse(date, formatter)

    // Get the current date and time
    val currentDate = LocalDateTime.now()

    // Calculate the difference
    val hours = ChronoUnit.HOURS.between(providedDate, currentDate)
    val days = ChronoUnit.DAYS.between(providedDate, currentDate)
    val weeks = ChronoUnit.WEEKS.between(providedDate, currentDate)
    val months = ChronoUnit.MONTHS.between(providedDate, currentDate)
    val years = ChronoUnit.YEARS.between(providedDate, currentDate)

    // Determine the appropriate unit to display
    val dateDiff = when {
        years > 0 -> "$years years ago"
        months > 0 -> "$months months ago"
        weeks > 0 -> "$weeks weeks ago"
        days > 0 -> "$days days ago"
        hours > 0 -> "$hours hours ago"
        else -> "recently"
    }


    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(100))
                    .size(40.dp)

            ) {
                AsyncImage(
                    model = image,
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(modifier = Modifier.width(5.dp))
            Surface(
                color = Color(216,216,216),
                shape = RoundedCornerShape(10.dp),
            ) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 5.dp, vertical = 5.dp)
                ) {
                    Text(
                        text = name,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(bottom = 3.dp)
                    )
                    Text(
                        text = comment,
                        fontSize = 12.sp,
                    )
                }
            }
        }
        Text(
            text = dateDiff,
            fontSize = 10.sp,
            modifier = Modifier.padding(start = 50.dp),
        )

    }


}

@Composable
fun CommentBar(
    modifier: Modifier = Modifier,
    reviewViewModel: ReviewViewModel,
    reviewId: String,
    serviceType: String
){

    var text by remember { mutableStateOf("") }
    val localFocusManager = LocalFocusManager.current
    var isTextFieldFocused by remember { mutableStateOf(false) }


    Surface(
        color = Color.White,
        tonalElevation = 10.dp,
        shadowElevation = 10.dp,
        modifier = modifier
            .height(70.dp)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
//                .padding(
//                    horizontal = 15.dp,
//                )
            ,

            verticalAlignment = Alignment.CenterVertically
        ){

            BasicTextField(
                value = text,
                onValueChange = {
                    text = it
                },
                textStyle = TextStyle(fontSize = 12.sp, color = Color(0xFF6B6B6B)),
                modifier = if (isTextFieldFocused) {
                    Modifier
                        .weight(1f)
                        .border(1.dp, Orange, shape = RoundedCornerShape(10.dp))
                        .onFocusChanged { isTextFieldFocused = it.isFocused }
                } else {
                    Modifier
                        .weight(1f)
                        .border(1.dp, Orange, shape = RoundedCornerShape(10.dp))
                        .onFocusChanged { isTextFieldFocused = it.isFocused }
                },
                singleLine = !isTextFieldFocused,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Default
                ),
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier
                            .padding(10.dp) //15
                            .fillMaxWidth()
                        // .fillMaxHeight()
                    ){
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (text.isEmpty()) {
                                Text(
                                    text = if(isTextFieldFocused) "Write your comment here..." else "Comment",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Normal,
                                    color = Color(0xFF6B6B6B),
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            //Spacer(modifier = Modifier.weight(1f))
                        }
                        innerTextField()
                    }
                }

            )
            FloatingActionButton(
                onClick = {
                    if (text.isNotEmpty()) {
                       // chatViewModel.sendMessage(text, selectedImageUris)
                        reviewViewModel.addComment(reviewId, text, serviceType)
                        text = ""
                        localFocusManager.clearFocus()
                    }

                },
                shape = CircleShape,
                containerColor = if(text.isEmpty()) Orange.copy(0.4f)  else Orange,
                contentColor = Color.White,
                elevation = FloatingActionButtonDefaults.elevation(
                    defaultElevation = 10.dp
                ),
                modifier = Modifier
                    .padding(start = 10.dp)
                    .size(30.dp)

            ) {
                Icon(
                    imageVector = Icons.Filled.Send,
                    contentDescription = "Send",
                    modifier = Modifier.size(18.dp),
                )
            }
        }

    }

}


/*@Composable
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
    val commentsCount by remember {
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
            onClick = { *//*TODO*//* }
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.more_vertical),
                contentDescription = "",
                modifier = Modifier.size(18.dp)
            )
        }
    }
}*/



@Preview
@Composable
private fun StaycationReviewsItemPreview(){

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


    val reviewViewModel = viewModel(modelClass = ReviewViewModel::class.java)
    val staycationId = "LxpNxRFdwkQzBxujF3gx"
    val touristId = "Myedxx1M4qSCOEtldHJj"
    val serviceType = "Staycation"

//    CommentRow(
//        comment = "sobrang angassss",
//        image = "https://upload.wikimedia.org/wikipedia/commons/8/89/Portrait_Placeholder.png",
//        name = "Kyle",
//        date = "04 April 2023 "
//    )

    ReviewsScreen(
        touristId = touristId,
        serviceId = staycationId,
        serviceType = serviceType,
        reviewViewModel = reviewViewModel,
        onBack = {

        }
    )
}