package com.example.tripnila.data

data class Tour(

    val tourId: String = "",
    val host: Host = Host(),
    val tourTags: List<Tag> = emptyList(),
    val tourTitle: String = "",
    val tourDescription: String = "",
    val tourType: String = "",
    val tourLocation: String = "",
    val tourDuration: Int = 0,
    val tourLanguage: String = "",
    val tourContact: String = "",
    val tourEmail: String = "",
    val tourFacebook: String = "",
    val tourInstagram: String = "",
    val additionalInfo: String = "",
    val tourPrice: Double = 0.0,
    val amenities: List<Amenity> = emptyList(),
    val tourImages: List<Photo> = emptyList(),
    val tourMenu: List<Photo> = emptyList(),
    val promotions: List<Promotion> = emptyList(),
    val schedule: List<DailySchedule> = emptyList(),
    val reviews: List<Review> = emptyList(),

)
