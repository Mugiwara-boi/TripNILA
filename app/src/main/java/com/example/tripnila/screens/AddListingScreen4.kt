package com.example.tripnila.screens

import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.tripnila.R
import com.example.tripnila.data.PropertyDescription
import com.example.tripnila.model.AddBusinessViewModel
import com.example.tripnila.model.AddListingViewModel
import com.example.tripnila.model.HostTourViewModel
import com.example.tripnila.ui.LocationPermissionScreen
import com.example.tripnila.ui.MapScreen
import com.example.tripnila.ui.theme.GoogleMapsTheme
import com.example.tripnila.utils.checkForPermission

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddListingScreen4(
    listingType: String = "",
    addListingViewModel: AddListingViewModel? = null,
    hostTourViewModel: HostTourViewModel? = null,
    addBusinessViewModel: AddBusinessViewModel? = null,
    onNavToNext: (String) -> Unit,
    onNavToBack: () -> Unit,
){
    val context = LocalContext.current
    var staycationLat by remember { mutableStateOf(addListingViewModel?.staycation?.value?.staycationLat) }
    var staycationLng by remember { mutableStateOf(addListingViewModel?.staycation?.value?.staycationLng) }
    var staycationLocation by remember { mutableStateOf(addListingViewModel?.staycation?.value?.staycationLocation) }
    val header = if (listingType == "Staycation") {
        "Where is it located?"
    } else if (listingType == "Business"){
        "Where is it located?"
    }
    else {
        "Where will you meet?"
    }

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
                    }
                )
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
                    verticalArrangement = Arrangement.spacedBy(15.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    item {
                        Text(
                            text = header,
                            color = Color(0xff333333),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier
                                .width(267.dp)
                                .padding(bottom = 10.dp)
                        )
                        GoogleMapsTheme {
                            Surface(
                                modifier = Modifier.fillMaxSize().height(500.dp),
                                color = MaterialTheme.colorScheme.background
                            ) {
                                var hasLocationPermission by remember {
                                    mutableStateOf(checkForPermission(context))
                                }

                                if (hasLocationPermission) {
                                    if (addListingViewModel != null) {
                                        MapScreen(context,addListingViewModel)
                                    }
                                } else {
                                    LocationPermissionScreen {
                                        hasLocationPermission = true
                                    }
                                }
                            }
                        }
                    }


                }
                Spacer(modifier = Modifier.weight(1f))
                AddListingStepIndicator(modifier = Modifier, currentPage = 0, pageCount = 4)

            }
        }
    }
}
@Composable
fun WebViewPage(initialUrl: String) {
    // variable to hold the current URL displayed in the WebView
    var currentUrl by remember { mutableStateOf(initialUrl) }

    // Column composable to arrange UI elements vertically
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Text composable displaying instructions or information
        Text(
            text = "Zoom and Hover Map to Business vicinity, long press to add marker",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )

        // AndroidView composable to embed a WebView in the Compose UI
        AndroidView(
            factory = { context ->
                WebView(context).apply {
                    // Configure WebView settings
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        0
                    )
                    settings.javaScriptEnabled = true
                    settings.setSupportZoom(true)

                    // Set a WebViewClient to update currentUrl when a page finishes loading
                    webViewClient = object : WebViewClient() {
                        override fun onPageFinished(view: WebView?, url: String?) {
                            super.onPageFinished(view, url)
                            currentUrl = url.orEmpty()
                        }
                    }

                    // Load the initial URL
                    loadUrl(initialUrl)
                }
            },
            // Set modifiers
            modifier = Modifier
                .fillMaxWidth()
                .height(500.dp)
        )

        // Extract latitude and longitude from the current URL
        val (latitude, longitude) = extractLatLngFromUrl(currentUrl)

    }
}

// Function to extract latitude and longitude from a URL using a regular expression
fun extractLatLngFromUrl(url: String): Pair<String, String> {
    // Define a regular expression pattern for extracting latitude and longitude
    val pattern = Regex("@([-+]?\\d*\\.?\\d+),([-+]?\\d*\\.?\\d+),?")

    // Find a match in the URL using the pattern
    val matchResult = pattern.find(url)

    // Destructure the match result into latitude and longitude components
    val (lat, lng) = matchResult?.destructured?.let { (latitude, longitude) ->
        latitude to longitude
    } ?: "" to ""

    // Return a Pair containing the extracted latitude and longitude
    return lat to lng
}
