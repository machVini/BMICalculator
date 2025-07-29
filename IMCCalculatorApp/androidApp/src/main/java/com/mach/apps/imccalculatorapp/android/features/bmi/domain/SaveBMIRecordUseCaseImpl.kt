package com.mach.apps.imccalculatorapp.android.features.bmi.domain

import com.mach.apps.imccalculatorapp.android.features.bmi.data.entity.BMIRecord
import com.mach.apps.imccalculatorapp.android.features.bmi.data.repository.BMIRepository

class SaveBMIRecordUseCaseImpl(
    private val repository: BMIRepository
): SaveBMIRecordUseCase {

    override suspend operator fun invoke(bmiRecord: BMIRecord) {
        repository.saveBMIRecord(bmiRecord)
    }
}