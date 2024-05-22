package com.example.hopshop.domain.usecase.item

import com.example.hopshop.data.model.ItemModel
import com.example.hopshop.data.model.ListModel
import com.example.hopshop.domain.repository.ItemsRepository
import com.example.hopshop.domain.repository.ListRepository
import org.koin.core.annotation.Single

@Single
class GetItemsByListIdUseCase(
    private val itemsRepository: ItemsRepository
) {
    suspend operator fun invoke(listId: String): List<ItemModel> {
        return itemsRepository.getItemsByListId(listId)
    }
}