package com.app.data

data class MechanicDto(
    val id: String? = null,
    val name: String? = null,
    val rating: Double? = null,
    val distance: String? = null,
    val location: String? = null,
    val address: String? = null,
    val services: List<String>? = null,
    val workingHours: String? = null,
    val phoneNumber: String? = null,
    val imageUrl: String? = null
)