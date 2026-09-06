package com.app.data.local.mechanic

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface MechanicDao {

    @Query("SELECT * FROM mechanics ORDER BY distanceKm ASC")
    fun observeMechanics(): Flow<List<MechanicEntity>>

    @Query("SELECT * FROM mechanics WHERE id = :mechanicId LIMIT 1")
    suspend fun getMechanicById(
        mechanicId: String
    ): MechanicEntity?

    @Upsert
    suspend fun upsertMechanics(
        mechanics: List<MechanicEntity>
    )

    @Query("DELETE FROM mechanics")
    suspend fun deleteAllMechanics()

    @Transaction
    suspend fun replaceMechanics(
        mechanics: List<MechanicEntity>
    ) {
        deleteAllMechanics()
        upsertMechanics(mechanics)
    }
}