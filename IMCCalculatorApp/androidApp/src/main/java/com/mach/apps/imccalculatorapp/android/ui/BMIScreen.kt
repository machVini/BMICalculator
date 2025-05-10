package com.mach.apps.imccalculatorapp.android.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mach.apps.imccalculatorapp.android.R

@Composable
fun BMIScreen(
    uiState: BMIViewModel.UiState,
    action: (BMIViewModel.Action) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.40f)
                    .background(Color(0xFF8DCEFA))
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .background(Color(0xFFFBFBFB))
            )
        }

        Scaffold(
            containerColor = Color.Transparent,
            bottomBar = { AdBanner() }
        ) { padding ->
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(vertical = 16.dp, horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = stringResource(R.string.toolbar_lable),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))
                Icon(
                    painter = painterResource(R.drawable.ic_logo),
                    contentDescription = null,
                    modifier = Modifier.size(128.dp),
                    tint = Color(0xFFFBFBFB)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0XFFFDFDFD)),
                    elevation = CardDefaults.cardElevation(5.dp),
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        OutlinedTextField(
                            value = uiState.weight,
                            onValueChange = { weight ->
                                action.invoke(BMIViewModel.Action.UpdateWeight(weight))
                            },
                            label = { Text(stringResource(R.string.weight_label)) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = uiState.height,
                            onValueChange = { height ->
                                action.invoke(BMIViewModel.Action.UpdateHeight(height))
                            },
                            label = { Text(stringResource(R.string.height_label)) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = {
                                action.invoke(BMIViewModel.Action.Calculate)
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(5.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.calculate_button),
                                fontSize = 16.sp
                            )
                        }

                        uiState.bmi?.let {
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = stringResource(R.string.imc_classification).format(it),
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center,
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            BmiScaleIndicator(imc = it)
                            Text(
                                text = uiState.bmiCategory.uppercase(),
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center,
                                color = uiState.categoryColor?.let { color ->
                                    colorResource(id = color)
                                } ?: Color.Unspecified
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BmiScaleIndicator(imc: Float, totalWidth: Dp = 300.dp) {
    val position = calculateImcPosition(imc)
    val density = LocalDensity.current

    Box(
        modifier = Modifier
            .width(totalWidth)
            .wrapContentHeight()
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .height(12.dp)
                .clip(RoundedCornerShape(5.dp))
        ) {
            repeat(4) { index ->
                Box(
                    Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .background(colorForSegment(index))
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.Start)
        ) {
            with(density) {
                val offsetPx = (position * totalWidth.toPx()).toDp()
                Icon(
                    Icons.Default.ArrowDropUp,
                    contentDescription = null,
                    modifier = Modifier.offset(x = offsetPx - 12.dp)
                )
            }
        }
    }
}

private fun calculateImcPosition(imc: Float): Float {
    val segments = listOf(
        0f to 18.5f,
        18.5f to 24.9f,
        24.9f to 29.9f,
        29.9f to 39.9f
    )
    val segmentWidth = 1f / segments.size

    segments.forEachIndexed { index, (start, end) ->
        if (imc <= end) {
            val localProgress = (imc - start) / (end - start)
            return (index * segmentWidth) + (localProgress * segmentWidth)
        }
    }
    return 0.98f
}

private fun colorForSegment(index: Int): Color {
    return when (index) {
        0 -> Color(0xFF4FC3F7)
        1 -> Color(0xFF81C784)
        2 -> Color(0xFFFFF176)
        else -> Color(0xFFE57373)
    }
}
