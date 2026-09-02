package com.app.data

import com.app.domain.model.Mechanic
import com.app.domain.repository.MechanicRepository
import javax.inject.Inject

class MechanicRepositoryImpl @Inject constructor(
    private val remoteDataSource: MechanicRemoteDataSource
) : MechanicRepository {

    override suspend fun getMechanics(): List<Mechanic> {
        return remoteDataSource
            .getMechanics()
            .mapNotNull { it.toDomain() }
    }

    override suspend fun getMechanic(id: String): Mechanic? {
        return remoteDataSource
            .getMechanic(id)
            ?.toDomain()
    }
}