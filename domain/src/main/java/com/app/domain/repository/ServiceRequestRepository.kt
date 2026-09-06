package com.app.domain.repository

import com.app.domain.model.ServiceRequest

enum class SubmissionOutcome {
    SUBMITTED,
    QUEUED
}

interface ServiceRequestRepository {

    suspend fun submitRequest(
        request: ServiceRequest
    ): SubmissionOutcome

    suspend fun syncPendingRequests(): Boolean
}