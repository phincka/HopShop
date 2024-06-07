package com.example.hopshop.domain.usecase.list

import com.example.hopshop.data.model.ListModel
import com.example.hopshop.domain.repository.ListRepository
import org.koin.core.annotation.Single

@Single
class EditListUseCase(
    private val apiaryRepository: ListRepository
) {
    suspend operator fun invoke(
        listId: String,
        name: String,
        tag: String,
        description: String
    ) = apiaryRepository.editList(listId, name, tag, description)
}