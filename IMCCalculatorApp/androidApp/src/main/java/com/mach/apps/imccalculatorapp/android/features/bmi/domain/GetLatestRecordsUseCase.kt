package com.mach.apps.imccalculatorapp.android.features.bmi.domain

import com.mach.apps.imccalculatorapp.android.features.bmi.data.entity.BMIRecord
import kotlinx.coroutines.flow.Flow

interface GetLatestRecordsUseCase {
    suspend operator fun invoke(): Flow<List<BMIRecord>>
}