package com.example.gopaywallet.data.repository

import com.example.gopaywallet.data.api.AuthApi
import com.example.gopaywallet.data.model.ForgotPasswordRequest
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val authApi: AuthApi
) {
    // ... existing repository methods ...

    suspend fun forgotPassword(email: String) = 
        authApi.forgotPassword(ForgotPasswordRequest(email))
} 