package com.app.data

data class MechanicDto(
    val name: String? = null,
    val rating: Double? = null,
    val reviewCount: Int? = null,
    val distanceKm: Double? = null,
    val locality: String? = null,
    val address: String? = null,
    val phone: String? = null,
    val openTime: String? = null,
    val closeTime: String? = null,
    val services: List<String>? = null
)