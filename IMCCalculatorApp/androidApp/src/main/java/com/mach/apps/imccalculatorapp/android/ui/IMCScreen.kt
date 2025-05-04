package com.mach.apps.imccalculatorapp.android.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mach.apps.imccalculatorapp.android.R

@Composable
fun IMCScreen(
    uiState: BMIViewModel.UiState,
    onWeightChange: (String) -> Unit,
    onHeightChange: (String) -> Unit,
    onCalculateClick: () -> Unit,
    onHistoryClick: () -> Unit,
    onTipsClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(colors = listOf(Color(0xFF4A90E2), Color(0xFF1453A3))))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.toolbar_lable),
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Icon(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(100.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, shape = RoundedCornerShape(16.dp))
                .padding(20.dp)
        ) {
            OutlinedTextField(
                value = uiState.weight,
                onValueChange = onWeightChange,
                label = { Text("Peso (kg)") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = uiState.height,
                onValueChange = onHeightChange,
                label = { Text("Altura (cm)") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = onCalculateClick,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Calcular IMC")
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (uiState.bmiResult.isNotEmpty()) {
                Text(
                    text = "Seu IMC é: ${uiState.bmiResult}",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1453A3),
                    fontSize = 18.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }

        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            OutlinedButton(onClick = onHistoryClick) {
                Text("Histórico")
            }
            OutlinedButton(onClick = onTipsClick) {
                Text("Dicas")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}
