package com.example.tripnila.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tripnila.R
import com.example.tripnila.common.*

@Composable
fun SignupScreen(){

    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .offset(y = (-40).dp)
        ){
            Image(
                painter = painterResource(id = R.drawable.polygon1),
                contentDescription = "",
                contentScale = ContentScale.FillWidth,
                modifier = Modifier.size(size = 300.dp)
            )
        }
        Box(modifier = Modifier
            .fillMaxSize()
            .offset(y = 650.dp)
        ){
            Image(
                painter = painterResource(id = R.drawable.polygon2),
                contentDescription = "",
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    //.size(size = 400.dp)
                    .fillMaxWidth()
            )
        }
        TripNilaIcon()
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 115.dp)
        ) {
            Text(
                text = "Create an account",
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .padding(bottom = 24.dp)
            )
            TextFieldWithIcon(
                labelValue = "First Name",
                painterResource(id = R.drawable.person)
            )
            Spacer(
                modifier = Modifier
                    .height(15.dp)
            )
            TextFieldWithIcon(
                labelValue = "Middle Name",
                painterResource(id = R.drawable.person)
            )
            Spacer(
                modifier = Modifier
                    .height(15.dp)
            )
            TextFieldWithIcon(
                labelValue = "Last Name",
                painterResource(id = R.drawable.person)
            )
            Spacer(
                modifier = Modifier
                    .height(15.dp)
            )
            TextFieldWithIcon(
                labelValue = "Username",
                painterResource(id = R.drawable.person)
            )
            Spacer(
                modifier = Modifier
                    .height(15.dp)
            )
            CreatePasswordFieldWithIcon(
                labelValue = "Password",
                painterResource(id = R.drawable.encrypted)
            )
            Spacer(
                modifier = Modifier
                    .height(15.dp)
            )
            PasswordFieldWithIcon(
                labelValue = "Confirm Password",
                painterResource(id = R.drawable.encrypted)
            )
            Spacer(
                modifier = Modifier
                    .height(15.dp)
            )
            Agreement(
                modifier = Modifier.offset(x = (-20).dp)
            )
            Spacer(
                modifier = Modifier
                    .height(20.dp)
            )
            BookingFilledButton(
                buttonText = "Sign up",
                onClick = {},
                modifier = Modifier.width(width = 216.dp)
            )
        }
    }

}

@Composable
fun Agreement(modifier: Modifier = Modifier) {

    val checkedState = remember { mutableStateOf(false) }

    Box(
        modifier
    ) {

        Checkbox(
            checked = checkedState.value,
            onCheckedChange = { checkedState.value = it },
            colors = CheckboxDefaults.colors(
                checkedColor = Orange
            )
        )
        Row(
            modifier = Modifier
                .offset(x = 45.dp, y = 12.dp)
                .align(Alignment.TopStart)
        ) {
            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(
                        color = Color.Black,
                        fontSize = 9.sp)
                    ) {
                        append("I agree to the ")
                    }
                    withStyle(style = SpanStyle(
                        color = Color.Black,
                        fontSize = 9.sp,
                        textDecoration = TextDecoration.Underline,
                        fontWeight = FontWeight.Medium)
                    ) {
                        append("TripNILA’s Rules and Regulations and Privacy Policy.")
                    }
                },
                modifier = Modifier.width(240.dp)
            )
        }
    }
}


@Preview
@Composable
fun LoginScreenPreview(){
    SignupScreen()
}

