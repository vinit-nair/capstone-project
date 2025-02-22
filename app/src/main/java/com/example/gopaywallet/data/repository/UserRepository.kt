package com.example.gopaywallet.data.repository

import com.example.gopaywallet.data.model.LoginResponse
import com.example.gopaywallet.data.model.User
import com.example.gopaywallet.data.model.ForgotPasswordResponse
import com.example.gopaywallet.data.model.ProfileUpdateRequest

interface UserRepository {
    suspend fun login(email: String, password: String): LoginResponse
    suspend fun register(fullName: String, email: String, phoneNumber: String, password: String): User
    suspend fun resetPassword(email: String, otp: String, newPassword: String)
    suspend fun getCurrentUser(): User?
    suspend fun updateUserDetails(updatedDetails: ProfileUpdateRequest): User
    suspend fun verifyOtp(email: String, otp: String)
    suspend fun forgotPassword(email: String): ForgotPasswordResponse
}