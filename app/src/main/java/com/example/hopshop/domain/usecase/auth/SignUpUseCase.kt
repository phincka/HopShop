package com.example.hopshop.domain.usecase.auth

import com.example.hopshop.domain.repository.AuthRepository
import org.koin.core.annotation.Single

@Single
class SignUpUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(name: String, email: String, password: String, repeatPassword: String) = authRepository.firebaseEmailSignUp(name, email, password, repeatPassword)
}