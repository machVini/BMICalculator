package com.mach.apps.imccalculatorapp.android.features.history.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mach.apps.imccalculatorapp.android.features.bmi.data.entity.BMIRecord
import com.mach.apps.imccalculatorapp.android.features.bmi.domain.GetLatestRecordsUseCase
import com.mach.apps.imccalculatorapp.android.navigation.NavigationEvent
import com.mach.apps.imccalculatorapp.android.navigation.NavigationHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val getLatestRecordsUseCase: GetLatestRecordsUseCase
) : ViewModel(), NavigationHandler {
    private val _uiState = MutableStateFlow(HistoryUiState())
    val uiState = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<NavigationEvent>()
    override val navigationEvent = _navigationEvent.asSharedFlow()


    init {
        loadHistory()
    }

    fun performAction(action: Action) {
        when (action) {
            Action.NavigateBack -> navigateBack()
            Action.OnAdBannerClicked -> openAppRating()
        }
    }

    private fun loadHistory() {
        viewModelScope.launch {
            getLatestRecordsUseCase().collect { records ->
                _uiState.update { it.copy(records = records) }
            }
        }
    }

    private fun openAppRating() {
        viewModelScope.launch {
            _navigationEvent.emit(NavigationEvent.NavigateToAppRating)
        }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            _navigationEvent.emit(NavigationEvent.NavigateBack())
        }
    }

    sealed class Action {
        data object NavigateBack : Action()
        data object OnAdBannerClicked : Action()
    }

    data class HistoryUiState(
        val records: List<BMIRecord> = emptyList(),
        val isLoading: Boolean = false
    )
}