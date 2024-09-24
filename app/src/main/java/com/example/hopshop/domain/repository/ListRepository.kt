package com.example.hopshop.domain.repository

import com.example.hopshop.data.model.FormListModel
import com.example.hopshop.data.model.ItemsCountModel
import com.example.hopshop.data.model.ListModel
import com.example.hopshop.presentation.dashboard.CreateListState
import com.example.hopshop.presentation.dashboard.ListsState
import com.example.hopshop.presentation.dashboard.RemoveSharedListState
import com.example.hopshop.presentation.dashboard.ShareListState
import com.example.hopshop.presentation.list.ClearListItemsState
import com.example.hopshop.presentation.list.RemoveListState


interface ListRepository {
    suspend fun getLists(): ListsState

    suspend fun getListById(listId: String): ListModel?

    suspend fun getListItemsCount(listId: String): ItemsCountModel

    suspend fun createList(formListModel: FormListModel): CreateListState

    suspend fun editList(
        listId: String,
        name: String,
        tag: String,
        description: String
    ): CreateListState

    suspend fun shareList(
        listId: String,
        email: String,
    ): ShareListState

    suspend fun removeSharedList(
        listId: String,
        email: String,
    ): RemoveSharedListState

    suspend fun removeList(listId: String): RemoveListState
}