package com.app.data.serviceRequest

import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ServiceRequestRemoteDataSource @Inject constructor(
    private val database: FirebaseDatabase
) {

    suspend fun submitRequest(request: ServiceRequestDto) {
        database
            .getReference("serviceRequests")
            .push()
            .setValue(request)
            .await()
    }
}