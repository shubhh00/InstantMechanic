package com.app.domain.model

import java.time.LocalTime
import java.time.format.DateTimeFormatter

fun Mechanic.isOpenNow(): Boolean {
    val formatter = DateTimeFormatter.ofPattern("HH:mm")

    val now = LocalTime.now()
    val open = LocalTime.parse(openTime, formatter)
    val close = LocalTime.parse(closeTime, formatter)

    return !now.isBefore(open) && now.isBefore(close)
}