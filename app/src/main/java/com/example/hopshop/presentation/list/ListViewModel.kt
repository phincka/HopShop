package pl.hincka.hopshop.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import pl.hincka.hopshop.data.model.FormListModel
import pl.hincka.hopshop.data.model.ItemModel
import pl.hincka.hopshop.data.model.ItemsCountModel
import pl.hincka.hopshop.data.model.ListModel
import pl.hincka.hopshop.data.util.AccountUserState
import pl.hincka.hopshop.domain.usecase.auth.GetCurrentUserUseCase
import pl.hincka.hopshop.domain.usecase.item.CreateItemUseCase
import pl.hincka.hopshop.domain.usecase.item.GetItemsByListIdUseCase
import pl.hincka.hopshop.domain.usecase.item.RemoveItemUseCase
import pl.hincka.hopshop.domain.usecase.item.SetItemSelectedUseCase
import pl.hincka.hopshop.domain.usecase.list.ClearListItemsUseCase
import pl.hincka.hopshop.domain.usecase.list.EditListUseCase
import pl.hincka.hopshop.domain.usecase.list.GetListByIdUseCase
import pl.hincka.hopshop.domain.usecase.list.RemoveListUseCase
import pl.hincka.hopshop.presentation.dashboard.CreateListState
import pl.hincka.hopshop.presentation.dashboard.ShareListState
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class ListViewModel(
    private val listId: String,
    private val getListByIdUseCase: GetListByIdUseCase,
    private val getItemsByListIdUseCase: GetItemsByListIdUseCase,
    private val createItemUseCase: CreateItemUseCase,
    private val setItemSelectedUseCase: SetItemSelectedUseCase,
    private val removeItemUseCase: RemoveItemUseCase,
    private val removeListUseCase: RemoveListUseCase,
    private val clearListItemsUseCase: ClearListItemsUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val editListUseCase: EditListUseCase,
    firebaseFireStore: FirebaseFirestore,
) : ViewModel() {
    private val _accountUserState = MutableStateFlow<AccountUserState>(AccountUserState.None)
    val accountUserState = _accountUserState.asStateFlow()

    private val _listState: MutableStateFlow<ListState> = MutableStateFlow(ListState.Loading)
    val listState: StateFlow<ListState> = _listState

    private val _itemsState: MutableStateFlow<ItemsState> = MutableStateFlow(ItemsState.Loading)
    val itemsState: StateFlow<ItemsState> = _itemsState

    private val _itemsCountState: MutableStateFlow<ItemsCountState> =
        MutableStateFlow(ItemsCountState.Loading)
    val itemsCountState: StateFlow<ItemsCountState> = _itemsCountState

    private val _createItemState: MutableStateFlow<CreateItemState> =
        MutableStateFlow(CreateItemState.None)
    val createItemState: StateFlow<CreateItemState> = _createItemState

    private val _setItemSelectedState: MutableStateFlow<SetItemSelectedState> =
        MutableStateFlow(SetItemSelectedState.None)
    val setItemSelectedState: StateFlow<SetItemSelectedState> = _setItemSelectedState

    private val _removeItemState: MutableStateFlow<RemoveItemState> =
        MutableStateFlow(RemoveItemState.None)
    val removeItemState: StateFlow<RemoveItemState> = _removeItemState

    private val _removeListState: MutableStateFlow<RemoveListState> =
        MutableStateFlow(RemoveListState.None)
    val removeListState: StateFlow<RemoveListState> = _removeListState

    private val _clearListItemsState: MutableStateFlow<ClearListItemsState> =
        MutableStateFlow(ClearListItemsState.None)
    val clearListItemsState: StateFlow<ClearListItemsState> = _clearListItemsState

    private val _shareListState: MutableStateFlow<ShareListState> =
        MutableStateFlow(ShareListState.None)
    val shareListState: StateFlow<ShareListState> = _shareListState

    private val _createListState: MutableStateFlow<CreateListState> = MutableStateFlow(
        CreateListState.None)
    val createListState: StateFlow<CreateListState> = _createListState

    init {
        getCurrentUser()
        getListById(listId = listId)

        firebaseFireStore.collection("items")
            .whereEqualTo("listId", listId)
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    return@addSnapshotListener
                }

                getItemsByListId(listId = listId)
            }
    }

    private fun getCurrentUser() = viewModelScope.launch {
        _accountUserState.value = AccountUserState.Loading
        _accountUserState.value = getCurrentUserUseCase()
    }

    private fun getListById(listId: String) {
        _listState.value = ListState.Loading

        viewModelScope.launch {
            try {
                val list = getListByIdUseCase(listId = listId)

                if (list != null) {
                    _listState.value = ListState.Success(list)
                } else {
                    _listState.value = ListState.Error("Brak list o podanym id")
                }
            } catch (e: Exception) {
                _listState.value = ListState.Error("Failed: ${e.message}")
            }
        }
    }

    private fun getItemsByListId(listId: String) {
        _itemsState.value = ItemsState.Loading

        viewModelScope.launch {
            try {
                val items = getItemsByListIdUseCase(listId = listId)
                getListItemsCount(items = items)

                _itemsState.value = ItemsState.Success(items = items)
            } catch (e: Exception) {
                _itemsState.value = ItemsState.Error("Failed: ${e.message}")
            }
        }
    }

    private fun getListItemsCount(items: List<ItemModel>) {
        val selectedItems = items.filter { it.selected }
        val itemsCount = ItemsCountModel(
            items = items.size,
            selected = selectedItems.size
        )

        _itemsCountState.value = ItemsCountState.Success(itemsCount = itemsCount)
    }

    fun createItem(
        name: String,
        listId: String,
    ) {
        _createItemState.value = CreateItemState.Loading

        viewModelScope.launch {
            try {
                createItemUseCase(name, listId)

                _createItemState.value = CreateItemState.Success
                delay(500)
                _createItemState.value = CreateItemState.None
            } catch (e: Exception) {
                _createItemState.value = CreateItemState.Error("${e.message}")
            }
        }
    }

    fun removeItem(
        itemId: String,
    ) {
        _removeItemState.value = RemoveItemState.None

        viewModelScope.launch {
            try {
                _removeItemState.value = removeItemUseCase(itemId = itemId)
            } catch (e: Exception) {
                _removeItemState.value = RemoveItemState.Error("${e.message}")
            }
        }
    }

    fun setItemSelected(
        itemId: String,
        isSelected: Boolean,
    ) {
        viewModelScope.launch {
            try {
                _setItemSelectedState.value = setItemSelectedUseCase(itemId, isSelected)
            } catch (e: Exception) {
                _setItemSelectedState.value = SetItemSelectedState.Error("${e.message}")
            }

        }
    }

    fun removeList(
        listId: String,
    ) {
        viewModelScope.launch {
            try {
                _removeListState.value = removeListUseCase(listId = listId)
            } catch (e: Exception) {
                _removeListState.value = RemoveListState.Error("${e.message}")
            }
        }
    }

    fun clearListItems() {
        viewModelScope.launch {
            try {
                _clearListItemsState.value = clearListItemsUseCase(listId = listId)
                _clearListItemsState.value = ClearListItemsState.Success
            } catch (e: Exception) {
                _clearListItemsState.value = ClearListItemsState.Error("${e.message}")
            }
        }
    }

    fun editList(formListModel: FormListModel) {
        _createListState.value = CreateListState.Loading
        viewModelScope.launch {
            try {
                _createListState.value = editListUseCase(
                    listId = listId,
                    name = formListModel.name,
                    tag = formListModel.tag,
                    description = formListModel.description,
                )

                getListById(listId = listId)
                _createListState.value = CreateListState.Success
            } catch (e: Exception) {
                _createListState.value = CreateListState.Error("${e.message}")
            }
        }

    }
}

