package com.app.data.serviceRequest

data class ServiceRequestDto(
    val mechanicId: String = "",
    val customerName: String = "",
    val phoneNumber: String = "",
    val vehicleNumber: String = "",
    val selectedService: String = "",
    val problemDescription: String = ""
)