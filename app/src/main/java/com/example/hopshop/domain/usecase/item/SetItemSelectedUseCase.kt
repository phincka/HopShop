package com.example.hopshop.domain.usecase.item

import com.example.hopshop.data.model.ListModel
import com.example.hopshop.domain.repository.ItemsRepository
import com.example.hopshop.domain.repository.ListRepository
import org.koin.core.annotation.Single

@Single
class SetItemSelectedUseCase(
    private val itemsRepository: ItemsRepository
) {
    suspend operator fun invoke(
        itemId: String,
        isSelected: Boolean,
    ) = itemsRepository.setItemSelected(itemId, isSelected)
}