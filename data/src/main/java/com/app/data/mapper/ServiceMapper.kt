package com.app.data.mapper

import com.app.data.serviceRequest.ServiceRequestDto
import com.app.domain.model.ServiceRequest

fun ServiceRequest.toDto(): ServiceRequestDto {
    return ServiceRequestDto(
        mechanicId = mechanicId,
        customerName = customerName,
        phoneNumber = phoneNumber,
        vehicleNumber = vehicleNumber,
        selectedService = selectedService,
        problemDescription = problemDescription
    )
}