package com.mach.apps.imccalculatorapp.android.features.bmi.domain.usecase

import com.mach.apps.imccalculatorapp.android.features.bmi.domain.model.BmiEntry
import com.mach.apps.imccalculatorapp.android.features.bmi.domain.repository.BMIRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLatestBmiEntriesUseCase @Inject constructor(
    private val repository: BMIRepository
) {
    operator fun invoke(): Flow<List<BmiEntry>> = repository.getLatestEntries()
}
