package com.mach.apps.imccalculatorapp.android.features.history.presentation

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mach.apps.imccalculatorapp.BmiCategory
import com.mach.apps.imccalculatorapp.android.R
import com.mach.apps.imccalculatorapp.android.core.presentation.AdBanner
import com.mach.apps.imccalculatorapp.android.core.presentation.CustomTopAppBar
import com.mach.apps.imccalculatorapp.android.core.presentation.colorRes
import com.mach.apps.imccalculatorapp.android.core.presentation.labelRes
import com.mach.apps.imccalculatorapp.android.features.bmi.domain.model.BmiEntry
import me.bytebeats.views.charts.bar.BarChart
import me.bytebeats.views.charts.bar.BarChartData
import me.bytebeats.views.charts.bar.render.bar.SimpleBarDrawer
import me.bytebeats.views.charts.bar.render.label.SimpleLabelDrawer
import me.bytebeats.views.charts.bar.render.xaxis.SimpleXAxisDrawer
import me.bytebeats.views.charts.bar.render.yaxis.SimpleYAxisDrawer
import me.bytebeats.views.charts.simpleChartAnimation
import java.time.Month
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun HistoryScreen(
    modifier: Modifier = Modifier,
    bmiHistory: List<BmiEntry>,
    action: (HistoryViewModel.Action) -> Unit,
) {
    Scaffold(
        topBar = {
            CustomTopAppBar(
                title = stringResource(R.string.history_screen_title),
                navigationIcon = {
                    IconButton(onClick = { action(HistoryViewModel.Action.NavigateBack) }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                },
            )
        },
        content = { padding ->
            if (bmiHistory.isEmpty()) {
                FallBackContent(modifier = modifier, padding = padding)
            } else {
                Content(
                    modifier = modifier,
                    padding = padding,
                    bmiHistory = bmiHistory,
                    action = action
                )
            }
        }
    )
}

@Composable
fun Content(
    modifier: Modifier = Modifier,
    padding: PaddingValues,
    bmiHistory: List<BmiEntry>,
    action: (HistoryViewModel.Action) -> Unit,
) {
    LazyColumn(
        modifier = modifier.padding(padding),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            BarChartView(bmiHistory = bmiHistory)
            Spacer(modifier = modifier.height(16.dp))
        }
        items(bmiHistory) { record ->
            HistoryItem(record = record)
        }
        item {
            AdBannerItem(action = action)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun FallBackContent(
    modifier: Modifier,
    padding: PaddingValues
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(padding)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.no_history_available),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun HistoryItem(record: BmiEntry) {
    val pattern = stringResource(R.string.date_pattern)
    val dateFormatter = remember(pattern) {
        DateTimeFormatter.ofPattern(pattern, Locale.getDefault())
            .withZone(ZoneId.systemDefault())
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = dateFormatter.format(record.date),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row {
                    Text(
                        text = stringResource(R.string.bmi_label),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = String.format(" %.1f", record.value),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "(${stringResource(record.category.labelRes)})",
                        style = MaterialTheme.typography.titleMedium,
                        color = colorResource(record.category.colorRes)
                    )
                }
            }
            Column(verticalArrangement = Arrangement.Center) {
                Text(
                    text = "${String.format("%.0f", record.weightKg)}kg / ${
                        String.format(
                            "%.0f",
                            record.heightCm
                        )
                    }cm",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@get:StringRes
private val Month.labelRes: Int
    get() = when (this) {
        Month.JANUARY -> R.string.january
        Month.FEBRUARY -> R.string.february
        Month.MARCH -> R.string.march
        Month.APRIL -> R.string.april
        Month.MAY -> R.string.may
        Month.JUNE -> R.string.june
        Month.JULY -> R.string.july
        Month.AUGUST -> R.string.august
        Month.SEPTEMBER -> R.string.september
        Month.OCTOBER -> R.string.october
        Month.NOVEMBER -> R.string.november
        Month.DECEMBER -> R.string.december
    }

@Composable
fun BarChartView(
    bmiHistory: List<BmiEntry>
) {
    BarChart(
        barChartData = BarChartData(
            bars = bmiHistory.map {
                BarChartData.Bar(
                    value = it.value,
                    label = stringResource(
                        it.date.atZone(ZoneId.systemDefault()).month.labelRes
                    ),
                    color = when (it.category) {
                        BmiCategory.NORMAL_WEIGHT -> Color(0xFFBFFCC6)
                        BmiCategory.OVERWEIGHT -> Color(0xFFFFF5BA)
                        BmiCategory.UNDERWEIGHT -> Color(0xFFACE7FF)
                        BmiCategory.OBESITY_1,
                        BmiCategory.OBESITY_2,
                        BmiCategory.OBESITY_3 -> Color(0xFFFFABAB)
                    }
                )
            },
            startAtZero = false,
        ),
        modifier = Modifier
            .height(208.dp)
            .padding(16.dp),
        animation = simpleChartAnimation(),
        barDrawer = SimpleBarDrawer(),
        yAxisDrawer = SimpleYAxisDrawer(
            labelTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
            axisLineColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        xAxisDrawer = SimpleXAxisDrawer(
            axisLineColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        labelDrawer = SimpleLabelDrawer(
            drawLocation = SimpleLabelDrawer.DrawLocation.XAxis,
            labelTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
        ),
    )
}

@Composable
fun AdBannerItem(
    action: (HistoryViewModel.Action) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.padding(8.dp)
        ) {
            Button(
                onClick = { action.invoke(HistoryViewModel.Action.OnAdBannerClicked) },
                shape = RoundedCornerShape(5.dp),
            ) {
                Text(text = stringResource(R.string.ad_banner_button_text))
            }
            Spacer(Modifier.width(8.dp))
            AdBanner(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding()
            )
        }
    }
}
