package com.app.domain.model

data class Mechanic(
    val id: String = "",
    val name: String = "",
    val rating: Double = 0.0,
    val distance: String = "",
    val location: String = "",
    val address: String = "",
    val services: List<String> = emptyList(),
    val isOpen: Boolean = false,
    val workingHours: String = "",
    val phoneNumber: String = "",
    val imageUrl: String = ""
)