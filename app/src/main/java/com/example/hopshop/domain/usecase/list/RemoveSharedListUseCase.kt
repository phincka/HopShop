package com.example.hopshop.domain.usecase.list

import com.example.hopshop.domain.repository.ListRepository
import org.koin.core.annotation.Single

@Single
class RemoveSharedListUseCase(
    private val listRepository: ListRepository
) {
    suspend operator fun invoke(
        listId: String,
        email: String,
    ) = listRepository.removeSharedList(
        listId = listId,
        email = email
    )
}