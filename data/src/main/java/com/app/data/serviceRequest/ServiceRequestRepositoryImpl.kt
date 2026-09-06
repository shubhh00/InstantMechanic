package com.app.data.serviceRequest

import com.app.data.local.serviceRequest.RequestSyncStatus
import com.app.data.local.serviceRequest.ServiceRequestDao
import com.app.data.local.serviceRequest.ServiceRequestEntity
import com.app.data.mapper.toDto
import com.app.data.mapper.toEntity
import com.app.domain.model.ServiceRequest
import com.app.domain.repository.ServiceRequestRepository
import com.app.domain.repository.SubmissionOutcome
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withTimeout
import java.util.UUID
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

class ServiceRequestRepositoryImpl @Inject constructor(
    private val remoteDataSource: ServiceRequestRemoteDataSource,
    private val serviceRequestDao: ServiceRequestDao
) : ServiceRequestRepository {

    override suspend fun submitRequest(
        request: ServiceRequest
    ): SubmissionOutcome {
        val requestEntity = request.toEntity(
            requestId = UUID.randomUUID().toString(),
            createdAtEpochMillis = System.currentTimeMillis()
        )

        serviceRequestDao.insertRequest(requestEntity)

        val wasSynced = syncRequest(requestEntity)

        return if (wasSynced) {
            SubmissionOutcome.SUBMITTED
        } else {
            SubmissionOutcome.QUEUED
        }
    }

    override suspend fun syncPendingRequests(): Boolean {
        val unsyncedRequests =
            serviceRequestDao.getUnsyncedRequests(
                syncedStatus = RequestSyncStatus.SYNCED
            )

        var allRequestsSynced = true

        unsyncedRequests.forEach { request ->
            val wasSynced = syncRequest(request)

            if (!wasSynced) {
                allRequestsSynced = false
            }
        }

        return allRequestsSynced
    }

    private suspend fun syncRequest(
        request: ServiceRequestEntity
    ): Boolean {
        return try {
            withTimeout(8_000.milliseconds) {
                remoteDataSource.submitRequest(
                    requestId = request.requestId,
                    request = request.toDto()
                )
            }

            serviceRequestDao.updateSyncStatus(
                requestId = request.requestId,
                syncStatus = RequestSyncStatus.SYNCED,
                errorMessage = null
            )

            true
        } catch (error: TimeoutCancellationException) {
            markRequestAsFailed(request, "Upload timed out")
            false
        } catch (error: CancellationException) {
            throw error
        } catch (error: Exception) {
            markRequestAsFailed(
                request = request,
                errorMessage = error.message ?: "Unknown synchronization error"
            )
            false
        }
    }

    private suspend fun markRequestAsFailed(
        request: ServiceRequestEntity,
        errorMessage: String
    ) {
        serviceRequestDao.updateSyncStatus(
            requestId = request.requestId,
            syncStatus = RequestSyncStatus.FAILED,
            errorMessage = errorMessage
        )
    }
}