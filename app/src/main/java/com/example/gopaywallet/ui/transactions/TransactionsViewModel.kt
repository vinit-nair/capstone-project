package com.example.gopaywallet.ui.transactions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gopaywallet.data.model.Transaction
import com.example.gopaywallet.data.repository.TransactionRepository
import com.example.gopaywallet.data.SessionManager
import kotlinx.coroutines.launch
import javax.inject.Inject
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel
class TransactionsViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _transactions = MutableLiveData<List<Transaction>>()
    val transactions: LiveData<List<Transaction>> = _transactions

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private var currentPage = 0
    private val pageSize = 20
    private var isLastPage = false

    init {
        loadTransactions(refresh = true)
    }

    fun loadTransactions(refresh: Boolean = false) {
        if (refresh) {
            currentPage = 0
            isLastPage = false
        }
        
        if (isLastPage) return

        viewModelScope.launch {
            try {
                _isLoading.value = true
                val userId = sessionManager.getUserId()
                println("Loading transactions for user: $userId")
                val newTransactions = transactionRepository.getUserTransactions(userId, currentPage, pageSize)
                println("Received ${newTransactions.size} transactions")
                
                if (newTransactions.isEmpty()) {
                    isLastPage = true
                }

                val currentList = if (refresh) emptyList() else _transactions.value.orEmpty()
                val updatedList = currentList + newTransactions
                println("Updated list size: ${updatedList.size}")
                _transactions.value = updatedList
                
                if (newTransactions.isNotEmpty()) {
                    currentPage++
                }
            } catch (e: Exception) {
                println("Error in ViewModel: ${e.message}")
                e.printStackTrace()
                _error.value = e.message ?: "Failed to load transactions"
                if (refresh) {
                    _transactions.value = emptyList()
                }
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun refreshTransactions() {
        loadTransactions(refresh = true)
    }
} 