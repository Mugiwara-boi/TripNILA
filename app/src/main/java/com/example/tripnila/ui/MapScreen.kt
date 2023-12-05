package com.example.tripnila.ui

import android.content.Context
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import com.example.tripnila.model.AddBusinessViewModel
import com.example.tripnila.model.AddListingViewModel
import com.example.tripnila.model.HostTourViewModel
import com.example.tripnila.model.LocationViewModel
import com.example.tripnila.model.LocationViewModelFactory
import com.example.tripnila.model.MyMap
import com.example.tripnila.utils.getCurrentLocation
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.MapProperties

@Composable
fun MapScreen(
    listingType: String = "",
    context: Context,
    addListingViewModel: AddListingViewModel,
    locationViewModelFactory: LocationViewModelFactory,
    addBusinessViewModel: AddBusinessViewModel,
    hostTourViewModel: HostTourViewModel) {
    var showMap by remember { mutableStateOf(false) }
    var location by remember { mutableStateOf(LatLng(0.0, 0.0)) }
    var mapProperties by remember { mutableStateOf(MapProperties()) }
    var changeIcon by remember { mutableStateOf(false) }

    val viewModelStore = LocalViewModelStoreOwner.current?.viewModelStore
    val locationViewModel = remember {
        ViewModelProvider(viewModelStore!!, locationViewModelFactory)[LocationViewModel::class.java]
    }

    getCurrentLocation(context) {
        location = it
        showMap = true
    }

    if (showMap) {
        MyMap(
            listingType = listingType,
            addBusinessViewModel = addBusinessViewModel,
            hostTourViewModel = hostTourViewModel,
            locationViewModel = locationViewModel,
            addListingViewModel = addListingViewModel,
            context = context,
            latLng = location,
            mapProperties = mapProperties,
            changeIcon = changeIcon,
            onChangeMarkerIcon = {
                changeIcon = !changeIcon
            },
            onChangeMapType = {
                mapProperties = mapProperties.copy(mapType = it)
            })
    } else {
        Text(text = "Loading Map...")
    }
}