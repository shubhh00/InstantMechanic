package com.app.instantmechanic

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.domain.model.ServiceRequest
import com.app.domain.repository.ServiceRequestRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ServiceRequestViewModel @Inject constructor(
    private val repository: ServiceRequestRepository
) : ViewModel() {

    fun submitRequest(
        request: ServiceRequest,
        onSuccess: () -> Unit,
        onError: (Throwable) -> Unit
    ) {
        viewModelScope.launch {
            try {
                repository.submitRequest(request)

                Log.d("ServiceRequest", "Request submitted successfully")
                onSuccess()

            } catch (e: Exception) {

                Log.e("ServiceRequest", "Request submission failed", e)
                onError(e)
            }
        }
    }
}