package com.mach.apps.imccalculatorapp.android.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mach.apps.imccalculatorapp.IMCCalculator
import com.mach.apps.imccalculatorapp.NORMAL_WEIGHT
import com.mach.apps.imccalculatorapp.OBESITY_1
import com.mach.apps.imccalculatorapp.OBESITY_2
import com.mach.apps.imccalculatorapp.OBESITY_3
import com.mach.apps.imccalculatorapp.OVERWEIGHT
import com.mach.apps.imccalculatorapp.UNDERWEIGHT
import com.mach.apps.imccalculatorapp.android.R
import com.mach.apps.imccalculatorapp.android.data.entity.BMIRecord
import com.mach.apps.imccalculatorapp.android.data.repository.BMIRepository
import com.mach.apps.imccalculatorapp.android.util.ResourceProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject
import kotlin.math.round

@HiltViewModel
class BMIViewModel @Inject constructor(
    private val imcCalculator: IMCCalculator,
    private val resources: ResourceProvider,
    private val repository: BMIRepository,
) : ViewModel() {
    private var lastCalculationTime = 0L
    private val CLICK_THRESHOLD = 1000

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<Event>()
    val events: SharedFlow<Event> = _events.asSharedFlow()

    val bmiHistory =
        repository.latestRecords.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun performAction(action: Action) {
        when (action) {
            is Action.UpdateWeight -> updateWeight(action.weight)
            is Action.UpdateHeight -> updateHeight(action.height)
            is Action.Calculate -> calculateBMI()
            is Action.NavigateToHistory -> navigateToHistory()
            is Action.NavigateToTips -> navigateToTips()
            is Action.NavigateToHome -> navigateToHome()
            is Action.CleanFields -> cleanFields()
            is Action.OnAdBannerClicked -> openAppRating()
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
            val currentTime = System.currentTimeMillis()
            if (currentTime - lastCalculationTime < CLICK_THRESHOLD) {
                return@launch
            }
            lastCalculationTime = currentTime

            try {
                val weight = _uiState.value.weight.toDoubleOrNull()
                val height = _uiState.value.height.toDoubleOrNull()

                if (weight == null || height == null) {
                    _events.emit(Event.ShowError(resources.getString(R.string.error_invalid_input)))
                    return@launch
                }

                val heightInMeters = height / 100

                val bmi = imcCalculator.calculate(heightInMeters, weight)
                val roundedBmi = round(bmi * 10) / 10
                val categoryCode = imcCalculator.classify(bmi)

                val category = translateCategory(categoryCode)

                _uiState.update { currentState ->
                    currentState.copy(
                        bmi = roundedBmi.toFloat(),
                        bmiCategory = category,
                        categoryColor = getCategoryColor(categoryCode)
                    )
                }

                _events.emit(Event.ShowResult(roundedBmi.toString(), category))

                saveToHistory(roundedBmi, categoryCode, weight, height)
            } catch (e: Exception) {
                _events.emit(Event.ShowError(resources.getString(R.string.calculating_error)))
            }
        }
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
            repository.saveBMIRecord(bmiRecord)
        }
    }

    private fun navigateToHistory() {
        // Implementation for navigation to history screen
    }

    private fun navigateToTips() {
        // Implementation for navigation to tips screen
    }

    private fun navigateToHome() {
        // Implementation for navigation to home screen
    }

    private fun openAppRating() = viewModelScope.launch {
        _events.emit(Event.OpenAppRating)
    }

    sealed class Event {
        data class ShowResult(val bmi: String, val category: String) : Event()
        object NavigateToHistoryScreen : Event()
        object NavigateToTipsScreen : Event()
        data class ShowError(val message: String) : Event()
        object OpenAppRating : Event()
    }

    sealed class Action {
        data class UpdateWeight(val weight: String) : Action()
        data class UpdateHeight(val height: String) : Action()
        data object Calculate : Action()
        data object NavigateToHome : Action()
        data object NavigateToHistory : Action()
        data object NavigateToTips : Action()
        data object CleanFields : Action()
        data object OnAdBannerClicked : Action()
    }

    data class UiState(
        val weight: String = "",
        val height: String = "",
        val bmi: Float? = null,
        val bmiCategory: String = "",
        val categoryColor: Int? = null,
    )
}
