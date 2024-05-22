package com.example.hopshop.domain.repository

import com.example.hopshop.data.util.AccountUserState
import com.example.hopshop.data.util.AuthState


interface AuthRepository {
    suspend fun getCurrentUser(): AccountUserState
    suspend fun firebaseEmailSignIn(email: String, password: String): AuthState
    suspend fun firebaseEmailSignUp(email: String, password: String, repeatPassword: String): AuthState
    suspend fun firebaseSignOut(): AccountUserState
    suspend fun checkEmailVerification(): AuthState
    suspend fun resendVerificationEmail(): AuthState
}