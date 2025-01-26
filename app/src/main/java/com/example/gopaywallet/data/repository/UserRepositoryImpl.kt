package com.example.gopaywallet.data.repository

import android.util.Log
import com.example.gopaywallet.data.api.AuthApi
import com.example.gopaywallet.data.model.LoginRequest
import com.example.gopaywallet.data.model.RegisterRequest
import com.example.gopaywallet.data.model.User
import com.example.gopaywallet.data.model.LoginResponse
import com.example.gopaywallet.data.model.ForgotPasswordRequest
import com.example.gopaywallet.data.model.ForgotPasswordResponse
import com.example.gopaywallet.data.model.VerifyOtpRequest
import com.example.gopaywallet.data.model.ResetPasswordRequest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(private val authApi: AuthApi) : UserRepository {

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

    override suspend fun resetPassword(email: String, otp: String, newPassword: String) {
        authApi.resetPassword(ResetPasswordRequest(email, otp, newPassword))
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

    override suspend fun forgotPassword(email: String): ForgotPasswordResponse {
        try {
            return authApi.forgotPassword(ForgotPasswordRequest(email))
        } catch (e: retrofit2.HttpException) {
            when (e.code()) {
                400 -> throw Exception("Email not registered")
                else -> throw Exception("Failed to send reset email")
            }
        } catch (e: Exception) {
            throw Exception(e.message ?: "Failed to send reset email")
        }
    }

    override suspend fun verifyOtp(email: String, otp: String) {
        authApi.verifyOtp(VerifyOtpRequest(email, otp))
    }
} 