package com.app.data

import android.util.Log
import com.app.data.api.MechanicApi
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class MechanicRemoteDataSource @Inject constructor(
    private val api: MechanicApi
) {

    suspend fun getMechanics(): List<MechanicDto> {
        return api.getMechanics().values.toList()
    }

    suspend fun getMechanic(id: String): MechanicDto? {
        return api.getMechanics()[id]
    }

//    suspend fun getMechanics(): List<MechanicDto> {
//        val snapshot = database
//            .getReference("mechanics")
//            .get()
//            .await()
//
//        Log.d("FirebaseTest", "exists = ${snapshot.exists()}")
//        Log.d("FirebaseTest", "children = ${snapshot.childrenCount}")
//        Log.d("FirebaseTest", "raw value = ${snapshot.value}")
//
//        return snapshot.children.mapNotNull { child ->
//            Log.d("FirebaseTest", "child key = ${child.key}")
//            Log.d("FirebaseTest", "child value = ${child.value}")
//
//            val dto = child.getValue(MechanicDto::class.java)
//
//            Log.d("FirebaseTest", "parsed DTO = $dto")
//
//            dto
//        }
//    }
//
//    suspend fun getMechanic(id: String): MechanicDto? {
//        val snapshot = database
//            .getReference("mechanics")
//            .child(id)
//            .get()
//            .await()
//
//        Log.d("FirebaseTest", "single exists = ${snapshot.exists()}")
//        Log.d("FirebaseTest", "single value = ${snapshot.value}")
//
//        val dto = snapshot.getValue(MechanicDto::class.java)
//
//        Log.d("FirebaseTest", "single DTO = $dto")
//
//        return dto
//    }
}