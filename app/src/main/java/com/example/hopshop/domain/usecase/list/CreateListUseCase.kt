package com.example.hopshop.domain.usecase.list

import com.example.hopshop.data.model.ListModel
import com.example.hopshop.domain.repository.ListRepository
import org.koin.core.annotation.Single

@Single
class CreateListUseCase(
    private val apiaryRepository: ListRepository
) {
    suspend operator fun invoke(
        name: String,
        tag: String,
        sharedMail: String,
        description: String
    ) = apiaryRepository.createList(name, tag, sharedMail, description)
}