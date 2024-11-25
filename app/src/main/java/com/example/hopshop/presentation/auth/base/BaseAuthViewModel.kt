package pl.hincka.hopshop.presentation.auth.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import pl.hincka.hopshop.data.util.AuthState
import pl.hincka.hopshop.domain.usecase.auth.SignInUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class BaseAuthViewModel(
    private val signInUseCase: SignInUseCase,
): ViewModel() {
    private val _state: MutableStateFlow<AuthState> = MutableStateFlow(AuthState.Success(false))
    val signInState: StateFlow<AuthState> = _state


    fun signIn(
        email: String,
        password: String
    ) {
        viewModelScope.launch {
            _state.value = AuthState.Loading
            _state.value = signInUseCase(email, password)
        }
    }
}