package com.itenirary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.tripnila.HostRoutes
import com.example.tripnila.R
import com.example.tripnila.model.BusinessManagerViewModel
import com.example.tripnila.screens.BusinessBottomBookingBar
import com.example.tripnila.screens.BusinessManagerScreen

class BusinessActivityDetails : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val businessManagerViewModel = viewModel(modelClass = BusinessManagerViewModel::class.java)

            BusinessManagerScreen(
                businessManagerViewModel = businessManagerViewModel,
                businessId = intent.getStringExtra("businessId")!!,
                hostId = intent.getStringExtra("touristId")!!,
                onNavToEditBusiness = { a,b,c ->

                },
                onNavToDashboard = {

                }
            )
        }
    }
}