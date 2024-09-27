package com.example.hopshop.domain.usecase.list

import com.example.hopshop.domain.repository.ItemsRepository
import org.koin.core.annotation.Single

@Single
class ClearListItemsUseCase(
    private val listRepository: ItemsRepository
) {
    suspend operator fun invoke(
        listId: String,
    ) = listRepository.clearListItems(listId)
}