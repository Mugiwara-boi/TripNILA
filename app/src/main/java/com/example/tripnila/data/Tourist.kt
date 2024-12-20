package com.example.tripnila.data

data class Tourist(
    val touristId: String = "",
    val uid: String = "",
    val firstName: String = "",
    val middleName: String = "",
    val lastName: String = "",
    val username: String = "",
    val password: String = "",
    var preferences: List<Preference> = emptyList(),
    val profilePicture: String = "https://upload.wikimedia.org/wikipedia/commons/8/89/Portrait_Placeholder.png",
)

data class TouristWallet(
    val currentBalance : Double = 0.0,
    val touristId: String = "",
    val paypalBalance: Double = 0.0,
    val paymayaBalance: Double = 0.0,
    val gcashBalance: Double = 0.0,
    val pendingBalance: Double = 0.0,
)