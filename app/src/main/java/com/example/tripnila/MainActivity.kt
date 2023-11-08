package com.example.tripnila

import LoginViewModel
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tripnila.model.SignupViewModel
import com.example.tripnila.screens.InboxScreen
import com.example.tripnila.screens.LandingScreen
import com.example.tripnila.screens.SignupScreen
import com.example.tripnila.ui.theme.MyApplicationTheme



class MainActivity : ComponentActivity() {

   // private val loginViewModel: LoginViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                val loginViewModel = viewModel(modelClass = LoginViewModel::class.java)
                val signupViewModel = viewModel(modelClass = SignupViewModel::class.java)

               // LandingScreen(loginViewModel)
                SignupScreen(signupViewModel)

            }
        }
    }
}
