package com.app.data.mapper

import com.app.data.local.mechanic.MechanicEntity
import com.app.data.mechanic.MechanicDto
import com.app.domain.model.Mechanic

fun MechanicDto.toEntity(id: String): MechanicEntity? {
    val mechanicName = name ?: return null

    return MechanicEntity(
        id = id,
        name = mechanicName,
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

fun MechanicEntity.toDomain(): Mechanic {
    return Mechanic(
        id = id,
        name = name,
        rating = rating,
        reviewCount = reviewCount,
        distanceKm = distanceKm,
        location = location,
        address = address,
        services = services,
        openTime = openTime,
        closeTime = closeTime,
        phoneNumber = phoneNumber
    )
}