package com.example.gopaywallet.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gopaywallet.data.SessionManager
import com.example.gopaywallet.data.model.Transaction
import com.example.gopaywallet.data.repository.TransactionRepository
import java.math.BigDecimal
import java.time.LocalDateTime
import kotlinx.coroutines.launch

class HomeViewModel(
    private val transactionRepository: TransactionRepository,
    private val sessionManager: SessionManager
) : ViewModel() {
    private val _balance = MutableLiveData<Double>()
    val balance: LiveData<Double> = _balance

    private val _transactions = MutableLiveData<List<Transaction>>()
    val transactions: LiveData<List<Transaction>> = _transactions

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        loadBalance()
        loadTransactions()
    }

    private fun loadBalance() {
        // TODO: Fetch actual balance from backend
        _balance.value = 1000.00
    }

    private fun loadTransactions() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val userId = sessionManager.getUserId()
                println("Loading transactions for user: $userId")
                val result = transactionRepository.getUserTransactions(userId, 0, 5)
                println("Loaded ${result.size} transactions")
                _transactions.value = result
            } catch (e: Exception) {
                println("Error loading transactions: ${e.message}")
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun refreshTransactions() {
        loadTransactions()
    }
} 