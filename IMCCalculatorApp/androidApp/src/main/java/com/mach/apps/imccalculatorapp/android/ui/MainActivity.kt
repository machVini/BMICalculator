package com.mach.apps.imccalculatorapp.android.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.ads.MobileAds
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: BMIViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MobileAds.initialize(this)

        lifecycleScope.launch(Dispatchers.IO) {
            viewModel.events.collect { event ->
                handleEvent(event)
            }
        }

        setContent {
            BMICalculatorAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val uiState by viewModel.uiState.collectAsState()
                    BMIScreen(
                        uiState = uiState,
                        action = ::performAction,
                    )
                }
            }
        }
    }

    private fun performAction(action: BMIViewModel.Action) = viewModel.performAction(action)

    private fun handleEvent(event: BMIViewModel.Event) {
        when (event) {
            is BMIViewModel.Event.ShowResult -> {
                // Aqui você pode mostrar um toast ou um snackbar com o resultado
                // O resultado já estará atualizado no uiState
            }

            is BMIViewModel.Event.NavigateToHistoryScreen -> {
                // Implementar navegação para a tela de histórico
                // Por exemplo: startActivity(Intent(this, HistoryActivity::class.java))
            }

            is BMIViewModel.Event.NavigateToTipsScreen -> {
                // Implementar navegação para a tela de dicas
                // Por exemplo: startActivity(Intent(this, TipsActivity::class.java))
            }

            is BMIViewModel.Event.ShowError -> {
                Toast.makeText(this, event.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}

@Preview
@Composable
fun DefaultPreview() {
    BMICalculatorAppTheme {
        BMIScreen(
            uiState = BMIViewModel.UiState(),
            action = {},
        )
    }
}
