package com.mach.apps.imccalculatorapp.android.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.mach.apps.imccalculatorapp.IMCCalculator
import com.mach.apps.imccalculatorapp.NORMAL_WEIGHT
import com.mach.apps.imccalculatorapp.OBESITY_1
import com.mach.apps.imccalculatorapp.OBESITY_2
import com.mach.apps.imccalculatorapp.OVERWEIGHT
import com.mach.apps.imccalculatorapp.UNDERWEIGHT
import com.mach.apps.imccalculatorapp.android.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IMCScreen() {
    var weight by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var imcResult by remember { mutableStateOf<Double?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var classificationKey by remember { mutableStateOf<String?>(null) }

    val calculator = remember { IMCCalculator() }
    val context = LocalContext.current
    var showInfoDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.toolbar_lable)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                    navigationIconContentColor = MaterialTheme.colorScheme.primary,
                    actionIconContentColor = MaterialTheme.colorScheme.primary
                ),
                actions = {
                    IconButton(onClick = { showInfoDialog = true }) {
                        Icon(Icons.Filled.Info, contentDescription = "Info")
                    }
                }
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = stringResource(R.string.toolbar_lable),
                        modifier = Modifier.size(150.dp),
                        alignment = Alignment.Center
                    )
                }

                OutlinedTextField(
                    value = weight,
                    onValueChange = { weight = it },
                    label = { Text(stringResource(R.string.weight_label)) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = height,
                    onValueChange = { height = it },
                    label = { Text(stringResource(R.string.height_label)) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Button(
                    onClick = {
                        val w = parseInput(weight)
                        val h = parseInput(height)
                        if (w != null && h != null && h > 0.0) {
                            val result = calculator.calculate(h, w)
                            imcResult = result
                            classificationKey = calculator.classify(result)
                            errorMessage = null
                        } else {
                            errorMessage = context.getString(R.string.invalid_format_error)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(R.string.calculate_button))
                }

                errorMessage?.let {
                    Text(text = it, color = MaterialTheme.colorScheme.error)
                }

                imcResult?.let { imc ->
                    classificationKey?.let { key ->
                        val classificationResId = when (key) {
                            UNDERWEIGHT -> R.string.underweight
                            NORMAL_WEIGHT -> R.string.normal_weight
                            OVERWEIGHT -> R.string.overweight
                            OBESITY_1 -> R.string.obesity_1
                            OBESITY_2 -> R.string.obesity_2
                            else -> R.string.obesity_3
                        }

                        Text(text = stringResource(R.string.imc_result, String.format("%.2f", imc)))
                        Text(
                            text = stringResource(
                                R.string.imc_classification,
                                stringResource(classificationResId)
                            )
                        )
                    }
                }
            }
            if (showInfoDialog) {
                InfoDialog(onDismiss = { showInfoDialog = false })
            }
        },
        bottomBar = {
            AdBanner()
        }
    )
}

@Composable
fun InfoDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.bmi_classification)) },
        text = {
            Text(text = stringResource(R.string.bmi_info))
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.close))
            }
        }
    )
}

private fun parseInput(input: String): Double? {
    val sanitizedInput = input.replace(",", ".")
    return sanitizedInput.toDoubleOrNull()
}
