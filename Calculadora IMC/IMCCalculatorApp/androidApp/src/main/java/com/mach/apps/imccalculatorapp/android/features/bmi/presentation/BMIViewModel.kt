package com.mach.apps.imccalculatorapp.android.features.bmi.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mach.apps.imccalculatorapp.BmiCategory
import com.mach.apps.imccalculatorapp.android.R
import com.mach.apps.imccalculatorapp.android.core.utils.ResourceProvider
import com.mach.apps.imccalculatorapp.android.features.bmi.domain.model.BmiEntry
import com.mach.apps.imccalculatorapp.android.features.bmi.domain.usecase.CalculateBmiUseCase
import com.mach.apps.imccalculatorapp.android.features.bmi.domain.usecase.SaveBmiEntryUseCase
import com.mach.apps.imccalculatorapp.android.navigation.NavigationEmitter
import com.mach.apps.imccalculatorapp.android.navigation.NavigationEvent
import com.mach.apps.imccalculatorapp.android.navigation.NavigationHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Clock
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class BMIViewModel @Inject constructor(
    private val resources: ResourceProvider,
    private val calculateBmiUseCase: CalculateBmiUseCase,
    private val saveBmiEntryUseCase: SaveBmiEntryUseCase,
    private val clock: Clock,
) : ViewModel(), NavigationHandler {

    private var lastCalculationTime = 0L

    private val navigation = NavigationEmitter()
    override val navigationEvent = navigation.navigationEvent

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    fun performAction(action: Action) {
        when (action) {
            is Action.UpdateWeight -> updateWeight(action.weight)
            is Action.UpdateHeight -> updateHeight(action.height)
            is Action.Calculate -> calculateBMI()
            is Action.OpenAccount -> navigation.emit(NavigationEvent.NavigateToAuth)
        }
    }

    private fun cleanFields() {
        _uiState.update { currentState ->
            currentState.copy(weight = "", height = "", bmi = null, category = null)
        }
    }

    private fun updateWeight(weight: String) {
        _uiState.update { it.copy(weight = weight) }
    }

    private fun updateHeight(height: String) {
        _uiState.update { it.copy(height = height) }
    }

    private fun calculateBMI() {
        viewModelScope.launch {
            if (!shouldCalculate()) return@launch

            val (weight, height) = getValidatedInputs() ?: return@launch

            // Só o cálculo lança exceção (peso/altura <= 0). Capturar Exception
            // aqui engoliria CancellationException e quebraria o cancelamento
            // estruturado das coroutines.
            val result = try {
                calculateBmiUseCase(weightKg = weight, heightCm = height)
            } catch (e: IllegalArgumentException) {
                handleCalculationError()
                return@launch
            }

            _uiState.update {
                it.copy(bmi = result.value.toFloat(), category = result.category)
            }

            saveBmiEntryUseCase(
                BmiEntry(
                    value = result.value.toFloat(),
                    category = result.category,
                    date = Instant.now(clock),
                    weightKg = weight.toFloat(),
                    heightCm = height.toFloat()
                )
            )
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

    private fun getValidatedInputs(): Pair<Double, Double>? {
        val weight = _uiState.value.weight.toDoubleOrNull()
        val height = _uiState.value.height.toDoubleOrNull()

        return if (weight == null || height == null) {
            navigation.emit(NavigationEvent.ShowError(resources.getString(R.string.error_invalid_input)))
            null
        } else {
            Pair(weight, height)
        }
    }

    private fun handleCalculationError() {
        navigation.emit(NavigationEvent.ShowError(resources.getString(R.string.calculating_error)))
        cleanFields()
    }

    sealed class Action {
        data class UpdateWeight(val weight: String) : Action()
        data class UpdateHeight(val height: String) : Action()
        data object Calculate : Action()
        data object OpenAccount : Action()
    }

    data class UiState(
        val weight: String = "",
        val height: String = "",
        val bmi: Float? = null,
        val category: BmiCategory? = null,
    )

    private companion object {
        const val CLICK_THRESHOLD = 500L
    }
}
