package com.example.tripnila.model

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tripnila.data.Amenity
import com.example.tripnila.data.Host
import com.example.tripnila.data.Offer
import com.example.tripnila.data.Photo
import com.example.tripnila.data.Staycation
import com.example.tripnila.data.Tour
import com.example.tripnila.data.TourSchedule
import com.example.tripnila.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.Duration

class HostTourViewModel(private val repository: UserRepository = UserRepository()) : ViewModel() {

    private val _tour = MutableStateFlow(Tour()) // Initialize with an empty Host
    val tour = _tour.asStateFlow()

    private val _isLoadingAddTour = MutableStateFlow<Boolean?>(null)
    val isLoadingAddTour: StateFlow<Boolean?> = _isLoadingAddTour

    private val _isSuccessAddTour = MutableStateFlow(false)
    val isSuccessAddTour: StateFlow<Boolean> = _isSuccessAddTour

    fun setHostId(hostId: String) {
        _tour.value = _tour.value.copy(host = Host(hostId = hostId))
    }

    fun setTourType(type: String) {
        _tour.value = _tour.value.copy(tourType = type)
    }

    fun clearTourType() {
        _tour.value = _tour.value.copy(tourType = "")
    }

    fun setTitle(title: String) {
        _tour.value = _tour.value.copy(tourTitle = title)
    }
    fun setDescription(description: String) {
        _tour.value = _tour.value.copy(tourDescription = description)
    }

    fun setDuration(duration: String) {
        _tour.value = _tour.value.copy(tourDuration = duration)
    }

    fun setLanguage(language: String) {
        _tour.value = _tour.value.copy(tourLanguage = language)
    }


    fun setContact(contact: String) {
        _tour.value = _tour.value.copy(tourContact = contact)
    }

    fun setEmail(email: String) {
        _tour.value = _tour.value.copy(tourEmail = email)
    }

    fun setFacebook(url: String) {
        _tour.value = _tour.value.copy(tourFacebook = url)
    }

    fun setInstagram(url: String) {
        _tour.value = _tour.value.copy(tourInstagram = url)
    }

    fun addOffer(offerType: String, offer: String) {
        val newOffer = Offer(offer = offer, typeOfOffer = offerType)
        _tour.value = _tour.value.copy(offers = _tour.value.offers + newOffer)
        Log.d("", "${_tour.value.offers}")
    }

    fun removeOffer(offerType: String) {
        val newOffer = _tour.value.offers.filter { it.typeOfOffer != offerType }
        _tour.value = _tour.value.copy(offers = newOffer)
        Log.d("", "${_tour.value.offers}")
    }

    fun setSchedule(tourSchedule: TourSchedule){
        _tour.value = _tour.value.copy(schedule = _tour.value.schedule + tourSchedule)
        Log.d("Sched", "${_tour.value.schedule}")
    }

    fun removeSchedule(tourSchedule: TourSchedule){
        _tour.value = _tour.value.copy(schedule = _tour.value.schedule - tourSchedule)
    }


    fun setCoverPhoto(uri: Uri){
        val coverPhoto = Photo(photoUri = uri, photoType = "Cover")
        _tour.value = _tour.value.copy(tourImages = _tour.value.tourImages + listOf(coverPhoto))
    }

    fun setSelectedImageUris(uris: List<Uri>) {
        val imageUris = uris.map { Photo(photoUri = it, photoType = "Others") }
        _tour.value = _tour.value.copy(tourImages = _tour.value.tourImages + imageUris)
    }

    fun setTourPrice(price: Double) {
        _tour.value = _tour.value.copy(tourPrice = price)

    }

    suspend fun addNewTour() {
        viewModelScope.launch {
            try {
                _isLoadingAddTour.value = true

                // Call addStaycationReview with the assigned values
                val success = repository.addTour(
                    hostId = _tour.value.host.hostId,
                    tourType = _tour.value.tourType,
                    tourTitle = _tour.value.tourTitle,
                    tourDescription = _tour.value.tourDescription,
                    tourDuration = _tour.value.tourDuration,
                    tourLanguage = _tour.value.tourLanguage,

                    tourContact = _tour.value.tourContact,
                    tourEmail = _tour.value.tourEmail,
                    tourFacebook = _tour.value.tourFacebook,
                    tourInstagram = _tour.value.tourInstagram,
                    tourLocation = _tour.value.tourLocation,
                    tourPrice = _tour.value.tourPrice,
                    offers = _tour.value.offers,
                    tourImages = _tour.value.tourImages,
                    schedule = _tour.value.schedule,
                    tourTags = _tour.value.tourTags.map { it.tagName }
                )

                _isSuccessAddTour.value = success == true

            } catch (e: Exception) {
                e.printStackTrace()
                _isSuccessAddTour.value = false
            } finally {
                _isLoadingAddTour.value = false
            }
        }
    }


}
