package com.app.data.api


import com.app.data.MechanicDto
import retrofit2.http.GET

interface MechanicApi {

    @GET("mechanics.json")
    suspend fun getMechanics(): Map<String, MechanicDto>
}