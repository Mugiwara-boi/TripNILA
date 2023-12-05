package com.example.tripnila.model

import android.content.Context
import android.location.Geocoder
import androidx.lifecycle.ViewModel
import java.io.IOException
import java.util.Locale

class LocationViewModel (private val context: Context) : ViewModel() {

    fun getAddressFromLatLng(latitude: Double, longitude: Double): String? {
        val geocoder = Geocoder(context, Locale.getDefault())
        return try {
            val addresses = geocoder.getFromLocation(latitude, longitude, 1)
            if (addresses?.isNotEmpty() == true) {
                addresses[0]?.getAddressLine(0)
            } else {
                null
            }
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }

    }
    companion object {
        const val TAG = "LocationViewModel"

        // Modify create() to accept a Context parameter
        fun create(context: Context): LocationViewModel {
            return LocationViewModel(context)
        }
    }
}

