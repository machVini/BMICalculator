package com.mach.apps.imccalculatorapp.android.features.tips.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mach.apps.imccalculatorapp.android.navigation.NavigationEvent
import com.mach.apps.imccalculatorapp.android.navigation.NavigationHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TipsViewModel @Inject constructor() : ViewModel(), NavigationHandler {

    private val _navigationEvent = MutableSharedFlow<NavigationEvent>()
    override val navigationEvent = _navigationEvent.asSharedFlow()

    fun performAction(action: Action) {
        when (action) {
            is Action.NavigateBack -> navigateBack()
        }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            _navigationEvent.emit(NavigationEvent.NavigateBack())
        }
    }

    sealed class Action {
        data object NavigateBack : Action()
    }
}