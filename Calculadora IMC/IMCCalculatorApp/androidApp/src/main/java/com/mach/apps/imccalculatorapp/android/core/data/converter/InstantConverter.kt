package com.mach.apps.imccalculatorapp.android.core.data.converter

import androidx.room.TypeConverter
import java.time.Instant

/**
 * Persiste como epoch millis — exatamente o que o converter de java.util.Date
 * gravava antes, então as linhas já existentes continuam sendo lidas sem migration.
 */
class InstantConverter {
    @TypeConverter
    fun toEpochMilli(instant: Instant): Long = instant.toEpochMilli()

    @TypeConverter
    fun fromEpochMilli(epochMilli: Long): Instant = Instant.ofEpochMilli(epochMilli)
}
