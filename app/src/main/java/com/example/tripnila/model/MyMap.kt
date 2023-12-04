package com.example.tripnila.model

import android.content.Context
import android.location.Geocoder
import android.location.Location
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.example.tripnila.utils.capitaliseIt
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MarkerInfoWindowContent
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.Locale

@Composable
fun MyMap(
    addListingViewModel: AddListingViewModel? = null,
    context: Context,
    latLng: LatLng,
    changeIcon: Boolean = false,
    mapProperties: MapProperties = MapProperties(),
    onChangeMarkerIcon: () -> Unit,
    onChangeMapType: (mapType: MapType) -> Unit,

) {

    var staycationLat by remember { mutableStateOf(addListingViewModel?.staycation?.value?.staycationLat) }
    var staycationLng by remember { mutableStateOf(addListingViewModel?.staycation?.value?.staycationLng) }
    var staycationId by remember { mutableStateOf(addListingViewModel?.staycation?.value?.staycationId) }
    var staycationLocation by remember { mutableStateOf(addListingViewModel?.staycation?.value?.staycationLocation) }
    var distanceText by remember { mutableStateOf<String?> (null) }
    var markerLatLng by remember { mutableStateOf(latLng) }


    var mapTypeMenuExpanded by remember { mutableStateOf(false) }
    var mapTypeMenuSelectedText by remember {
        mutableStateOf(
            MapType.NORMAL.name.capitaliseIt()
        )
    }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(latLng, 15f)
    }
    val documentId = "marker-1" // Replace with your document ID

    var showInfoWindow by remember {
        mutableStateOf(true)
    }
    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.matchParentSize(),
            cameraPositionState = cameraPositionState,
            properties = mapProperties,
            onMapClick = {clickedLatLng ->
                markerLatLng = clickedLatLng
                staycationLat = markerLatLng.latitude
                staycationLng = markerLatLng.longitude
                addListingViewModel?.setStaycationLat(staycationLat!!)
                addListingViewModel?.setStaycationLng(staycationLng!!)
                /*getSavedLocationFromFirebase(documentId) { savedLocation ->
                    // Use the retrieved LatLng value here
                    if (savedLocation != null) {
                        val distance = calculateDistanceBetweenMarkers(markerLatLng, savedLocation)
                        val formatdistance = String.format("%.2f", distance)
                        //saveDistanceToFirestore(distance)
                        distanceText = "Distance: $formatdistance m"
                    } else {
                        // Handle case when LatLng couldn't be retrieved or doesn't exist
                    }
                }*/

            }
        ) {
            MarkerInfoWindowContent(
                state = MarkerState(position = markerLatLng),
                onClick = {

                    if (showInfoWindow) {
                        MarkerState(position = markerLatLng).showInfoWindow()
                    } else {
                        MarkerState(position = markerLatLng).hideInfoWindow()
                    }
                    showInfoWindow = !showInfoWindow
                    return@MarkerInfoWindowContent false
                },
                title = "India Map Title"
            ){
                val rememberedDistanceText by remember { mutableStateOf(distanceText) }

                Column {
                    Text(text = "Lat: $staycationLat Lng: $staycationLng id: $staycationId")
                }
            }
            /*
                Marker(
                    state = MarkerState(position = markerLatLng),
                    title = "Location",
                    snippet = "Marker in current location",
                    icon = if (changeIcon) {
                        bitmapDescriptor(context, R.drawable.ic_shopping_cart_24)
                    } else null
                )

            if (distanceText != null) {
                Box(
                    modifier = Modifier
                        .offset { IntOffset(0, (-48).dp.roundToPx()) } // Adjust the offset as needed
                        .background(Color.White)
                        .padding(8.dp)
                ) {
                    Text(text = distanceText!!)
                }
            }
            */


        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 4.dp)
        ) {
            Spacer(modifier = Modifier.width(4.dp))
            Row {
                Button(onClick = { mapTypeMenuExpanded = true }) {
                    Text(text = mapTypeMenuSelectedText)
                    Icon(
                        imageVector = Icons.Filled.ArrowDropDown,
                        contentDescription = "Dropdown arrow",
                        modifier = Modifier.size(ButtonDefaults.IconSize)
                    )
                }
                DropdownMenu(expanded = mapTypeMenuExpanded,
                    onDismissRequest = { mapTypeMenuExpanded = false }) {
                    MapType.values().forEach {
                        val mapType = it.name.capitaliseIt()
                        DropdownMenuItem(text = {
                            Text(text = mapType)
                        }, onClick = {
                            onChangeMapType(it)
                            mapTypeMenuSelectedText = mapType
                            mapTypeMenuExpanded = false
                        })
                    }
                }
            }
            Spacer(modifier = Modifier.width(4.dp))

        }


    }
}


fun getSavedLocationFromFirebase(documentId: String, onComplete: (LatLng?) -> Unit) {
    val db = FirebaseFirestore.getInstance()

    // Reference the "locations" collection and retrieve the specified document
    db.collection("markers").document(documentId)
        .get()
        .addOnSuccessListener { documentSnapshot: DocumentSnapshot? ->
            if (documentSnapshot != null && documentSnapshot.exists()) {
                // Extract LatLng values from the document
                val latitude = documentSnapshot.getDouble("lat")
                val longitude = documentSnapshot.getDouble("lng")

                // Check if both latitude and longitude exist in the document
                if (latitude != null && longitude != null) {
                    val retrievedLatLng = LatLng(latitude, longitude)
                    onComplete(retrievedLatLng) // Pass the retrieved LatLng to the callback
                } else {
                    onComplete(null) // Return null if LatLng is incomplete or missing
                }
            } else {
                onComplete(null) // Return null if document doesn't exist
            }
        }
        .addOnFailureListener {
            onComplete(null) // Return null if there's an error fetching the document
        }
}
fun calculateDistanceBetweenMarkers(marker1: LatLng, marker2: LatLng): Float {
    val location1 = Location("Marker 1")
    location1.latitude = marker1.latitude
    location1.longitude = marker1.longitude

    val location2 = Location("Marker 2")
    location2.latitude = marker2.latitude
    location2.longitude = marker2.longitude

    return location1.distanceTo(location2)
}
fun saveDistanceToFirestore(distance: Float) {
    val db = FirebaseFirestore.getInstance()
    val distancesCollection = db.collection("distances") // Collection to store distances

    // Create a map with the distance value
    val data = hashMapOf(
        "distance" to distance
    )

    // Add the distance data to Firestore
    distancesCollection.add(data)
        .addOnSuccessListener { documentReference ->
            // Handle successful addition to Firestore
        }
        .addOnFailureListener { e ->
            // Handle failures
        }
}

fun saveLatLngToFirestore(listingID: String, latLng: LatLng) {
    val db = FirebaseFirestore.getInstance()

    // Replace "markers" with your desired collection name
    val markersCollection = db.collection("markers")

    // Use the listingID as the document ID and set the LatLng values
    markersCollection.document(listingID)
        .set(mapOf(
            "staycationLat" to latLng.latitude,
            "stayationLng" to latLng.longitude,
        ))
        .addOnSuccessListener {
            // Successful addition
        }
        .addOnFailureListener { e ->
            // Handle failures
        }
}