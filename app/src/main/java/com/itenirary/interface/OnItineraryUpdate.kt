package com.itenirary.`interface`

import com.itenirary.firebaseclass.instruction

interface OnItineraryUpdate {
    fun onUpdate(position: Int, model: instruction)
}