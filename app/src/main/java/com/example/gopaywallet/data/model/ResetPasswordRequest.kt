package com.example.gopaywallet.data.model

data class ResetPasswordRequest(
    val email: String,
    val otp: String,
    val newPassword: String
) 