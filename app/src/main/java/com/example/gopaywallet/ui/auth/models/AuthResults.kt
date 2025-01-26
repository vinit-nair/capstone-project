package com.example.gopaywallet.ui.auth.models

sealed class ForgotPasswordResult {
    data class Success(val message: String) : ForgotPasswordResult()
    data class Error(val message: String) : ForgotPasswordResult()
}

sealed class VerifyOtpResult {
    object Success : VerifyOtpResult()
    data class OtpResent(val message: String) : VerifyOtpResult()
    data class Error(val message: String) : VerifyOtpResult()
} 