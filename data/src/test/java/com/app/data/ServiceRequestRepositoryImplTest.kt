package com.app.data.serviceRequest

import com.app.data.local.serviceRequest.RequestSyncStatus
import com.app.data.local.serviceRequest.ServiceRequestDao
import com.app.data.local.serviceRequest.ServiceRequestEntity
import com.app.domain.model.ServiceRequest
import com.app.domain.repository.SubmissionOutcome
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.IOException

class ServiceRequestRepositoryImplTest {

    private val remoteDataSource =
        mockk<ServiceRequestRemoteDataSource>()

    private val serviceRequestDao =
        mockk<ServiceRequestDao>()

    private lateinit var repository: ServiceRequestRepositoryImpl

    @Before
    fun setUp() {
        repository = ServiceRequestRepositoryImpl(
            remoteDataSource = remoteDataSource,
            serviceRequestDao = serviceRequestDao
        )
    }

    @Test
    fun `submitRequest uploads successfully and returns submitted`() =
        runTest {
            val savedEntity = slot<ServiceRequestEntity>()

            coEvery {
                serviceRequestDao.insertRequest(
                    capture(savedEntity)
                )
            } just Runs

            coEvery {
                remoteDataSource.submitRequest(
                    any(),
                    any()
                )
            } just Runs

            coEvery {
                serviceRequestDao.updateSyncStatus(
                    requestId = any(),
                    syncStatus = any(),
                    errorMessage = any()
                )
            } returns 1

            val result = repository.submitRequest(createRequest())

            assertEquals(
                SubmissionOutcome.SUBMITTED,
                result
            )

            assertEquals(
                RequestSyncStatus.PENDING,
                savedEntity.captured.syncStatus
            )

            coVerify(exactly = 1) {
                serviceRequestDao.insertRequest(any())
            }

            coVerify(exactly = 1) {
                remoteDataSource.submitRequest(
                    requestId = savedEntity.captured.requestId,
                    request = any()
                )
            }

            coVerify(exactly = 1) {
                serviceRequestDao.updateSyncStatus(
                    requestId = savedEntity.captured.requestId,
                    syncStatus = RequestSyncStatus.SYNCED,
                    errorMessage = null
                )
            }
        }

    @Test
    fun `submitRequest saves locally and returns queued when upload fails`() =
        runTest {
            val savedEntity = slot<ServiceRequestEntity>()

            coEvery {
                serviceRequestDao.insertRequest(
                    capture(savedEntity)
                )
            } just Runs

            coEvery {
                remoteDataSource.submitRequest(
                    any(),
                    any()
                )
            } throws IOException("No internet")

            coEvery {
                serviceRequestDao.updateSyncStatus(
                    requestId = any(),
                    syncStatus = any(),
                    errorMessage = any()
                )
            } returns 1

            val result = repository.submitRequest(createRequest())

            assertEquals(
                SubmissionOutcome.QUEUED,
                result
            )

            assertEquals(
                RequestSyncStatus.PENDING,
                savedEntity.captured.syncStatus
            )

            coVerify(exactly = 1) {
                remoteDataSource.submitRequest(
                    requestId = savedEntity.captured.requestId,
                    request = any()
                )
            }

            coVerify(exactly = 1) {
                serviceRequestDao.updateSyncStatus(
                    requestId = savedEntity.captured.requestId,
                    syncStatus = RequestSyncStatus.FAILED,
                    errorMessage = any()
                )
            }
        }

    @Test
    fun `syncPendingRequests uploads failed request and marks it synced`() =
        runTest {
            val failedRequest = createEntity(
                requestId = "request-123",
                syncStatus = RequestSyncStatus.FAILED
            )

            coEvery {
                serviceRequestDao.getUnsyncedRequests(
                    RequestSyncStatus.SYNCED
                )
            } returns listOf(failedRequest)

            coEvery {
                remoteDataSource.submitRequest(
                    any(),
                    any()
                )
            } just Runs

            coEvery {
                serviceRequestDao.updateSyncStatus(
                    requestId = any(),
                    syncStatus = any(),
                    errorMessage = any()
                )
            } returns 1

            val result = repository.syncPendingRequests()

            assertTrue(result)

            coVerify(exactly = 1) {
                remoteDataSource.submitRequest(
                    requestId = "request-123",
                    request = any()
                )
            }

            coVerify(exactly = 1) {
                serviceRequestDao.updateSyncStatus(
                    requestId = "request-123",
                    syncStatus = RequestSyncStatus.SYNCED,
                    errorMessage = null
                )
            }
        }

    @Test
    fun `syncPendingRequests returns false when retry fails`() =
        runTest {
            val failedRequest = createEntity(
                requestId = "request-123",
                syncStatus = RequestSyncStatus.FAILED
            )

            coEvery {
                serviceRequestDao.getUnsyncedRequests(
                    RequestSyncStatus.SYNCED
                )
            } returns listOf(failedRequest)

            coEvery {
                remoteDataSource.submitRequest(
                    any(),
                    any()
                )
            } throws IOException("Still offline")

            coEvery {
                serviceRequestDao.updateSyncStatus(
                    requestId = any(),
                    syncStatus = any(),
                    errorMessage = any()
                )
            } returns 1

            val result = repository.syncPendingRequests()

            assertFalse(result)

            coVerify(exactly = 1) {
                serviceRequestDao.updateSyncStatus(
                    requestId = "request-123",
                    syncStatus = RequestSyncStatus.FAILED,
                    errorMessage = any()
                )
            }
        }

    @Test
    fun `syncPendingRequests returns true when there are no unsynced requests`() =
        runTest {
            coEvery {
                serviceRequestDao.getUnsyncedRequests(
                    RequestSyncStatus.SYNCED
                )
            } returns emptyList()

            val result = repository.syncPendingRequests()

            assertTrue(result)

            coVerify(exactly = 0) {
                remoteDataSource.submitRequest(
                    any(),
                    any()
                )
            }

            coVerify(exactly = 0) {
                serviceRequestDao.updateSyncStatus(
                    requestId = any(),
                    syncStatus = any(),
                    errorMessage = any()
                )
            }
        }

    private fun createRequest(): ServiceRequest {
        return ServiceRequest(
            mechanicId = "m_01",
            customerName = "Shubh",
            phoneNumber = "9876543210",
            vehicleNumber = "UP16AB1234",
            selectedService = "Battery",
            problemDescription = "Car is not starting"
        )
    }

    private fun createEntity(
        requestId: String,
        syncStatus: RequestSyncStatus
    ): ServiceRequestEntity {
        return ServiceRequestEntity(
            requestId = requestId,
            mechanicId = "m_01",
            customerName = "Shubh",
            phoneNumber = "9876543210",
            vehicleNumber = "UP16AB1234",
            selectedService = "Battery",
            problemDescription = "Car is not starting",
            syncStatus = syncStatus,
            createdAtEpochMillis = 1_000L,
            lastSyncError = "Previous network failure"
        )
    }
}