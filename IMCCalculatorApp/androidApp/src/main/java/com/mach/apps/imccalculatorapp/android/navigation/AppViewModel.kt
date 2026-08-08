package com.mach.apps.imccalculatorapp.android.navigation

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    firebaseAuth: FirebaseAuth
) : ViewModel() {

    /**
     * O FirebaseAuth já persiste a sessão em disco, então quem entrou uma vez
     * não precisa entrar de novo a cada abertura. Resolvido uma única vez na
     * criação do ViewModel: se fosse recalculado a cada recomposição, o NavHost
     * trocaria de destino inicial no meio da navegação.
     */
    val startDestination: Any =
        if (firebaseAuth.currentUser != null) HomeRoute else AuthRoute
}
