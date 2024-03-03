package com.itenirary.utils

import android.app.Dialog
import android.content.Context
import android.icu.text.SimpleDateFormat
import androidx.appcompat.app.AlertDialog
import androidx.compose.ui.text.TextStyle
import com.example.tripnila.R
import com.google.firebase.firestore.FirebaseFirestore
import com.itenirary.rv.rv_days_data
import java.text.NumberFormat
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Date
import java.util.Locale

class utils {
    var dialog: Dialog? = null

    fun showProgress(myContext: Context) {
        var builder = AlertDialog.Builder(myContext)
        builder.setView(R.layout.progress)
        dialog = builder.create()
        (dialog as AlertDialog).setCancelable(false)
        (dialog as AlertDialog).show()
    }

    fun hideProgress() {
        dialog?.dismiss()
    }

    fun isStartTimeBeforeEndTime(startTime: String, endTime: String): Boolean {
        val dateFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())

        val startTimeDate = dateFormat.parse(startTime)
        val endTimeDate = dateFormat.parse(endTime)

        return startTimeDate < endTimeDate
    }

    fun formatDateRange(startDate: String, endDate: String): String {
        val dateFormat = SimpleDateFormat("MMMM dd, yyyy", Locale.ENGLISH)

        val startDateParsed = dateFormat.parse(startDate)
        val endDateParsed = dateFormat.parse(endDate)

        val startMonthDay = SimpleDateFormat("MMM dd", Locale.ENGLISH).format(startDateParsed)
        val endMonthDay = SimpleDateFormat("MMM dd", Locale.ENGLISH).format(endDateParsed)

        return "$startMonthDay - $endMonthDay"
    }

    fun convert12HourTo24Hour(time12Hour: String): String {
        val inputFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        val outputFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

        val date = inputFormat.parse(time12Hour)

        println(time12Hour)
        println(outputFormat.format(date))

        return outputFormat.format(date)
    }


    fun isEndTimeGreaterThanStartTime(startTime: String, endTime: String): Boolean {
        return startTime < endTime
    }

    fun countDaysBetween(startDate: Date, endDate: Date): Long {
        val startLocalDate = LocalDateTime.ofInstant(startDate.toInstant(), ZoneId.of("Asia/Manila")).toLocalDate()
        val endLocalDate = LocalDateTime.ofInstant(endDate.toInstant(), ZoneId.of("Asia/Manila")).toLocalDate()

        return ChronoUnit.DAYS.between(startLocalDate, endLocalDate)
    }

    fun formatNumber(number: Double, decimalPlaces: Int): String {
        val format = NumberFormat.getInstance(Locale.US)
        format.minimumFractionDigits = decimalPlaces
        format.maximumFractionDigits = decimalPlaces

        return format.format(number)
    }

    fun convertTo24HourFormat(time12Hour: String): Int {
        val formatter12Hour = DateTimeFormatter.ofPattern("hh:mm a")
        val formatter24Hour = DateTimeFormatter.ofPattern("HH:mm")

        // Parse the input time with case-insensitive formatter
        val time = LocalTime.parse(time12Hour, formatter12Hour.withLocale(Locale.ENGLISH))
        return time.hour
    }

    fun isStoreOpenDuringYourSchedule(
        storeOpeningTime: LocalTime,
        storeClosingTime: LocalTime,
        yourScheduledStartTime: LocalTime,
        yourScheduledEndTime: LocalTime
    ): Boolean {
        return (yourScheduledStartTime >= storeOpeningTime && yourScheduledStartTime < storeClosingTime) &&
                (yourScheduledEndTime > storeOpeningTime && yourScheduledEndTime <= storeClosingTime)
    }


    fun countIntervals(startTime: LocalTime, endTime: LocalTime, intervalSize: Int): Long {
        val duration = ChronoUnit.HOURS.between(startTime, endTime)
        return duration / intervalSize
    }

    fun convertTimeFormat(timeString: String): String? {
        val h_mm_a = SimpleDateFormat("h:mm a", Locale.ENGLISH)
        val hh_mm_ss = SimpleDateFormat("HH:mm:ss")

        return try {
            val parsedDate = h_mm_a.parse(timeString)
            hh_mm_ss.format(parsedDate!!)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}