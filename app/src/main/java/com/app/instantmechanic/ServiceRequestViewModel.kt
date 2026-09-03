package com.app.instantmechanic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.domain.model.ServiceRequest
import com.app.domain.repository.ServiceRequestRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds


sealed interface ServiceRequestUiState {
    data object Idle : ServiceRequestUiState
    data object Loading : ServiceRequestUiState
    data object Success : ServiceRequestUiState
    data class Error(val message: String) : ServiceRequestUiState
}
@HiltViewModel
class ServiceRequestViewModel @Inject constructor(
    private val repository: ServiceRequestRepository
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<ServiceRequestUiState>(ServiceRequestUiState.Idle)

    val uiState: StateFlow<ServiceRequestUiState> = _uiState

    fun submitRequest(request: ServiceRequest) {
        viewModelScope.launch {
            _uiState.value = ServiceRequestUiState.Loading

            try {
                withTimeout(8_000.milliseconds) {
                    repository.submitRequest(request)
                }

                _uiState.value = ServiceRequestUiState.Success

            } catch (e: TimeoutCancellationException) {
                _uiState.value = ServiceRequestUiState.Error(
                    "Unable to submit request. Please check your internet connection."
                )

            } catch (e: Exception) {
                _uiState.value = ServiceRequestUiState.Error(
                    e.message ?: "Unable to submit request"
                )
            }
        }
    }

    fun resetState() {
        _uiState.value = ServiceRequestUiState.Idle
    }
}