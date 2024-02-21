package com.example.tripnila.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tripnila.common.AppBottomNavigationBar
import com.example.tripnila.common.Orange
import com.example.tripnila.data.WalletTransaction

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HostWalletScreen(){
    val horizontalPaddingValue = 16.dp
    val verticalPaddingValue = 10.dp

    val walletTransactions = listOf(
        WalletTransaction(
            transactionType = "Withdraw",
            transactionDate = "September 06, 2023",
            amount = 1000.00
        ),
        WalletTransaction(
            transactionType = "Withdraw",
            transactionDate = "September 06, 2023",
            amount = 1000.00
        ),
    )

    var selectedItemIndex by rememberSaveable { mutableStateOf(3) }

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
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .background(Color(0xFFF8F8F9))
            ) {
                item {
                    AppTopBar(
                        headerText = "Wallet",
                        color = Color.White
                    )
                }
                item {
                    AvailableBalanceCardWithWithdrawButton(
                        hostName = "Joshua Araneta",
                        hostBalance = 7600.00,
                        modifier = Modifier
                            .padding(
                                vertical = verticalPaddingValue,
                                horizontal = horizontalPaddingValue
                            )
                    )
                }
                item {
                    TotalBalanceCard(
                        totalBalance = 8600.00,
                        fundsOnHold = 1000.00,
                        modifier = Modifier.padding(
                            vertical = verticalPaddingValue,
                            horizontal = horizontalPaddingValue
                        )
                    )
                }

                item {
                    RecentTransactionsList(
                        walletTransactions = walletTransactions,
                        modifier = Modifier.padding(
                            horizontal = horizontalPaddingValue
                        )
                    )
                }
//                item {
//                    HostBusinessesList(
//                        properties = businessProperties,
//                        modifier = Modifier
//                            .padding(
//                                vertical = verticalPaddingValue,
//                                //horizontal = horizontalPaddingValue
//                            )
//                    )
//                }
//                item {
//                    HostToursList(
//                        properties = tourProperties,
//                        modifier = Modifier
//                            .padding(
//                                vertical = verticalPaddingValue,
//                                //horizontal = horizontalPaddingValue
//                            )
//                    )
//                }
            }
        }
    }
}

@Composable
fun AvailableBalanceCardWithWithdrawButton(
    hostName: String,
    hostBalance: Double,
    modifier: Modifier = Modifier
){
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Orange
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        shape = RoundedCornerShape(20.dp),
        modifier = modifier
            .fillMaxWidth()
            .height(140.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(
                    horizontal = 15.dp,
                    vertical = 15.dp // 12
                )
                .fillMaxWidth()
        ) {
            Row {
                Column {
                    Text(
                        text = hostName,
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                    )
                    Text(
                        text = "Host",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                    )

                }
                Spacer(modifier = Modifier.weight(1f))
                Column {
                    Text(
                        text = "Available balance",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                    )
                    Text(
                        text =  "₱ ${String.format("%.2f", hostBalance)}",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))

        }
    }
}

@Composable
fun TotalBalanceCard(
    totalBalance: Double,
    fundsOnHold: Double,
    modifier: Modifier = Modifier
){
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        shape = RoundedCornerShape(20.dp),
        modifier = modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(
                    horizontal = 15.dp,
                    vertical = 15.dp // 12
                )
                .fillMaxWidth()
        ) {
            Text(
                text = "Total Balance",
                color = Color(0xff333333),
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "₱ ${String.format("%.2f", totalBalance)}",
                color = Color(0xFF333333),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = "Funds on hold",
                color = Color(0xff333333),
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "₱ ${String.format("%.2f", fundsOnHold)}",
                color = Color(0xFF333333),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
            )
        }
    }
}

@Composable
fun RecentTransactionsList(walletTransactions: List<WalletTransaction>, modifier: Modifier = Modifier){
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = "Recent Transactions",
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.height(2.dp))
        walletTransactions.forEach { walletTransaction ->
            RecentTransactionCard(
                transaction = walletTransaction,
                modifier = Modifier.padding(top = 8.dp, start = 5.dp, end = 5.dp)
            )
        }
        BookingOutlinedButton(
            buttonText = "See all transactions",
            containerColor = Color(0xFFF8F8F9),
            buttonShape = RoundedCornerShape(10.dp),
            borderStroke = BorderStroke(1.dp, Color(0xff999999)),
            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 0.dp),
            contentFontSize  = 12.sp,
            contentFontWeight = FontWeight.Medium,
            contentColor = Color(0xff999999),
            onClick = {

            },
            modifier = Modifier
                .padding(start = 30.dp, end = 30.dp, top = 12.dp)
                .fillMaxWidth()
                .height(35.dp)
        )
    }


}

@Composable
fun RecentTransactionCard(
    transaction: WalletTransaction,
    modifier: Modifier = Modifier
) {
    var sign = if (transaction.transactionType == "Withdraw") "-" else "+"

    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF8F8F9)
        ),
        border = BorderStroke(1.dp, Color(0xff999999)),
        shape = RoundedCornerShape(10.dp),
        modifier = modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(
                    horizontal = 15.dp,
                    vertical = 15.dp // 12
                )
                .fillMaxWidth()
        ) {
            Column {
                Text(
                    text = transaction.transactionType,
                    color = Color(0xff333333),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = transaction.transactionDate,
                    color = Color(0xff999999),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "$sign ₱ ${String.format("%.2f", transaction.amount)}",
                color = Orange,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.align(CenterVertically)
            )

        }

    }
}


@Preview
@Composable
private fun HostWalletPreview(){

    val transactions = listOf(
        WalletTransaction(
            transactionType = "Withdraw",
            transactionDate = "September 06, 2023",
            amount = 1000.00
        ),
        WalletTransaction(
            transactionType = "Withdraw",
            transactionDate = "September 06, 2023",
            amount = 1000.00
        ),
    )

    RecentTransactionsList(transactions)


}

@Preview
@Composable
private fun HostWalletScreenPreview(){
    HostWalletScreen()
}