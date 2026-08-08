package com.mach.apps.imccalculatorapp.android.features.tips.presentation

import androidx.lifecycle.ViewModel
import com.mach.apps.imccalculatorapp.android.navigation.NavigationEmitter
import com.mach.apps.imccalculatorapp.android.navigation.NavigationEvent
import com.mach.apps.imccalculatorapp.android.navigation.NavigationHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TipsViewModel @Inject constructor() : ViewModel(), NavigationHandler {

    private val navigation = NavigationEmitter()
    override val navigationEvent = navigation.navigationEvent

    fun performAction(action: Action) {
        when (action) {
            is Action.NavigateBack -> navigation.emit(NavigationEvent.NavigateBack)
        }
    }

    sealed class Action {
        data object NavigateBack : Action()
    }
}
