package com.example.gopaywallet.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gopaywallet.data.repository.UserRepository
import com.example.gopaywallet.ui.auth.models.ForgotPasswordResult
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    val email = MutableLiveData<String>()

    private val _resetPasswordResult = MutableLiveData<ForgotPasswordResult>()
    val resetPasswordResult: LiveData<ForgotPasswordResult> = _resetPasswordResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun resetPassword() {
        val emailValue = email.value
        if (emailValue.isNullOrBlank()) {
            _resetPasswordResult.value = ForgotPasswordResult.Error("Email is required")
            return
        }

        if (!isValidEmail(emailValue)) {
            _resetPasswordResult.value = ForgotPasswordResult.Error("Please enter a valid email")
            return
        }

        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response = userRepository.forgotPassword(emailValue)
                _resetPasswordResult.value = ForgotPasswordResult.Success(
                    response.message ?: "OTP sent to your email"
                )
            } catch (e: Exception) {
                // Handle HTTP errors
                val errorMessage = when {
                    e.message?.contains("400") == true -> "Email not registered"
                    else -> e.message ?: "Failed to send reset email"
                }
                _resetPasswordResult.value = ForgotPasswordResult.Error(errorMessage)
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
} 