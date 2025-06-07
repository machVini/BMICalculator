package com.mach.apps.imccalculatorapp.android.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.mach.apps.imccalculatorapp.android.navigation.NavigationItem

@Composable
fun MainNavigation(
    viewModel: BMIViewModel,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    val items = listOf(
        NavigationItem.History,
        NavigationItem.Home,
        NavigationItem.Tips
    )

    Scaffold(
        topBar = {},
        bottomBar = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                NavigationBar(
                    modifier = Modifier
                        .clip(RoundedCornerShape(topEnd = 20.dp, topStart = 20.dp))
                        .height(60.dp),
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ) {
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentDestination = navBackStackEntry?.destination

                    items.forEach { item ->
                        NavigationBarItem(
                            icon = {
                                Icon(item.icon, contentDescription = null)
                            },
                            label = null,
                            selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }

                                when (item) {
                                    is NavigationItem.History -> viewModel.performAction(
                                        BMIViewModel.Action.NavigateToHistory
                                    )

                                    is NavigationItem.Tips -> viewModel.performAction(BMIViewModel.Action.NavigateToTips)
                                    is NavigationItem.Home -> viewModel.performAction(
                                        BMIViewModel.Action.NavigateToHome
                                    )
                                }
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                selectedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                indicatorColor = Color.Transparent,
                                unselectedIconColor = MaterialTheme.colorScheme.tertiary,
                                unselectedTextColor = MaterialTheme.colorScheme.tertiary
                            ),
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = NavigationItem.Home.route,
            modifier = modifier.padding(innerPadding),
        ) {
            composable(NavigationItem.Home.route) {
                BMIScreen(
                    uiState = viewModel.uiState.collectAsState().value,
                    action = viewModel::performAction
                )
            }
            composable(NavigationItem.History.route) {
                HistoryScreen(
                    bmiHistory = viewModel.bmiHistory.collectAsState().value,
                    action = viewModel::performAction,
                    navController = navController
                )
            }
            composable(NavigationItem.Tips.route) {
                TipsScreen(
                    action = viewModel::performAction,
                    navController = navController
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable () -> Unit = { },
    containerColor: Color = MaterialTheme.colorScheme.primaryContainer,
    actions: @Composable RowScope.() -> Unit = {}
) {
    CenterAlignedTopAppBar(
        modifier = modifier,
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = containerColor,
            titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        title = {
            Text(text = title)
        },
        navigationIcon = navigationIcon,
        actions = actions
    )
}

