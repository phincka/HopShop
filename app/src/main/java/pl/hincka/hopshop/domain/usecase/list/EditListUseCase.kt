package pl.hincka.hopshop.domain.usecase.list

import pl.hincka.hopshop.domain.repository.ListRepository
import org.koin.core.annotation.Single

@Single
class EditListUseCase(
    private val listRepository: ListRepository
) {
    suspend operator fun invoke(
        listId: String,
        name: String,
        tag: String,
        description: String
    ) = listRepository.editList(listId, name, tag, description)
}