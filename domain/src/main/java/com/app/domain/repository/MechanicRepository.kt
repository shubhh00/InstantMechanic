package com.app.domain.repository

import com.app.domain.model.Mechanic

interface MechanicRepository {

    suspend fun getMechanics(): List<Mechanic>

    suspend fun getMechanic(id: String): Mechanic?
}
