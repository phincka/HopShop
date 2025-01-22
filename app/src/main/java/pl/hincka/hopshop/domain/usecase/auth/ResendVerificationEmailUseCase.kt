package pl.hincka.hopshop.domain.usecase.auth

import pl.hincka.hopshop.domain.repository.AuthRepository
import org.koin.core.annotation.Single

@Single
class ResendVerificationEmailUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke() = authRepository.resendVerificationEmail()
}