package com.mach.apps.imccalculatorapp.android.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.ui.graphics.vector.ImageVector
import com.mach.apps.imccalculatorapp.android.R

sealed class NavigationItem(val route: String, val icon: ImageVector, val titleResId: Int) {
    data object Home : NavigationItem("home", Icons.Default.Home, R.string.nav_home)
    data object History : NavigationItem("history", Icons.Default.History, R.string.nav_history)
    data object Tips : NavigationItem("tips", Icons.Default.Lightbulb, R.string.nav_tips)
}