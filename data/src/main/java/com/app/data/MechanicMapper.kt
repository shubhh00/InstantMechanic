package com.app.data

import android.R.attr.phoneNumber
import com.app.domain.model.Mechanic

fun MechanicDto.toDomain(): Mechanic? {

    val id = id ?: return null
    val name = name ?: return null

    return Mechanic(
        id = id,
        name = name,
        rating = rating ?: 0.0,
        distance = distance.orEmpty(),
        location = location.orEmpty(),
        address = address.orEmpty(),
        services = services.orEmpty(),
        isOpen = false,
        workingHours = workingHours.orEmpty(),
        phoneNumber = phoneNumber.orEmpty(),
        imageUrl = imageUrl.orEmpty()
    )
}