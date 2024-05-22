package com.example.hopshop.presentation.createList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hopshop.domain.usecase.list.CreateListUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class CreateListViewModel(
    private val createListUseCase: CreateListUseCase
) : ViewModel() {
    private val _createListState: MutableStateFlow<CreateListState> = MutableStateFlow(CreateListState.Success)
    val createListState: StateFlow<CreateListState> = _createListState

    private val _userIdState: MutableStateFlow<UserIdState> = MutableStateFlow(UserIdState.Success)
    val userIdState: StateFlow<UserIdState> = _userIdState

    fun createList(
        name: String,
        tag: String,
        sharedMail: String,
        description: String
    ) {
        viewModelScope.launch {
            _createListState.value = CreateListState.Loading
            _createListState.value = createListUseCase(name, tag, sharedMail, description)
        }
    }
}

sealed class CreateListState {
    data object Loading : CreateListState()
    data object Success : CreateListState()
    data class Redirect(val listId: String) : CreateListState()
    data class Error(val message: String) : CreateListState()
}

sealed class UserIdState {
    data object None : UserIdState()
    data object Loading : UserIdState()
    data object Success : UserIdState()
    data class Error(val message: String) : UserIdState()
}