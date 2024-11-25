package pl.hincka.hopshop.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import pl.hincka.hopshop.data.model.FormListModel
import pl.hincka.hopshop.data.model.ItemsCountModel
import pl.hincka.hopshop.data.model.ListModel
import pl.hincka.hopshop.data.util.AccountUserState
import pl.hincka.hopshop.domain.usecase.auth.GetCurrentUserUseCase
import pl.hincka.hopshop.domain.usecase.auth.SignOutUseCase
import pl.hincka.hopshop.domain.usecase.list.CreateListUseCase
import pl.hincka.hopshop.domain.usecase.list.GetListsUseCase
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
    private val signOutUseCase: SignOutUseCase,
    firebaseFireStore: FirebaseFirestore,
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
            .addSnapshotListener { _, e ->
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

    fun createList(formListModel: FormListModel) {
        viewModelScope.launch {
            _createListState.value = CreateListState.Loading
            _createListState.value = createListUseCase(formListModel)
        }
    }

    fun signOut() = viewModelScope.launch {
        _accountUserState.value = AccountUserState.Loading
        _accountUserState.value = signOutUseCase()
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