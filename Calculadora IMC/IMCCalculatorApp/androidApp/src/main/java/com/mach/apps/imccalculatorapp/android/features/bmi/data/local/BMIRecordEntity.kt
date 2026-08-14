package com.mach.apps.imccalculatorapp.android.features.bmi.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mach.apps.imccalculatorapp.BmiCategory
import java.time.Instant

/**
 * Nome da tabela e das colunas preservados do esquema original —
 * é só a classe Kotlin que mudou de nome, então não há migration a fazer.
 */
@Entity(tableName = "bmi_records")
data class BMIRecordEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val bmiValue: Float,
    val category: BmiCategory,
    val date: Instant,
    val weight: Float,
    val height: Float
)
