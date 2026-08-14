package com.mach.apps.imccalculatorapp.android.features.bmi.domain.repository

import com.mach.apps.imccalculatorapp.android.features.bmi.domain.model.BmiEntry
import kotlinx.coroutines.flow.Flow

/**
 * O contrato vive no domínio e a implementação em data: assim a camada de
 * dados depende do domínio, e não o contrário.
 */
interface BMIRepository {
    suspend fun saveEntry(entry: BmiEntry)

    fun getLatestEntries(): Flow<List<BmiEntry>>
}
