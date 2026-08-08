package com.mach.apps.imccalculatorapp.android.features.history.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mach.apps.imccalculatorapp.android.features.bmi.domain.model.BmiEntry
import com.mach.apps.imccalculatorapp.android.features.bmi.domain.usecase.GetLatestBmiEntriesUseCase
import com.mach.apps.imccalculatorapp.android.navigation.NavigationEmitter
import com.mach.apps.imccalculatorapp.android.navigation.NavigationEvent
import com.mach.apps.imccalculatorapp.android.navigation.NavigationHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    getLatestBmiEntriesUseCase: GetLatestBmiEntriesUseCase
) : ViewModel(), NavigationHandler {

    private val navigation = NavigationEmitter()
    override val navigationEvent = navigation.navigationEvent

    // WhileSubscribed encerra a observação do banco quando a tela sai de cena
    // e a retoma se o usuário voltar dentro de 5s (ex.: rotação de tela).
    val uiState: StateFlow<HistoryUiState> = getLatestBmiEntriesUseCase()
        .map { entries -> HistoryUiState(records = entries) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = HistoryUiState()
        )

    fun performAction(action: Action) {
        when (action) {
            Action.NavigateBack -> navigation.emit(NavigationEvent.NavigateBack)
            Action.OnAdBannerClicked -> navigation.emit(NavigationEvent.NavigateToAppRating)
        }
    }

    sealed class Action {
        data object NavigateBack : Action()
        data object OnAdBannerClicked : Action()
    }

    data class HistoryUiState(
        val records: List<BmiEntry> = emptyList()
    )
}
