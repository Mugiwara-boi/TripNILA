package com.example.tripnila.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tripnila.R
import com.example.tripnila.common.AppBottomNavigationBar
import com.example.tripnila.common.Orange
import com.example.tripnila.data.User


@Composable
fun ProfileScreen(){

    val currentUser = User(
        profilePicture = R.drawable.joshua,
        fullName = "Joshua Araneta",
        userName = "@joshAr26"
    )

    val horizontalPaddingValue = 16.dp

    var selectedItemIndex by rememberSaveable {
        mutableStateOf(3)
    }


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
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                item {
                    AppTopBarWithIcon(
                        headerText = "Profile",
                        headerIcon = ImageVector.vectorResource(id = R.drawable.logout),

                    )
                }
                item{
                    UserProfileComposable(
                        user = currentUser,
                        modifier = Modifier.padding(horizontal = horizontalPaddingValue)
                    )
                }
                item{
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp, bottom = 10.dp)
                            .padding(horizontal = horizontalPaddingValue),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        AppFilledCard(cardText = "Bookings")
                        AppOutlinedCard(cardText = "Preferences")
                        AppFilledCard(cardText = "Edit Profile")
                    }
                }
                item{
                    AppOutlinedCard(
                        cardText = "Verify your account",
                        modifier = Modifier
                            .padding(horizontal = horizontalPaddingValue)
                    )
                }
                item {
                    Text(
                        text = "Settings",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                            .padding(vertical = 10.dp, horizontal = horizontalPaddingValue)
                            .fillMaxWidth()
                            .wrapContentWidth(align = Alignment.Start)

                    )
                }
                item {
                    OptionsRow(
                        icon = R.drawable.notification,
                        rowText = "Notification",
                        modifier = Modifier
                            .padding(horizontal = horizontalPaddingValue)
                    )
                }
                item {
                    OptionsRow(
                        icon = R.drawable.payment,
                        rowText = "Payment and payouts",
                        modifier = Modifier
                            .padding(horizontal = horizontalPaddingValue)
                    )
                }
                item {
                    OptionsRow(
                        icon = R.drawable.privacy,
                        rowText = "Privacy",
                        modifier = Modifier
                            .padding(horizontal = horizontalPaddingValue)
                    )
                }
                item {
                    Text(
                        text = "Hosting",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                            .padding(vertical = 10.dp, horizontal = horizontalPaddingValue)
                            .fillMaxWidth()
                            .wrapContentWidth(align = Alignment.Start)
                    )
                }
                item {
                    OptionsRow(
                        icon = R.drawable.resource_switch,
                        rowText = "Switch to hosting",
                        modifier = Modifier
                                .padding(horizontal = horizontalPaddingValue)
                    )
                }
                item {
                    Text(
                        text = "Legal",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                            .padding(vertical = 10.dp, horizontal = horizontalPaddingValue)
                            .fillMaxWidth()
                            .wrapContentWidth(align = Alignment.Start)
                    )

                }
                item {
                    OptionsRow(
                        icon = R.drawable.document,
                        rowText = "Terms of Service",
                        modifier = Modifier
                            .padding(horizontal = horizontalPaddingValue)
                    )
                }
                item {
                    OptionsRow(
                        icon = R.drawable.document,
                        rowText = "Privacy Policy",
                        modifier = Modifier
                            .padding(horizontal = horizontalPaddingValue)
                    )
                }
                item {
                    Text(
                        text = "Version 1.0 (Beta)",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF999999),
                        modifier = Modifier
                            .padding(vertical = 10.dp, horizontal = horizontalPaddingValue)
                            .fillMaxWidth()
                            .wrapContentWidth(align = Alignment.Start)
                    )
                }

            }

        }
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBarWithIcon(headerText: String, headerIcon: ImageVector, modifier: Modifier = Modifier) {
    TopAppBar(
        title = {
            Text(
                text = headerText,
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium
            )
        },
        actions = {
                Icon(
                    imageVector = headerIcon,
                    contentDescription = "",
                    modifier = Modifier.size(32.dp)
                )
        },
        modifier = modifier

    )
}

@Composable
fun AppOutlinedCard(cardText: String, modifier: Modifier = Modifier, cornerSize: Dp = 10.dp) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(cornerSize),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        ),
        border = BorderStroke(1.dp, Orange)
    ) {
        Row(
            modifier = Modifier
                .padding(vertical = 8.dp, horizontal = 10.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = cardText,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Orange
            )
        }
    }
}

@Composable
fun AppFilledCard(cardText: String, modifier: Modifier = Modifier, cornerSize: Dp = 10.dp) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(cornerSize),
        colors = CardDefaults.cardColors(
            containerColor = Orange
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        ),
        border = BorderStroke(1.dp, Orange)
    ) {
        Row(
            modifier = Modifier
                .padding(vertical = 8.dp, horizontal = 10.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = cardText,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White
            )
        }
    }
}

@Composable
fun UserProfileComposable(user : User, modifier: Modifier = Modifier){

    Box(
        modifier = modifier
            .padding(vertical = 10.dp)
            .size(140.dp)
            .clip(RoundedCornerShape(100.dp))
    ){
        Image(
            painter = painterResource(id = user.profilePicture),
            contentDescription = "Profile Picture",
            contentScale = ContentScale.Crop
        )
    }
    Text(
        text = user.fullName,
        fontSize = 24.sp,
        fontWeight = FontWeight.Medium
    )
    Text(
        text = user.userName,
        fontSize = 16.sp,
        fontWeight = FontWeight.Medium,
        color = Color(0xFF999999)
    )

}

@Composable
fun OptionsRow(icon: Int, rowText: String, modifier: Modifier = Modifier){
    Box(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(icon),
                contentDescription = ""
            )
            Spacer(modifier = Modifier.width(20.dp))
            Text(
                text = rowText,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(top = 3.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = Icons.Filled.KeyboardArrowRight,
                contentDescription = "",
                modifier = Modifier.size(34.dp)
            )
        }
    }
    Divider(modifier = Modifier.padding(vertical = 5.dp))
}

@Preview
@Composable
fun ProfileScreenPreview(){
    ProfileScreen()
}