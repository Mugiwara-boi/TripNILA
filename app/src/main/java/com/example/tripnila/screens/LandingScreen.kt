package com.example.tripnila.screens

import LoginUiState
import LoginViewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterEnd
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.End
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tripnila.R
import com.example.tripnila.common.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LandingScreen(
    loginViewModel: LoginViewModel? = null
) {

    val context = LocalContext.current
    var openBottomSheet by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )


    if (openBottomSheet) {
        ModalBottomSheet(
            modifier = Modifier.fillMaxHeight(0.6f),
            sheetState = bottomSheetState,
            onDismissRequest = {
                openBottomSheet = false
            },
            dragHandle = {

                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    BottomSheetDefaults.DragHandle(modifier = Modifier.align(Center))
                    IconButton(
                        onClick = {
                            scope.launch { bottomSheetState.hide() }.invokeOnCompletion {
                                if (!bottomSheetState.isVisible) {
                                    openBottomSheet = false
                                }
                            }
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
            containerColor = Color.White
        ) {
            if (loginViewModel != null) {
                BottomSheetContent(
                    loginViewModel = loginViewModel,
                    openBottomSheet = openBottomSheet,
                    onSignInButtonClick = {
                        loginViewModel.loginUser(context)
//                        scope.launch { bottomSheetState.hide() }.invokeOnCompletion {
//                            if (!bottomSheetState.isVisible) {
//                                openBottomSheet = false
//                            }
//                        }
                    }
                )
            }
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = "background",
            contentScale = ContentScale.FillBounds
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
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
                    openBottomSheet = true
                },
                modifier = Modifier.width(width = 216.dp)
            )
            Spacer(modifier = Modifier.height(34.dp))
            Row {
                Text(text = "Don't have an account? ",
                    color = Color.White
                )
                UnderlinedText(
                    textLabel = "Sign up",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    onClick = {
                    }
                )
            }
        }
    }
}

@Composable
private fun BottomSheetContent(
    loginViewModel: LoginViewModel = LoginViewModel(),
    onSignInButtonClick: () -> Unit,
    openBottomSheet: Boolean
) {

    var localFocusManager = LocalFocusManager.current
    val prevOpenBottomSheet by remember { mutableStateOf(openBottomSheet) }

    LaunchedEffect(openBottomSheet) {
        if (prevOpenBottomSheet) {
            // Clear the fields in the LoginViewModel when the bottom sheet closes
            loginViewModel.setUsername("") // Clear username field
            loginViewModel.setPassword("") // Clear password field
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextFieldWithIcon(
            textValue = loginViewModel.loginUiState.username,
            onValueChange = {
                loginViewModel.setUsername(it)
            },
            labelValue = "Username",
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions {
                localFocusManager.moveFocus(FocusDirection.Down)
            },
            supportingText = null,
            painterResource(id = R.drawable.person),

        )
        Spacer(modifier = Modifier.height(23.dp))
        PasswordFieldWithIcon(
            password = loginViewModel.loginUiState.password,
            onValueChange = {
                loginViewModel.setPassword(it)
            },
            labelValue = "Password",
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions {
                localFocusManager.clearFocus()
            },
            painterResource(id = R.drawable.encrypted)
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
        BookingFilledButton(
            buttonText = "Sign in",
            onClick = {
                onSignInButtonClick()
            },
            modifier = Modifier.width(width = 216.dp),
            isLoading = loginViewModel.loginUiState.isLoading
        )
    }
}

@Preview
@Composable
private fun LandingPreview() {
    BookingFilledButton(
        buttonText = "Sign in",
        onClick = {

        },
        modifier = Modifier.width(width = 216.dp).height(42.dp),
        isLoading = false
    )
}




@Preview
@Composable
fun LandingPagePreview() {

    LandingScreen()
}