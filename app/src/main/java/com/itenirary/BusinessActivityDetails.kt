package com.itenirary

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tripnila.model.BusinessDetailViewModel
import com.example.tripnila.screens.BusinessDetailsScreen

class BusinessActivityDetails : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val businessDetailViewModel = viewModel(modelClass = BusinessDetailViewModel::class.java)

            BusinessDetailsScreen(
                businessDetailViewModel = businessDetailViewModel,
                businessId = intent.getStringExtra("businessId")!!,
                touristId = intent.getStringExtra("touristId")!!,
            )
        }
    }
}