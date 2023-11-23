package com.example.tripnila.model

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.tripnila.data.Amenity
import com.example.tripnila.data.Host
import com.example.tripnila.data.Photo
import com.example.tripnila.data.Staycation
import com.example.tripnila.data.Tour
import com.example.tripnila.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class HostTourViewModel(private val repository: UserRepository = UserRepository()) : ViewModel() {

    private val _tour = MutableStateFlow(Tour()) // Initialize with an empty Host
    val tour = _tour.asStateFlow()

    fun setHostId(hostId: String) {
        _tour.value = _tour.value.copy(host = Host(hostId = hostId))
    }
    fun setTourType(type: String) {
        _tour.value = _tour.value.copy(tourType = type)
    }

    fun clearTourType() {
        _tour.value = _tour.value.copy(tourType = "")
    }

    fun addAmenity(amenity: String) {
        val newAmenity = Amenity(amenityName = amenity)
        _tour.value = _tour.value.copy(amenities = _tour.value.amenities + newAmenity)
    }

    fun removeAmenity(amenity: String) {
        val newAmenity = Amenity(amenityName = amenity)
        _tour.value = _tour.value.copy(amenities = _tour.value.amenities - newAmenity)
    }

    fun setCoverPhoto(uri: Uri){
        val coverPhoto = Photo(photoUri = uri, photoType = "Cover")
        _tour.value = _tour.value.copy(tourImages = _tour.value.tourImages + listOf(coverPhoto))
    }


    fun setSelectedImageUris(uris: List<Uri>) {
        val imageUris = uris.map { Photo(photoUri = it, photoType = "Others") }
        _tour.value = _tour.value.copy(tourImages = _tour.value.tourImages + imageUris)
    }

}
