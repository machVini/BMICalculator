package com.mach.apps.imccalculatorapp.android.features.bmi.data.repository

import com.mach.apps.imccalculatorapp.android.features.bmi.data.entity.BMIRecord
import kotlinx.coroutines.flow.Flow

interface BMIRepository {
    suspend fun saveBMIRecord(bmiRecord: BMIRecord)

    suspend fun getLatestRecords(): Flow<List<BMIRecord>>

    suspend fun deleteBMIRecord(id: Long)

    suspend fun getBMIRecordById(id: Long): BMIRecord?
}