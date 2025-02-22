package com.example.gopaywallet.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.gopaywallet.R
import com.example.gopaywallet.databinding.ActivityResetPasswordBinding
import com.example.gopaywallet.utils.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResetPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResetPasswordBinding
    private val viewModel: ResetPasswordViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_reset_password)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        // Get email and OTP from intent
        intent.getStringExtra("email")?.let { 
            viewModel.email.value = it
            Log.d("ResetPasswordActivity", "Email received: $it")
        }
        intent.getStringExtra("otp")?.let { 
            viewModel.otp.value = it
            Log.d("ResetPasswordActivity", "OTP received: $it")
        }

        setupClickListeners()
        setupObservers()
    }

    private fun setupClickListeners() {
        binding.btnResetPassword.setOnClickListener {
            Log.d("ResetPasswordActivity", "Reset button clicked")
            binding.passwordLayout.error = null
            binding.confirmPasswordLayout.error = null
            viewModel.resetPassword()
        }
    }

    private fun setupObservers() {
        viewModel.resetPasswordResult.observe(this) { result ->
            when (result) {
                is ResetPasswordResult.Success -> {
                    showToast("Password reset successful")
                    startActivity(Intent(this, LoginActivity::class.java))
                    finishAffinity()
                }
                is ResetPasswordResult.Error -> {
                    when {
                        result.message.contains("match") -> {
                            binding.confirmPasswordLayout.error = result.message
                        }
                        result.message.contains("Password must be") -> {
                            binding.passwordLayout.error = result.message
                        }
                        else -> showToast(result.message)
                    }
                }
            }
        }

        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.btnResetPassword.isEnabled = !isLoading
        }
    }
} 