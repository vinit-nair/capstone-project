package com.example.gopaywallet.data.model

data class ForgotPasswordResponse(
    val message: String,
    val success: Boolean,
    val otpExpiry: Long? = null  // timestamp when OTP will expire
) 