package com.app.data.mapper

import com.app.data.MechanicDto
import com.app.domain.model.Mechanic

fun MechanicDto.toDomain(id: String): Mechanic? {
    val name = name ?: return null

    return Mechanic(
        id = id,
        name = name,
        rating = rating ?: 0.0,
        reviewCount = reviewCount ?: 0,
        distanceKm = distanceKm ?: 0.0,
        location = locality.orEmpty(),
        address = address.orEmpty(),
        services = services.orEmpty(),
        openTime = openTime.orEmpty(),
        closeTime = closeTime.orEmpty(),
        phoneNumber = phone.orEmpty()
    )
}