package com.example.hopshop.presentation.list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hopshop.data.model.ItemModel
import com.example.hopshop.data.model.ItemsCountModel
import com.example.hopshop.data.model.ListModel
import com.example.hopshop.domain.usecase.auth.GetCurrentUserUseCase
import com.example.hopshop.domain.usecase.item.CreateItemUseCase
import com.example.hopshop.domain.usecase.item.GetItemsByListIdUseCase
import com.example.hopshop.domain.usecase.item.RemoveItemUseCase
import com.example.hopshop.domain.usecase.item.SetItemSelectedUseCase
import com.example.hopshop.domain.usecase.list.GetListByIdUseCase
import com.example.hopshop.domain.usecase.list.GetListItemsCountUseCase
import com.example.hopshop.domain.usecase.list.GetListsUseCase
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class ListViewModel(
    listId: String,
    private val getListByIdUseCase: GetListByIdUseCase,
    private val getListItemsCountUseCase: GetListItemsCountUseCase,
    private val getItemsByListIdUseCase: GetItemsByListIdUseCase,
    private val createItemUseCase: CreateItemUseCase,
    private val setItemSelectedUseCase: SetItemSelectedUseCase,
    private val removeItemUseCase: RemoveItemUseCase,
    private val firebaseFireStore: FirebaseFirestore,
    ) : ViewModel() {
    private val _listState: MutableStateFlow<ListState> = MutableStateFlow(ListState.Loading)
    val listState: StateFlow<ListState> = _listState

    private val _itemsState: MutableStateFlow<ItemsState> = MutableStateFlow(ItemsState.Loading)
    val itemsState: StateFlow<ItemsState> = _itemsState

    private val _itemsCountState: MutableStateFlow<ItemsCountState> = MutableStateFlow(ItemsCountState.Loading)
    val itemsCountState: StateFlow<ItemsCountState> = _itemsCountState

    private val _createItemState: MutableStateFlow<CreateItemState> = MutableStateFlow(CreateItemState.None)
    val createItemState: StateFlow<CreateItemState> = _createItemState

    private val _setItemSelectedState: MutableStateFlow<SetItemSelectedState> = MutableStateFlow(SetItemSelectedState.None)
    val setItemSelectedState: StateFlow<SetItemSelectedState> = _setItemSelectedState

    private val _removeItemState: MutableStateFlow<RemoveItemState> = MutableStateFlow(RemoveItemState.None)
    val removeItemState: StateFlow<RemoveItemState> = _removeItemState

    init {
        getListById(listId = listId)

        firebaseFireStore.collection("items")
            .whereEqualTo("listId", listId)
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    Log.w("LOG_HIN", "listen:error", e)
                    return@addSnapshotListener
                }

                getItemsByListId(listId = listId)

                Log.w("LOG_HIN", "INIT")
            }

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

                Log.w("LOG_HIN", items.toString())
                Log.w("LOG_HIN", _itemsState.value.toString())

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
        viewModelScope.launch {
            try {
                _createItemState.value = CreateItemState.Loading
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
