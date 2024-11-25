package pl.hincka.hopshop.domain.usecase.list

import pl.hincka.hopshop.domain.repository.ListRepository
import org.koin.core.annotation.Single

@Single
class ShareListUseCase(
    private val listRepository: ListRepository
) {
    suspend operator fun invoke(
        listId: String,
        email: String,
    ) = listRepository.shareList(
        listId = listId,
        email = email
    )
}