package com.example.tripnila.data

data class Business (
    val businessId: String = "",
    val host: Host = Host(),
    val businessTags: List<Tag> = emptyList(),
    val businessTitle: String = "",
    val businessDescription: String = "",
    val businessType: String = "",
    val businessLat: Double = 0.0,
    val businessLng: Double = 0.0,
    val businessLocation: String = "",
    val businessContact: String = "",
    val businessEmail: String = "",
    val businessURL: String = "",
    val minSpend: Double = 0.0,
    val entranceFee: Double = 0.0,
    val additionalInfo: String = "",
    val cheapestPrice: Double = 0.0,
    val mostExpensivePrice: Double = 0.0,
    val amenities: List<Amenity> = emptyList(),
    val businessImages: List<Photo> = emptyList(),
    val businessMenu: List<Photo> = emptyList(),
    val promotions: List<Promotion> = emptyList(),
    val schedule: List<DailySchedule> = emptyList(),
    val reviews: List<Review> = emptyList(),
)

data class BusinessViews(
    val month: Int = 0,
    val serviceId: String = "",
    val viewCount: Int = 0,
)


