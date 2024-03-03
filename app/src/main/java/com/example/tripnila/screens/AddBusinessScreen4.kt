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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tripnila.R
import com.example.tripnila.model.AddBusinessViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBusinessScreen4(
    listingType: String = "",
    addBusinessViewModel: AddBusinessViewModel? = null,
    onNavToNext: (String) -> Unit,
    onNavToBack: () -> Unit,
){

    val localFocusManager = LocalFocusManager.current

    var mutableTitle = remember {
        mutableStateOf( addBusinessViewModel?.business?.value?.businessTitle)
    }

    var mutableDescription = remember { mutableStateOf( addBusinessViewModel?.business?.value?.businessDescription) }
    var mutableContact = remember { mutableStateOf( addBusinessViewModel?.business?.value?.businessContact) }
    var mutableEmail = remember { mutableStateOf( addBusinessViewModel?.business?.value?.businessEmail) }
    var mutableUrl = remember { mutableStateOf( addBusinessViewModel?.business?.value?.businessURL) }

    val title = addBusinessViewModel?.business?.collectAsState()?.value?.businessTitle
    val description = addBusinessViewModel?.business?.collectAsState()?.value?.businessDescription
    val contactNumber = addBusinessViewModel?.business?.collectAsState()?.value?.businessContact
    val email = addBusinessViewModel?.business?.collectAsState()?.value?.businessEmail
    val url = addBusinessViewModel?.business?.collectAsState()?.value?.businessURL

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
                    enableRightButton = title != "" && description != "" && contactNumber != ""

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
                Text(
                    text = "Give basic information about your business",
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
                        text = "What is your business called?",
                        color = Color(0xff333333),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                    mutableTitle?.let { title ->
                        ShortBasicTextFieldWithLimit(
                            inputText = mutableTitle,
                            maxCharacterLimit = 50,
                            onValueChange = { newText ->
                                addBusinessViewModel?.setTitle(newText)
                            },
                            modifier = Modifier
                        )
                    }
                    Text(
                        text = "Now, give us some description",
                        color = Color(0xff333333),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )

                    LongBasicTextFieldWithCharacterLimit(
                        inputText = mutableDescription,
                        maxCharacterLimit = 500,
                        onTextChanged = { newText ->
                            addBusinessViewModel?.setDescription(newText)
                        }
                    )
                    Text(
                        text = "Provide some contact information",
                        color = Color(0xff333333),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Column {
                        mutableContact?.let { contact ->
                            BasicTextFieldWithLeadingIcon(
                                inputText = contact,
                                onTextChange = { newText ->
                                    addBusinessViewModel?.setContact(newText)
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
                                addBusinessViewModel?.setEmail(newText)
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
                        Spacer(modifier = Modifier.height(8.dp))
                        BasicTextFieldWithLeadingIcon(
                            inputText = mutableUrl,
                            onTextChange = { newText ->
                                addBusinessViewModel?.setUrl(newText)
                            },
                            icon = R.drawable.web,
                            placeholder = "lbgrill.com",
                            keyboardOptions = KeyboardOptions(
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions {
                                localFocusManager.clearFocus()
                            },
                        )
                    }

                }

                Spacer(modifier = Modifier.weight(1f))
                AddListingStepIndicator(modifier = Modifier, currentPage = 0, pageCount = 4)

            }
        }
    }
}

@Composable
fun BasicTextFieldWithLeadingIcon(
    inputText: MutableState<String?>,
    icon: Int,
    placeholder: String,
    keyboardOptions: KeyboardOptions,
    keyboardActions: KeyboardActions,
    onTextChange: (String) -> Unit,
    modifier: Modifier = Modifier
){

    BasicTextField(
        value = inputText.value ?: "",
        onValueChange = {
            inputText.value = it
            onTextChange(it)
        },
        textStyle = TextStyle(fontSize = 12.sp, color = Color(0xFF6B6B6B)),
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier
                    .padding(vertical = 4.dp, horizontal = 6.dp)
                    .fillMaxWidth(),
                contentAlignment = Alignment.CenterStart
            ) {
                Row {
                    Icon(
                        imageVector = ImageVector.vectorResource(icon),
                        contentDescription = "",
                        tint = Color(0xFF6B6B6B)
                    )
                    Spacer(modifier = Modifier.width(15.dp))
                    if (inputText.value?.isEmpty() == true) {
                        Text(
                            text = placeholder,
                            fontSize = 12.sp,
                            color = Color(0xFF6B6B6B),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                Row {
                    Spacer(modifier = Modifier.width(32.dp))
                    innerTextField()
                }
            }
        },
        singleLine = true,
        modifier = modifier
            .fillMaxWidth()
            .height(30.dp)
            .border(1.dp, Color(0xFF999999), shape = RoundedCornerShape(10.dp))
    )
}

@Composable
fun ShortBasicTextFieldWithLimit(
    inputText: MutableState<String?>,
    maxCharacterLimit: Int,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
){

    val localFocusManager = LocalFocusManager.current
    val remainingCharacters = maxCharacterLimit - inputText.value?.length!!

    Column {
        BasicTextField(
            value = inputText.value ?: "",
            onValueChange = {
                if (it.length <= maxCharacterLimit) {
                    inputText.value = it
                    onValueChange(it)
                }
            },
            textStyle = TextStyle(fontSize = 12.sp, color = Color(0xFF6B6B6B)),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions {
                localFocusManager.clearFocus()
            },
            singleLine = true,
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .padding(vertical = 4.dp, horizontal = 6.dp)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Row {
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
            },
            modifier = modifier
                .fillMaxWidth()
                .height(30.dp)
                .border(1.dp, Color(0xFF999999), shape = RoundedCornerShape(10.dp))

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
            color = Color(0xFF6B6B6B)
        )
    }
}

@Preview
@Composable
private fun AddBusinessPreview(){


}

@Preview
@Composable
private fun AddBusinessScreen4Preview(){
   // AddBusinessScreen4()
}