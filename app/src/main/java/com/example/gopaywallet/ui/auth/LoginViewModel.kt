package com.example.gopaywallet.ui.auth

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gopaywallet.data.model.User
import com.example.gopaywallet.data.repository.UserRepository

import kotlinx.coroutines.launch

class LoginViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()

    fun login() {
        val emailValue = email.value
        val passwordValue = password.value

        if (emailValue.isNullOrBlank() || passwordValue.isNullOrBlank()) {
            _loginResult.value = LoginResult.Error("Email and password are required")
            return
        }

        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response = userRepository.login(emailValue, passwordValue)
                _loginResult.value = LoginResult.Success(
                    token = response.token,
                    userId = response.user.id
                )
            } catch (e: Exception) {
                Log.e("LoginViewModel", "Login failed", e)
                _loginResult.value = LoginResult.Error(e.message ?: "Login failed")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loginAfterRegistration(email: String, password: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                Log.d("LoginViewModel", "Auto-login after registration for email: $email")
                val response = userRepository.login(email, password)
                Log.d("LoginViewModel", "Auto-login successful")
                _loginResult.value = LoginResult.Success(response.token, response.user.id)
            } catch (e: Exception) {
                Log.e("LoginViewModel", "Auto-login failed", e)
                _loginResult.value = LoginResult.Error(e.message ?: "Auto-login failed")
            } finally {
                _isLoading.value = false
            }
        }
    }
}

sealed class LoginResult {
    data class Success(val token: String?, val userId: Long) : LoginResult()
    data class Error(val message: String) : LoginResult()
}