sealed class ListState {
    data object Loading : ListState()
    data class Success(val list: ListModel) : ListState()
    data class Error(val message: String) : ListState()
}

sealed class ItemsState {
    data object Loading : ItemsState()
    data class Success(val items: List<ItemModel>) : ItemsState()
    data class Error(val message: String) : ItemsState()
}

sealed class ItemsCountState {
    data object Loading : ItemsCountState()
    data class Success(val itemsCount: ItemsCountModel) : ItemsCountState()
    data class Error(val message: String) : ItemsCountState()
}

sealed class CreateItemState {
    data object Loading : CreateItemState()
    data object None : CreateItemState()
    data object Success : CreateItemState()
    data class Error(val message: String) : CreateItemState()
}

sealed class RemoveItemState {
    data object None : RemoveItemState()
    data object Success : RemoveItemState()
    data class Error(val message: String) : RemoveItemState()
}

sealed class SetItemSelectedState {
    data object None : SetItemSelectedState()
    data object Success : SetItemSelectedState()
    data class Error(val message: String) : SetItemSelectedState()
}

sealed class RemoveListState {
    data object None : RemoveListState()
    data object Success : RemoveListState()
    data class Error(val message: String) : RemoveListState()
}

sealed class ClearListItemsState {
    data object None : ClearListItemsState()
    data object Success : ClearListItemsState()
    data class Error(val message: String) : ClearListItemsState()
}