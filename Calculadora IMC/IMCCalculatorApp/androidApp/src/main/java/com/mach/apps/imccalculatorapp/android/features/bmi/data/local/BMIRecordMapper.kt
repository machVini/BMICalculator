package com.mach.apps.imccalculatorapp.android.features.bmi.data.local

import com.mach.apps.imccalculatorapp.android.features.bmi.domain.model.BmiEntry

fun BMIRecordEntity.toDomain(): BmiEntry = BmiEntry(
    id = id,
    value = bmiValue,
    category = category,
    date = date,
    weightKg = weight,
    heightCm = height
)

fun BmiEntry.toEntity(): BMIRecordEntity = BMIRecordEntity(
    id = id,
    bmiValue = value,
    category = category,
    date = date,
    weight = weightKg,
    height = heightCm
)
