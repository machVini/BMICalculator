package com.mach.apps.imccalculatorapp.android.di

import android.content.Context
import com.mach.apps.imccalculatorapp.IMCCalculator
import com.mach.apps.imccalculatorapp.android.data.BMIDatabase
import com.mach.apps.imccalculatorapp.android.data.dao.BMIRecordDao
import com.mach.apps.imccalculatorapp.android.data.repository.BMIRepository
import com.mach.apps.imccalculatorapp.android.util.AndroidResourceProvider
import com.mach.apps.imccalculatorapp.android.util.ResourceProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ResourceModule {
    @Binds
    @Singleton
    abstract fun bindResourceProvider(
        androidResourceProvider: AndroidResourceProvider
    ): ResourceProvider
}

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideIMCCalculator(): IMCCalculator {
        return IMCCalculator()
    }

    @Provides
    @Singleton
    fun provideBMIDatabase(@ApplicationContext context: Context): BMIDatabase {
        return BMIDatabase.getDatabase(context)
    }

    @Provides
    @Singleton
    fun provideBMIRecordDao(database: BMIDatabase): BMIRecordDao {
        return database.bmiRecordDao()
    }

    @Provides
    @Singleton
    fun provideBMIRepository(bmiRecordDao: BMIRecordDao): BMIRepository {
        return BMIRepository(bmiRecordDao)
    }
}
