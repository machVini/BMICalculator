package com.mach.apps.imccalculatorapp.android.features.bmi.di

import android.content.Context
import com.mach.apps.imccalculatorapp.IMCCalculator
import com.mach.apps.imccalculatorapp.android.features.bmi.data.BMIDatabase
import com.mach.apps.imccalculatorapp.android.features.bmi.data.dao.BMIRecordDao
import com.mach.apps.imccalculatorapp.android.features.bmi.data.repository.BMIRepository
import com.mach.apps.imccalculatorapp.android.features.bmi.data.repository.BMIRepositoryImpl
import com.mach.apps.imccalculatorapp.android.features.bmi.domain.CalculateBMIUseCase
import com.mach.apps.imccalculatorapp.android.features.bmi.domain.CalculateBMIUseCaseImpl
import com.mach.apps.imccalculatorapp.android.features.bmi.domain.GetBMIClassificationUseCase
import com.mach.apps.imccalculatorapp.android.features.bmi.domain.GetBMIClassificationUseCaseImpl
import com.mach.apps.imccalculatorapp.android.features.bmi.domain.GetLatestRecordsUseCase
import com.mach.apps.imccalculatorapp.android.features.bmi.domain.GetLatestRecordsUseCaseImpl
import com.mach.apps.imccalculatorapp.android.features.bmi.domain.SaveBMIRecordUseCase
import com.mach.apps.imccalculatorapp.android.features.bmi.domain.SaveBMIRecordUseCaseImpl
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
        return BMIRepositoryImpl(bmiRecordDao)
    }

    @Provides
    @Singleton
    fun provideSaveBMIRecordUseCase(repository: BMIRepository): SaveBMIRecordUseCase {
        return SaveBMIRecordUseCaseImpl(repository)
    }

    @Provides
    @Singleton
    fun provideGetLatestRecordsUseCase(repository: BMIRepository): GetLatestRecordsUseCase {
        return GetLatestRecordsUseCaseImpl(repository)
    }

    @Provides
    @Singleton
    fun provideCalculateBMIUseCase(imcCalculator: IMCCalculator): CalculateBMIUseCase {
        return CalculateBMIUseCaseImpl(imcCalculator)
    }

    @Provides
    @Singleton
    fun provideGetBMIClassificationUseCase(imcCalculator: IMCCalculator): GetBMIClassificationUseCase {
        return GetBMIClassificationUseCaseImpl(imcCalculator)
    }
}