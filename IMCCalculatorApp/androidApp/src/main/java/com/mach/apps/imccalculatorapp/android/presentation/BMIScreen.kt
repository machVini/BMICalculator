package com.mach.apps.imccalculatorapp.android.presentation

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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mach.apps.imccalculatorapp.android.R

@Composable
fun BMIScreen(
    uiState: BMIViewModel.UiState,
    action: (BMIViewModel.Action) -> Unit,
    modifier: Modifier = Modifier
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    LaunchedEffect(Unit) {
        focusManager.clearFocus(force = true)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.5f)
                    .background(MaterialTheme.colorScheme.primaryContainer)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .background(MaterialTheme.colorScheme.background)
            )
        }

        Scaffold(
            topBar = {
                CustomTopAppBar(
                    title = stringResource(R.string.toolbar_lable),
                    modifier = modifier
                )
            },
            containerColor = Color.Transparent,
            bottomBar = {
                AdBanner(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 4.dp)
                )
            }
        ) { padding ->
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                item {
                    Icon(
                        painter = painterResource(R.drawable.ic_logo),
                        contentDescription = null,
                        modifier = Modifier.size(148.dp),
                        tint = uiState.categoryColor?.let { color ->
                            colorResource(id = color)
                        } ?: Color(0xFFFBFBFB)
                    )
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer),
                        elevation = CardDefaults.cardElevation(5.dp),
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            OutlinedTextField(
                                value = uiState.weight,
                                onValueChange = { weight ->
                                    action.invoke(BMIViewModel.Action.UpdateWeight(weight))
                                },
                                label = { Text(stringResource(R.string.weight_label)) },
                                keyboardOptions = KeyboardOptions.Default.copy(
                                    keyboardType = KeyboardType.Number,
                                    imeAction = ImeAction.Next
                                ),
                                modifier = Modifier.fillMaxWidth(),
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = uiState.height,
                                onValueChange = { height ->
                                    action.invoke(BMIViewModel.Action.UpdateHeight(height))
                                },
                                label = { Text(stringResource(R.string.height_label)) },
                                keyboardOptions = KeyboardOptions.Default.copy(
                                    keyboardType = KeyboardType.Number,
                                    imeAction = ImeAction.Done
                                ),
                                keyboardActions = KeyboardActions(
                                    onDone = {
                                        action.invoke(BMIViewModel.Action.Calculate)
                                        keyboardController?.hide()
                                    }
                                ),
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
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
fun BmiScaleIndicator(imc: Float) {
    val position = calculateImcPosition(imc)
    val density = LocalDensity.current
    var totalWidth by remember { mutableStateOf(0) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .onGloballyPositioned { layoutCoordinates ->
                totalWidth = layoutCoordinates.size.width
            }
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
                        .background(colorResource(colorIdForSegment(index)))
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.Start)
        ) {
            with(density) {
                val offsetPx = (position * totalWidth).toDp()
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

private fun colorIdForSegment(index: Int): Int {
    return when (index) {
        0 -> R.color.underweight
        1 -> R.color.normal_weight
        2 -> R.color.overweight
        else -> R.color.obesity_1
    }
}
