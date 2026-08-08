package com.mach.apps.imccalculatorapp

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class IMCCalculatorTest {

    private val calculator = IMCCalculator()

    @Test
    fun `calculate returns weight divided by height squared`() {
        // 70kg, 1,75m -> 70 / 3,0625 = 22,857...
        val result = calculator.calculate(height = 1.75, weight = 70.0)

        assertTrue(result in 22.85..22.86, "esperado ~22.857, veio $result")
    }

    @Test
    fun `calculate rejects non positive height`() {
        assertFailsWith<IllegalArgumentException> {
            calculator.calculate(height = 0.0, weight = 70.0)
        }
    }

    @Test
    fun `calculate rejects non positive weight`() {
        assertFailsWith<IllegalArgumentException> {
            calculator.calculate(height = 1.75, weight = 0.0)
        }
    }

    @Test
    fun `classify maps each range to its category`() {
        assertEquals(BmiCategory.UNDERWEIGHT, calculator.classify(17.0))
        assertEquals(BmiCategory.NORMAL_WEIGHT, calculator.classify(22.0))
        assertEquals(BmiCategory.OVERWEIGHT, calculator.classify(27.0))
        assertEquals(BmiCategory.OBESITY_1, calculator.classify(32.0))
        assertEquals(BmiCategory.OBESITY_2, calculator.classify(37.0))
        assertEquals(BmiCategory.OBESITY_3, calculator.classify(45.0))
    }

    @Test
    fun `classify boundaries fall on the upper category`() {
        // Os limites são exclusivos no início da faixa seguinte: 18.5 já não é
        // abaixo do peso, 24.9 já não é peso normal.
        assertEquals(BmiCategory.NORMAL_WEIGHT, calculator.classify(18.5))
        assertEquals(BmiCategory.OVERWEIGHT, calculator.classify(24.9))
        assertEquals(BmiCategory.OBESITY_1, calculator.classify(29.9))
        assertEquals(BmiCategory.OBESITY_2, calculator.classify(34.9))
        assertEquals(BmiCategory.OBESITY_3, calculator.classify(39.9))
    }

    @Test
    fun `fromCode round trips every category`() {
        BmiCategory.entries.forEach { category ->
            assertEquals(category, BmiCategory.fromCode(category.code))
        }
    }

    @Test
    fun `fromCode returns null for unknown code`() {
        assertEquals(null, BmiCategory.fromCode("nao_existe"))
    }
}
