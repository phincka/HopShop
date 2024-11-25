package pl.hincka.hopshop.domain.repository

import pl.hincka.hopshop.data.util.AccountUserState
import pl.hincka.hopshop.data.util.AuthState


interface AuthRepository {
    suspend fun getCurrentUser(): AccountUserState
    suspend fun firebaseEmailSignIn(email: String, password: String): AuthState
    suspend fun firebaseEmailSignUp(name: String, email: String, password: String, repeatPassword: String): AuthState
    suspend fun firebaseSignOut(): AccountUserState
    suspend fun checkEmailVerification(): AuthState
    suspend fun resendVerificationEmail(): AuthState
}