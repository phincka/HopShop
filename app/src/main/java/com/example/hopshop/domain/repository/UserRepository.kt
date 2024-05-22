package com.example.hopshop.domain.repository

import com.example.hopshop.data.util.AccountUserState
import com.example.hopshop.data.util.AuthState


interface UserRepository {
    suspend fun getUserIdByEmail(email: String): String
}