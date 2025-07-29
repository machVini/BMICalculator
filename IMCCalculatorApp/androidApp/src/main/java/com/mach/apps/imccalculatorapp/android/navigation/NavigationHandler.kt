package com.mach.apps.imccalculatorapp.android.navigation

import kotlinx.coroutines.flow.SharedFlow

interface NavigationHandler {
    val navigationEvent: SharedFlow<NavigationEvent>
}