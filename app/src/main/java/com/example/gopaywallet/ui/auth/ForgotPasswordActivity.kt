package com.example.gopaywallet.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.gopaywallet.R
import com.example.gopaywallet.databinding.ActivityForgotPasswordBinding
import com.example.gopaywallet.ui.auth.models.ForgotPasswordResult
import com.example.gopaywallet.utils.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForgotPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityForgotPasswordBinding
    private val viewModel: ForgotPasswordViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_forgot_password)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        setupClickListeners()
        setupObservers()

        // Get email from intent if passed
        intent.getStringExtra("email")?.let {
            viewModel.email.value = it
        }
    }

    private fun setupClickListeners() {
        binding.btnResetPassword.setOnClickListener {
            viewModel.resetPassword()
        }
    }

    private fun setupObservers() {
        viewModel.resetPasswordResult.observe(this) { result ->
            when (result) {
                is ForgotPasswordResult.Success -> {
                    showToast(result.message)
                    startActivity(Intent(this, VerifyOtpActivity::class.java).apply {
                        putExtra("email", viewModel.email.value)
                    })
                    finish()
                }
                is ForgotPasswordResult.Error -> {
                    showToast(result.message)
                    binding.emailLayout.error = result.message
                }
            }
        }

        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.btnResetPassword.isEnabled = !isLoading
        }
    }
} 