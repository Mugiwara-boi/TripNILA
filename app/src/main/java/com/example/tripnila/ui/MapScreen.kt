package com.example.tripnila.ui

import android.content.Context
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.tripnila.model.AddListingViewModel
import com.example.tripnila.model.MyMap
import com.example.tripnila.utils.getCurrentLocation
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.MapProperties

@Composable
fun MapScreen(context: Context, addListingViewModel: AddListingViewModel) {
    var showMap by remember { mutableStateOf(false) }
    var location by remember { mutableStateOf(LatLng(0.0, 0.0)) }
    var mapProperties by remember { mutableStateOf(MapProperties()) }
    var changeIcon by remember { mutableStateOf(false) }


    getCurrentLocation(context) {
        location = it
        showMap = true
    }

    if (showMap) {
        MyMap(
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