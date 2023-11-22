package com.example.tripnila.model

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tripnila.data.Amenity
import com.example.tripnila.data.Host
import com.example.tripnila.data.Photo
import com.example.tripnila.data.Staycation
import com.example.tripnila.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AddListingViewModel(private val repository: UserRepository = UserRepository()) : ViewModel() {

    private val _staycation = MutableStateFlow(Staycation()) // Initialize with an empty Host
    val staycation = _staycation.asStateFlow()

    fun setHostId(hostId: String) {
        _staycation.value = _staycation.value.copy()
        Log.d("Staycation", "${_staycation.value.staycationType}")
    }
    fun setStaycationType(type: String) {
        _staycation.value = _staycation.value.copy(staycationType = type)
        Log.d("Staycation", "${_staycation.value.staycationType}")
    }
    fun clearStaycationType() {
        _staycation.value = _staycation.value.copy(staycationType = "")
        Log.d("Staycation", "${_staycation.value.staycationType}")
    }
    fun setStaycationTitle(title: String) {
        _staycation.value = _staycation.value.copy(staycationTitle = title)
        Log.d("Staycation", "${_staycation.value.staycationTitle}")
    }
    fun setStaycationDescription(description: String) {
        _staycation.value = _staycation.value.copy(staycationDescription = description)
        Log.d("Staycation", "${_staycation.value.staycationDescription}")
    }
    fun setStaycationAmenities(amenity: List<String>) {
        val amenities = amenity.map { Amenity(amenityName = it) }
        _staycation.value = _staycation.value.copy(amenities = amenities)
        Log.d("Staycation", "${_staycation.value.amenities.map { it.amenityName }}")
    }

    fun setStaycationCoverPhoto(uri: Uri){
        val coverPhoto = Photo(photoUri = uri, photoType = "Cover")
        _staycation.value = _staycation.value.copy(staycationImages = listOf(coverPhoto))
        Log.d("Staycation", "${_staycation.value.staycationImages}")
    }

    fun setSelectedImageUris(uris: List<Uri>) {
        val imageUris = uris.map { Photo(photoUri = it, photoType = "Others") }
        _staycation.value = _staycation.value.copy(staycationImages = imageUris)
        Log.d("Staycation", "${_staycation.value.staycationImages}")
       // Log.d("Staycation", "${_staycation.value.staycationImages.map { it.photoUri }}")
    }
    fun setStaycationLocation(location: String) {
        _staycation.value = _staycation.value.copy(staycationLocation = location)
        Log.d("Staycation", "$_staycation")
    }

    fun setStaycationSpace(space: String) {
        _staycation.value = _staycation.value.copy(staycationSpace = space)
        Log.d("Staycation", "${_staycation.value.staycationSpace}")
    }

    fun clearStaycationSpace() {
        _staycation.value = _staycation.value.copy(staycationSpace = "")
        Log.d("Staycation", "${_staycation.value.staycationSpace}")
    }

    fun setStaycationPrice(price: Double) {
        _staycation.value = _staycation.value.copy(staycationPrice = price)
        Log.d("Staycation", "${_staycation.value.staycationPrice}")
    }

    fun setHasDangerousAnimal(isTrue: Boolean) {
        _staycation.value = _staycation.value.copy(hasDangerousAnimal = isTrue)
        Log.d("Staycation", "$_staycation")
    }

    fun setHasSecurityCamera(isTrue: Boolean) {
        _staycation.value = _staycation.value.copy(hasSecurityCamera = isTrue)
        Log.d("Staycation", "$_staycation")
    }
    fun setHasWeapon(isTrue: Boolean) {
        _staycation.value = _staycation.value.copy(hasWeapon = isTrue)
        Log.d("Staycation", "$_staycation")
    }

    fun setNoOfBathrooms(count: Int) {
        _staycation.value = _staycation.value.copy(noOfBathrooms = count)
        Log.d("Bathrooms", "${_staycation.value.noOfBathrooms}")
    }

    fun setNoOfBedrooms(count: Int) {
        _staycation.value = _staycation.value.copy(noOfBedrooms = count)
        Log.d("Bedrooms", "${_staycation.value.noOfBedrooms}")
    }
    fun setNoOfBeds(count: Int) {
        _staycation.value = _staycation.value.copy(noOfBeds = count)
        Log.d("Beds", "${_staycation.value.noOfBeds}")
    }

    fun setNoOfGuests(count: Int) {
        _staycation.value = _staycation.value.copy(noOfGuests = count)
        Log.d("Guests", "${_staycation.value.noOfGuests}")
    }

}