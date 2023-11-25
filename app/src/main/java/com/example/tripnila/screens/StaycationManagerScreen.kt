package com.example.tripnila.screens

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.tripnila.R
import com.example.tripnila.common.AppOutlinedButton
import com.example.tripnila.common.AppReviewsCard
import com.example.tripnila.common.Orange
import com.example.tripnila.common.Tag
import com.example.tripnila.data.AmenityBrief
import com.example.tripnila.data.ReviewUiState
import com.example.tripnila.data.Transaction
import com.example.tripnila.model.StaycationManagerViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun StaycationManagerScreen(
    staycationId: String = "",
    hostId: String = "",
    staycationManagerViewModel: StaycationManagerViewModel
){

    Log.d("Staycation", "$staycationId $hostId")

    LaunchedEffect(staycationId) {
        staycationManagerViewModel.getSelectedStaycation(staycationId)
    }

    var staycation = staycationManagerViewModel?.staycation?.collectAsState()

    val transactions = staycation?.value?.staycationBookings?.map {staycationBooking ->
        val formatter = SimpleDateFormat("MMM d", Locale.getDefault())
        val start = formatter.format(staycationBooking.checkInDate)
        val end = formatter.format(staycationBooking.checkOutDate)
        val year = SimpleDateFormat("yyyy", Locale.getDefault()).format(staycationBooking.checkInDate)

        Transaction(
            customerImage = staycationBooking.tourist?.profilePicture ?: "",
           // bookedRental = staycationBooking.staycation?.staycationTitle ?: "",
            customerName = "${staycationBooking.tourist?.firstName} ${staycationBooking.tourist?.lastName}",
            customerUsername = staycationBooking.tourist?.username ?: "",
            guestsCount = staycationBooking.noOfGuests,
            price = staycationBooking.totalAmount ?: 0.0,
            bookedDate = "$start-$end, $year",
            transactionDate = SimpleDateFormat("MMM dd, yyyy").format(staycationBooking.bookingDate) , //staycationBooking.bookingDate.toString(),
            transactionStatus = staycationBooking.bookingStatus
        )
    }?.sortedByDescending { it.transactionDate } ?: emptyList()




//    val staycationReviews = staycation?.value?.staycationBookings
//        ?.filter { it.bookingReview != null }
//        ?.mapNotNull { it.bookingReview }

    val staycationReviews = staycation?.value?.staycationBookings?.mapNotNull { it.bookingReview }
    val reviews: List<ReviewUiState> = staycationReviews?.filter { it.bookingId != "" }
        ?.map { review ->
        review.let {
            ReviewUiState(
                rating = it.rating.toDouble() ?: 0.0,
                comment = it.comment ?: "",
                touristImage = it?.reviewer?.profilePicture ?: "https://upload.wikimedia.org/wikipedia/commons/8/89/Portrait_Placeholder.png",
                touristName = "${it?.reviewer?.firstName} ${it.reviewer?.lastName}",
                reviewDate = it.reviewDate.toString()
            )
        }
    } ?: emptyList()



//    val transactions = listOf(
//        Transaction(
//          //  customerImage = R.drawable.joshua,
//            customerName = "Juan Cruz",
//            customerUsername = "@jcruz",
//            guestsCount = 4,
//            price = 2500.00,
//            bookedDate = "Sep 3-6, 2023",
//            transactionDate = "Aug 24, 2023",
//            transactionStatus = "Completed"
//        ),
//        Transaction(
//          // customerImage = R.drawable.joshua,
//            customerName = "Juan Cruz",
//            customerUsername = "@jcruz",
//            guestsCount = 4,
//            price = 2500.00,
//            bookedDate = "Sep 3-6, 2023",
//            transactionDate = "Aug 24, 2023",
//            transactionStatus = "Completed"
//        ),
//
//        )

    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = Color(0xFFEFEFEF)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.staycation1),
                        contentDescription = "h5 1",
                        contentScale = ContentScale.FillWidth
                    )
                    TopBarIcons(forStaycationManager = true)
                }
            }
            item {
                StaycationDescriptionCard1(
                    staycation = staycation?.value,
                    withEditButton = true,
                    modifier = Modifier
                        .offset(y = (-17).dp)
                )
            }
            item {
                StaycationDescriptionCard3(
                    staycation = staycation?.value,
                    //amenities = amenities,
                    withEditButton = true,
                    modifier = Modifier
                        .offset(y = (-5).dp)
                        .padding(bottom = 12.dp)
                )
            }
            item {
                StaycationAmenitiesCard(
                    staycation = staycation?.value,
                    //amenities = amenities, /*TODO*/
                    withEditButton = true,
                    modifier = Modifier
                        .offset(y = (-5).dp)
                        .padding(bottom = 12.dp)
                )
            }
            item {
                StaycationAdditionalInformationCard(
                    withEditButton = true,
                    modifier = Modifier
                        .offset(y = (-5).dp)
                        .padding(bottom = 12.dp) // 7
                )
            }
            item {
                AppTransactionsCard(
                    transactions = transactions,
                    modifier = Modifier
                        .offset(y = (-5).dp)
                        .padding(bottom = 12.dp) // 7
                )
            }
            item {
                staycation?.value?.averageReviewRating?.let { averageRating ->
                    staycation?.value?.totalReviews?.let { totalReviews ->
                        AppReviewsCard(
                            totalReviews = totalReviews,
                            averageRating = averageRating,
                            reviews = reviews,
                            modifier = Modifier
                                .offset(y = (-5).dp)
                                .padding(bottom = 7.dp)
                        )
                    }
                }
            }
