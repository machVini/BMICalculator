package com.mach.apps.imccalculatorapp.android.core.presentation

import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import com.mach.apps.imccalculatorapp.BmiCategory
import com.mach.apps.imccalculatorapp.android.R

/**
 * Tradução de categoria para recursos de UI.
 *
 * Fica aqui, e não no ViewModel, para que a camada de apresentação seja a
 * única a conhecer R.string/R.color. Como [BmiCategory] é um enum, os `when`
 * abaixo são exaustivos: adicionar uma faixa nova quebra a compilação em vez
 * de cair silenciosamente num `else`.
 */
@get:StringRes
val BmiCategory.labelRes: Int
    get() = when (this) {
        BmiCategory.UNDERWEIGHT -> R.string.underweight
        BmiCategory.NORMAL_WEIGHT -> R.string.normal_weight
        BmiCategory.OVERWEIGHT -> R.string.overweight
        BmiCategory.OBESITY_1 -> R.string.obesity_1
        BmiCategory.OBESITY_2 -> R.string.obesity_2
        BmiCategory.OBESITY_3 -> R.string.obesity_3
    }

@get:ColorRes
val BmiCategory.colorRes: Int
    get() = when (this) {
        BmiCategory.UNDERWEIGHT -> R.color.underweight
        BmiCategory.NORMAL_WEIGHT -> R.color.normal_weight
        BmiCategory.OVERWEIGHT -> R.color.overweight
        BmiCategory.OBESITY_1 -> R.color.obesity_1
        BmiCategory.OBESITY_2 -> R.color.obesity_2
        BmiCategory.OBESITY_3 -> R.color.obesity_3
    }
