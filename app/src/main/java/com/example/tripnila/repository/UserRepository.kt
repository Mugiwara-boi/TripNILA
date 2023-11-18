package com.example.tripnila.repository

import android.nfc.tech.MifareUltralight.PAGE_SIZE
import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.cachedIn
import com.example.tripnila.common.Constants
import com.example.tripnila.data.Amenity
import com.example.tripnila.data.Filter
import com.example.tripnila.data.Photo
import com.example.tripnila.data.Preference
import com.example.tripnila.data.Promotion
import com.example.tripnila.data.Review
import com.example.tripnila.data.Staycation
import com.example.tripnila.data.StaycationAvailability
import com.example.tripnila.data.StaycationBooking
import com.example.tripnila.data.Tag
import com.example.tripnila.data.Tourist
import com.example.tripnila.model.StaycationPagingSource
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.tasks.await
import org.checkerframework.checker.units.qual.min
import java.security.MessageDigest
import java.util.Calendar
import java.util.Date
import java.util.TimeZone
import kotlin.experimental.and
import kotlin.math.min


class UserRepository {
    private val db = FirebaseFirestore.getInstance()
    private val touristCollection = db.collection("tourist")
    private val hostCollection = db.collection("host")
    private val touristPreferencesCollection = db.collection("tourist_preference")
    private val staycationCollection = db.collection("staycation")
    private val staycationAvailabilityCollection = db.collection("staycation_availability")
    private val staycationBookingCollection = db.collection("staycation_booking")
    private val amenityCollection = db.collection("amenity")
    private val serviceAmenityCollection = db.collection("service_amenity")
    private val promotionCollection = db.collection("promotion")
    private val servicePromotionCollection = db.collection("service_promotion")
    private val serviceTagCollection = db.collection("service_tag")
    private val servicePhotoCollection = db.collection("service_photo")
    private val reviewCollection = db.collection("review")
    private val likeCollection = db.collection("like")
    private val commentCollection = db.collection("comment")
    private val paymentCollection = db.collection("payment")


    private var currentUser: Tourist? = null
    private var staycationList: List<Staycation>? = null

    suspend fun addStaycationBooking(
        //bookingDate: Date,
        bookingStatus: String,
        checkInDateMillis: Long,
        checkOutDateMillis: Long,
        timeZone: TimeZone,
        noOfGuests: Int,
        staycationId: String,
        totalAmount: Double,
        touristId: String,
        commission: Double,
        paymentStatus: String,
        paymentMethod: String,
    ): Boolean {
        try {
            val checkInDate = Date(checkInDateMillis)
            val checkOutDate = Date(checkOutDateMillis)

            val bookingData = hashMapOf(
                "bookingDate" to FieldValue.serverTimestamp(),
                "bookingStatus" to bookingStatus,
                "checkInDate" to formatDateInTimeZone(checkInDate, timeZone),
                "checkOutDate" to formatDateInTimeZone(checkOutDate, timeZone),
                "noOfGuests" to noOfGuests,
                "staycationId" to staycationId,
                "totalAmount" to totalAmount,
                "touristId" to touristId
            )

            val staycationBookingDocRef= staycationBookingCollection.add(bookingData).await()
            val staycationBookingId = staycationBookingDocRef.id

            deleteAvailabilityInRange(
                staycationId = staycationId,
                checkInDate = checkInDateMillis,
                checkOutDate = checkOutDateMillis
            )

            addPaymentData(
                amount = totalAmount,
                commission = commission,
                paymentStatus = paymentStatus,
                serviceBookingId = staycationBookingId,
                serviceType = "Staycation",
                transactionId = "TODO", /*TODO*/
                paymentMethod = paymentMethod,
            )

            return true

        } catch (e: Exception) {
            e.printStackTrace()
            return false
            // Handle the error case as needed
        }
    }

    private suspend fun addPaymentData(
        amount: Double,
        commission: Double,
       // paymentDate: String,
        paymentMethod: String,
        paymentStatus: String,
        serviceBookingId: String,
        serviceType: String,
        transactionId: String
    ) {
        try {
            val paymentData = hashMapOf(
                "amount" to amount,
                "commission" to commission,
                "paymentDate" to FieldValue.serverTimestamp(),
                "paymentStatus" to paymentStatus,
                "serviceBookingId" to serviceBookingId,
                "serviceType" to serviceType,
                "paymentMethod" to paymentMethod,
                "transactionId" to transactionId
            )

            paymentCollection.add(paymentData).await()
        } catch (e: Exception) {
            e.printStackTrace()
            // Handle the error case as needed
        }
    }

