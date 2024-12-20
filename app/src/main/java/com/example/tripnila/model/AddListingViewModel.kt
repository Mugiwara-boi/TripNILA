package com.example.tripnila.model

import android.content.Context
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
import com.example.tripnila.data.Tag
import com.example.tripnila.repository.UserRepository
import com.google.firebase.Timestamp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId
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

    private val _isUserVerified = MutableStateFlow(null)
    val isUserVerified = _isUserVerified.asStateFlow()


    fun setHostId(hostId: String) {
        _staycation.value = _staycation.value.copy(host = Host(hostId = hostId))
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

    fun addAvailableDate(localDate: LocalDate){
        val newStaycationAvailability = StaycationAvailability(
            availableDate = Timestamp(Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant()))
        )
        _staycation.value = _staycation.value.copy(availableDates = _staycation.value.availableDates + newStaycationAvailability)
    }

    fun removeAvailableDate(localDate: LocalDate) {

        val updatedAvailableDates = _staycation.value.availableDates.filterNot {
            val timestamp = it.availableDate
            val date = timestamp?.toDate()?.toInstant()?.atZone(ZoneId.systemDefault())?.toLocalDate()
            date == localDate
        }

        _staycation.value = _staycation.value.copy(availableDates = updatedAvailableDates)
    }


    fun setStaycationTitle(title: String) {
        _staycation.value = _staycation.value.copy(staycationTitle = title)
        Log.d("Staycation", "${_staycation.value.staycationTitle}")
    }
    fun setStaycationDescription(description: String) {
        _staycation.value = _staycation.value.copy(staycationDescription = description)
        Log.d("Staycation", "${_staycation.value.staycationDescription}")
    }

    fun setStaycationAdditionalInfo(description: String) {
        _staycation.value = _staycation.value.copy(additionalInfo = description)
        Log.d("Staycation", "${_staycation.value.additionalInfo}")
    }

    fun setStaycationPhone(contact: String) {
        _staycation.value = _staycation.value.copy(phoneNo = contact)
        Log.d("Staycation", "${_staycation.value.phoneNo}")
    }

    fun setStaycationEmail(email: String) {
        _staycation.value = _staycation.value.copy(email = email)
        Log.d("Staycation", "${_staycation.value.email}")
    }

    fun setStaycationMaxGuest(guest: Int) {
        _staycation.value = _staycation.value.copy(maxNoOfGuests = guest)
        Log.d("Staycation", "${_staycation.value.maxNoOfGuests}")
    }

    fun setStaycationAdditionalFeePerGuest(amount: Double) {
        _staycation.value = _staycation.value.copy(additionalFeePerGuest = amount)
        Log.d("Staycation", "${_staycation.value.additionalFeePerGuest}")
    }

    fun setStaycationAmenities(amenity: List<String>) {
        val amenities = amenity.map { Amenity(amenityName = it) }
        _staycation.value = _staycation.value.copy(amenities = amenities)
        Log.d("Staycation", "${_staycation.value.amenities.map { it.amenityName }}")
    }



    fun addStaycationAmenity(amenity: String) {
        val newAmenity = Amenity(amenityName = amenity)
        _staycation.value = _staycation.value.copy(amenities = _staycation.value.amenities + newAmenity)
        var tagName = ""
        when (amenity) {
            "Swimming pool" -> tagName = "Swimming"
            "Gym equipment" -> tagName = "Sports"
            "Gaming Consoles", "Board Games" -> tagName = "Gaming"
            "Karaoke" -> tagName = "Karaoke"
            "Kitchen", "Refrigerator" -> tagName = "Foodtrip"
            "Park View", "Garden View" -> tagName = "Nature"
            "Smoking Area", "Parking" -> tagName = "Clubs"
            "City View", "Sunset/Sunrise View" -> tagName = "Sightseeing"
            "Honesty Snack Bar", "Dedicated workspace" -> tagName = "Shop"
            "Balcony", "Sauna" -> tagName = "History"
        }

        if (tagName.isNotEmpty()) {
            // Create the Tag object
            val newTag = Tag(tagName = tagName)

            // Check if the tag already exists in staycationTags
            val tagExists = _staycation.value.staycationTags.any { it.tagName == tagName }

            // If the tag doesn't exist, add it to staycationTags
            if (!tagExists) {
                _staycation.value = _staycation.value.copy(staycationTags = _staycation.value.staycationTags + newTag)
                Log.d("Staycation", "${_staycation.value.staycationTags}")
            } else {
                Log.d("Staycation", "Tag $tagName already exists")
            }
        } else {
            Log.d("Staycation", "Invalid tag name")
        }
    }

    fun addStaycationTag(amenity: String){

    }

    fun removeStaycationAmenity(amenity: String) {

        _staycation.value = _staycation.value.copy(
            amenities = _staycation.value.amenities.filter { it.amenityName != amenity }
        )

        Log.d("Staycation", "${_staycation.value.amenities.map { it.amenityName }}")
    }

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
        val newCoverPhoto = Photo(photoUri = uri, photoType = "Cover")

        _staycation.value = _staycation.value.copy(
            staycationImages = _staycation.value.staycationImages
                .filterNot { it.photoType == "Cover" } // Remove existing cover photo if any
                .plus(newCoverPhoto)
        )

        Log.d("Staycation", "${_staycation.value.staycationImages}")
    }

    fun setSelectedImageUris(uris: List<Uri>) {
        val newImages = uris.map { Photo(photoUri = it, photoType = "Others") }
        _staycation.value = _staycation.value.copy(staycationImages = _staycation.value.staycationImages + newImages)

        Log.d("Staycation", "${_staycation.value.staycationImages.map { it.photoUri }}")
    }

    fun setInitialMaxGuest(){
        _staycation.value = _staycation.value.copy(maxNoOfGuests = _staycation.value.noOfGuests)
    }

    fun removeSelectedImage(uri: Uri) {
        _staycation.value = _staycation.value.copy(staycationImages = _staycation.value.staycationImages.filter { it.photoUri != uri })
        Log.d("Staycation", "${_staycation.value.staycationImages.map { it.photoUri }}")
    }


    fun setStaycationLocation(location: String) {
        _staycation.value = _staycation.value.copy(staycationLocation = location)
        Log.d("Staycation", "$_staycation")
    }
    fun setStaycationLat(lat: Double) {
        _staycation.value = _staycation.value.copy(staycationLat = lat)
        Log.d("Staycation", "$_staycation")
    }
    fun setStaycationLng(lng: Double) {
        _staycation.value = _staycation.value.copy(staycationLng = lng)
        Log.d("Staycation", "$_staycation")
    }

    fun setStaycationSpace(space: String) {
        _staycation.value = _staycation.value.copy(staycationSpace = space)
        Log.d("Staycation", _staycation.value.staycationSpace)
    }

    fun clearStaycationSpace() {
        _staycation.value = _staycation.value.copy(staycationSpace = "")
        Log.d("Staycation", _staycation.value.staycationSpace)
    }

    fun setStaycationPrice(price: Double) {
        _staycation.value = _staycation.value.copy(staycationPrice = price)
        Log.d("Staycation", "${_staycation.value.staycationPrice}")
    }

    fun setHasDangerousAnimal(isTrue: Boolean) {
        _staycation.value = _staycation.value.copy(hasDangerousAnimal = isTrue)
        Log.d("Staycation", "${_staycation.value.hasDangerousAnimal}")
    }

    fun setNoCancel(isTrue: Boolean) {
        _staycation.value = _staycation.value.copy(noCancel = isTrue)
        Log.d("Staycation", "${_staycation.value.noCancel}")
    }

    fun setNoReschedule(isTrue: Boolean) {
        _staycation.value = _staycation.value.copy(noReschedule = isTrue)
        Log.d("Staycation", "${_staycation.value.noReschedule}")
    }

    fun sethasFirstAidKit(isTrue: Boolean) {
        _staycation.value = _staycation.value.copy(hasFirstAid = isTrue)
        Log.d("Staycation", "${_staycation.value.hasFirstAid}")
    }

    fun sethasFireExit(isTrue: Boolean) {
        _staycation.value = _staycation.value.copy(hasFireExit = isTrue)
        Log.d("Staycation", "${_staycation.value.hasFireExit}")
    }

    fun sethasFireExtinguisher(isTrue: Boolean) {
        _staycation.value = _staycation.value.copy(hasFireExtinguisher = isTrue)
        Log.d("Staycation", "${_staycation.value.hasFireExtinguisher}")
    }

    fun setAllowPets(isTrue: Boolean) {
        _staycation.value = _staycation.value.copy(allowPets = isTrue)
        Log.d("Staycation", "${_staycation.value.allowPets}")
    }

    fun setAllowSmoking(isTrue: Boolean) {
        _staycation.value = _staycation.value.copy(allowSmoking = isTrue)
        Log.d("Staycation", "${_staycation.value.allowSmoking}")
    }

    fun setNoisePolicy(isTrue: Boolean) {
        _staycation.value = _staycation.value.copy(noisePolicy = isTrue)
        Log.d("Staycation", "${_staycation.value.noisePolicy}")
    }

    fun setEcoFriendly(isTrue: Boolean) {
        _staycation.value = _staycation.value.copy(isEcoFriendly = isTrue)

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

    fun clearStaycation() {
        _isLoadingAddListing.value = null
        _isSuccessAddListing.value = false
        _staycation.value = Staycation()
    }



    fun getSelectedStaycation(staycationId: String) {
        viewModelScope.launch {
            val staycation = repository.getStaycationById(staycationId)
            _staycation.value = staycation ?: Staycation()

            Log.d("Staycation", "${_staycation.value}")
        }
    }
    /*
    private fun getAddressFromLatLng(latitude: Double, longitude: Double) {
        val geocoder = Geocoder(this, Locale.getDefault())

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val addresses = geocoder.getFromLocation(latitude, longitude, 1)

                if (addresses != null) {
                    val address = addresses[0]?.getAddressLine(0)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
     */

    suspend fun addNewListing(context: Context) {
        viewModelScope.launch {
            try {
                _isLoadingAddListing.value = true

                // Call addStaycationReview with the assigned values
                val success = repository.addStaycation(
                    staycationId = _staycation.value.staycationId,
                    context = context,
                    hasDangerousAnimal = _staycation.value.hasDangerousAnimal,
                    hasSecurityCamera = _staycation.value.hasSecurityCamera,
                    hasWeapon = _staycation.value.hasWeapon,
                    hasFireExit = _staycation.value.hasFireExit,
                    hasFireExtinguisher = _staycation.value.hasFireExtinguisher,
                    hasFirstAid = _staycation.value.hasFirstAid,
                    isEcoFriendly = _staycation.value.isEcoFriendly,
                    allowPets = _staycation.value.allowPets,
                    allowSmoking = _staycation.value.allowSmoking,
                    noCancel = _staycation.value.noCancel,
                    noReschedule = _staycation.value.noReschedule,
                    additionalInfo = _staycation.value.additionalInfo,
                    phoneNo = _staycation.value.phoneNo,
                    email = _staycation.value.email,
                    hostId = _staycation.value.host.hostId,
                    noOfBathrooms = _staycation.value.noOfBathrooms,
                    noOfBedrooms = _staycation.value.noOfBedrooms,
                    noOfBeds = _staycation.value.noOfBeds,
                    noOfGuests = _staycation.value.noOfGuests,
                    maxNoOfGuests = _staycation.value.maxNoOfGuests,
                    additionalFeePerGuest = _staycation.value.additionalFeePerGuest,
                    noisePolicy = !_staycation.value.noisePolicy,
                    staycationDescription = _staycation.value.staycationDescription,
                    staycationLocation = _staycation.value.staycationLocation,
                    staycationLat = _staycation.value.staycationLat,
                    staycationLng = _staycation.value.staycationLng,
                    staycationPrice = _staycation.value.staycationPrice,
                    staycationSpace = _staycation.value.staycationSpace,
                    staycationTitle = _staycation.value.staycationTitle,
                    staycationType = _staycation.value.staycationType,
                    //amenities = _staycation.value.amenities.map { it.amenityName },
                    amenities = _staycation.value.amenities,
                    photos = _staycation.value.staycationImages,
                   // availableDates = _staycation.value.availableDates.map { it.availableDate!! },
                    availableDates = _staycation.value.availableDates,
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

}