package com.example.tripnila.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tripnila.R
import com.example.tripnila.model.AddListingViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddListingScreen9(
    listingType: String = "Staycation",
    addListingViewModel: AddListingViewModel? = null,
    onNavToNext: (String) -> Unit,
    onNavToBack: () -> Unit,
){

    var staycationTitle = remember { mutableStateOf(addListingViewModel?.staycation?.value?.staycationTitle) }
    var staycationDescription = remember { mutableStateOf(addListingViewModel?.staycation?.value?.staycationDescription) }
    var mutableContact = remember { mutableStateOf(addListingViewModel?.staycation?.value?.phoneNo) }
    var mutableEmail = remember { mutableStateOf(addListingViewModel?.staycation?.value?.email) }
    val localFocusManager = LocalFocusManager.current

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
                    enableRightButton = addListingViewModel?.staycation?.collectAsState()?.value?.staycationTitle != ""
                            && addListingViewModel?.staycation?.collectAsState()?.value?.staycationDescription != ""
                )
            },
            topBar = {
                TopAppBar(
                    title = {
                        /*SaveAndExitButton(
                            onClick = { *//*TODO*//* }
                        )*/
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

                LazyColumn(
                    //contentPadding = PaddingValues(vertical = 25.dp),
                    verticalArrangement = Arrangement.spacedBy(15.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    item {
                        Text(
                            text = "Give your staycation a title",
                            color = Color(0xff333333),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier
                                // .width(300.dp)
                                .fillMaxWidth()
                                .padding(bottom = 10.dp)
                        )
                    }
                    item {
                        LongBasicTextFieldWithCharacterLimit(
                            inputText = staycationTitle,
                            maxCharacterLimit = 50,
                            onTextChanged = { newText ->
                                addListingViewModel?.setStaycationTitle(newText)
                            }
                        )
                    }
                    item {
                        Text(
                            text = "Now, give us some description",
                            color = Color(0xff333333),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier
                                // .width(300.dp)
                                .fillMaxWidth()
                                .padding(vertical = 10.dp)
                        )
                    }
                    item {
                        LongBasicTextFieldWithCharacterLimit(
                            inputText = staycationDescription,
                            maxCharacterLimit = 500,
                            onTextChanged = { newText ->
                                addListingViewModel?.setStaycationDescription(newText)
                            }
                        )
                    }
                    item{
                        Text(
                            text = "Provide some contact information",
                            color = Color(0xff333333),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    item {

                        Column {
                            mutableContact?.let { contact ->
                                BasicTextFieldWithLeadingIcon(
                                    inputText = contact,
                                    onTextChange = { newText ->
                                            addListingViewModel?.setStaycationPhone(newText)
                                    },
                                    icon = R.drawable.telephone,
                                    placeholder = "09********",
                                    keyboardOptions = KeyboardOptions(
                                        imeAction = ImeAction.Next
                                    ),
                                    keyboardActions = KeyboardActions {
                                        localFocusManager.moveFocus(FocusDirection.Down)
                                    },
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            BasicTextFieldWithLeadingIcon(
                                inputText = mutableEmail,
                                onTextChange = { newText ->
                                    addListingViewModel?.setStaycationEmail(newText)
                                },
                                icon = R.drawable.email,
                                placeholder = "abc@lbgrill.com",
                                keyboardOptions = KeyboardOptions(
                                    imeAction = ImeAction.Next
                                ),
                                keyboardActions = KeyboardActions {
                                    localFocusManager.moveFocus(FocusDirection.Down)
                                },
                            )

                        }
                    }
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    AddListingStepIndicator(modifier = Modifier, currentPage = 1, pageCount = 4)
                }


            }
        }
    }


@Composable
fun LongBasicTextFieldWithCharacterLimit(
    inputText: MutableState<String?>,
    maxCharacterLimit: Int,
    onTextChanged: (String) -> Unit,
    modifier: Modifier = Modifier
){

//    var inputText by remember {
//        mutableStateOf("")
//    }
    val localFocusManager = LocalFocusManager.current
    val remainingCharacters = maxCharacterLimit - inputText.value?.length!!

    Column {
        BasicTextField(
            value = inputText.value ?: "",
            onValueChange = {
                if (it.length <= maxCharacterLimit) {
                    inputText.value = it
                    onTextChanged(it)
                }
            },
            textStyle = TextStyle(fontSize = 12.sp, color = Color(0xFF6B6B6B)),
            modifier = modifier
                .fillMaxWidth()
                .height(95.dp)
                .border(1.dp, if (remainingCharacters <= 0) Color.Red else Color(0xFF999999), shape = RoundedCornerShape(10.dp)),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions {
                localFocusManager.clearFocus()
            },
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .padding(vertical = 4.dp, horizontal = 6.dp)
                        .fillMaxWidth()
                    // .fillMaxHeight()
                ) {
                    Row(
                        // modifier =Modifier.fillMaxHeight(),
                        verticalAlignment = Alignment.Top
                    ) {
                        if (inputText.value?.isEmpty() == true) {
                            Text(
                                text = "Write something here...",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Normal,
                                color = Color(0xFF6B6B6B),
                                modifier = Modifier.weight(1f)
                            )
                        }

                    }
                    innerTextField()
                }
            }
        )
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("$remainingCharacters")
                }
                withStyle(style = SpanStyle(fontWeight = FontWeight.Medium)) {
                    append(" characters available")
                }
            },
            fontSize = 10.sp,
            color = if (remainingCharacters <= 0) Color.Red else Color(0xFF6B6B6B)
        )
    }

}

@Preview
@Composable
private fun AddListing8Preview(){
   // LongBasicTextFieldWithCharacterLimit(maxCharacterLimit = 50)

}

@Preview
@Composable
private fun AddListingScreen9Preview(){
    val addListingViewModel = viewModel(modelClass = AddListingViewModel::class.java)

    AddListingScreen9(
        addListingViewModel = addListingViewModel,
        onNavToBack = {},
        onNavToNext = {}
    )
}
