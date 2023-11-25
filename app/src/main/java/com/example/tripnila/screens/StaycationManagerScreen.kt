package com.example.tripnila.screens

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
import coil.compose.AsyncImage
import com.example.tripnila.R
import com.example.tripnila.common.AppOutlinedButton
import com.example.tripnila.common.AppReviewsCard
import com.example.tripnila.common.Orange
import com.example.tripnila.common.Tag
import com.example.tripnila.data.AmenityBrief
import com.example.tripnila.data.ReviewUiState
import com.example.tripnila.data.Transaction

@Composable
fun StaycationManagerScreen(){

    val amenities = listOf(
        AmenityBrief(
            image = R.drawable.person,
            count = 4,
            name = "person"
        ),
        AmenityBrief(
            image = R.drawable.pool,
            count = 1,
            name = "swimming pool"
        ),
        AmenityBrief(
            image = R.drawable.bedroom,
            count = 2,
            name = "bedroom"
        ),
        AmenityBrief(
            image = R.drawable.bathroom,
            count = 2,
            name = "bathroom"
        ),
        AmenityBrief(
            image = R.drawable.kitchen,
            count = 1,
            name = "kitchen"
        )
    )
    val reviews = listOf(
        ReviewUiState(
            rating = 4.5,
            comment = "A wonderful staycation experience!",
          //  touristImage = R.drawable.joshua,
            touristName = "John Doe",
            reviewDate = "2023-05-15"
        ),
        ReviewUiState(
            rating = 5.0,
            comment = "Amazing place and great service!",
          //  touristImage = R.drawable.joshua,
            touristName = "Jane Smith",
            reviewDate = "2023-04-20"
        ),
        ReviewUiState(
            rating = 5.0,
            comment = "Amazing place and great service!",
         //   touristImage = R.drawable.joshua,
            touristName = "Jane Smith",
            reviewDate = "2023-04-20"
        ),
        ReviewUiState(
            rating = 5.0,
            comment = "Amazing place and great service!",
           // touristImage = R.drawable.joshua,
            touristName = "Jane Smith",
            reviewDate = "2023-04-20"
        ),
        ReviewUiState(
            rating = 5.0,
            comment = "Amazing place and great service!",
         //   touristImage = R.drawable.joshua,
            touristName = "Jane Smith",
            reviewDate = "2023-04-20"
        ),
        ReviewUiState(
            rating = 5.0,
            comment = "Amazing place and great service!",
          //  touristImage = R.drawable.joshua,
            touristName = "Jane Smith",
            reviewDate = "2023-04-20"
        ),
    )
    val transactions = listOf(
        Transaction(
          //  customerImage = R.drawable.joshua,
            customerName = "Juan Cruz",
            customerUsername = "@jcruz",
            guestsCount = 4,
            price = 2500.00,
            bookedDate = "Sep 3-6, 2023",
            transactionDate = "Aug 24, 2023",
            transactionStatus = "Completed"
        ),
        Transaction(
          // customerImage = R.drawable.joshua,
            customerName = "Juan Cruz",
            customerUsername = "@jcruz",
            guestsCount = 4,
            price = 2500.00,
            bookedDate = "Sep 3-6, 2023",
            transactionDate = "Aug 24, 2023",
            transactionStatus = "Completed"
        ),

        )

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
                    withEditButton = true,
                    modifier = Modifier
                        .offset(y = (-17).dp)
                )
            }
            item {
                StaycationDescriptionCard3(
                    //amenities = amenities,
                    withEditButton = true,
                    modifier = Modifier
                        .offset(y = (-5).dp)
                        .padding(bottom = 12.dp)
                )
            }
            item {
                StaycationAmenitiesCard(
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
                AppReviewsCard(
                    reviews = reviews,
                    modifier = Modifier
                        .offset(y = (-5).dp)
                        .padding(bottom = 7.dp)
                )
            }
            item {
                StaycationBottomBookingBar(onClickBook = {}, onClickChatHost = {}, onClickUnderlinedText = {})
            }
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

            transactions.forEach { transaction ->
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

    StaycationManagerScreen()

}