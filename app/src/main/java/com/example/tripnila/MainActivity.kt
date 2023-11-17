package com.example.tripnila

import android.os.Bundle
import android.view.ViewConfiguration
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.tripnila.data.Filter
import com.example.tripnila.data.Staycation
import com.example.tripnila.model.DetailViewModel
import com.example.tripnila.model.HomeViewModel
import com.example.tripnila.model.LoginViewModel
import com.example.tripnila.model.PreferenceViewModel
import com.example.tripnila.model.SignupViewModel
import com.example.tripnila.model.StaycationViewModel
import com.example.tripnila.repository.UserRepository
import com.example.tripnila.ui.theme.MyApplicationTheme
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {

    private val userRepository = UserRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {

                val staycationViewModel = viewModel(modelClass = StaycationViewModel::class.java)

               // FetchStaycationsContent()
       //         HomeScreen(staycationViewModel)


                val loginViewModel = viewModel(modelClass = LoginViewModel::class.java)
                val signupViewModel = viewModel(modelClass = SignupViewModel::class.java)
                val preferenceViewModel = viewModel(modelClass = PreferenceViewModel::class.java)
                val homeViewModel = viewModel(modelClass = HomeViewModel::class.java)
                val detailViewModel = viewModel(modelClass = DetailViewModel::class.java)

                Navigation(
                    loginViewModel = loginViewModel,
                    signupViewModel = signupViewModel,
                    preferenceViewModel = preferenceViewModel,
                    homeViewModel = homeViewModel,
                    detailViewModel = detailViewModel
                )


            }

        }
    }

    @Preview
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun TryDateRangePicker() {
        val snackState = remember { SnackbarHostState() }
        val snackScope = rememberCoroutineScope()
        SnackbarHost(hostState = snackState, Modifier.zIndex(1f))

        val state = rememberDateRangePickerState()
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Top) {
            // Add a row with "Save" and dismiss actions.
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp, end = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = { /* dismiss the UI */ }) {
                    Icon(Icons.Filled.Close, contentDescription = "Localized description")
                }
                TextButton(
                    onClick = {
                        snackScope.launch {
                            snackState.showSnackbar(
                                "Saved range (timestamps): " +
                                        "${state.selectedStartDateMillis!!..state.selectedEndDateMillis!!}"
                            )
                        }
                    },
                    enabled = state.selectedEndDateMillis != null
                ) {
                    Text(text = "Save")
                }
            }

            val redColor = Color.Red

            DateRangePicker(
                state = state,
                colors = DatePickerDefaults.colors(
                    containerColor = redColor,
                    titleContentColor = redColor,
                    headlineContentColor = redColor,
                    weekdayContentColor = redColor,
                    subheadContentColor = redColor,
                    yearContentColor = redColor,
                    currentYearContentColor = redColor,
                    selectedYearContentColor = redColor,
                    selectedYearContainerColor = redColor,
                    dayContentColor = redColor,
                    disabledDayContentColor = redColor,
                    selectedDayContentColor = redColor,
                    disabledSelectedDayContentColor = redColor,
                    selectedDayContainerColor = redColor,
                    disabledSelectedDayContainerColor = redColor,
                    todayContentColor = redColor,
                    todayDateBorderColor = redColor,
                    dayInSelectionRangeContentColor = redColor,
                    dayInSelectionRangeContainerColor = redColor
                ),
              //  modifier = Modifier.background(Color.White).weight(1f)
            )

        }

    }

    @Composable
    fun FetchStaycationsContent() {
        val userRepository = UserRepository()

        var staycations by remember { mutableStateOf(listOf<Staycation>()) }
        var selectedFilter by remember { mutableStateOf(Filter()) }

//        LaunchedEffect(true) {
//            // Fetch staycations when the composable is launched
//            staycations = userRepository.getAllStaycationsByTag("History")
//        }

        // Display staycations or handle UI accordingly
        // For simplicity, just printing the IDs here
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            staycations.forEach { staycation ->
                Text(text = "Staycation: ${staycation}")
            }
        }
    }

    @Composable
    fun HomeScreen(viewModel: StaycationViewModel) {
        val staycations: LazyPagingItems<Staycation> =
            viewModel.staycations.collectAsLazyPagingItems()

        LazyColumn {
            items(staycations.itemCount) { index ->
                val staycation = staycations[index]
                staycation?.let {
                    // Access properties of Staycation
                    Text(text = "Title: ${staycation.staycationTitle}")
                    Text(text = "Description: ${staycation.staycationDescription}")
                    Text(text = "Location: ${staycation.staycationLocation}")
                    // Add more Text composable for other staycation properties
                }
            }
        }
    }
}

