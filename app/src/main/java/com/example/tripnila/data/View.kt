package com.example.tripnila.data

import java.time.LocalDate
import java.util.Calendar
import java.util.Date
import java.util.Locale

data class View(
    val id: String,
    val month: Int,
    val views: Int
) {
    val date: Date = Calendar.getInstance().apply {
        set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR))
        set(Calendar.MONTH, month - 1) // Calendar months are zero-based
        set(Calendar.DAY_OF_MONTH, 1) // Set to the first day of the month
    }.time

    fun getMonthName(): String {
        val calendar = Calendar.getInstance()
        calendar.time = date
        return calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())
    }
}