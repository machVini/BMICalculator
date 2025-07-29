package com.mach.apps.imccalculatorapp.android.features.auth.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun AuthScreen(
    uiState: AuthViewModel.UiState,
    authState: AuthViewModel.AuthState,
    action: (AuthViewModel.Action) -> Unit,
    modifier: Modifier = Modifier
) {

    LaunchedEffect(authState) {
        if (authState is AuthViewModel.AuthState.Success) {
            action.invoke(AuthViewModel.Action.OnAuthSuccess)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = uiState.email,
            onValueChange = { action.invoke(AuthViewModel.Action.OnEmailChange(it)) },
            label = { Text("Email") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = uiState.password,
            onValueChange = { action.invoke(AuthViewModel.Action.OnPasswordChange(it)) },
            label = { Text("Senha") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        if (!uiState.isLogin) {
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = uiState.age,
                onValueChange = { action.invoke(AuthViewModel.Action.OnAgeChange(it)) },
                label = { Text("Idade") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = uiState.activityLevel,
                onValueChange = { action.invoke(AuthViewModel.Action.OnActivityLevelChange(it)) },
                label = { Text("Nível de Atividade") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (authState) {
            is AuthViewModel.AuthState.Loading -> CircularProgressIndicator()
            is AuthViewModel.AuthState.Error -> Text(
                text = authState.message,
                color = MaterialTheme.colorScheme.error
            )
            else -> {}
        }

        Button(
            onClick = {
                action.invoke(AuthViewModel.Action.OnPrimaryButtonClick)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(uiState.primaryButtonRes))
        }

        TextButton(
            onClick = { action.invoke(AuthViewModel.Action.OnSecondaryButtonClick) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(uiState.secondaryButtonRes))
        }
    }
}