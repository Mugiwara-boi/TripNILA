package com.example.tripnila.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tripnila.R
import com.example.tripnila.common.Orange
import com.example.tripnila.data.Account
import com.example.tripnila.data.PaymentMethod

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentAndPayoutScreen() {

    val horizontalPaddingValue = 16.dp
    val verticalPaddingValue = 10.dp

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(state = rememberTopAppBarState())

    val linkedAccounts = listOf(
        Account(
            accountType = "Paypal",
            account = "example@gmail.com"
        ),
        Account(
            accountType = "Gcash",
            account = "09123456731"
        ),
        Account(
            accountType = "Paymaya",
            account = "09123456731"
        )
    )

    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Scaffold(
            topBar = {
                PaymentAndPayoutTopBar(
                    onBack = { /*TODO*/ },
                    scrollBehavior = scrollBehavior
                )
            },
            bottomBar = {
                AddListingBottomBookingBar(
                    leftButtonText = "Back",
                    rightButtonText = "Confirm",

                    onNext = {
                        //onNavToNext(listingType)
                    },
                    onCancel = {
                        // onNavToBack()
                    },
                )
            },
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .background(Color(0xFFF8F8F9))
            ) {
                item {
                    ConnectAccountCard(
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
                            .padding(
                                vertical = verticalPaddingValue,
                                horizontal = horizontalPaddingValue
                            )
                    ) {
                        Text(
                            text = "Linked accounts",
                            color = Color(0xff333333),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium
                        )
                        linkedAccounts.forEach {  account ->
                            LinkedAccountCard(
                                account = account,
                                onRemove = { account ->
                                    /*TODO*/
                                },
                                modifier = Modifier
                                    .padding(
                                        vertical = (verticalPaddingValue/2),
                                    )
                            )
                        }
                    }

                }

            }
        }
    }
}

@Composable
fun LinkedAccountCard(
    modifier: Modifier = Modifier,
    account: Account,
    onRemove: (String) -> Unit
) {

    val maskedAccount = when(account.accountType) {
        "Paypal" -> {

            val email = account.account

            val atIndex = email.indexOf('@')

            if (atIndex <= 0 || atIndex == email.length - 1) {
                email
            } else {
                val maskedPart = email.substring(3, atIndex).replace(Regex("[a-zA-Z0-9]"), "*")
                val visiblePart = email.substring(0, 3) // Keep the first three characters visible
                val domainPart = email.substring(atIndex)
                visiblePart + maskedPart + domainPart
            }
        }
        "Gcash" -> {
            val prefix = account.account.substring(0, 2)
            val suffix = account.account.substring(account.account.length - 2)

            "${prefix}*********${suffix}"
        }
        "Paymaya" -> {
            val prefix = account.account.substring(0, 2)
            val suffix = account.account.substring(account.account.length - 2)

            "${prefix}*********${suffix}"
        }

        else -> "Not indicated"
    }

    val accounTypeLogo = when(account.accountType) {
        "Paypal" -> R.drawable.paypal
        "Gcash" -> R.drawable.gcash
        "Paymaya" -> R.drawable.paymaya
        else -> 0
    }


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
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(
                    horizontal = 15.dp,
                    vertical = 8.dp // 12
                )
                .fillMaxWidth()
        ) {

            Box(
                modifier = Modifier
                    .height(30.dp)
                    .width(80.dp)
                    .padding(end = 5.dp)
            ) {
                Image(
                    painter = painterResource(id = accounTypeLogo),
                    contentDescription = "",
                    contentScale = ContentScale.FillWidth,
                )
            }
            Text(
                text = maskedAccount,
                color = Color(0xff666666),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f)
            )
            BookingOutlinedButton(
                contentFontSize = 12.sp,
                buttonText = "Remove",
                contentPadding = PaddingValues(10.dp, 0.dp),
                onClick = {
                    onRemove(account.account)
                }
            )

        }

    }
}

@Composable
fun ConnectAccountCard(
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
                    text = "Connect your account",
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
                        buttonText = "Link",
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentAndPayoutTopBar(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior? = null
) {
    TopAppBar(
        title = {
            Text(
                text = "Payment & Payout",
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium
            )
        },
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
        scrollBehavior = scrollBehavior,
        modifier = modifier

    )

}

@Preview
@Composable
private fun PaymentAndPayoutScreenPreview() {
    PaymentAndPayoutScreen()
}