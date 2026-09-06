package com.app.data.di

import android.content.Context
import androidx.room.Room
import com.app.data.local.InstantMechanicDatabase
import com.app.data.local.mechanic.MechanicDao
import com.app.data.local.serviceRequest.ServiceRequestDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): InstantMechanicDatabase {
        return Room.databaseBuilder(
            context,
            InstantMechanicDatabase::class.java,
            "instant_mechanic.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideMechanicDao(
        database: InstantMechanicDatabase
    ): MechanicDao {
        return database.mechanicDao()
    }

    @Provides
    @Singleton
    fun provideServiceRequestDao(
        database: InstantMechanicDatabase
    ): ServiceRequestDao {
        return database.serviceRequestDao()
    }
}