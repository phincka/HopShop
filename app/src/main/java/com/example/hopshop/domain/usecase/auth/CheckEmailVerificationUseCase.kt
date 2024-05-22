package com.example.hopshop.domain.usecase.auth

import com.example.hopshop.domain.repository.AuthRepository
import org.koin.core.annotation.Single

@Single
class CheckEmailVerificationUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke() = authRepository.checkEmailVerification()
}