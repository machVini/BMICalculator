package com.mach.apps.imccalculatorapp.android.ui

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
import com.mach.apps.imccalculatorapp.android.util.ResourceProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.round

@HiltViewModel
class BMIViewModel @Inject constructor(
    private val imcCalculator: IMCCalculator,
    private val resources: ResourceProvider
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<Event>()
    val events: SharedFlow<Event> = _events.asSharedFlow()

    fun performAction(action: Action) {
        when (action) {
            is Action.UpdateWeight -> updateWeight(action.weight)
            is Action.UpdateHeight -> updateHeight(action.height)
            is Action.Calculate -> calculateBMI()
            is Action.NavigateToHistory -> navigateToHistory()
            is Action.NavigateToTips -> navigateToTips()
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
            try {
                val weight = _uiState.value.weight.toDoubleOrNull()
                val height = _uiState.value.height.toDoubleOrNull()

                if (weight == null || height == null) {
                    _events.emit(Event.ShowError("Por favor, insira valores válidos"))
                    return@launch
                }

                // Convert height from cm to meters
                val heightInMeters = height / 100

                // Use o IMCCalculator do módulo shared para calcular o IMC
                val bmi = imcCalculator.calculate(heightInMeters, weight)
                val roundedBmi = round(bmi * 10) / 10

                // Use o método classify do IMCCalculator para obter a categoria
                val categoryCode = imcCalculator.classify(bmi)

                // Converta o código da categoria para texto legível
                val category = translateCategory(categoryCode)

                _uiState.update { currentState ->
                    currentState.copy(
                        bmi = roundedBmi.toString(),
                        bmiCategory = category,
                        bmiResult = "$roundedBmi - $category"
                    )
                }

                _events.emit(Event.ShowResult(roundedBmi.toString(), category))

                // Aqui você poderia salvar o resultado no histórico
                saveToHistory(roundedBmi, category)
            } catch (e: IllegalArgumentException) {
                _events.emit(Event.ShowError("Erro: ${e.message}"))
            } catch (e: Exception) {
                _events.emit(Event.ShowError("Ocorreu um erro ao calcular o IMC"))
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

    private fun saveToHistory(bmi: Double, category: String) {
        // Implementation for saving to history
        // This could connect to a database or shared preferences
    }

    private fun navigateToHistory() {
        // Implementation for navigation to history screen
    }

    private fun navigateToTips() {
        // Implementation for navigation to tips screen
    }

    sealed class Event {
        data class ShowResult(val bmi: String, val category: String) : Event()
        object NavigateToHistoryScreen : Event()
        object NavigateToTipsScreen : Event()
        data class ShowError(val message: String) : Event()
    }

    sealed class Action {
        data class UpdateWeight(val weight: String) : Action()
        data class UpdateHeight(val height: String) : Action()
        object Calculate : Action()
        object NavigateToHistory : Action()
        object NavigateToTips : Action()
    }

    data class UiState(
        val weight: String = "",
        val height: String = "",
        val bmi: String = "",
        val bmiCategory: String = "",
        val bmiResult: String = ""
    )
}
