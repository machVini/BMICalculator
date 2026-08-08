package com.mach.apps.imccalculatorapp.android.navigation

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

interface NavigationHandler {
    val navigationEvent: Flow<NavigationEvent>
}

/**
 * Emissor de eventos de navegação para ViewModels.
 *
 * Usa Channel em vez de SharedFlow porque evento de navegação é consumido
 * uma única vez: o Channel guarda o evento até existir um coletor, enquanto
 * um SharedFlow sem replay obriga o emissor a esperar por um.
 */
class NavigationEmitter : NavigationHandler {
    private val events = Channel<NavigationEvent>(Channel.BUFFERED)
    override val navigationEvent: Flow<NavigationEvent> = events.receiveAsFlow()

    fun emit(event: NavigationEvent) {
        events.trySend(event)
    }
}
