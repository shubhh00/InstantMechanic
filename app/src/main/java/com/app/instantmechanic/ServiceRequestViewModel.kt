package com.app.instantmechanic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.domain.model.ServiceRequest
import com.app.domain.repository.ServiceRequestRepository
import com.app.domain.repository.SubmissionOutcome
import com.app.instantmechanic.worker.ServiceRequestSyncScheduler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface ServiceRequestUiState {

    data object Idle : ServiceRequestUiState

    data object Loading : ServiceRequestUiState

    data class Success(
        val queuedForSync: Boolean
    ) : ServiceRequestUiState

    data class Error(
        val message: String
    ) : ServiceRequestUiState
}

@HiltViewModel
class ServiceRequestViewModel @Inject constructor(
    private val repository: ServiceRequestRepository,
    private val syncScheduler: ServiceRequestSyncScheduler
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<ServiceRequestUiState>(ServiceRequestUiState.Idle)

    val uiState: StateFlow<ServiceRequestUiState> =
        _uiState.asStateFlow()

    init {
        // Recovers requests left unsynced after process death or an app restart.
        syncScheduler.scheduleSync()
    }

    fun submitRequest(request: ServiceRequest) {
        viewModelScope.launch {
            _uiState.value = ServiceRequestUiState.Loading

            try {
                val outcome = repository.submitRequest(request)

                val queuedForSync =
                    outcome == SubmissionOutcome.QUEUED

                if (queuedForSync) {
                    syncScheduler.scheduleSync()
                }

                _uiState.value = ServiceRequestUiState.Success(
                    queuedForSync = queuedForSync
                )
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                _uiState.value = ServiceRequestUiState.Error(
                    message = exception.message
                        ?: "Unable to save service request"
                )
            }
        }
    }

    fun resetState() {
        _uiState.value = ServiceRequestUiState.Idle
    }
}