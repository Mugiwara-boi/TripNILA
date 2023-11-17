package com.example.tripnila.data

data class Tourist(
    val touristId: String = "",
    val firstName: String = "",
    val middleName: String = "",
    val lastName: String = "",
    val username: String = "",
    val password: String = "",
    var preferences: List<Preference> = emptyList()
)