package com.mach.apps.imccalculatorapp.android.features.bmi.data.local

import androidx.room.TypeConverter
import com.mach.apps.imccalculatorapp.BmiCategory

class BmiCategoryConverter {
    @TypeConverter
    fun toCode(category: BmiCategory): String = category.code

    @TypeConverter
    fun fromCode(code: String): BmiCategory =
        // Só cai no fallback se a linha tiver um code que não existe mais no enum,
        // o que significa dado corrompido ou uma categoria removida sem migration.
        BmiCategory.fromCode(code) ?: BmiCategory.NORMAL_WEIGHT
}
