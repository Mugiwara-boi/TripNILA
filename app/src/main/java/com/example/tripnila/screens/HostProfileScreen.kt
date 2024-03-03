package com.example.tripnila.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.tripnila.R
import com.example.tripnila.common.HostBottomNavigationBar
import com.example.tripnila.common.LoadingScreen
import com.example.tripnila.common.TouristBottomNavigationBar
import com.example.tripnila.data.ProfileData
import com.example.tripnila.model.LoginViewModel
import com.example.tripnila.model.ProfileViewModel

@Composable
fun HostProfileScreen(
    profileViewModel: ProfileViewModel? = null,
    loginViewModel: LoginViewModel? = null,
    hostId: String = "",
    navController: NavHostController? = null,
    onNavToHome: (String) -> Unit,
    onLogout: () -> Unit,
){
  //  Log.d("ID", "$touristId")

    LaunchedEffect(hostId) {
        profileViewModel?.fetchUserData(hostId.substring(5))
        Log.d("Fetching", "")
    }

    val isLoading = profileViewModel?.isLoading?.collectAsState()

    val context = LocalContext.current

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


    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        if (isLoading!!.value) {
            LoadingScreen(isLoadingCompleted = false, isLightModeActive = true)
        } else {
            Scaffold(
                bottomBar = {
                    navController?.let {
                        HostBottomNavigationBar(
                            hostId = hostId,
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
                            horizontalArrangement = Arrangement.Center
                        ) {
                            AppFilledCard(
                                cardText = "Edit Profile",
                                onClick = {
                                    //  onNavToBookingHistory(touristId)
                                }
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
                                //     onNavToTouristWallet(touristId)
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
                        Text(
                            text = "Travelling",
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
                            rowText = "Switch to traveller",
                            onClick = {
                                onNavToHome(hostId.substring(5))
                            },
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
        }

    }

}
