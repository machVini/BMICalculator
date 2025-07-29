package com.mach.apps.imccalculatorapp.android.features.bmi.domain

import com.mach.apps.imccalculatorapp.IMCCalculator
import kotlin.math.round

class CalculateBMIUseCaseImpl(
    private val imcCalculator: IMCCalculator
): CalculateBMIUseCase {

    override operator fun invoke(weight: Double, height: Double): Double {
        val bmi = imcCalculator.calculate(height = height, weight = weight)
        return round(bmi * 10) / 10
    }
}