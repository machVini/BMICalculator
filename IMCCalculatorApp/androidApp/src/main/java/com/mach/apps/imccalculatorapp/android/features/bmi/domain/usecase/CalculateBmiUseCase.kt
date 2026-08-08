package com.mach.apps.imccalculatorapp.android.features.bmi.domain.usecase

import com.mach.apps.imccalculatorapp.BmiCategory
import com.mach.apps.imccalculatorapp.IMCCalculator
import javax.inject.Inject
import kotlin.math.round

data class BmiResult(
    val value: Double,
    val category: BmiCategory
)

/**
 * Calcula e classifica em uma passada só. Antes eram dois use cases, e cabia
 * ao ViewModel saber que a altura vem em cm e que a classificação usa o valor
 * já arredondado — detalhes de regra de negócio que agora ficam aqui.
 *
 * @throws IllegalArgumentException se peso ou altura forem <= 0.
 */
class CalculateBmiUseCase @Inject constructor(
    private val imcCalculator: IMCCalculator
) {
    operator fun invoke(weightKg: Double, heightCm: Double): BmiResult {
        val raw = imcCalculator.calculate(height = heightCm / 100, weight = weightKg)
        val rounded = round(raw * 10) / 10
        return BmiResult(value = rounded, category = imcCalculator.classify(rounded))
    }
}
