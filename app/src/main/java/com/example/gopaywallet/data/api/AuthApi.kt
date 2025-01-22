package com.example.gopaywallet.data.api

import com.example.gopaywallet.data.model.LoginRequest
import com.example.gopaywallet.data.model.LoginResponse
import com.example.gopaywallet.data.model.RegisterRequest
import com.example.gopaywallet.data.model.RegisterResponse
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface AuthApi {
    @Headers("Content-Type: application/json")
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @Headers("Content-Type: application/json")
    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): RegisterResponse

    @Headers("Content-Type: application/json")
    @POST("auth/forgot-password")
    suspend fun forgotPassword(@Body email: String)
} 