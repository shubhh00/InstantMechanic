package com.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.app.data.local.mechanic.MechanicDao
import com.app.data.local.mechanic.MechanicEntity
import com.app.data.local.serviceRequest.ServiceRequestDao
import com.app.data.local.serviceRequest.ServiceRequestEntity

@Database(
    entities = [
        MechanicEntity::class,
        ServiceRequestEntity::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(StringListConverter::class)
abstract class InstantMechanicDatabase : RoomDatabase() {

    abstract fun mechanicDao(): MechanicDao

    abstract fun serviceRequestDao(): ServiceRequestDao

}


