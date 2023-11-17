package com.example.tripnila.screens

import android.annotation.SuppressLint
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tripnila.R
import com.example.tripnila.common.PasswordFieldWithIcon
import com.example.tripnila.common.TextFieldWithIcon
import com.example.tripnila.common.TripNilaHeader
import com.example.tripnila.common.UnderlinedText
import com.example.tripnila.model.LoginViewModel
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TempScreen(
    loginViewModel: LoginViewModel? = null
) {

    Surface {



    }



}



@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun TempScreenPreview(){
//    val scaffoldState = rememberBottomSheetScaffoldState(
//        bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed),
//        snackbarHostState = SnackbarHostState()
//    )
//    val coroutineScope = rememberCoroutineScope()
//
//    BottomSheetScaffold(
//        scaffoldState = scaffoldState,
//        sheetContent = { Text("This is the Bottom Sheet content") },
//        floatingActionButtonPosition = FabPosition.Center,
//        fo = {
//            FloatingActionButton(onClick = {
//                coroutineScope.launch {
//                    scaffoldState.snackbarHostState.showSnackbar("FAB clicked!")
//                }
//            }) {
//                Icon(Icons.Default.Add, contentDescription = "Add")
//            }
//        },
//        content = { innerPadding ->
//            Box(modifier = Modifier.padding(innerPadding)) {
//                Text("This is the main content")
//            }
//        }
//    )
}

