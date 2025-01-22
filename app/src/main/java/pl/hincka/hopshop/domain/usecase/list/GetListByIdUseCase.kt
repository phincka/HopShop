package pl.hincka.hopshop.domain.usecase.list

import pl.hincka.hopshop.data.model.ListModel
import pl.hincka.hopshop.domain.repository.ListRepository
import org.koin.core.annotation.Single

@Single
class GetListByIdUseCase(
    private val listRepository: ListRepository
) {
    suspend operator fun invoke(listId: String): ListModel? {
        return listRepository.getListById(listId)
    }
}