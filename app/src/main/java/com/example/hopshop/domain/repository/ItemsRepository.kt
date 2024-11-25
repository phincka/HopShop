package pl.hincka.hopshop.domain.repository

import pl.hincka.hopshop.data.model.ItemModel
import pl.hincka.hopshop.presentation.list.ClearListItemsState
import pl.hincka.hopshop.presentation.list.RemoveItemState
import pl.hincka.hopshop.presentation.list.SetItemSelectedState

interface ItemsRepository {
    suspend fun getItemsByListId(listId: String): List<ItemModel>

    suspend fun createItem(
        name: String,
        listId: String
    ): String

    suspend fun removeItem(
        itemId: String
    ): RemoveItemState

    suspend fun clearListItems(listId: String): ClearListItemsState

    suspend fun setItemSelected(
        itemId: String,
        isSelected: Boolean
    ): SetItemSelectedState
}