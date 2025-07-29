package com.mach.apps.imccalculatorapp.android.features.bmi.domain

interface CalculateBMIUseCase {
    operator fun invoke(weight: Double, height: Double): Double
}