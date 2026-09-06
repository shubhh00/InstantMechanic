package com.app.data.local.serviceRequest

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ServiceRequestDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertRequest(
        request: ServiceRequestEntity
    )

    @Query(
        """
        SELECT * FROM service_requests
        ORDER BY createdAtEpochMillis DESC
        """
    )
    fun observeRequests(): Flow<List<ServiceRequestEntity>>

    @Query(
        """
        SELECT * FROM service_requests
        WHERE syncStatus != :syncedStatus
        ORDER BY createdAtEpochMillis ASC
        """
    )
    suspend fun getUnsyncedRequests(
        syncedStatus: RequestSyncStatus
    ): List<ServiceRequestEntity>

    @Query(
        """
        UPDATE service_requests
        SET syncStatus = :syncStatus,
            lastSyncError = :errorMessage
        WHERE requestId = :requestId
        """
    )
    suspend fun updateSyncStatus(
        requestId: String,
        syncStatus: RequestSyncStatus,
        errorMessage: String?
    ): Int
}