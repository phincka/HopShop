package pl.hincka.hopshop.domain.usecase.auth

import pl.hincka.hopshop.domain.repository.AuthRepository
import org.koin.core.annotation.Single

@Single
class SignInUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String) = authRepository.firebaseEmailSignIn(email, password)
}