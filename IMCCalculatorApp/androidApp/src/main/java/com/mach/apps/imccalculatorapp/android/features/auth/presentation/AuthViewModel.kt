package com.mach.apps.imccalculatorapp.android.features.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mach.apps.imccalculatorapp.android.R
import com.mach.apps.imccalculatorapp.android.navigation.NavigationEvent
import com.mach.apps.imccalculatorapp.android.navigation.NavigationHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor() : ViewModel(), NavigationHandler {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState = _authState.asStateFlow()

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<NavigationEvent>()
    override val navigationEvent = _navigationEvent.asSharedFlow()

    fun performAction(action: Action) {
        when (action) {
            is Action.OnEmailChange -> onEmailChange(action.email)
            is Action.OnPasswordChange -> onPasswordChange(action.password)
            is Action.OnAgeChange -> onAgeChange(action.age)
            is Action.OnActivityLevelChange -> onActivityLevelChange(action.activityLevel)
            is Action.OnAuthSuccess -> onAuthSuccess()
            is Action.OnPrimaryButtonClick -> onPrimaryButtonClick()
            is Action.OnSecondaryButtonClick -> onSecondaryButtonClick()
        }
    }

    private fun onAuthSuccess() {
        viewModelScope.launch {
            _navigationEvent.emit(NavigationEvent.NavigateToHome)
        }
    }

    private fun onPrimaryButtonClick() {
        if (uiState.value.isLogin) {
            loginUser(
                uiState.value.email,
                uiState.value.password
            )
        } else {
            registerUser(
                uiState.value.email,
                uiState.value.password,
                uiState.value.age,
                uiState.value.activityLevel
            )
        }
    }

    private fun onSecondaryButtonClick() {
        _uiState.update { currentState ->
            currentState.copy(
                isLogin = !currentState.isLogin,
                primaryButtonRes = if (currentState.isLogin) R.string.auth_register_button else R.string.auth_login_button,
                secondaryButtonRes = if (currentState.isLogin) R.string.auth_not_registered_yet_button else R.string.auth_already_registered_button
            )
        }
    }

    private fun loginUser(email: String, password: String) {
        TODO("Not yet implemented")
    }

    private fun registerUser(
        email: String,
        password: String,
        age: String,
        activityLevel: String
    ) {
        TODO("Not yet implemented")
    }

    private fun onEmailChange(email: String) {
        _uiState.value = _uiState.value.copy(email = email)
    }

    private fun onPasswordChange(password: String) {
        _uiState.value = _uiState.value.copy(password = password)
    }

    private fun onAgeChange(age: String) {
        _uiState.value = _uiState.value.copy(age = age)
    }

    private fun onActivityLevelChange(activityLevel: String) {
        _uiState.value = _uiState.value.copy(activityLevel = activityLevel)
    }

    sealed class Action() {
        data class OnEmailChange(val email: String) : Action()
        data class OnPasswordChange(val password: String) : Action()
        data class OnAgeChange(val age: String) : Action()
        data class OnActivityLevelChange(val activityLevel: String) : Action()
        data object OnAuthSuccess : Action()
        data object OnPrimaryButtonClick : Action()
        data object OnSecondaryButtonClick : Action()
    }

    sealed class AuthState {
        object Idle : AuthState()
        object Loading : AuthState()
        object Success : AuthState()
        data class Error(val message: String) : AuthState()
    }

    data class UiState(
        val isLogin: Boolean = true,
        val email: String = "",
        val password: String = "",
        val age: String = "",
        val activityLevel: String = "",
        val isLoading: Boolean = false,
        val error: String? = null,
        val primaryButtonRes: Int = R.string.auth_login_button,
        val secondaryButtonRes: Int = R.string.auth_not_registered_yet_button
    )
}