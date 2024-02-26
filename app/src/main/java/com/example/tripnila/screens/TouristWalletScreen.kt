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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tripnila.common.Orange
import com.example.tripnila.data.WalletTransaction
import com.example.tripnila.model.TouristWalletViewModel
import java.text.DecimalFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TouristWalletScreen(
    touristId: String = "",
    touristWalletViewModel: TouristWalletViewModel,
    onBack: () -> Unit,
    onNavToCashIn: (String) -> Unit
){
    val horizontalPaddingValue = 16.dp
    val verticalPaddingValue = 10.dp
    val touristWallet by touristWalletViewModel.touristWallet.collectAsState()
    val walletTransactions = listOf(
        WalletTransaction(
            transactionType = "Cash in",
            transactionDate = "September 06, 2023",
            amount = 1000.00
        ),
        WalletTransaction(
            transactionType = "Payment",
            transactionDate = "September 06, 2023",
            amount = 1000.00
        ),
    )

    touristWalletViewModel.getWallet(touristId)
    val currentBalance = touristWallet.currentBalance

    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Scaffold(
            topBar = {
                AppTopBar(
                    headerText = "Wallet",
                    color = Color(0xFFF8F8F9)
                )
            },
            bottomBar = {
                WalletBottomBar(
                    onBack = {
                        onBack()
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
                    AvailableBalanceCardWithCashinButton(
                        userName = "Joshua Araneta",
                        userBalance = currentBalance,
                        onCashin = {
                            onNavToCashIn(touristId)
                        },
                        modifier = Modifier
                            .padding(
                                vertical = verticalPaddingValue,
                                horizontal = horizontalPaddingValue
                            )
                    )
                }

                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                vertical = verticalPaddingValue,
                                horizontal = horizontalPaddingValue
                            )
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

            }
        }
    }
}

@Composable
fun WalletBottomBar(
    modifier: Modifier = Modifier,
    onBack: (() -> Unit)? = null,
    rightButtonText: String = "Back",
    enableRightButton: Boolean = true,
){
    Surface(
        color = Color.White,
        tonalElevation = 10.dp,
        shadowElevation = 10.dp,
        modifier = modifier
            .height(78.dp)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 25.dp, vertical = 12.dp),
        ) {
            Spacer(modifier = Modifier.weight(1f))
            BookingFilledButton(
                enabled = enableRightButton,
                buttonText = rightButtonText,
                onClick = {
                    onBack?.invoke()
                },
                modifier = Modifier.width(120.dp)
            )
        }
    }
}


@Composable
fun AvailableBalanceCardWithCashinButton(
    userName: String,
    userBalance: Double,
    onCashin: () -> Unit,
    modifier: Modifier = Modifier
){

    val formattedBalance = DecimalFormat("#,##0.${"0".repeat(2)}").format(userBalance)


    Card(
        colors = CardDefaults.cardColors(
            containerColor = Orange
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        shape = RoundedCornerShape(10.dp),
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
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = userName,
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                    )
                    Text(
                        text = "User",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                    )

                }

                Column {
                    Text(
                        text = "Available balance",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                    )
                    Text(
                        text =  "₱ $formattedBalance",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.align(Alignment.End)
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            BookingFilledButton(
                buttonText = "Cash in",
                contentFontSize = 12.sp,
                contentColor = Orange,
                contentPadding = PaddingValues(18.dp, 0.dp),
                containerColor = Color.White,
                onClick = {
                    onCashin()
                },
                modifier = Modifier
                    .height(30.dp)
                    .align(Alignment.End)
            )

        }
    }
}


@Preview
@Composable
private fun UserWalletScreenPreview(){
    TouristWalletScreen(onNavToCashIn = {_ ->}, touristWalletViewModel = TouristWalletViewModel(), onBack = {})
}