package com.example.tripnila.model

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tripnila.data.Amenity
import com.example.tripnila.data.Business
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
        val newCoverPhoto = Photo(photoUri = uri, photoType = "Cover")

        _tour.value = _tour.value.copy(
            tourImages = _tour.value.tourImages
                .filterNot { it.photoType == "Cover" } // Remove existing cover photo if any
                .plus(newCoverPhoto)// If the list is empty, add the new cover photo
        )

        //Log.d("Business", "${_business.value.businessImages}")
    }

    fun setSelectedImageUris(uris: List<Uri>) {
        val imageUris = uris.map { Photo(photoUri = it, photoType = "Others") }
        _tour.value = _tour.value.copy(tourImages = _tour.value.tourImages + imageUris)
    }

    fun removeSelectedImage(uri: Uri) {

        _tour.value = _tour.value.copy(tourImages = _tour.value.tourImages.filter { it.photoUri != uri })
        Log.d("Tour", "${_tour.value.tourImages.map { it.photoUri }}")
    }


    fun setTourPrice(price: Double) {
        _tour.value = _tour.value.copy(tourPrice = price)

    }

    fun clearTour() {
        _isLoadingAddTour.value = null
        _isSuccessAddTour.value = false
        _tour.value = Tour()
    }

    fun getSelectedTour(tourId: String) {
        viewModelScope.launch {
            val tour = repository.getTourById(tourId)
            _tour.value = tour

            Log.d("Tour", "$tour")
            Log.d("Tour", "${_tour.value}")
        }
    }


    suspend fun addNewTour(context: Context) {
        viewModelScope.launch {
            try {
                _isLoadingAddTour.value = true

                // Call addStaycationReview with the assigned values
                val success = repository.addTour(
                    tourId = _tour.value.tourId,
                    context = context,
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
