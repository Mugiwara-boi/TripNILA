package com.example.tripnila.model

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tripnila.data.Amenity
import com.example.tripnila.data.Host
import com.example.tripnila.data.Photo
import com.example.tripnila.data.Promotion
import com.example.tripnila.data.Staycation
import com.example.tripnila.data.StaycationAvailability
import com.example.tripnila.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date

class AddListingViewModel(private val repository: UserRepository = UserRepository()) : ViewModel() {

    private val _staycation = MutableStateFlow(Staycation()) // Initialize with an empty Host
    val staycation = _staycation.asStateFlow()

    private val _isAgreeToPrivacyPolicy = MutableStateFlow(false)
    val isAgreeToPrivacyPolicy: StateFlow<Boolean> = _isAgreeToPrivacyPolicy

    private val _isAgreeToNonDiscrimination = MutableStateFlow(false)
    val isAgreeToNonDiscrimination: StateFlow<Boolean> = _isAgreeToNonDiscrimination

    private val _isLoadingAddListing = MutableStateFlow<Boolean?>(null)
    val isLoadingAddListing: StateFlow<Boolean?> = _isLoadingAddListing

    private val _isSuccessAddListing = MutableStateFlow(false)
    val isSuccessAddListing: StateFlow<Boolean> = _isSuccessAddListing


    suspend fun addNewListing() {
        viewModelScope.launch {
            try {
                _isLoadingAddListing.value = true

                // Call addStaycationReview with the assigned values
                val success = repository.addStaycation(
                    hasDangerousAnimal = _staycation.value.hasDangerousAnimal,
                    hasSecurityCamera = _staycation.value.hasSecurityCamera,
                    hasWeapon = _staycation.value.hasWeapon,
                    hostId = _staycation.value.host.hostId,
                    noOfBathrooms = _staycation.value.noOfBathrooms,
                    noOfBedrooms = _staycation.value.noOfBedrooms,
                    noOfBeds = _staycation.value.noOfBeds,
                    noOfGuests = _staycation.value.noOfGuests,
                    staycationDescription = _staycation.value.staycationDescription,
                    staycationLocation = _staycation.value.staycationLocation,
                    staycationPrice = _staycation.value.staycationPrice,
                    staycationSpace = _staycation.value.staycationSpace,
                    staycationTitle = _staycation.value.staycationTitle,
                    staycationType = _staycation.value.staycationType,
                    amenities = _staycation.value.amenities.map { it.amenityName },
                    photos = _staycation.value.staycationImages,
                    availableDates = _staycation.value.availableDates.map { it.availableDate!! },
                    promotions = _staycation.value.promotions,
                    nearbyAttractions = _staycation.value.nearbyAttractions,
                    staycationTags = _staycation.value.staycationTags.map { it.tagName }
                )

                _isSuccessAddListing.value = success == true

            } catch (e: Exception) {
                e.printStackTrace()
                _isSuccessAddListing.value = false
            } finally {
                _isLoadingAddListing.value = false
        }
    }
}



    fun onAgreeToPP(checked: Boolean) {
        _isAgreeToPrivacyPolicy.value = checked
        Log.d("Staycation", "${_isAgreeToPrivacyPolicy.value}")
    }

    fun onAgreeToNP(checked: Boolean) {
        _isAgreeToNonDiscrimination.value = checked
        Log.d("Staycation", "${_isAgreeToNonDiscrimination.value}")
    }

    fun validatePP(): Boolean {
        return _isAgreeToPrivacyPolicy.value
    }

    fun validateNP(): Boolean {
        return _isAgreeToNonDiscrimination.value
    }

    fun setHostId(hostId: String) {
        _staycation.value = _staycation.value.copy(host = Host(hostId = hostId))
        Log.d("Staycation", "${_staycation.value.staycationType}")
    }
    fun setStaycationType(type: String) {
        _staycation.value = _staycation.value.copy(staycationType = type)
        Log.d("Staycation", "${_staycation.value.staycationType}")
    }

    fun setAvailableDates(availability: List<StaycationAvailability>) {
        _staycation.value = _staycation.value.copy(availableDates = availability)
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

    fun addStaycationAmenity(amenity: String) {
        val newAmenity = Amenity(amenityName = amenity)
        _staycation.value = _staycation.value.copy(amenities = _staycation.value.amenities + newAmenity)
        Log.d("Staycation", "${_staycation.value.amenities.map { it.amenityName }}")
    }

    fun removeStaycationAmenity(amenity: String) {
        val newAmenity = Amenity(amenityName = amenity)
        _staycation.value = _staycation.value.copy(amenities = _staycation.value.amenities - newAmenity)
        Log.d("Staycation", "${_staycation.value.amenities.map { it.amenityName }}")
    }

//    fun isPromotionSelected(promotion: Promotion): Boolean {
//        // Check if the promotion is in the current staycation's promotions
//        return _staycation.value.promotions.contains(promotion)
//    }
//
//
//    fun setStaycationPromotions(promotion: List<Promotion>) {
//        //val amenities = amenity.map { Promotion(amenityName = it) }
//        _staycation.value = _staycation.value.copy(promotions = promotion)
//        Log.d("Staycation", "${_staycation.value.promotions.map { it.promoName }}")
//    }

    fun addStaycationPromotion(promotion: Promotion) {
        //val amenities = amenity.map { Promotion(amenityName = it) }
        _staycation.value = _staycation.value.copy(promotions = _staycation.value.promotions + promotion)
        Log.d("Staycation", "${_staycation.value.promotions.map { it.promoId }}")
    }

    fun removeStaycationPromotion(promotion: Promotion) {
        _staycation.value = _staycation.value.copy(promotions = _staycation.value.promotions - promotion)
        Log.d("Staycation", "${_staycation.value.promotions.map { it.promoName }}")
    }

    fun setStaycationCoverPhoto(uri: Uri){
        val coverPhoto = Photo(photoUri = uri, photoType = "Cover")
        _staycation.value = _staycation.value.copy(staycationImages = _staycation.value.staycationImages + listOf(coverPhoto))
        Log.d("Staycation", "${_staycation.value.staycationImages}")
    }


    fun setSelectedImageUris(uris: List<Uri>) {
        val imageUris = uris.map { Photo(photoUri = it, photoType = "Others") }
        _staycation.value = _staycation.value.copy(staycationImages = _staycation.value.staycationImages + imageUris)
        Log.d("Staycation", "${_staycation.value.staycationImages}")
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
        Log.d("Staycation", "${_staycation.value.hasDangerousAnimal}")
    }

    fun setHasSecurityCamera(isTrue: Boolean) {
        _staycation.value = _staycation.value.copy(hasSecurityCamera = isTrue)
        Log.d("Staycation", "${_staycation.value.hasSecurityCamera}")
    }
    fun setHasWeapon(isTrue: Boolean) {
        _staycation.value = _staycation.value.copy(hasWeapon = isTrue)
        Log.d("Staycation", "${_staycation.value.hasWeapon}")
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