    suspend fun deleteAvailabilityInRange(staycationId: String, checkInDate: Long, checkOutDate: Long) {
        try {
            val query = staycationAvailabilityCollection
                .whereEqualTo("staycationId", staycationId)
                .whereGreaterThanOrEqualTo("availableDate", Date(checkInDate))
                .whereLessThan("availableDate", Date(checkOutDate))

            val documents = query.get().await()

            for (document in documents.documents) {
                staycationAvailabilityCollection.document(document.id).delete().await()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            // Handle the error case as needed
        }
    }

    private fun formatDateInTimeZone(date: Date, timeZone: TimeZone): Date {
        val calendar = Calendar.getInstance(timeZone)
        calendar.time = date
        return calendar.time
    }
    fun getCurrentUser(): Tourist? { return currentUser }

    suspend fun getServiceIdsByTag(tagName: String): List<Tag> {
        return try {
            val result = serviceTagCollection
                .whereEqualTo("tagName", tagName)
                .get()
                .await()

            val tags = mutableListOf<Tag>()

            for (document in result.documents) {
                val tagId = document.id
                val serviceId = document.getString("serviceId") ?: ""

                tags.add(Tag(tagId = tagId, tagName = tagName, serviceId = serviceId))
            }

            tags
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList() // Handle the error case as needed
        }
    }

    suspend fun getAllStaycationsByTag(tab: String, page: Int): List<Staycation> {
        return try {
            val pageSize = Constants.PAGE_SIZE
            // Fetch serviceIds with the specified tag
            val serviceIds = getServiceIdsByTag(tab)

            // Fetch staycations with document id (staycationId) equal to the serviceId
            val staycationList = mutableListOf<Staycation>()

            val startIdx = page * pageSize
            val endIdx = startIdx + pageSize

            for (i in startIdx until min(endIdx, serviceIds.size)) {
                val serviceId = serviceIds[i].serviceId
                val document = db.collection("staycation").document(serviceId).get().await()

                if (document.exists()) {
                    val staycationId = document.id
                    val staycation = createStaycationFromDocument(document, staycationId)
                    staycationList.add(staycation)
                }
            }

            staycationList

        } catch (e: Exception) {
            e.printStackTrace()
            emptyList() // Handle the error case as needed
        }
    }

    suspend fun getStaycationById(staycationId: String): Staycation? {

        val document = staycationCollection.document(staycationId).get().await()

        if (document.exists()) {
            // If the document exists, create a Staycation object using the existing logic
            return createStaycationFromDocument(document, staycationId)
        } else {
            // Document with the specified staycationId doesn't exist
            return null
        }
    }

    private suspend fun createStaycationFromDocument(document: DocumentSnapshot, staycationId: String): Staycation {
        // (Your existing logic to create Staycation object)
        val hostId = document.getString("hostId") ?: ""
        val noOfGuests = document.getLong("noOfGuests")?.toInt() ?: 0
        val noOfBedrooms = document.getLong("noOfBedrooms")?.toInt() ?: 0
        val noOfBeds = document.getLong("noOfBeds")?.toInt() ?: 0
        val noOfBathrooms = document.getLong("noOfBathrooms")?.toInt() ?: 0
        val staycationDescription = document.getString("staycationDescription") ?: ""
        val staycationLocation = document.getString("staycationLocation") ?: ""
        val staycationPrice = document.getDouble("staycationPrice") ?: 0.0
        val staycationSpace = document.getString("staycationSpace") ?: ""
        val staycationTitle = document.getString("staycationTitle") ?: ""
        val staycationType = document.getString("staycationType") ?: ""
        val hasSecurityCamera = document.getBoolean("hasSecurityCamera") ?: false
        val hasWeapon = document.getBoolean("hasWeapon") ?: false
        val hasDangerousAnimal = document.getBoolean("hasDangerousAnimal") ?: false

        val touristInfo = getTouristInfo(hostId)
        val staycationImages = getStaycationImages(staycationId)
        val staycationTags = getStaycationTags(staycationId)
        val promotions = getPromotions(staycationId)
        val availability = getStaycationAvailability(staycationId)
        val amenities = getAmenities(staycationId)
        val bookings = getStaycationBookings(staycationId)
        return Staycation(
            staycationId = staycationId,
            hostFirstName = touristInfo?.firstName ?: "",
            hostMiddleName = touristInfo?.middleName ?: "",
            hostLastName = touristInfo?.lastName ?: "",
            noOfGuests = noOfGuests,
            noOfBedrooms = noOfBedrooms,
            noOfBeds = noOfBeds,
            noOfBathrooms = noOfBathrooms,
            staycationDescription = staycationDescription,
            staycationLocation = staycationLocation,
            staycationPrice = staycationPrice,
            staycationSpace = staycationSpace,
            staycationTitle = staycationTitle,
            staycationType = staycationType,
            hasSecurityCamera = hasSecurityCamera,
            hasWeapon = hasWeapon,
            hasDangerousAnimal = hasDangerousAnimal,
            staycationImages = staycationImages,
            staycationTags = staycationTags,
            promotions = promotions,
            availableDates = availability,
            amenities = amenities,
            staycationBookings = bookings,
            averageReviewRating = bookings.calculateAverageReviewRating()
        )
    }

    suspend fun getAllStaycations(tab: String): List<Staycation> {
        return try {
            val result = db.collection("staycation")
                .get()
                .await()

            val staycationList = mutableListOf<Staycation>()

            for (document in result.documents) {
                val staycationId = document.id
                val hostId = document.getString("hostId") ?: ""
                val noOfGuests = document.getLong("noOfGuests")?.toInt() ?: 0
                val noOfBedrooms = document.getLong("noOfBedrooms")?.toInt() ?: 0
                val noOfBeds = document.getLong("noOfBeds")?.toInt() ?: 0
                val noOfBathrooms = document.getLong("noOfBathrooms")?.toInt() ?: 0
                val staycationDescription = document.getString("staycationDescription") ?: ""
                val staycationLocation = document.getString("staycationLocation") ?: ""
                val staycationPrice = document.getDouble("staycationPrice") ?: 0.0
                val staycationSpace = document.getString("staycationSpace") ?: ""
                val staycationTitle = document.getString("staycationTitle") ?: ""
                val staycationType = document.getString("staycationType") ?: ""
                val hasSecurityCamera = document.getBoolean("hasSecurityCamera") ?: false
                val hasWeapon = document.getBoolean("hasWeapon") ?: false
                val hasDangerousAnimal = document.getBoolean("hasDangerousAnimal") ?: false

                val touristInfo = getTouristInfo(hostId)
                val staycationImages = getStaycationImages(staycationId)
                val staycationTags = getStaycationTags(staycationId)
                val promotions = getPromotions(staycationId)
                val availability = getStaycationAvailability(staycationId)
                val amenities = getAmenities(staycationId)
                val bookings = getStaycationBookings(staycationId)

                val staycation = Staycation(
                    staycationId = staycationId,
                    hostFirstName = touristInfo?.firstName ?: "",
                    hostMiddleName = touristInfo?.middleName ?: "",
                    hostLastName = touristInfo?.lastName ?: "",
                    noOfGuests = noOfGuests,
                    noOfBedrooms = noOfBedrooms,
                    noOfBeds = noOfBeds,
                    noOfBathrooms = noOfBathrooms,
                    staycationDescription = staycationDescription,
                    staycationLocation = staycationLocation,
                    staycationPrice = staycationPrice,
                    staycationSpace = staycationSpace,
                    staycationTitle = staycationTitle,
                    staycationType = staycationType,
                    hasSecurityCamera = hasSecurityCamera,
                    hasWeapon = hasWeapon,
                    hasDangerousAnimal = hasDangerousAnimal,
                    staycationImages = staycationImages,
                    staycationTags = staycationTags,
                    promotions = promotions,
                    availableDates = availability,
                    amenities = amenities,
                    staycationBookings = bookings,
                    averageReviewRating = bookings.calculateAverageReviewRating()
                )
                staycationList.add(staycation)
            }

            staycationList

        } catch (e: Exception) {
            e.printStackTrace()
            emptyList() // Handle the error case as needed
        }
    }


    suspend fun getStaycationBookings(staycationId: String): List<StaycationBooking> {
        return try {
            val result = staycationBookingCollection
                .whereEqualTo("staycationId", staycationId)
                .get()
                .await()

            val bookings = mutableListOf<StaycationBooking>()

            for (document in result.documents) {
                val staycationBookingId = document.id
                val bookingDate = document.getTimestamp("bookingDate")?.toDate()
                val bookingStatus = document.getString("bookingStatus") ?: ""
                val checkInDate = document.getTimestamp("checkInDate")?.toDate()
                val checkOutDate = document.getTimestamp("checkOutDate")?.toDate()
                val noOfGuests = document.getLong("noOfGuests")?.toInt() ?: 0
                val totalAmount = document.getDouble("totalAmount") ?: 0.0
                val touristId = document.getString("touristId") ?: ""

                // Fetch booking review using bookingId
                val bookingReview = getBookingReview(staycationBookingId)

                // Construct a StaycationBooking object and add it to the list
                val booking = StaycationBooking(
                    staycationBookingId = staycationBookingId,
                    bookingDate = bookingDate,
                    bookingStatus = bookingStatus,
                    checkInDate = checkInDate,
                    checkOutDate = checkOutDate,
                    noOfGuests = noOfGuests,
                    totalAmount = totalAmount,
                    touristId = touristId,
                    staycationID = staycationId,
                    bookingReview = bookingReview
                    // Add other fields as needed
                )

                bookings.add(booking)
            }

            bookings

        } catch (e: Exception) {
            e.printStackTrace()
            emptyList() // Handle the error case as needed
        }
    }

    private fun List<StaycationBooking>.calculateAverageReviewRating(): Double {
        val totalReviews = sumOf { it.bookingReview?.rating ?: 0 }
        val totalBookings = count()
        return if (totalBookings > 0) {
            totalReviews.toDouble() / totalBookings
        } else {
            0.0
        }
    }

    suspend fun getBookingReview(bookingId: String): Review? {
        return try {
            val result = reviewCollection
                .whereEqualTo("bookingId", bookingId)
                .get()
                .await()

            if (result.documents.isNotEmpty()) {
                val document = result.documents[0]
                val reviewId = document.id
                val reviewComment = document.getString("reviewComment") ?: ""
                val reviewDate = document.getTimestamp("reviewDate")?.toDate()
                val reviewRating = document.getLong("reviewRating")?.toInt() ?: 0
                val serviceType = document.getString("serviceType") ?: ""

                // Return the BookingReview object
                Review(
                    reviewId = reviewId,
                    comment = reviewComment,
                    reviewDate = reviewDate,
                    rating = reviewRating,
                    serviceType = serviceType,
                    bookingId = bookingId
                    // Add other fields as needed
                )
            } else {
                // No review found, return null
                null
            }

        } catch (e: Exception) {
            e.printStackTrace()
            null // Handle the error case as needed
        }
    }

    suspend fun getTouristInfo(hostId: String): Tourist? {
        return try {
            // Remove the first 5 characters from hostId to get touristId
            val touristId = hostId.substring(5)
            val touristDocument = touristCollection.document(touristId).get().await()

            if (touristDocument.exists()) {
                val firstName = touristDocument.getString("fullName.firstName") ?: ""
                val middleName = touristDocument.getString("fullName.middleName") ?: ""
                val lastName = touristDocument.getString("fullName.lastName") ?: ""

                Tourist(touristId, firstName, middleName, lastName, "", "") // Return Tourist object
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null // Handle the error case as needed
        }
    }

    suspend fun getStaycationImages(staycationId: String): List<Photo> {
        return try {
            val result = servicePhotoCollection
                .whereEqualTo("serviceId", staycationId)
                .get()
                .await()

            val staycationImages = mutableListOf<Photo>()

            for (document in result.documents) {
                val photoType = document.getString("photoType") ?: ""
                val photoUrl = document.getString("photoUrl") ?: ""

                staycationImages.add(Photo(photoUrl = photoUrl, photoType = photoType))
            }

            staycationImages

        } catch (e: Exception) {
            e.printStackTrace()
            emptyList() // Handle the error case as needed
        }
    }

    suspend fun getStaycationTags(staycationId: String): List<Tag> {
        return try {
            val result = serviceTagCollection
                .whereEqualTo("serviceId", staycationId)
                .get()
                .await()

            val staycationTags = mutableListOf<Tag>()

            for (document in result.documents) {
                val tagId = document.id
                val tagName = document.getString("tagName") ?: ""
                val staycationId = staycationId

                staycationTags.add(Tag(tagId = tagId, tagName = tagName, serviceId = staycationId))
            }

            staycationTags

        } catch (e: Exception) {
            e.printStackTrace()
            emptyList() // Handle the error case as needed
        }
    }

    suspend fun getPromotions(staycationId: String): List<Promotion> {
        return try {
            val result = servicePromotionCollection
                .whereEqualTo("serviceId", staycationId)
                .get()
                .await()

            val promotions = mutableListOf<Promotion>()

            for (document in result.documents) {
                val promoId = document.getString("promoId") ?: ""

                // Fetch promotion details from the promotion collection using promoId
                val promoDetails = getPromoDetails(promoId)

                // Construct Promotion object
                val promotion = Promotion(
                    promoId = promoId,
                    description = promoDetails?.description ?: "",
                    discount = promoDetails?.discount ?: 0.0,
                    promoName = promoDetails?.promoName ?: "",
                    status = promoDetails?.status ?: ""
                    // Add other fields as needed
                )

                promotions.add(promotion)
            }

            promotions

        } catch (e: Exception) {
            e.printStackTrace()
            emptyList() // Handle the error case as needed
        }
    }

    private suspend fun getPromoDetails(promoId: String): Promotion? {
        return try {
            val document = promotionCollection.document(promoId).get().await()

            if (document.exists()) {
                val description = document.getString("description") ?: ""
                val discount = document.getDouble("discount") ?: 0.0
                val promoName = document.getString("promoName") ?: ""
                val status = document.getString("status") ?: ""

                Promotion(promoId = promoId, description = description, discount = discount, promoName = promoName, status = status)
            } else {
                null
            }

        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun getStaycationAvailability(staycationId: String): List<StaycationAvailability> {
        return try {
            val result = staycationAvailabilityCollection
                .whereEqualTo("staycationId", staycationId)
                .get()
                .await()

            val availability = mutableListOf<StaycationAvailability>()

            for (document in result.documents) {
                val availableDateTimestamp = document.getTimestamp("availableDate")
              //  val staycationId = document.getString("staycationId") ?: ""

//                // Convert timestamp to Date
//                val availableDate = availableDateTimestamp?.toDate()

                // Construct StaycationAvailability object
                val staycationAvailability = StaycationAvailability(
                    availableDate = availableDateTimestamp,
                    // Add other fields as needed
                )

                availability.add(staycationAvailability)
            }

            availability

        } catch (e: Exception) {
            e.printStackTrace()
            emptyList() // Handle the error case as needed
        }
    }

    suspend fun getAmenities(staycationId: String): List<Amenity> {
        return try {
            val result = serviceAmenityCollection
                .whereEqualTo("serviceId", staycationId)
                .get()
                .await()

            val amenities = mutableListOf<Amenity>()

            for (document in result.documents) {
                val amenityId = document.getString("amenityId") ?: ""

                // Fetch amenity details from the amenity collection using amenityId
                val amenityDetails = getAmenityDetails(amenityId)

                // Construct Amenity object
                val amenity = Amenity(
                    amenityId = amenityId,
                    amenityName = amenityDetails?.amenityName ?: ""
                    // Add other fields as needed
                )

                amenities.add(amenity)
            }

            amenities

        } catch (e: Exception) {
            e.printStackTrace()
            emptyList() // Handle the error case as needed
        }
    }

    private suspend fun getAmenityDetails(amenityId: String): Amenity? {
        return try {
            val document = amenityCollection.document(amenityId).get().await()

            if (document.exists()) {
                val amenityName = document.getString("amenityName") ?: ""
                // Add other fields as needed
                Amenity(amenityId, amenityName)
            } else {
                null
            }

        } catch (e: Exception) {
            e.printStackTrace()
            null // Handle the error case as needed
        }
    }

    suspend fun addTouristPreferences(touristId: String, preferences: List<String>): Boolean {
        return try {

            val batch = db.batch()

            for ((index, preference) in preferences.withIndex()) {
                val newDocumentRef = touristPreferencesCollection.document()
                batch.set(newDocumentRef, mapOf("touristId" to touristId, "preference" to preference))
            }

            batch.commit().await()
            currentUser?.preferences = preferences.map { Preference(it) }

            true
        } catch (e: Exception) {
            Log.d("User Repository", "Add tourist preferences")
            e.printStackTrace()
            false
        }
    }

    suspend fun loginUser(username: String, password: String): Boolean {
        return try {
            val hashedPassword = hashPassword(password)

            val result = touristCollection
                .whereEqualTo("username", username)
                .whereEqualTo("password", hashedPassword)
                .get()
                .await()

            Log.d("UserRepository", "Query Result: $result")

            //!result.isEmpty
            if (result.documents.isNotEmpty()) {
                val document = result.documents[0]
                val firstName = document.getString("fullName.firstName") ?: ""
                val middleName = document.getString("fullName.middleName") ?: ""
                val lastName = document.getString("fullName.lastName") ?: ""
                val storedUsername = document.getString("username") ?: ""
                val touristId = document.id

                currentUser = Tourist(touristId, firstName, middleName, lastName, storedUsername, "")
                Log.d("UserRepository", "Login result: true")
                Log.d("UserRepository", "Current user: $currentUser")

                getTouristPreferences(touristId)
                true
            } else {
                Log.d("UserRepository", "Login result: false")
                false
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }


    suspend fun addUser(user: Tourist): Boolean {
        return try {
            val hashedPassword = hashPassword(user.password)
            val userData = hashMapOf(
                "fullName" to mapOf(
                    "firstName" to user.firstName,
                    "middleName" to user.middleName,
                    "lastName" to user.lastName
                ),
                "username" to user.username,
                "password" to hashedPassword
            )

            val touristDocRef = touristCollection.add(userData).await()
            val touristId = touristDocRef.id

            val hostData = hashMapOf(
                "touristId" to touristId
            )
            hostCollection.document("HOST-$touristId").set(hostData).await()

            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    private fun hashPassword(password: String): String {
        val bytes = password.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        md.update(bytes)
        val digest = md.digest()
        return digest.joinToString("") { byte -> "%02x".format(byte and 0xFF.toByte()) }
    }

    suspend fun checkIfUsernameExists(username: String): Boolean {
        return try {
            val result = touristCollection.whereEqualTo("username", username).get().await()
            result.documents.isNotEmpty()
        } catch (e: Exception) {
            false
        }
    }

    suspend fun getTouristPreferences(touristId: String): List<Preference> {
        return try {
            val result = touristPreferencesCollection
                .whereEqualTo("touristId", touristId)
                .get()
                .await()

            val preferencesList = mutableListOf<Preference>()

            for (document in result.documents) {
                val preference = document.getString("preference")
                preference?.let {
                    preferencesList.add(Preference(it))
                }
            }
            Log.d("UserRepository", "Preferences List: $preferencesList")

            // Update currentUser with the retrieved preferences
            currentUser?.preferences = preferencesList

            Log.d("UserRepository", "Current User: $currentUser")

            preferencesList

        } catch (e: Exception) {
            e.printStackTrace()
            emptyList() // Handle the error case as needed
        }
    }

}


//    fun getStaycationsByTagPagingSource(tag: String): PagingSource<QuerySnapshot, Staycation> {
//        return object : PagingSource<QuerySnapshot, Staycation>() {
//            override suspend fun load(params: LoadParams<QuerySnapshot>): LoadResult<QuerySnapshot, Staycation> {
//                return try {
//                    // Use the same logic as your existing getAllStaycationsByTag function
//                    val serviceIds = getServiceIdsByTag(tag)
//
//                    // Fetch staycations with document id (staycationId) equal to the serviceId
//                    val staycationList = mutableListOf<Staycation>()
//
//                    for (serviceId in serviceIds) {
//                        val document = db.collection("staycation").document(serviceId.serviceId).get().await()
//
//                        if (document.exists()) {
//                            val staycationId = document.id
//                            val hostId = document.getString("hostId") ?: ""
//                            val noOfGuests = document.getLong("noOfGuests")?.toInt() ?: 0
//                            val noOfBedrooms = document.getLong("noOfBedrooms")?.toInt() ?: 0
//                            val noOfBeds = document.getLong("noOfBeds")?.toInt() ?: 0
//                            val noOfBathrooms = document.getLong("noOfBathrooms")?.toInt() ?: 0
//                            val staycationDescription = document.getString("staycationDescription") ?: ""
//                            val staycationLocation = document.getString("staycationLocation") ?: ""
//                            val staycationPrice = document.getDouble("staycationPrice") ?: 0.0
//                            val staycationSpace = document.getString("staycationSpace") ?: ""
//                            val staycationTitle = document.getString("staycationTitle") ?: ""
//                            val staycationType = document.getString("staycationType") ?: ""
//                            val hasSecurityCamera = document.getBoolean("hasSecurityCamera") ?: false
//                            val hasWeapon = document.getBoolean("hasWeapon") ?: false
//                            val hasDangerousAnimal = document.getBoolean("hasDangerousAnimal") ?: false
//
//                            val touristInfo = getTouristInfo(hostId)
//                            val staycationImages = getStaycationImages(staycationId)
//                            val staycationTags = getStaycationTags(staycationId)
//                            val promotions = getPromotions(staycationId)
//                            val availability = getStaycationAvailability(staycationId)
//                            val amenities = getAmenities(staycationId)
//                            val bookings = getStaycationBookings(staycationId)
//
//                            val staycation = Staycation(
//                                staycationId = staycationId,
//                                hostFirstName = touristInfo?.firstName ?: "",
//                                hostMiddleName = touristInfo?.middleName ?: "",
//                                hostLastName = touristInfo?.lastName ?: "",
//                                noOfGuests = noOfGuests,
//                                noOfBedrooms = noOfBedrooms,
//                                noOfBeds = noOfBeds,
//                                noOfBathrooms = noOfBathrooms,
//                                staycationDescription = staycationDescription,
//                                staycationLocation = staycationLocation,
//                                staycationPrice = staycationPrice,
//                                staycationSpace = staycationSpace,
//                                staycationTitle = staycationTitle,
//                                staycationType = staycationType,
//                                hasSecurityCamera = hasSecurityCamera,
//                                hasWeapon = hasWeapon,
//                                hasDangerousAnimal = hasDangerousAnimal,
//                                staycationImages = staycationImages,
//                                staycationTags = staycationTags,
//                                promotions = promotions,
//                                availableDates = availability,
//                                amenities = amenities,
//                                staycationBookings = bookings,
//                                averageReviewRating = bookings.calculateAverageReviewRating()
//                            )
//                            staycationList.add(staycation)
//                        }
//                    }
//
//                    staycationList
//
//                    // Return the loaded data as LoadResult.Page
//                    LoadResult.Page(
//                        data = staycationList,
//                        prevKey = null,  // Pass null for the initial load
//                        nextKey = getNextPageKey(nextPage)  // Use your logic to get the next page
//                    )
//                } catch (e: Exception) {
//                    LoadResult.Error(e)
//                }
//            }
//
//            override fun getRefreshKey(state: PagingState<QuerySnapshot, Staycation>): QuerySnapshot? {
//                TODO("Not yet implemented")
//            }
//
//            private suspend fun getNextPageKey(nextPage: Query): QuerySnapshot? {
//                return try {
//                    nextPage.get().await()
//                } catch (e: Exception) {
//                    null
//                }
//            }
//
//            // Override other functions as needed
//        }
//    }

//    suspend fun getAllStaycationsByTag(tab: String): List<Staycation> {
//        return try {
//            // Fetch serviceIds with the specified tag
//            val serviceIds = getServiceIdsByTag(tab)
//
//            // Fetch staycations with document id (staycationId) equal to the serviceId
//            val staycationList = mutableListOf<Staycation>()
//
//            for (serviceId in serviceIds) {
//                val document = db.collection("staycation").document(serviceId.serviceId).get().await()
//
//                if (document.exists()) {
//                    val staycationId = document.id
//                    val hostId = document.getString("hostId") ?: ""
//                    val noOfGuests = document.getLong("noOfGuests")?.toInt() ?: 0
//                    val noOfBedrooms = document.getLong("noOfBedrooms")?.toInt() ?: 0
//                    val noOfBeds = document.getLong("noOfBeds")?.toInt() ?: 0
//                    val noOfBathrooms = document.getLong("noOfBathrooms")?.toInt() ?: 0
//                    val staycationDescription = document.getString("staycationDescription") ?: ""
//                    val staycationLocation = document.getString("staycationLocation") ?: ""
//                    val staycationPrice = document.getDouble("staycationPrice") ?: 0.0
//                    val staycationSpace = document.getString("staycationSpace") ?: ""
//                    val staycationTitle = document.getString("staycationTitle") ?: ""
//                    val staycationType = document.getString("staycationType") ?: ""
//                    val hasSecurityCamera = document.getBoolean("hasSecurityCamera") ?: false
//                    val hasWeapon = document.getBoolean("hasWeapon") ?: false
//                    val hasDangerousAnimal = document.getBoolean("hasDangerousAnimal") ?: false
//
//                    val touristInfo = getTouristInfo(hostId)
//                    val staycationImages = getStaycationImages(staycationId)
//                    val staycationTags = getStaycationTags(staycationId)
//                    val promotions = getPromotions(staycationId)
//                    val availability = getStaycationAvailability(staycationId)
//                    val amenities = getAmenities(staycationId)
//                    val bookings = getStaycationBookings(staycationId)
//
//                    val staycation = Staycation(
//                        staycationId = staycationId,
//                        hostFirstName = touristInfo?.firstName ?: "",
//                        hostMiddleName = touristInfo?.middleName ?: "",
//                        hostLastName = touristInfo?.lastName ?: "",
//                        noOfGuests = noOfGuests,
//                        noOfBedrooms = noOfBedrooms,
//                        noOfBeds = noOfBeds,
//                        noOfBathrooms = noOfBathrooms,
//                        staycationDescription = staycationDescription,
//                        staycationLocation = staycationLocation,
//                        staycationPrice = staycationPrice,
//                        staycationSpace = staycationSpace,
//                        staycationTitle = staycationTitle,
//                        staycationType = staycationType,
//                        hasSecurityCamera = hasSecurityCamera,
//                        hasWeapon = hasWeapon,
//                        hasDangerousAnimal = hasDangerousAnimal,
//                        staycationImages = staycationImages,
//                        staycationTags = staycationTags,
//                        promotions = promotions,
//                        availableDates = availability,
//                        amenities = amenities,
//                        staycationBookings = bookings,
//                        averageReviewRating = bookings.calculateAverageReviewRating()
//                    )
//                    staycationList.add(staycation)
//                }
//            }
//
//            staycationList
//
//        } catch (e: Exception) {
//            e.printStackTrace()
//            emptyList() // Handle the error case as needed
//        }
//    }



//    suspend fun getAllStaycationsByTag(tab: String, currentPage: QuerySnapshot?): List<Staycation> {
//        return try {
//            // Fetch serviceIds with the specified tag
//            val serviceIds = getServiceIdsByTag(tab)
//
//            // Fetch staycations with document id (staycationId) equal to the serviceId
//            val staycationList = mutableListOf<Staycation>()
//
//            for (serviceId in serviceIds) {
//                val document = db.collection("staycation").document(serviceId.serviceId).get().await()
//
//                if (document.exists()) {
//                    val staycationId = document.id
//                    val hostId = document.getString("hostId") ?: ""
//                    val noOfGuests = document.getLong("noOfGuests")?.toInt() ?: 0
//                    val noOfBedrooms = document.getLong("noOfBedrooms")?.toInt() ?: 0
//                    val noOfBeds = document.getLong("noOfBeds")?.toInt() ?: 0
//                    val noOfBathrooms = document.getLong("noOfBathrooms")?.toInt() ?: 0
//                    val staycationDescription = document.getString("staycationDescription") ?: ""
//                    val staycationLocation = document.getString("staycationLocation") ?: ""
//                    val staycationPrice = document.getDouble("staycationPrice") ?: 0.0
//                    val staycationSpace = document.getString("staycationSpace") ?: ""
//                    val staycationTitle = document.getString("staycationTitle") ?: ""
//                    val staycationType = document.getString("staycationType") ?: ""
//                    val hasSecurityCamera = document.getBoolean("hasSecurityCamera") ?: false
//                    val hasWeapon = document.getBoolean("hasWeapon") ?: false
//                    val hasDangerousAnimal = document.getBoolean("hasDangerousAnimal") ?: false
//
//                    val touristInfo = getTouristInfo(hostId)
//                    val staycationImages = getStaycationImages(staycationId)
//                    val staycationTags = getStaycationTags(staycationId)
//                    val promotions = getPromotions(staycationId)
//                    val availability = getStaycationAvailability(staycationId)
//                    val amenities = getAmenities(staycationId)
//                    val bookings = getStaycationBookings(staycationId)
//
//                    val staycation = Staycation(
//                        staycationId = staycationId,
//                        hostFirstName = touristInfo?.firstName ?: "",
//                        hostMiddleName = touristInfo?.middleName ?: "",
//                        hostLastName = touristInfo?.lastName ?: "",
//                        noOfGuests = noOfGuests,
//                        noOfBedrooms = noOfBedrooms,
//                        noOfBeds = noOfBeds,
//                        noOfBathrooms = noOfBathrooms,
//                        staycationDescription = staycationDescription,
//                        staycationLocation = staycationLocation,
//                        staycationPrice = staycationPrice,
//                        staycationSpace = staycationSpace,
//                        staycationTitle = staycationTitle,
//                        staycationType = staycationType,
//                        hasSecurityCamera = hasSecurityCamera,
//                        hasWeapon = hasWeapon,
//                        hasDangerousAnimal = hasDangerousAnimal,
//                        staycationImages = staycationImages,
//                        staycationTags = staycationTags,
//                        promotions = promotions,
//                        availableDates = availability,
//                        amenities = amenities,
//                        staycationBookings = bookings,
//                        averageReviewRating = bookings.calculateAverageReviewRating()
//                    )
//                    staycationList.add(staycation)
//                }
//            }
//
//            staycationList
//        } catch (e: Exception) {
//            e.printStackTrace()
//            emptyList() // Handle the error case as needed
//        }
//    }



//        filter: Filter,

//    suspend fun getAllStaycations(filter: Filter, tab: String): List<Staycation> {
//        return try {
//            val result = db.collection("staycation")
//
//                .get()
//                .await()
//
//            val staycationList = mutableListOf<Staycation>()
//
//            for (document in result.documents) {
//                val staycationId = document.id
//                val hostId = document.getString("hostId") ?: ""
//                val noOfGuests = document.getLong("noOfGuests")?.toInt() ?: 0
//                val noOfBedrooms = document.getLong("noOfBedrooms")?.toInt() ?: 0
//                val noOfBeds = document.getLong("noOfBeds")?.toInt() ?: 0
//                val noOfBathrooms = document.getLong("noOfBathrooms")?.toInt() ?: 0
//                val staycationDescription = document.getString("staycationDescription") ?: ""
//                val staycationLocation = document.getString("staycationLocation") ?: ""
//                val staycationPrice = document.getDouble("staycationPrice") ?: 0.0
//                val staycationSpace = document.getString("staycationSpace") ?: ""
//                val staycationTitle = document.getString("staycationTitle") ?: ""
//                val staycationType = document.getString("staycationType") ?: ""
//                val hasSecurityCamera = document.getBoolean("hasSecurityCamera") ?: false
//                val hasWeapon = document.getBoolean("hasWeapon") ?: false
//                val hasDangerousAnimal = document.getBoolean("hasDangerousAnimal") ?: false
//
//                val touristInfo = getTouristInfo(hostId)
//                val staycationImages = getStaycationImages(staycationId)
//                val staycationTags = getStaycationTags(staycationId)
//                val promotions = getPromotions(staycationId)
//                val availability = getStaycationAvailability(staycationId)
//                val amenities = getAmenities(staycationId)
//                val bookings = getStaycationBookings(staycationId)
//
//                val staycation = Staycation(
//                    staycationId = staycationId,
//                    hostFirstName = touristInfo?.firstName ?: "",
//                    hostMiddleName = touristInfo?.middleName ?: "",
//                    hostLastName = touristInfo?.lastName ?: "",
//                    noOfGuests = noOfGuests,
//                    noOfBedrooms = noOfBedrooms,
//                    noOfBeds = noOfBeds,
//                    noOfBathrooms = noOfBathrooms,
//                    staycationDescription = staycationDescription,
//                    staycationLocation = staycationLocation,
//                    staycationPrice = staycationPrice,
//                    staycationSpace = staycationSpace,
//                    staycationTitle = staycationTitle,
//                    staycationType = staycationType,
//                    hasSecurityCamera = hasSecurityCamera,
//                    hasWeapon = hasWeapon,
//                    hasDangerousAnimal = hasDangerousAnimal,
//                    staycationImages = staycationImages,
//                    staycationTags = staycationTags,
//                    promotions = promotions,
//                    availableDates = availability,
//                    amenities = amenities,
//                    staycationBookings = bookings,
//                    averageReviewRating = bookings.calculateAverageReviewRating()
//                )
//                staycationList.add(staycation)
//            }
//
//            staycationList
//
//        } catch (e: Exception) {
//            e.printStackTrace()
//            emptyList() // Handle the error case as needed
//        }
//    }



//    suspend fun getAllStaycations(): List<Staycation> {
//        return try {
//            val result = staycationCollection.get().await()
//
//            val staycationList = mutableListOf<Staycation>()
//
//            for (document in result.documents) {
//                val staycationId = document.id
//                // Extract other fields as needed from the document
//                // For example:
//                // val name = document.getString("name") ?: ""
//
//                // Construct a Staycation object and add it to the list
//                val staycation = Staycation(staycationId /*, other fields*/)
//                staycationList.add(staycation)
//            }
//
//            Log.d("UserRepository", "Staycation List: $staycationList")
//
//            staycationList
//
//        } catch (e: Exception) {
//            e.printStackTrace()
//            emptyList() // Handle the error case as needed
//        }
//    }



//
//    private fun calculateAverageReviewRating(reviews: List<Review>): Double {
//        if (reviews.isEmpty()) {
//            return 0.0
//        }
//
//        val totalRating = reviews.sumBy { it.rating }
//        return totalRating.toDouble() / reviews.size
//    }

//    private suspend fun getBookingReview(bookingId: String): Review? {
//        return try {
//            val result = reviewCollection
//                .whereEqualTo("bookingId", bookingId)
//                .get()
//                .await()
//
//            val reviews = mutableListOf<Review>()
//
//            for (document in result.documents) {
//                val reviewId = document.id
//                val reviewComment = document.getString("reviewComment") ?: ""
//                val reviewDate = document.getTimestamp("reviewDate")?.toDate()
//                val reviewRating = document.getLong("reviewRating")?.toInt() ?: 0
//                val serviceType = document.getString("serviceType") ?: ""
//
//                // Construct a BookingReview object and add it to the list
//                val review = Review(
//                    reviewId = reviewId,
//                    comment = reviewComment,
//                    reviewDate = reviewDate,
//                    rating = reviewRating,
//                    serviceType = serviceType
//                    // Add other fields as needed
//                )
//
//                reviews.add(review)
//            }
//
//            // Return the first review if available, otherwise return null
//            reviews.firstOrNull()
//
//        } catch (e: Exception) {
//            e.printStackTrace()
//            null // Handle the error case as needed
//        }
//    }


//    private suspend fun getPromotions(staycationId: String): List<Promotion> {
//        return try {
//            val result = promotionCollection
//                .whereEqualTo("serviceId", staycationId)
//                .get()
//                .await()
//
//            val promotions = mutableListOf<Promotion>()
//
//            for (document in result.documents) {
//                val promoId = document.getString("promoId") ?: ""
//                val status = document.getString("status") ?: ""
//
//                promotions.add(Promotion(promoId, staycationId, status))
//            }
//
//            promotions
//
//        } catch (e: Exception) {
//            e.printStackTrace()
//            emptyList() // Handle the error case as needed
//        }
//    }


//    suspend fun loginUser(username: String, password: String): Boolean {
//        return try {
//            val hashedPassword = hashPassword(password) // Hash the provided password
//
//            val result = touristCollection
//                .whereEqualTo("username", username)
//                .whereEqualTo("password", hashedPassword) // Compare hashed password
//                .get()
//                .await()
//
//            result.documents.isNotEmpty()
//        } catch (e: Exception) {
//            e.printStackTrace()
//            false
//        }
//    }


//suspend fun loginUser(username: String, password: String): Boolean {
//    return try {
//        val result = touristCollection
//            .whereEqualTo("username", username)
//            .whereEqualTo("password", password)
//            .get()
//            .await()
//
//        result.documents.isNotEmpty()
//    } catch (e: Exception) {
//        // Handle the exception, here assuming login failed due to exception
//        e.printStackTrace()
//        false
//    }
//}

//    suspend fun getUserByCredentials(
//        username: String,
//        password: String
//    ): Tourist? = withContext(Dispatchers.IO) {
//        return@withContext try {
//            val result = touristCollection
//                .whereEqualTo("username", username)
//                .whereEqualTo("password", password)
//                .get()
//                .await()
//
//            if (result.documents.isNotEmpty()) {
//                val document = result.documents[0]
//                val retrievedUsername = document.getString("username") ?: ""
//                val retrievedPassword = document.getString("password") ?: ""
//                Tourist(retrievedUsername, retrievedPassword)
//            } else {
//                null // No user found for provided credentials
//            }
//        } catch (e: Exception) {
//            null // Handle the error according to your application's requirements
//        }
//    }
//    suspend fun addUser(user: Tourist): Boolean = withContext(Dispatchers.IO) {
//        return@withContext try {
//            val usernameExists = checkIfUsernameExists(user.username)
//            if (usernameExists) {
//                // Username already exists, return false or handle as needed
//                false
//            } else {
//                val userData = hashMapOf(
//                    "fullName" to mapOf(
//                        "firstName" to user.firstName,
//                        "middleName" to user.middleName,
//                        "lastName" to user.lastName
//                    ),
//                    "username" to user.username,
//                    "password" to user.password
//                )
//                val touristDocRef = touristCollection.add(userData).await()
//                val userId = touristDocRef.id // Get the ID of the newly added document in tourist collection
//                val hostId = "HOST-$userId"
//
//                // Create data for the host collection and add the document
//                val hostData = hashMapOf(
//                    "userId" to userId
//                    // Add more fields as needed
//                )
//                hostCollection.document(hostId).set(hostData).await()
//
//                true // Return true if addition was successful
//            }
//        } catch (e: Exception) {
//            false // Return false or handle the error according to your application's requirements
//        }
//    }



//    suspend fun addUser(user: Tourist): OperationResult {
//        return try {
//            val usernameExists = checkIfUsernameExists(user.username)
//            return if (usernameExists) {
//                OperationResult.Error("Username already exists")
//            } else {
//                val userData = hashMapOf(
//                    "fullName" to mapOf(
//                        "firstName" to user.firstName,
//                        "middleName" to user.middleName,
//                        "lastName" to user.lastName
//                    ),
//                    "username" to user.username,
//                    "password" to user.password
//                )
//                val touristDocRef = touristCollection.add(userData).await()
//                val userId = touristDocRef.id
//
//                val hostData = hashMapOf(
//                    "userId" to userId
//                )
//                hostCollection.document("HOST-$userId").set(hostData).await()
//
//                OperationResult.Success("Account created successfully")
//            }
//        } catch (e: Exception) {
//            OperationResult.Error(e.localizedMessage ?: "An error occurred")
//        }
//    }


//    private suspend fun checkIfUsernameExists(
//        username: String
//    ): Boolean = withContext(Dispatchers.IO) {
//        return@withContext try {
//            val result = touristCollection.whereEqualTo("username", username).get().await()
//            result.documents.isNotEmpty()
//        } catch (e: Exception) {
//            // Handle the error according to your application's requirements
//            false
//        }
//    }


//    suspend fun addTouristPreferences(userId: String, preferences: List<String>): Boolean {
//        return try {
//            val preferencesData = hashMapOf(
//                "userId" to userId,
//                "preferences" to preferences
//            )
//
//            touristPreferencesCollection.document(userId).set(preferencesData).await()
//
//            // Update currentUser with the new preferences
//            currentUser?.preferences = preferences.map { Preference(it) }
//
//            true
//        } catch (e: Exception) {
//            e.printStackTrace()
//            false
//        }
//    }

//    suspend fun addTouristPreferences(userId: String, preferences: List<String>): Boolean {
//        return try {
//            // Create a Firestore batch write
//            val batch = db.batch()
//
//            // Get a reference to the document in the "tourist_preference" collection with the specified userId
//            val userPreferenceDocRef = touristPreferencesCollection.document(userId)
//
//            // Set the data for the document using the batch write
//            batch.set(userPreferenceDocRef, mapOf("userId" to userId, "preference" to preferences))
//
//            // You can add more batch.set() calls if you need to update other documents in the same batch
//
//            // Commit the batch operation
//            batch.commit().await()
//
//            // Update currentUser with the new preferences
//            currentUser?.preferences = preferences.map { Preference(it) }
//
//            true
//        } catch (e: Exception) {
//            // Handle any exceptions that might occur during the operation
//            e.printStackTrace()
//            false
//        }
//    }

