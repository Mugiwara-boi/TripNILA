package com.example.tripnila.data

import android.net.Uri
import com.google.firebase.Timestamp
import java.util.Calendar
import java.util.Date
import java.util.concurrent.TimeUnit
import kotlin.math.abs


data class Staycation(
    val staycationId: String = "",
    val hostImage: String = "",  /*TODO*/
    val hostFirstName: String = "",
    val hostMiddleName: String = "",
    val hostLastName: String = "",
    val staycationTags: List<Tag> = emptyList(),
    val staycationTitle: String = "",
    val staycationDescription: String = "",
    val staycationType: String = "",
    val staycationSpace: String = "",
    val staycationLocation: String = "",
    val noOfGuests: Int = 0,
    val noOfBedrooms: Int = 0,
    val noOfBeds: Int = 0,
    val noOfBathrooms: Int = 0,
    val staycationPrice: Double = 0.00,
    val hasSecurityCamera: Boolean = false,
    val hasWeapon: Boolean = false,
    val hasDangerousAnimal: Boolean = false,
    val staycationImages: List<Photo> = emptyList(),
    val promotions: List<Promotion> = emptyList(),
    val availableDates: List<StaycationAvailability> = emptyList(),
    val amenities: List<Amenity> = emptyList(),
    val staycationBookings: List<StaycationBooking> = emptyList(),
  //  val averageReviewRating: Double = 0.0
) {
    val totalReviews: Int
        get() = staycationBookings.count { it.bookingReview != null }
    val averageReviewRating: Double
        get() {
            val validReviews = staycationBookings.filter { it.bookingReview != null }
            return if (validReviews.isNotEmpty()) {
                validReviews.map { it.bookingReview!!.rating }.average()
            } else {
                0.0 // or any default value if there are no reviews
            }
        }
}

data class Photo(
    val photoId: String = "",
    val photoUrl: String? = null,
    val photoType: String = "",
    val photoUri: Uri? = null
)


data class StaycationBooking(
    val staycationBookingId: String = "",
 //   val touristId: String = "",
    val tourist: Tourist? = null,
   // val staycationId: String = "",
    val bookingDate: Date? = null,
    val checkInDate: Date? = null,
    val checkOutDate: Date? = null,
    val noOfGuests: Int = 0,
    val noOfPets: Int = 0,
    val noOfInfants: Int = 0,
    val totalAmount: Double = 0.00,
    val bookingStatus: String = "",
    val bookingReview: Review? = null, // new
    val staycation: Staycation? = null
) {
    fun getDaysDifference(): Long {
        return if (checkInDate != null && checkOutDate != null) {
            val checkIn = Calendar.getInstance().apply {
                time = checkInDate
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }.timeInMillis

            val checkOut = Calendar.getInstance().apply {
                time = checkOutDate
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }.timeInMillis

            val diffInMillies = Math.abs(checkOut - checkIn)
            TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS)
        } else {
            0
        }
    }
}

data class Review(
    val reviewId: String = "",
    val serviceType: String = "",
    val bookingId: String = "",
    val reviewer: Tourist = Tourist(),
    //val bookingId: StaycationBooking? = null,
    val rating: Int = 0,
    val comment: String = "",
    val reviewDate: Date? = null,
    val reviewPhotos: List<ReviewPhoto> = emptyList()
)


data class Filter(
    val minPrice: Int? = null,
    val maxPrice: Int? = null,
    val location: String? = null,
    val type: String? = null,
    val isEcoFriendly: Boolean? = null,
    val capacity: Int? = null,
    val bedroomCount: Int? = null,
    val bedCount: Int? = null,
    val bathroomCount: Int? = null,
    val amenities: List<Amenity>? = emptyList(),
    val checkInDate: Date? = null,
    val checkOutDate: Date? = null,
)

data class Tag(
    val tagId: String = "",
    val tagName: String = "",
    val serviceId: String = "",
)

data class StaycationAvailability(
    val staycationAvailabilityId: String = "",
    val availableDate: Timestamp? = null
)




data class ReviewPhoto(
    val reviewPhotoId: String = "",
    val reviewId: String = "",
    val reviewPhoto: String? = null,
    val reviewUri: Uri? = null
)

data class Amenity(
    val amenityId: String = "",
    val amenityName: String = "",
    val amenityIcon: Int = 0
)


data class Promotion(
    val promoId: String = "",
    val promoName: String = "",
    val description: String = "",
    val discount: Double = 0.00,
    val status: String = "",
)