package com.example.hopshop.domain.usecase.list

import com.example.hopshop.data.model.ItemsCountModel
import com.example.hopshop.data.model.ListModel
import com.example.hopshop.domain.repository.ListRepository
import org.koin.core.annotation.Single

@Single
class GetListItemsCountUseCase(
    private val listRepository: ListRepository
) {
    suspend operator fun invoke(listId: String): ItemsCountModel {
        return listRepository.getListItemsCount(listId = listId)
    }
}