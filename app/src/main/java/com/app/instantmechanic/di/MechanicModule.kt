package com.app.instantmechanic.di

import com.app.data.MechanicRepositoryImpl
import com.app.domain.repository.MechanicRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class MechanicModule {

    @Binds
    @Singleton
    abstract fun bindMechanicRepository(
        impl: MechanicRepositoryImpl
    ): MechanicRepository
}