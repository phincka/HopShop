package pl.hincka.hopshop.domain.repository

import pl.hincka.hopshop.data.util.AccountUserState
import pl.hincka.hopshop.data.util.AuthState


interface UserRepository {
    suspend fun getUserIdByEmail(email: String): String
}