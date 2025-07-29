package com.mach.apps.imccalculatorapp.android.features.bmi.domain

import com.mach.apps.imccalculatorapp.android.features.bmi.data.repository.BMIRepository

class GetLatestRecordsUseCaseImpl(
    private val repository: BMIRepository,
): GetLatestRecordsUseCase {

    override suspend operator fun invoke() = repository.getLatestRecords()
}