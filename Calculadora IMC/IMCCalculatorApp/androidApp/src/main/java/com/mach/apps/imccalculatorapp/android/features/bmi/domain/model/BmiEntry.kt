package com.mach.apps.imccalculatorapp.android.features.bmi.domain.model

import com.mach.apps.imccalculatorapp.BmiCategory
import java.time.Instant

/**
 * Registro de IMC como o resto do app o enxerga.
 *
 * Separado da entidade do Room de propósito: mudar o esquema do banco
 * não deve obrigar a mexer em ViewModel nem em tela.
 */
data class BmiEntry(
    val id: Long = 0,
    val value: Float,
    val category: BmiCategory,
    val date: Instant,
    val weightKg: Float,
    val heightCm: Float
)
