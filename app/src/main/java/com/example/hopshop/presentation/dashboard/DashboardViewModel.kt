package com.example.hopshop.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hopshop.data.model.ItemsCountModel
import com.example.hopshop.data.model.ListModel
import com.example.hopshop.data.util.AccountUserState
import com.example.hopshop.domain.usecase.auth.GetCurrentUserUseCase
import com.example.hopshop.domain.usecase.list.GetListsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class DashboardViewModel(
    private val getListsUseCase: GetListsUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
) : ViewModel() {
    private val _accountUserState = MutableStateFlow<AccountUserState>(AccountUserState.None)
    val accountUserState = _accountUserState.asStateFlow()

    private val _listsState: MutableStateFlow<ListsState> = MutableStateFlow(ListsState.Loading)
    val listsState: StateFlow<ListsState> = _listsState

    init {
        getCurrentUser()
        getLists()
    }

    private fun getLists() {
        _listsState.value = ListsState.Loading

        viewModelScope.launch {
            try {
                _listsState.value = getListsUseCase()
            } catch (e: Exception) {
                _listsState.value = ListsState.Error(e.message.toString())
            }
        }
    }

    private fun getCurrentUser() = viewModelScope.launch {
        _accountUserState.value = AccountUserState.Loading
        _accountUserState.value = getCurrentUserUseCase()
    }
}


sealed class ListsState {
    data object None : ListsState()
    data object Loading : ListsState()
    data class Success(
        val lists: List<ListModel>,
        val itemsCount: ItemsCountModel
    ) : ListsState()

    data class Error(val message: String) : ListsState()
}

sealed class ShareListState {
    data object None : ShareListState()
    data object Loading : ShareListState()
    data object Success : ShareListState()
    data class Error(val message: String) : ShareListState()
}

sealed class RemoveSharedListState {
    data object None : RemoveSharedListState()
    data object Loading : RemoveSharedListState()
    data object Success : RemoveSharedListState()
    data class Error(val message: String) : RemoveSharedListState()
}