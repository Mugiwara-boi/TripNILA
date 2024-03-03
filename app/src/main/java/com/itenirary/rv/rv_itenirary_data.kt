package com.itenirary.rv

import com.itenirary.firebaseclass.instruction

data class rv_itenirary_data(
    val businessId: String,
    var businessName: String,
    val distance: String,
    val category: String,
    val time: String,
    val selectedLat: Double,
    val selectedLng: Double,
    val businessLat: Double,
    val businessLng: Double,
    val amount: String,
    val day: String,
    val arrivalTime: String,
    var instructionList: ArrayList<instruction>?
)