//            item {
//                StaycationBottomBookingBar(onClickBook = {}, onClickChatHost = {}, onClickUnderlinedText = {})
//            }
        }
    }
}

@Composable
fun AppTransactionsCard(transactions: List<Transaction>, modifier: Modifier = Modifier){
    Box(
        modifier = modifier
            .fillMaxWidth()
            //.height(height = 110.dp)
            .clip(shape = RoundedCornerShape(20.dp))
            .background(color = Color.White)
    ) {
        Column(
            modifier = Modifier
                //                .padding(horizontal = 25.dp, vertical = 12.dp),
                .padding(
                    horizontal = 25.dp,
                    vertical = 20.dp // 12
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Transactions",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(bottom = 4.dp)
            )

            transactions.take(2).forEach { transaction ->
                TransactionCard(
                    transaction = transaction,
                    modifier = Modifier.padding(top = 7.dp)
                )
            }

            AppOutlinedButton(
                buttonText = "See all transactions",
                modifier = Modifier
                    .padding(top = 12.dp)
            )

        }
    }
}

@Composable
fun TransactionCard(transaction: Transaction, modifier: Modifier = Modifier){
    OutlinedCard(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, Color(0xff999999)),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        )
    ){
        Row(
            modifier = Modifier
                .padding(
                    horizontal = 25.dp,
                    vertical = 15.dp // 12
                ),
            horizontalArrangement = Arrangement.Center
        ) {
            Column {
                Row {
                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .clip(shape = RoundedCornerShape(50.dp))
                    ) {
                        AsyncImage(
                            model = if (transaction.customerImage == "") "https://upload.wikimedia.org/wikipedia/commons/8/89/Portrait_Placeholder.png" else transaction.customerImage,//imageLoader,
                            contentDescription = "",
                            contentScale = ContentScale.Crop

                        )
                    }
                    Column(
                        modifier = Modifier.padding(start = 5.dp, end = 5.dp, bottom = 5.dp)
                    ) {
                        Text(
                            text = transaction.customerName,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                        )
                        Text(
                            text = transaction.customerUsername,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF999999)
                        )
                    }
                    Tag(tag = transaction.transactionStatus)

                }
                Text(
                    text = "${transaction.guestsCount} guests • ₱ ${"%.2f".format(transaction.price)} • ${transaction.bookedDate}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF999999),
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Column {
                Text(
                    text = transaction.transactionDate,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = Orange,
                    modifier = Modifier.offset(x = (-10).dp)
                )
                IconButton(
                    onClick = { /*TODO*/ },
                    modifier = Modifier
                        .size(36.dp)
                        .align(Alignment.End)
                        .offset(x = 10.dp, y = (-7).dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.KeyboardArrowRight,
                        contentDescription = "Expand",
                        tint = Color(0xFF999999),
                        modifier = Modifier.size(36.dp)
                    )
                }
            }

        }
    }

}

@Preview
@Composable
private fun StaycationManagerPreview() {

    val transactions = listOf(
        Transaction(
        //    customerImage = R.drawable.joshua,
            customerName = "Juan Cruz",
            customerUsername = "@jcruz",
            guestsCount = 4,
            price = 2500.00,
            bookedDate = "Sep 3-6, 2023",
            transactionDate = "Aug 24, 2023",
            transactionStatus = "Completed"
        ),
        Transaction(
      //      customerImage = R.drawable.joshua,
            customerName = "Juan Cruz",
            customerUsername = "@jcruz",
            guestsCount = 4,
            price = 2500.00,
            bookedDate = "Sep 3-6, 2023",
            transactionDate = "Aug 24, 2023",
            transactionStatus = "Completed"
        ),

    )

    AppTransactionsCard(transactions)

    //TransactionCard(transaction)

}


@Preview
@Composable
private fun StaycationManagerScreenPreview() {

    val staycationManagerViewModel = viewModel(modelClass = StaycationManagerViewModel::class.java)

    StaycationManagerScreen(
        hostId = "HOST-ITZbCFfF7Fzqf1qPBiwx",
        staycationId = "LxpNxRFdwkQzBxujF3gx",
        staycationManagerViewModel = staycationManagerViewModel
    )

}