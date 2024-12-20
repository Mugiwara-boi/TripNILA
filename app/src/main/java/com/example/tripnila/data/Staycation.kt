package com.example.tripnila.data

import android.net.Uri
import com.google.firebase.Timestamp
import java.time.LocalDate
import java.time.ZoneId
import java.util.Calendar
import java.util.Date
import java.util.concurrent.TimeUnit


data class StaycationAvailability(
    val staycationAvailabilityId: String = "",
    val availableDate: Timestamp? = null
)

data class Staycation(
    val staycationId: String = "",
    val host: Host = Host(),
    val staycationTags: List<Tag> = emptyList(),
    val staycationTitle: String = "",
    val staycationDescription: String = "",
    val staycationType: String = "",
    val staycationSpace: String = "",
    val staycationLocation: String = "",
    val staycationLat: Double = 0.000000,
    val staycationLng: Double = 0.000000,
    val noOfGuests: Int = 0,
    val noOfBedrooms: Int = 0,
    val noOfBeds: Int = 0,
    val noOfBathrooms: Int = 0,
    val staycationPrice: Double = 0.00,
    val hasSecurityCamera: Boolean = false,
    val hasWeapon: Boolean = false,
    val hasDangerousAnimal: Boolean = false,
    val hasFirstAid: Boolean = false,
    val hasFireExit: Boolean = false,
    val hasFireExtinguisher: Boolean = false,
    val staycationImages: List<Photo> = emptyList(),
    val promotions: List<Promotion> = emptyList(),
    val availableDates: List<StaycationAvailability> = emptyList(),
    val amenities: List<Amenity> = emptyList(),
    val staycationBookings: List<StaycationBooking> = emptyList(),
    val nearbyAttractions: List<String> = emptyList(),

    val maxNoOfGuests: Int = 0,
    val additionalFeePerGuest: Double = 0.0,
    val noisePolicy: Boolean = false,
    val allowSmoking: Boolean = false,
    val allowPets: Boolean = false,
    val additionalInfo: String = "",
    val noCancel: Boolean = false,
    val noReschedule: Boolean = false,
    val isEcoFriendly: Boolean = false,

    val phoneNo: String = "",
    val email: String = "",
) {
    val totalReviews: Int
        get() = staycationBookings.mapNotNull { it.bookingReview }.filter { it.bookingId != "" }.count()

    val averageReviewRating: Double
        get() {
            val validReviews = staycationBookings
                .mapNotNull { it.bookingReview }
                .filter { it.bookingId != "" }

            val average = validReviews.map { it.rating }.average()
            return if (average.isNaN()) 0.0 else average
        }
    val pendingBookingsCount: Int
        get() = staycationBookings.count { it.bookingStatus == "Pending" }
    val ongoingBookingsCount: Int
        get() = staycationBookings.count { it.bookingStatus == "Ongoing" }

    val completedBookingsCount: Int
        get() = staycationBookings.count { it.bookingStatus == "Completed" }

    val cancelledBookingsCount: Int
        get() = staycationBookings.count { it.bookingStatus == "Cancelled" }

    val checkingOutBookingsCount: Int
        get() = staycationBookings.count { it.bookingStatus == "Ongoing" && (it.checkOutDate?.toInstant()?.atZone(ZoneId.systemDefault())?.toLocalDate() == LocalDate.now() || it.checkOutDate?.toInstant()?.atZone(ZoneId.systemDefault())?.toLocalDate() == LocalDate.now().plusDays(1))
    }
}



data class Amenity(
    val amenityId: String = "",
    val amenityName: String = "",
    val amenityIcon: Int = 0
)


data class Photo(
    val photoId: String = "",
    val photoUrl: String? = null,
    val photoType: String = "",
    val photoUri: Uri? = null
)

data class BookingInfo(
    val totalAmount: Double,
    val commission: Double
)
data class StaycationBooking(
    val staycationBookingId: String = "",
    val tourist: Tourist? = null,
    val bookingDate: Date? = null,
    val checkInDate: Date? = null,
    val checkOutDate: Date? = null,
    val noOfGuests: Int = 0,
    val noOfPets: Int = 0,
    val noOfInfants: Int = 0,
    val additionalFee: Double = 0.0,
    val commission: Double =0.0,
    val totalAmount: Double = 0.00,
    val bookingStatus: String = "",
    val bookingReview: Review? = null, // new
    val staycation: Staycation? = null
) {

    fun getDatesBetween(): List<LocalDate> {
        val dates = mutableListOf<LocalDate>()
        var start = checkInDate?.toInstant()?.atZone(ZoneId.systemDefault())?.toLocalDate()
        val end = checkOutDate?.toInstant()?.atZone(ZoneId.systemDefault())?.toLocalDate()

        while (start != null && end != null && !start.isAfter(end)) {
            dates.add(start)
            start = start.plusDays(1)
        }
        return dates
    }

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
    val reviewerId: String = "",
    //val bookingId: StaycationBooking? = null,
    val rating: Int = 0,
    val comment: String = "",
    val reviewDate: Date? = null,
    val reviewPhotos: List<ReviewPhoto> = emptyList(),
    val comments: List<Comment> = emptyList(),
    val likes: List<Like> = emptyList(),
)

data class Comment(
    val commentId: String,
    val comment: String,
    val commentDate: String,
    val reviewId: String,
    val commenterId: String,
    val commenter: Tourist
)


data class Like(
    val likeId: String,
    val reviewId: String,
    val touristId: String
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


data class Attraction(
    val attractionId: String = "",
    val attractionName: String = "",
)



data class ReviewPhoto(
    val reviewPhotoId: String = "",
    val reviewId: String = "",
    val reviewPhoto: String? = null,
    val reviewUri: Uri? = null,
    val reviewUrl: String = ""
)



data class Promotion(
    val servicePromotionId: String = "",
    val promoId: String = "",
    val promoName: String = "",
    val description: String = "",
    val discount: Double = 0.00,
    val status: String = "",
)