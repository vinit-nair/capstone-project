package com.example.gopaywallet.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gopaywallet.data.model.User
import com.example.gopaywallet.data.repository.UserRepository
import kotlinx.coroutines.launch

class RegistrationViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _registrationResult = MutableLiveData<RegistrationResult>()
    val registrationResult: LiveData<RegistrationResult> = _registrationResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    val fullName = MutableLiveData<String>()
    val email = MutableLiveData<String>()
    val phoneNumber = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    val confirmPassword = MutableLiveData<String>()

    fun register() {
        val fullNameValue = fullName.value
        val emailValue = email.value
        val phoneNumberValue = phoneNumber.value
        val passwordValue = password.value
        val confirmPasswordValue = confirmPassword.value

        if (fullNameValue.isNullOrBlank() || emailValue.isNullOrBlank() || 
            phoneNumberValue.isNullOrBlank() || passwordValue.isNullOrBlank()) {
            _registrationResult.value = RegistrationResult.Error("All fields are required")
            return
        }

        if (passwordValue != confirmPasswordValue) {
            _registrationResult.value = RegistrationResult.Error("Passwords do not match")
            return
        }

        _isLoading.value = true
        viewModelScope.launch {
            try {
                val user = userRepository.register(
                    fullNameValue,
                    emailValue,
                    phoneNumberValue,
                    passwordValue
                )
                _registrationResult.value = RegistrationResult.Success(user)
            } catch (e: Exception) {
                _registrationResult.value = RegistrationResult.Error(e.message ?: "Registration failed")
            } finally {
                _isLoading.value = false
            }
        }
    }
}

sealed class RegistrationResult {
    data class Success(val user: User) : RegistrationResult()
    data class Error(val message: String) : RegistrationResult()
} 