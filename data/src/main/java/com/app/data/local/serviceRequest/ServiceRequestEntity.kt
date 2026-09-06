package com.app.data.local.serviceRequest

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

enum class RequestSyncStatus {
    PENDING,
    SYNCED,
    FAILED
}

@Entity(
    tableName = "service_requests",
    indices = [
        Index(value = ["syncStatus"])
    ]
)
data class ServiceRequestEntity(
    @PrimaryKey
    val requestId: String,
    val mechanicId: String,
    val customerName: String,
    val phoneNumber: String,
    val vehicleNumber: String,
    val selectedService: String,
    val problemDescription: String,
    val syncStatus: RequestSyncStatus,
    val createdAtEpochMillis: Long,
    val lastSyncError: String? = null
)