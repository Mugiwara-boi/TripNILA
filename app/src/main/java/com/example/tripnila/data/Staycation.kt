package com.example.tripnila.data

import android.util.Range
import androidx.core.util.rangeTo
import com.google.firebase.Timestamp
import java.time.LocalDate
import java.util.Date


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
    val averageReviewRating: Double = 0.0
) {
    val totalReviews: Int
        get() = staycationBookings.count { it.bookingReview != null }
}

data class StaycationBooking(
    val staycationBookingId: String = "",
    val touristId: String = "",
    val staycationID: String = "",
    val bookingDate: Date? = null,
    val checkInDate: Date? = null,
    val checkOutDate: Date? = null,
    val noOfGuests: Int = 0,
    val totalAmount: Double = 0.00,
    val bookingStatus: String = "",
    val bookingReview: Review? = null, // new
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


data class Review(
    val reviewId: String = "",
    val serviceType: String = "",
    val bookingId: String = "",
    val rating: Int = 0,
    val comment: String = "",
    val reviewDate: Date? = null,
    val reviewerImage: String = "https://upload.wikimedia.org/wikipedia/commons/8/89/Portrait_Placeholder.png",
    val reviewerFirstName: String = "",
    val reviewerMiddleName: String = "",
    val reviewerLastName: String = "",
)

data class Amenity(
    val amenityId: String = "",
    val amenityName: String = "",
    val amenityIcon: Int = 0
)

data class Photo(
   // val photoId: String = "",
    val photoUrl: String = "",
    val photoType: String = ""
)

data class Promotion(
    val promoId: String = "",
    val promoName: String = "",
    val description: String = "",
    val discount: Double = 0.00,
    val status: String = "",
)