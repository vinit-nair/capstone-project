package com.example.gopaywallet.ui.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gopaywallet.data.SessionManager
import com.example.gopaywallet.data.model.ProfileUpdateRequest
import com.example.gopaywallet.data.model.User
import com.example.gopaywallet.data.repository.UserRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepositoryImpl: UserRepositoryImpl,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _user = MutableLiveData<User?>()
    val user: MutableLiveData<User?> = _user

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: MutableLiveData<String?> = _error

    private val _success = MutableLiveData<String?>()
    val success: MutableLiveData<String?> = _success

    // Load initial user details
    fun loadUserDetails() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val userDetails = userRepositoryImpl.getCurrentUser()
                _user.value = userDetails
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to load user details"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Save the updated user details
    fun saveUserDetails(updatedUser: User) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                Log.d("ProfileViewModel", "Saving user details: $updatedUser")

                // Create a ProfileUpdateRequest object to send to the API
                val profileUpdateRequest = ProfileUpdateRequest(
                    fullName = updatedUser.fullName ?: "",
                    email = updatedUser.email ?: "",
                    phoneNumber = updatedUser.phoneNumber ?: ""
                )

                userRepositoryImpl.updateUserDetails(profileUpdateRequest)
                // Update the UI using the same request data
                _user.value = updatedUser
                _success.value = "Updated Details"
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "Error updating user details", e)
                _error.value = e.message ?: "Unable to update details"
            } finally {
                _isLoading.value = false
            }
        }
    }
    fun clearSuccess() {
        _success.value = null
    }

    fun clearError() {
        _error.value = null
    }
}
