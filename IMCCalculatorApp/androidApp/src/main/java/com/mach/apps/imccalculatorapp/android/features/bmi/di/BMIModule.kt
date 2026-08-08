package com.mach.apps.imccalculatorapp.android.features.bmi.di

import android.content.Context
import androidx.room.Room
import com.mach.apps.imccalculatorapp.android.features.bmi.data.BMIRepositoryImpl
import com.mach.apps.imccalculatorapp.android.features.bmi.data.local.BMIDatabase
import com.mach.apps.imccalculatorapp.android.features.bmi.data.local.BMIRecordDao
import com.mach.apps.imccalculatorapp.android.features.bmi.domain.repository.BMIRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BMIModule {

    @Provides
    @Singleton
    fun provideBMIDatabase(@ApplicationContext context: Context): BMIDatabase =
        Room.databaseBuilder(context, BMIDatabase::class.java, "bmi_database").build()

    @Provides
    fun provideBMIRecordDao(database: BMIDatabase): BMIRecordDao = database.bmiRecordDao()
}

@Module
@InstallIn(SingletonComponent::class)
interface BMIBindings {

    @Binds
    @Singleton
    fun bindBMIRepository(impl: BMIRepositoryImpl): BMIRepository
}
