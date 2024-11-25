package pl.hincka.hopshop.domain.usecase.item

import pl.hincka.hopshop.data.model.ItemModel
import pl.hincka.hopshop.data.model.ListModel
import pl.hincka.hopshop.domain.repository.ItemsRepository
import pl.hincka.hopshop.domain.repository.ListRepository
import org.koin.core.annotation.Single

@Single
class GetItemsByListIdUseCase(
    private val itemsRepository: ItemsRepository
) {
    suspend operator fun invoke(listId: String): List<ItemModel> {
        return itemsRepository.getItemsByListId(listId)
    }
}