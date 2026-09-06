package com.app.data

import com.app.data.mapper.toDto
import com.app.data.local.serviceRequest.RequestSyncStatus
import com.app.data.local.serviceRequest.ServiceRequestEntity
import com.app.data.mapper.toEntity
import com.app.domain.model.ServiceRequest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ServiceRequestMapperTest {

    @Test
    fun `toEntity creates pending request with supplied id and timestamp`() {
        val request = ServiceRequest(
            mechanicId = "m_01",
            customerName = "Shubh",
            phoneNumber = "9876543210",
            vehicleNumber = "UP16AB1234",
            selectedService = "Battery",
            problemDescription = "Car is not starting"
        )

        val entity = request.toEntity(
            requestId = "request-123",
            createdAtEpochMillis = 1_000L
        )

        assertEquals("request-123", entity.requestId)
        assertEquals("m_01", entity.mechanicId)
        assertEquals("Shubh", entity.customerName)
        assertEquals(1_000L, entity.createdAtEpochMillis)
        assertEquals(
            RequestSyncStatus.PENDING,
            entity.syncStatus
        )
        assertNull(entity.lastSyncError)
    }

    @Test
    fun `toDto maps cached entity for remote upload`() {
        val entity = ServiceRequestEntity(
            requestId = "request-123",
            mechanicId = "m_01",
            customerName = "Shubh",
            phoneNumber = "9876543210",
            vehicleNumber = "UP16AB1234",
            selectedService = "Battery",
            problemDescription = "Car is not starting",
            syncStatus = RequestSyncStatus.FAILED,
            createdAtEpochMillis = 1_000L,
            lastSyncError = "No internet"
        )

        val dto = entity.toDto()

        assertEquals(entity.mechanicId, dto.mechanicId)
        assertEquals(entity.customerName, dto.customerName)
        assertEquals(entity.phoneNumber, dto.phoneNumber)
        assertEquals(entity.vehicleNumber, dto.vehicleNumber)
        assertEquals(entity.selectedService, dto.selectedService)
        assertEquals(
            entity.problemDescription,
            dto.problemDescription
        )
    }
}