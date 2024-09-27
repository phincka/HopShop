package com.example.hopshop.domain.repository

import com.example.hopshop.data.model.ItemModel
import com.example.hopshop.presentation.list.ClearListItemsState
import com.example.hopshop.presentation.list.RemoveItemState
import com.example.hopshop.presentation.list.SetItemSelectedState

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