package com.app.data.mechanic

import com.app.data.MechanicDto
import com.app.data.api.MechanicApi
import javax.inject.Inject

class MechanicRemoteDataSource @Inject constructor(
    private val api: MechanicApi
) {

    suspend fun getMechanics(): Map<String, MechanicDto> {
        return api.getMechanics()
    }

    suspend fun getMechanic(id: String): MechanicDto? {
        return api.getMechanics()[id]
    }
}