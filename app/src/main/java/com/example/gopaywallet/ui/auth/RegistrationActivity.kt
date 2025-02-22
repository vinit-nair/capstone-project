package com.example.gopaywallet.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.example.gopaywallet.MainActivity
import com.example.gopaywallet.R
import com.example.gopaywallet.databinding.ActivityRegistrationBinding
import com.example.gopaywallet.di.NetworkModule
import com.example.gopaywallet.data.repository.UserRepositoryImpl
import com.example.gopaywallet.utils.showToast
import com.example.gopaywallet.data.SessionManager
import com.example.gopaywallet.ui.home.HomeActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegistrationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrationBinding
    private lateinit var sessionManager: SessionManager
    
    private val viewModel: RegistrationViewModel by viewModels()
    private val loginViewModel: LoginViewModel by viewModels()

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

        lifecycleScope.launch {
            viewModel.registrationState.collect { state ->
                when (state) {
                    is RegistrationState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.btnRegister.isEnabled = false
                    }
                    is RegistrationState.Success -> {
                        binding.progressBar.visibility = View.GONE
                        binding.btnRegister.isEnabled = true
                    }
                    is RegistrationState.Error -> {
                        binding.progressBar.visibility = View.GONE
                        binding.btnRegister.isEnabled = true
                        showToast(state.message)
                    }
                    is RegistrationState.Initial -> {
                        binding.progressBar.visibility = View.GONE
                        binding.btnRegister.isEnabled = true
                    }
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