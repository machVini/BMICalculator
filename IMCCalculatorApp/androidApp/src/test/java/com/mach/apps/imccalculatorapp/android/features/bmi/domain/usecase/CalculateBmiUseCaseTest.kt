package com.mach.apps.imccalculatorapp.android.features.bmi.domain.usecase

import com.mach.apps.imccalculatorapp.BmiCategory
import com.mach.apps.imccalculatorapp.IMCCalculator
import org.junit.Assert.assertEquals
import org.junit.Test

class CalculateBmiUseCaseTest {

    private val useCase = CalculateBmiUseCase(IMCCalculator())

    @Test
    fun `converts height from centimeters to meters`() {
        // Se a conversão cm -> m não acontecesse, 70kg/175cm daria 0,002.
        val result = useCase(weightKg = 70.0, heightCm = 175.0)

        assertEquals(22.9, result.value, 0.001)
    }

    @Test
    fun `rounds the value to one decimal place`() {
        val result = useCase(weightKg = 70.0, heightCm = 175.0)

        assertEquals(22.9, result.value, 0.0)
    }

    @Test
    fun `classifies using the rounded value not the raw one`() {
        // 71,85kg / 1,70m -> IMC cru 24,8615, que seria PESO NORMAL (< 24,9).
        // Arredondado vira 24,9, que já é SOBREPESO. Esta entrada foi escolhida
        // justamente por atravessar o limite: se a classificação voltar a usar
        // o valor cru, este teste quebra.
        val result = useCase(weightKg = 71.85, heightCm = 170.0)

        assertEquals(24.9, result.value, 0.0)
        assertEquals(BmiCategory.OVERWEIGHT, result.category)
    }

    @Test
    fun `returns the category matching the calculated value`() {
        assertEquals(BmiCategory.NORMAL_WEIGHT, useCase(70.0, 175.0).category)
        assertEquals(BmiCategory.UNDERWEIGHT, useCase(45.0, 175.0).category)
        assertEquals(BmiCategory.OBESITY_3, useCase(140.0, 175.0).category)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `propagates error for non positive input`() {
        useCase(weightKg = 0.0, heightCm = 175.0)
    }
}
