package pl.hincka.hopshop.domain.usecase.list

import pl.hincka.hopshop.data.model.ListModel
import pl.hincka.hopshop.domain.repository.ListRepository
import pl.hincka.hopshop.presentation.dashboard.ListsState
import org.koin.core.annotation.Single

@Single
class GetListsUseCase(
    private val listRepository: ListRepository
) {
    suspend operator fun invoke(): ListsState {
        return listRepository.getLists()
    }
}