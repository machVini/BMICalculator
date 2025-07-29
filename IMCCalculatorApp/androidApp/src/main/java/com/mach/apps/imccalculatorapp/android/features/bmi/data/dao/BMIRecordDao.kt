package com.mach.apps.imccalculatorapp.android.features.bmi.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.mach.apps.imccalculatorapp.android.features.bmi.data.entity.BMIRecord
import kotlinx.coroutines.flow.Flow

@Dao
interface BMIRecordDao {
    @Insert
    suspend fun insert(bmiRecord: BMIRecord)

    @Query("SELECT * FROM bmi_records ORDER BY date DESC LIMIT 5")
    fun getLatestRecords(): Flow<List<BMIRecord>>

    @Query("SELECT COUNT(*) FROM bmi_records")
    suspend fun getRecordCount(): Int

    @Query("DELETE FROM bmi_records WHERE id IN (SELECT id FROM bmi_records ORDER BY date ASC LIMIT 1)")
    suspend fun deleteOldestRecord()

    @Query("SELECT * FROM bmi_records ORDER BY date DESC LIMIT 1")
    suspend fun getLastRecord(): BMIRecord?
}
