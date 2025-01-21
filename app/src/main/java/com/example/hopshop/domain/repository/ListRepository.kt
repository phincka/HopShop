package pl.hincka.hopshop.domain.repository

import pl.hincka.hopshop.data.model.FormListModel
import pl.hincka.hopshop.data.model.ItemsCountModel
import pl.hincka.hopshop.data.model.ListModel
import pl.hincka.hopshop.presentation.dashboard.CreateListState
import pl.hincka.hopshop.presentation.dashboard.ListsState
import pl.hincka.hopshop.presentation.dashboard.ShareListState
import pl.hincka.hopshop.presentation.list.RemoveListState
import pl.hincka.hopshop.presentation.list.RemoveSharedListState


interface ListRepository {
    suspend fun getLists(): ListsState

    suspend fun getListById(listId: String): ListModel?

    suspend fun getListByShareCode(shareCode: String): ListModel?

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
    ): ShareListState

    suspend fun generateShareCode(
        listId: String,
    ): String?

    suspend fun removeSharedList(
        listId: String,
    ): RemoveSharedListState

    suspend fun removeList(listId: String): RemoveListState
}