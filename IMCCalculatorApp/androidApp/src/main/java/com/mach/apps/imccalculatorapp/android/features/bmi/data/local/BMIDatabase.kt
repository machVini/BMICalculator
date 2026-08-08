package com.mach.apps.imccalculatorapp.android.features.bmi.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mach.apps.imccalculatorapp.android.core.data.converter.InstantConverter

@Database(entities = [BMIRecordEntity::class], version = 1, exportSchema = false)
@TypeConverters(InstantConverter::class, BmiCategoryConverter::class)
abstract class BMIDatabase : RoomDatabase() {
    abstract fun bmiRecordDao(): BMIRecordDao
}
