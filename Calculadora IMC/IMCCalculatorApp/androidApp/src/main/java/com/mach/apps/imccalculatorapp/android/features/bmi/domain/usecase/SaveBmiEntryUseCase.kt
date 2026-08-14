package com.mach.apps.imccalculatorapp.android.features.bmi.domain.usecase

import com.mach.apps.imccalculatorapp.android.features.bmi.domain.model.BmiEntry
import com.mach.apps.imccalculatorapp.android.features.bmi.domain.repository.BMIRepository
import javax.inject.Inject

class SaveBmiEntryUseCase @Inject constructor(
    private val repository: BMIRepository
) {
    suspend operator fun invoke(entry: BmiEntry) = repository.saveEntry(entry)
}
