package com.mach.apps.imccalculatorapp.android.features.bmi.domain

import com.mach.apps.imccalculatorapp.android.features.bmi.data.entity.BMIRecord

interface SaveBMIRecordUseCase {

    suspend operator fun invoke(bmiRecord: BMIRecord)
}