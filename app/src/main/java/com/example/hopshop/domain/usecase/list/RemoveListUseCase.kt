package pl.hincka.hopshop.domain.usecase.list

import pl.hincka.hopshop.domain.repository.ListRepository
import org.koin.core.annotation.Single

@Single
class RemoveListUseCase(
    private val listRepository: ListRepository
) {
    suspend operator fun invoke(
        listId: String,
    ) = listRepository.removeList(listId)
}