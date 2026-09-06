package com.app.instantmechanic.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.app.domain.repository.ServiceRequestRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CancellationException

@HiltWorker
class ServiceRequestSyncWorker @AssistedInject constructor(
    @Assisted applicationContext: Context,
    @Assisted workerParameters: WorkerParameters,
    private val repository: ServiceRequestRepository
) : CoroutineWorker(
    applicationContext,
    workerParameters
) {

    override suspend fun doWork(): Result {
        return try {
            val allRequestsSynced =
                repository.syncPendingRequests()

            if (allRequestsSynced) {
                Result.success()
            } else {
                Result.retry()
            }
        } catch (error: CancellationException) {
            throw error
        } catch (error: Exception) {
            Result.retry()
        }
    }
}