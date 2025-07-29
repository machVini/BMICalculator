package com.mach.apps.imccalculatorapp.android.features.bmi.data.repository

import com.mach.apps.imccalculatorapp.android.features.bmi.data.dao.BMIRecordDao
import com.mach.apps.imccalculatorapp.android.features.bmi.data.entity.BMIRecord
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BMIRepositoryImpl @Inject constructor(
    private val bmiRecordDao: BMIRecordDao
): BMIRepository {

    override suspend fun saveBMIRecord(bmiRecord: BMIRecord) {
        val lastRecord = bmiRecordDao.getLastRecord()

        if (lastRecord != null &&
            lastRecord.bmiValue == bmiRecord.bmiValue &&
            lastRecord.weight == bmiRecord.weight &&
            lastRecord.height == bmiRecord.height) {
            return
        }

        if (bmiRecordDao.getRecordCount() >= 5) {
            bmiRecordDao.deleteOldestRecord()
        }

        bmiRecordDao.insert(bmiRecord)
    }

    override suspend fun getLatestRecords(): Flow<List<BMIRecord>> {
        return bmiRecordDao.getLatestRecords()
    }

    override suspend fun deleteBMIRecord(id: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun getBMIRecordById(id: Long): BMIRecord? {
        TODO("Not yet implemented")
    }
}
