package com.mach.apps.imccalculatorapp.android.presentation

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.ads.MobileAds
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
                    MainNavigation(viewModel = viewModel)
                }
            }
        }
    }

    private suspend fun handleEvent(event: BMIViewModel.Event) {
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
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@MainActivity,
                        event.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }

                viewModel.performAction(BMIViewModel.Action.CleanFields)
            }

            is BMIViewModel.Event.OpenAppRating -> {
                openAppRating(this)
            }
        }
    }
}

private fun openAppRating(context: Context) {
    try {
        val uri = Uri.parse("market://details?id=com.mach.apps.imccalculatorapp.android")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        val uri =
            Uri.parse("https://play.google.com/store/apps/details?id=com.mach.apps.imccalculatorapp.android")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, "Erro ao abrir a loja de aplicativos", Toast.LENGTH_SHORT).show()
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
