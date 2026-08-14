package com.mach.apps.imccalculatorapp.android.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.ui.graphics.vector.ImageVector
import com.mach.apps.imccalculatorapp.android.R

/**
 * Itens da barra inferior, na ordem em que aparecem.
 * Auth não entra aqui de propósito: não é um destino da barra.
 */
enum class NavigationItem(
    val route: Any,
    val icon: ImageVector,
    @StringRes val titleResId: Int
) {
    History(HistoryRoute, Icons.Default.History, R.string.nav_history),
    Home(HomeRoute, Icons.Default.Home, R.string.nav_home),
    Tips(TipsRoute, Icons.Default.Lightbulb, R.string.nav_tips)
}
