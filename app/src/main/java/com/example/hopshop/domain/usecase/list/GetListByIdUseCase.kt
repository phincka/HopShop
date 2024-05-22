package com.example.hopshop.domain.usecase.list

import com.example.hopshop.data.model.ListModel
import com.example.hopshop.domain.repository.ListRepository
import org.koin.core.annotation.Single

@Single
class GetListByIdUseCase(
    private val listRepository: ListRepository
) {
    suspend operator fun invoke(listId: String): ListModel? {
        return listRepository.getListById(listId)
    }
}