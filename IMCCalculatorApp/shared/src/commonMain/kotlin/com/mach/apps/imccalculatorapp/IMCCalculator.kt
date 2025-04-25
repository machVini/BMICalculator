package com.mach.apps.imccalculatorapp

const val UNDERWEIGHT = "underweight"
const val NORMAL_WEIGHT = "normal_weight"
const val OVERWEIGHT = "overweight"
const val OBESITY_1 = "obesity_1"
const val OBESITY_2 = "obesity_2"
const val OBESITY_3 = "obesity_3"

class IMCCalculator {
    fun calculate(height: Double, weight: Double): Double {
        if (height <= 0 || weight <= 0) {
            throw IllegalArgumentException("Height and weight must be greater than 0")
        }
        return weight / (height * height)
    }

    fun classify(imc: Double): String {
        return when {
            imc < 18.5 -> UNDERWEIGHT
            imc < 24.9 -> NORMAL_WEIGHT
            imc < 29.9 -> OVERWEIGHT
            imc < 34.9 -> OBESITY_1
            imc < 39.9 -> OBESITY_2
            else -> OBESITY_3
        }
    }
}