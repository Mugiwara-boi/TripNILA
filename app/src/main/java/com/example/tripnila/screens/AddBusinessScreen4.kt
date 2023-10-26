package com.example.tripnila.screens

import android.graphics.drawable.Icon
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.Placeholder
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBusinessScreen4(){

    val localFocusManager = LocalFocusManager.current

    Surface(
        modifier = Modifier
            .fillMaxSize()
    ){
        Scaffold(
            bottomBar = {
                AddListingBottomBookingBar()
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
                    ShortBasicTextFieldWithLimit(
                        maxCharacterLimit = 50,
                        modifier = Modifier
                    )
                    Text(
                        text = "Now, give us some description",
                        color = Color(0xff333333),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                    LongBasicTextFieldWithCharacterLimit(maxCharacterLimit = 500)
                    Text(
                        text = "Provide some contact information",
                        color = Color(0xff333333),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Column {
                        BasicTextFieldWithLeadingIcon(
                            icon = R.drawable.telephone,
                            placeholder = "09********",
                            KeyboardOptions(
                                imeAction = ImeAction.Next
                            ),
                            keyboardActions = KeyboardActions {
                                localFocusManager.moveFocus(FocusDirection.Down)
                            },
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        BasicTextFieldWithLeadingIcon(
                            icon = R.drawable.email,
                            placeholder = "abc@lbgrill.com",
                            KeyboardOptions(
                                imeAction = ImeAction.Next
                            ),
                            keyboardActions = KeyboardActions {
                                localFocusManager.moveFocus(FocusDirection.Down)
                            },
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        BasicTextFieldWithLeadingIcon(
                            icon = R.drawable.web,
                            placeholder = "lbgrill.com",
                            KeyboardOptions(
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
    icon: Int,
    placeholder: String,
    keyboardOptions: KeyboardOptions,
    keyboardActions: KeyboardActions,
    modifier: Modifier = Modifier
){
    var inputText by remember {
        mutableStateOf("")
    }

    BasicTextField(
        value = inputText,
        onValueChange = {
            inputText = it
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
                    if (inputText.isEmpty()) {
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
fun ShortBasicTextFieldWithLimit(maxCharacterLimit: Int, modifier: Modifier = Modifier){
    var inputText by remember {
        mutableStateOf("")
    }
    val localFocusManager = LocalFocusManager.current
    val remainingCharacters = maxCharacterLimit - inputText.length

    Column {
        BasicTextField(
            value = inputText,
            onValueChange = {
                if (it.length <= maxCharacterLimit) {
                    inputText = it
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
                        if (inputText.isEmpty()) {
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
    AddBusinessScreen4()
}