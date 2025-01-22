package com.example.gopaywallet.data.repository

import android.util.Log
import com.example.gopaywallet.data.api.AuthApi
import com.example.gopaywallet.data.model.LoginRequest
import com.example.gopaywallet.data.model.RegisterRequest
import com.example.gopaywallet.data.model.User
import com.example.gopaywallet.data.model.LoginResponse

class UserRepositoryImpl(private val authApi: AuthApi) : UserRepository {

    override suspend fun login(email: String, password: String): LoginResponse {
        try {
            Log.d("UserRepositoryImpl", "Making login API call")
            val response = authApi.login(LoginRequest(email, password))
            Log.d("UserRepositoryImpl", "Login API call successful")
            return response
        } catch (e: Exception) {
            Log.e("UserRepositoryImpl", "Login API call failed", e)
            throw e
        }
    }

    override suspend fun register(
        fullName: String, email: String, phoneNumber: String, password: String
    ): User {
        val response = authApi.register(
            RegisterRequest(fullName, email, phoneNumber, password)
        )
        // Save token for future requests
        saveAuthToken(response.token)
        return response.user
    }

    override suspend fun resetPassword(email: String) {
        authApi.forgotPassword(email)
    }

    override suspend fun getCurrentUser(): User {
        // Implement getting current user from API
        TODO("Not yet implemented")
    }

    override suspend fun updateProfile(fullName: String, email: String, phoneNumber: String) {
        // Implement profile update
        TODO("Not yet implemented")
    }

    private fun saveAuthToken(token: String) {
        // Implement token storage (e.g., in SharedPreferences)
        // This should be done securely, possibly using EncryptedSharedPreferences
    }
} 