package com.example.hopshop.domain.repository

import com.example.hopshop.data.model.ItemsCountModel
import com.example.hopshop.data.model.ListModel
import com.example.hopshop.presentation.createList.CreateListState
import com.example.hopshop.presentation.dashboard.ListsState


interface ListRepository {
    suspend fun getLists(): ListsState

    suspend fun getListById(listId: String): ListModel?

    suspend fun getListItemsCount(listId: String): ItemsCountModel

    suspend fun createList(
        name: String,
        tag: String,
        sharedMail: String,
        description: String
    ): CreateListState
}