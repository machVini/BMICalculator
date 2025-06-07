package com.mach.apps.imccalculatorapp.android.data.repository

import com.mach.apps.imccalculatorapp.android.data.dao.BMIRecordDao
import com.mach.apps.imccalculatorapp.android.data.entity.BMIRecord
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BMIRepository @Inject constructor(
    private val bmiRecordDao: BMIRecordDao
) {
    val latestRecords: Flow<List<BMIRecord>> = bmiRecordDao.getLatestRecords()

    suspend fun saveBMIRecord(bmiRecord: BMIRecord): Boolean {
        val lastRecord = bmiRecordDao.getLastRecord()

        if (lastRecord != null &&
            lastRecord.bmiValue == bmiRecord.bmiValue &&
            lastRecord.weight == bmiRecord.weight &&
            lastRecord.height == bmiRecord.height) {
            return false
        }

        if (bmiRecordDao.getRecordCount() >= 5) {
            bmiRecordDao.deleteOldestRecord()
        }

        bmiRecordDao.insert(bmiRecord)
        return true
    }
}
