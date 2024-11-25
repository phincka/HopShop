package pl.hincka.hopshop.domain.usecase.list

import pl.hincka.hopshop.data.model.FormListModel
import pl.hincka.hopshop.data.model.ListModel
import pl.hincka.hopshop.domain.repository.ListRepository
import org.koin.core.annotation.Single

@Single
class CreateListUseCase(
    private val apiaryRepository: ListRepository
) {
    suspend operator fun invoke(formListModel: FormListModel) = apiaryRepository.createList(formListModel)
}