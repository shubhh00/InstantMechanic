package com.app.data.mapper

import com.app.data.local.serviceRequest.RequestSyncStatus
import com.app.data.local.serviceRequest.ServiceRequestEntity
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

fun ServiceRequest.toEntity(
    requestId: String,
    createdAtEpochMillis: Long
): ServiceRequestEntity {
    return ServiceRequestEntity(
        requestId = requestId,
        mechanicId = mechanicId,
        customerName = customerName,
        phoneNumber = phoneNumber,
        vehicleNumber = vehicleNumber,
        selectedService = selectedService,
        problemDescription = problemDescription,
        syncStatus = RequestSyncStatus.PENDING,
        createdAtEpochMillis = createdAtEpochMillis,
        lastSyncError = null
    )
}

fun ServiceRequestEntity.toDto(): ServiceRequestDto {
    return ServiceRequestDto(
        mechanicId = mechanicId,
        customerName = customerName,
        phoneNumber = phoneNumber,
        vehicleNumber = vehicleNumber,
        selectedService = selectedService,
        problemDescription = problemDescription
    )
}