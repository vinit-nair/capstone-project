package com.example.gopaywallet.ui.transactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gopaywallet.data.SessionManager
import com.example.gopaywallet.data.repository.TransactionRepository
import com.example.gopaywallet.di.NetworkModule

/*
class CreateTransactionViewModelFactory(
    private val sessionManager: SessionManager
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreateTransactionViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CreateTransactionViewModel(
                TransactionRepository(
                    NetworkModule.transactionApi,
                    sessionManager
                )
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
} */
