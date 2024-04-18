package com.example.tripnila.data

import java.time.LocalDate


data class Tour1(
    val tourId: String = "",
    val tourContact: String = "",
    val tourDescription: String = "",
    val tourDuration: String = "",
    val tourEmail: String = "",
    val tourFacebook: String = "",
    val tourInstagram: String = "",
    val tourLanguage: String = "",
    val tourLat: Double = 0.0,
    val tourLng: Double = 0.0,
    val tourLocation: String = "",
    val tourPrice: Double = 0.0,
    val tourTitle: String = "",
    val tourType: String = "",
    val hostId: String = ""
)
data class Tour(

    val tourId: String = "",
    val host: Host = Host(),
    val tourTags: List<Tag> = emptyList(),
    val tourTitle: String = "",
    val tourDescription: String = "",
    val tourType: String = "",
    val tourLat: Double = 0.0,
    val tourLng: Double = 0.0,
    val tourLocation: String = "",
    val tourDuration: String = "",
    val tourLanguage: String = "",
    val tourContact: String = "",
    val tourEmail: String = "",
    val tourFacebook: String = "",
    val tourInstagram: String = "",
    val additionalInfo: String = "",
    val tourPrice: Double = 0.0,
    val offers: List<Offer> = emptyList(),
    val tourImages: List<Photo> = emptyList(),
    val tourMenu: List<Photo> = emptyList(),
    val promotions: List<Promotion> = emptyList(),
    val schedule: List<TourSchedule> = emptyList(),
    val reviews: List<Review> = emptyList(),
    val hostId: String = "",

)

data class Offer(
    val tourOfferId: String = "",
    val tourId: String = "",
    val typeOfOffer: String = "",
    val offer: String = "",
)

data class TourSchedule(
    val tourScheduleId: String = "",
    val date: LocalDate = LocalDate.now(),
    val startTime: String = "00:00 AM",
    val endTime: String = "00:00 PM",
    val slot: Int,
    val bookedSlot: Int
)