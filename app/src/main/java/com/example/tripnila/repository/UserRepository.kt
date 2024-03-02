package com.example.tripnila.repository

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.tripnila.data.Amenity
import com.example.tripnila.data.BookingInfo
import com.example.tripnila.data.Business
import com.example.tripnila.data.Chat
import com.example.tripnila.data.DailySchedule
import com.example.tripnila.data.HomePagingItem
import com.example.tripnila.data.Host
import com.example.tripnila.data.Inbox
import com.example.tripnila.data.Message
import com.example.tripnila.data.Offer
import com.example.tripnila.data.Photo
import com.example.tripnila.data.Preference
import com.example.tripnila.data.Promotion
import com.example.tripnila.data.Review
import com.example.tripnila.data.Staycation
import com.example.tripnila.data.StaycationAvailability
import com.example.tripnila.data.StaycationBooking
import com.example.tripnila.data.Tag
import com.example.tripnila.data.Tour
import com.example.tripnila.data.TourBooking
import com.example.tripnila.data.TourSchedule
import com.example.tripnila.data.Tourist
import com.example.tripnila.data.TouristWallet
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.security.MessageDigest
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.SortedSet
import java.util.TimeZone
import java.util.UUID
import java.util.concurrent.TimeUnit
import kotlin.experimental.and


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
    private val staycationNearbyAttractionCollection = db.collection("staycation_nearby_attraction")
    private val reviewCollection = db.collection("review")
    private val likeCollection = db.collection("like")
    private val commentCollection = db.collection("comment")
    private val paymentCollection = db.collection("payment")
    private val touristProfileCollection = db.collection("tourist_profile")
    private val reviewPhotoCollection = db.collection("review_photo")
    private val serviceCollection = db.collection("service")
    private val hostWalletCollection = db.collection("host_wallet")
    private val touristWalletCollection = db.collection("tourist_wallet")
    private val businessCollection = db.collection("business")
    private val businessMenuCollection = db.collection("business_menu")
    private val businessAvailabilityCollection = db.collection("business_availability")

    private val tourCollection = db.collection("tour")
    private val tourHistoryCollection = db.collection("tour_history")
    private val tourOffersCollection = db.collection("tour_offers")
    private val tourAvailabilityCollection = db.collection("tour_availability")
    private val tourBookingCollection = db.collection("tour_booking")
    private val tourBookingHistoryCollection = db.collection("tour_booking_history")


    private val paymentHistoryCollection = db.collection("payment_history")
    private val staycationBookingHistoryCollection = db.collection("staycation_booking_history")
    private val staycationHistoryCollection = db.collection("staycation_history")
    private val businessHistoryCollection = db.collection("business_history")
    private val serviceAmenityHistoryCollection = db.collection("service_amenity_history")
    private val servicePhotoHistoryCollection = db.collection("service_photo_history")

    private val chatCollection = db.collection("chat")
    private val messageCollection = db.collection("message")

    private val storageReference = FirebaseStorage.getInstance().reference


    private var currentUser: Tourist? = null
    private var staycationList: List<Staycation>? = null
    private val processedServiceIds = mutableSetOf<String>()



    suspend fun getAllChatsByUserId(
        userId: String,
        currentPageNumber: Int,
        pageSize: Int
    ): List<Inbox> {

        val chats = getChatIdsByUserId(userId, currentPageNumber, pageSize)

        val itemsList = mutableListOf<Inbox>()

        for (chat in chats) {
            val messageQuerySnapshot = messageCollection
                .whereEqualTo("chatId", chat.chatId)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(1)
                .get()
                .await()

            if (!messageQuerySnapshot.isEmpty) {
                val latestMessageDocument = messageQuerySnapshot.documents[0]
                val content = latestMessageDocument.getString("content") ?: ""
                val lastSender = latestMessageDocument.getString("senderId") ?: ""
                val timestamp = latestMessageDocument.getLong("timestamp") ?: 0

                val receiverProfile = getTouristProfile(chat.receiverId)

                val inbox = Inbox(
                    chatId = chat.chatId,
                    image = receiverProfile?.profilePicture ?: "https://upload.wikimedia.org/wikipedia/commons/8/89/Portrait_Placeholder.png",
                    name = "${receiverProfile?.firstName} ${receiverProfile?.lastName}",
                    inboxPreview = content,
                    lastSender = lastSender,
                    timeSent = timestamp,
                    receiverId = chat.receiverId
                )

                itemsList.add(inbox)
            }

        }


        return itemsList
    }

    private suspend fun getChatIdsByUserId(
        userId: String,
        pageNumber: Int,
        pageSize: Int,
    ): List<Chat> {
        return try {

            val initialLoadSize = pageSize * 3
            val startIndex = if (pageNumber == 0) {
                0
            } else {
                ((pageNumber - 1) * pageSize) + initialLoadSize
            }

            val result = mutableListOf<Chat>()

            var query = chatCollection
                .whereArrayContains("participants", userId)
               // .orderBy("serviceId")

            // If it's not the first page, start after the last document of the previous page
            if (pageNumber > 0) {
                val lastDocumentSnapshot = chatCollection
                    .whereArrayContains("participants", userId)
                   // .orderBy("serviceId")
                    .limit(startIndex.toLong())
                    .get()
                    .await()
                    .documents
                    .lastOrNull()

                if (lastDocumentSnapshot != null) {
                    query = query.startAfter(lastDocumentSnapshot)
                }
            }

            val querySnapshot = query
                .limit(pageSize.toLong() + 1) // Fetch one extra to check if there's a next page
                .get()
                .await()

            querySnapshot.documents.forEach { document ->
                val chatId = document.id
                val participants = document.get("participants") as List<String>
                val receiverId = participants.find { it != userId }

                val chat = Chat(
                    chatId = chatId,
                    senderId = userId,
                    receiverId = receiverId ?: ""
                )

                result.add(chat)
            }

            // If there are more documents than the requested page size, remove the extra one
            if (result.size > pageSize) {
                result.removeAt(result.size - 1)
            }

            result
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList() // Handle the error case as needed
        }
    }

    suspend fun addNewChat(userId1: String, userId2: String): String {
        try {
            // Create a new chat document with participants field
            val participants = listOf(userId1, userId2)
            val newChatData = hashMapOf(
                "participants" to participants
            )
            val documentReference = chatCollection.add(newChatData).await()
            return documentReference.id
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }




    suspend fun getChatByUserIds(userId1: String, userId2: String): Chat {
        try {
            val chatsSnapshot = chatCollection.get().await()

            for (document in chatsSnapshot.documents) {
                val chatId = document.id
                val participants = document.get("participants") as List<String>

                // Check if the chat contains both user IDs
                if (participants.contains(userId1) && participants.contains(userId2)) {
                    return Chat(chatId, userId1, userId2)
                }
            }

            return Chat("", userId1, userId2)
        } catch (e: Exception) {
            e.printStackTrace()
            // If an exception occurs, you should handle it appropriately, like throwing it or returning a default value
            throw e
        }
    }



//    suspend fun getChatByUserIds(userId1: String, userId2: String): Chat? {
//        try {
//            val chatsSnapshot = chatCollection.get().await()
//
//            // Check if chatsSnapshot is empty
//            if (chatsSnapshot.isEmpty) {
//                // If chatsSnapshot is empty, add a new document to the collection
//                val chatId = UUID.randomUUID().toString() // Generate a unique chatId
//                val participants = listOf(userId1, userId2)
//
//
//
//                // Create a new document with the provided user IDs
//
//                chatCollection.document(chatId).set(
//
//                    mapOf(
//
//                        "chatId" to chatId,
//
//                        "participants" to participants
//
//                    )
//
//                ).await()
//
//
//
//                // Return a new Chat object with the generated chatId and user IDs
//
//                return Chat(chatId, userId1, userId2)
//
//            }
//
//
//
//            for (document in chatsSnapshot.documents) {
//
//                val chatId = document.id
//
//                val participants = document.get("participants") as List<String>
//
//
//
//                // Check if the chat contains both user IDs
//
//                if (participants.contains(userId1) && participants.contains(userId2)) {
//
//                    return Chat(chatId, userId1, userId2)
//
//                }
//
//            }
//
//        } catch (e: Exception) {
//
//            e.printStackTrace()
//
//        }
//
//        return null
//
//    }

//    suspend fun getChats(): List<Chat> {
//        try {
//            val chatsSnapshot = chatCollection.get().await()
//
//            val chatList = mutableListOf<Chat>()
//
//            for (document in chatsSnapshot.documents) {
//                val chatId = document.id
//                val participants = document.get("participants") as List<String>
//                chatList.add(Chat(chatId, participants))
//            }
//
//            return chatList
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//        return emptyList()
//    }



    suspend fun getMessages(chatId: String): List<Message> {
        try {
            val messagesSnapshot = messageCollection
                .whereEqualTo("chatId", chatId)
                .get()
                .await()

            val messageList = mutableListOf<Message>()

            for (document in messagesSnapshot.documents) {
                val messageId = document.id
                val senderId = document.getString("senderId") ?: ""
                val content = document.getString("content") ?: ""
                val timestamp = document.getLong("timestamp") ?: 0
                val imageUrls = document.get("imageUrls") as? List<String> ?: emptyList() // Fetch image URLs if available
                val images = imageUrls.map { Photo(photoUrl = it) }
                messageList.add(
                    Message(
                        messageId = messageId,
                        chatId = chatId,
                        senderId = senderId,
                        images = images,
                        content = content,
                        timestamp = timestamp
                    )
                )
            }

            return messageList
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return emptyList()
    }

    suspend fun sendMessage(chatId: String, senderId: String, content: String, images: List<Photo>): Message? {
        try {

            val imageUrls = uploadImages(images)

            val newImage = imageUrls.map { url ->
                Photo(
                    photoUrl = url,
                )
            }

            val newMessage = hashMapOf(
                "chatId" to chatId,
                "senderId" to senderId,
                "content" to content,
                "imageUrls" to imageUrls,
                "timestamp" to System.currentTimeMillis()
            )

            val documentReference = messageCollection.add(newMessage).await()

            val addedMessageSnapshot = documentReference.get().await()

            val messageId = documentReference.id
            val timestamp = addedMessageSnapshot.getLong("timestamp") ?: 0

            return Message(
                messageId = messageId,
                chatId = chatId,
                senderId = senderId,
                content = content,
                images = newImage,
                timestamp = timestamp
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    private suspend fun uploadImages(images: List<Photo>): List<String> {
        val imageUrls = mutableListOf<String>()
        for (photo in images) {
            photo.photoUri?.let { uri ->

                val fileName = UUID.randomUUID().toString()
                val imageRef = storageReference.child("images/messages/$fileName")

                imageRef.putFile(uri).await()

                // Get download URL of uploaded image
                val imageUrl = imageRef.downloadUrl.await().toString()
                imageUrls.add(imageUrl)
            }
        }
        return imageUrls
    }

//    fun getHotels(): Flow<PagingData<Staycation>> {
//        val query = FirebaseFirestore.getInstance().collection("staycation")
//        return Pager(PagingConfig(pageSize = 20)) {
//            FirestorePagingSource(query)
//        }.flow
//    }


    suspend fun getStaycationsByHostId(host: Host): List<Staycation> {
        val staycations = mutableListOf<Staycation>()

        try {

            val hostId = host.hostId

            val querySnapshot = staycationCollection
                .whereEqualTo("hostId", hostId)
                .get()
                .await()

            for (document in querySnapshot.documents) {
                val staycationId = document.id
                val hasDangerousAnimal = document.getBoolean("hasDangerousAnimal") ?: false
                val hasSecurityCamera = document.getBoolean("hasSecurityCamera") ?: false
                val hasWeapon = document.getBoolean("hasWeapon") ?: false
                val hostId = document.getString("hostId") ?: ""
                val noOfBathrooms = document.getLong("noOfBathrooms")?.toInt() ?: 0
                val noOfBedrooms = document.getLong("noOfBedrooms")?.toInt() ?: 0
                val noOfBeds = document.getLong("noOfBeds")?.toInt() ?: 0
                val noOfGuests = document.getLong("noOfGuests")?.toInt() ?: 0
                val staycationDescription = document.getString("staycationDescription") ?: ""
                val staycationLocation = document.getString("staycationLocation") ?: ""
                val staycationLat = document.getDouble("staycationLat") ?: 0.0
                val staycationLng = document.getDouble("staycationLng") ?: 0.0
                val staycationPrice = document.getDouble("staycationPrice") ?: 0.0
                val staycationSpace = document.getString("staycationSpace") ?: ""
                val staycationTitle = document.getString("staycationTitle") ?: ""
                val staycationType = document.getString("staycationType") ?: ""

                val hasFirstAid = document.getBoolean("hasFirstAid") ?: false
                val hasFireExit = document.getBoolean("hasFireExit") ?: false
                val hasFireExtinguisher = document.getBoolean("hasFireExtinguisher") ?: false
                val maxNoOfGuests = document.getLong("maxNoOfGuests")?.toInt() ?: 0
                val additionalFeePerGuest = document.getDouble("additionalFeePerGuest") ?: 0.0
                val noisePolicy = document.getBoolean("noisePolicy") ?: false
                val allowSmoking = document.getBoolean("allowSmoking") ?: false
                val allowPets = document.getBoolean("allowPets") ?: false
                val additionalInfo = document.getString("additionalInfo") ?: ""
                val noCancel = document.getBoolean("noCancel") ?: false
                val noReschedule = document.getBoolean("noCancel") ?: false
                val phoneNo = document.getString("phoneNo") ?: ""
                val email = document.getString("email") ?: ""

                val staycationImages = getServiceImages(staycationId, "Staycation")
                val staycationBookings = getStaycationBookings(staycationId)


            //    val hostInfo = getHostInfo(hostId)

                val staycation = Staycation(
                    staycationId = staycationId,
                    hasDangerousAnimal = hasDangerousAnimal,
                    hasSecurityCamera = hasSecurityCamera,
                    hasWeapon = hasWeapon,
                    noOfBathrooms = noOfBathrooms,
                    noOfBedrooms = noOfBedrooms,
                    noOfBeds = noOfBeds,
                    noOfGuests = noOfGuests,
                    staycationDescription = staycationDescription,
                    staycationLocation = staycationLocation,
                    staycationLat = staycationLat,
                    staycationLng = staycationLng,
                    staycationPrice = staycationPrice,
                    staycationSpace = staycationSpace,
                    staycationTitle = staycationTitle,
                    staycationType = staycationType,
                    hasFirstAid = hasFirstAid,
                    hasFireExit = hasFireExit,
                    hasFireExtinguisher = hasFireExtinguisher,
                    maxNoOfGuests = maxNoOfGuests,
                    additionalFeePerGuest = additionalFeePerGuest,
                    additionalInfo = additionalInfo,
                    noisePolicy = noisePolicy,
                    allowPets = allowPets,
                    allowSmoking = allowSmoking,
                    noReschedule = noReschedule,
                    noCancel = noCancel,
                    phoneNo = phoneNo,
                    email = email,
                    staycationImages = staycationImages,
                    staycationBookings = staycationBookings,
                    host = Host(
                        profilePicture = host?.profilePicture ?: "",
                        firstName = host?.firstName ?: "",
                        middleName = host?.middleName ?: "",
                        lastName = host?.lastName ?: "",
                        hostId = host?.hostId ?: "",
                        username = host?.username ?: "",
                    )
                )
                staycations.add(staycation)


            }
        } catch (e: Exception) {
            // Handle the exception, e.g., log or throw
            e.printStackTrace()
        }
        return staycations
    }

    suspend fun createHostWallet(hostId: String){
        val currentBalance = 0.0
        val pendingBalance = 0.0
        val paypalBalance = 10000.0
        val paymayaBalance = 10000.0
        val gcashBalance = 10000.0
        val hostWallet = hashMapOf(
            "hostId" to hostId,
            "currentBalance" to currentBalance,
            "pendingBalance" to pendingBalance,
            "paypalBalance" to paypalBalance,
            "paymayaBalance" to paymayaBalance,
            "gcashBalance" to gcashBalance
        )

        hostWalletCollection.add(hostWallet).await()
    }
    suspend fun createTouristWallet(touristId: String){
        val currentBalance = 0.0
        val pendingBalance = 0.0
        val paypalBalance = 10000.0
        val paymayaBalance = 10000.0
        val gcashBalance = 10000.0
        val touristWallet = hashMapOf(
            "touristId" to touristId,
            "currentBalance" to currentBalance,
            "pendingBalance" to pendingBalance,
            "paypalBalance" to paypalBalance,
            "paymayaBalance" to paymayaBalance,
            "gcashBalance" to gcashBalance
        )
        touristWalletCollection.add(touristWallet).await()
    }
    suspend fun getTouristWallet(touristId: String): TouristWallet{

        try {
            val querySnapshot = touristWalletCollection
                .whereEqualTo("touristId", touristId)
                .get()
                .await()

            for (document in querySnapshot.documents) {
                val currentBalance = document.getDouble("currentBalance") ?: 0.0
                val pendingBalance = document.getDouble("pendingBalance") ?: 0.0
                val paypalBalance = document.getDouble("paypalBalance") ?: 0.0
                val paymayaBalance = document.getDouble("paymayaBalance") ?: 0.0
                val gcashBalance = document.getDouble("gcashBalance") ?: 0.0

                return TouristWallet(
                    touristId = touristId,
                    currentBalance = currentBalance,
                    paypalBalance = paypalBalance,
                    paymayaBalance = paymayaBalance,
                    gcashBalance = gcashBalance,
                    pendingBalance = pendingBalance
                )
            }


        } catch (e: Exception) {
            e.printStackTrace()
            // Handle the error case as needed
        }

        return TouristWallet()
    }

    suspend fun getAdminWallet(): TouristWallet{

        try {
            val querySnapshot = touristWalletCollection
                .whereEqualTo("touristId", "admin")
                .get()
                .await()

            for (document in querySnapshot.documents) {
                val currentBalance = document.getDouble("currentBalance") ?: 0.0
                val pendingBalance = document.getDouble("pendingBalance") ?: 0.0
                val paypalBalance = document.getDouble("paypalBalance") ?: 0.0
                val paymayaBalance = document.getDouble("paymayaBalance") ?: 0.0
                val gcashBalance = document.getDouble("gcashBalance") ?: 0.0

                return TouristWallet(
                    touristId = "admin",
                    currentBalance = currentBalance,
                    paypalBalance = paypalBalance,
                    paymayaBalance = paymayaBalance,
                    gcashBalance = gcashBalance,
                    pendingBalance = pendingBalance
                )
            }


        } catch (e: Exception) {
            e.printStackTrace()
            // Handle the error case as needed
        }

        return TouristWallet()
    }

    suspend fun addBalance(touristId: String, amount:Double, paypalBalance: Double, paymayaBalance: Double, gcashBalance: Double, pendingBalance: Double){
        try{
        touristWalletCollection
            .whereEqualTo("touristId", touristId)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    touristWalletCollection.document(document.id).delete()
                }
            }
            .await()

            val tagData = hashMapOf(
                "touristId" to touristId,
                "currentBalance" to amount,
                "paypalBalance" to paypalBalance,
                "gcashBalance" to gcashBalance,
                "paymayaBalance" to paymayaBalance,
                "pendingBalance" to pendingBalance
            )

            touristWalletCollection.add(tagData).await()


    } catch (e: Exception) {
        e.printStackTrace()
    }

    }
    suspend fun getStaycationBookingsByStaycationId(staycationId: String) : List<StaycationBooking> {

        val bookings = mutableListOf<StaycationBooking>()

        try {
            val querySnapshot = staycationBookingCollection
                .whereEqualTo("staycationId", staycationId)
                .get()
                .await()

            for (document in querySnapshot.documents) {
                val bookingId = document.id
                val bookingDate = (document["bookingDate"] as? Timestamp)?.toDate() ?: Date()
                val bookingStatus = document.getString("bookingStatus") ?: ""
                val checkInDate = (document["checkInDate"] as? Timestamp)?.toDate() ?: Date()
                val checkOutDate = (document["checkOutDate"] as? Timestamp)?.toDate() ?: Date()
                val noOfGuests = document.getLong("noOfGuests")?.toInt() ?: 0
                val noOfInfants = document.getLong("noOfInfants")?.toInt() ?: 0
                val noOfPets = document.getLong("noOfPets")?.toInt() ?: 0
                val totalAmount = document.getDouble("totalAmount") ?: 0.0
                val touristId = document.getString("touristId") ?: ""

                val guest = getTouristProfile(touristId)

                val staycationBooking = StaycationBooking(
                    staycationBookingId = bookingId,
                    bookingDate = bookingDate,
                    bookingStatus = bookingStatus,
                    checkInDate = checkInDate,
                    checkOutDate = checkOutDate,
                    noOfGuests = noOfGuests,
                    noOfInfants = noOfInfants,
                    noOfPets = noOfPets,
                    totalAmount = totalAmount,
                    tourist = Tourist(
                        touristId = touristId,
                        firstName = guest?.firstName ?: "",
                        middleName = guest?.middleName ?: "",
                        lastName = guest?.lastName ?: "",
                        profilePicture = guest?.profilePicture ?: "",
                    )
                )

                bookings.add(staycationBooking)
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return bookings

    }

    suspend fun getBusinessById(businessId: String): Business {
        try {
            val businessDocument = businessCollection.document(businessId).get().await()

            if (businessDocument.exists()) {
                val hostId = businessDocument.getString("hostId") ?: ""
                val businessContact = businessDocument.getString("businessContact") ?: ""
                val businessDescription = businessDocument.getString("businessDescription") ?: ""
                val businessEmail = businessDocument.getString("businessEmail") ?: ""
                val businessLat = businessDocument.getDouble("businessLat") ?: 0.0
                val businessLng = businessDocument.getDouble("businessLng") ?: 0.0
                val businessLocation = businessDocument.getString("businessLocation") ?: ""
                val businessTitle = businessDocument.getString("businessTitle") ?: ""
                val businessType = businessDocument.getString("businessType") ?: ""
                val businessURL = businessDocument.getString("businessURL") ?: ""
                val additionalInfo = businessDocument.getString("additionalInfo") ?: ""
                val minSpend = businessDocument.getDouble("minSpend") ?: 0.0
                val entranceFee = businessDocument.getDouble("entranceFee") ?: 0.0
                val businessImages = getServiceImages(businessId, "Business")
                val businessMenuImages = getBusinessMenu(businessId)
                val hostInfo = getHostInfo(hostId)
                val businessTags = getServiceTags(businessId, "Business")
                val amenities = getAmenities(businessId, "Business")
                val schedule = getBusinessAvailability(businessId)

                return Business(
                    businessId = businessId,
                    businessContact = businessContact,
                    businessDescription = businessDescription,
                    businessEmail = businessEmail,
                    businessLat = businessLat,
                    businessLng = businessLng,
                    businessLocation = businessLocation,
                    businessTitle = businessTitle,
                    businessType = businessType,
                    businessURL = businessURL,
                    minSpend = minSpend,
                    entranceFee = entranceFee,
                    additionalInfo = additionalInfo,
                    host = Host(
                        profilePicture = hostInfo?.profilePicture ?: "",
                        firstName = hostInfo?.firstName ?: "",
                        middleName = hostInfo?.middleName ?: "",
                        lastName = hostInfo?.lastName ?: "",
                        hostId = hostId,
                    ),
                    businessTags = businessTags,
                    businessImages = businessImages,
                    businessMenu = businessMenuImages,
                    amenities = amenities,
                    schedule = schedule,

                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return Business()
    }

    suspend fun getTourById(tourId: String): Tour {

        try {

            val tourDocument = tourCollection.document(tourId).get().await()

            if (tourDocument.exists()) {
                val tourId = tourDocument.id
                val hostId = tourDocument.getString("hostId") ?: ""
                val tourContact = tourDocument.getString("tourContact") ?: ""
                val tourDescription = tourDocument.getString("tourDescription") ?: ""
                val tourDuration = tourDocument.getString("tourDuration") ?: ""
                val tourEmail = tourDocument.getString("tourEmail") ?: ""
                val tourFacebook = tourDocument.getString("tourFacebook") ?: ""
                val tourInstagram = tourDocument.getString("tourInstagram") ?: ""
                val tourLanguage = tourDocument.getString("tourLanguage") ?: ""
                val tourLat = tourDocument.getDouble("tourLat") ?: 0.0
                val tourLng = tourDocument.getDouble("tourLng") ?: 0.0
                val tourLocation = tourDocument.getString("tourLocation") ?: ""
                val tourPrice = tourDocument.getDouble("tourPrice") ?: 0.0
                val tourTitle = tourDocument.getString("tourTitle") ?: ""
                val tourType = tourDocument.getString("tourType") ?: ""

                val tourImages = getServiceImages(tourId, "Tour")
                val hostInfo = getHostInfo(hostId)
                val tourTags = getServiceTags(tourId, "Tour")
                val schedule = getTourAvailabilities(tourId)
                val offers = getTourOffers(tourId)

                Log.d("Repository", "found")

                return Tour(
                    tourId = tourId,
                    tourContact = tourContact,
                    tourDescription = tourDescription,
                    tourDuration = tourDuration,
                    tourEmail = tourEmail,
                    tourFacebook = tourFacebook,
                    tourInstagram = tourInstagram,
                    tourLanguage = tourLanguage,
                    tourLat = tourLat,
                    tourLng = tourLng,
                    tourLocation = tourLocation,
                    tourPrice = tourPrice,
                    tourTitle = tourTitle,
                    tourType = tourType,
                    tourImages = tourImages,
                    tourTags = tourTags,
                    schedule = schedule,
                    offers = offers,
                    host = Host(
                        profilePicture = hostInfo?.profilePicture ?: "",
                        firstName = hostInfo?.firstName ?: "",
                        middleName = hostInfo?.middleName ?: "",
                        lastName = hostInfo?.lastName ?: "",
                        hostId = hostId,
                        touristId = hostInfo?.touristId ?: "",
                    )
                )
            }
        } catch (e: Exception) {
            // Handle the exception, e.g., log or throw
            e.printStackTrace()
        }
        return Tour()
    }

    private suspend fun getTourOffers(tourId: String): List<Offer> {
        return try {
            val result = tourOffersCollection
                .whereEqualTo("tourId", tourId)
                .get()
                .await()

            val offers = mutableListOf<Offer>()

            for (document in result.documents) {
                val offerId = document.id
                val offer = document.getString("offer") ?: ""
                val typeOfOffer = document.getString("typeOfOffer") ?: ""

                // Fetch amenity details from the amenity collection using amenityId
//                val amenityDetails = getAmenityDetails(amenityId)

                val offerInstance = Offer(
                    tourId = tourId,
                    tourOfferId = offerId,
                    offer = offer,
                    typeOfOffer = typeOfOffer
                )

                offers.add(offerInstance)
            }

            offers

        } catch (e: Exception) {
            e.printStackTrace()
            emptyList() // Handle the error case as needed
        }
    }


    suspend fun getToursByHostId(host: Host): List<Tour> {
        val tours = mutableListOf<Tour>()

        try {

            val hostId = host.hostId

            val querySnapshot = tourCollection
                .whereEqualTo("hostId", hostId)
                .get()
                .await()

            for (document in querySnapshot.documents) {
                val tourId = document.id
                val tourContact = document.getString("tourContact") ?: ""
                val tourDescription = document.getString("tourDescription") ?: ""
                val tourDuration = document.getString("tourDuration") ?: ""
                val tourEmail = document.getString("tourEmail") ?: ""
                val tourFacebook = document.getString("tourFacebook") ?: ""
                val tourInstagram = document.getString("tourInstagram") ?: ""
                val tourLanguage = document.getString("tourLanguage") ?: ""
                val tourLat = document.getDouble("tourLat") ?: 0.0
                val tourLng = document.getDouble("tourLng") ?: 0.0
                val tourLocation = document.getString("tourLocation") ?: ""
                val tourPrice = document.getDouble("tourPrice") ?: 0.0
                val tourTitle = document.getString("tourTitle") ?: ""
                val tourType = document.getString("tourType") ?: ""

                val tourImages = getServiceImages(tourId, "Tour")

                val tour = Tour(
                    tourId = tourId,
                    tourContact = tourContact,
                    tourDescription = tourDescription,
                    tourDuration = tourDuration,
                    tourEmail = tourEmail,
                    tourFacebook = tourFacebook,
                    tourInstagram = tourInstagram,
                    tourLanguage = tourLanguage,
                    tourLat = tourLat,
                    tourLng = tourLng,
                    tourLocation = tourLocation,
                    tourPrice = tourPrice,
                    tourTitle = tourTitle,
                    tourType = tourType,
                    tourImages = tourImages,
                    host = Host(
                        profilePicture = host?.profilePicture ?: "",
                        firstName = host?.firstName ?: "",
                        middleName = host?.middleName ?: "",
                        lastName = host?.lastName ?: "",
                        hostId = host?.hostId ?: "",
                    )
                )

                tours.add(tour)
            }
        } catch (e: Exception) {
            // Handle the exception, e.g., log or throw
            e.printStackTrace()
        }
        return tours
    }

    suspend fun getBusinessesByHostId(host: Host): List<Business> {
        val businesses = mutableListOf<Business>()

        try {
            val hostId = host.hostId

            val querySnapshot = businessCollection
                .whereEqualTo("hostId", hostId)
                .get()
                .await()

            for (document in querySnapshot.documents) {
                val businessId = document.id
                val additionalInfo = document.getString("additionalInfo") ?: ""
                val businessContact = document.getString("businessContact") ?: ""
                val businessDescription = document.getString("businessDescription") ?: ""
                val businessEmail = document.getString("businessEmail") ?: ""
                val businessLat = document.getDouble("businessLat") ?: 0.0
                val businessLng = document.getDouble("businessLng") ?: 0.0
                val minSpend = document.getDouble("minSpend") ?: 0.0
                val entranceFee = document.getDouble("entranceFee") ?: 0.0
                val businessLocation = document.getString("businessLocation") ?: ""
                val businessTitle = document.getString("businessTitle") ?: ""
                val businessType = document.getString("businessType") ?: ""
                val businessURL = document.getString("businessURL") ?: ""

                val businessImages = getServiceImages(businessId, "Business")

                val business = Business(
                    businessId = businessId,
                    additionalInfo = additionalInfo,
                    minSpend = minSpend,
                    entranceFee = entranceFee,
                    businessContact = businessContact,
                    businessDescription = businessDescription,
                    businessEmail = businessEmail,
                    businessLat = businessLat,
                    businessLng = businessLng,
                    businessLocation = businessLocation,
                    businessTitle = businessTitle,
                    businessType = businessType,
                    businessURL = businessURL,
                    businessImages = businessImages,
                    host = Host(
                        profilePicture = host?.profilePicture ?: "",
                        firstName = host?.firstName ?: "",
                        middleName = host?.middleName ?: "",
                        lastName = host?.lastName ?: "",
                        hostId = host?.hostId ?: "",
                    )
                )

                businesses.add(business)
            }
        } catch (e: Exception) {
            // Handle the exception, e.g., log or throw
            e.printStackTrace()
        }

        return businesses
    }

    suspend fun getAllTours(): List<Tour> {
        try {


            val result = tourCollection.get().await()

            val tours = mutableListOf<Tour>()

            for (document in result.documents) {
                val tourId = document.id
                val hostId = document.getString("hostId") ?: ""
                val tourContact = document.getString("tourContact") ?: ""
                val tourDescription = document.getString("tourDescription") ?: ""
                val tourDuration = document.getString("tourDuration") ?: ""
                val tourEmail = document.getString("tourEmail") ?: ""
                val tourFacebook = document.getString("tourFacebook") ?: ""
                val tourInstagram = document.getString("tourInstagram") ?: ""
                val tourLanguage = document.getString("tourLanguage") ?: ""
                val tourLat = document.getDouble("tourLat") ?: 0.0
                val tourLng = document.getDouble("tourLng") ?: 0.0
                val tourLocation = document.getString("tourLocation") ?: ""
                val tourPrice = document.getDouble("tourPrice") ?: 0.0
                val tourTitle = document.getString("tourTitle") ?: ""
                val tourType = document.getString("tourType") ?: ""

                val host = getHostInfo(hostId)
                val tourImages = getServiceImages(tourId, "Tour")

                val tour = Tour(
                    tourId = tourId,
                    host = Host(
                        firstName = host?.firstName ?: "" ,
                        middleName = host?.middleName ?: "",
                        lastName = host?.lastName ?: "",
                        username = host?.username ?: "",
                        profilePicture = host?.profilePicture ?: "",
                        hostId = hostId
                    ),
                    tourContact = tourContact,
                    tourDescription = tourDescription,
                    tourDuration = tourDuration,
                    tourEmail = tourEmail,
                    tourFacebook = tourFacebook,
                    tourInstagram = tourInstagram,
                    tourLanguage = tourLanguage,
                    tourLat = tourLat,
                    tourLng = tourLng,
                    tourLocation = tourLocation,
                    tourPrice = tourPrice,
                    tourTitle = tourTitle,
                    tourType = tourType,
                    tourImages = tourImages
                    // Add other fields as needed
                )

                tours.add(tour)
            }

            return tours
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return emptyList()
    }

    suspend fun getAllBusinesses(): List<Business> {
        try {

            val result = businessCollection.get().await()

            val businesses = mutableListOf<Business>()

            for (document in result.documents) {
                val businessId = document.id
                val additionalInfo = document.getString("additionalInfo") ?: ""
                val businessContact = document.getString("businessContact") ?: ""
                val businessDescription = document.getString("businessDescription") ?: ""
                val businessEmail = document.getString("businessEmail") ?: ""
                val businessLat = document.getDouble("businessLat") ?: 0.0
                val businessLng = document.getDouble("businessLng") ?: 0.0
                val minSpend = document.getDouble("minSpend") ?: 0.0
                val entranceFee = document.getDouble("entranceFee") ?: 0.0
                val businessLocation = document.getString("businessLocation") ?: ""
                val businessTitle = document.getString("businessTitle") ?: ""
                val businessType = document.getString("businessType") ?: ""
                val businessURL = document.getString("businessURL") ?: ""
                val hostId = document.getString("hostId") ?: ""

                val host = getHostInfo(hostId)
                val businessImages = getServiceImages(businessId, "Business")
                val businessMenu = getBusinessMenu(businessId)


                val business = Business(
                    businessId = businessId,
                    additionalInfo = additionalInfo,
                    minSpend = minSpend,
                    entranceFee = entranceFee,
                    businessContact = businessContact,
                    businessDescription = businessDescription,
                    businessEmail = businessEmail,
                    businessLat = businessLat,
                    businessLng = businessLng,
                    businessLocation = businessLocation,
                    businessTitle = businessTitle,
                    businessType = businessType,
                    businessURL = businessURL,
                    host = Host(
                        firstName = host?.firstName ?: "" ,
                        middleName = host?.middleName ?: "",
                        lastName = host?.lastName ?: "",
                        username = host?.username ?: "",
                        profilePicture = host?.profilePicture ?: "",
                        hostId = hostId
                    ),
                    businessImages = businessImages,
                    businessMenu = businessMenu
                )

                businesses.add(business)
            }

            return businesses
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return emptyList()
    }





    suspend fun addTour(
        tourId: String = "",
        hostId: String = "",
        tourType: String = "",
        tourTitle: String = "",
        tourDescription: String = "",
        tourDuration: String = "",
        tourLanguage: String = "",
        tourContact: String = "",
        tourEmail: String = "",
        tourFacebook: String = "",
        tourInstagram: String = "",
        tourLat: Double = 0.0,
        tourLng: Double = 0.0,
        tourLocation: String = "",
        offers: List<Offer> = emptyList(),
        tourImages: List<Photo> = emptyList(),
        schedule: List<TourSchedule> = emptyList(),
        tourPrice: Double = 0.0,
        // promotions: List<Promotion> = emptyList(),
        //  nearbyAttractions: List<String> = emptyList(), ?????????   /*TODO*/
        tourTags: List<String> = emptyList(),
        context: Context
    ): Boolean {
        try {

            val newId = if (tourId == "") {
                val querySnapshot = tourHistoryCollection
                    .orderBy("tourId", Query.Direction.DESCENDING)
                    .limit(1)
                    .get()
                    .await()

                val lastDocument = querySnapshot.documents.firstOrNull()
                val lastId = lastDocument?.getString("tourId")?.toIntOrNull() ?: 0
                lastId + 1
            } else {
                tourId.toIntOrNull() ?: 0
            }

            val tour = hashMapOf(
                "hostId" to hostId,
                "tourTitle" to tourTitle,
                "tourDescription" to tourDescription,
                "tourType" to tourType,
                "tourLat" to tourLat,
                "tourLng" to tourLng,
                "tourLocation" to tourLocation,
                "tourDuration" to tourDuration,
                "tourLanguage" to tourLanguage,
                "tourContact" to tourContact,
                "tourEmail" to tourEmail,
                "tourFacebook" to tourFacebook,
                "tourInstagram" to tourInstagram,
                "tourPrice" to tourPrice,
            )

            val transaction= if (tourId == "") {
                "add"
            } else {
                "update"
            }

            tourCollection.document(newId.toString()).set(tour).await()

            addDocumentToService(newId.toString(), "Tour")

            addTourToHistory(newId.toString(), transaction)

            addTourOffers(newId.toString(), offers)

            addServicePhotos(context, newId.toString(), "Tour", tourImages)

            addServiceTags(newId.toString(), "Tour", tourTags)

            addTourAvailability(newId.toString(), schedule)

            //   addStaycationPromotions(newId.toString(), promotions)

            //   addStaycationNearbyAttractions(newId.toString(), nearbyAttractions) /*TODO*/



            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    private suspend fun getTourAvailabilities(tourId: String): List<TourSchedule> {
        val tourAvailabilities = mutableListOf<TourSchedule>()

        try {
            val querySnapshot = tourAvailabilityCollection
                .whereEqualTo("tourId", tourId)
                .get()
                .await()

            for (document in querySnapshot.documents) {
                val date = document.getString("date")
                val startTime = document.getString("startTime")
                val endTime = document.getString("endTime")
                val slot = document.getLong("slot")?.toInt() ?: 0
                val bookedSlot = document.getLong("bookedSlot")?.toInt() ?: 0

                val tourSchedule = TourSchedule(
                    tourScheduleId = document.id,
                    date = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                    startTime = startTime ?: "00:00 AM",
                    endTime = endTime ?: "00:00 PM",
                    slot = slot,
                    bookedSlot = bookedSlot
                )

                tourAvailabilities.add(tourSchedule)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            // Handle the exception as needed
        }

        return tourAvailabilities
    }


    private suspend fun addTourAvailability(tourId: String, schedules: List<TourSchedule>) {
        try {

            tourAvailabilityCollection
                .whereEqualTo("tourId", tourId)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        tourAvailabilityCollection.document(document.id).delete()
                    }
                }
                .await()

            schedules.forEach { schedule ->
                val scheduleData = hashMapOf(
                    "tourId" to tourId,
                    "date" to schedule.date.toString(),
                    "startTime" to schedule.startTime,
                    "endTime" to schedule.endTime,
                    "slot" to schedule.slot,
                    "bookedSlot" to schedule.bookedSlot,
                )

                tourAvailabilityCollection.add(scheduleData).await()

            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private suspend fun addTourOffers(tourId: String, offers: List<Offer>) {
        try {

            tourOffersCollection
                .whereEqualTo("tourId", tourId)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        tourOffersCollection.document(document.id).delete()
                    }
                }
                .await()

            offers.forEach { offer ->
                val data = hashMapOf(
                    "tourId" to tourId,
                    "typeOfOffer" to offer.typeOfOffer,
                    "offer" to offer.offer
                )

                // Use add() to automatically generate a unique document ID
                tourOffersCollection.add(data).await()

            }
        } catch (e: Exception) {
            e.printStackTrace()
            // Handle the error case as needed
        }
    }

    suspend fun addTourToHistory(tourId: String, transaction: String) {
        try {
            val tourDocument = tourCollection.document(tourId).get().await()

            // Check if the document exists
            if (tourDocument.exists()) {
                // Get the data from the document
                val tourData = tourDocument.data

                // Add additional fields
                tourData?.put("tourId", tourId)
                tourData?.put("transaction", transaction) // or "update" or "delete" based on your use case
                tourData?.put("transactionTimestamp", FieldValue.serverTimestamp())

                // Add the fetched document to the payment_history collection
                tourHistoryCollection.add(tourData!!).await()
            } else {
                Log.d("", "No document found with the provided paymentId")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            // Handle the error case as needed
        }
    }



    suspend fun addBusiness(
        businessId: String = "",
        hostId: String = "",
        businessDescription: String = "",
        businessLat: Double = 0.0,
        businessLng: Double = 0.0,
        businessLocation: String = "",
        businessTitle: String = "",
        businessType: String = "",
        businessContact: String = "",
        businessEmail: String = "",
        businessURL: String = "",
        minSpend: Double = 0.0,
        entranceFee: Double = 0.0,
        additionalInfo: String = "",
        amenities: List<Amenity> = emptyList(),
        businessImages: List<Photo> = emptyList(),
        businessMenusPhotos: List<Photo> = emptyList(),
        schedule: List<DailySchedule> = emptyList(),
       // promotions: List<Promotion> = emptyList(),
      //  nearbyAttractions: List<String> = emptyList(), ?????????   /*TODO*/
        businessTags: List<String> = emptyList(),
        context: Context
    ): Boolean {
        try {

            val newId = if (businessId == "") {
                val querySnapshot = businessHistoryCollection
                    .orderBy("businessId", Query.Direction.DESCENDING)
                    .limit(1)
                    .get()
                    .await()

                val lastDocument = querySnapshot.documents.firstOrNull()
                val lastId = lastDocument?.getString("businessId")?.toIntOrNull() ?: 0
                lastId + 1
            } else {
                businessId.toIntOrNull() ?: 0
            }

//            val querySnapshot = businessHistoryCollection
//                .orderBy("businessId", Query.Direction.DESCENDING)
//                .limit(1)
//                .get()
//                .await()
//
//            val lastDocument = querySnapshot.documents.firstOrNull()
//            val lastId = lastDocument?.getString("businessId")
//            val newId = (lastId?.toInt() ?: 0) + 1

            val business = hashMapOf(
                "hostId" to hostId,
                "additionalInfo" to additionalInfo,
                "minSpend" to minSpend,
                "entranceFee" to entranceFee,
                "businessDescription" to businessDescription,
                "businessLat" to businessLat,
                "businessLng" to businessLng,
                "businessLocation" to businessLocation,
                "businessEmail" to businessEmail,
                "businessContact" to businessContact,
                "businessURL" to businessURL,
                "businessTitle" to businessTitle,
                "businessType" to businessType
            )

            val transaction= if (businessId == "") {
                "add"
            } else {
                "update"
            }

            businessCollection.document(newId.toString()).set(business).await()

            addDocumentToService(newId.toString(), "Business")

            addBusinessToHistory(newId.toString(), transaction)

            addServiceAmenities(newId.toString(), "Business", amenities)

            addServicePhotos(context, newId.toString(), "Business", businessImages)

            addServiceTags(newId.toString(), "Business", businessTags)

            addBusinessMenu(context, newId.toString(), businessMenusPhotos)

            addBusinessAvailability(newId.toString(), schedule)

         //   addStaycationPromotions(newId.toString(), promotions)

         //   addStaycationNearbyAttractions(newId.toString(), nearbyAttractions) /*TODO*/



            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    private suspend fun getBusinessAvailability(businessId: String): List<DailySchedule> {

        val availabilityList = mutableListOf<DailySchedule>()

        try {
            val querySnapshot = businessAvailabilityCollection
                .whereEqualTo("businessId", businessId)
                .get()
                .await()

            for (document in querySnapshot) {
                val closingTime = document.getString("closingTime") ?: ""
                val day = document.getString("day") ?: ""
                val openingTime = document.getString("openingTime") ?: ""

                val businessAvailability = DailySchedule(
                    closingTime = closingTime,
                    day = day,
                    openingTime = openingTime
                )

                availabilityList.add(businessAvailability)
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return availabilityList
    }

    private suspend fun addBusinessAvailability(businessId: String, schedules: List<DailySchedule>) {
        try {

            businessAvailabilityCollection
                .whereEqualTo("businessId", businessId)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        businessAvailabilityCollection.document(document.id).delete()
                    }
                }
                .await()

            schedules.forEach { schedule ->
                val scheduleData = hashMapOf(
                    "businessId" to businessId,
                    "day" to schedule.day,
                    "openingTime" to schedule.openingTime,
                    "closingTime" to schedule.closingTime,
                )

                businessAvailabilityCollection.add(scheduleData).await()

            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    suspend fun addBusinessToHistory(businessId: String, transaction: String) {
        try {
            val businessDocument = businessCollection.document(businessId).get().await()

            // Check if the document exists
            if (businessDocument.exists()) {
                // Get the data from the document
                val businessData = businessDocument.data

                // Add additional fields
                businessData?.put("businessId", businessId)
                businessData?.put("transaction", transaction) // or "update" or "delete" based on your use case
                businessData?.put("transactionTimestamp", FieldValue.serverTimestamp())

                // Add the fetched document to the payment_history collection
                businessHistoryCollection.add(businessData!!).await()
            } else {
                Log.d("", "No document found with the provided paymentId")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            // Handle the error case as needed
        }
    }

    private suspend fun addBusinessMenu(context: Context, businessId: String, photos: List<Photo>) {
        try {

            businessMenuCollection
                .whereEqualTo("businessId", businessId)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        businessMenuCollection.document(document.id).delete()
                    }
                }
                .await()

            for (photo in photos) {
                if (photo.photoUri != null) {
                    try {
                        val uri = photo.photoUri

                        if (uri.toString().startsWith("https://firebasestorage.googleapis.com")) {
                            // Download the file from Firebase Storage
                            val fileName = UUID.randomUUID().toString()
                            val imageRef = FirebaseStorage.getInstance().getReferenceFromUrl(uri.toString())
                            val file = File(context.cacheDir, fileName)
                            imageRef.getFile(file).await()

                            // Continue with the upload
                            val newImageRef = storageReference.child("images/service_photos/business_menus/$fileName")
                            newImageRef.putFile(Uri.fromFile(file)).await()

                            val photoUrl = newImageRef.downloadUrl.await().toString()
                            val photoType = photo.photoType ?: "Others"

                            val photoData = hashMapOf(
                                "businessId" to businessId,
                                "photoUrl" to photoUrl,
                                "photoType" to photoType
                            )

                            businessMenuCollection.add(photoData).await()
                        } else {
                            // Use the provided file URI for uploading
                            val fileName = UUID.randomUUID().toString()
                            val imageRef = storageReference.child("images/service_photos/business_menus/$fileName")

                            imageRef.putFile(uri).await()

                            val photoUrl = imageRef.downloadUrl.await().toString()
                            val photoType = photo.photoType ?: "Others"

                            val photoData = hashMapOf(
                                "businessId" to businessId,
                                "photoUrl" to photoUrl,
                                "photoType" to photoType
                            )

                            businessMenuCollection.add(photoData).await()
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Log.e("Upload", "Error processing URI: $e")
                    }
                }
            }

//            photos.mapNotNull { it.photoUri }.forEach { uri ->
//                val fileName = UUID.randomUUID().toString()
//                val imageRef = storageReference.child("images/service_photos/business_menus/$fileName")
//                imageRef.putFile(uri).await()
//
//                val photoUrl = imageRef.downloadUrl.await().toString()
//
//                val photoType = photos.firstOrNull { it.photoUri == uri }?.photoType ?: "Others"
//
//                val photoData = hashMapOf(
//                    "businessId" to businessId,
//                    "photoUrl" to photoUrl,
//                    "photoType" to photoType
//                )
//
//                businessMenuCollection.add(photoData).await()
//            }

        } catch (e: Exception) {
            e.printStackTrace()
            // Handle the error case as needed
        }
    }

    private suspend fun addDocumentToService(serviceId: String, serviceType: String) {
        try {

            serviceCollection
                .whereEqualTo("serviceId", serviceId)
                .whereEqualTo("serviceType", serviceType)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        serviceCollection.document(document.id).delete()
                    }
                }
                .await()

            val serviceData = hashMapOf(
                "serviceId" to serviceId,
                "serviceType" to serviceType
            )

            serviceCollection.add(serviceData).await()

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }



    suspend fun addStaycation(
        staycationId: String = "",
        hasDangerousAnimal: Boolean = false,
        hasSecurityCamera: Boolean = false,
        hasWeapon: Boolean = false,
        hasFireExit: Boolean = false,
        hasFireExtinguisher: Boolean = false,
        hasFirstAid: Boolean = false,
        allowPets: Boolean = false,
        allowSmoking: Boolean = false,
        noReschedule: Boolean = false,
        noCancel: Boolean = false,
        additionalInfo: String = "",
        hostId: String = "",
        noOfBathrooms: Int = 1,
        noOfBedrooms: Int = 1,
        noOfBeds: Int = 1,
        phoneNo: String = "",
        email: String = "",
        noOfGuests: Int = 1,
        maxNoOfGuests: Int = 0,
        additionalFeePerGuest: Double = 0.0,
        noisePolicy: Boolean = false,
        staycationDescription: String = "",
        staycationLocation: String = "",
        staycationLat: Double = 0.0,
        staycationLng: Double = 0.0,
        staycationPrice: Double = 0.0,
        staycationSpace: String = "",
        staycationTitle: String = "",
        staycationType: String = "",
       // amenities: List<String> = emptyList(),
        amenities: List<Amenity> = emptyList(),
        photos: List<Photo> = emptyList(),
        availableDates: List<StaycationAvailability> = emptyList(),
        promotions: List<Promotion> = emptyList(),
        nearbyAttractions: List<String> = emptyList(),
        staycationTags: List<String> = emptyList(),
        context: Context
    ): Boolean {
        try {

            val newId = if (staycationId == "") {
                val querySnapshot = staycationHistoryCollection
                    .orderBy("staycationId", Query.Direction.DESCENDING)
                    .limit(1)
                    .get()
                    .await()

                val lastDocument = querySnapshot.documents.firstOrNull()
                val lastId = lastDocument?.getString("staycationId")?.toIntOrNull() ?: 0
                lastId + 1
            } else {
                staycationId.toIntOrNull() ?: 0
            }

            val staycationData: HashMap<String, Any> = hashMapOf(
                "hasDangerousAnimal" to hasDangerousAnimal,
                "hasSecurityCamera" to hasSecurityCamera,
                "hasWeapon" to hasWeapon,
                "hasFireExit" to hasFireExit,
                "hasFireExtinguisher" to hasFireExtinguisher,
                "hasFirstAid" to hasFirstAid,
                "allowPets" to allowPets,
                "allowSmoking" to allowSmoking,
                "maxNoOfGuests" to maxNoOfGuests,
                "additionalFeePerGuest" to additionalFeePerGuest,
                "noisePolicy" to noisePolicy,
                "hostId" to hostId,
                "noReschedule" to noReschedule,
                "noCancel" to noCancel,
                "additionalInfo" to additionalInfo,
                "noOfBathrooms" to noOfBathrooms,
                "noOfBedrooms" to noOfBedrooms,
                "noOfBeds" to noOfBeds,
                "noOfGuests" to noOfGuests,
                "phoneNo" to phoneNo,
                "email" to email,
                "staycationDescription" to staycationDescription,
                "staycationLocation" to staycationLocation,
                "staycationLat" to staycationLat,
                "staycationLng" to staycationLng,
                "staycationPrice" to staycationPrice,
                "staycationSpace" to staycationSpace,
                "staycationTitle" to staycationTitle,
                "staycationType" to staycationType
            )

            val transaction= if (staycationId == "") {
                "add"
            } else {
                "update"
            }


            staycationCollection.document(newId.toString()).set(staycationData).await()


            addDocumentToService(newId.toString(), "Staycation")

            addStaycationToHistory(newId.toString(), staycationData, transaction)



            addServiceAmenities(newId.toString(), "Staycation", amenities)

            addServicePhotos(context, newId.toString(), "Staycation", photos)  // TRY

            addStaycationAvailability(newId.toString(), availableDates) // NOT SURE

            addStaycationPromotions(newId.toString(), "Staycation", promotions) // NOT SURE


            addStaycationNearbyAttractions(newId.toString(), nearbyAttractions) // NOT SURE

            addServiceTags(newId.toString(), "Staycation", staycationTags) // NOT SURE

            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    private suspend fun addServiceTags(serviceId: String, serviceType: String, tags: List<String>) {
        try {

            serviceTagCollection
                .whereEqualTo("serviceId", serviceId)
                .whereEqualTo("serviceType", serviceType)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        serviceTagCollection.document(document.id).delete()
                    }
                }
                .await()

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

            staycationNearbyAttractionCollection
                .whereEqualTo("staycationId", staycationId)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        staycationNearbyAttractionCollection.document(document.id).delete()
                    }
                }
                .await()

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

    private suspend fun addStaycationPromotions(serviceId: String, serviceType: String, promotions: List<Promotion>) {
        try {

            servicePromotionCollection
                .whereEqualTo("serviceId", serviceId)
                .whereEqualTo("serviceType", serviceType)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        servicePromotionCollection.document(document.id).delete()
                    }
                }
                .await()


            if (promotions.isNotEmpty()) {
                promotions.forEach { promotion ->
                    val promotionData = hashMapOf(
                        "serviceId" to serviceId,
                        "promoId" to promotion.promoId,
                        "serviceType" to serviceType
                    )

                    servicePromotionCollection.add(promotionData).await()
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun addTourAvailabilityFromManager(tourId: String, schedule: TourSchedule) {
        try {

            val scheduleData = hashMapOf(
                "tourId" to tourId,
                "date" to schedule.date.toString(),
                "startTime" to schedule.startTime,
                "endTime" to schedule.endTime,
                "slot" to schedule.slot,
                "bookedSlot" to schedule.bookedSlot,
            )

            tourAvailabilityCollection.add(scheduleData).await()



        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun deleteTourAvailabilityFromManager(tourId: String, schedule: TourSchedule) {
        try {

            val matchingSchedules = tourAvailabilityCollection
                .whereEqualTo("tourId", tourId)
                .whereEqualTo("date", schedule.date.toString())
                .whereEqualTo("startTime", schedule.startTime)
                .whereEqualTo("endTime", schedule.endTime)
                .whereEqualTo("slot", schedule.slot)
                .get()
                .await()

            for (document in matchingSchedules.documents) {
                tourAvailabilityCollection.document(document.id).delete().await()
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    suspend fun addStaycationAvailabilityFromManager(staycationId: String, availableDate: StaycationAvailability) {
        try {

            val availabilityData = hashMapOf(
                "staycationId" to staycationId,
                "availableDate" to availableDate.availableDate
            )

            staycationAvailabilityCollection.add(availabilityData).await()

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun removeStaycationAvailabilityFromManager(staycationId: String, localDate: LocalDate) {
        try {
            val availableDates = staycationAvailabilityCollection
                .whereEqualTo("staycationId", staycationId)
                .whereEqualTo("availableDate", Timestamp(Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant())))
                .get()
                .await()

            for (document in availableDates.documents) {
                staycationAvailabilityCollection.document(document.id).delete().await()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            // Handle the exception as needed
        }
    }





    private suspend fun addStaycationAvailability(staycationId: String, availableDates: List<StaycationAvailability>) {
        try {

            staycationAvailabilityCollection
                .whereEqualTo("staycationId", staycationId)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        staycationAvailabilityCollection.document(document.id).delete()
                    }
                }
                .await()

            availableDates.forEach { date ->
                val availabilityData = hashMapOf(
                    "staycationId" to staycationId,
                    "availableDate" to date.availableDate
                )

                staycationAvailabilityCollection.add(availabilityData).await()
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

//    suspend fun addServicePhotos(context: Context, serviceId: String, serviceType: String, photos: List<Photo>) {
//        try {
//            servicePhotoCollection
//                .whereEqualTo("serviceId", serviceId)
//                .whereEqualTo("serviceType", serviceType)
//                .get()
//                .addOnSuccessListener { documents ->
//                    for (document in documents) {
//                        servicePhotoCollection.document(document.id).delete()
//                    }
//                }
//                .await()
//
//            for (photo in photos) {
//                if (photo.photoUri != null) {
//                    try {
//                        val uri = photo.photoUri
//                        Log.d("Upload", "URI: $uri")
//
//                        val fileName = UUID.randomUUID().toString()
//                        val imageRef = storageReference.child("images/service_photos/${serviceType.lowercase(Locale.getDefault())}s/$fileName")
//
//                        // Check if the URI is a content URI
//                        if (ContentResolver.SCHEME_CONTENT == uri.scheme) {
//                            // Content URI, resolve it to a file URI
//                            val file = createFileFromContentUri(context, uri)
//                            if (file != null) {
//                                // Use the file URI for uploading
//                                imageRef.putFile(file.toUri()).await()
//                            }
//                        } else {
//                            // Use the provided file URI for uploading
//                            imageRef.putFile(uri).await()
//                        }
//
//                        val photoUrl = imageRef.downloadUrl.await().toString()
//                        val photoType = photo.photoType ?: "Others"
//
//                        val photoData = hashMapOf(
//                            "serviceId" to serviceId,
//                            "serviceType" to serviceType,
//                            "photoUrl" to photoUrl,
//                            "photoType" to photoType
//                        )
//
//                        servicePhotoCollection.add(photoData).await()
//
//                    } catch (e: Exception) {
//                        e.printStackTrace()
//                        Log.e("Upload", "Error processing URI: $e")
//                    }
//                }
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//            Log.e("Upload", "Error processing URI: $e")
//        }
//    }


    private suspend fun addServicePhotos(context: Context, serviceId: String, serviceType: String, photos: List<Photo>) {
        try {
            servicePhotoCollection
                .whereEqualTo("serviceId", serviceId)
                .whereEqualTo("serviceType", serviceType)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        servicePhotoCollection.document(document.id).delete()
                    }
                }
                .await()

            for (photo in photos) {
                if (photo.photoUri != null) {
                    try {
                        val uri = photo.photoUri
                        Log.d("Upload", "URI: $uri")

                        Log.d("ContentResolver", ContentResolver.SCHEME_CONTENT)
                        Log.d("UriScheme", uri.scheme ?: "")

                        if (uri.toString().startsWith("https://firebasestorage.googleapis.com")) {
                            // Download the file from Firebase Storage
                            val fileName = UUID.randomUUID().toString()
                            val imageRef = FirebaseStorage.getInstance().getReferenceFromUrl(uri.toString())
                            val file = File(context.cacheDir, fileName)
                            imageRef.getFile(file).await()

                            // Continue with the upload
                            val newImageRef = storageReference.child("images/service_photos/${serviceType.lowercase(Locale.getDefault())}s/$fileName")
                            newImageRef.putFile(Uri.fromFile(file)).await()

                            val photoUrl = newImageRef.downloadUrl.await().toString()
                            val photoType = photo.photoType ?: "Others"

                            val photoData = hashMapOf(
                                "serviceId" to serviceId,
                                "serviceType" to serviceType,
                                "photoUrl" to photoUrl,
                                "photoType" to photoType
                            )

                            servicePhotoCollection.add(photoData).await()
                        } else {
                            // Use the provided file URI for uploading
                            val fileName = UUID.randomUUID().toString()
                            val imageRef = storageReference.child("images/service_photos/${serviceType.lowercase(Locale.getDefault())}s/$fileName")

                            imageRef.putFile(uri).await()

                            val photoUrl = imageRef.downloadUrl.await().toString()
                            val photoType = photo.photoType ?: "Others"

                            val photoData = hashMapOf(
                                "serviceId" to serviceId,
                                "serviceType" to serviceType,
                                "photoUrl" to photoUrl,
                                "photoType" to photoType
                            )

                            servicePhotoCollection.add(photoData).await()
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Log.e("Upload", "Error processing URI: $e")
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("Upload", "Error processing URI: $e")
        }
    }


    private fun createFileFromContentUri(context: Context, uri: Uri): File? {
        try {
            val contentResolver = context.contentResolver
            val cursor = contentResolver.query(uri, null, null, null, null)
            return cursor?.use {
                val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                it.moveToFirst()
                val fileName = it.getString(nameIndex)
                val outputDir = context.cacheDir
                val outputFile = File(outputDir, fileName)
                try {
                    contentResolver.openInputStream(uri)?.use { inputStream ->
                        FileOutputStream(outputFile).use { outputStream ->
                            inputStream.copyTo(outputStream)
                        }
                    }
                    outputFile
                } catch (e: IOException) {
                    e.printStackTrace()
                    null
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }



//    suspend fun addServicePhotos(context: Context, serviceId: String, serviceType: String, photos: List<Photo>) {
//        try {
//            servicePhotoCollection
//                .whereEqualTo("serviceId", serviceId)
//                .whereEqualTo("serviceType", serviceType)
//                .get()
//                .addOnSuccessListener { documents ->
//                    for (document in documents) {
//                        servicePhotoCollection.document(document.id).delete()
//                    }
//                }
//                .await()
//
//            for (photo in photos) {
//                if (photo.photoUri != null) {
//                    try {
//                        val uri = photo.photoUri
//                        Log.d("Upload", "URI: $uri")
//
//                        val fileName = UUID.randomUUID().toString()
//                        val imageRef = storageReference.child("images/service_photos/${serviceType.lowercase(Locale.getDefault())}s/$fileName")
//
//                        // Check if the URI is a content URI
//                        if (ContentResolver.SCHEME_CONTENT == uri.scheme) {
//                            // Content URI, resolve it to a file URI
//                            val file = createFileFromContentUri(context, uri)
//                            if (file != null) {
//                                // Use the file URI for uploading
//                                imageRef.putFile(file.toUri()).await()
//                            }
//                        } else {
//                            // Use the provided file URI for uploading
//                            imageRef.putFile(uri).await()
//                        }
//
//                        val photoUrl = imageRef.downloadUrl.await().toString()
//                        val photoType = photo.photoType ?: "Others"
//
//                        val photoData = hashMapOf(
//                            "serviceId" to serviceId,
//                            "serviceType" to serviceType,
//                            "photoUrl" to photoUrl,
//                            "photoType" to photoType
//                        )
//
//                        servicePhotoCollection.add(photoData).await()
//
//                    } catch (e: Exception) {
//                        e.printStackTrace()
//                        Log.e("Upload", "Error processing URI: $e")
//                    }
//                }
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//            Log.e("Upload", "Error processing URI: $e")
//        }
//    }
//




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

    private suspend fun addServiceAmenities(serviceId: String, serviceType: String, amenities: List<Amenity>) {
        try {

            serviceAmenityCollection
                .whereEqualTo("serviceId", serviceId)
                .whereEqualTo("serviceType", serviceType)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        serviceAmenityCollection.document(document.id).delete()
                    }
                }
                .await()

            amenities.forEach { amenity ->
                val data: HashMap<String, Any> = hashMapOf(
                    "serviceId" to serviceId,
                    "amenityName" to amenity.amenityName,
                    "serviceType" to serviceType
                )

                 serviceAmenityCollection.add(data).await()

              //  addServiceAmenityToHistory(documentId, data, transaction)

            }
        } catch (e: Exception) {
            e.printStackTrace()
            // Handle the error case as needed
        }
    }

    private suspend fun addServiceAmenityToHistory(amenityId: String, amenityData: HashMap<String, Any>, transaction: String) {
        try {

            amenityData["amenityId"] = amenityId
            amenityData["transaction"] = transaction
            amenityData["timestamp"] = FieldValue.serverTimestamp()

            // Add to service_amenity_history collection
            serviceAmenityHistoryCollection.add(amenityData).await()


        } catch (e: Exception) {
            e.printStackTrace()
            // Handle the error case as needed
        }
    }


    private suspend fun addStaycationToHistory(staycationId: String, staycationData: HashMap<String, Any>, transaction: String) {
        try {

            staycationData["staycationId"] = staycationId
            staycationData["transaction"] = transaction // or "update" or "delete" based on your use case
            staycationData["transactionTimestamp"] = FieldValue.serverTimestamp()

            // Add the fetched document to the payment_history collection
            staycationHistoryCollection.add(staycationData).await()

        } catch (e: Exception) {
            e.printStackTrace()
            // Handle the error case as needed
        }
    }

    suspend fun addReview(
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

    suspend fun getHostedStaycation(hostId : String): List<Staycation>{
        try {
            val query = staycationCollection
                .whereEqualTo("hostId", hostId)
            val result = query.get().await()
            val staycations = mutableListOf<Staycation>()

            for (document in result.documents) {
                val staycation = getStaycationDetailsById(document.id) ?: Staycation()
                staycations.add(staycation)

            }
            return staycations

        }
        catch (e: Exception) {
            e.printStackTrace()
        }
        return emptyList()
    }

    suspend fun getHostedTours(hostId : String): List<Tour>{
        try {
            val query = tourCollection
                .whereEqualTo("hostId", hostId)
            val result = query.get().await()
            val tours = mutableListOf<Tour>()

            for (document in result.documents) {
                val tour = getTourById(document.id)
                tours.add(tour)

            }
            return tours

        }
        catch (e: Exception) {
            e.printStackTrace()
        }
        return emptyList()
    }

    suspend fun getCompletedStaycationBookingForStaycation(staycationId: String): List<StaycationBooking>{
        try{
            val query = staycationBookingCollection
                .whereEqualTo("staycationId", staycationId)

            val result = query.get().await()

            val staycationBookings = mutableListOf<StaycationBooking>()

            for (document in result.documents) {

                val bookingStatus = document.getString("bookingStatus") ?: ""

                if (bookingStatus == "Completed") {

                    val bookingId = document.id

                    val bookingDate = document.getDate("bookingDate") ?: Date()
                    val checkInDate = document.getDate("checkInDate") ?: Date()
                    val checkOutDate = document.getDate("checkOutDate") ?: Date()
                    val noOfGuests = document.getLong("noOfGuests")?.toInt() ?: 0
                    val noOfInfants = document.getLong("noOfInfants")?.toInt() ?: 0
                    val noOfPets = document.getLong("noOfPets")?.toInt() ?: 0
                    val totalAmount = document.getLong("totalAmount")?.toDouble() ?: 0.0
                    val staycation = getStaycationDetailsById(staycationId) ?: Staycation()

                    val staycationBooking = StaycationBooking(
                        staycationBookingId = bookingId,
                        bookingDate = bookingDate,
                        bookingStatus = bookingStatus,
                        checkInDate = checkInDate,
                        checkOutDate = checkOutDate,
                        noOfGuests = noOfGuests,
                        noOfInfants = noOfInfants,
                        noOfPets = noOfPets,
                        staycation = staycation,
                        totalAmount = totalAmount,
                    )

                    staycationBookings.add(staycationBooking)
                }


            }
            return staycationBookings
        }catch (e: Exception) {
            e.printStackTrace()
        }
        return emptyList()
    }
    suspend fun getCancelledStaycationBookingForStaycation(staycationId: String): List<StaycationBooking>{
        try{
            val query = staycationBookingCollection
                .whereEqualTo("staycationId", staycationId)

            val result = query.get().await()

            val staycationBookings = mutableListOf<StaycationBooking>()

            for (document in result.documents) {

                val bookingStatus = document.getString("bookingStatus") ?: ""

                if (bookingStatus == "Cancelled") {

                    val bookingId = document.id

                    val bookingDate = document.getDate("bookingDate") ?: Date()
                    val checkInDate = document.getDate("checkInDate") ?: Date()
                    val checkOutDate = document.getDate("checkOutDate") ?: Date()
                    val noOfGuests = document.getLong("noOfGuests")?.toInt() ?: 0
                    val noOfInfants = document.getLong("noOfInfants")?.toInt() ?: 0
                    val noOfPets = document.getLong("noOfPets")?.toInt() ?: 0
                    val totalAmount = document.getLong("totalAmount")?.toDouble() ?: 0.0
                    val staycation = getStaycationDetailsById(staycationId) ?: Staycation()

                    val staycationBooking = StaycationBooking(
                        staycationBookingId = bookingId,
                        bookingDate = bookingDate,
                        bookingStatus = bookingStatus,
                        checkInDate = checkInDate,
                        checkOutDate = checkOutDate,
                        noOfGuests = noOfGuests,
                        noOfInfants = noOfInfants,
                        noOfPets = noOfPets,
                        staycation = staycation,
                        totalAmount = totalAmount,
                    )

                    staycationBookings.add(staycationBooking)
                }


            }
            return staycationBookings
        }catch (e: Exception) {
            e.printStackTrace()
        }
        return emptyList()
    }


    suspend fun getStaycationBookingForItinerary(touristId: String): List<StaycationBooking> {
        try {
            val query = staycationBookingCollection
                .whereEqualTo("touristId", touristId)
                .orderBy("bookingStatus")
                .orderBy("bookingDate")


            val result = query.get().await()

            val staycationBookings = mutableListOf<StaycationBooking>()

            for (document in result.documents) {

                val bookingStatus = document.getString("bookingStatus") ?: ""

                if (bookingStatus !in listOf("Cancelled", "Completed")) {

                    val bookingId = document.id
                    val bookingDate = document.getDate("bookingDate") ?: Date()
                    val checkInDate = document.getDate("checkInDate") ?: Date()
                    val checkOutDate = document.getDate("checkOutDate") ?: Date()
                    val noOfGuests = document.getLong("noOfGuests")?.toInt() ?: 0
                    val noOfInfants = document.getLong("noOfInfants")?.toInt() ?: 0
                    val noOfPets = document.getLong("noOfPets")?.toInt() ?: 0
                    val staycationId = document.getString("staycationId") ?: ""
                    val totalAmount = document.getLong("totalAmount")?.toDouble() ?: 0.0

                    val staycation = getStaycationDetailsById(staycationId) ?: Staycation()

                    val staycationBooking = StaycationBooking(
                        staycationBookingId = bookingId,
                        bookingDate = bookingDate,
                        bookingStatus = bookingStatus,
                        checkInDate = checkInDate,
                        checkOutDate = checkOutDate,
                        noOfGuests = noOfGuests,
                        noOfInfants = noOfInfants,
                        noOfPets = noOfPets,
                        staycation = staycation,
                        totalAmount = totalAmount,
                        tourist = Tourist(touristId = touristId),
                    )

                    staycationBookings.add(staycationBooking)
                }


            }

            return staycationBookings
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return emptyList()
    }



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

    suspend fun updateBookingStatusToOngoingIfCheckInDatePassed(touristId: String) {
        try {
            val currentTimestamp = Timestamp.now()

            val staycationQuery = staycationBookingCollection
                .whereLessThanOrEqualTo("checkInDate", currentTimestamp)
                .whereEqualTo("bookingStatus", "Pending")

            val staycationResult = staycationQuery.get().await()

            for (document in staycationResult.documents) {
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

            val tourQuery = tourBookingCollection
                .whereEqualTo("touristId", touristId)
                .whereEqualTo("bookingStatus", "Pending")

            val tourResult = tourQuery.get().await()

            for (document in tourResult.documents) {
                val bookingId = document.id
                val startTimeString  = document.getString("startTime") ?: ""
                val tourDateString  = document.getString("tourDate") ?: ""

                val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                val tourDate = LocalDate.parse(tourDateString, dateFormatter)

                val timeFormatter = DateTimeFormatter.ofPattern("hh:mm a")
                val startTime = LocalTime.parse(startTimeString, timeFormatter)

                val startDateTime = LocalDateTime.of(tourDate, startTime)

                // Convert to UTC+8 timezone (Asia/Manila)
                val manilaZoneId = ZoneId.of("Asia/Manila")
                val zonedDateTime = ZonedDateTime.of(startDateTime, manilaZoneId)

                // Convert to timestamp
                val timestamp = Timestamp(Date.from(zonedDateTime.toInstant()))

                if (timestamp <= currentTimestamp) {
                    tourBookingCollection.document(bookingId)
                        .update("bookingStatus", "Ongoing")
                        .addOnSuccessListener {
                            // Update successful
                            println("Tour booking $bookingId status updated to Ongoing")
                        }
                        .addOnFailureListener { e ->
                            // Handle the error
                            println("Error updating tour booking status: $e")
                        }
                }
            }


        } catch (e: Exception) {
            Log.e("BookingStatusUpdate", "An error occurred: $e")
        }
    }


//    suspend fun updateBookingStatusToOngoingIfCheckInDatePassed() {
//        try {
//            val currentTimestamp = Timestamp.now()
//
//            val query = staycationBookingCollection
//                .whereLessThanOrEqualTo("checkInDate", currentTimestamp)
//                .whereEqualTo("bookingStatus", "Pending")
//
//            val result = query.get().await()
//
//            Log.d("BookingStatusUpdate", "Found ${result.size()} bookings to check")
//
//            for (document in result.documents) {
//                val bookingId = document.id
//
//                staycationBookingCollection.document(bookingId)
//                    .update("bookingStatus", "Ongoing")
//                    .addOnSuccessListener {
//                        // Update successful
//                        Log.d("BookingStatusUpdate", "Booking $bookingId status updated to Ongoing")
//                    }
//                    .addOnFailureListener { e ->
//                        // Handle the error
//                        Log.d("BookingStatusUpdate", "Error updating booking status: $e")
//                    }
//            }
//        } catch (e: Exception) {
//            Log.e("BookingStatusUpdate", "An error occurred: $e")
//        }
//    }


   /* suspend fun updateBookingStatusToFinishedIfExpired() {
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
    }*/

    suspend fun updateBookingStatusToFinishedIfExpiredForTours(): Map<String, BookingInfo> {
        try {
            val currentTimestamp = Timestamp.now()

            val query = tourBookingCollection
                .whereEqualTo("bookingStatus", "Ongoing")

            val result = query.get().await()

            val tourDataMap = mutableMapOf<String, BookingInfo>() // Map to store tourId, totalAmount, and commission

            for (document in result.documents) {
                val endTimeString = document.getString("endTime") ?: ""
                val tourDateString = document.getString("tourDate") ?: ""
                val totalAmount = document.getDouble("totalAmount") ?: 0.0
                val commission = document.getDouble("commission") ?: 0.0
                val tourId = document.getString("tourId")

                val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                val tourDate = LocalDate.parse(tourDateString, dateFormatter)

                val timeFormatter = DateTimeFormatter.ofPattern("hh:mm a")
                val endTime = LocalTime.parse(endTimeString, timeFormatter)

                val endDateTime = LocalDateTime.of(tourDate, endTime)

                // Convert to UTC+8 timezone (Asia/Manila)
                val manilaZoneId = ZoneId.of("Asia/Manila")
                val zonedDateTime = ZonedDateTime.of(endDateTime, manilaZoneId)

                // Convert to timestamp
                val timestamp = Timestamp(Date.from(zonedDateTime.toInstant()))

                if (timestamp <= currentTimestamp && tourId != null) {
                    Log.d("BookingStatusUpdate", "BookingId: ${document.id}, Check OutDate: $zonedDateTime")
                    Log.d("TOUR", "TOUR")
                    val bookingId = document.id
                    val updateTask = tourBookingCollection.document(bookingId)
                        .update("bookingStatus", "Completed")
                        .addOnSuccessListener {
                            // Update successful
                            Log.d(
                                "UserRepository",
                                "Booking $bookingId status updated to Finished"
                            )

                            Log.d(
                                "UserRepository",
                                "tourId ID: $tourId, Total Amount: $totalAmount, Commission: $commission"
                            )
                            Log.d("UserRepository", "staycationDataMap updated: $tourDataMap")
                        }
                        .addOnFailureListener { e ->
                            // Handle the error
                            Log.d(
                                "UserRepository",
                                "Error updating booking status for $bookingId: $e"
                            )
                        }

                    tourDataMap[tourId.toString()] = BookingInfo(totalAmount, commission)
                }
            }
            Log.d("tourDataMap", "tourDataMap: $tourDataMap")
            return tourDataMap
        } catch (e: Exception) {
            Log.e("BookingStatusUpdate", "An error occurred: $e")
        }
        return emptyMap()
    }


    suspend fun updateBookingStatusToFinishedIfExpired(): Map<String, BookingInfo> {
        try {
            val currentTimestamp = Timestamp.now()
            val query = staycationBookingCollection
                .whereLessThan("checkOutDate", currentTimestamp)
                .whereEqualTo("bookingStatus", "Ongoing")
            val result = query.get().await()

            val bookingInfoMap = mutableMapOf<String, BookingInfo>()

            for (document in result.documents) {
                val staycationId = document.getString("staycationId")
                val totalAmount = document.getDouble("totalAmount")
                val commission = document.getDouble("commission")

                if (staycationId != null && totalAmount != null && commission != null) {
                    val bookingInfo = BookingInfo(totalAmount, commission)
                    bookingInfoMap[staycationId] = bookingInfo

                    // Update booking status to "Completed"
                    staycationBookingCollection.document(document.id)
                        .update("bookingStatus", "Completed")
                        .addOnSuccessListener {
                            Log.d("UserRepository", "Booking ${document.id} status updated to Finished")
                        }
                        .addOnFailureListener { e ->
                            Log.e("UserRepository", "Error updating booking status for ${document.id}: $e")
                        }
                }
            }

            Log.d("BookingStatusUpdate", "Updated booking info: $bookingInfoMap")
            return bookingInfoMap
        } catch (e: Exception) {
            Log.e("BookingStatusUpdate", "An error occurred: $e")
        }
        return emptyMap()
    }

    suspend fun processCompletedBookings(staycationDataMap: Map<String, BookingInfo>): Map<String, Pair<String, BookingInfo>> {
        val staycationDetailsMap = mutableMapOf<String, Pair<String, BookingInfo>>()

        for ((staycationId, bookingInfo) in staycationDataMap) {
            try {
                val staycationDocument = staycationCollection.document(staycationId).get().await()

                if (staycationDocument.exists()) {
                    val hostId = staycationDocument.getString("hostId")
                    if (hostId != null) {
                        // Add the staycationId, hostId, and booking info to the map
                        staycationDetailsMap[staycationId] = Pair(hostId, bookingInfo)
                    } else {
                        // If hostId is null, log an error
                        Log.e("GetStaycationDetails", "HostId is null for staycationId $staycationId")
                    }
                } else {
                    // No document found for the given staycationId
                    Log.e("GetStaycationDetails", "No document found for staycationId $staycationId")
                }
            } catch (e: Exception) {
                // Handle exceptions
                Log.e("GetStaycationDetails", "Error getting details for staycationId $staycationId: $e")
            }
        }

        return staycationDetailsMap
    }

    suspend fun processCompletedTourBookings(tourDataMap: Map<String, BookingInfo>): Map<String, Pair<String, BookingInfo>> {
        val tourDetailsMap = mutableMapOf<String, Pair<String, BookingInfo>>()

        for ((tourId, bookingInfo) in tourDataMap) {
            try {
                val tourDocument = tourCollection.document(tourId).get().await()


                if (tourDocument.exists()) {
                    // Retrieve the hostId from the document

                    val hostId = tourDocument.getString("hostId")
                    if (hostId != null) {
                        // Add the staycationId, hostId, and totalAmount to the map
                        tourDetailsMap[tourId] = Pair(hostId, bookingInfo)
                    } else {
                        // If hostId is null, log an error
                        Log.e("processCompletedTourBookings", "HostId is null for staycationId $tourId")
                    }
                } else {
                    // No document found for the given staycationId
                    Log.e("processCompletedTourBookings", "No document found for staycationId $tourId")
                }
            } catch (e: Exception) {
                // Handle exceptions
                Log.e("processCompletedTourBookings", "Error getting details for staycationId $tourId: $e")
            }
        }

        return tourDetailsMap
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
                val staycationLat = staycationDocument.getDouble("staycationLat") ?: 0.0
                val staycationLng = staycationDocument.getDouble("staycationLng") ?: 0.0
                val staycationPrice = staycationDocument.getDouble("staycationPrice") ?: 0.0
                val staycationSpace = staycationDocument.getString("staycationSpace") ?: ""
                val staycationTitle = staycationDocument.getString("staycationTitle") ?: ""
                val staycationType = staycationDocument.getString("staycationType") ?: ""

                val hasFirstAid = staycationDocument.getBoolean("hasFirstAid") ?: false
                val hasFireExit = staycationDocument.getBoolean("hasFireExit") ?: false
                val hasFireExtinguisher = staycationDocument.getBoolean("hasFireExtinguisher") ?: false
                val maxNoOfGuests = staycationDocument.getLong("maxNoOfGuests")?.toInt() ?: 0
                val additionalFeePerGuest = staycationDocument.getDouble("additionalFeePerGuest") ?: 0.0
                val noisePolicy = staycationDocument.getBoolean("noisePolicy") ?: false
                val allowSmoking = staycationDocument.getBoolean("allowSmoking") ?: false
                val allowPets = staycationDocument.getBoolean("allowPets") ?: false
                val additionalInfo = staycationDocument.getString("additionalInfo") ?: ""
                val noCancel = staycationDocument.getBoolean("noCancel") ?: false
                val noReschedule = staycationDocument.getBoolean("noCancel") ?: false
                val phoneNo = staycationDocument.getString("phoneNo") ?: ""
                val email = staycationDocument.getString("email") ?: ""

                // Fetch Staycation images
                val staycationImages = getServiceImages(staycationId, "Staycation")
                val hostInfo = getHostInfo(hostId)
                val availableDates = getStaycationAvailability(staycationId)

                Log.d("Host Id", hostInfo?.touristId ?: "")

                return Staycation(
                    staycationId = staycationId,
                    hasDangerousAnimal = hasDangerousAnimal,
                    hasSecurityCamera = hasSecurityCamera,
                    hasWeapon = hasWeapon,
                    noOfBathrooms = noOfBathrooms,
                    noOfBedrooms = noOfBedrooms,
                    noOfBeds = noOfBeds,
                    noOfGuests = noOfGuests,
                    staycationDescription = staycationDescription,
                    staycationLocation = staycationLocation,
                    staycationLat = staycationLat,
                    staycationLng = staycationLng,
                    staycationPrice = staycationPrice,
                    staycationSpace = staycationSpace,
                    staycationTitle = staycationTitle,
                    staycationType = staycationType,
                    staycationImages = staycationImages,
<<<<<<< Updated upstream
                    hasFirstAid = hasFirstAid,
                    hasFireExit = hasFireExit,
                    hasFireExtinguisher = hasFireExtinguisher,
                    maxNoOfGuests = maxNoOfGuests,
                    additionalFeePerGuest = additionalFeePerGuest,
                    additionalInfo = additionalInfo,
                    noisePolicy = noisePolicy,
                    allowPets = allowPets,
                    allowSmoking = allowSmoking,
                    noReschedule = noReschedule,
                    noCancel = noCancel,
                    phoneNo = phoneNo,
                    email = email,

=======
                    availableDates = availableDates,
>>>>>>> Stashed changes
                    host = Host(
                        profilePicture = hostInfo?.profilePicture ?: "",
                        firstName = hostInfo?.firstName ?: "",
                        middleName = hostInfo?.middleName ?: "",
                        lastName = hostInfo?.lastName ?: "",
                        touristId = hostInfo?.touristId ?: "",
                        hostId = hostId
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    suspend fun updatePendingBalance(hostMap: Map<String, Double>) {
        try {
            for ((hostId, totalAmount) in hostMap) {
                // Query the touristWalletCollection to get the touristWallet document
                val query = touristWalletCollection.whereEqualTo("touristId", hostId)
                val snapshot = query.get().await()

                // Check if the query returned any documents
                if (!snapshot.isEmpty) {
                    val document = snapshot.documents.first()

                    // Get the current pendingBalance and currentBalance
                    val pendingBalance = document.getDouble("pendingBalance") ?: 0.0
                    val currentBalance = document.getDouble("currentBalance") ?: 0.0

                    // Calculate the new balances
                    val newPendingBalance = pendingBalance - totalAmount
                    val newCurrentBalance = currentBalance + totalAmount

                    // Update the touristWallet document
                    document.reference.update(
                        mapOf(
                            "pendingBalance" to newPendingBalance,
                            "currentBalance" to newCurrentBalance
                        )
                    ).await()

                    // Log the update success
                    Log.d("UpdateTouristWallet", "Tourist wallet updated successfully")
                } else {
                    Log.d("UpdateTouristWallet", "No tourist wallet found for hostId: $hostId")
                }
            }
        } catch (e: Exception) {
            // Handle exceptions
            Log.e("UpdateTouristWallet", "Error updating tourist wallet: $e")
        }
    }

    suspend fun updateAdminPendingBalance(hostMap: Map<String, Double>) {
        try {
            for ((hostId, commission) in hostMap) {
                // Query the touristWalletCollection to get the touristWallet document
                val query = touristWalletCollection.whereEqualTo("touristId", "admin")
                val snapshot = query.get().await()

                // Check if the query returned any documents
                if (!snapshot.isEmpty) {
                    val document = snapshot.documents.first()

                    // Get the current pendingBalance and currentBalance
                    val pendingBalance = document.getDouble("pendingBalance") ?: 0.0
                    val currentBalance = document.getDouble("currentBalance") ?: 0.0

                    // Calculate the new balances
                    val newPendingBalance = pendingBalance - commission
                    val newCurrentBalance = currentBalance + commission

                    // Update the touristWallet document
                    document.reference.update(
                        mapOf(
                            "pendingBalance" to newPendingBalance,
                            "currentBalance" to newCurrentBalance
                        )
                    ).await()

                    // Log the update success
                    Log.d("UpdateAdminWallet", "Tourist wallet updated successfully")
                } else {
                    Log.d("UpdateAdminWallet", "No tourist wallet found for hostId: $hostId")
                }
            }
        } catch (e: Exception) {
            // Handle exceptions
            Log.e("UpdateAdminWallet", "Error updating tourist wallet: $e")
        }
    }

    suspend fun getHostIdFromStaycation(staycationId: String): String? {
        return try {
            val documentSnapshot = staycationCollection.document(staycationId).get().await()

            if (documentSnapshot.exists()) {
                documentSnapshot.getString("hostId")
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
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

    private suspend fun getServiceImages(serviceId: String, serviceType: String): List<Photo> {
        return try {
            val result = servicePhotoCollection
                .whereEqualTo("serviceId", serviceId)
                .whereEqualTo("serviceType", serviceType)
                .get()
                .await()

            val serviceImages = mutableListOf<Photo>()

            for (document in result.documents) {
                val photoId = document.id
                val photoType = document.getString("photoType") ?: ""
                val photoUrl = document.getString("photoUrl") ?: ""

                serviceImages.add(Photo(photoId = photoId, photoUrl = photoUrl, photoType = photoType, photoUri = Uri.parse(photoUrl)))
            }

            serviceImages

        } catch (e: Exception) {
            e.printStackTrace()
            emptyList() // Handle the error case as needed
        }
    }

    suspend fun getBusinessMenu(businessId: String): List<Photo> {
        return try {
            val result = businessMenuCollection
                .whereEqualTo("businessId", businessId)
                .get()
                .await()

            val menuImages = mutableListOf<Photo>()

            for (document in result.documents) {
                val photoId = document.id
                val photoType = document.getString("photoType") ?: ""
                val photoUrl = document.getString("photoUrl") ?: ""

                menuImages.add(Photo(photoId = photoId, photoUrl = photoUrl, photoType = photoType, photoUri = Uri.parse(photoUrl)))
            }

            menuImages

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

    suspend fun updateStaycationBookingPayment(
        serviceBookingId: String,
        newAmount: Double,
        amountRefunded: Double,
        commission: Double,
        paymentStatus: String,
    ) {
        try {

            val querySnapshot = paymentCollection
                .whereEqualTo("serviceBookingId", serviceBookingId)
                .whereEqualTo("serviceType", "Staycation")
                .get()
                .await()

            for (document in querySnapshot.documents) {

                val documentId = document.id

                addPaymentToHistory(documentId, "Update")

                document.reference.update(
                    mapOf(
                        "amount" to newAmount, // New amount
                        "amountRefunded" to amountRefunded, // New amountRefunded
                        "commission" to commission, // New commission
                        "paymentDate" to Timestamp.now(), // New paymentDate
                        "paymentStatus" to paymentStatus // New paymentStatus
                    )
                ).await()

                addPaymentToHistory(documentId, "Update")

            }
            Log.d("","Booking details updated successfully.")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    suspend fun updateStaycationBooking(
        bookingId: String,
        staycationId: String,
        initialCheckInDateMillis: Long,
        initialCheckOutDateMillis: Long,
        newCheckInDateMillis: Long,
        newCheckOutDateMillis: Long,
        commission: Double,
        amountRefunded: Double,
        newTotalAmount: Double,
        newNoOfGuests: Int,
        newNoOfPets: Int,
        newNoOfInfants: Int
    ): Boolean {
        return try {

            val staycationBookingRef = staycationBookingCollection
                .document(bookingId)

            val checkInDate = Date(newCheckInDateMillis)
            val checkOutDate = Date(newCheckOutDateMillis)

            val calendar = Calendar.getInstance()
            calendar.time = checkInDate
            calendar.add(Calendar.HOUR_OF_DAY, 6)
            val checkInDatePlus6Hours = calendar.time

            calendar.time = checkOutDate
            calendar.add(Calendar.HOUR_OF_DAY, 4)
            val checkOutDatePlus4Hours = calendar.time

            val newData = mapOf(
                "checkInDate" to checkInDatePlus6Hours,
                "checkOutDate" to checkOutDatePlus4Hours,
                "totalAmount" to newTotalAmount,
                "noOfGuests" to newNoOfGuests,
                "noOfPets" to newNoOfPets,
                "noOfInfants" to newNoOfInfants
            )

            addStaycationBookingToHistory(
                bookingId = bookingId,
                transaction = "update"
            )

            staycationBookingRef.update(newData).await()

            addStaycationBookingToHistory(
                bookingId = bookingId,
                transaction = "update"
            )

            makeAvailabilityInRange(staycationId, initialCheckInDateMillis, initialCheckOutDateMillis)

            deleteAvailabilityInRange(staycationId, newCheckInDateMillis, newCheckOutDateMillis)

            updateStaycationBookingPayment(
                serviceBookingId = bookingId,
                newAmount = newTotalAmount,
                amountRefunded = amountRefunded,
                commission = commission,
                paymentStatus = "Pending",
            )

            Log.d("Update", "Booking $bookingId status updated")
            true // Operation succeeded

        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
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

    suspend fun cancelTourBooking(bookingId: String, tourAvailabilityId: String, guestCount: Int) {
        try {
            val updatedData = hashMapOf(
                "bookingStatus" to "Cancelled"
            )

            addTourBookingToHistory(bookingId, "update")

            tourBookingCollection.document(bookingId).update(updatedData as Map<String, Any>).await()

            updateTourAvailability(
                tourAvailabilityId = tourAvailabilityId,
                newBookedSlot = guestCount,
                transaction = "Cancel Booking"
            )

            addTourBookingToHistory(bookingId, "update")

            cancelPaymentData(bookingId)


            Log.d("Update", "Booking $bookingId status updated to Cancelled")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private suspend fun cancelPaymentData(serviceBookingId: String) {
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

    private suspend fun addTourBookingToHistory(bookingId: String, transaction: String) {
        try {

            val tourBookingDocument = tourBookingCollection.document(bookingId).get().await()

            if (tourBookingDocument.exists()) {
                val bookingData = tourBookingDocument.data

                // Add additional fields
                bookingData?.put("tourBookingId", bookingId)
                bookingData?.put("transaction", transaction) // or "update" or "delete" based on your use case
                bookingData?.put("transactionTimestamp", FieldValue.serverTimestamp())

                // Add the fetched document to the payment_history collection
                tourBookingHistoryCollection.add(bookingData!!).await()
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

            val calendar = Calendar.getInstance()
            calendar.time = checkInDate
            calendar.add(Calendar.HOUR_OF_DAY, 6)
            val checkInDatePlus2Hours = calendar.time

            calendar.time = checkOutDate
            calendar.add(Calendar.HOUR_OF_DAY, 4)
            val checkOutDatePlus4Hours = calendar.time

            val bookingData = hashMapOf(
                "bookingDate" to FieldValue.serverTimestamp(),
                "bookingStatus" to bookingStatus,
             //   "checkInDate" to formatDateInTimeZone(checkInDate, timeZone),
              //  "checkOutDate" to formatDateInTimeZone(checkOutDate, timeZone),
                "checkInDate" to checkInDatePlus2Hours,
                "checkOutDate" to checkOutDatePlus4Hours,
                "noOfGuests" to noOfGuests,
                "noOfInfants" to noOfInfants,
                "noOfPets" to noOfPets,
                "commission" to commission,
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
                checkInDateMillis = checkInDateMillis,
                checkOutDateMillis = checkOutDateMillis
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

    suspend fun addTourBooking(
        tourAvailabilityId: String,
        tourDate: String,
        startTime: String,
        endTime: String,
        noOfGuests: Int,
        tourId: String,
        totalAmount: Double,
        touristId: String,
        commission: Double,
        paymentStatus: String,
        paymentMethod: String
    ): Boolean {
        try {

            val bookingData = hashMapOf(
                "bookingDate" to FieldValue.serverTimestamp(),
                "bookingStatus" to "Pending",
                "endTime" to endTime,
                "noOfGuests" to noOfGuests,
                "startTime" to startTime,
                "totalAmount" to totalAmount,
                "tourAvailabilityId" to tourAvailabilityId,
                "commission" to commission,
                "tourDate" to tourDate,
                "tourId" to tourId,
                "touristId" to touristId,

            )

            val tourBookingDocRef = tourBookingCollection.add(bookingData).await()
            val tourBookingId = tourBookingDocRef.id


            addTourBookingToHistory(
                bookingId = tourBookingId,
                transaction = "add"
            )

            updateTourAvailability(
                tourAvailabilityId = tourAvailabilityId,
                newBookedSlot = noOfGuests,
                transaction = "Add Booking"
            )

            addPaymentData(
                amount = totalAmount,
                commission = commission,
                paymentStatus = paymentStatus,
                serviceBookingId = tourBookingId,
                serviceType = "Tour",
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

    private suspend fun updateTourAvailability(tourAvailabilityId: String, newBookedSlot: Int, transaction: String) {
        try {

            val documentSnapshot = tourAvailabilityCollection.document(tourAvailabilityId).get().await()
            val currentBookedSlot = documentSnapshot.getLong("bookedSlot")?.toInt() ?: 0

            val updatedBookedSlot = if (transaction == "Add Booking") currentBookedSlot + newBookedSlot else currentBookedSlot - newBookedSlot

            tourAvailabilityCollection.document(tourAvailabilityId)
                .update("bookedSlot", updatedBookedSlot)
                .await()

        } catch (e: Exception) {
            e.printStackTrace()
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

    suspend fun makeAvailabilityInRange(staycationId: String, checkInDateMillis: Long, checkOutDateMillis: Long) {
        try {


//            val checkInDateMillis = 1709877600000
//            val checkOutDateMillis = 1710043200000

            val calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8")) // Create a calendar with UTC time zone
            calendar.timeInMillis = checkInDateMillis // Set the check-in date
            calendar.add(Calendar.HOUR_OF_DAY, -14) // Subtract 14 hours

            val startDate = calendar.time // Get the updated start date

            calendar.timeInMillis = checkOutDateMillis // Set the check-out date
            calendar.add(Calendar.HOUR_OF_DAY, -12) // Subtract 12 hours

            val endDate = calendar.time // Get the updated end date

            val dateRange = (startDate.time..endDate.time).step(TimeUnit.DAYS.toMillis(1))
                .map { Date(it) }

            // Create availability records for each date in the range
            for (date in dateRange) {
                val availabilityData = hashMapOf(
                    "staycationId" to staycationId,
                    "availableDate" to date,
                    // Add other fields as needed
                )

                Log.d("Availability Data", availabilityData.toString())

                // Add the availability record to the collection
                val documentReference = staycationAvailabilityCollection.add(availabilityData).await()

                Log.d("Document Added", "Document ID: ${documentReference.id}")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            // Handle the error case as needed
        }
    }


//    suspend fun makeAvailabilityInRange(staycationId: String, checkInDateMillis: Long, checkOutDateMillis: Long) {
//        try {
//
////            val checkInDateMillis = 1709258400000
////            val checkOutDateMillis = 1709870400000
//
//            val calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8")) // Create a calendar with UTC time zone
//            calendar.timeInMillis = checkInDateMillis // Set the check-in date
//            calendar.add(Calendar.HOUR_OF_DAY, -10) // Subtract 10 hours
//
//            val startDate = calendar.time // Get the updated start date
//
//            calendar.timeInMillis = checkOutDateMillis // Set the check-out date
//            calendar.add(Calendar.HOUR_OF_DAY, -10) // Subtract 10 hours
//
//            val endDate = calendar.time // Get the updated end date
//
//            val dateRange = (startDate.time until endDate.time step TimeUnit.DAYS.toMillis(1))
//                .map { Date(it) }
//
//            // Create availability records for each date in the range
//            for (date in dateRange) {
//                val availabilityData = hashMapOf(
//                    "staycationId" to staycationId,
//                    "availableDate" to date,
//                    // Add other fields as needed
//                )
//
//                Log.d("Availability Data", availabilityData.toString())
//
//                // Add the availability record to the collection
//                val documentReference = staycationAvailabilityCollection.add(availabilityData).await()
//
//                Log.d("Document Added", "Document ID: ${documentReference.id}")
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//            // Handle the error case as needed
//        }
//    }




    private suspend fun deleteAvailabilityInRange(staycationId: String, checkInDateMillis: Long, checkOutDateMillis: Long) {
        try {

            val checkInDate = Date(checkInDateMillis)
            val checkOutDate = Date(checkOutDateMillis)

            val calendar = Calendar.getInstance()

            calendar.time = checkInDate
            calendar.add(Calendar.HOUR_OF_DAY, -8)
            val checkInDateMinus8Hours = calendar.time

            calendar.time = checkOutDate
            calendar.add(Calendar.HOUR_OF_DAY, -8)
            val checkOutDateMinus8Hours = calendar.time

            val query = staycationAvailabilityCollection
                .whereEqualTo("staycationId", staycationId)
                .whereGreaterThanOrEqualTo("availableDate", checkInDateMinus8Hours)
                .whereLessThanOrEqualTo("availableDate", checkOutDateMinus8Hours)

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

   // val fetchedServiceIds = mutableSetOf<String>()

//    private suspend fun getServiceIdsByTags(tagNames: List<String>, pageNumber: Int, pageSize: Int): List<Tag> {
//        return try {
//            val result = mutableListOf<Tag>()
//
//            var query = serviceTagCollection
//                .whereIn("tagName", tagNames)
//                .orderBy("serviceId")
//                .limit(pageSize.toLong())
//
//            // If it's not the first page, start after the last document of the previous page
//            if (pageNumber > 0) {
//                val lastDocumentSnapshot = serviceTagCollection
//                    .whereIn("tagName", tagNames)
//                    .orderBy("serviceId")
//                    .limit((pageNumber * pageSize).toLong())
//                    .get()
//                    .await()
//                    .documents
//                    .lastOrNull()
//
//                if (lastDocumentSnapshot != null) {
//                    query = query.startAfter(lastDocumentSnapshot)
//                }
//            }
//
//            val querySnapshot = query.get().await()
//
//            querySnapshot.documents.forEach { document ->
//                val tagId = document.id
//                val tagName = document.getString("tagName") ?: ""
//                val serviceId = document.getString("serviceId") ?: ""
//
//                if (!fetchedServiceIds.contains(serviceId)) {
//                    val tag = Tag(tagId = tagId, tagName = tagName, serviceId = serviceId)
//                    result.add(tag)
//                    fetchedServiceIds.add(serviceId)
//
//                    Log.d("FirestoreTag", "Tag: $tagName, ServiceId: $serviceId")
//                }
//            }
//
//            result
//        } catch (e: Exception) {
//            e.printStackTrace()
//            emptyList() // Handle the error case as needed
//        }
//    }


//    private suspend fun getServiceIdsByTags(tagNames: List<String>, pageNumber: Int, pageSize: Int): List<Tag> {
//        return try {
//            val result = mutableListOf<Tag>()
//
//            var query = serviceTagCollection
//                .whereIn("tagName", tagNames)
//                .orderBy("serviceId")
//                .limit(pageSize.toLong())
//
//            // If it's not the first page, start after the last document of the previous page
//            if (pageNumber > 0) {
//                val lastDocumentSnapshot = serviceTagCollection
//                    .whereIn("tagName", tagNames)
//                    .orderBy("serviceId")
//                    .limit((pageNumber * pageSize).toLong())
//                    .get()
//                    .await()
//                    .documents
//                    .lastOrNull()
//
//                if (lastDocumentSnapshot != null) {
//                    query = query.startAfter(lastDocumentSnapshot)
//                }
//            }
//
//            val querySnapshot = query.get().await()
//
//            querySnapshot.documents.forEach { document ->
//                val tagId = document.id
//                val tagName = document.getString("tagName") ?: ""
//                val serviceId = document.getString("serviceId") ?: ""
//
//                val tag = Tag(tagId = tagId, tagName = tagName, serviceId = serviceId)
//                result.add(tag)
//
//                Log.d("FirestoreTag", "Tag: $tagName, ServiceId: $serviceId")
//            }
//
//            result
//        } catch (e: Exception) {
//            e.printStackTrace()
//            emptyList() // Handle the error case as needed
//        }
//    }

    private suspend fun getServiceIdsByTags(
        tagNames: List<String>,
        pageNumber: Int,
        pageSize: Int,
        serviceIdSet: SortedSet<String>
    ): List<Tag> {
        return try {

            processedServiceIds.clear()

            // val startIndex = pageNumber * pageSize

            // startIndex = [0, 18, 24, 30, 36]

            val initialLoadSize = pageSize * 3
            val startIndex = if (pageNumber == 0) {
                0
            } else {
                ((pageNumber - 1) * pageSize) + initialLoadSize
            }

            val result = mutableListOf<Tag>()

//            Log.d("Page Number(Start): ", pageNumber.toString())
//            Log.d("Start Index(Start): ", startIndex.toString())
//            Log.d("Page Size(Start): ", pageSize.toString())

            var query = serviceTagCollection
                .whereIn("tagName", tagNames)
                .orderBy("serviceId")

//            // Log initial result size and content
//            Log.d("Result Size(Start): ", result.size.toString())
//            Log.d("Result (Start): ", result.map { it.serviceId }.toString())

            // If it's not the first page, start after the last document of the previous page
            if (pageNumber > 0) {
                val lastDocumentSnapshot = serviceTagCollection
                    .whereIn("tagName", tagNames)
                    .orderBy("serviceId")
                    .limit(startIndex.toLong())
                    .get()
                    .await()
                    .documents
                    .lastOrNull()

                if (lastDocumentSnapshot != null) {
                    query = query.startAfter(lastDocumentSnapshot)
                  //  Log.d("Start after document: ", lastDocumentSnapshot.toString())
                }
            }

            val querySnapshot = query
                .limit(pageSize.toLong() + 1) // Fetch one extra to check if there's a next page
                .get()
                .await()

            querySnapshot.documents.forEach { document ->
                val tagId = document.id
                val tagName = document.getString("tagName") ?: ""
                val serviceId = document.getString("serviceId") ?: ""

//                Log.d("NOTE", "STARTED")
//                Log.d("ProcessedServiceIds", processedServiceIds.toString())
//                Log.d("ServiceIdSet", serviceIdSet.toString())
//                Log.d("ServiceId", serviceId)

                if (serviceIdSet.contains(serviceId) && serviceId !in processedServiceIds) {
                    val tag = Tag(tagId = tagId, tagName = tagName, serviceId = serviceId)
                    result.add(tag)
                    processedServiceIds.add(serviceId)

                    // Log added tag and related information
//                    Log.d("SEPARATOR", "---------------------------------------")
//                    Log.d("FirestoreTag", "Tag: $tagName, ServiceId: $serviceId")
//                    Log.d("ProcessedServiceIds", processedServiceIds.toString())
//                    Log.d("ServiceIdSet", serviceIdSet.toString())
                }
            }

        //    Log.d("Result Size(BEFORE REMOVAL): ", result.toString())
            // If there are more documents than the requested page size, remove the extra one
            if (result.size > pageSize) {
                result.removeAt(result.size - 1)
            }

//            // Log final result size and content
//            Log.d("Page Number(Last): ", pageNumber.toString())
//            Log.d("Start Index(Last): ", startIndex.toString())
//            Log.d("Page Size(Last): ", pageSize.toString())
//            Log.d("Result Size(Last): ", result.size.toString())
//            Log.d("Result (Last): ", result.map { it.serviceId }.toString())

            result
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList() // Handle the error case as needed
        }
    }

    suspend fun getStaycationBookingsWithPaging(
        touristId: String,
        pageNumber: Int,
        pageSize: Int,
        initialLoadSize: Int
    ): List<StaycationBooking> {
        return try {

            val startIndex = if (pageNumber == 0) {
                0
            } else {
                ((pageNumber - 1) * pageSize) + initialLoadSize
            }

            val staycationBookings = mutableListOf<StaycationBooking>()

            var query = staycationBookingCollection
                .whereEqualTo("touristId", touristId)
                .orderBy("bookingDate", Query.Direction.DESCENDING)

            if (pageNumber > 0) {
                val lastDocumentSnapshot = staycationBookingCollection
                    .whereEqualTo("touristId", touristId)
                    .orderBy("bookingDate", Query.Direction.DESCENDING)
                    .limit(startIndex.toLong())
                    .get()
                    .await()
                    .documents
                    .lastOrNull()

                if (lastDocumentSnapshot != null) {
                    query = query.startAfter(lastDocumentSnapshot)
                }
            }

            val querySnapshot = query
                .limit(pageSize.toLong() + 1) // Fetch one extra to check if there's a next page
                .get()
                .await()

            querySnapshot.documents.forEach { document ->

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

                val staycation = getStaycationDetailsById(staycationId) ?: Staycation()
                val review = getBookingReview(bookingId, touristId)

                Log.d("Staycation", staycation.toString())

                val staycationBooking = StaycationBooking(
                    staycationBookingId = bookingId,
                    bookingDate = bookingDate,
                    bookingStatus = bookingStatus,
                    checkInDate = checkInDate,
                    checkOutDate = checkOutDate,
                    noOfGuests = noOfGuests,
                    noOfInfants = noOfInfants,
                    noOfPets = noOfPets,
                    staycation = staycation,
                    totalAmount = totalAmount,
                    tourist = Tourist(touristId = touristId),
                    bookingReview = review
                )

                staycationBookings.add(staycationBooking)

            }

            if (staycationBookings.size > pageSize) {
                staycationBookings.removeAt(staycationBookings.size - 1)
            }

            staycationBookings
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun getTourBookingsWithPaging(
        touristId: String,
        pageNumber: Int,
        pageSize: Int,
        initialLoadSize: Int
    ): List<TourBooking> {
        return try {

            val startIndex = if (pageNumber == 0) {
                0
            } else {
                ((pageNumber - 1) * pageSize) + initialLoadSize
            }

            val tourBookings = mutableListOf<TourBooking>()

            var query = tourBookingCollection
                .whereEqualTo("touristId", touristId)
                .orderBy("bookingDate", Query.Direction.DESCENDING)

            if (pageNumber > 0) {
                val lastDocumentSnapshot = tourBookingCollection
                    .whereEqualTo("touristId", touristId)
                    .orderBy("bookingDate", Query.Direction.DESCENDING)
                    .limit(startIndex.toLong())
                    .get()
                    .await()
                    .documents
                    .lastOrNull()

                if (lastDocumentSnapshot != null) {
                    query = query.startAfter(lastDocumentSnapshot)
                }
            }

            val querySnapshot = query
                .limit(pageSize.toLong() + 1) // Fetch one extra to check if there's a next page
                .get()
                .await()

            querySnapshot.documents.forEach { document ->

                val bookingId = document.id
                val bookingDate = document.getDate("bookingDate") ?: Date()
                val bookingStatus = document.getString("bookingStatus") ?: ""
                val tourDate = document.getString("tourDate") ?: ""
                val startTime = document.getString("startTime") ?: ""
                val endTime = document.getString("endTime") ?: ""
                val noOfGuests = document.getLong("noOfGuests")?.toInt() ?: 0
                val totalAmount = document.getLong("totalAmount")?.toDouble() ?: 0.0
                val tourAvailabilityId = document.getString("tourAvailabilityId") ?: ""
                val tourId = document.getString("tourId") ?: ""

                val tour = getTourById(tourId)
                val review = getBookingReview(bookingId, touristId)


                val tourBooking = TourBooking(
                    tourBookingId = bookingId,
                    tourist = Tourist(touristId = touristId),
                    bookingDate = bookingDate,
                    bookingStatus = bookingStatus,
                    startTime = startTime,
                    endTime = endTime,
                    noOfGuests = noOfGuests,
                    totalAmount = totalAmount,
                    bookingReview = review,
                    tour = tour,
                    tourAvailabilityId = tourAvailabilityId,
                    tourDate = tourDate
                )

                tourBookings.add(tourBooking)

            }

            if (tourBookings.size > pageSize) {
                tourBookings.removeAt(tourBookings.size - 1)
            }

            tourBookings
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }



    private suspend fun getServiceIdsWithTag(
        tagName: String,
        pageNumber: Int,
        pageSize: Int,
        initialLoadSize: Int
        //   serviceIdSet: SortedSet<String>
    ): List<String> {
        return try {

          //  val initialLoadSize = pageSize * 3
            val startIndex = if (pageNumber == 0) {
                0
            } else {
                ((pageNumber - 1) * pageSize) + initialLoadSize
            }

            val result = mutableListOf<String>()

            var query = serviceCollection
                .whereNotEqualTo("serviceType", "Business")
                .orderBy("serviceType") // Order by the same property as the inequality filter
                .orderBy("serviceId", Query.Direction.DESCENDING)

            if (pageNumber > 0) {
                val lastDocumentSnapshot = serviceCollection
                    .whereNotEqualTo("serviceType", "Business")
                    .orderBy("serviceType") // Order by the same property as the inequality filter
                    .orderBy("serviceId", Query.Direction.DESCENDING)
                    .limit(startIndex.toLong())
                    .get()
                    .await()
                    .documents
                    .lastOrNull()

                if (lastDocumentSnapshot != null) {
                    query = query.startAfter(lastDocumentSnapshot)
                }
            }

            val querySnapshot = query
                .limit(pageSize.toLong() + 1) // Fetch one extra to check if there's a next page
                .get()
                .await()

            querySnapshot.documents.forEach { document ->

                val serviceId = document.getString("serviceId") ?: ""

                Log.d("ServiceId(before tag)", serviceId)

                val fetched = serviceTagCollection
                    .whereEqualTo("serviceId", serviceId)
                    .whereEqualTo("tagName", tagName)
                    .limit(1)
                    .get()
                    .await()

                if (!fetched.isEmpty) {
                    for (doc in fetched.documents) {
                        Log.d("FetchedDocument", doc.data.toString())
                    }
                    result.add(serviceId)
                }

            }

            if (result.size > pageSize) {
                result.removeAt(result.size - 1)
            }

            result
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList() // Handle the error case as needed
        }
    }


    private suspend fun getServiceIdsWithTags(
        tagNames: List<String>,
        pageNumber: Int,
        pageSize: Int,
        initialLoadSize: Int,
     //   serviceIdSet: SortedSet<String>
    ): List<String> {
        return try {

          //  val initialLoadSize = pageSize * 3
            val startIndex = if (pageNumber == 0) {
                0
            } else {
                ((pageNumber - 1) * pageSize) + initialLoadSize
            }

            val result = mutableListOf<String>()

            var query = serviceCollection
                .whereNotEqualTo("serviceType", "Business")
                .orderBy("serviceType") // Order by the same property as the inequality filter
                .orderBy("serviceId", Query.Direction.DESCENDING)

            if (pageNumber > 0) {
                val lastDocumentSnapshot = serviceCollection
                    .whereNotEqualTo("serviceType", "Business")
                    .orderBy("serviceType") // Order by the same property as the inequality filter
                    .orderBy("serviceId", Query.Direction.DESCENDING)
                    .limit(startIndex.toLong())
                    .get()
                    .await()
                    .documents
                    .lastOrNull()

                if (lastDocumentSnapshot != null) {
                    query = query.startAfter(lastDocumentSnapshot)
                }
            }

            val querySnapshot = query
                .limit(pageSize.toLong() + 1) // Fetch one extra to check if there's a next page
                .get()
                .await()

            querySnapshot.documents.forEach { document ->

                val serviceId = document.getString("serviceId") ?: ""

                Log.d("ServiceId(before tag)", serviceId)

                val fetched = serviceTagCollection
                    .whereEqualTo("serviceId", serviceId)
                    .whereIn("tagName", tagNames)
                    .limit(1)
                    .get()
                    .await()

                if (!fetched.isEmpty) {
                    for (doc in fetched.documents) {
                        Log.d("FetchedDocument", doc.data.toString())
                    }
                    result.add(serviceId)
                }

            }

            if (result.size > pageSize) {
                result.removeAt(result.size - 1)
            }

            result
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList() // Handle the error case as needed
        }
    }




    //  PAGING BUT DUPLICATED
//    private suspend fun getServiceIdsByTags(
//        tagNames: List<String>,
//        pageNumber: Int,
//        pageSize: Int,
//        serviceIdSet: SortedSet<String>
//    ): List<Tag> {
//        return try {
//
//            val startIndex = pageNumber * pageSize
//            val result = mutableListOf<Tag>()
//
//            var query = serviceTagCollection
//                .whereIn("tagName", tagNames)
//                .orderBy("serviceId")
//
//            Log.d("Page Number(Start): ", pageNumber.toString())
//            Log.d("Start Index(Start): ", startIndex.toString())
//            Log.d("Page Size(Start): ", pageSize.toString())
//            Log.d("Result Size(Start): ", result.size.toString())
//            Log.d("Result (Start): ", result.map { it.serviceId }.toString())
//
//            // If it's not the first page, start after the last document of the previous page
//            if (pageNumber > 0) {
//                val lastDocumentSnapshot = serviceTagCollection
//                    .whereIn("tagName", tagNames)
//                    .orderBy("serviceId")
//                    .limit(startIndex.toLong())
//                    .get()
//                    .await()
//                    .documents
//                    .lastOrNull()
//
//                if (lastDocumentSnapshot != null) {
//                    query = query.startAfter(lastDocumentSnapshot)
//                }
//            }
//
//            val querySnapshot = query
//                .limit(pageSize.toLong() + 1) //  + 1       // Fetch one extra to check if there's a next page
//                .get()
//                .await()
//
//            querySnapshot.documents.forEach { document ->
//                val tagId = document.id
//                val tagName = document.getString("tagName") ?: ""
//                val serviceId = document.getString("serviceId") ?: ""
//
//                if (serviceIdSet.contains(serviceId) && serviceId !in processedServiceIds) {
//                    val tag = Tag(tagId = tagId, tagName = tagName, serviceId = serviceId)
//                    result.add(tag)
//                    processedServiceIds.add(serviceId)
//                    Log.d("FirestoreTag", "Tag: $tagName, ServiceId: $serviceId")
//                    Log.d("ProcessedServiceIds", processedServiceIds.toString())
//                    Log.d("ServiceIdSet", serviceIdSet.toString())
//                }
//
//            }
//
//            // If there are more documents than the requested page size, remove the extra one
//            if (result.size > pageSize) {
//                result.removeAt(result.size - 1)
//
//            }
//
//            Log.d("Page Number(Last): ", pageNumber.toString())
//            Log.d("Start Index(Last): ", startIndex.toString())
//            Log.d("Page Size(Last): ", pageSize.toString())
//            Log.d("Result Size(Last): ", result.size.toString())
//            Log.d("Result (Last): ", result.map { it.serviceId }.toString())
//
//            result
//        } catch (e: Exception) {
//            e.printStackTrace()
//            emptyList() // Handle the error case as needed
//        }
//    }



//    private suspend fun getServiceIdsByTags(
//        tagNames: List<String>,
//        pageNumber: Int,
//        pageSize: Int,
//        serviceIdSet: SortedSet<String>
//    ): List<Tag> {
//        return try {
//            val startIndex = pageNumber * pageSize
//
//            val result = mutableListOf<Tag>()
//
//            // Split the serviceIdSet into smaller batches
//            val batches = serviceIdSet.chunked(3) // Adjust the batch size as needed
//
//            Log.d("Batches", batches.toString())
//
//            for (batch in batches) {
//                var query = serviceTagCollection
//                    .whereIn("tagName", tagNames)
//                    .whereIn("serviceId", batch)
//                    .orderBy("serviceId")
//                    .limit(pageSize.toLong() + 1)
//
//                // If it's not the first page, start after the last document of the previous page
//                if (pageNumber > 0) {
//                    val lastDocumentSnapshot = serviceTagCollection
//                        .whereIn("tagName", tagNames)
//                        .whereIn("serviceId", batch)
//                        .orderBy("serviceId")
//                        .limit(startIndex.toLong())
//                        .get()
//                        .await()
//                        .documents
//                        .lastOrNull()
//
//                    if (lastDocumentSnapshot != null) {
//                        query = query.startAfter(lastDocumentSnapshot)
//                    }
//                }
//
//                val querySnapshot = query.get().await()
//
//                querySnapshot.documents.forEach { document ->
//                    val tagId = document.id
//                    val tagName = document.getString("tagName") ?: ""
//                    val serviceId = document.getString("serviceId") ?: ""
//
//                    val tag = Tag(tagId = tagId, tagName = tagName, serviceId = serviceId)
//                    result.add(tag)
//                    Log.d("FirestoreTag", "Tag: $tagName, ServiceId: $serviceId")
//                }
//
//                // If there are more documents than the requested page size, remove the extra one
//                if (result.size > pageSize) {
//                    result.removeAt(result.size - 1)
//                }
//            }
//
//            Log.d("Page Number(Last): ", pageNumber.toString())
//            Log.d("Start Index(Last): ", startIndex.toString())
//            Log.d("Page Size(Last): ", pageSize.toString())
//            Log.d("Result Size(Last): ", result.size.toString())
//
//            result
//        } catch (e: Exception) {
//            e.printStackTrace()
//            emptyList() // Handle the error case as needed
//        }
//    }
//
//






//    private suspend fun getServiceIdsByTags(tagNames: List<String>): List<Tag> {
//        return try {
//            val result = serviceTagCollection
//                .whereIn("tagName", tagNames)
//                .get()
//                .await()
//
//            val tags = mutableListOf<Tag>()
//
//            for (document in result.documents) {
//                val tagId = document.id
//                val tagName = document.getString("tagName") ?: ""
//                val serviceId = document.getString("serviceId") ?: ""
//
//                tags.add(Tag(tagId = tagId, tagName = tagName, serviceId = serviceId))
//            }
//
//            tags
//        } catch (e: Exception) {
//            e.printStackTrace()
//            emptyList() // Handle the error case as needed
//        }
//    }

//    suspend fun getAllStaycationsByTags(tags: List<String>, page: Int): List<Staycation> {
//        return try {
//            val pageSize = Constants.PAGE_SIZE
//            // Fetch serviceIds with the specified tags
//            val serviceIds = getServiceIdsByTags(tags)
//
//            // Fetch staycations with document id (staycationId) equal to the serviceId
//            val staycationList = mutableListOf<Staycation>()
//
//            val startIdx = page * pageSize
//            val endIdx = startIdx + pageSize
//
//            for (i in startIdx until min(endIdx, serviceIds.size)) {
//                val serviceId = serviceIds[i].serviceId
//                val document = db.collection("staycation").document(serviceId).get().await()
//
//                if (document.exists()) {
//                    val staycationId = document.id
//                    val staycation = createStaycationFromDocument(document, staycationId)
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


    suspend fun getAllUniqueServiceId(): SortedSet<String> {
        val uniqueServiceIds = sortedSetOf<String>()
        val querySnapshot = serviceTagCollection
            .orderBy("serviceId")
            .get()
            .await()

        for (document in querySnapshot.documents) {
            val serviceId = document.getString("serviceId") ?: ""
            val serviceType = document.getString("serviceType")

            if (serviceType != "Business") {
                uniqueServiceIds.add(serviceId)
            }
        }

        return uniqueServiceIds
    }

    suspend fun getAllServicesByTagWithPaging(
        hostId: String,
        tag: String,
        pageNumber: Int,
        pageSize: Int,
        initialLoadSize: Int,

        searchText: String,
        includeStaycation: Boolean,
        includeTour: Boolean,
        houseSelected: Boolean,
        apartmentSelected: Boolean,
        condoSelected: Boolean,
        campSelected: Boolean,
        guestHouseSelected: Boolean,
        hotelSelected: Boolean,
        photoTourSelected: Boolean,
        foodTripSelected: Boolean,
        barHoppingSelected: Boolean,
        selectedRating: Int,
        minPrice: String,
        maxPrice: String,
        city: String,
        capacity: String,
        bedroomCount: String,
        bedCount: String,
        bathroomCount: String,
        checkedAmenities: List<Boolean>,
        checkedOffers: List<Boolean>,
        startDate: Long? = null,
        endDate: Long? = null
    ): List<HomePagingItem> {

        //val allAmenities = listOf("Swimming Pool", "Gym", "Parking", "Wi-Fi", "Restaurant", "Spa", "Bar", "Business Center")
        val allAmenities = listOf("Wifi", "TV", "Kitchen", "Washing machine", "Dedicated workspace" , "Pool", "Gym equipment", "Hot tub", "City view")
        val allOffers = listOf("Food", "Souvenir", "Transportation", "Drinks")

        val checkedAmenityNames = mutableListOf<String>()
        val checkedOfferNames = mutableListOf<String>()

        for (index in checkedAmenities.indices) {
            if (checkedAmenities[index]) {
                checkedAmenityNames.add(allAmenities[index])
            }
        }

        for (index in checkedOffers.indices) {
            if (checkedOffers[index]) {
                checkedOfferNames.add(allOffers[index])
            }
        }


       // val serviceIds = getServiceIdsByTag(tag, pageNumber, pageSize)

        val serviceIds = getServiceIdsWithTag(tag, pageNumber, pageSize, initialLoadSize)
        val itemsList = mutableListOf<HomePagingItem>()

        for (serviceId in serviceIds) {
            if (includeStaycation) {
                val staycationDoc = staycationCollection.document(serviceId).get().await()
                val staycationImage = getServiceImages(serviceId, "Staycation")

                if (staycationDoc.exists() && staycationDoc.getString("hostId") != hostId) {
                    val staycationTitle = staycationDoc.getString("staycationTitle") ?: ""
                    val staycationType = staycationDoc.getString("staycationType") ?: ""
                    val staycationLocation = staycationDoc.getString("staycationLocation") ?: ""
                    val staycationCapacity = staycationDoc.getLong("noOfGuests")?.toInt() ?: 0
                    val staycationBeds = staycationDoc.getLong("noOfBeds")?.toInt() ?: 0
                    val staycationBedrooms = staycationDoc.getLong("noOfBedrooms")?.toInt() ?: 0
                    val staycationBathrooms = staycationDoc.getLong("noOfBathrooms")?.toInt() ?: 0
                    val staycationPrice = staycationDoc.getLong("staycationPrice")?.toInt() ?: 0
                    val bookings = getStaycationBookings(serviceId)
                    val amenities = getAmenities(serviceId, "Staycation")
                    val staycationAvailability = getStaycationAvailability(serviceId)

                    val inBetween = if (startDate != null && endDate != null) {
                        allDatesAvailable(
                            staycationAvailability.map { it.availableDate?.toDate() ?: Date(0) }.sorted(),
                            startDate,
                            endDate
                        )
                    } else {
                        true
                    }

                   // Log.d("Staycation In Between", inBetween.toString())

                    val validReviews = bookings
                        .mapNotNull { it.bookingReview }
                        .filter { it.bookingId != "" }

                    val average = validReviews.map { it.rating }.average()
                    val averageReviewRating = if (average.isNaN()) 0.0 else average
                    val staycationAmenities = amenities.filter { checkedAmenityNames.contains(it.amenityName) }

                    Log.d("Staycation Amenities After Filter", staycationAmenities.map { it.amenityName }.toString())

                    if ((searchText == "" || staycationTitle.contains(searchText, ignoreCase = true)) &&
                        (city == "" || staycationLocation.contains(city, ignoreCase = true)) &&
                        (capacity == "" || staycationCapacity >= capacity.toInt()) &&
                        (bedroomCount == "Any" || bedroomCount.toInt() <= staycationBedrooms) &&
                        (bedCount == "Any" || bedCount.toInt() <= staycationBeds) &&
                        (bathroomCount == "Any" || bathroomCount.toInt() <= staycationBathrooms) &&
                        (staycationType == "House" && houseSelected ||
                                staycationType == "Apartment" && apartmentSelected ||
                                staycationType == "Condominium" && condoSelected ||
                                staycationType == "Camp" && campSelected ||
                                staycationType == "Guest House" && guestHouseSelected ||
                                staycationType == "Hotel" && hotelSelected) &&
                        averageReviewRating >= selectedRating &&
                        (minPrice == "" || staycationPrice >= minPrice.toInt()) &&
                        (maxPrice == "" || staycationPrice <= maxPrice.toInt()) &&
                        inBetween
                    // staycationAmenities.isNotEmpty()   UNCOMMENT THIS
                    ) {
                        val staycation = HomePagingItem(
                            serviceId = serviceId,
                            serviceCoverPhoto = staycationImage.find { it.photoType == "Cover" }?.photoUrl
                                ?: "https://upload.wikimedia.org/wikipedia/commons/thumb/3/3f/Placeholder_view_vector.svg/1022px-Placeholder_view_vector.svg.png",
                            serviceTitle = staycationTitle,
                            averageReviewRating = averageReviewRating,
                            location = staycationDoc.getString("staycationLocation") ?: "",
                            price = staycationDoc.getDouble("staycationPrice") ?: 0.0,
                            hostId = staycationDoc.getString("hostId") ?: "",
                            serviceType = "Staycation"
                        )
                        itemsList.add(staycation)
                    }
                }
            }

            if (includeTour) {
                val tourDoc = tourCollection.document(serviceId).get().await()
                val tourImage = getServiceImages(serviceId, "Tour")

                if (tourDoc.exists() && tourDoc.getString("hostId") != hostId) {
                    val tourTitle = tourDoc.getString("tourTitle") ?: ""
                    val tourType = tourDoc.getString("tourType") ?: ""
                    val tourLocation = tourDoc.getString("tourLocation") ?: ""
                    val tourPrice = tourDoc.getLong("tourPrice")?.toInt() ?: 0
                    val offers = getTourOffers(serviceId)
                    val tourAvailability = getTourAvailabilities(serviceId)

                    val tourOffers = offers.filter { checkedOfferNames.contains(it.typeOfOffer) }

                    val tourAvailabilityDates = tourAvailability.map { localDate ->
                        Date.from(localDate.date.atStartOfDay(ZoneId.systemDefault()).toInstant())
                    }.sorted()

                    val inBetween: Boolean = if (startDate != null && endDate != null) {
                        allDatesAvailable(
                            tourAvailabilityDates,
                            startDate,
                            endDate
                        )
                    } else {
                        true
                    }

                    if ((searchText == "" || tourTitle.contains(searchText, ignoreCase = true)) &&
                        (city == "" || tourLocation.contains(city, ignoreCase = true)) &&
                        (tourType == "Photo Tour" && photoTourSelected ||
                                tourType == "Food Trip" && foodTripSelected ||
                                tourType == "Bar Hopping" && barHoppingSelected) &&
                        (minPrice == "" || tourPrice >= minPrice.toInt()) &&
                        (maxPrice == "" || tourPrice <= maxPrice.toInt()) &&
                        tourOffers.isNotEmpty() &&
                        inBetween
                    ) {
                        val tour = HomePagingItem(
                            serviceId = serviceId,
                            serviceCoverPhoto = tourImage.find { it.photoType == "Cover" }?.photoUrl
                                ?: "https://upload.wikimedia.org/wikipedia/commons/thumb/3/3f/Placeholder_view_vector.svg/1022px-Placeholder_view_vector.svg.png",
                            serviceTitle = tourTitle,
                            averageReviewRating = tourDoc.getDouble("averageReviewRating") ?: 0.0,
                            location = tourDoc.getString("tourLocation") ?: "",
                            price = tourDoc.getDouble("tourPrice") ?: 0.0,
                            tourDuration = tourDoc.getString("tourDuration")?.toInt(),
                            hostId = tourDoc.getString("hostId") ?: "",
                            serviceType = "Tour"
                        )
                        itemsList.add(tour)
                    }
                }
            }
        }

        return itemsList
    }

    private suspend fun getServiceIdsByTag(
        tagName: String,
        pageNumber: Int,
        pageSize: Int
    ): List<Tag> {
        return try {

            val initialLoadSize = pageSize * 3
            val startIndex = if (pageNumber == 0) {
                0
            } else {
                ((pageNumber - 1) * pageSize) + initialLoadSize
            }

            val result = mutableListOf<Tag>()

            var query = serviceTagCollection
                .whereEqualTo("tagName", tagName)
                .orderBy("serviceId")

            // If it's not the first page, start after the last document of the previous page
            if (pageNumber > 0) {
                val lastDocumentSnapshot = serviceTagCollection
                    .whereEqualTo("tagName", tagName)
                    .orderBy("serviceId")
                    .limit(startIndex.toLong())
                    .get()
                    .await()
                    .documents
                    .lastOrNull()

                if (lastDocumentSnapshot != null) {
                    query = query.startAfter(lastDocumentSnapshot)
                }
            }

            val querySnapshot = query
                .limit(pageSize.toLong() + 1) // Fetch one extra to check if there's a next page
                .get()
                .await()

            querySnapshot.documents.forEach { document ->
                val tagId = document.id
                val tagName = document.getString("tagName") ?: ""
                val serviceId = document.getString("serviceId") ?: ""

                val tag = Tag(tagId = tagId, tagName = tagName, serviceId = serviceId)
                result.add(tag)
            }

            // If there are more documents than the requested page size, remove the extra one
            if (result.size > pageSize) {
                result.removeAt(result.size - 1)
            }

            Log.d("Result", result.map { it.tagName }.toString())

            result
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList() // Handle the error case as needed
        }
    }

    suspend fun getAllServicesByTagsWithPaging(
        hostId: String,
        tags: List<String>,
        pageNumber: Int,
        pageSize: Int,
        initialLoadSize: Int,
        serviceIdSet: SortedSet<String>,

        searchText: String,
        includeStaycation: Boolean,
        includeTour: Boolean,
        houseSelected: Boolean,
        apartmentSelected: Boolean,
        condoSelected: Boolean,
        campSelected: Boolean,
        guestHouseSelected: Boolean,
        hotelSelected: Boolean,
        photoTourSelected: Boolean,
        foodTripSelected: Boolean,
        barHoppingSelected: Boolean,
        selectedRating: Int,
        minPrice: String,
        maxPrice: String,
        city: String,
        capacity: String,
        bedroomCount: String,
        bedCount: String,
        bathroomCount: String,
        checkedAmenities: List<Boolean>,
        checkedOffers: List<Boolean>,
        startDate: Long? = null,
        endDate: Long? = null

    ): List<HomePagingItem> {

        val allAmenities = listOf("Wifi", "TV", "Kitchen", "Washing machine", "Dedicated workspace" , "Pool", "Gym equipment", "Hot tub", "City view")
        val allOffers = listOf("Food", "Souvenir", "Transportation", "Drinks")

        val checkedAmenityNames = mutableListOf<String>()
        val checkedOfferNames = mutableListOf<String>()

        for (index in checkedAmenities.indices) {
            if (checkedAmenities[index]) {
                checkedAmenityNames.add(allAmenities[index])
            }
        }

        for (index in checkedOffers.indices) {
            if (checkedOffers[index]) {
                checkedOfferNames.add(allOffers[index])
            }
        }

      //  val serviceIds = getServiceIdsByTags(tags, pageNumber, pageSize, serviceIdSet)
        val serviceIds = getServiceIdsWithTags(tags, pageNumber, pageSize, initialLoadSize)
        val itemsList = mutableListOf<HomePagingItem>()

        Log.d("serviceIds", serviceIds.toString())

        for (serviceId in serviceIds) {
            if (includeStaycation) {
                val staycationDoc = staycationCollection.document(serviceId).get().await()
                val staycationImage = getServiceImages(serviceId, "Staycation")


                if (staycationDoc.exists() && staycationDoc.getString("hostId") != hostId) {
                    val staycationTitle = staycationDoc.getString("staycationTitle") ?: ""
                    val staycationType = staycationDoc.getString("staycationType") ?: ""
                    val staycationLocation = staycationDoc.getString("staycationLocation") ?: ""
                    val staycationCapacity = staycationDoc.getLong("noOfGuests")?.toInt() ?: 0
                    val staycationBeds = staycationDoc.getLong("noOfBeds")?.toInt() ?: 0
                    val staycationBedrooms = staycationDoc.getLong("noOfBedrooms")?.toInt() ?: 0
                    val staycationBathrooms = staycationDoc.getLong("noOfBathrooms")?.toInt() ?: 0
                    val staycationPrice = staycationDoc.getLong("staycationPrice")?.toInt() ?: 0
                    val bookings = getStaycationBookings(serviceId)
                    val amenities = getAmenities(serviceId, "Staycation")
                    val staycationAvailability = getStaycationAvailability(serviceId)

                    val inBetween = if (startDate != null && endDate != null) {
                        allDatesAvailable(
                            staycationAvailability.map { it.availableDate?.toDate() ?: Date(0) }.sorted(),
                            startDate,
                            endDate
                        )
                    } else {
                        true
                    }

                    val validReviews = bookings
                        .mapNotNull { it.bookingReview }
                        .filter { it.bookingId != "" }

                    val average = validReviews.map { it.rating }.average()
                    val averageReviewRating = if (average.isNaN()) 0.0 else average
                    val staycationAmenities = amenities.filter { checkedAmenityNames.contains(it.amenityName) }


                    if ((searchText == "" || staycationTitle.contains(searchText, ignoreCase = true)) &&
                        (city == "" || staycationLocation.contains(city, ignoreCase = true)) &&
                        (capacity == "" || staycationCapacity >= capacity.toInt()) &&
                        (bedroomCount == "Any" || bedroomCount.toInt() <= staycationBedrooms) &&
                        (bedCount == "Any" || bedCount.toInt() <= staycationBeds) &&
                        (bathroomCount == "Any" || bathroomCount.toInt() <= staycationBathrooms) &&
                        (staycationType == "House" && houseSelected ||
                                staycationType == "Apartment" && apartmentSelected ||
                                staycationType == "Condominium" && condoSelected ||
                                staycationType == "Camp" && campSelected ||
                                staycationType == "Guest House" && guestHouseSelected ||
                                staycationType == "Hotel" && hotelSelected) &&
                        averageReviewRating >= selectedRating &&
                        (minPrice == "" || staycationPrice >= minPrice.toInt()) &&
                        (maxPrice == "" || staycationPrice <= maxPrice.toInt()) &&
                        inBetween
                    // staycationAmenities.isNotEmpty()   UNCOMMENT THIS
                    ) {

                        val staycation = HomePagingItem(
                            serviceId = serviceId,
                            serviceCoverPhoto = staycationImage.find { it.photoType == "Cover" }?.photoUrl
                                ?: "https://upload.wikimedia.org/wikipedia/commons/thumb/3/3f/Placeholder_view_vector.svg/1022px-Placeholder_view_vector.svg.png",
                            serviceTitle = staycationTitle,
                            averageReviewRating = averageReviewRating,
                            location = staycationDoc.getString("staycationLocation") ?: "",
                            price = staycationDoc.getDouble("staycationPrice") ?: 0.0,
                            hostId = staycationDoc.getString("hostId") ?: "",
                            serviceType = "Staycation"
                        )
                        itemsList.add(staycation)
                    }
                }
            }

            if (includeTour) {
                val tourDoc = tourCollection.document(serviceId).get().await()
                val tourImage = getServiceImages(serviceId, "Tour")

                if (tourDoc.exists() && tourDoc.getString("hostId") != hostId) {
                    val tourTitle = tourDoc.getString("tourTitle") ?: ""
                    val tourType = tourDoc.getString("tourType") ?: ""
                    val tourLocation = tourDoc.getString("tourLocation") ?: ""
                    val tourPrice = tourDoc.getLong("tourPrice")?.toInt() ?: 0
                    val offers = getTourOffers(serviceId)
                    val tourAvailability = getTourAvailabilities(serviceId)

                    val tourOffers = offers.filter { checkedOfferNames.contains(it.typeOfOffer) }


                    val tourAvailabilityDates = tourAvailability.map { localDate ->
                        Date.from(localDate.date.atStartOfDay(ZoneId.systemDefault()).toInstant())
                    }.sorted()

                    val inBetween: Boolean = if (startDate != null && endDate != null) {
                        allDatesAvailable(
                            tourAvailabilityDates,
                            startDate,
                            endDate
                        )
                    } else {
                        true
                    }

                    if ((searchText == "" || tourTitle.contains(searchText, ignoreCase = true)) &&
                        (city == "" || tourLocation.contains(city, ignoreCase = true)) &&
                        (tourType == "Photo Tour" && photoTourSelected ||
                                tourType == "Food Trip" && foodTripSelected ||
                                tourType == "Bar Hopping" && barHoppingSelected) &&
                        (minPrice == "" || tourPrice >= minPrice.toInt()) &&
                        (maxPrice == "" || tourPrice <= maxPrice.toInt()) &&
                        tourOffers.isNotEmpty() &&
                        inBetween
                    ) {
                        val tour = HomePagingItem(
                            serviceId = serviceId,
                            serviceCoverPhoto = tourImage.find { it.photoType == "Cover" }?.photoUrl
                                ?: "https://upload.wikimedia.org/wikipedia/commons/thumb/3/3f/Placeholder_view_vector.svg/1022px-Placeholder_view_vector.svg.png",
                            serviceTitle = tourTitle,
                            averageReviewRating = tourDoc.getDouble("averageReviewRating") ?: 0.0,
                            location = tourDoc.getString("tourLocation") ?: "",
                            price = tourDoc.getDouble("tourPrice") ?: 0.0,
                            tourDuration = tourDoc.getString("tourDuration")?.toInt(),
                            hostId = tourDoc.getString("hostId") ?: "",
                            serviceType = "Tour"
                        )
                        itemsList.add(tour)
                    }
                }
            }
        }

        return itemsList
    }




//    suspend fun getAllServicesByTagsWithPaging(
//        hostId: String,
//        tags: List<String>,
//        pageNumber: Int,
//        pageSize: Int,
//        serviceIdSet: SortedSet<String>,
//
//        searchText: String,
//        includeStaycation: Boolean,
//        includeTour: Boolean,
//        houseSelected: Boolean,
//        apartmentSelected: Boolean,
//        condoSelected: Boolean,
//        campSelected: Boolean,
//        guestHouseSelected: Boolean,
//        hotelSelected: Boolean,
//        photoTourSelected: Boolean,
//        foodTripSelected: Boolean,
//        barHoppingSelected: Boolean,
//        selectedRating: Int,
//        minPrice: String,
//        maxPrice: String,
//        city: String,
//        capacity: String,
//        bedroomCount: String,
//        bedCount: String,
//        bathroomCount: String,
//        checkedAmenities: List<Boolean>,
//        checkedOffers: List<Boolean>,
//        startDate: Long? = null,
//        endDate: Long? = null
//
//    ): List<HomePagingItem> {
//
//        val allAmenities = listOf("Wifi", "TV", "Kitchen", "Washing machine", "Dedicated workspace" , "Pool", "Gym equipment", "Hot tub", "City view")
//        val allOffers = listOf("Food", "Souvenir", "Transportation", "Drinks")
//
//        val checkedAmenityNames = mutableListOf<String>()
//        val checkedOfferNames = mutableListOf<String>()
//
//        for (index in checkedAmenities.indices) {
//            if (checkedAmenities[index]) {
//                checkedAmenityNames.add(allAmenities[index])
//            }
//        }
//
//        for (index in checkedOffers.indices) {
//            if (checkedOffers[index]) {
//                checkedOfferNames.add(allOffers[index])
//            }
//        }
//
//      //  Log.d("Checked Amenity Names", checkedAmenityNames.toString())
//      //  Log.d("Checked Offer Names", checkedOfferNames.toString())
//
//        val serviceIds = getServiceIdsByTags(tags, pageNumber, pageSize, serviceIdSet)
//        val itemsList = mutableListOf<HomePagingItem>()
//
//       // Log.d("serviceIds", serviceIds.toString())
//
//        for (serviceId in serviceIds) {
//            if (includeStaycation) {
//                val staycationDoc = staycationCollection.document(serviceId.serviceId).get().await()
//                val staycationImage = getServiceImages(serviceId.serviceId, "Staycation")
//
//
//                if (staycationDoc.exists() && staycationDoc.getString("hostId") != hostId) {
//                    val staycationTitle = staycationDoc.getString("staycationTitle") ?: ""
//                    val staycationType = staycationDoc.getString("staycationType") ?: ""
//                    val staycationLocation = staycationDoc.getString("staycationLocation") ?: ""
//                    val staycationCapacity = staycationDoc.getLong("noOfGuests")?.toInt() ?: 0
//                    val staycationBeds = staycationDoc.getLong("noOfBeds")?.toInt() ?: 0
//                    val staycationBedrooms = staycationDoc.getLong("noOfBedrooms")?.toInt() ?: 0
//                    val staycationBathrooms = staycationDoc.getLong("noOfBathrooms")?.toInt() ?: 0
//                    val staycationPrice = staycationDoc.getLong("staycationPrice")?.toInt() ?: 0
//                    val bookings = getStaycationBookings(serviceId.serviceId)
//                    val amenities = getAmenities(serviceId.serviceId, "Staycation")
//                    val staycationAvailability = getStaycationAvailability(serviceId.serviceId)
//
//
//                //    Log.d("StaycationId", serviceId.serviceId)
////                    Log.d("Bookings", bookings.toString())
////                    Log.d("Staycation Type", staycationType)
//                 //   Log.d("Staycation Amenities From DB", amenities.map { it.amenityName }.toString())
//                //    Log.d("Staycation Availability", staycationAvailability.map { it.availableDate }.toString())
//                //    Log.d("Staycation Availability ID", staycationAvailability.map { it.staycationAvailabilityId }.toString())
////                    staycationAvailability.map { it.availableDate to it.staycationAvailabilityId }.forEach { (date, id) ->
////                        if (date != null) {
////                            Log.d("Staycation Availability", SimpleDateFormat("MMM d", Locale.getDefault()).format(truncateToDay(date.seconds * 1000L)) + " - " + id)
////                        }
////                    }
//
//                    val inBetween = if (startDate != null && endDate != null) {
//                        allDatesAvailable(
//                            staycationAvailability.map { it.availableDate?.toDate() ?: Date(0) }.sorted(),
//                            startDate,
//                            endDate
//                        )
//                    } else {
//                        true
//                    }
//
//                    //Log.d("Staycation In Between", inBetween.toString())
//
//                    val validReviews = bookings
//                        .mapNotNull { it.bookingReview }
//                        .filter { it.bookingId != "" }
//
//                    val average = validReviews.map { it.rating }.average()
//                    val averageReviewRating = if (average.isNaN()) 0.0 else average
//                    val staycationAmenities = amenities.filter { checkedAmenityNames.contains(it.amenityName) }
//
//                //    Log.d("Staycation Amenities After Filter", staycationAmenities.map { it.amenityName }.toString())
//
//                    if ((searchText == "" || staycationTitle.contains(searchText, ignoreCase = true)) &&
//                        (city == "" || staycationLocation.contains(city, ignoreCase = true)) &&
//                        (capacity == "" || staycationCapacity >= capacity.toInt()) &&
//                        (bedroomCount == "Any" || bedroomCount.toInt() <= staycationBedrooms) &&
//                        (bedCount == "Any" || bedCount.toInt() <= staycationBeds) &&
//                        (bathroomCount == "Any" || bathroomCount.toInt() <= staycationBathrooms) &&
//                        (staycationType == "House" && houseSelected ||
//                                staycationType == "Apartment" && apartmentSelected ||
//                                staycationType == "Condominium" && condoSelected ||
//                                staycationType == "Camp" && campSelected ||
//                                staycationType == "Guest House" && guestHouseSelected ||
//                                staycationType == "Hotel" && hotelSelected) &&
//                        averageReviewRating >= selectedRating &&
//                        (minPrice == "" || staycationPrice >= minPrice.toInt()) &&
//                        (maxPrice == "" || staycationPrice <= maxPrice.toInt()) &&
//                        inBetween
//                       // staycationAmenities.isNotEmpty()   UNCOMMENT THIS
//                    ) {
//
//                        val staycation = HomePagingItem(
//                            serviceId = serviceId.serviceId,
//                            serviceCoverPhoto = staycationImage.find { it.photoType == "Cover" }?.photoUrl
//                                ?: "https://upload.wikimedia.org/wikipedia/commons/thumb/3/3f/Placeholder_view_vector.svg/1022px-Placeholder_view_vector.svg.png",
//                            serviceTitle = staycationTitle,
//                            averageReviewRating = averageReviewRating,
//                            location = staycationDoc.getString("staycationLocation") ?: "",
//                            price = staycationDoc.getDouble("staycationPrice") ?: 0.0,
//                            hostId = staycationDoc.getString("hostId") ?: "",
//                            serviceType = "Staycation"
//                        )
//                        itemsList.add(staycation)
//                    }
//                }
//            }
//
//            if (includeTour) {
//                val tourDoc = tourCollection.document(serviceId.serviceId).get().await()
//                val tourImage = getServiceImages(serviceId.serviceId, "Tour")
//
//                if (tourDoc.exists() && tourDoc.getString("hostId") != hostId) {
//                    val tourTitle = tourDoc.getString("tourTitle") ?: ""
//                    val tourType = tourDoc.getString("tourType") ?: ""
//                    val tourLocation = tourDoc.getString("tourLocation") ?: ""
//                    val tourPrice = tourDoc.getLong("tourPrice")?.toInt() ?: 0
//                    val offers = getTourOffers(serviceId.serviceId)
//                    val tourAvailability = getTourAvailabilities(serviceId.serviceId)
//
//                    val tourOffers = offers.filter { checkedOfferNames.contains(it.typeOfOffer) }
//
////                    Log.d("Tour Type", tourType)
////                    Log.d("Tour Availablity", tourAvailability.map { it.date }.toString())
////
////                    Log.d("Tour Availablity After Format", tourAvailabilityDates.toString())
////
////                    Log.d("Tour Offers After Filter", tourOffers.map { it.typeOfOffer }.toString())
//
//                    val tourAvailabilityDates = tourAvailability.map { localDate ->
//                        Date.from(localDate.date.atStartOfDay(ZoneId.systemDefault()).toInstant())
//                    }.sorted()
//
//                    val inBetween: Boolean = if (startDate != null && endDate != null) {
//                        allDatesAvailable(
//                            tourAvailabilityDates,
//                            startDate,
//                            endDate
//                        )
//                    } else {
//                        true
//                    }
//
//                  //  Log.d("Tour In Between", inBetween.toString())
//
//                    if ((searchText == "" || tourTitle.contains(searchText, ignoreCase = true)) &&
//                        (city == "" || tourLocation.contains(city, ignoreCase = true)) &&
//                        (tourType == "Photo Tour" && photoTourSelected ||
//                                tourType == "Food Trip" && foodTripSelected ||
//                                tourType == "Bar Hopping" && barHoppingSelected) &&
//                        (minPrice == "" || tourPrice >= minPrice.toInt()) &&
//                        (maxPrice == "" || tourPrice <= maxPrice.toInt()) &&
//                        tourOffers.isNotEmpty() &&
//                        inBetween
//                    ) {
//                        val tour = HomePagingItem(
//                            serviceId = serviceId.serviceId,
//                            serviceCoverPhoto = tourImage.find { it.photoType == "Cover" }?.photoUrl
//                                ?: "https://upload.wikimedia.org/wikipedia/commons/thumb/3/3f/Placeholder_view_vector.svg/1022px-Placeholder_view_vector.svg.png",
//                            serviceTitle = tourTitle,
//                            averageReviewRating = tourDoc.getDouble("averageReviewRating") ?: 0.0,
//                            location = tourDoc.getString("tourLocation") ?: "",
//                            price = tourDoc.getDouble("tourPrice") ?: 0.0,
//                            tourDuration = tourDoc.getString("tourDuration")?.toInt(),
//                            hostId = tourDoc.getString("hostId") ?: "",
//                            serviceType = "Tour"
//                        )
//                        itemsList.add(tour)
//                    }
//                }
//            }
//        }
//
//        return itemsList
//    }

    private fun allDatesAvailable(availabilities: List<Date>, startDate: Long, endDate: Long): Boolean {
        val startDateTruncated = truncateToDay(startDate)
        val endDateTruncated = truncateToDay(endDate)


//        Log.d("startDateTruncated", SimpleDateFormat("MMM d", Locale.getDefault()).format(startDateTruncated) + " " + startDateTruncated.toString())
//        Log.d("endDateTruncated", SimpleDateFormat("MMM d", Locale.getDefault()).format(endDateTruncated) + " " + endDateTruncated.toString())

        // Iterate over each date between startDate and endDate
        var currentDate = startDateTruncated
        while (currentDate <= endDateTruncated) {
            // Check if the current date exists in staycationAvailability
            if (!availabilities.any { availability ->
                    val availabilityDate = availability.time
                    val availabilityDateTruncated = truncateToDay(availabilityDate)

                //    Log.d("availabilityDateTruncated", SimpleDateFormat("MMM d", Locale.getDefault()).format(availabilityDateTruncated) + " " + availabilityDateTruncated.toString())

                    availabilityDateTruncated == currentDate
                }) {
                // If any date is missing, return false
                return false
            }
            // Move to the next day
            currentDate += 24 * 60 * 60 * 1000 // Add one day in milliseconds
        }
        // If all dates are present, return true
        return true
    }


    private fun truncateToDay(timestamp: Long): Long {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = timestamp
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        return calendar.timeInMillis
    }

//    suspend fun getAllServicesByTags(tags: List<String>): List<HomePagingItem> {
//        return try {
//            val serviceIds = getServiceIdsByTags(tags)
//            Log.d("Service IDs: ", serviceIds.count().toString())
//
//            val itemsList = mutableListOf<HomePagingItem>()
//
//            for (serviceId in serviceIds) {
//                // Fetch staycation document
//                val staycationDoc = staycationCollection.document(serviceId.serviceId).get().await()
//                val staycationImage = getServiceImages(serviceId.serviceId, "Staycation")
//
//                // Check if staycation document is not empty before creating HomePagingItem
//                if (staycationDoc.exists()) {
//                    val staycation = HomePagingItem(
//                        serviceId = serviceId.serviceId,
//                        serviceCoverPhoto = staycationImage.find { it.photoType == "Cover" }?.photoUrl
//                            ?: "https://upload.wikimedia.org/wikipedia/commons/thumb/3/3f/Placeholder_view_vector.svg/1022px-Placeholder_view_vector.svg.png",
//                        serviceTitle = staycationDoc.getString("staycationTitle") ?: "",
//                        averageReviewRating = staycationDoc.getDouble("averageReviewRating") ?: 0.0,
//                        location = staycationDoc.getString("staycationLocation") ?: "",
//                        price = staycationDoc.getDouble("staycationPrice") ?: 0.0
//                    )
//                    itemsList.add(staycation)
//                }
//
//                // Fetch tour document
//                val tourDoc = tourCollection.document(serviceId.serviceId).get().await()
//                val tourImage = getServiceImages(serviceId.serviceId, "Tour")
//
//                // Check if tour document is not empty before creating HomePagingItem
//                if (tourDoc.exists()) {
//                    val tour = HomePagingItem(
//                        serviceId = serviceId.serviceId,
//                        serviceCoverPhoto = tourImage.find { it.photoType == "Cover" }?.photoUrl
//                            ?: "https://upload.wikimedia.org/wikipedia/commons/thumb/3/3f/Placeholder_view_vector.svg/1022px-Placeholder_view_vector.svg.png",
//                        serviceTitle = tourDoc.getString("tourTitle") ?: "",
//                        averageReviewRating = tourDoc.getDouble("averageReviewRating") ?: 0.0,
//                        location = tourDoc.getString("tourLocation") ?: "",
//                        price = tourDoc.getDouble("tourPrice") ?: 0.0,
//                        tourDuration = tourDoc.getString("tourDuration")?.toInt()
//                    )
//                    itemsList.add(tour)
//                }
//            }
//
//            itemsList
//        } catch (e: Exception) {
//            e.printStackTrace()
//            emptyList() // Handle the error case as needed
//        }
//    }


//    suspend fun getAllServicesByTags(tags: List<String>): List<HomePagingItem> {
//        return try {
//
//            val serviceIds = getServiceIdsByTags(tags)
//
//            Log.d("Service IDs: ", serviceIds.count().toString())
//
//            val itemsList = mutableListOf<HomePagingItem>()
//
//            for (serviceId in serviceIds) {
//                // Fetch staycation document
//                val staycationDoc = staycationCollection.document(serviceId.serviceId).get().await()
//                val staycationImage = getServiceImages(serviceId.serviceId, "Staycation")
//
//                // Check if staycation document is not empty before creating HomePagingItem
//                if (!staycationDoc.exists()) continue
//
//                val staycation = HomePagingItem(
//                    serviceId = serviceId.serviceId,
//                    serviceCoverPhoto = staycationImage.find { it.photoType == "Cover" }?.photoUrl
//                        ?: "https://upload.wikimedia.org/wikipedia/commons/thumb/3/3f/Placeholder_view_vector.svg/1022px-Placeholder_view_vector.svg.png",
//                    serviceTitle = staycationDoc.getString("staycationTitle") ?: "",
//                    averageReviewRating = staycationDoc.getDouble("averageReviewRating") ?: 0.0,
//                    location = staycationDoc.getString("staycationLocation") ?: "",
//                    price = staycationDoc.getDouble("staycationPrice") ?: 0.0
//                )
//                itemsList.add(staycation)
//
//                // Fetch tour document
//                val tourDoc = tourCollection.document(serviceId.serviceId).get().await()
//                val tourImage = getServiceImages(serviceId.serviceId, "Tour")
//
//                // Check if tour document is not empty before creating HomePagingItem
//                if (!tourDoc.exists()) continue
//
//                val tour = HomePagingItem(
//                    serviceId = serviceId.serviceId,
//                    serviceCoverPhoto = tourImage.find { it.photoType == "Cover" }?.photoUrl
//                        ?: "https://upload.wikimedia.org/wikipedia/commons/thumb/3/3f/Placeholder_view_vector.svg/1022px-Placeholder_view_vector.svg.png",
//                    serviceTitle = tourDoc.getString("tourTitle") ?: "",
//                    averageReviewRating = tourDoc.getDouble("averageReviewRating") ?: 0.0,
//                    location = tourDoc.getString("tourLocation") ?: "",
//                    price = tourDoc.getDouble("tourPrice") ?: 0.0,
//                    tourDuration = tourDoc.getString("tourDuration")?.toInt()
//                )
//                itemsList.add(tour)
//            }
//
//            itemsList
//
//        } catch (e: Exception) {
//            e.printStackTrace()
//            emptyList() // Handle the error case as needed
//        }
//    }

    // WORKING WITHOUT PAGING
//    suspend fun getAllServicesByTag(tag: String): List<HomePagingItem> {
//        return try {
//            val serviceIds = getServiceIdsByTag(tag)
//
//            Log.d("Service IDs: ", serviceIds.count().toString())
//
//            val itemsList = mutableListOf<HomePagingItem>()
//
//            for (serviceId in serviceIds) {
//                // Fetch staycation document
//                val staycationDoc = staycationCollection.document(serviceId.serviceId).get().await()
//                val staycationImage = getServiceImages(serviceId.serviceId, "Staycation")
//
//                // Check if staycation document is not empty before creating HomePagingItem
//                if (!staycationDoc.exists()) continue
//
//                val staycation = HomePagingItem(
//                    serviceId = serviceId.serviceId,
//                    serviceCoverPhoto = staycationImage.find { it.photoType == "Cover" }?.photoUrl
//                        ?: "https://upload.wikimedia.org/wikipedia/commons/thumb/3/3f/Placeholder_view_vector.svg/1022px-Placeholder_view_vector.svg.png",
//                    serviceTitle = staycationDoc.getString("staycationTitle") ?: "",
//                    averageReviewRating = staycationDoc.getDouble("averageReviewRating") ?: 0.0,
//                    location = staycationDoc.getString("staycationLocation") ?: "",
//                    price = staycationDoc.getDouble("staycationPrice") ?: 0.0
//                )
//                itemsList.add(staycation)
//
//                // Fetch tour document
//                val tourDoc = tourCollection.document(serviceId.serviceId).get().await()
//                val tourImage = getServiceImages(serviceId.serviceId, "Tour")
//
//                // Check if tour document is not empty before creating HomePagingItem
//                if (!tourDoc.exists()) continue
//
//                val tour = HomePagingItem(
//                    serviceId = serviceId.serviceId,
//                    serviceCoverPhoto = tourImage.find { it.photoType == "Cover" }?.photoUrl
//                        ?: "https://upload.wikimedia.org/wikipedia/commons/thumb/3/3f/Placeholder_view_vector.svg/1022px-Placeholder_view_vector.svg.png",
//                    serviceTitle = tourDoc.getString("tourTitle") ?: "",
//                    averageReviewRating = tourDoc.getDouble("averageReviewRating") ?: 0.0,
//                    location = tourDoc.getString("tourLocation") ?: "",
//                    price = tourDoc.getDouble("tourPrice") ?: 0.0,
//                    tourDuration = tourDoc.getString("tourDuration")?.toInt()
//                )
//                itemsList.add(tour)
//            }
//
//            Log.d("Services Fetched", "$itemsList")
//            itemsList
//
//        } catch (e: Exception) {
//            e.printStackTrace()
//            emptyList() // Handle the error case as needed
//        }
//    }



//    private suspend fun getServiceIdsByTag(tagName: String): List<Tag> {
//        return try {
//            val result = serviceTagCollection
//                .whereEqualTo("tagName", tagName)
//                .get()
//                .await()
//
//            val tags = mutableListOf<Tag>()
//
//            for (document in result.documents) {
//                val tagId = document.id
//                val serviceId = document.getString("serviceId") ?: ""
//
//                tags.add(Tag(tagId = tagId, tagName = tagName, serviceId = serviceId))
//            }
//
//            tags
//        } catch (e: Exception) {
//            e.printStackTrace()
//            emptyList() // Handle the error case as needed
//        }
//    }

//    suspend fun getAllStaycationsByTag(tab: String, page: Int): List<Staycation> {
//        return try {
//            val pageSize = Constants.PAGE_SIZE
//            // Fetch serviceIds with the specified tag
//            val serviceIds = getServiceIdsByTag(tab)
//
//            // Fetch staycations with document id (staycationId) equal to the serviceId
//            val staycationList = mutableListOf<Staycation>()
//
//            val startIdx = page * pageSize
//            val endIdx = startIdx + pageSize
//
//            for (i in startIdx until min(endIdx, serviceIds.size)) {
//                val serviceId = serviceIds[i].serviceId
//                val document = db.collection("staycation").document(serviceId).get().await()
//
//                if (document.exists()) {
//                    val staycationId = document.id
//                    val staycation = createStaycationFromDocument(document, staycationId)
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
        val staycationLat = document.getDouble("staycationLat") ?: 0.0
        val staycationLng = document.getDouble("staycationLng") ?: 0.0
        val staycationPrice = document.getDouble("staycationPrice") ?: 0.0
        val staycationSpace = document.getString("staycationSpace") ?: ""
        val staycationTitle = document.getString("staycationTitle") ?: ""
        val staycationType = document.getString("staycationType") ?: ""
        val hasSecurityCamera = document.getBoolean("hasSecurityCamera") ?: false
        val hasWeapon = document.getBoolean("hasWeapon") ?: false
        val hasDangerousAnimal = document.getBoolean("hasDangerousAnimal") ?: false
        val hasFirstAid = document.getBoolean("hasFirstAid") ?: false
        val hasFireExit = document.getBoolean("hasFireExit") ?: false
        val hasFireExtinguisher = document.getBoolean("hasFireExtinguisher") ?: false
        val maxNoOfGuests = document.getLong("maxNoOfGuests")?.toInt() ?: 0
        val additionalFeePerGuest = document.getDouble("additionalFeePerGuest") ?: 0.0
        val noisePolicy = document.getBoolean("noisePolicy") ?: false
        val allowSmoking = document.getBoolean("allowSmoking") ?: false
        val allowPets = document.getBoolean("allowPets") ?: false
        val additionalInfo = document.getString("additionalInfo") ?: ""
        val noCancel = document.getBoolean("noCancel") ?: false
        val noReschedule = document.getBoolean("noCancel") ?: false
        val phoneNo = document.getString("phoneNo") ?: ""
        val email = document.getString("email") ?: ""

        val touristInfo = getHostInfo(hostId)
        val staycationImages = getServiceImages(staycationId, "Staycation")
        val staycationTags = getServiceTags(staycationId, "Staycation")
        val promotions = getPromotions(staycationId, "Staycation")
        val availability = getStaycationAvailability(staycationId)
        val amenities = getAmenities(staycationId, "Staycation")
        val bookings = getStaycationBookings(staycationId)
        return Staycation(
            staycationId = staycationId,
            host = Host(
                profilePicture = touristInfo?.profilePicture ?: "",
                firstName = touristInfo?.firstName ?: "",
                middleName = touristInfo?.middleName ?: "",
                lastName = touristInfo?.lastName ?: "",
                touristId = touristInfo?.touristId ?: "",
                hostId = "HOST-${touristInfo?.touristId}"
            ),
            noOfGuests = noOfGuests,
            noOfBedrooms = noOfBedrooms,
            noOfBeds = noOfBeds,
            noOfBathrooms = noOfBathrooms,
            staycationDescription = staycationDescription,
            staycationLocation = staycationLocation,
            staycationLat = staycationLat,
            staycationLng = staycationLng,
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
            hasFirstAid = hasFirstAid,
            hasFireExit = hasFireExit,
            hasFireExtinguisher = hasFireExtinguisher,
            maxNoOfGuests = maxNoOfGuests,
            additionalFeePerGuest = additionalFeePerGuest,
            additionalInfo = additionalInfo,
            noisePolicy = noisePolicy,
            allowPets = allowPets,
            allowSmoking = allowSmoking,
            noReschedule = noReschedule,
            noCancel = noCancel,
            phoneNo = phoneNo,
            email = email,

            //averageReviewRating = bookings.calculateAverageReviewRating()
        )
    }

//    suspend fun getServiceForHome(): List<HomePagingItem> {
//        val serviceDocs = serviceCollection
//           // .limit(20)
//            .get()
//            .await()
//
//        val staycationList = mutableListOf<HomePagingItem>()
//
//        for (doc in serviceDocs) {
//            val serviceId = doc.getString("serviceId") ?: ""
//            val staycationDoc = staycationCollection.document(serviceId).get().await()
//
//            val staycation = HomePagingItem(
//                serviceId = serviceId,
//                serviceTitle = staycationDoc.getString("serviceTitle") ?: "",
//                averageReviewRating = staycationDoc.getDouble("averageReviewRating") ?: 0.0,
//                location = staycationDoc.getString("location") ?: "",
//                price = staycationDoc.getDouble("price") ?: 0.0,
//                tourDuration = staycationDoc.getLong("tourDuration")?.toInt()
//            )
//
//            staycationList.add(staycation)
//        }
//
//        return staycationList
//    }

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
                val staycationLat = document.getDouble("staycationLat") ?: 0.0
                val staycationLng = document.getDouble("staycationLng") ?: 0.0
                val staycationPrice = document.getDouble("staycationPrice") ?: 0.00
                val staycationSpace = document.getString("staycationSpace") ?: ""
                val staycationTitle = document.getString("staycationTitle") ?: ""
                val staycationType = document.getString("staycationType") ?: ""
                val hasSecurityCamera = document.getBoolean("hasSecurityCamera") ?: false
                val hasWeapon = document.getBoolean("hasWeapon") ?: false
                val hasDangerousAnimal = document.getBoolean("hasDangerousAnimal") ?: false

                val hasFirstAid = document.getBoolean("hasFirstAid") ?: false
                val hasFireExit = document.getBoolean("hasFireExit") ?: false
                val hasFireExtinguisher = document.getBoolean("hasFireExtinguisher") ?: false
                val maxNoOfGuests = document.getLong("maxNoOfGuests")?.toInt() ?: 0
                val additionalFeePerGuest = document.getDouble("additionalFeePerGuest") ?: 0.0
                val noisePolicy = document.getBoolean("noisePolicy") ?: false
                val allowSmoking = document.getBoolean("allowSmoking") ?: false
                val allowPets = document.getBoolean("allowPets") ?: false
                val additionalInfo = document.getString("additionalInfo") ?: ""
                val noCancel = document.getBoolean("noCancel") ?: false
                val noReschedule = document.getBoolean("noCancel") ?: false
                val phoneNo = document.getString("phoneNo") ?: ""
                val email = document.getString("email") ?: ""

                val touristInfo = getHostInfo(hostId)
                val staycationImages = getServiceImages(staycationId, "Staycation")
                val staycationTags = getServiceTags(staycationId, "Staycation")
                val promotions = getPromotions(staycationId, "Staycation")
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
                    staycationLat = staycationLat,
                    staycationLng = staycationLng,
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
                    hasFirstAid = hasFirstAid,
                    hasFireExit = hasFireExit,
                    hasFireExtinguisher = hasFireExtinguisher,
                    maxNoOfGuests = maxNoOfGuests,
                    additionalFeePerGuest = additionalFeePerGuest,
                    additionalInfo = additionalInfo,
                    noisePolicy = noisePolicy,
                    allowPets = allowPets,
                    allowSmoking = allowSmoking,
                    noReschedule = noReschedule,
                    noCancel = noCancel,
                    phoneNo = phoneNo,
                    email = email,
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

    suspend fun getStaycationBookingByBookingId(staycationBookingId: String): StaycationBooking? {
        return try {
            val document = staycationBookingCollection.document(staycationBookingId).get().await()

            if (document.exists()) {
                val bookingDate = document.getTimestamp("bookingDate")?.toDate() ?: Date()
                val bookingStatus = document.getString("bookingStatus") ?: ""
                val checkInDate = document.getTimestamp("checkInDate")?.toDate() ?: Date()
                val checkOutDate = document.getTimestamp("checkOutDate")?.toDate() ?: Date()
                val noOfGuests = document.getLong("noOfGuests")?.toInt() ?: 0
                val noOfInfants = document.getLong("noOfInfants")?.toInt() ?: 0
                val noOfPets = document.getLong("noOfPets")?.toInt() ?: 0
                val staycationId = document.getString("staycationId") ?: ""
                val totalAmount = document.getDouble("totalAmount") ?: 0.0
                val touristId = document.getString("touristId") ?: ""

                val staycation = getStaycationDetailsById(staycationId) ?: Staycation()

                // Construct a StaycationBooking object
                StaycationBooking(
                    staycationBookingId = staycationBookingId,
                    bookingDate = bookingDate,
                    bookingStatus = bookingStatus,
                    checkInDate = checkInDate,
                    checkOutDate = checkOutDate,
                    noOfGuests = noOfGuests,
                    noOfPets = noOfPets,
                    noOfInfants = noOfInfants,
                    totalAmount = totalAmount,
                    tourist = Tourist(touristId = touristId),
                    staycation = staycation
                )
            } else {
                null // Document doesn't exist, return null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null // Handle the error case as needed
        }
    }



    private suspend fun getStaycationBookings(staycationId: String): List<StaycationBooking> {
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


    private suspend fun getServiceTags(serviceId: String, serviceType: String): List<Tag> {
        return try {
            val result = serviceTagCollection
                .whereEqualTo("serviceId", serviceId)
                .whereEqualTo("serviceType", serviceType)
                .get()
                .await()

            val serviceTags = mutableListOf<Tag>()

            for (document in result.documents) {
                val tagId = document.id
                val tagName = document.getString("tagName") ?: ""
                val serviceId = serviceId

                serviceTags.add(Tag(tagId = tagId, tagName = tagName, serviceId = serviceId))
            }

            serviceTags

        } catch (e: Exception) {
            e.printStackTrace()
            emptyList() // Handle the error case as needed
        }
    }


    private suspend fun getPromotions(serviceId: String, serviceType: String): List<Promotion> {
        return try {
            val result = servicePromotionCollection
                .whereEqualTo("serviceId", serviceId)
                .whereEqualTo("serviceType", serviceType)
                .get()
                .await()

            val promotions = mutableListOf<Promotion>()

            for (document in result.documents) {
                val servicePromotionId = document.id
                val promoId = document.getString("promoId") ?: ""

                // Fetch promotion details from the promotion collection using promoId
                val promoDetails = getPromoDetails(promoId)

                // Construct Promotion object
                val promotion = Promotion(
                    servicePromotionId = servicePromotionId,
                    promoId = promoId,
                    description = promoDetails?.description ?: "",
                    discount = promoDetails?.discount ?: 0.0,
                    promoName = promoDetails?.promoName ?: "",
                    status = promoDetails?.status ?: ""
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

    private suspend fun getStaycationAvailability(staycationId: String): List<StaycationAvailability> {
        return try {
            val result = staycationAvailabilityCollection
                .whereEqualTo("staycationId", staycationId)
                .get()
                .await()

            val availability = mutableListOf<StaycationAvailability>()

            for (document in result.documents) {
                val availabilityId = document.id
                val availableDateTimestamp = document.getTimestamp("availableDate") ?: Timestamp.now()

                val staycationAvailability = StaycationAvailability(
                    staycationAvailabilityId = availabilityId,
                    availableDate = availableDateTimestamp,
                )

                availability.add(staycationAvailability)
            }

            availability

        } catch (e: Exception) {
            e.printStackTrace()
            emptyList() // Handle the error case as needed
        }
    }

    private suspend fun getAmenities(serviceId: String, serviceType: String): List<Amenity> {
        return try {
            val result = serviceAmenityCollection
                .whereEqualTo("serviceId", serviceId)
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
            val hostId = "HOST-$touristId"
            val hostData = hashMapOf(
                "touristId" to touristId
            )
            hostCollection.document("HOST-$touristId").set(hostData).await()
            createHostWallet(hostId)
            createTouristWallet(touristId)
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

@Preview
@Composable
private fun QueryTests() {

    val checkInDateMillis = 1709877600000
    val checkOutDateMillis = 1710043200000

    val calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8")) // Create a calendar with UTC time zone
    calendar.timeInMillis = checkInDateMillis // Set the check-in date
    calendar.add(Calendar.HOUR_OF_DAY, -14) // Subtract 14 hours

    val startDate = calendar.time // Get the updated start date

    calendar.timeInMillis = checkOutDateMillis // Set the check-out date
    calendar.add(Calendar.HOUR_OF_DAY, -12) // Subtract 12 hours

    val endDate = calendar.time // Get the updated end date

    val dateRange = (startDate.time..endDate.time).step(TimeUnit.DAYS.toMillis(1))
        .map { Date(it) }

// Create availability records for each date in the range
    for (date in dateRange) {
        println(date)
    }
}