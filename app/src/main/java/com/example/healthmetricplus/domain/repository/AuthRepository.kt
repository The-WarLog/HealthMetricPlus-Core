package com.example.healthmetricplus.domain.repository

import android.content.Context
import com.example.healthmetricplus.domain.model.AuthStatus
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val authStatus: Flow<AuthStatus>
    suspend fun signInAnonymously(): Result<Boolean>
    suspend fun signIn(context: Context): Result<Boolean>
    suspend fun anonymousUserSignInWithGoogle(context: Context): Result<Boolean>
    suspend fun signOut(): Result<Boolean>
}