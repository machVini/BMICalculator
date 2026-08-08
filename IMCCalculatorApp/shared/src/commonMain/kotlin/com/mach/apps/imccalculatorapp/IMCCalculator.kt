package com.mach.apps.imccalculatorapp

/**
 * Faixas de classificação do IMC.
 *
 * O [code] é o valor persistido no banco — nunca renomeie um code existente
 * sem uma migration, ou os registros antigos deixam de ser reconhecidos.
 */
enum class BmiCategory(val code: String) {
    UNDERWEIGHT("underweight"),
    NORMAL_WEIGHT("normal_weight"),
    OVERWEIGHT("overweight"),
    OBESITY_1("obesity_1"),
    OBESITY_2("obesity_2"),
    OBESITY_3("obesity_3");

    companion object {
        fun fromCode(code: String): BmiCategory? = entries.firstOrNull { it.code == code }
    }
}

class IMCCalculator {
    fun calculate(height: Double, weight: Double): Double {
        if (height <= 0 || weight <= 0) {
            throw IllegalArgumentException("Height and weight must be greater than 0")
        }
        return weight / (height * height)
    }

    fun classify(imc: Double): BmiCategory {
        return when {
            imc < 18.5 -> BmiCategory.UNDERWEIGHT
            imc < 24.9 -> BmiCategory.NORMAL_WEIGHT
            imc < 29.9 -> BmiCategory.OVERWEIGHT
            imc < 34.9 -> BmiCategory.OBESITY_1
            imc < 39.9 -> BmiCategory.OBESITY_2
            else -> BmiCategory.OBESITY_3
        }
    }
}
