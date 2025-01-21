package pl.hincka.hopshop.domain.usecase.list

import pl.hincka.hopshop.data.model.ListModel
import pl.hincka.hopshop.domain.repository.ListRepository
import org.koin.core.annotation.Single

@Single
class GetListByShareCodeUseCase(
    private val listRepository: ListRepository
) {
    suspend operator fun invoke(shareCode: String): ListModel? {
        return listRepository.getListByShareCode(shareCode)
    }
}