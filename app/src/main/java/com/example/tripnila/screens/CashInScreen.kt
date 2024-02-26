package com.example.tripnila.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tripnila.R
import com.example.tripnila.common.Orange
import com.example.tripnila.data.PaymentMethod
import com.example.tripnila.data.WalletTransaction
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CashInScreen(
    touristId: String = "",
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
                    headerText = "Cash In",
                    color = Color(0xFFF8F8F9)
                )
            },
            bottomBar = {
                AddListingBottomBookingBar(
                    leftButtonText = "Cancel",
                    rightButtonText = "Confirm",

                    onNext = {
                        //onNavToNext(listingType)
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
                    CashInChoosePaymentMethodCard(
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
//                item {
//                    RemainingBalanceCard(
//                        remainingBalance = 6600.00,
//                        modifier = Modifier
//                            .padding(
//                                vertical = verticalPaddingValue,
//                                horizontal = horizontalPaddingValue
//                            )
//                    )
//                }
            }

        }
    }
}

@Composable
fun CashInChoosePaymentMethodCard(
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
                    text = "Choose cash in method",
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

@Composable
fun WithdrawCard(
    modifier: Modifier = Modifier,
    availableBalance: Double
) {

    val formattedBalance = DecimalFormat("#,##0.${"0".repeat(2)}").format(availableBalance)

    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color.White
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
        ) {
            Text(
                text = "Available Balance",
                color = Color(0xff333333),
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "₱ $formattedBalance",
                color = Color(0xff333333),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = "Withdraw amount",
                color = Color(0xff333333),
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(3.dp))
            WithdrawTextField(
                availableBalance = availableBalance
            )
        }
    }
}

@Composable
fun WithdrawTextField(
    modifier: Modifier = Modifier,
    availableBalance: Double
) {
    var text by remember {
        mutableDoubleStateOf(0.00)
    }

    var isFocused by remember { mutableStateOf(false) }

    val localFocusManager = LocalFocusManager.current

    val inputAmount = text

    // Format the input amount with two decimal places
    val formattedAmount = String.format("%.2f", inputAmount)

    val enoughBalance = inputAmount <= availableBalance
    val balanceTextColor = if (enoughBalance) Color.Green else Color.Red
    val balanceText = if (enoughBalance) "There's enough balance." else "There's insufficient balance."

    BasicTextField(
        value = formattedAmount,
        onValueChange = { newValue ->
            // Filter out non-numeric characters
            text = newValue.toDoubleOrNull() ?: 0.0
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

    Text(
        text = balanceText,
        color = balanceTextColor,
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium,
        modifier = Modifier.padding(start = 8.dp) // Add padding to separate from the input field
    )
}

@Composable
fun RemainingBalanceCard(
    modifier: Modifier = Modifier,
    remainingBalance: Double
) {

    val formattedBalance = DecimalFormat("#,##0.${"0".repeat(2)}").format(remainingBalance)

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
            Text(
                text = "Remaining Balance",
                color = Color(0xff333333),
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "₱ $formattedBalance",
                color = Orange,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.align(Alignment.CenterVertically)
            )

        }

    }
}


@Preview
@Composable
private fun CashInComposablePreview() {

}

@Preview
@Composable
private fun CashInScreenPreview() {
    CashInScreen(onCancel = {})
}