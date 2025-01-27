package com.example.gopaywallet.ui.transactions

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.gopaywallet.R
import com.example.gopaywallet.data.model.TransactionType
import com.example.gopaywallet.databinding.ActivityCreateTransactionBinding
import com.example.gopaywallet.di.NetworkModule
import com.example.gopaywallet.data.repository.TransactionRepository
import com.example.gopaywallet.utils.showToast
import com.example.gopaywallet.data.SessionManager
import com.example.gopaywallet.ui.auth.LoginActivity
import com.example.gopaywallet.ui.home.HomeActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateTransactionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateTransactionBinding
    private lateinit var sessionManager: SessionManager
    
    private val viewModel: CreateTransactionViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_transaction)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        
        sessionManager = SessionManager(this)

        setupObservers()
        setupClickListeners()
    }

    private fun setupObservers() {
        viewModel.transactionResult.observe(this) { result ->
            result.onSuccess { transaction ->
                showToast("Transaction successful")
                val intent = Intent().apply {
                    putExtra("REFRESH_TRANSACTIONS", true)
                }
                setResult(RESULT_OK, intent)
                finish()
            }.onFailure { exception ->
                showToast(exception.message ?: "Transaction failed")
            }
        }

        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.btnSend.isEnabled = !isLoading
        }
    }

    private fun setupClickListeners() {
        binding.btnSend.setOnClickListener {
            val userId = sessionManager.getUserId()
            if (userId != -1L) {
                if (validateInputs()) {
                    viewModel.createTransaction(userId, TransactionType.SEND)
                }
            } else {
                showToast("User session expired. Please login again")
                startActivity(Intent(this, LoginActivity::class.java))
                finishAffinity()
            }
        }
    }

    private fun validateInputs(): Boolean {
        val title = binding.etTitle.text.toString()
        val amount = binding.etAmount.text.toString()
        val recipientName = binding.etRecipientName.text.toString()
        val recipientPhone = binding.etRecipientPhone.text.toString()

        if (title.isBlank()) {
            binding.etTitle.error = "Title is required"
            return false
        }
        if (amount.isBlank()) {
            binding.etAmount.error = "Amount is required"
            return false
        }
        if (recipientName.isBlank()) {
            binding.etRecipientName.error = "Recipient name is required"
            return false
        }
        if (recipientPhone.isBlank()) {
            binding.etRecipientPhone.error = "Recipient phone is required"
            return false
        }
        return true
    }

    companion object {
        const val REQUEST_CODE = 100
    }
} 