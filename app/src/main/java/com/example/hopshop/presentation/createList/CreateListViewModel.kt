package com.example.hopshop.presentation.createList

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hopshop.data.model.ListModel
import com.example.hopshop.domain.usecase.list.CreateListUseCase
import com.example.hopshop.domain.usecase.list.EditListUseCase
import com.example.hopshop.domain.usecase.list.GetListByIdUseCase
import com.example.hopshop.presentation.list.ListState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class CreateListViewModel(
    listId: String?,
    private val getListByIdUseCase: GetListByIdUseCase,
    private val createListUseCase: CreateListUseCase,
    private val editListUseCase: EditListUseCase,
) : ViewModel() {
    private val _createListState: MutableStateFlow<CreateListState> = MutableStateFlow(CreateListState.None)
    val createListState: StateFlow<CreateListState> = _createListState

    init {
        if (listId != null) {
            getListById(listId)
        } else {
            _createListState.value = CreateListState.Success()
        }
    }

    private fun getListById(listId: String) {
        _createListState.value = CreateListState.Loading

        viewModelScope.launch {
            try {
                val list = getListByIdUseCase(listId = listId)

                if (list != null) {
                    _createListState.value = CreateListState.Success(list)
                } else {
                    _createListState.value = CreateListState.Error("Brak list o podanym id")
                }
            } catch (e: Exception) {
                _createListState.value = CreateListState.Error("Failed: ${e.message}")
            }
        }
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

    fun editList(
        listId: String,
        name: String,
        tag: String,
        description: String,
    ) {
        viewModelScope.launch {
            _createListState.value = CreateListState.Loading
            _createListState.value = editListUseCase(listId, name, tag, description)
        }
    }
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