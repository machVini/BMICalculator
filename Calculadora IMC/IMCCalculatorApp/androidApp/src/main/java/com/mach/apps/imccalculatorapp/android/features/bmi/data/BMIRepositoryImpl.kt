package com.mach.apps.imccalculatorapp.android.features.bmi.data

import com.mach.apps.imccalculatorapp.android.features.bmi.data.local.BMIRecordDao
import com.mach.apps.imccalculatorapp.android.features.bmi.data.local.toDomain
import com.mach.apps.imccalculatorapp.android.features.bmi.data.local.toEntity
import com.mach.apps.imccalculatorapp.android.features.bmi.domain.model.BmiEntry
import com.mach.apps.imccalculatorapp.android.features.bmi.domain.repository.BMIRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BMIRepositoryImpl @Inject constructor(
    private val bmiRecordDao: BMIRecordDao
) : BMIRepository {

    override suspend fun saveEntry(entry: BmiEntry) {
        val lastRecord = bmiRecordDao.getLastRecord()

        val isDuplicate = lastRecord != null &&
            lastRecord.bmiValue == entry.value &&
            lastRecord.weight == entry.weightKg &&
            lastRecord.height == entry.heightCm
        if (isDuplicate) return

        if (bmiRecordDao.getRecordCount() >= MAX_RECORDS) {
            bmiRecordDao.deleteOldestRecord()
        }

        bmiRecordDao.insert(entry.toEntity())
    }

    override fun getLatestEntries(): Flow<List<BmiEntry>> =
        bmiRecordDao.getLatestRecords().map { records -> records.map { it.toDomain() } }

    private companion object {
        const val MAX_RECORDS = 5
    }
}
