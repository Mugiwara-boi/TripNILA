package com.example.tripnila.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterEnd
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.tripnila.R
import com.example.tripnila.common.*
import com.example.tripnila.data.Tourist
import com.example.tripnila.model.LoginViewModel
import com.example.tripnila.repository.UserRepository
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel? = null,
    onNavToHomeScreen: (String) -> Unit,
    onNavToPreferenceScreen: (String) -> Unit,
    onNavToSignupScreen : () -> Unit
) {
    val loginUiState = loginViewModel?.loginUiState?.collectAsState()
 //   val currentUserState: State<Tourist?>? = loginViewModel?.currentUser?.collectAsState()
//    val currentUser: Tourist? = currentUserState?.value

    var localFocusManager = LocalFocusManager.current
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = SheetState(
            skipPartiallyExpanded = true, // true
            initialValue = SheetValue.Hidden
        ),
        snackbarHostState = remember { SnackbarHostState() }
    )
    val coroutineScope = rememberCoroutineScope()
    var bottomSheetExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(bottomSheetExpanded) {
        if (bottomSheetExpanded) {
            coroutineScope.launch {
                scaffoldState.bottomSheetState.expand()
            }
        } else {
            coroutineScope.launch {
                scaffoldState.bottomSheetState.hide()
            }
        }
    }

    LaunchedEffect(bottomSheetExpanded) {
        if (!bottomSheetExpanded) {
            loginViewModel?.setUsername("")
            loginViewModel?.setPassword("")
        }
    }


    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        BottomSheetScaffold(
            modifier = Modifier.fillMaxSize(),
            sheetShape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp),
            scaffoldState = scaffoldState,
            sheetContainerColor = Color.White,
            sheetPeekHeight = 0.dp,
            snackbarHost = { SnackbarHost(hostState = scaffoldState.snackbarHostState) },
            sheetDragHandle = {
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    BottomSheetDefaults.DragHandle(modifier = Modifier.align(Center))
                    IconButton(
                        onClick = {
                            bottomSheetExpanded = !bottomSheetExpanded
                        },
                        modifier = Modifier
                            .padding(end = 12.dp, top = 0.dp)
                            .size(30.dp)
                            .align(CenterEnd)

                    ) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "Close",
                            tint = Color(0xFFCECECE),
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }
            },
            sheetContent = {
                // POPUP
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.6f)
                        .padding(top = 20.dp),
                    horizontalAlignment = CenterHorizontally
                ) {
                    TextFieldWithIcon(
                        textValue = loginUiState?.value?.username ?: "",
                        onValueChange = {
                            loginViewModel?.setUsername(it)
                        },
                        labelValue = "Username",
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions {  localFocusManager.moveFocus(FocusDirection.Down) },
                        supportingText = null,
                        painterResource = painterResource(id = R.drawable.person)
                        )
                    Spacer(modifier = Modifier.height(23.dp))
                    PasswordFieldWithIcon(
                        password = loginUiState?.value?.password ?: "",
                        onValueChange = {
                            loginViewModel?.setPassword(it)
                        },
                        labelValue = "Password",
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions {   localFocusManager.clearFocus() },
                        painterResource = painterResource(id = R.drawable.encrypted)
                    )
                    Spacer(
                        modifier = Modifier
                            .height(23.dp)
                    )
                    UnderlinedText(
                        textLabel = "Forgot your password?",
                        color = Color.Black.copy(alpha = 0.4f),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        onClick = {

                        }
                    )
                    Spacer(
                        modifier = Modifier
                            .height(43.dp)
                    )
                    BookingFilledButton(
                        buttonText = "Sign in",
                        onClick = {
                            loginViewModel?.loginUser()
                        },
                        modifier = Modifier.width(width = 216.dp),
                        isLoading = loginUiState?.value?.isLoading ?: false
                    )
                   // Text(text = loginUiState?.value?.isSuccessLogin.toString())

                    LaunchedEffect(loginUiState?.value?.isSuccessLogin) {
                        val isSuccess = loginUiState?.value?.isSuccessLogin

                        isSuccess?.let { success ->
                            val snackbarMessage = if (success) {
                                "Login successful"
                            } else {
                                "Login failed"
                            }

                            if (success) {
                                if (loginViewModel.currentUser?.value?.preferences?.isEmpty() == true) {
                                    loginViewModel.currentUser.value?.let { onNavToPreferenceScreen.invoke(it.touristId) }
                                } else {
                                    loginViewModel.currentUser.value?.let { onNavToHomeScreen.invoke(it.touristId) }
                                }

                            }
//                            coroutineScope.launch {
//                                scaffoldState.snackbarHostState.showSnackbar(snackbarMessage)
//                            }
                        }
                    }
                }
            }
        ) {
            // CONTENT
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.background),
                    contentDescription = "background",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
            Column(
                horizontalAlignment = CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Spacer(modifier = Modifier.height(90.dp))
                TripNilaHeader(color = Color.White)
                Text(
                    modifier = Modifier.offset(y = (-12).dp),
                    text = "TRIP IN MANILA",
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(289.dp))
                BookingFilledButton(
                    buttonText = "Sign in",
                    onClick = {
                        bottomSheetExpanded = !bottomSheetExpanded
                    },
                    modifier = Modifier.width(width = 216.dp)
                )
                Spacer(modifier = Modifier.height(34.dp))
                Row {
                    Text(
                        text = "Don't have an account? ",
                        color = Color.White
                    )
                    UnderlinedText(
                        textLabel = "Sign up",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        onClick = {
                            onNavToSignupScreen.invoke()
                        }
                    )
                }
            }
        }
    }
}



//                    LaunchedEffect(loginUiState?.value?.isSuccessLogin) {
//                        val isSuccess = loginUiState?.value?.isSuccessLogin
//
//                        isSuccess?.let { success ->
//                            val snackbarMessage = if (success) {
//                                "Login successful"
//                            } else {
//                                "Login failed"
//                            }
//
//                            if (success) {
//                                // Check if the user has preferences
//                                val hasPreferences = loginViewModel?.hasCurrentUserPreferences() ?: false
//
//                                coroutineScope.launch {
//                                    if (hasPreferences) {
//                                        // User has preferences, navigate to home screen
//                                        onNavToHomeScreen.invoke()
//                                    } else {
//                                        // User doesn't have preferences, navigate to preference screen
//                                        scaffoldState.snackbarHostState.showSnackbar("User doesn't have preferences")
//                                        // Add navigation logic to preference screen here
//                                    }
//
//                                    scaffoldState.snackbarHostState.showSnackbar(snackbarMessage)
//                                }
//                            }
//                        }
//                    }



@Preview
@Composable
private fun LandingPreview() {
    //        BookingFilledButton(
//            buttonText = "Sign in",
//            onClick = {
//                onSignInButtonClick()
//            },
//            modifier = Modifier.width(width = 216.dp)
//        )
//        if (loginViewModel.loginUiState.isLoading) {
//            CircularProgressIndicator(
//                color = Orange,
//                modifier = Modifier.padding(vertical = 20.dp)
//            ) // Display the progress indicator
//        }

}




@Preview
@Composable
fun LandingPagePreview() {

    LoginScreen(onNavToHomeScreen = {}, onNavToSignupScreen = {}, onNavToPreferenceScreen = {})
}