package com.mach.apps.imccalculatorapp.android.features.bmi.domain

import com.mach.apps.imccalculatorapp.IMCCalculator

class GetBMIClassificationUseCaseImpl(
    private val imcCalculator: IMCCalculator
): GetBMIClassificationUseCase {

    override operator fun invoke(bmi: Double): String {
        return imcCalculator.classify(bmi)
    }
}