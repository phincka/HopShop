package com.example.hopshop.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hopshop.data.model.ItemsCountModel
import com.example.hopshop.data.model.ListModel
import com.example.hopshop.data.util.AccountUserState
import com.example.hopshop.domain.usecase.auth.GetCurrentUserUseCase
import com.example.hopshop.domain.usecase.list.CreateListUseCase
import com.example.hopshop.domain.usecase.list.GetListsUseCase
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class DashboardViewModel(
    private val getListsUseCase: GetListsUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val createListUseCase: CreateListUseCase,
    private val firebaseFireStore: FirebaseFirestore,
) : ViewModel() {
    private val _accountUserState = MutableStateFlow<AccountUserState>(AccountUserState.None)
    val accountUserState = _accountUserState.asStateFlow()

    private val _listsState: MutableStateFlow<ListsState> = MutableStateFlow(ListsState.Loading)
    val listsState: StateFlow<ListsState> = _listsState

    private val _createListState: MutableStateFlow<CreateListState> = MutableStateFlow(CreateListState.None)
    val createListState: StateFlow<CreateListState> = _createListState

    init {
        getCurrentUser()
        getLists()

        firebaseFireStore.collection("lists")
            .addSnapshotListener { snapshots, e ->
                if (e != null) return@addSnapshotListener
                getLists()
            }
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

sealed class CreateListState {
    data object Loading : CreateListState()
    data object None : CreateListState()
    data class Success(val list: ListModel? = null) : CreateListState()
    data class Redirect(val listId: String) : CreateListState()
    data class Error(val message: String) : CreateListState()
}

sealed class UserIdState {
    data object None : UserIdState()
    data object Loading : UserIdState()
    data object Success : UserIdState()
    data class Error(val message: String) : UserIdState()
}