package com.app.domain.model

data class Mechanic(
    val id: String = "",
    val name: String = "",
    val rating: Double = 0.0,
    val reviewCount: Int = 0,
    val distanceKm: Double = 0.0,
    val location: String = "",
    val address: String = "",
    val services: List<String> = emptyList(),
    val openTime: String = "",
    val closeTime: String = "",
    val phoneNumber: String = ""
)