package pl.hincka.hopshop.domain.usecase.item

import pl.hincka.hopshop.data.model.ListModel
import pl.hincka.hopshop.domain.repository.ItemsRepository
import pl.hincka.hopshop.domain.repository.ListRepository
import org.koin.core.annotation.Single

@Single
class CreateItemUseCase(
    private val itemsRepository: ItemsRepository
) {
    suspend operator fun invoke(
        name: String,
        listId: String,
    ) = itemsRepository.createItem(name, listId)
}