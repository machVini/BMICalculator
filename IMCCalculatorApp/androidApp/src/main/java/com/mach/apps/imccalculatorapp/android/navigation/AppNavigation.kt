package com.mach.apps.imccalculatorapp.android.navigation

import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mach.apps.imccalculatorapp.android.features.auth.presentation.AuthScreen
import com.mach.apps.imccalculatorapp.android.features.auth.presentation.AuthViewModel
import com.mach.apps.imccalculatorapp.android.features.bmi.presentation.BMIScreen
import com.mach.apps.imccalculatorapp.android.features.bmi.presentation.BMIViewModel
import com.mach.apps.imccalculatorapp.android.features.history.presentation.HistoryScreen
import com.mach.apps.imccalculatorapp.android.features.history.presentation.HistoryViewModel
import com.mach.apps.imccalculatorapp.android.features.tips.presentation.TipsScreen
import com.mach.apps.imccalculatorapp.android.features.tips.presentation.TipsViewModel

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    val items = NavigationItem.values().toList()

    Scaffold(
        modifier = Modifier.navigationBarsPadding().imePadding(),
        bottomBar = { BottomNavigationBar(navController, items) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = NavigationItem.Auth.route,
            modifier = modifier.padding(innerPadding),
        ) {
            composable(NavigationItem.Auth.route) {
                val viewModel: AuthViewModel = hiltViewModel()
                NavigationHandler(navController = navController, navigationHandler = viewModel)
                AuthScreen(
                    uiState = viewModel.uiState.collectAsState().value,
                    authState = viewModel.authState.collectAsStateWithLifecycle().value,
                    action = viewModel::performAction
                )
            }
            composable(NavigationItem.Home.route) {
                val viewModel: BMIViewModel = hiltViewModel()
                NavigationHandler(navController = navController, navigationHandler = viewModel)
                BMIScreen(
                    uiState = viewModel.uiState.collectAsState().value,
                    action = viewModel::performAction
                )
            }
            composable(NavigationItem.History.route) {
                val viewModel: HistoryViewModel = hiltViewModel()
                NavigationHandler(navController = navController, navigationHandler = viewModel)
                HistoryScreen(
                    bmiHistory = viewModel.uiState.collectAsState().value.records,
                    action = viewModel::performAction,
                )
            }
            composable(NavigationItem.Tips.route) {
                val viewModel: TipsViewModel = hiltViewModel()
                NavigationHandler(navController = navController, navigationHandler = viewModel)
                TipsScreen(
                    action = viewModel::performAction,
                )
            }
        }
    }
}

