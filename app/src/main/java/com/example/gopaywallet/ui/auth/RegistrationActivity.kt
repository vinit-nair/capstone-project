package com.example.gopaywallet.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.gopaywallet.MainActivity
import com.example.gopaywallet.R
import com.example.gopaywallet.databinding.ActivityRegistrationBinding
import com.example.gopaywallet.di.NetworkModule
import com.example.gopaywallet.data.repository.UserRepositoryImpl
import com.example.gopaywallet.utils.showToast
import com.example.gopaywallet.data.SessionManager
import com.example.gopaywallet.ui.home.HomeActivity

class RegistrationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrationBinding
    private lateinit var sessionManager: SessionManager
    
    private val viewModel: RegistrationViewModel by viewModels {
        RegistrationViewModelFactory(
            UserRepositoryImpl(NetworkModule.authApi)
        )
    }

    private val loginViewModel: LoginViewModel by viewModels {
        LoginViewModelFactory(
            UserRepositoryImpl(NetworkModule.authApi)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_registration)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        
        sessionManager = SessionManager(this)
        
        setupObservers()
        setupClickListeners()
    }

    private fun setupObservers() {
        viewModel.registrationResult.observe(this) { result ->
            when (result) {
                is RegistrationResult.Success -> {
                    // Auto-login after successful registration
                    val email = viewModel.email.value ?: ""
                    val password = viewModel.password.value ?: ""
                    loginViewModel.loginAfterRegistration(email, password)
                }
                is RegistrationResult.Error -> {
                    showToast(result.message)
                }
            }
        }

        loginViewModel.loginResult.observe(this) { result ->
            when (result) {
                is LoginResult.Success -> {
                    result.token?.let { token ->
                        sessionManager.saveAuthToken(token)
                    }
                    startActivity(Intent(this, HomeActivity::class.java))
                    finishAffinity()
                }
                is LoginResult.Error -> {
                    showToast(result.message)
                }
            }
        }

        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.btnRegister.isEnabled = !isLoading
        }
    }

    private fun setupClickListeners() {
        binding.btnRegister.setOnClickListener {
            viewModel.register()
        }

        binding.tvLogin.setOnClickListener {
            finish()
        }
    }
}