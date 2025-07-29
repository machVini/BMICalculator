package com.mach.apps.imccalculatorapp.android.features.bmi.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mach.apps.imccalculatorapp.NORMAL_WEIGHT
import com.mach.apps.imccalculatorapp.OBESITY_1
import com.mach.apps.imccalculatorapp.OBESITY_2
import com.mach.apps.imccalculatorapp.OBESITY_3
import com.mach.apps.imccalculatorapp.OVERWEIGHT
import com.mach.apps.imccalculatorapp.UNDERWEIGHT
import com.mach.apps.imccalculatorapp.android.R
import com.mach.apps.imccalculatorapp.android.core.utils.ResourceProvider
import com.mach.apps.imccalculatorapp.android.features.bmi.data.entity.BMIRecord
import com.mach.apps.imccalculatorapp.android.features.bmi.domain.CalculateBMIUseCase
import com.mach.apps.imccalculatorapp.android.features.bmi.domain.GetBMIClassificationUseCase
import com.mach.apps.imccalculatorapp.android.features.bmi.domain.SaveBMIRecordUseCase
import com.mach.apps.imccalculatorapp.android.navigation.NavigationEvent
import com.mach.apps.imccalculatorapp.android.navigation.NavigationHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class BMIViewModel @Inject constructor(
    private val resources: ResourceProvider,
    private val saveBMIRecordUseCase: SaveBMIRecordUseCase,
    private val calculateBMIUseCase: CalculateBMIUseCase,
    private val getBMIClassificationUseCase: GetBMIClassificationUseCase,
) : ViewModel(), NavigationHandler {

    private var lastCalculationTime = 0L
    private val CLICK_THRESHOLD = 500L

    private val _navigationEvent = MutableSharedFlow<NavigationEvent>()
    override val navigationEvent = _navigationEvent.asSharedFlow()

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    fun performAction(action: Action) {
        when (action) {
            is Action.UpdateWeight -> updateWeight(action.weight)
            is Action.UpdateHeight -> updateHeight(action.height)
            is Action.Calculate -> calculateBMI()
        }
    }

    private fun cleanFields() {
        _uiState.update { currentState ->
            currentState.copy(
                weight = "",
                height = "",
                bmi = null,
                bmiCategory = "",
                categoryColor = null
            )
        }
    }

    private fun updateWeight(weight: String) {
        _uiState.update { currentState ->
            currentState.copy(weight = weight)
        }
    }

    private fun updateHeight(height: String) {
        _uiState.update { currentState ->
            currentState.copy(height = height)
        }
    }

    private fun calculateBMI() {
        viewModelScope.launch {
            if (!shouldCalculate()) return@launch

            try {
                val (weight, height) = getValidatedInputs() ?: return@launch
                val heightInMeters = height / 100

                val bmi = calculateBMIUseCase(weight, heightInMeters)
                val categoryCode = getBMIClassificationUseCase(bmi)

                updateUiState(bmi, categoryCode)
                saveToHistory(bmi, categoryCode, weight, height)

            } catch (e: Exception) {
                handleCalculationError()
            }
        }
    }

    private fun shouldCalculate(): Boolean {
        val currentTime = System.currentTimeMillis()
        return if (currentTime - lastCalculationTime < CLICK_THRESHOLD) {
            false
        } else {
            lastCalculationTime = currentTime
            true
        }
    }

    private suspend fun getValidatedInputs(): Pair<Double, Double>? {
        val weight = _uiState.value.weight.toDoubleOrNull()
        val height = _uiState.value.height.toDoubleOrNull()

        return if (weight == null || height == null) {
            _navigationEvent.emit(NavigationEvent.ShowError(resources.getString(R.string.error_invalid_input)))
            null
        } else {
            Pair(weight, height)
        }
    }

    private fun updateUiState(bmi: Double, categoryCode: String) {
        val category = translateCategory(categoryCode)

        _uiState.update { currentState ->
            currentState.copy(
                bmi = bmi.toFloat(),
                bmiCategory = category,
                categoryColor = getCategoryColor(categoryCode)
            )
        }
    }

    private suspend fun handleCalculationError() {
        _navigationEvent.emit(NavigationEvent.ShowError(resources.getString(R.string.calculating_error)))
        cleanFields()
    }

    private fun translateCategory(categoryCode: String): String {
        return when (categoryCode) {
            UNDERWEIGHT -> resources.getString(R.string.underweight)
            NORMAL_WEIGHT -> resources.getString(R.string.normal_weight)
            OVERWEIGHT -> resources.getString(R.string.overweight)
            OBESITY_1 -> resources.getString(R.string.obesity_1)
            OBESITY_2 -> resources.getString(R.string.obesity_2)
            OBESITY_3 -> resources.getString(R.string.obesity_3)
            else -> ""
        }
    }

    private fun getCategoryColor(categoryCode: String): Int {
        return when (categoryCode) {
            UNDERWEIGHT -> R.color.underweight
            NORMAL_WEIGHT -> R.color.normal_weight
            OVERWEIGHT -> R.color.overweight
            else -> R.color.obesity_1
        }
    }

    private fun saveToHistory(bmi: Double, category: String, weight: Double?, height: Double?) {
        viewModelScope.launch {
            val bmiRecord = BMIRecord(
                bmiValue = bmi.toFloat(),
                category = category,
                date = Date(),
                weight = (weight ?: 0).toFloat(),
                height = (height ?: 0).toFloat()
            )
            saveBMIRecordUseCase(bmiRecord)
        }
    }

    sealed class Action {
        data class UpdateWeight(val weight: String) : Action()
        data class UpdateHeight(val height: String) : Action()
        data object Calculate : Action()
    }

    data class UiState(
        val weight: String = "",
        val height: String = "",
        val bmi: Float? = null,
        val bmiCategory: String = "",
        val categoryColor: Int? = null,
        val isLoading: Boolean = false,
        val error: String? = null,
        val latestRecords: List<BMIRecord> = emptyList(),
    )
}
