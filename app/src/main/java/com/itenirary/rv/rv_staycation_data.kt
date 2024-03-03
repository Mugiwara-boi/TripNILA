package com.itenirary.rv

import java.util.Date

data class rv_staycation_data(
    val hotelName: String,
    val displaySchedule: String,
    val hostId: String,
    val stayInDate: Date,
    val stayOutDate: Date,
    val firstName :String,
    val address: String,
    val stayCationId: String,
    val staycationLat: Double,
    val staycationLng: Double
)
