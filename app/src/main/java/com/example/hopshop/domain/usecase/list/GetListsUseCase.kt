package com.example.hopshop.domain.usecase.list

import com.example.hopshop.data.model.ListModel
import com.example.hopshop.domain.repository.ListRepository
import com.example.hopshop.presentation.dashboard.ListsState
import org.koin.core.annotation.Single

@Single
class GetListsUseCase(
    private val listRepository: ListRepository
) {
    suspend operator fun invoke(): ListsState {
        return listRepository.getLists()
    }
}