package com.mach.apps.imccalculatorapp.android.navigation

import kotlinx.serialization.Serializable

/**
 * Destinos como tipos, não strings.
 *
 * Rota errada ou argumento faltando viram erro de compilação, e não uma
 * exceção em runtime como acontecia com as rotas em String.
 */
@Serializable
data object AuthRoute

@Serializable
data object HomeRoute

@Serializable
data object HistoryRoute

@Serializable
data object TipsRoute
