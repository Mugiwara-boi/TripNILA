package com.example.tripnila.data

data class HostProperty(
    val propertyName: String,
    val propertyDescription: String = "",
    val image: Int,
    val host: String,
    val location: String
)
