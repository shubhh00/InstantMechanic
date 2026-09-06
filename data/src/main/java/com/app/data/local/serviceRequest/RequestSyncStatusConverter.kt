package com.app.data.local.serviceRequest

import androidx.room.TypeConverter

class RequestSyncStatusConverter {

    @TypeConverter
    fun fromSyncStatus(
        status: RequestSyncStatus
    ): String {
        return status.name
    }

    @TypeConverter
    fun toSyncStatus(
        value: String
    ): RequestSyncStatus {
        return RequestSyncStatus.entries
            .firstOrNull { status ->
                status.name == value
            }
            ?: RequestSyncStatus.FAILED
    }
}