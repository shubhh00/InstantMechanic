package com.app.data.mechanic

import com.app.data.local.mechanic.MechanicDao
import com.app.data.mapper.toDomain
import com.app.data.mapper.toEntity
import com.app.domain.model.Mechanic
import com.app.domain.repository.MechanicRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MechanicRepositoryImpl @Inject constructor(
    private val remoteDataSource: MechanicRemoteDataSource,
    private val mechanicDao: MechanicDao
) : MechanicRepository {

    override fun observeMechanics(): Flow<List<Mechanic>> {
        return mechanicDao
            .observeMechanics()
            .map { entities ->
                entities.map { entity ->
                    entity.toDomain()
                }
            }
    }

    override suspend fun refreshMechanics() {
        val remoteMechanics = remoteDataSource.getMechanics()

        val entities = remoteMechanics.mapNotNull { (id, dto) ->
            dto.toEntity(id)
        }
        mechanicDao.replaceMechanics(entities)
    }

    override suspend fun getMechanic(id: String): Mechanic? {
        return mechanicDao
            .getMechanicById(id)
            ?.toDomain()
    }
}