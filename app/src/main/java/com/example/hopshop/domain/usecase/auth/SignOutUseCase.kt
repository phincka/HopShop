package com.example.hopshop.domain.usecase.auth

import com.example.hopshop.domain.repository.AuthRepository
import org.koin.core.annotation.Single

@Single
class SignOutUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke() = authRepository.firebaseSignOut()
}