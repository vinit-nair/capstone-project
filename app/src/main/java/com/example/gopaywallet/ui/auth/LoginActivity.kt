package com.example.gopaywallet.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gopaywallet.MainActivity
import com.example.gopaywallet.R
import com.example.gopaywallet.databinding.ActivityLoginBinding
import com.example.gopaywallet.di.NetworkModule
import com.example.gopaywallet.data.SessionManager
import com.example.gopaywallet.data.repository.UserRepository
import com.example.gopaywallet.data.repository.UserRepositoryImpl
import com.example.gopaywallet.ui.home.HomeActivity
import com.example.gopaywallet.utils.showToast

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var sessionManager: SessionManager
    
    private val viewModel: LoginViewModel by viewModels {
        LoginViewModelFactory(
            UserRepositoryImpl(NetworkModule.authApi)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        
        sessionManager = SessionManager(this)
        
        setupObservers()
        setupClickListeners()
    }

    private fun setupObservers() {
        viewModel.loginResult.observe(this) { result ->
            when (result) {
                is LoginResult.Success -> {
                    result.token?.let { token ->
                        sessionManager.saveAuthToken(token)
                    }
                    sessionManager.saveUserId(result.userId)
                    startActivity(Intent(this, HomeActivity::class.java))
                    finishAffinity()
                }
                is LoginResult.Error -> {
                    Log.e("LoginActivity", "Login failed: ${result.message}")
                    showToast(result.message)
                }
            }
        }

        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.btnLogin.isEnabled = !isLoading
        }
    }

    private fun setupClickListeners() {
        binding.btnLogin.setOnClickListener {
            viewModel.login()
        }

        binding.tvForgotPassword.setOnClickListener {
            // Navigate to forgot password screen
            // TODO: Implement forgot password navigation
        }

        binding.tvCreateAccount.setOnClickListener {
            startActivity(Intent(this, RegistrationActivity::class.java))
        }
    }
}

// ViewModelFactory for LoginViewModel
class LoginViewModelFactory(
    private val userRepository: UserRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

