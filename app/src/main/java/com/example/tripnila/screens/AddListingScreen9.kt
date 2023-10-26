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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddListingScreen9(){

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
                        LongBasicTextFieldWithCharacterLimit(maxCharacterLimit = 50)
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
                        LongBasicTextFieldWithCharacterLimit(maxCharacterLimit = 500)
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
                AddListingStepIndicator(modifier = Modifier, currentPage = 1, pageCount = 4)
            }
        }
    }
}

@Composable
fun LongBasicTextFieldWithCharacterLimit(maxCharacterLimit: Int, modifier: Modifier = Modifier){

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
            modifier = modifier
                .fillMaxWidth()
                .height(95.dp)
                .border(1.dp, Color(0xFF999999), shape = RoundedCornerShape(10.dp)),
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
            color = Color(0xFF6B6B6B)
        )
    }

}

@Preview
@Composable
private fun AddListing8Preview(){
    LongBasicTextFieldWithCharacterLimit(maxCharacterLimit = 50)

}

@Preview
@Composable
private fun AddListingScreen8Preview(){
    AddListingScreen9()
}
