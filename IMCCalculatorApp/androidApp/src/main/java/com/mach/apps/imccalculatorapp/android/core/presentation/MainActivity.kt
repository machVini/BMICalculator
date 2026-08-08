package com.mach.apps.imccalculatorapp.android.core.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.ads.MobileAds
import com.mach.apps.imccalculatorapp.android.core.presentation.theme.BMICalculatorAppTheme
import com.mach.apps.imccalculatorapp.android.navigation.AppNavigation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        // A partir do targetSdk 35 o app desenha atrás das barras do sistema de
        // qualquer forma; declarar isso explicitamente deixa o comportamento claro.
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        // MobileAds.initialize toca disco e rede. Na main thread é causa
        // clássica de ANR no startup — o próprio Google recomenda background.
        lifecycleScope.launch(Dispatchers.IO) {
            MobileAds.initialize(this@MainActivity)
        }

        setContent {
            BMICalculatorAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
}
