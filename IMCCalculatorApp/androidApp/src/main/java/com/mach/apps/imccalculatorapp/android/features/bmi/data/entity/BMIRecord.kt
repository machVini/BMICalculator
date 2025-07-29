package com.mach.apps.imccalculatorapp.android.features.bmi.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "bmi_records")
data class BMIRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val bmiValue: Float,
    val category: String,
    val date: Date,
    val weight: Float,
    val height: Float
)