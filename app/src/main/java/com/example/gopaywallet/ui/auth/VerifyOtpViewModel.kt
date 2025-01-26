package com.example.gopaywallet.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gopaywallet.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class VerifyOtpViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    val email = MutableLiveData<String>()
    val otp = MutableLiveData<String>()

    private val _verificationResult = MutableLiveData<VerifyOtpResult>()
    val verificationResult: LiveData<VerifyOtpResult> = _verificationResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun verifyOtp() {
        val otpValue = otp.value
        if (otpValue.isNullOrBlank()) {
            _verificationResult.value = VerifyOtpResult.Error("Please enter OTP")
            return
        }

        viewModelScope.launch {
            try {
                _isLoading.value = true
                userRepository.verifyOtp(email.value ?: "", otpValue)
                _verificationResult.value = VerifyOtpResult.Success
            } catch (e: Exception) {
                _verificationResult.value = VerifyOtpResult.Error(e.message ?: "Failed to verify OTP")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun resendOtp() {
        val emailValue = email.value
        if (emailValue.isNullOrBlank()) {
            _verificationResult.value = VerifyOtpResult.Error("Email not found")
            return
        }

        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response = userRepository.forgotPassword(emailValue)
                _verificationResult.value = VerifyOtpResult.OtpResent(
                    response.message ?: "OTP has been resent"
                )
            } catch (e: Exception) {
                _verificationResult.value = VerifyOtpResult.Error(
                    e.message ?: "Failed to resend OTP"
                )
            } finally {
                _isLoading.value = false
            }
        }
    }
}

sealed class VerifyOtpResult {
    object Success : VerifyOtpResult()
    data class OtpResent(val message: String) : VerifyOtpResult()
    data class Error(val message: String) : VerifyOtpResult()
} 