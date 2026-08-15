package com.mach.apps.imccalculatorapp.android.navigation

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController

@Composable
fun NavigationHandler(
    navController: NavHostController,
    navigationHandler: NavigationHandler
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(navigationHandler, lifecycleOwner) {
        // repeatOnLifecycle evita navegar com o app em background:
        // a coleta pausa em onStop e retoma em onStart.
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            navigationHandler.navigationEvent.collect { event ->
                when (event) {
                    NavigationEvent.NavigateToHistory ->
                        navController.navigateWithSaveState(HistoryRoute)

                    NavigationEvent.NavigateToTips ->
                        navController.navigateWithSaveState(TipsRoute)

                    NavigationEvent.NavigateToHome ->
                        navController.navigateWithSaveState(HomeRoute)

                    // navigate simples, não navigateWithSaveState: a conta é um
                    // desvio a partir da calculadora, e o usuário volta com "voltar".
                    NavigationEvent.NavigateToAuth -> navController.navigate(AuthRoute)

                    NavigationEvent.NavigateBack -> navController.navigateUp()

                    NavigationEvent.NavigateToAppRating -> openAppRating(
                        context = navController.context
                    )

                    is NavigationEvent.ShowError -> makeToastError(
                        context = navController.context,
                        message = event.message
                    )
                }
            }
        }
    }
}

private fun openAppRating(context: Context) {
    try {
        val uri = Uri.parse("market://details?id=com.mach.apps.imccalculatorapp.android")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        val uri =
            Uri.parse("https://play.google.com/store/apps/details?id=com.mach.apps.imccalculatorapp.android")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, "Erro ao abrir a loja de aplicativos", Toast.LENGTH_SHORT).show()
    }
}

private fun makeToastError(context: Context, message: String){
    Toast.makeText(
        context,
        message,
        Toast.LENGTH_SHORT
    ).show()
}