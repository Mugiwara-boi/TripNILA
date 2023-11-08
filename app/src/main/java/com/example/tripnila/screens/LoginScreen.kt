package com.example.tripnila.screens
//
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxHeight
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.offset
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.text.KeyboardActions
//import androidx.compose.foundation.text.KeyboardOptions
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Surface
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.focus.FocusDirection
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.platform.LocalFocusManager
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.input.ImeAction
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.*
//import com.example.tripnila.R
//import com.example.tripnila.common.*
//
//@Composable
//fun LoginScreen() {
//
//    var localFocusManager = LocalFocusManager.current
//
//    Surface(
//        modifier = Modifier
//            .fillMaxSize()
//    ) {
//        Image(
//            painter = painterResource(id = R.drawable.background),
//            contentDescription = "background",
//            contentScale = ContentScale.FillBounds
//        )
//        Box(modifier = Modifier
//            .fillMaxSize()
//        ){
//            Image(
//                modifier = Modifier
//                    .height(400.dp)
//                    .fillMaxWidth(),
//                painter = painterResource(id = R.drawable.backgroundhalf),
//                contentDescription = "background",
//                contentScale = ContentScale.FillBounds
//
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
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .offset(y = 137.dp)
//                    .fillMaxHeight()
//                    .clip(shape = RoundedCornerShape(topStart = 60.dp))
//                    .background(color = Color.White)
//            ){
//                Column(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(top = 50.dp),
//                    horizontalAlignment = Alignment.CenterHorizontally
//                ) {
//                    TextFieldWithIcon(
//                        labelValue = "Username",
//                        keyboardOptions = KeyboardOptions(
//                            imeAction = ImeAction.Next
//                        ),
//                        keyboardActions = KeyboardActions {
//                            localFocusManager.moveFocus(FocusDirection.Down)
//                        },
//                        painterResource(id = R.drawable.person)
//                    )
//                    Spacer(modifier = Modifier.height(23.dp))
//                    PasswordFieldWithIcon(
//                        labelValue = "Password",
//                        keyboardOptions = KeyboardOptions(
//                            imeAction = ImeAction.Next
//                        ),
//                        keyboardActions = KeyboardActions {
//                            localFocusManager.moveFocus(FocusDirection.Down)
//                        },
//                        painterResource(id = R.drawable.encrypted)
//                    )
//                    Spacer(
//                        modifier = Modifier
//                            .height(23.dp)
//                    )
//                    UnderlinedText(
//                        textLabel = "Forgot your password?",
//                        color = Color.Black.copy(alpha = 0.4f),
//                        fontSize = 14.sp,
//                        fontWeight = FontWeight.Normal
//                    )
//                    Spacer(
//                        modifier = Modifier
//                            .height(43.dp)
//                    )
//                    BookingFilledButton(
//                        buttonText = "Sign in",
//                        onClick = {},
//                        modifier = Modifier.width(width = 216.dp)
//                    )
//                }
//            }
//        }
//    }
//}
//
//
//@Preview
//@Composable
//fun LoginScreenPrevieww(){
//    LoginScreen()
//}
//