//    if (openBottomSheet) {
//        BottomSheetScaffold(
//            modifier = Modifier.fillMaxHeight(0.6f),
//            scaffoldState = bottomSheetState,
//            onDismissRequest = {
//                openBottomSheet = false
//            },
//            dragHandle = {
//
//                Box(
//                    modifier = Modifier.fillMaxWidth()
//                ) {
//                    BottomSheetDefaults.DragHandle(modifier = Modifier.align(Alignment.Center))
//                    IconButton(
//                        onClick = {
//                            coroutineScope.launch { bottomSheetState.hide() }.invokeOnCompletion {
//                                if (!bottomSheetState.isVisible) {
//                                    openBottomSheet = false
//                                }
//                            }
//                        },
//                        modifier = Modifier
//                            .padding(end = 12.dp, top = 0.dp)
//                            .size(30.dp)
//                            .align(Alignment.CenterEnd)
//
//                    ) {
//                        Icon(
//                            imageVector = Icons.Filled.Close,
//                            contentDescription = "Close",
//                            tint = Color(0xFFCECECE),
//                            modifier = Modifier.size(30.dp)
//                        )
//                    }
//                }
//            },
//            containerColor = Color.White
//        ) {
//            LaunchedEffect(openBottomSheet) {
//                if (prevOpenBottomSheet) {
//                    // Clear the fields in the LoginViewModel when the bottom sheet closes
//                    loginViewModel?.setUsername("") // Clear username field
//                    loginViewModel?.setPassword("") // Clear password field
//                }
//            }
//
//            Column(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(top = 20.dp),
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                TextFieldWithIcon(
//                    textValue = loginViewModel?.loginUiState?.username ?: "",
//                    onValueChange = {
//                        loginViewModel?.setUsername(it)
//                    },
//                    labelValue = "Username",
//                    keyboardOptions = KeyboardOptions(
//                        imeAction = ImeAction.Next
//                    ),
//                    keyboardActions = KeyboardActions {
//                        localFocusManager.moveFocus(FocusDirection.Down)
//                    },
//                    supportingText = null,
//                    painterResource(id = R.drawable.person),
//
//                    )
//                Spacer(modifier = Modifier.height(23.dp))
//                PasswordFieldWithIcon(
//                    password = loginViewModel?.loginUiState?.password  ?: "",
//                    onValueChange = {
//                        loginViewModel?.setPassword(it)
//                    },
//                    labelValue = "Password",
//                    keyboardOptions = KeyboardOptions(
//                        keyboardType = KeyboardType.Password,
//                        imeAction = ImeAction.Done
//                    ),
//                    keyboardActions = KeyboardActions {
//                        localFocusManager.clearFocus()
//                    },
//                    painterResource(id = R.drawable.encrypted)
//                )
//                Spacer(
//                    modifier = Modifier
//                        .height(23.dp)
//                )
//                UnderlinedText(
//                    textLabel = "Forgot your password?",
//                    color = Color.Black.copy(alpha = 0.4f),
//                    fontSize = 14.sp,
//                    fontWeight = FontWeight.Normal,
//                    onClick = {
//
//                    }
//                )
//                Spacer(
//                    modifier = Modifier
//                        .height(43.dp)
//                )
//                BookingFilledButton(
//                    buttonText = "Sign in",
//                    onClick = {
//                        loginViewModel?.loginUser()
//                    },
//                    modifier = Modifier.width(width = 216.dp),
//                    isLoading = loginViewModel?.loginResult?.collectAsState()?.value is OperationResult.Loading
//                )
//            }
//
//        }
//    }




//@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun TempScreen(
//    loginViewModel: LoginViewModel? = null
//) {
//
//
//    val snackbarHostState = remember { SnackbarHostState() }
//    val coroutineScope = rememberCoroutineScope()
//
//    val operationResult = loginViewModel?.loginResult?.collectAsState()
//    var openBottomSheet by remember { mutableStateOf(false) }
//    val bottomSheetState = rememberModalBottomSheetState(
//        skipPartiallyExpanded = true
//    )
//    val prevOpenBottomSheet by remember { mutableStateOf(openBottomSheet) }
//    var localFocusManager = LocalFocusManager.current
//
//    Scaffold(
//        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
//        modifier = Modifier.fillMaxSize()
//    ) {
//        Box(
//            modifier = Modifier.fillMaxSize()
//        ) {
//            Image(
//                painter = painterResource(id = R.drawable.background),
//                contentDescription = "background",
//                contentScale = ContentScale.Crop,
//                modifier = Modifier.fillMaxSize()
//            )
//        }
//        Column(
//            horizontalAlignment = Alignment.CenterHorizontally,
//            modifier = Modifier
//                .fillMaxSize()
//        ) {
//            Spacer(modifier = Modifier.height(90.dp))
//            TripNilaHeader(color = Color.White)
//            Text(
//                modifier = Modifier.offset(y = (-12).dp),
//                text = "TRIP IN MANILA",
//                color = Color.White,
//                style = MaterialTheme.typography.titleMedium
//            )
//            Spacer(modifier = Modifier.height(289.dp))
//            BookingFilledButton(
//                buttonText = "Sign in",
//                onClick = {
//                    openBottomSheet = true
//                    coroutineScope.launch {
//                        snackbarHostState.showSnackbar("hello")
//                    }
//                },
//                modifier = Modifier.width(width = 216.dp)
//            )
//            Spacer(modifier = Modifier.height(34.dp))
//            Row {
//                Text(
//                    text = "Don't have an account? ",
//                    color = Color.White
//                )
//                UnderlinedText(
//                    textLabel = "Sign up",
//                    color = Color.White,
//                    fontSize = 14.sp,
//                    fontWeight = FontWeight.Bold,
//                    onClick = {
//                    }
//                )
//            }
//
//
//            operationResult?.value?.let { result ->
//                when (result) {
//                    is OperationResult.Success -> {
//                        coroutineScope.launch {
//                            snackbarHostState.showSnackbar(result.message)
//                        }
//                    }
//                    is OperationResult.Error -> {
//                        coroutineScope.launch {
//                            snackbarHostState.showSnackbar(result.message)
//                        }
//                    }
//                    OperationResult.Loading -> {
//                        // Show loading indicator, progress bar, etc.
//                    }
//                }
//            }
//
//        }
//
//    }
//
//    if (openBottomSheet) {
//        ModalBottomSheet(
//            modifier = Modifier.fillMaxHeight(0.6f),
//            sheetState = bottomSheetState,
//            onDismissRequest = {
//                openBottomSheet = false
//            },
//            dragHandle = {
//
//                Box(
//                    modifier = Modifier.fillMaxWidth()
//                ) {
//                    BottomSheetDefaults.DragHandle(modifier = Modifier.align(Alignment.Center))
//                    IconButton(
//                        onClick = {
//                            coroutineScope.launch { bottomSheetState.hide() }.invokeOnCompletion {
//                                if (!bottomSheetState.isVisible) {
//                                    openBottomSheet = false
//                                }
//                            }
//                        },
//                        modifier = Modifier
//                            .padding(end = 12.dp, top = 0.dp)
//                            .size(30.dp)
//                            .align(Alignment.CenterEnd)
//
//                    ) {
//                        Icon(
//                            imageVector = Icons.Filled.Close,
//                            contentDescription = "Close",
//                            tint = Color(0xFFCECECE),
//                            modifier = Modifier.size(30.dp)
//                        )
//                    }
//                }
//            },
//            containerColor = Color.White
//        ) {
//            LaunchedEffect(openBottomSheet) {
//                if (prevOpenBottomSheet) {
//                    // Clear the fields in the LoginViewModel when the bottom sheet closes
//                    loginViewModel?.setUsername("") // Clear username field
//                    loginViewModel?.setPassword("") // Clear password field
//                }
//            }
//
//            Column(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(top = 20.dp),
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                TextFieldWithIcon(
//                    textValue = loginViewModel?.loginUiState?.username ?: "",
//                    onValueChange = {
//                        loginViewModel?.setUsername(it)
//                    },
//                    labelValue = "Username",
//                    keyboardOptions = KeyboardOptions(
//                        imeAction = ImeAction.Next
//                    ),
//                    keyboardActions = KeyboardActions {
//                        localFocusManager.moveFocus(FocusDirection.Down)
//                    },
//                    supportingText = null,
//                    painterResource(id = R.drawable.person),
//
//                    )
//                Spacer(modifier = Modifier.height(23.dp))
//                PasswordFieldWithIcon(
//                    password = loginViewModel?.loginUiState?.password  ?: "",
//                    onValueChange = {
//                        loginViewModel?.setPassword(it)
//                    },
//                    labelValue = "Password",
//                    keyboardOptions = KeyboardOptions(
//                        keyboardType = KeyboardType.Password,
//                        imeAction = ImeAction.Done
//                    ),
//                    keyboardActions = KeyboardActions {
//                        localFocusManager.clearFocus()
//                    },
//                    painterResource(id = R.drawable.encrypted)
//                )
//                Spacer(
//                    modifier = Modifier
//                        .height(23.dp)
//                )
//                UnderlinedText(
//                    textLabel = "Forgot your password?",
//                    color = Color.Black.copy(alpha = 0.4f),
//                    fontSize = 14.sp,
//                    fontWeight = FontWeight.Normal,
//                    onClick = {
//
//                    }
//                )
//                Spacer(
//                    modifier = Modifier
//                        .height(43.dp)
//                )
//                BookingFilledButton(
//                    buttonText = "Sign in",
//                    onClick = {
//                        loginViewModel?.loginUser()
//                    },
//                    modifier = Modifier.width(width = 216.dp),
//                    isLoading = loginViewModel?.loginResult?.collectAsState()?.value is OperationResult.Loading
//                )
//            }
//
//        }
//    }
//}

