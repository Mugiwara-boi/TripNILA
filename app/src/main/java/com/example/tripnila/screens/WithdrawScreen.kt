package com.example.tripnila.screens

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tripnila.R
import com.example.tripnila.common.Orange
import com.example.tripnila.data.PaymentMethod

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WithdrawScreen(
    hostId: String = "",
    onCancel: () -> Unit,
) {

    val horizontalPaddingValue = 16.dp
    val verticalPaddingValue = 10.dp

    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Scaffold(
            topBar = {
                AppTopBar(
                    headerText = "Withdraw",
                    color = Color(0xFFF8F8F9)
                )
            },
            bottomBar = {
                AddListingBottomBookingBar(
                    leftButtonText = "Cancel",
                    rightButtonText = "Confirm",

                    onNext = {
                        // CONFIRM BUTTON
                    },
                    onCancel = {
                        onCancel()
                    },
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
                    WithdrawChoosePaymentMethodCard(
                        modifier = Modifier
                            .padding(
                                vertical = verticalPaddingValue,
                                horizontal = horizontalPaddingValue
                            )
                    )
                }
                item {
                    WithdrawCard(
                        availableBalance = 7600.00,
                        modifier = Modifier
                            .padding(
                                vertical = verticalPaddingValue,
                                horizontal = horizontalPaddingValue
                            )
                    )
                }
                item {
                    RemainingBalanceCard(
                        remainingBalance = 6600.00,
                        modifier = Modifier
                            .padding(
                                vertical = verticalPaddingValue,
                                horizontal = horizontalPaddingValue
                            )
                    )
                }
            }

        }
    }
}

@Composable
fun WithdrawChoosePaymentMethodCard(
    modifier: Modifier = Modifier
){

    var selectedPaymentMethod by remember { mutableIntStateOf(-1) }

    var paymentMethods = listOf(
        PaymentMethod(R.drawable.paypal),
        PaymentMethod(R.drawable.gcash),
        PaymentMethod(R.drawable.paymaya)
    )

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
    ) {
        Column(
            modifier = Modifier
                .padding(
                    horizontal = 15.dp,
                    vertical = 15.dp // 12
                )
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                Text(
                    text = "Choose payout method",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                )
                Row {
                    paymentMethods.forEach { paymentMethod ->
                        PaymentMethodCard(
                            logoId = paymentMethod.logoId,
                            selectedBorderColor = Color.White,
                            selectedContainerColor = Orange,
                            isSelected = selectedPaymentMethod == paymentMethods.indexOf(paymentMethod),
                            onCardSelect = {

                                if (selectedPaymentMethod == paymentMethods.indexOf(paymentMethod)) {
                                    // If the card is already selected, unselect it
                                    selectedPaymentMethod = -1
                                } else {
                                    // Otherwise, select the clicked card
                                    selectedPaymentMethod = paymentMethods.indexOf(paymentMethod)
                                }

                            },
                            modifier = if (paymentMethods.indexOf(paymentMethod) == 2) {
                                Modifier
                                    .height(40.dp)
                                    .width(80.dp)
                            } else {
                                Modifier
                            }
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    BookingFilledButton(
                        buttonText = "Add",
                        contentFontSize = 12.sp,
                        buttonShape = RoundedCornerShape(20.dp),
                        contentColor = Orange,
                        contentPadding = PaddingValues(18.dp, 0.dp),
                        containerColor = Color.White,
                        onClick = {
                            // ADD BUTTON FUNCTION
                        },
                        modifier = Modifier
                            .height(30.dp)
                            .align(Alignment.CenterVertically)

                    )
                }

            }

        }
    }
}


@Preview
@Composable
private fun WithdrawScreenPreview() {
  //  WithdrawScreen()
}