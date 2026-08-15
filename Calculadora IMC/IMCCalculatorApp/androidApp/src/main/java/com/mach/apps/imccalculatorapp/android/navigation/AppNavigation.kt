package com.mach.apps.imccalculatorapp.android.navigation

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
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

    Scaffold(
        modifier = Modifier.imePadding(),
        bottomBar = { BottomNavigationBar(navController) },
        // Zerado de propósito: cada tela tem seu próprio Scaffold com TopAppBar,
        // e a TopAppBar já consome o inset da barra de status. Sem isso, o
        // recuo do topo seria aplicado duas vezes.
        contentWindowInsets = WindowInsets(0)
    ) { innerPadding ->
        NavHost(
            navController = navController,
            // A calculadora é a tela inicial: o app funciona por inteiro sem
            // conta, então exigir login na abertura era barreira sem
            // contrapartida — e foi o que travou a revisão do Play Console.
            startDestination = HomeRoute,
            modifier = modifier.padding(innerPadding),
        ) {
            composable<AuthRoute> {
                val viewModel: AuthViewModel = hiltViewModel()
                NavigationHandler(navController = navController, navigationHandler = viewModel)
                AuthScreen(
                    uiState = viewModel.uiState.collectAsStateWithLifecycle().value,
                    authState = viewModel.authState.collectAsStateWithLifecycle().value,
                    action = viewModel::performAction
                )
            }
            composable<HomeRoute> {
                val viewModel: BMIViewModel = hiltViewModel()
                NavigationHandler(navController = navController, navigationHandler = viewModel)
                BMIScreen(
                    uiState = viewModel.uiState.collectAsStateWithLifecycle().value,
                    action = viewModel::performAction
                )
            }
            composable<HistoryRoute> {
                val viewModel: HistoryViewModel = hiltViewModel()
                NavigationHandler(navController = navController, navigationHandler = viewModel)
                HistoryScreen(
                    bmiHistory = viewModel.uiState.collectAsStateWithLifecycle().value.records,
                    action = viewModel::performAction,
                )
            }
            composable<TipsRoute> {
                val viewModel: TipsViewModel = hiltViewModel()
                NavigationHandler(navController = navController, navigationHandler = viewModel)
                TipsScreen(
                    action = viewModel::performAction,
                )
            }
        }
    }
}
