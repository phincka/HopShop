package com.example.hopshop.domain.usecase.list

import com.example.hopshop.data.model.FormListModel
import com.example.hopshop.data.model.ListModel
import com.example.hopshop.domain.repository.ListRepository
import org.koin.core.annotation.Single

@Single
class CreateListUseCase(
    private val apiaryRepository: ListRepository
) {
    suspend operator fun invoke(formListModel: FormListModel) = apiaryRepository.createList(formListModel)
}