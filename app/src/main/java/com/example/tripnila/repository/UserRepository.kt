package com.example.tripnila.repository

import android.net.Uri
import android.util.Log
import com.example.tripnila.common.Constants
import com.example.tripnila.data.Amenity
import com.example.tripnila.data.Host
import com.example.tripnila.data.Photo
import com.example.tripnila.data.Preference
import com.example.tripnila.data.Promotion
import com.example.tripnila.data.Review
import com.example.tripnila.data.Staycation
import com.example.tripnila.data.StaycationAvailability
import com.example.tripnila.data.StaycationBooking
import com.example.tripnila.data.Tag
import com.example.tripnila.data.Tourist
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.security.MessageDigest
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.UUID
import java.util.concurrent.TimeUnit
import kotlin.experimental.and
import kotlin.math.min


class UserRepository {
    val db = FirebaseFirestore.getInstance()
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
    private val staycationNearbyAttractionCollection = db.collection("staycation_nearby_attraction")
    private val reviewCollection = db.collection("review")
    private val likeCollection = db.collection("like")
    private val commentCollection = db.collection("comment")
    private val paymentCollection = db.collection("payment")
    private val touristProfileCollection = db.collection("tourist_profile")
    private val reviewPhotoCollection = db.collection("review_photo")

    private val paymentHistoryCollection = db.collection("payment_history")
    private val staycationBookingHistoryCollection = db.collection("staycation_booking_history")
    private val staycationHistoryCollection = db.collection("staycation_history")
    private val serviceAmenityHistoryCollection = db.collection("service_amenity_history")
    private val servicePhotoHistoryCollection = db.collection("service_photo_history")

    private val storageReference = FirebaseStorage.getInstance().reference


    private var currentUser: Tourist? = null
    private var staycationList: List<Staycation>? = null




//        hasDangerousAnimal: Boolean,
//        hasSecurityCamera: Boolean,
//        hasWeapon: Boolean,
//        hostId: String,
//        noOfBathrooms: Int,
//        noOfBedrooms: Int,
//        noOfBeds: Int,
//        noOfGuests: Int,
//        staycationDescription: String,
//        staycationLocation: String,
//        staycationPrice: Int,
//        staycationSpace: String,
//        staycationTitle: String,
//        staycationType: String,
//        amenities: List<String>,
//        photos: List<Photo>,
//        availableDates: List<Timestamp>,
//        promotions: List<Promotion>,
//        nearbyAttractions: List<String>,          /*TODO*/
//        staycationTags: List<String>               /*TODO*/

