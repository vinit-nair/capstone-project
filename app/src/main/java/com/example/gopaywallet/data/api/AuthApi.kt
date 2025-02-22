package com.example.gopaywallet.data.api

import com.example.gopaywallet.data.model.BaseResponse
import com.example.gopaywallet.data.model.LoginRequest
import com.example.gopaywallet.data.model.LoginResponse
import com.example.gopaywallet.data.model.RegisterRequest
import com.example.gopaywallet.data.model.RegisterResponse
import com.example.gopaywallet.data.model.ForgotPasswordRequest
import com.example.gopaywallet.data.model.ForgotPasswordResponse
import com.example.gopaywallet.data.model.ProfileUpdateRequest
import com.example.gopaywallet.data.model.VerifyOtpRequest
import com.example.gopaywallet.data.model.ResetPasswordRequest
import com.example.gopaywallet.data.model.User
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface AuthApi {
    @Headers("Content-Type: application/json")
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @Headers("Content-Type: application/json")
    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): RegisterResponse

    @Headers("Content-Type: application/json")
    @POST("auth/forgot-password")
    suspend fun forgotPassword(@Body request: ForgotPasswordRequest): ForgotPasswordResponse

    @Headers("Content-Type: application/json")
    @POST("auth/verify-otp")
    suspend fun verifyOtp(@Body request: VerifyOtpRequest): BaseResponse

    @Headers("Content-Type: application/json")
    @POST("auth/reset-password")
    suspend fun resetPassword(@Body request: ResetPasswordRequest): BaseResponse

    // Get User Details
    @GET("auth/user/{userId}")
    suspend fun getCurrentUser(
        @Path("userId") userId: Long,
        @Header("Authorization") token: String
    ): User

    // Update User Details
    @PUT("auth/user/{userId}")
    suspend fun updateProfile(
        @Path("userId") userId: Long,
        @Body update: ProfileUpdateRequest,
        @Header("Authorization") token: String
    ): User
}