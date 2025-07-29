package com.mach.apps.imccalculatorapp.android.features.bmi.domain

interface GetBMIClassificationUseCase {
    operator fun invoke(bmi: Double): String
}