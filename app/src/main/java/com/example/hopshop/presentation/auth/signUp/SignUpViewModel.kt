package com.example.hopshop.presentation.auth.signUp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hopshop.data.util.AuthState
import com.example.hopshop.domain.usecase.auth.SignInUseCase
import com.example.hopshop.domain.usecase.auth.SignUpUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class SignUpViewModel(
    private val signUpUseCase: SignUpUseCase,
) : ViewModel() {
    private val _state: MutableStateFlow<AuthState> = MutableStateFlow(AuthState.None)
    val signInState: StateFlow<AuthState> = _state

    fun signUp(
        name: String,
        email: String,
        password: String,
        repeatPassword: String,
    ) {
        viewModelScope.launch {
            _state.value = AuthState.Loading
            _state.value = signUpUseCase(name, email, password, repeatPassword)
        }
    }
}