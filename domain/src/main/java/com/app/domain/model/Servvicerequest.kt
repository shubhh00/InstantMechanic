package com.app.domain.model

data class ServiceRequest(
    val mechanicId: String,
    val customerName: String,
    val phoneNumber: String,
    val vehicleNumber: String,
    val selectedService: String,
    val problemDescription: String
)