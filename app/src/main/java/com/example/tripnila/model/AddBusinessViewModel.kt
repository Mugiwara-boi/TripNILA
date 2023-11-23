package com.example.tripnila.model

import android.icu.text.CaseMap.Title
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.tripnila.data.Amenity
import com.example.tripnila.data.Business
import com.example.tripnila.data.DailySchedule
import com.example.tripnila.data.Host
import com.example.tripnila.data.Photo
import com.example.tripnila.data.Staycation
import com.example.tripnila.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AddBusinessViewModel(private val repository: UserRepository = UserRepository()) : ViewModel() {

    private val _business = MutableStateFlow(Business()) // Initialize with an empty Host
    val business = _business.asStateFlow()


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
        val newAmenity = Amenity(amenityName = amenity)
        _business.value = _business.value.copy(amenities = _business.value.amenities - newAmenity)
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

    fun setCoverPhoto(uri: Uri){
        val coverPhoto = Photo(photoUri = uri, photoType = "Cover")
        _business.value = _business.value.copy(businessImages = _business.value.businessImages + listOf(coverPhoto))
        Log.d("BusinessImage", "${_business.value.businessImages}")
    }


    fun setSelectedImageUris(uris: List<Uri>) {
        val imageUris = uris.map { Photo(photoUri = it, photoType = "Others") }
        _business.value = _business.value.copy(businessImages = _business.value.businessImages + imageUris)
        Log.d("BusinessImage", "${_business.value.businessImages}")
    }

    fun setMenuCoverPhoto(uri: Uri){
        val coverPhoto = Photo(photoUri = uri, photoType = "Cover")
        _business.value = _business.value.copy(businessMenu = _business.value.businessMenu + listOf(coverPhoto))
        Log.d("BusinessImage", "${_business.value.businessMenu}")
    }


    fun setMenuSelectedImageUris(uris: List<Uri>) {
        val imageUris = uris.map { Photo(photoUri = it, photoType = "Others") }
        _business.value = _business.value.copy(businessMenu = _business.value.businessMenu + imageUris)
        Log.d("BusinessImage", "${_business.value.businessMenu}")
    }

    fun setAdditionalInfo(info: String) {
        _business.value = _business.value.copy(additionalInfo = info)
    }

    fun addSchedule(day: String, openingTime: String, closingTime: String) {
        val newSchedule = DailySchedule(day = day, openingTime = openingTime, closingTime = closingTime)
        _business.value = _business.value.copy(schedule = _business.value.schedule + newSchedule)
        Log.d("", "${_business.value}")
    }

//    fun removeSchedule(day: String, openingTime: String, closingTime: String) {
//        val newSchedule = DailySchedule(day = day, openingTime = openingTime, closingTime = closingTime)
//        _business.value = _business.value.copy(schedule = _business.value.schedule - newSchedule)
//        Log.d("", "${_business.value.schedule}")
//    }

    fun removeSchedule(day: String) {
        val newSchedule = _business.value.schedule.filter { it.day != day }
        _business.value = _business.value.copy(schedule = newSchedule)
        Log.d("", "${_business.value}")
    }

    fun addDay(day: String) {
        val newDay = DailySchedule(day = day)
        _business.value = _business.value.copy(schedule = _business.value.schedule + newDay)
    }

    fun removeDay(day: String) {
        val newDay = DailySchedule(day = day)
        _business.value = _business.value.copy(schedule = _business.value.schedule - newDay)
    }





}
