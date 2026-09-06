package com.app.data.local.mechanic

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mechanics")
data class MechanicEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val rating: Double,
    val reviewCount: Int,
    val distanceKm: Double,
    val location: String,
    val address: String,
    val services: List<String>,
    val openTime: String,
    val closeTime: String,
    val phoneNumber: String
)