package com.app.domain.repository

import com.app.domain.model.ServiceRequest

interface ServiceRequestRepository {
    suspend fun submitRequest(request: ServiceRequest)
}