    suspend fun forAddingRecordsFromCSV(
        user: Tourist,
        staycationDescription: String = "",
        staycationLocation: String = "",
        staycationPrice: Int = 0,
        staycationTitle: String = "",
        amenities: List<String> = emptyList(),
        nearbyAttractions: List<String> = emptyList(),
        staycationTags: List<String> = emptyList()
    ): Boolean {
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

            val isSuccess = addStaycation(
                staycationTitle = staycationTitle,
                staycationDescription = staycationDescription,
                staycationTags = staycationTags,
                hostId = "HOST-$touristId",
                staycationPrice = staycationPrice.toDouble(),
                amenities = amenities,
                staycationLocation = staycationLocation,
                nearbyAttractions = nearbyAttractions

            )

            Log.d("AddStaycation", "$isSuccess")

            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }

    }

    suspend fun addStaycation(
        hasDangerousAnimal: Boolean = false,
        hasSecurityCamera: Boolean = false,
        hasWeapon: Boolean = false,
        hostId: String = "",
        noOfBathrooms: Int = 1,
        noOfBedrooms: Int = 1,
        noOfBeds: Int = 1,
        noOfGuests: Int = 1,
        staycationDescription: String = "",
        staycationLocation: String = "",
        staycationPrice: Double = 0.0,
        staycationSpace: String = "",
        staycationTitle: String = "",
        staycationType: String = "",
        amenities: List<String> = emptyList(),
        photos: List<Photo> = emptyList(),
        availableDates: List<Timestamp> = emptyList(),
        promotions: List<Promotion> = emptyList(),
        nearbyAttractions: List<String> = emptyList(),
        staycationTags: List<String> = emptyList()
    ): Boolean {
        try {
            val querySnapshot = staycationHistoryCollection
                .orderBy("staycationId", Query.Direction.DESCENDING)
                .limit(1)
                .get()
                .await()

            val lastDocument = querySnapshot.documents.firstOrNull()
            val lastId = lastDocument?.getString("staycationId")
            val newId = (lastId?.toInt() ?: 0) + 1

            val staycation = hashMapOf(
                "hasDangerousAnimal" to hasDangerousAnimal,
                "hasSecurityCamera" to hasSecurityCamera,
                "hasWeapon" to hasWeapon,
                "hostId" to hostId,
                "noOfBathrooms" to noOfBathrooms,
                "noOfBedrooms" to noOfBedrooms,
                "noOfBeds" to noOfBeds,
                "noOfGuests" to noOfGuests,
                "staycationDescription" to staycationDescription,
                "staycationLocation" to staycationLocation,
                "staycationPrice" to staycationPrice,
                "staycationSpace" to staycationSpace,
                "staycationTitle" to staycationTitle,
                "staycationType" to staycationType
            )

            staycationCollection.document(newId.toString()).set(staycation).await()

            addStaycationToHistory(newId.toString(), "add")

            addServiceAmenities(newId.toString(), "Staycation", amenities)

            addServicePhotos(newId.toString(), "Staycation", photos)

            addStaycationAvailability(newId.toString(), availableDates)

            addStaycationPromotions(newId.toString(), promotions)

            addStaycationNearbyAttractions(newId.toString(), nearbyAttractions)

            addServiceTags(newId.toString(), "Staycation", staycationTags)

            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

//    suspend fun addStaycationNearbyAttractions(staycationId: String, attractions: List<String>) {
//        try {
//            // Query the staycationNearbyAttractionCollection for documents where the staycationId field is equal to the staycationId parameter
//            val querySnapshot = staycationNearbyAttractionCollection
//                .whereEqualTo("staycationId", staycationId)
//                .get()
//                .await()
//
//            // Get the ID of the first document in the query snapshot, or set documentId to null if the query snapshot is empty
//            val documentId = if (querySnapshot.documents.isNotEmpty()) {
//                querySnapshot.documents.first().id
//            } else {
//                null
//            }
//
//            // Create a batch write
//            val batch = Firebase.firestore.batch()
//
//            // Iterate over the attractions list and set the data for each attraction
//            attractions.forEach { attraction ->
//                val attractionData = hashMapOf(
//                    "staycationId" to staycationId,
//                    "attractionName" to attraction
//                )
//
//                // If documentId is not null, update the existing document with the new data using the merge option
//                if (documentId != null) {
//                    val attractionRef = staycationNearbyAttractionCollection.document(documentId)
//                    batch.set(attractionRef, attractionData, SetOptions.merge())
//                } else {
//                    // Otherwise, create a new document
//                    batch.set(staycationNearbyAttractionCollection.document(), attractionData)
//                }
//            }
//
//            // Commit the batch write
//            batch.commit().await()
//
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//    }

    suspend fun addServiceTags(serviceId: String, serviceType: String, tags: List<String>) {
        try {
            tags.forEach { tag ->
                val tagData = hashMapOf(
                    "serviceId" to serviceId,
                    "serviceType" to serviceType,
                    "tagName" to tag
                )

                serviceTagCollection.add(tagData).await()

            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    suspend fun addStaycationNearbyAttractions(staycationId: String, attractions: List<String>) {
        try {

            attractions.forEach { attraction ->
                val attractionData = hashMapOf(
                    "staycationId" to staycationId,
                    "attractionName" to attraction
                )

                staycationNearbyAttractionCollection.add(attractionData).await()

            }


        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun addStaycationPromotions(staycationId: String, promotions: List<Promotion>) {
        try {
            if (promotions.isNotEmpty()) {
                promotions.forEach { promotion ->
                    val promotionData = hashMapOf(
                        "staycationId" to staycationId,
                        "promoId" to promotion.promoId
                    )

                    servicePromotionCollection.add(promotionData).await()
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun addStaycationAvailability(staycationId: String, availableDates: List<Timestamp>) {
        try {
            availableDates.forEach { date ->
                val availabilityData = hashMapOf(
                    "staycationId" to staycationId,
                    "availableDate" to date
                )

                staycationAvailabilityCollection.add(availabilityData).await()

            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    suspend fun addServicePhotos(serviceId: String, serviceType: String, photos: List<Photo>) {
        try {
            photos.mapNotNull { it.photoUri }.forEach { uri ->
                val fileName = UUID.randomUUID().toString()
                val imageRef = storageReference.child("images/service_photos/${serviceType.lowercase(Locale.getDefault())}s/$fileName")
                imageRef.putFile(uri).await()

                val photoUrl = imageRef.downloadUrl.await().toString()

                val photoType = photos.firstOrNull { it.photoUri == uri }?.photoType ?: "Others"

                val photoData = hashMapOf(
                    "serviceId" to serviceId,
                    "serviceType" to serviceType,
                    "photoUrl" to photoUrl,
                    "photoType" to photoType
                )

                val documentReference = servicePhotoCollection.add(photoData).await()
                val servicePhotoId = documentReference.id

                addServicePhotosToHistory(servicePhotoId, photoData, "add")


            }

        } catch (e: Exception) {
            e.printStackTrace()
            // Handle the error case as needed
        }
    }

    suspend fun addServicePhotosToHistory(servicePhotoId: String, photoData: HashMap<String, String>, transaction: String) {
        try {
            val historyData = hashMapOf(
                "servicePhotoId" to servicePhotoId,
                "transaction" to transaction,
                "timestamp" to FieldValue.serverTimestamp() // timestamp of the operation
            )

            // Combine amenity data and history data
            val combinedData = photoData + historyData

            // Add to service_amenity_history collection
            servicePhotoHistoryCollection.add(combinedData).await()

        } catch (e: Exception) {
            e.printStackTrace()
            // Handle the error case as needed
        }
    }



    suspend fun addServiceAmenities(serviceId: String, serviceType: String, amenities: List<String>) {
        try {
            amenities.forEach { amenity ->
                val data = hashMapOf(
                    "serviceId" to serviceId,
                    "amenityName" to amenity,
                    "serviceType" to serviceType
                )

                // Use add() to automatically generate a unique document ID
                val documentReference = serviceAmenityCollection.add(data).await()

                val documentId = documentReference.id

                addServiceAmenityToHistory(documentId, data, "add")

            }
        } catch (e: Exception) {
            e.printStackTrace()
            // Handle the error case as needed
        }
    }

    suspend fun addServiceAmenityToHistory(amenityId: String, amenityData: HashMap<String, String>, transaction: String) {
        try {

            val historyData = hashMapOf(
                "amenityId" to amenityId,
                "transaction" to transaction,
                "timestamp" to FieldValue.serverTimestamp() // timestamp of the operation
            )

            // Combine amenity data and history data
            val combinedData = amenityData + historyData

            // Add to service_amenity_history collection
            serviceAmenityHistoryCollection.add(combinedData).await()


        } catch (e: Exception) {
            e.printStackTrace()
            // Handle the error case as needed
        }
    }


    suspend fun addStaycationToHistory(staycationId: String, transaction: String) {
        try {
            val staycationDocument = staycationCollection.document(staycationId).get().await()

            // Check if the document exists
            if (staycationDocument.exists()) {
                // Get the data from the document
                val staycationData = staycationDocument.data

                // Add additional fields
                staycationData?.put("staycationId", staycationId)
                staycationData?.put("transaction", transaction) // or "update" or "delete" based on your use case
                staycationData?.put("transactionTimestamp", FieldValue.serverTimestamp())

                // Add the fetched document to the payment_history collection
                staycationHistoryCollection.add(staycationData!!).await()
            } else {
                Log.d("", "No document found with the provided paymentId")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            // Handle the error case as needed
        }
    }

    suspend fun addStaycationReview(
        bookingId: String,
        reviewComment: String,
        reviewRating: Int,
        serviceType: String,
        reviewPhotos: List<Uri?>
    ): Boolean {
        try {

            val uniqueId = UUID.randomUUID().toString()

            val reviewData = hashMapOf(
                "bookingId" to bookingId,
                "reviewComment" to reviewComment,
                "reviewDate" to FieldValue.serverTimestamp(),
                "reviewRating" to reviewRating,
                "serviceType" to serviceType
            )

            // Add data to the 'review' collection
            val reviewDocRef = reviewCollection.add(reviewData).await()
            val reviewId = reviewDocRef.id

            // Add data to the 'review_photo' collection for each non-null photo in the list
            reviewPhotos.filterNotNull().forEach { uri ->
                val fileName = UUID.randomUUID().toString()
                val imageRef = storageReference.child("images/reviews/$fileName")
                imageRef.putFile(uri).await()

                val photoUrl = imageRef.downloadUrl.await().toString()

                val reviewPhotoData = hashMapOf(
                    "reviewId" to reviewId,
                    "reviewPhotoUrl" to photoUrl
                )

                reviewPhotoCollection.add(reviewPhotoData).await()
            }

            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
            // Handle the error case as needed
        }
    }

//    suspend fun getStaycationBookingsForTourist(touristId: String, startAt: Int, pageSize: Int): StaycationBooking {
//        try {
//            val query = db.collection("staycation_booking")
//                .whereEqualTo("touristId", touristId)
//                .orderBy("bookingDate")
//                .limit(pageSize.toLong())
//                .startAt(startAt.toLong())
//
//            val result = query.get().await()
//
//            for (document in result.documents) {
//                val bookingId = document.id
//                val bookingDate = document.getDate("bookingDate") ?: Date()
//                val bookingStatus = document.getString("bookingStatus") ?: ""
//                val checkInDate = document.getDate("checkInDate") ?: Date()
//                val checkOutDate = document.getDate("checkOutDate") ?: Date()
//                val noOfGuests = document.getLong("noOfGuests")?.toInt() ?: 0
//                val noOfInfants = document.getLong("noOfInfants")?.toInt() ?: 0
//                val noOfPets = document.getLong("noOfPets")?.toInt() ?: 0
//                val totalAmount = document.getLong("totalAmount")?.toDouble() ?: 0.0
//
//
//                val staycationBooking = StaycationBooking(
//                    staycationBookingId = bookingId,
//                    bookingDate = bookingDate,
//                    bookingStatus = bookingStatus,
//                    checkInDate = checkInDate,
//                    checkOutDate = checkOutDate,
//                    noOfGuests = noOfGuests,
//                    noOfInfants = noOfInfants,
//                    noOfPets = noOfPets,
//                    totalAmount = totalAmount,
//                    tourist = Tourist(touristId = touristId),
//                )
//
//            return staycationBooking
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//            StaycationBooking()
//    }

    suspend fun getStaycationBookingsForTourist(touristId: String, startAt: Int, pageSize: Int): List<StaycationBooking> {
        try {
            val query = staycationBookingCollection
                .whereEqualTo("touristId", touristId)
                .orderBy("bookingDate")
                .limit(pageSize.toLong())
                .startAt(startAt.toLong())

            val result = query.get().await()

            val staycationBookings = mutableListOf<StaycationBooking>()

            for (document in result.documents) {
                val bookingId = document.id
                val bookingDate = document.getDate("bookingDate") ?: Date()
                val bookingStatus = document.getString("bookingStatus") ?: ""
                val checkInDate = document.getDate("checkInDate") ?: Date()
                val checkOutDate = document.getDate("checkOutDate") ?: Date()
                val noOfGuests = document.getLong("noOfGuests")?.toInt() ?: 0
                val noOfInfants = document.getLong("noOfInfants")?.toInt() ?: 0
                val noOfPets = document.getLong("noOfPets")?.toInt() ?: 0
                val staycationId = document.getString("staycationId") ?: ""
                val totalAmount = document.getLong("totalAmount")?.toDouble() ?: 0.0

//                val staycation = getStaycationDetailsById(staycationId) ?: Staycation()
//                val review = getBookingReview(bookingId, touristId)

                val staycationBooking = StaycationBooking(
                    staycationBookingId = bookingId,
                    bookingDate = bookingDate,
                    bookingStatus = bookingStatus,
                    checkInDate = checkInDate,
                    checkOutDate = checkOutDate,
                    noOfGuests = noOfGuests,
                    noOfInfants = noOfInfants,
                    noOfPets = noOfPets,
                    //     staycation = staycation,
                    totalAmount = totalAmount,
                    tourist = Tourist(touristId = touristId),
                    //        bookingReview = review
                )

                staycationBookings.add(staycationBooking)
            }

            return staycationBookings
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return emptyList()
    }



    suspend fun updateBookingStatusToOngoingIfCheckInDatePassed() {
        try {
            val currentTimestamp = Timestamp.now()

            val query = staycationBookingCollection
                .whereLessThanOrEqualTo("checkInDate", currentTimestamp)
                .whereEqualTo("bookingStatus", "Pending")

            val result = query.get().await()

            Log.d("BookingStatusUpdate", "Found ${result.size()} bookings to check")

            for (document in result.documents) {
                val bookingId = document.id

                staycationBookingCollection.document(bookingId)
                    .update("bookingStatus", "Ongoing")
                    .addOnSuccessListener {
                        // Update successful
                        Log.d("BookingStatusUpdate", "Booking $bookingId status updated to Ongoing")
                    }
                    .addOnFailureListener { e ->
                        // Handle the error
                        Log.d("BookingStatusUpdate", "Error updating booking status: $e")
                    }
            }
        } catch (e: Exception) {
            Log.e("BookingStatusUpdate", "An error occurred: $e")
        }
    }


    suspend fun updateBookingStatusToFinishedIfExpired() {
        try {
            val currentTimestamp = Timestamp.now()

            val query = staycationBookingCollection
                .whereLessThan("checkOutDate", currentTimestamp)
                .whereEqualTo("bookingStatus", "Ongoing")

            val result = query.get().await()

            Log.d("BookingStatusUpdate", "Found ${result.size()} bookings to check")

            for (document in result.documents) {

                val checkOutTimestamp = document.getTimestamp("checkOutDate")

                if (checkOutTimestamp  != null) {
                    Log.d("BookingStatusUpdate", "BookingId: ${document.id}, CheckOutDate: $checkOutTimestamp")

                    val bookingId = document.id
                    staycationBookingCollection.document(bookingId)
                        .update("bookingStatus", "Completed")
                        .addOnSuccessListener {
                            // Update successful
                            Log.d("UserRepository", "Booking $bookingId status updated to Finished")
                        }
                        .addOnFailureListener { e ->
                            // Handle the error
                            Log.d("UserRepository", "Errorrrrrr")
                        }
                }
            }
        } catch (e: Exception) {
            Log.e("BookingStatusUpdate", "An error occurred: $e")
        }
    }

    suspend fun getStaycationDetailsById(staycationId: String): Staycation? {
        try {
            val staycationDocument = staycationCollection.document(staycationId).get().await()

            if (staycationDocument.exists()) {
                // Retrieve Staycation details
                val hasDangerousAnimal = staycationDocument.getBoolean("hasDangerousAnimal") ?: false
                val hasSecurityCamera = staycationDocument.getBoolean("hasSecurityCamera") ?: false
                val hasWeapon = staycationDocument.getBoolean("hasWeapon") ?: false
                val hostId = staycationDocument.getString("hostId") ?: ""
                val noOfBathrooms = staycationDocument.getLong("noOfBathrooms")?.toInt() ?: 0
                val noOfBedrooms = staycationDocument.getLong("noOfBedrooms")?.toInt() ?: 0
                val noOfBeds = staycationDocument.getLong("noOfBeds")?.toInt() ?: 0
                val noOfGuests = staycationDocument.getLong("noOfGuests")?.toInt() ?: 0
                val staycationDescription = staycationDocument.getString("staycationDescription") ?: ""
                val staycationLocation = staycationDocument.getString("staycationLocation") ?: ""
                val staycationPrice = staycationDocument.getDouble("staycationPrice") ?: 0.0
                val staycationSpace = staycationDocument.getString("staycationSpace") ?: ""
                val staycationTitle = staycationDocument.getString("staycationTitle") ?: ""
                val staycationType = staycationDocument.getString("staycationType") ?: ""

                // Fetch Staycation images
                val staycationImages = getStaycationImages(staycationId)
                val hostInfo = getHostInfo(hostId)

                return Staycation(
                    staycationId = staycationId,
                    hasDangerousAnimal = hasDangerousAnimal,
                    hasSecurityCamera = hasSecurityCamera,
                    hasWeapon = hasWeapon,
                    // hostId = hostId,
                    noOfBathrooms = noOfBathrooms,
                    noOfBedrooms = noOfBedrooms,
                    noOfBeds = noOfBeds,
                    noOfGuests = noOfGuests,
                    staycationDescription = staycationDescription,
                    staycationLocation = staycationLocation,
                    staycationPrice = staycationPrice,
                    staycationSpace = staycationSpace,
                    staycationTitle = staycationTitle,
                    staycationType = staycationType,
                    staycationImages = staycationImages,
                    host = Host(
                        profilePicture = hostInfo?.profilePicture ?: "",
                        firstName = hostInfo?.firstName ?: "",
                        middleName = hostInfo?.middleName ?: "",
                        lastName = hostInfo?.lastName ?: "",
                        touristId = hostInfo?.touristId ?: "",
                    )
                  //  hostImage = hostInfo?.profilePicture ?: "",
//                    hostFirstName = hostInfo?.firstName ?: "",
//                    hostMiddleName = hostInfo?.middleName ?: "",
//                    hostLastName = hostInfo?.lastName ?: "",

                    // Add other properties as needed
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }


    suspend fun getHostInfo(hostId: String): Tourist? {
        return try {
            // Remove the first 5 characters from hostId to get touristId
            val touristId = hostId.substring(5)
            val touristDocument = touristCollection.document(touristId).get().await()

            if (touristDocument.exists()) {
                val firstName = touristDocument.getString("fullName.firstName") ?: ""
                val middleName = touristDocument.getString("fullName.middleName") ?: ""
                val lastName = touristDocument.getString("fullName.lastName") ?: ""
                val username = touristDocument.getString("username") ?: ""

                val profilePicture = getTouristPhoto(touristId)

                Tourist(
                    touristId = touristId,
                    firstName = firstName,
                    middleName = middleName,
                    lastName = lastName,
                    username = username,
                    password = "",
                    profilePicture = profilePicture ?: "https://upload.wikimedia.org/wikipedia/commons/8/89/Portrait_Placeholder.png"
                ) // Return Tourist object
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


    suspend fun getTouristProfile(touristId: String): Tourist? {
        try {
            val touristDocument = touristCollection.document(touristId).get().await()

            if (touristDocument.exists()) {
                val firstName = touristDocument.getString("fullName.firstName") ?: ""
                val middleName = touristDocument.getString("fullName.middleName") ?: ""
                val lastName = touristDocument.getString("fullName.lastName") ?: ""
                val username = touristDocument.getString("username") ?: ""

                val profilePicture = getTouristPhoto(touristId) ?: "https://upload.wikimedia.org/wikipedia/commons/8/89/Portrait_Placeholder.png"

                return Tourist(
                    touristId = touristId,
                    firstName = firstName,
                    middleName = middleName,
                    lastName = lastName,
                    username = username,
                    profilePicture = profilePicture,
                ) // Return Tourist object
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            // Handle the error case as needed
        }
        return null
    }

    suspend fun getHostProfile(touristId: String): Host? {
        try {
            val touristDocument = touristCollection.document(touristId).get().await()

            Log.d("Document", "$touristDocument")

            if (touristDocument.exists()) {
                val firstName = touristDocument.getString("fullName.firstName") ?: ""
                val middleName = touristDocument.getString("fullName.middleName") ?: ""
                val lastName = touristDocument.getString("fullName.lastName") ?: ""
                val username = touristDocument.getString("username") ?: ""

                val profilePicture = getTouristPhoto(touristId) ?: "https://upload.wikimedia.org/wikipedia/commons/8/89/Portrait_Placeholder.png"

                return Host(
                    touristId = touristId,
                    firstName = firstName,
                    middleName = middleName,
                    lastName = lastName,
                    username = username,
                    profilePicture = profilePicture,
                    hostId = "HOST-$touristId",
                ) // Return Tourist object
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            // Handle the error case as needed
        }
        return null
    }

    suspend fun getTouristPhoto(touristId: String): String? {
        try {
            val touristProfileQuery = touristProfileCollection
                .whereEqualTo("touristId", touristId)
                .get()
                .await()

            if (!touristProfileQuery.isEmpty) {
                // Assuming there's only one document with the specified touristId
                val touristPhoto = touristProfileQuery.documents[0].getString("touristPhoto")
                return touristPhoto
            }
        } catch (e: Exception) {
            e.printStackTrace()
            // Handle the error case as needed
        }
        return null
    }


    suspend fun cancelStaycationBooking(bookingId: String) {
        try {
            val updatedData = hashMapOf(
                "bookingStatus" to "Cancelled"
            )

            addStaycationBookingToHistory(bookingId,"update")

            // Update the bookingStatus field in the staycation_booking collection
            staycationBookingCollection.document(bookingId).update(updatedData as Map<String, Any>).await()

            addStaycationBookingToHistory(bookingId,"update")

            cancelPaymentData(bookingId)


            Log.d("Update", "Booking $bookingId status updated to Cancelled")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun cancelPaymentData(serviceBookingId: String) {
        try {
            // Fetch the payment document based on serviceBookingId
            val query = paymentCollection
                .whereEqualTo("serviceBookingId", serviceBookingId)

            val result = query.get().await()

            if (!result.isEmpty) {
                val paymentDocument = result.documents.first()

                // Extract current payment data
                val amount = paymentDocument.getDouble("amount") ?: 0.0
                val amountRefunded = (amount * 0.8).coerceAtLeast(0.0)
                val newAmount = (amount * 0.2).coerceAtLeast(0.0)

                val updatedData = hashMapOf(
                    "amount" to newAmount,
                    "amountRefunded" to amountRefunded,
                    "paymentStatus" to "Cancelled",
                    "paymentDate" to FieldValue.serverTimestamp()
                )

                val paymentDocumentId = paymentDocument.id

                addPaymentToHistory(paymentDocumentId, "update")

                // Update the payment data
                paymentDocument.reference.update(updatedData).await()

                addPaymentToHistory(paymentDocumentId, "update")

                // Optionally, you can log a message indicating the successful update

            } else {

            }
        } catch (e: Exception) {
            // Handle the error case as needed
           // println("An error occurred: $e")
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

    private suspend fun addStaycationBookingToHistory(bookingId: String, transaction: String) {
        try {
            // Fetch the payment document
            val staycationBookingDocument = staycationBookingCollection.document(bookingId).get().await()

            // Check if the document exists
            if (staycationBookingDocument.exists()) {
                // Get the data from the document
                val bookingData = staycationBookingDocument.data

                // Add additional fields
                bookingData?.put("staycationBookingId", bookingId)
                bookingData?.put("transaction", transaction) // or "update" or "delete" based on your use case
                bookingData?.put("transactionTimestamp", FieldValue.serverTimestamp())

                // Add the fetched document to the payment_history collection
                staycationBookingHistoryCollection.add(bookingData!!).await()
            } else {
                Log.d("", "No document found with the provided paymentId")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            // Handle the error case as needed
        }
    }

    suspend fun addStaycationBooking(bookingStatus: String, checkInDateMillis: Long, checkOutDateMillis: Long, timeZone: TimeZone, noOfGuests: Int, noOfInfants: Int, noOfPets: Int, staycationId: String, totalAmount: Double, touristId: String, commission: Double, paymentStatus: String, paymentMethod: String): Boolean {
        try {
            val checkInDate = Date(checkInDateMillis)
            val checkOutDate = Date(checkOutDateMillis)

            val bookingData = hashMapOf(
                "bookingDate" to FieldValue.serverTimestamp(),
                "bookingStatus" to bookingStatus,
                "checkInDate" to formatDateInTimeZone(checkInDate, timeZone),
                "checkOutDate" to formatDateInTimeZone(checkOutDate, timeZone),
                "noOfGuests" to noOfGuests,
                "noOfInfants" to noOfInfants,
                "noOfPets" to noOfPets,
                "staycationId" to staycationId,
                "totalAmount" to totalAmount,
                "touristId" to touristId
            )

            val staycationBookingDocRef= staycationBookingCollection.add(bookingData).await()
            val staycationBookingId = staycationBookingDocRef.id

            addStaycationBookingToHistory(
                bookingId = staycationBookingId,
                transaction = "add"
            )

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


    private suspend fun addPaymentData(amount: Double, commission: Double, paymentMethod: String, paymentStatus: String, serviceBookingId: String, serviceType: String, transactionId: String) {

        val amountRefunded = 0

        try {
            val paymentData = hashMapOf(
                "amount" to amount,
                "commission" to commission,
                "paymentDate" to FieldValue.serverTimestamp(),
                "amountRefunded" to amountRefunded,
                "paymentStatus" to paymentStatus,
                "serviceBookingId" to serviceBookingId,
                "serviceType" to serviceType,
                "paymentMethod" to paymentMethod,
                "transactionId" to transactionId
            )

            val paymentCollectionRef = paymentCollection.add(paymentData).await()
            val paymentCollectionId = paymentCollectionRef.id

            addPaymentToHistory(paymentId = paymentCollectionId, transaction = "add")

        } catch (e: Exception) {
            e.printStackTrace()
            // Handle the error case as needed
        }
    }

    private suspend fun addPaymentToHistory(paymentId: String, transaction: String) {
        try {
            // Fetch the payment document
            val paymentDocument = paymentCollection.document(paymentId).get().await()

            // Check if the document exists
            if (paymentDocument.exists()) {
                // Get the data from the document
                val paymentData = paymentDocument.data

                // Add additional fields
                paymentData?.put("paymentId", paymentId)
                paymentData?.put("transaction", transaction) // or "update" or "delete" based on your use case
                paymentData?.put("transactionTimestamp", FieldValue.serverTimestamp())

                // Add the fetched document to the payment_history collection
                paymentHistoryCollection.add(paymentData!!).await()
            } else {
                Log.d("", "No document found with the provided paymentId")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            // Handle the error case as needed
        }
    }

    suspend fun makeAvailabilityInRange(staycationId: String, checkInDate: Long, checkOutDate: Long) {
        try {
            // Calculate the range of dates between checkInDate and checkOutDate
            val dateRange = (checkInDate until checkOutDate step TimeUnit.DAYS.toMillis(1))
                .map { Date(it) }

            // Create availability records for each date in the range
            for (date in dateRange) {
                val availabilityData = hashMapOf(
                    "staycationId" to staycationId,
                    "availableDate" to date,
                    // Add other fields as needed
                )

                // Add the availability record to the collection
                staycationAvailabilityCollection.add(availabilityData).await()
            }
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

        val touristInfo = getHostInfo(hostId)
        val staycationImages = getStaycationImages(staycationId)
        val staycationTags = getStaycationTags(staycationId)
        val promotions = getPromotions(staycationId)
        val availability = getStaycationAvailability(staycationId)
        val amenities = getAmenities(staycationId, "Staycation")
        val bookings = getStaycationBookings(staycationId)
        return Staycation(
            staycationId = staycationId,
//            hostFirstName = touristInfo?.firstName ?: "",
//            hostMiddleName = touristInfo?.middleName ?: "",
//            hostLastName = touristInfo?.lastName ?: "",
            host = Host(
                profilePicture = touristInfo?.profilePicture ?: "",
                firstName = touristInfo?.firstName ?: "",
                middleName = touristInfo?.middleName ?: "",
                lastName = touristInfo?.lastName ?: "",
                touristId = touristInfo?.touristId ?: "",
            ),
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
            //averageReviewRating = bookings.calculateAverageReviewRating()
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

                val touristInfo = getHostInfo(hostId)
                val staycationImages = getStaycationImages(staycationId)
                val staycationTags = getStaycationTags(staycationId)
                val promotions = getPromotions(staycationId)
                val availability = getStaycationAvailability(staycationId)
                val amenities = getAmenities(staycationId, "Staycation")
                val bookings = getStaycationBookings(staycationId)

                val staycation = Staycation(
                    staycationId = staycationId,
                    host = Host(
                        profilePicture = touristInfo?.profilePicture ?: "",
                        firstName = touristInfo?.firstName ?: "",
                        middleName = touristInfo?.middleName ?: "",
                        lastName = touristInfo?.lastName ?: "",
                        touristId = touristInfo?.touristId ?: "",
                    ),
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
                    // averageReviewRating = bookings.calculateAverageReviewRating()
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
                val bookingDate = document.getTimestamp("bookingDate")?.toDate() ?: Date()
                val bookingStatus = document.getString("bookingStatus") ?: ""
                val checkInDate = document.getTimestamp("checkInDate")?.toDate() ?: Date()
                val checkOutDate = document.getTimestamp("checkOutDate")?.toDate() ?: Date()
                val noOfGuests = document.getLong("noOfGuests")?.toInt() ?: 0
                val totalAmount = document.getDouble("totalAmount") ?: 0.0
                val touristId = document.getString("touristId") ?: ""

                // Fetch booking review using bookingId
                val bookingReview = getBookingReview(
                    bookingId = staycationBookingId,
                    reviewerId = touristId
                ) ?: Review()


                // Construct a StaycationBooking object and add it to the list
                val booking = StaycationBooking(
                    staycationBookingId = staycationBookingId,
                    bookingDate = bookingDate,
                    bookingStatus = bookingStatus,
                    checkInDate = checkInDate,
                    checkOutDate = checkOutDate,
                    noOfGuests = noOfGuests,
                    totalAmount = totalAmount,
                    tourist = Tourist(touristId = touristId),
                    // staycationId = staycationId, /*TODO*/ // RECENTLY UPDATEDDDDDDDDD
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


    suspend fun getBookingReview(bookingId: String, reviewerId: String): Review? {
        return try {
            val result = reviewCollection
                .whereEqualTo("bookingId", bookingId)
                .get()
                .await()

            if (result.documents.isNotEmpty()) {
                val document = result.documents[0]
                val reviewId = document.id
                val reviewComment = document.getString("reviewComment") ?: ""
                val reviewDate = document.getTimestamp("reviewDate")?.toDate() ?: Date()
                val reviewRating = document.getLong("reviewRating")?.toInt() ?: 0
                val serviceType = document.getString("serviceType") ?: ""

                val reviewer = getTouristProfile(reviewerId) ?: Tourist()


                // Return the BookingReview object
                Review(
                    reviewId = reviewId,
                    comment = reviewComment,
                    reviewDate = reviewDate,
                    rating = reviewRating,
                    serviceType = serviceType,
                    reviewer = reviewer,
                  //  booking = StaycationBooking(staycationBookingId = bookingId, tourist = reviewer),
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
                val availableDateTimestamp = document.getTimestamp("availableDate") ?: Timestamp.now()
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

    suspend fun getAmenities(staycationId: String, serviceType: String): List<Amenity> {
        return try {
            val result = serviceAmenityCollection
                .whereEqualTo("serviceId", staycationId)
                .whereEqualTo("serviceType", serviceType)
                .get()
                .await()

            val amenities = mutableListOf<Amenity>()

            for (document in result.documents) {
                val amenityId = document.id
                val amenityName = document.getString("amenityName") ?: ""

                // Fetch amenity details from the amenity collection using amenityId
//                val amenityDetails = getAmenityDetails(amenityId)

                val amenity = Amenity(
                    amenityId = amenityId,
                    amenityName = amenityName
                )

                amenities.add(amenity)
            }

            amenities

        } catch (e: Exception) {
            e.printStackTrace()
            emptyList() // Handle the error case as needed
        }
    }

//    private suspend fun getAmenityDetails(amenityId: String): Amenity? {
//        return try {
//            val document = amenityCollection.document(amenityId).get().await()
//
//            if (document.exists()) {
//                val amenityName = document.getString("amenityName") ?: ""
//                // Add other fields as needed
//                Amenity(amenityId, amenityName)
//            } else {
//                null
//            }
//
//        } catch (e: Exception) {
//            e.printStackTrace()
//            null // Handle the error case as needed
//        }
//    }

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

}
