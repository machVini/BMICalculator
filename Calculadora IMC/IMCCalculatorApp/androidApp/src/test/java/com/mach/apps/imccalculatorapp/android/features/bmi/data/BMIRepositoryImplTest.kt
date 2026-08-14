package com.mach.apps.imccalculatorapp.android.features.bmi.data

import com.mach.apps.imccalculatorapp.BmiCategory
import com.mach.apps.imccalculatorapp.android.features.bmi.data.local.BMIRecordDao
import com.mach.apps.imccalculatorapp.android.features.bmi.data.local.BMIRecordEntity
import com.mach.apps.imccalculatorapp.android.features.bmi.domain.model.BmiEntry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Instant

class BMIRepositoryImplTest {

    private val dao = FakeBMIRecordDao()
    private val repository = BMIRepositoryImpl(dao)

    private fun entry(
        value: Float = 22.9f,
        weight: Float = 70f,
        height: Float = 175f,
        date: Instant = Instant.ofEpochMilli(1_000)
    ) = BmiEntry(
        value = value,
        category = BmiCategory.NORMAL_WEIGHT,
        date = date,
        weightKg = weight,
        heightCm = height
    )

    @Test
    fun `saves the first entry`() = runTest {
        repository.saveEntry(entry())

        assertEquals(1, dao.records.size)
    }

    @Test
    fun `skips an entry identical to the last one`() = runTest {
        repository.saveEntry(entry())
        repository.saveEntry(entry(date = Instant.ofEpochMilli(2_000)))

        assertEquals("medição repetida não deve virar novo registro", 1, dao.records.size)
    }

    @Test
    fun `saves when the weight changed`() = runTest {
        repository.saveEntry(entry())
        repository.saveEntry(entry(value = 23.4f, weight = 71.5f, date = Instant.ofEpochMilli(2_000)))

        assertEquals(2, dao.records.size)
    }

    @Test
    fun `drops the oldest record once the limit is reached`() = runTest {
        repeat(5) { index ->
            repository.saveEntry(
                entry(
                    value = 20f + index,
                    weight = 70f + index,
                    date = Instant.ofEpochMilli(index.toLong() + 1)
                )
            )
        }
        assertEquals(5, dao.records.size)

        repository.saveEntry(entry(value = 30f, weight = 90f, date = Instant.ofEpochMilli(99)))

        assertEquals("o histórico não deve passar de 5", 5, dao.records.size)
        assertEquals("o mais antigo deve ter saído", 21f, dao.records.first().bmiValue, 0f)
    }

    @Test
    fun `maps entities to the domain model`() = runTest {
        repository.saveEntry(entry())

        val entries = repository.getLatestEntries().first()

        assertEquals(1, entries.size)
        assertEquals(22.9f, entries.first().value, 0f)
        assertEquals(70f, entries.first().weightKg, 0f)
        assertEquals(175f, entries.first().heightCm, 0f)
        assertEquals(BmiCategory.NORMAL_WEIGHT, entries.first().category)
    }
}

private class FakeBMIRecordDao : BMIRecordDao {
    val records = mutableListOf<BMIRecordEntity>()

    override suspend fun insert(record: BMIRecordEntity) {
        records += record
    }

    override fun getLatestRecords(): Flow<List<BMIRecordEntity>> =
        flowOf(records.sortedByDescending { it.date }.take(5))

    override suspend fun getRecordCount(): Int = records.size

    override suspend fun deleteOldestRecord() {
        records.minByOrNull { it.date }?.let { records.remove(it) }
    }

    override suspend fun getLastRecord(): BMIRecordEntity? = records.maxByOrNull { it.date }
}
