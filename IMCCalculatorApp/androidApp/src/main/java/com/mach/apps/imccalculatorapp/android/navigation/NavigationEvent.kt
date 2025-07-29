package com.mach.apps.imccalculatorapp.android.navigation

sealed class NavigationEvent {
    data object NavigateToHome : NavigationEvent()
    data object NavigateToTips : NavigationEvent()
    data object NavigateToHistory : NavigationEvent()
    data class NavigateBack(val route: String? = null): NavigationEvent()
    data object NavigateToAppRating : NavigationEvent()
    data class ShowError(val message: String) : NavigationEvent()
}