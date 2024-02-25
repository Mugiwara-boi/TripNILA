package com.example.tripnila.model

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tripnila.data.Amenity
import com.example.tripnila.data.Business
import com.example.tripnila.data.DailySchedule
import com.example.tripnila.data.Host
import com.example.tripnila.data.Photo
import com.example.tripnila.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AddBusinessViewModel(private val repository: UserRepository = UserRepository()) : ViewModel() {

    private val _business = MutableStateFlow(Business()) // Initialize with an empty Host
    val business = _business.asStateFlow()

    private val _isLoadingAddBusiness = MutableStateFlow<Boolean?>(null)
    val isLoadingAddBusiness: StateFlow<Boolean?> = _isLoadingAddBusiness

    private val _isSuccessAddBusiness = MutableStateFlow(false)
    val isSuccessAddBusiness: StateFlow<Boolean> = _isSuccessAddBusiness


    fun setHostId(hostId: String) {
        _business.value = _business.value.copy(host = Host(hostId = hostId))
    }

    fun setBusinessType(type: String) {
        _business.value = _business.value.copy(businessType = type)
    }

    fun clearBusinessType() {
        _business.value = _business.value.copy(businessType = "")
    }
    fun addBusinessAmenity(amenity: String) {
        val newAmenity = Amenity(amenityName = amenity)
        _business.value = _business.value.copy(amenities = _business.value.amenities + newAmenity)
    }

    fun removeBusinessAmenity(amenity: String) {
        _business.value = _business.value.copy(
            amenities = _business.value.amenities.filter { it.amenityName != amenity }
        )
        Log.d("Business", "${_business.value.amenities.map { it.amenityName }}")
    }


    fun setDescription(description: String) {
        _business.value = _business.value.copy(businessDescription = description)
    }

    fun setTitle(title: String) {
        _business.value = _business.value.copy(businessTitle = title)
    }

    fun setContact(contact: String) {
        _business.value = _business.value.copy(businessContact = contact)
    }

    fun setEmail(email: String) {
        _business.value = _business.value.copy(businessEmail = email)
    }

    fun setUrl(url: String) {
        _business.value = _business.value.copy(businessURL = url)
    }
    fun setBusinessLat(lat: Double) {
        _business.value = _business.value.copy(businessLat = lat)
        Log.d("", "$_business")
    }
    fun setBusinessLng(lng: Double) {
        _business.value = _business.value.copy(businessLng = lng)
        Log.d("", "$_business")
    }
    fun setBusinessLocation(location: String) {
        _business.value = _business.value.copy(businessLocation = location)
        Log.d("", "$_business")
    }

    fun setCoverPhoto(uri: Uri){
        val newCoverPhoto = Photo(photoUri = uri, photoType = "Cover")

        _business.value = _business.value.copy(
            businessImages = _business.value.businessImages
                .filterNot { it.photoType == "Cover" } // Remove existing cover photo if any
                .plus(newCoverPhoto) // If the list is empty, add the new cover photo
        )

        Log.d("Business", "${_business.value.businessImages}")
    }

    fun setSelectedImageUris(uris: List<Uri>) {
        val imageUris = uris.map { Photo(photoUri = it, photoType = "Others") }
        _business.value = _business.value.copy(businessImages = _business.value.businessImages + imageUris)
        Log.d("BusinessImage", "${_business.value.businessImages}")
    }

    fun removeSelectedImage(uri: Uri) {
        _business.value = _business.value.copy(businessImages = _business.value.businessImages.filter { it.photoUri != uri })
        Log.d("Staycation", "${_business.value.businessImages.map { it.photoUri }}")
    }

    fun setMenuCoverPhoto(uri: Uri){

        val coverPhoto = Photo(photoUri = uri, photoType = "Cover")
        _business.value = _business.value.copy(
            businessMenu = _business.value.businessMenu
                .filterNot { it.photoType == "Cover" } // Remove existing cover photo if any
                .plus(coverPhoto)
        )
        Log.d("BusinessMenu", "${_business.value.businessMenu}")
    }


    fun setMenuSelectedImageUris(uris: List<Uri>) {
        val imageUris = uris.map { Photo(photoUri = it, photoType = "Others") }
        _business.value = _business.value.copy(businessMenu = _business.value.businessMenu + imageUris)
        Log.d("BusinessImage", "${_business.value.businessMenu}")
    }

    fun removeMenuSelectedImage(uri: Uri) {
        _business.value = _business.value.copy(businessMenu = _business.value.businessMenu.filter { it.photoUri != uri })
    }

    fun setAdditionalInfo(info: String) {
        _business.value = _business.value.copy(additionalInfo = info)
    }

    fun setMinSpend(price: Double) {
        _business.value = _business.value.copy(minSpend = price)
        Log.d("minSpendOnBusiness", "${_business.value.minSpend}")
    }

    fun addSchedule(day: String, openingTime: String, closingTime: String) {
        val newSchedule = DailySchedule(day = day, openingTime = openingTime, closingTime = closingTime)
        _business.value = _business.value.copy(schedule = _business.value.schedule + newSchedule)
        Log.d("", "${_business.value}")
    }


    fun removeSchedule(day: String) {
        val newSchedule = _business.value.schedule.filter { it.day != day }
        _business.value = _business.value.copy(schedule = newSchedule)
        Log.d("", "${_business.value}")
    }

    fun clearBusiness() {
        _isLoadingAddBusiness.value = null
        _isSuccessAddBusiness.value = false
        _business.value = Business()
    }

    fun getSelectedBusiness(businessId: String) {
        viewModelScope.launch {
            val business = repository.getBusinessById(businessId)
            _business.value = business

            Log.d("AddBusinessViewModel", "${_business.value}")
        }
    }


    suspend fun addNewBusiness(context: Context) {
        viewModelScope.launch {
            try {
                _isLoadingAddBusiness.value = true

                // Call addStaycationReview with the assigned values
                val success = repository.addBusiness(
                    businessId = _business.value.businessId,
                    context = context,
                    hostId = _business.value.host.hostId,
                    businessDescription = _business.value.businessDescription,
                    businessLat = _business.value.businessLat,
                    businessLng = _business.value.businessLng,
                    businessLocation = _business.value.businessLocation,
                    businessTitle = _business.value.businessTitle,
                    businessType = _business.value.businessType,
                    businessContact = _business.value.businessContact,
                    businessEmail = _business.value.businessEmail,
                    businessURL = _business.value.businessURL,
                  //  amenities = _business.value.amenities.map { it.amenityName },
                    amenities = _business.value.amenities,
                    additionalInfo = _business.value.additionalInfo,
                    minSpend = _business.value.minSpend,
                    businessImages = _business.value.businessImages,
                    businessMenusPhotos = _business.value.businessMenu,
                    schedule = _business.value.schedule,
                    businessTags = _business.value.businessTags.map { it.tagName }
                )

                _isSuccessAddBusiness.value = success == true

            } catch (e: Exception) {
                e.printStackTrace()
                _isSuccessAddBusiness.value = false
            } finally {
                _isLoadingAddBusiness.value = false
            }
        }
    }






}
