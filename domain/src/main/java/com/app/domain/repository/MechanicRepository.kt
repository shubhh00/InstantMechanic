package com.app.domain.repository

import com.app.domain.model.Mechanic
import kotlinx.coroutines.flow.Flow

interface MechanicRepository {

    fun observeMechanics(): Flow<List<Mechanic>>

    suspend fun refreshMechanics()

    suspend fun getMechanic(id: String): Mechanic?
}