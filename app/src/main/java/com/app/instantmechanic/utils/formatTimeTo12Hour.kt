package com.app.instantmechanic.utils


import java.time.LocalTime
import java.time.format.DateTimeFormatter

fun formatTimeTo12Hour(time: String): String {
    return try {
        val inputFormatter = DateTimeFormatter.ofPattern("HH:mm")
        val outputFormatter = DateTimeFormatter.ofPattern("h:mm a")

        LocalTime.parse(time, inputFormatter)
            .format(outputFormatter)
    } catch (e: Exception) {
        time
    }
}