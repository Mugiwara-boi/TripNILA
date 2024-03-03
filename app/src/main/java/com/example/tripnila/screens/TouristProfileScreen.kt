package com.example.tripnila.screens

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.tripnila.R
import com.example.tripnila.common.LoadingScreen
import com.example.tripnila.common.Orange
import com.example.tripnila.common.TouristBottomNavigationBar
import com.example.tripnila.data.ProfileData
import com.example.tripnila.model.LoginViewModel
import com.example.tripnila.model.ProfileViewModel
import kotlinx.coroutines.launch
import com.itenirary.IteniraryActivity


@Composable
fun TouristProfileScreen(
    profileViewModel: ProfileViewModel? = null,
    loginViewModel: LoginViewModel? = null,
    touristId: String = "",
    navController: NavHostController? = null,
    onNavToTouristWallet: (String) -> Unit,
    onNavToBookingHistory: (String) -> Unit,
    onNavToPreference: (String) -> Unit,
   // onNavToEditProfile: (String) -> Unit,
    onNavToVerifyAccount: (String) -> Unit,
    onNavToHostDashboard: (String) -> Unit,
    onNavToFavorite: (String) -> Unit,
    onLogout: () -> Unit,
){
    Log.d("ID", "$touristId")

    LaunchedEffect(touristId) {
        profileViewModel?.fetchUserData(touristId)
        Log.d("Fetching", "")
    }

    val isLoading = profileViewModel?.isLoading?.collectAsState()
    val isUserVerified = profileViewModel?.isUserVerified?.collectAsState()

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val currentUser = profileViewModel?.currentUser?.collectAsState()?.value

    val currentProfileData = ProfileData(
        profilePicture = currentUser?.profilePicture ?: "",
        fullName = "${currentUser?.firstName} ${currentUser?.lastName}",
        userName = "@${currentUser?.username}"
    )

    val horizontalPaddingValue = 16.dp

    var selectedItemIndex by rememberSaveable {
        mutableIntStateOf(2)
    }

//    var openDialog = remember { mutableStateOf(false) }
//
//    if (isUserVerified?.value == true) {
//        onNavToHostDashboard(touristId)
//    } else if (isUserVerified?.value == false) {
//        openDialog.value = true
//    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
//<<<<<<< HEAD
        if (isLoading!!.value){
            LoadingScreen(isLoadingCompleted = false, isLightModeActive = true)
        } else {
            Scaffold(
                bottomBar = {
                    navController?.let {
                        TouristBottomNavigationBar(
                            touristId = touristId,
                            navController = it,
                            selectedItemIndex = selectedItemIndex,
                            onItemSelected = { newIndex ->
                                selectedItemIndex = newIndex
//=======
        /*Scaffold(
            bottomBar = {
                navController?.let {
                    TouristBottomNavigationBar(
                        touristId = touristId,
                        navController = it,
                        selectedItemIndex = selectedItemIndex,
                        onItemSelected = { newIndex ->
                            selectedItemIndex = newIndex
                        }
                    )
                }
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
                        onLogout = {
                            loginViewModel?.updateIsSuccessLogin(false)
                            onLogout()
                        }

                    )
                }
                item{
                    UserProfileComposable(
                        profileData = currentProfileData,
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
                        AppFilledCard(
                            cardText = "Bookings",
                            onClick = {
                                onNavToBookingHistory(touristId)
                            }
                        )
                        AppOutlinedCard(
                            cardText = "Preferences",
                            onClick = {
                                onNavToPreference(touristId)
                            }
                        )
                        AppFilledCard(
                            cardText = "Itinerary",
                            onClick = {
                                // sa touristId na variable nakastore yung Id ng Current User
                                // Profile Screen -> Itinerary Planner
                                // Pagente
                                *//*Toast.makeText(
                                    context,
                                    "TouristId: $touristId",
                                    Toast.LENGTH_SHORT
                                ).show()*//*

                                val intent = Intent(context, IteniraryActivity::class.java)
                                intent.putExtra("cameFromProfile", true)
                                intent.putExtra("touristId", touristId)
                                intent.putExtra("staycationId", "-")
                                context.startActivity(intent)
>>>>>>> 192e417654c69a0bed929f93b656226992ddfedc*/
                            }
                        )
                    }
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
                            onLogout = {
                                loginViewModel?.updateIsSuccessLogin(false)
                                onLogout()
                            }

                        )
                    }
                    item{
                        UserProfileComposable(
                            profileData = currentProfileData,
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
                            AppFilledCard(
                                cardText = "Bookings",
                                onClick = {
                                    onNavToBookingHistory(touristId)
                                }
                            )
                            AppOutlinedCard(
                                cardText = "Preferences",
                                onClick = {
                                    onNavToPreference(touristId)
                                }
                            )
                            AppFilledCard(
                                cardText = "Itinerary",
                                onClick = {
                                    val intent = Intent(context, IteniraryActivity::class.java)
                                    intent.putExtra("cameFromProfile", true)
                                    intent.putExtra("touristId", touristId)
                                    intent.putExtra("staycationId", "-")
                                    context.startActivity(intent)
                                }
                            )
                        }
                    }
                    item{
                        Row(
                            modifier = Modifier
//                                .fillMaxWidth()
//                                //.padding(top = 20.dp, bottom = 10.dp)
//                                .padding(horizontal = horizontalPaddingValue)
                            ,
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            AppOutlinedCard(
                                cardText = "Favorites",
                                onClick = {
                                    onNavToFavorite(touristId)
                                },
                                modifier = Modifier
                                   // .padding(horizontal = horizontalPaddingValue)
                            )

                            AppOutlinedCard(
                                cardText = "Verify your account",
                                onClick = {
                                    onNavToVerifyAccount(touristId)
                                },
                                modifier = Modifier
                                    .padding(horizontal = horizontalPaddingValue)
                            )
                        }
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
                            onClick = {
                                /*TODO*/
                            },
                            modifier = Modifier
                                .padding(horizontal = horizontalPaddingValue)
                        )
                    }
                    item {
                        OptionsRow(
                            icon = R.drawable.payment,
                            rowText = "Wallet",
                            onClick = {
                                onNavToTouristWallet(touristId)
                            },
                            modifier = Modifier
                                .padding(horizontal = horizontalPaddingValue)
                        )
                    }
                    item {
                        OptionsRow(
                            icon = R.drawable.privacy,
                            rowText = "Privacy",
                            onClick = {
                                /*TODO*/
                            },
                            modifier = Modifier
                                .padding(horizontal = horizontalPaddingValue)
                        )
                    }
                    item {
                  /*      Text(
                            text = if (isUserVerified?.value == true) "Hosting" else "Account needs verification",
                          //  fontColor = if (isUserVerified?.value == true) Color.Black else Color(0xffCC0033),
                            color = if (isUserVerified?.value == true) Color.Black else Color(0xFFCC0033),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier
                                .padding(vertical = 10.dp, horizontal = horizontalPaddingValue)
                                .fillMaxWidth()
                                .wrapContentWidth(align = Alignment.Start)
                        )*/

                        Text(
                            text = "Hosting",
                            //  fontColor = if (isUserVerified?.value == true) Color.Black else Color(0xffCC0033),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier
                                .padding(vertical = 10.dp, horizontal = horizontalPaddingValue)
                                .fillMaxWidth()
                                .wrapContentWidth(align = Alignment.Start)
                        )
                    }


//    LaunchedEffect(isUserVerified?.value) {
//        if (isUserVerified != null) {
//            if (isUserVerified.value == true) {
//                onNavToHostDashboard(touristId)
//            } else if (isUserVerified.value == false) {
//                openDialog.value = true
//            }
//        }
//    }

                    item {

                        if (isUserVerified?.value == true) {
                            OptionsRow(
                                icon = R.drawable.resource_switch,
                                rowText = "Switch to hosting",
                                onClick = {
                                  onNavToHostDashboard(touristId)
                                },
                                modifier = Modifier
                                    .padding(horizontal = horizontalPaddingValue)
                            )
                        } else {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                    }
                                    .padding(horizontal = horizontalPaddingValue)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                ) {
                                    Icon(
                                        imageVector = ImageVector.vectorResource(R.drawable.resource_switch),
                                        contentDescription = ""
                                    )
                                    Spacer(modifier = Modifier.width(20.dp))
                                    Text(
                                        text = "Hosting",
                                        fontSize = 16.sp,
                                        textDecoration = TextDecoration.LineThrough,
                                        color = Color.LightGray,
                                      //  textDecor = TextDecoration.LineThrough,
                                        fontWeight = FontWeight.Medium,
                                        modifier = Modifier.padding(top = 3.dp)
                                    )
                                    Text(
                                        text = "   Verification required",
                                        color = Color(0xFFCC0033),
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
                            onClick = {
                                /*TODO*/
                            },
                            modifier = Modifier
                                .padding(horizontal = horizontalPaddingValue)
                        )
                    }
                    item {
                        OptionsRow(
                            icon = R.drawable.document,
                            rowText = "Privacy Policy",
                            onClick = {
                                /*TODO*/
                            },
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

            /*if (openDialog.value) {
                Dialog(onDismissRequest = {
                    profileViewModel.setToNull()
                    openDialog.value = false

                }) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 20.dp
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            //.height(200.dp)
                            .padding(20.dp),
                    ) {
                        Column(
                            verticalArrangement = Arrangement.Center,
                        ) {

                            //   var text =

                            Text(
                                text = "Please verify your account first before proceeding.",
                                color = Color.Black,
                                fontSize = 18.sp,
                                modifier = Modifier
                                    .padding(horizontal = 20.dp)
                                    .padding(top = 25.dp),
                            )
                            Spacer(modifier = Modifier.height(30.dp))
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                //   horizontalArrangement = Arrangement.SpaceBetween,
                            ) {

                                TextButton(
                                    onClick = {
                                        profileViewModel.setToNull()
                                        openDialog.value = false

                                    },
                                    modifier = Modifier.padding(horizontal = 10.dp),
                                ) {
                                    Text("Dismiss", color = Color.Black)
                                }

                                Spacer(modifier = Modifier.weight(1f))
                                TextButton(
                                    onClick = {
                                        profileViewModel.setToNull()
                                        openDialog.value = false
                                        onNavToVerifyAccount(touristId)
                                    },
                                    modifier = Modifier.padding(horizontal = 10.dp),
                                ) {
                                    Text("Verify", color = Color.Black)
                                }

                            }
                        }
                    }
                }
            }*/
        }

    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBarWithIcon(headerText: String, onLogout: () -> Unit, headerIcon: ImageVector, modifier: Modifier = Modifier) {
    TopAppBar(
        title = {
            Text(
                text = headerText,
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium
            )
        },
        actions = {
                IconButton(onClick = { onLogout() }) {
                    Icon(
                        imageVector = headerIcon,
                        contentDescription = "",
                        modifier = Modifier.size(32.dp)
                    )
                }
        },
        modifier = modifier

    )
}

@Composable
fun AppOutlinedCard(
    cardText: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    cornerSize: Dp = 10.dp
) {
    Card(
        modifier = modifier.clickable { onClick() },
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
fun AppFilledCard(
    cardText: String,
    cornerSize: Dp = 10.dp,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.clickable { onClick() },
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
fun UserProfileComposable(profileData : ProfileData, modifier: Modifier = Modifier){

    Box(
        modifier = modifier
            .padding(vertical = 10.dp)
            .size(140.dp)
            .clip(RoundedCornerShape(100.dp))
    ){

        val imageLoader = rememberAsyncImagePainter(
            ImageRequest.Builder(LocalContext.current).data(data = profileData.profilePicture)
                .apply(block = fun ImageRequest.Builder.() {
                    crossfade(true)
                }).build()
        )

        Image(
            painter = imageLoader,
            contentDescription = "Profile Picture",
            contentScale = ContentScale.Crop
        )
    }
    Text(
        text = profileData.fullName,
        fontSize = 24.sp,
        fontWeight = FontWeight.Medium
    )
    Text(
        text = profileData.userName,
        fontSize = 16.sp,
        fontWeight = FontWeight.Medium,
        color = Color(0xFF999999)
    )

}

@Composable
fun OptionsRow(
    icon: Int,
    rowText: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
){
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
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

    val profileViewModel = viewModel(modelClass = ProfileViewModel::class.java)
    val loginViewModel = viewModel(modelClass = LoginViewModel::class.java)


    TouristProfileScreen(
        profileViewModel = profileViewModel,
        loginViewModel = loginViewModel,
        touristId = "ITZbCFfF7Fzqf1qPBiwx",
        onNavToTouristWallet = {

        },
        onNavToBookingHistory = {

        },
        onNavToPreference = {

        },
        onNavToVerifyAccount = {

        },
        onNavToHostDashboard = {

        },
        onLogout = {

        },
        onNavToFavorite = {

        },
    )
}