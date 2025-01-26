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
class ResetPasswordViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    val email = MutableLiveData<String>()
    val otp = MutableLiveData<String>()
    val newPassword = MutableLiveData<String>()
    val confirmPassword = MutableLiveData<String>()

    private val _resetPasswordResult = MutableLiveData<ResetPasswordResult>()
    val resetPasswordResult: LiveData<ResetPasswordResult> = _resetPasswordResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun resetPassword() {
        val newPass = newPassword.value
        val confirmPass = confirmPassword.value

        if (newPass.isNullOrBlank() || confirmPass.isNullOrBlank()) {
            _resetPasswordResult.value = ResetPasswordResult.Error("Please fill all fields")
            return
        }

        if (newPass != confirmPass) {
            _resetPasswordResult.value = ResetPasswordResult.Error("Passwords do not match")
            return
        }

        viewModelScope.launch {
            try {
                _isLoading.value = true
                userRepository.resetPassword(
                    email = email.value ?: "",
                    otp = otp.value ?: "",
                    newPassword = newPass
                )
                _resetPasswordResult.value = ResetPasswordResult.Success
            } catch (e: Exception) {
                _resetPasswordResult.value = ResetPasswordResult.Error(
                    e.message ?: "Failed to reset password"
                )
            } finally {
                _isLoading.value = false
            }
        }
    }
}

sealed class ResetPasswordResult {
    object Success : ResetPasswordResult()
    data class Error(val message: String) : ResetPasswordResult()
} 