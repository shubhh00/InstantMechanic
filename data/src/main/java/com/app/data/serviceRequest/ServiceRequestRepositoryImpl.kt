package com.app.data.serviceRequest

import com.app.data.mapper.toDto
import com.app.domain.model.ServiceRequest
import com.app.domain.repository.ServiceRequestRepository
import javax.inject.Inject

class ServiceRequestRepositoryImpl @Inject constructor(
    private val remoteDataSource: ServiceRequestRemoteDataSource
) : ServiceRequestRepository {

    override suspend fun submitRequest(request: ServiceRequest) {
        remoteDataSource.submitRequest(
            request.toDto()
        )
    }
}