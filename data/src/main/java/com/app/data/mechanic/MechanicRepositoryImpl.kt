package com.app.data.mechanic

import com.app.data.mapper.toDomain
import com.app.domain.model.Mechanic
import com.app.domain.repository.MechanicRepository
import javax.inject.Inject

class MechanicRepositoryImpl @Inject constructor(
    private val remoteDataSource: MechanicRemoteDataSource
) : MechanicRepository {

    override suspend fun getMechanics(): List<Mechanic> {
        return remoteDataSource
            .getMechanics()
            .mapNotNull { (id, dto) ->
                dto.toDomain(id)
            }
            .sortedBy { it.distanceKm }
    }

    override suspend fun getMechanic(id: String): Mechanic? {
        return remoteDataSource
            .getMechanic(id)
            ?.toDomain(id)
    }
}