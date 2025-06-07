package com.mach.apps.imccalculatorapp.android.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mach.apps.imccalculatorapp.android.data.converter.DateConverter
import com.mach.apps.imccalculatorapp.android.data.dao.BMIRecordDao
import com.mach.apps.imccalculatorapp.android.data.entity.BMIRecord

@Database(entities = [BMIRecord::class], version = 1, exportSchema = false)
@TypeConverters(DateConverter::class)
abstract class BMIDatabase : RoomDatabase() {

    abstract fun bmiRecordDao(): BMIRecordDao

    companion object {
        @Volatile
        private var INSTANCE: BMIDatabase? = null

        fun getDatabase(context: Context): BMIDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BMIDatabase::class.java,
                    "bmi_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
