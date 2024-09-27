package com.example.hopshop.data.repository

import android.content.Context
import android.util.Log
import com.example.hopshop.R
import com.example.hopshop.data.model.UserModel
import com.example.hopshop.data.util.AccountUserState
import com.example.hopshop.data.util.AuthState
import com.example.hopshop.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.userProfileChangeRequest
import kotlinx.coroutines.tasks.await
import org.koin.core.annotation.Single


@Single
class AuthRepositoryImpl(
    private val firebaseAuth: FirebaseAuth,
    private val context: Context
): AuthRepository {
    override suspend fun getCurrentUser(): AccountUserState = try {
        val user = firebaseAuth.currentUser

        if (user != null) {
            AccountUserState.SignedInState(
                UserModel(
                    userId = user.uid,
                    name = user.displayName ?: "",
                    email = user.email ?: "",
                    isEmailVerified = user.isEmailVerified
                )
            )
        } else {
            AccountUserState.GuestState
        }
    } catch (e: Exception) {
        AccountUserState.Error("Failed: ${e.message}")
    }

    override suspend fun firebaseEmailSignIn(
        email: String,
        password: String
    ): AuthState {
        return try {
            firebaseAuth.signInWithEmailAndPassword(email, password).await()
            AuthState.Success(true)
        } catch (error: Exception) {
            AuthState.Error(error.toString())
        }
    }

    override suspend fun firebaseEmailSignUp(
        name: String,
        email: String,
        password: String,
        repeatPassword: String
    ): AuthState {
        return if (password == repeatPassword) {
            try {
                firebaseAuth.createUserWithEmailAndPassword(email, password).await()
                firebaseEmailSignIn(email, password)

                val currentUser = firebaseAuth.currentUser
                if (currentUser != null) {
                    currentUser.sendEmailVerification().await()

                    val profileUpdates = userProfileChangeRequest {
                        displayName = name
                    }

                    currentUser.updateProfile(profileUpdates)
                    AuthState.Success(true)
                } else {
                    AuthState.Error(context.getString(R.string.auth_state_no_user))
                }
            } catch (error: Exception) {
                AuthState.Error(error.toString())
            }
        } else {
            AuthState.Error(context.getString(R.string.auth_state_passwords_not_equal))
        }
    }

    override suspend fun firebaseSignOut() = try {
        firebaseAuth.signOut()
        AccountUserState.GuestState
    } catch (error: Exception) {
        AccountUserState.Error(error.toString())
    }

    override suspend fun checkEmailVerification(): AuthState {
        val currentUser = firebaseAuth.currentUser

        return if (currentUser != null) {
            try {
                currentUser.reload().await()

                if (currentUser.isEmailVerified) {
                    AuthState.Success(true)
                } else {
                    AuthState.Success(
                        false,
                        message = context.getString(R.string.auth_state_verification_error)
                    )
                }
            } catch (e: Exception) {
                AuthState.Error("${context.getString(R.string.auth_state_update_error)} ${e.message}")
            }
        } else {
            AuthState.Error(context.getString(R.string.auth_state_no_user))
        }
    }

    override suspend fun resendVerificationEmail(): AuthState {
        val currentUser = firebaseAuth.currentUser

        return if (currentUser != null) {
            try {
                currentUser.sendEmailVerification()
                AuthState.Success(
                    success = false,
                    message = context.getString(R.string.auth_state_email_send)
                )
            } catch (e: Exception) {
                AuthState.Error("${context.getString(R.string.auth_state_email_not_send)} ${e.message}")
            }
        } else {
            AuthState.Error(context.getString(R.string.auth_state_no_user))
        }
    }
}