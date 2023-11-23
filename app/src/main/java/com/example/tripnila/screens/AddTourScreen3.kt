package com.example.tripnila.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tripnila.R
import com.example.tripnila.model.HostTourViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTourScreen3(
    listingType: String = "",
    hostTourViewModel: HostTourViewModel? = null,
    onNavToNext: (String) -> Unit,
    onNavToBack: () -> Unit,
){

    val localFocusManager = LocalFocusManager.current

    val title = hostTourViewModel?.tour?.collectAsState()?.value?.tourTitle
    val description = hostTourViewModel?.tour?.collectAsState()?.value?.tourDescription
    val duration = hostTourViewModel?.tour?.collectAsState()?.value?.tourDuration
    val language = hostTourViewModel?.tour?.collectAsState()?.value?.tourLanguage
    val contactNumber = hostTourViewModel?.tour?.collectAsState()?.value?.tourContact
    val email = hostTourViewModel?.tour?.collectAsState()?.value?.tourEmail
    val facebook = hostTourViewModel?.tour?.collectAsState()?.value?.tourFacebook
    val instagram = hostTourViewModel?.tour?.collectAsState()?.value?.tourInstagram

    Surface(
        modifier = Modifier
            .fillMaxSize()
    ){
        Scaffold(
            bottomBar = {
                AddListingBottomBookingBar(
                    leftButtonText = "Back",
                    onNext = {
                        onNavToNext(listingType)
                    },
                    onCancel = {
                        onNavToBack()
                    },
                    enableRightButton = title != "" && description != "" && duration != 0 && language != "" && contactNumber != ""
                )
            },
            topBar = {
                TopAppBar(
                    title = {
                        SaveAndExitButton(
                            onClick = { /*TODO*/ }
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.White
                    )
                )
            }
        ){
            Column(
                modifier = Modifier
                    .background(Color.White)
                    .fillMaxWidth()
                    .padding(horizontal = 25.dp, vertical = 20.dp)
                    .padding(it)
            ) {
                Text(
                    text = "Give basic information about your tour",
                    color = Color(0xff333333),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .width(250.dp)
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(15.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 25.dp)
                ) {
                    Text(
                        text = "What is your tour called?",
                        color = Color(0xff333333),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
//                    ShortBasicTextFieldWithLimit(
//                        maxCharacterLimit = 50,
//                        modifier = Modifier
//                    )
                    Text(
                        text = "Now, tell us what you’ll do on the tour",
                        color = Color(0xff333333),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                    /*TODO*/
//                    LongBasicTextFieldWithCharacterLimit(
//                        inputText = ,
//                        maxCharacterLimit = 500
//                    )
                    Text(
                        text = "How long will the tour be?",
                        color = Color(0xff333333),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
//                    BasicTextFieldWithLeadingIcon(
//                        icon = R.drawable.clock,
//                        placeholder = "* hours",
//                        KeyboardOptions(
//                            imeAction = ImeAction.Done
//                        ),
//                        keyboardActions = KeyboardActions {
//                            localFocusManager.clearFocus()
//                        },
//                    )
                    Text(
                        text = "What languages can you offer?",
                        color = Color(0xff333333),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
//                    BasicTextFieldWithLeadingIcon(
//                        icon = R.drawable.language,
//                        placeholder = "English",
//                        KeyboardOptions(
//                            imeAction = ImeAction.Done
//                        ),
//                        keyboardActions = KeyboardActions {
//                            localFocusManager.clearFocus()
//                        },
//                    )
                    Text(
                        text = "Provide some contact information",
                        color = Color(0xff333333),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Column {
//                        BasicTextFieldWithLeadingIcon(
//                            icon = R.drawable.telephone,
//                            placeholder = "09********",
//                            KeyboardOptions(
//                                imeAction = ImeAction.Next
//                            ),
//                            keyboardActions = KeyboardActions {
//                                localFocusManager.moveFocus(FocusDirection.Down)
//                            },
//                        )
//                        Spacer(modifier = Modifier.height(8.dp))
//                        BasicTextFieldWithLeadingIcon(
//                            icon = R.drawable.email,
//                            placeholder = "abc@lbgrill.com",
//                            KeyboardOptions(
//                                imeAction = ImeAction.Next
//                            ),
//                            keyboardActions = KeyboardActions {
//                                localFocusManager.moveFocus(FocusDirection.Down)
//                            },
//                        )
//                        Spacer(modifier = Modifier.height(8.dp))
//                        BasicTextFieldWithLeadingIcon(
//                            icon = R.drawable.facebook,
//                            placeholder = "facebook.com/abc",
//                            KeyboardOptions(
//                                imeAction = ImeAction.Done
//                            ),
//                            keyboardActions = KeyboardActions {
//                                localFocusManager.clearFocus()
//                            },
//                        )
//                        Spacer(modifier = Modifier.height(8.dp))
//                        BasicTextFieldWithLeadingIcon(
//                            icon = R.drawable.instagram,
//                            placeholder = "@abc",
//                            KeyboardOptions(
//                                imeAction = ImeAction.Done
//                            ),
//                            keyboardActions = KeyboardActions {
//                                localFocusManager.clearFocus()
//                            },
//                        )
                    }

                }

                Spacer(modifier = Modifier.weight(1f))
                AddListingStepIndicator(modifier = Modifier, currentPage = 0, pageCount = 4)

            }
        }
    }
}

@Preview
@Composable
private fun AddTourPreview(){


}

@Preview
@Composable
private fun AddTourScreen3Preview(){
    //AddTourScreen3({},{()})
}