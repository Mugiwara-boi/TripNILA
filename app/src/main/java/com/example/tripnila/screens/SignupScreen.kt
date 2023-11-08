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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tripnila.R
import com.example.tripnila.common.*
import com.example.tripnila.model.SignupViewModel

@Composable
fun SignupScreen(
    signupViewModel: SignupViewModel? = null
){

    val signUpUiState = signupViewModel?.signUpUiState
    var localFocusManager = LocalFocusManager.current
    val context = LocalContext.current

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
                textValue = signUpUiState?.firstName ?: "",
                onValueChange = {
                    signupViewModel?.setFirstName(it)
                },
                labelValue = "First Name",
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions {
                    localFocusManager.moveFocus(FocusDirection.Down)
                },
                supportingText = null,
                painterResource(id = R.drawable.person)
            )
            Spacer(
                modifier = Modifier
                    .height(15.dp)
            )
            TextFieldWithIcon(
                textValue = signUpUiState?.middleName ?: "",
                onValueChange = {
                    signupViewModel?.setMiddleName(it)
                },
                labelValue = "Middle Name",
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions {
                    localFocusManager.moveFocus(FocusDirection.Down)
                },
                supportingText = null,
                painterResource(id = R.drawable.person)
            )
            Spacer(
                modifier = Modifier
                    .height(15.dp)
            )
            TextFieldWithIcon(
                textValue = signUpUiState?.lastName ?: "",
                onValueChange = {
                    signupViewModel?.setLastName(it)
                },
                labelValue = "Last Name",
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions {
                    localFocusManager.moveFocus(FocusDirection.Down)
                },
                supportingText = null,
                painterResource(id = R.drawable.person)
            )
            Spacer(
                modifier = Modifier
                    .height(15.dp)
            )
            TextFieldWithIcon(
                textValue = signUpUiState?.username ?: "",
                onValueChange = {
                    signupViewModel?.setUsername(it)
                },
                labelValue = "Username",
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions {
                    localFocusManager.moveFocus(FocusDirection.Down)
                },
                supportingText = null,
                painterResource(id = R.drawable.person)
            )
            Spacer(
                modifier = Modifier
                    .height(15.dp)
            )
            PasswordFieldWithIcon(
                password = signUpUiState?.password ?: "",
                onValueChange = {
                    signupViewModel?.setPassword(it)
                },
                labelValue = "Password",
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions {
                    localFocusManager.moveFocus(FocusDirection.Down)
                },
                painterResource(id = R.drawable.encrypted)
            )
            Spacer(
                modifier = Modifier
                    .height(15.dp)
            )
            PasswordFieldWithIcon(
                password = signUpUiState?.confirmPassword ?: "",
                onValueChange = {
                    signupViewModel?.setConfirmPassword(it)
                },
                labelValue = "Confirm Password",
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
                    .height(15.dp)
            )
            Agreement(
                isChecked = signUpUiState?.isAgree ?: false,
                onCheckedChange = {
                    signupViewModel?.checkAgreement(it)
                },
                modifier = Modifier.offset(x = (-20).dp)
            )
            Spacer(
                modifier = Modifier
                    .height(20.dp)
            )
            BookingFilledButton(
                buttonText = "Sign in",
                onClick = {
                    signupViewModel?.createUser(context)
                },
                modifier = Modifier.width(width = 216.dp),
                isLoading = signUpUiState?.isLoading ?: false
            )
        }
    }

}

@Composable
fun Agreement(
    isChecked: Boolean = false,
    onCheckedChange: (Boolean) -> Unit,
    checkedColor: Color = Orange,
    modifier: Modifier = Modifier
) {

    Box(
        modifier
    ) {

        Checkbox(
            checked = isChecked,
            onCheckedChange = { onCheckedChange(it) },
            colors = CheckboxDefaults.colors(
                checkedColor = checkedColor
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
                        fontWeight = FontWeight.Medium,
                    )
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

