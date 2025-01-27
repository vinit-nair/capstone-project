package com.example.gopaywallet.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.gopaywallet.R
import com.example.gopaywallet.databinding.ActivityVerifyOtpBinding
import com.example.gopaywallet.utils.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VerifyOtpActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVerifyOtpBinding
    private val viewModel: VerifyOtpViewModel by viewModels()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_verify_otp)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        // Get email from intent
        intent.getStringExtra("email")?.let {
            viewModel.email.value = it
        }

        setupClickListeners()
        setupObservers()
    }

    private fun setupClickListeners() {
        binding.btnVerifyOtp.setOnClickListener {
            viewModel.verifyOtp()
        }

        binding.btnResendOtp.setOnClickListener {
            viewModel.resendOtp()
        }
    }

    private fun setupObservers() {
        viewModel.verificationResult.observe(this) { result ->
            when (result) {
                is VerifyOtpResult.Success -> {
                    showToast("OTP verified successfully")
                    startActivity(Intent(this, ResetPasswordActivity::class.java).apply {
                        putExtra("email", viewModel.email.value)
                        putExtra("otp", viewModel.otp.value)
                    })
                    finish()
                }
                is VerifyOtpResult.Error -> {
                    showToast(result.message)
                }
                is VerifyOtpResult.OtpResent -> {
                    showToast(result.message)
                }
            }
        }
    }
} 